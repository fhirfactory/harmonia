#!/usr/bin/env python3
"""
Test runner with progress tracking, timestamping, and stall detection.
Provides structured visibility into module and test execution to ensure efficient resource utilization.
"""

import sys
import time
import subprocess
import re
import threading
from datetime import datetime

ansi_escape = re.compile(r'\x1B(?:[@-Z\\-_]|\[[0-?]*[ -/]*[@-~])')

def format_duration(seconds):
    mins, secs = divmod(int(seconds), 60)
    return f"{mins}m {secs:02d}s" if mins > 0 else f"{secs}s"

def main():
    cmd = ["mvn"] + sys.argv[1:] if len(sys.argv) > 1 else ["mvn", "test"]
    log_file_path = "test-execution.log"

    print(f"[{datetime.now().strftime('%Y-%m-%d %H:%M:%S')}] Starting test runner with command: {' '.join(cmd)}")
    start_time = time.time()
    last_output_time = [time.time()]
    current_module = ["INIT"]
    current_test = [""]
    modules = []
    failures = []
    total_tests = [0]
    total_failures = [0]
    total_errors = [0]
    total_skipped = [0]
    stop_watchdog = threading.Event()

    def watchdog():
        while not stop_watchdog.wait(30):
            silence = time.time() - last_output_time[0]
            if silence >= 60:
                elapsed = format_duration(time.time() - start_time)
                print(f"[{datetime.now().strftime('%H:%M:%S')}] [{elapsed}] [STALL MONITOR] No output for {int(silence)}s. Active module: {current_module[0]} | Active test: {current_test[0]}", flush=True)

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
                    failures.append(f"{current_module[0]} -> {current_test[0]}: Fails={fails}, Errs={errs}")
                    print(f"[{datetime.now().strftime('%H:%M:%S')}]   RESULT FAILED: {clean_line}", flush=True)
                continue

            if "[ERROR] Failures:" in clean_line or "[ERROR] Errors:" in clean_line:
                print(f"[{datetime.now().strftime('%H:%M:%S')}] {clean_line}", flush=True)
            elif "BUILD SUCCESS" in clean_line:
                print(f"[{datetime.now().strftime('%H:%M:%S')}] >>> {clean_line}", flush=True)
            elif "BUILD FAILURE" in clean_line:
                print(f"[{datetime.now().strftime('%H:%M:%S')}] >>> {clean_line}", flush=True)

        ret_code = proc.wait()
        stop_watchdog.set()

    total_time = format_duration(time.time() - start_time)
    print("\n" + "=" * 80)
    print("TEST EXECUTION REPORT")
    print("=" * 80)
    print(f"Command:              {' '.join(cmd)}")
    print(f"Exit Code:            {ret_code}")
    print(f"Total Elapsed Time:   {total_time}")
    print(f"Modules Processed:    {len(modules)}")
    print(f"Total Tests Run:      {total_tests[0]}")
    print(f"Total Test Failures:  {total_failures[0]}")
    print(f"Total Test Errors:    {total_errors[0]}")
    print(f"Total Test Skipped:   {total_skipped[0]}")
    print(f"Last Active Module:   {current_module[0]}")
    print(f"Last Active Test:     {current_test[0]}")
    if failures:
        print(f"Failed Tests ({len(failures)}):")
        for f in failures:
            print(f"  - {f}")
    else:
        print("Failures:             0")
    print(f"Full log written to:  {log_file_path}")
    print("=" * 80)

    sys.exit(ret_code)

if __name__ == "__main__":
    main()
