#!/usr/bin/env python3
# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Verify exact original source editions and bounded retirement preservation."""
import argparse
from pathlib import Path
import subprocess
import architecture_continuum as c


def verify(root=c.ROOT):
    after=root/'enterprise-architecture/design/evidence/1.1.5/002/after'
    records=c.read_json(after/'source-succession.json')
    for row in records:
        raw=subprocess.run(['git','show',row['entryRevision']+':'+row['source']],cwd=root,capture_output=True,check=True).stdout
        c.require(c.sha(raw)==row['entrySha256'],'Original source edition drift: '+row['source'])
        original=raw.decode();current=(root/row['source']).read_text()
        for span in row['retiredSections']:
            text=original[span['startCharacter']:span['endCharacter']] if 'startCharacter' in span else original
            c.require(c.sha(text.encode())==span['originalSha256'],'Retirement correspondence drift: '+row['source'])
            c.require(text in current,'Retired original text lost: '+row['source'])
    return {'status':'PASS','retiredSourceRanges':sum(len(r['retiredSections']) for r in records),'sourceFiles':len(records),'originalRangeBytesPreserved':True,'characterOffsets':'Unicode code-point offsets in the exact UTF-8 entry source; line numbers are navigation only.'}

if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--report',type=Path,required=True);a=p.parse_args();r=verify();a.report.write_bytes(c.encoded(r));print(r['status'])
