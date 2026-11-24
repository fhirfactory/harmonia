#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Read the governed RAD release or explicitly prepare its next patch increment."""
import argparse
import json
import os
from pathlib import Path
import re
import sys
import tempfile

DEFAULT_RELEASE = Path(__file__).resolve().parents[1] / "architecture-release.json"
VERSION = re.compile(r"(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)\Z")


def version_parts(value):
    if not isinstance(value, str) or not VERSION.fullmatch(value):
        raise ValueError(f"Invalid architecture release: {value!r}")
    return tuple(map(int, value.split(".")))


def load_release(path=DEFAULT_RELEASE):
    def unique_pairs(items):
        result = {}
        for key, value in items:
            if key in result:
                raise ValueError(f"Duplicate release field: {key}")
            result[key] = value
        return result

    data = json.loads(Path(path).read_text(), object_pairs_hook=unique_pairs)
    if not isinstance(data, dict) or set(data) != {"version", "previous_version"}:
        raise ValueError("Release authority requires only version and previous_version")
    current = version_parts(data["version"])
    previous = version_parts(data["previous_version"])
    if previous >= current:
        raise ValueError("previous_version must precede version")
    return data


def bump_patch(path, expected):
    """An explicit acceptance operation; publication never calls this function."""
    path = Path(path)
    data = load_release(path)
    if data["version"] != expected:
        raise ValueError(f"Expected {expected}, found {data['version']}; no release changed")
    major, minor, patch = version_parts(expected)
    result = {"version": f"{major}.{minor}.{patch + 1}", "previous_version": expected}
    # Replace a complete file so readers cannot observe partially written JSON.
    temporary = None
    try:
        with tempfile.NamedTemporaryFile(mode="w", dir=path.parent, delete=False) as stream:
            temporary = Path(stream.name)
            stream.write(json.dumps(result, indent=2) + "\n")
        temporary.chmod(path.stat().st_mode & 0o777)
        os.replace(temporary, path)
    finally:
        if temporary is not None:
            temporary.unlink(missing_ok=True)
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--file", type=Path, default=DEFAULT_RELEASE)
    actions = parser.add_subparsers(dest="action", required=True)
    actions.add_parser("show", help="Print the current release without changing it")
    actions.add_parser("check", help="Validate the release authority without changing it")
    bump = actions.add_parser("bump", help="Prepare one accepted change set's patch increment")
    bump.add_argument("--expect", required=True, help="Current release before this change set")
    args = parser.parse_args()
    try:
        data = bump_patch(args.file, args.expect) if args.action == "bump" else load_release(args.file)
        print(data["version"] if args.action == "show" else json.dumps(data, indent=2))
        return 0
    except (OSError, ValueError) as exc:
        print(f"FAIL: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
