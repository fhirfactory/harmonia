#!/usr/bin/env python3
"""
Harmonia Test Runner with progress tracking, timestamping, stall detection,
per-test/process timeouts, parallel execution support, and failure diagnostics.
Provides structured visibility into module and test execution to ensure efficient resource utilization.
"""

import sys
import os
import time
import subprocess
import re
import threading
from pathlib import Path
import xml.etree.ElementTree as ET
from datetime import datetime

ansi_escape = re.compile(r'\x1B(?:[@-Z\\-_]|\[[0-?]*[ -/]*[@-~])')

def format_duration(seconds):
    mins, secs = divmod(int(seconds), 60)
    return f"{mins}m {secs:02d}s" if mins > 0 else f"{secs}s"

def collect_surefire_failures(start_time, search_root="."):
    failures = []
    seen = set()
    for p in Path(search_root).glob("**/target/surefire-reports/TEST-*.xml"):
        try:
            mtime = p.stat().st_mtime
            if mtime >= start_time - 5:
                tree = ET.parse(p)
                root = tree.getroot()
                for tc in root.findall(".//testcase"):
                    classname = tc.get("classname", "")
                    testname = tc.get("name", "")
                    full_name = f"{classname}.{testname}"
                    for fail in tc.findall("failure"):
                        msg = fail.get("message")
                        if not msg:
                            text = (fail.text or "").strip()
                            msg = text.split("\n")[0] if text else "Assertion failed"
                        key = (full_name, "FAILURE", msg.strip())
                        if key not in seen:
                            seen.add(key)
                            failures.append({
                                "test": full_name,
                                "type": "FAILURE",
                                "message": msg.strip()
                            })
                    for err in tc.findall("error"):
                        msg = err.get("message")
                        if not msg:
                            text = (err.text or "").strip()
                            msg = text.split("\n")[0] if text else "Error"
                        key = (full_name, "ERROR", msg.strip())
                        if key not in seen:
                            seen.add(key)
                            failures.append({
                                "test": full_name,
                                "type": "ERROR",
                                "message": msg.strip()
                            })
        except Exception:
            pass
    return failures

def parse_cli_args(args):
    process_timeout = 300
    stall_timeout = 60
    per_test_timeout = 120
    enable_parallel = False
    
    mvn_args = []
    i = 0
    while i < len(args):
        arg = args[i]
        if arg == "--process-timeout" and i + 1 < len(args):
            process_timeout = int(args[i+1])
            i += 2
        elif arg.startswith("--process-timeout="):
            process_timeout = int(arg.split("=", 1)[1])
            i += 1
        elif arg == "--stall-timeout" and i + 1 < len(args):
            stall_timeout = int(args[i+1])
            i += 2
        elif arg.startswith("--stall-timeout="):
            stall_timeout = int(arg.split("=", 1)[1])
            i += 1
        elif arg == "--per-test-timeout" and i + 1 < len(args):
            per_test_timeout = int(args[i+1])
            i += 2
        elif arg.startswith("--per-test-timeout="):
            per_test_timeout = int(arg.split("=", 1)[1])
            i += 1
        elif arg == "--parallel":
            enable_parallel = True
            i += 1
        else:
            mvn_args.append(arg)
            i += 1

    if not mvn_args:
        mvn_args = ["test"]

    if mvn_args[0] == "mvn":
        cmd = list(mvn_args)
    else:
        cmd = ["mvn"] + mvn_args

    if enable_parallel and not any(a.startswith("-T") for a in cmd):
        cmd.extend(["-T", "1C"])

    if per_test_timeout > 0 and not any("forkedProcessTimeoutInSeconds" in a for a in cmd):
        cmd.append(f"-Dsurefire.forkedProcessTimeoutInSeconds={per_test_timeout}")

    return cmd, process_timeout, stall_timeout, per_test_timeout

def main():
    cmd, process_timeout, stall_timeout, per_test_timeout = parse_cli_args(sys.argv[1:])
    log_file_path = "test-execution.log"
    summary_file_path = "test-summary.txt"

    print(f"[{datetime.now().strftime('%Y-%m-%d %H:%M:%S')}] Starting test runner with command: {' '.join(cmd)}")
    print(f"Configuration: process_timeout={process_timeout}s, stall_timeout={stall_timeout}s, per_test_timeout={per_test_timeout}s")
    start_time = time.time()
    last_output_time = [time.time()]
    current_module = ["INIT"]
    current_test = [""]
    modules = []
    failures_summary = []
    console_failures = []
    in_failure_section = False
    in_error_section = False
    timed_out = [False]

    total_tests = [0]
    total_failures = [0]
    total_errors = [0]
    total_skipped = [0]
    stop_watchdog = threading.Event()

    proc_holder = []

    def watchdog():
        while not stop_watchdog.wait(5):
            now = time.time()
            total_elapsed = now - start_time
            silence = now - last_output_time[0]
            if silence >= stall_timeout:
                elapsed = format_duration(total_elapsed)
                print(f"[{datetime.now().strftime('%H:%M:%S')}] [{elapsed}] [STALL MONITOR] No output for {int(silence)}s. Active module: {current_module[0]} | Active test: {current_test[0]}", flush=True)
            if process_timeout > 0 and total_elapsed >= process_timeout:
                print(f"[{datetime.now().strftime('%H:%M:%S')}] [TIMEOUT MONITOR] Process timeout of {process_timeout}s exceeded! Terminating Maven process...", flush=True)
                timed_out[0] = True
                if proc_holder:
                    try:
                        proc_holder[0].terminate()
                        time.sleep(2)
                        if proc_holder[0].poll() is None:
                            proc_holder[0].kill()
                    except Exception:
                        pass
                break

    watchdog_thread = threading.Thread(target=watchdog, daemon=True)
    watchdog_thread.start()

    module_pattern = re.compile(r"\[INFO\] Building (Harmonia :: .+|net\.fhirfactory\.harmonia:.+) \[(\d+)/(\d+)\]")
    test_run_pattern = re.compile(r"\[INFO\] Running (.+)")
    test_result_pattern = re.compile(r"\[INFO\] Tests run: (\d+), Failures: (\d+), Errors: (\d+), Skipped: (\d+)")

    with open(log_file_path, "w", encoding="utf-8") as log_file:
        proc = subprocess.Popen(
            cmd,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True,
            bufsize=1
        )
        proc_holder.append(proc)

        for line in proc.stdout:
            last_output_time[0] = time.time()
            log_file.write(line)
            log_file.flush()

            clean_line = ansi_escape.sub('', line).rstrip()

            mod_match = module_pattern.search(clean_line)
            if mod_match:
                current_module[0] = mod_match.group(1)
                current_test[0] = ""
                idx = mod_match.group(2)
                total = mod_match.group(3)
                modules.append(current_module[0])
                elapsed = format_duration(time.time() - start_time)
                print(f"[{datetime.now().strftime('%H:%M:%S')}] [{elapsed}] [{idx}/{total}] >>> MODULE: {current_module[0]}", flush=True)
                continue

            test_match = test_run_pattern.search(clean_line)
            if test_match:
                current_test[0] = test_match.group(1)
                elapsed = format_duration(time.time() - start_time)
                print(f"[{datetime.now().strftime('%H:%M:%S')}] [{elapsed}]   TEST: {current_test[0]}", flush=True)
                continue

            res_match = test_result_pattern.search(clean_line)
            if res_match:
                runs, fails, errs, skips = (int(x) for x in res_match.groups())
                total_tests[0] += runs
                total_failures[0] += fails
                total_errors[0] += errs
                total_skipped[0] += skips
                if fails > 0 or errs > 0:
                    failures_summary.append(f"{current_module[0]} -> {current_test[0]}: Fails={fails}, Errs={errs}")
                    print(f"[{datetime.now().strftime('%H:%M:%S')}]   RESULT FAILED: {clean_line}", flush=True)
                continue

            if clean_line.startswith("[ERROR] Failures:"):
                in_failure_section = True
                in_error_section = False
                print(f"[{datetime.now().strftime('%H:%M:%S')}] {clean_line}", flush=True)
            elif clean_line.startswith("[ERROR] Errors:"):
                in_error_section = True
                in_failure_section = False
                print(f"[{datetime.now().strftime('%H:%M:%S')}] {clean_line}", flush=True)
            elif clean_line.startswith("[INFO]") or "BUILD FAILURE" in clean_line or "BUILD SUCCESS" in clean_line:
                in_failure_section = False
                in_error_section = False
                if "BUILD SUCCESS" in clean_line or "BUILD FAILURE" in clean_line:
                    print(f"[{datetime.now().strftime('%H:%M:%S')}] >>> {clean_line}", flush=True)
            elif (in_failure_section or in_error_section) and clean_line.startswith("[ERROR]   "):
                err_detail = clean_line[len("[ERROR]   "):].strip()
                if err_detail:
                    console_failures.append(err_detail)
                print(f"[{datetime.now().strftime('%H:%M:%S')}] {clean_line}", flush=True)

        ret_code = proc.wait()
        stop_watchdog.set()

    if timed_out[0]:
        ret_code = 124

    total_time = format_duration(time.time() - start_time)
    detailed_failures = collect_surefire_failures(start_time)

    lines = []
    lines.append("\n" + "=" * 80)
    lines.append("TEST EXECUTION REPORT")
    lines.append("=" * 80)
    lines.append(f"Command:              {' '.join(cmd)}")
    lines.append(f"Exit Code:            {ret_code}")
    lines.append(f"Total Elapsed Time:   {total_time}")
    lines.append(f"Modules Processed:    {len(modules)}")
    lines.append(f"Total Tests Run:      {total_tests[0]}")
    lines.append(f"Total Test Failures:  {total_failures[0]}")
    lines.append(f"Total Test Errors:    {total_errors[0]}")
    lines.append(f"Total Test Skipped:   {total_skipped[0]}")
    lines.append(f"Last Active Module:   {current_module[0]}")
    lines.append(f"Last Active Test:     {current_test[0]}")

    if detailed_failures:
        lines.append(f"Failed Tests ({len(detailed_failures)}):")
        for f in detailed_failures:
            lines.append(f"  - [{f['type']}] {f['test']}: {f['message']}")
    elif console_failures:
        lines.append(f"Failed Tests ({len(console_failures)}):")
        for cf in console_failures:
            lines.append(f"  - {cf}")
    elif failures_summary:
        lines.append(f"Failed Tests ({len(failures_summary)}):")
        for fs in failures_summary:
            lines.append(f"  - {fs}")
    else:
        lines.append("Failures:             0")

    lines.append(f"Full log written to:  {log_file_path}")
    lines.append("=" * 80)

    report_text = "\n".join(lines)
    print(report_text)

    with open(summary_file_path, "w", encoding="utf-8") as sf:
        sf.write(report_text + "\n")

    with open(log_file_path, "a", encoding="utf-8") as lf:
        lf.write(report_text + "\n")

    sys.exit(ret_code)

if __name__ == "__main__":
    main()
