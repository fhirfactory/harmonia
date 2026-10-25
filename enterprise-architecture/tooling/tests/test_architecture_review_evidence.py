# Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
"""Iteration boundaries and attributable-event filename correspondence."""
import importlib.util
import hashlib
import json
from pathlib import Path
import sys
import tempfile
import unittest

SCRIPTS = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(SCRIPTS))
spec = importlib.util.spec_from_file_location("architecture_review_evidence", SCRIPTS / "architecture_review_evidence.py")
evidence = importlib.util.module_from_spec(spec)
spec.loader.exec_module(evidence)


class ArchitectureReviewEvidenceTests(unittest.TestCase):
    def setUp(self):
        temp = tempfile.TemporaryDirectory()
        self.addCleanup(temp.cleanup)
        self.root = Path(temp.name)
        self.design = self.root / "enterprise-architecture/design"
        hcrl = self.design / "review/9.8.7/hcrl-9.8.7-004.pending.md"
        hcrl.parent.mkdir(parents=True)
        hcrl.write_text("Pending semantic record\n")
        self.release = self.design / "evidence/9.8.7"
        self.boundary = self.release / "004"

    def record(self, role, name, event):
        side, label = evidence.ROLES[role]
        path = self.boundary / side / role / name
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(f"**{label}: {event}.**\nAttributable source: fixture, not a real historical event.\n")
        return path

    def pair(self):
        task = self.record("ai-agent-tasks", "20261021T064322Z-review.md", "2026-10-21T17:43:22.583+11:00")
        report = self.record("ai-agent-reports", "20261021T070000Z-review.md", "2026-10-21T07:00:00.123456Z")
        return task, report

    def check(self):
        return evidence.check_iteration(self.root, "9.8.7", "004")

    def test_offset_fraction_and_multiple_tasks_reports_use_one_model_without_mutation(self):
        self.pair()
        self.record("ai-agent-tasks", "20261021T064400Z-application.md", "2026-10-21T06:44:00Z")
        before = {p: p.read_bytes() for p in self.root.rglob("*") if p.is_file()}
        result = self.check()
        self.assertEqual("PASS", result["outcome"])
        self.assertEqual(3, len(result["records"]))
        self.assertEqual(before, {p: p.read_bytes() for p in self.root.rglob("*") if p.is_file()})

    def test_release_level_before_after_are_rejected(self):
        self.pair()
        (self.release / "before").mkdir()
        with self.assertRaisesRegex(ValueError, "requires an HCRL iteration"):
            self.check()

    def test_wrong_role_or_intermediate_provenance_taxonomy_is_rejected(self):
        task, _ = self.pair()
        wrong = self.boundary / "after/review/ai-agent-tasks"
        wrong.mkdir(parents=True)
        task.rename(wrong / task.name)
        self.assertEqual("FAIL", self.check()["outcome"])

    def test_output_at_iteration_root_is_rejected(self):
        self.pair()
        (self.boundary / "mapping.csv").write_text("durable output\n")
        self.assertEqual("FAIL", self.check()["outcome"])

    def test_supporting_evidence_needs_no_universal_metadata_or_archive(self):
        self.pair()
        (self.boundary / "after/mapping.csv").write_text("source,destination\na,b\n")
        self.assertEqual("PASS", self.check()["outcome"])

    def test_recorded_time_and_filename_must_correspond(self):
        task, _ = self.pair()
        task.rename(task.with_name("20261021T064323Z-review.md"))
        self.assertEqual("FAIL", self.check()["outcome"])

    def test_missing_or_invalid_event_or_offset_is_rejected(self):
        task, _ = self.pair()
        for body in ("No event field", "Task-Issued-At: 2026-10-21T06:43:22",
                     "Task-Issued-At: 2026-02-31T06:43:22Z"):
            with self.subTest(body=body):
                task.write_text(body)
                self.assertEqual("FAIL", self.check()["outcome"])

    def test_unavailable_original_time_remains_unresolved_without_fabricated_prefix(self):
        _, report = self.pair()
        report.write_text("Report-Produced-At: UNRESOLVED\nKnown original source unavailable.\n")
        report.rename(report.with_name("UNRESOLVED-review.md"))
        self.assertEqual("UNRESOLVED", self.check()["outcome"])

    def test_task_without_report_is_unresolved_not_a_completion_claim(self):
        _, report = self.pair()
        report.unlink()
        self.assertEqual("UNRESOLVED", self.check()["outcome"])

    def test_other_iterations_and_historical_layout_are_not_rewritten_or_audited(self):
        self.pair()
        old = self.design / "evidence/1.1.3/before/original.txt"
        old.parent.mkdir(parents=True)
        old.write_text("historical layout\n")
        prior = self.release / "003/after/ai-agent-reports/original.md"
        prior.parent.mkdir(parents=True)
        prior.write_text("historical original, no manufactured time\n")
        self.assertEqual("PASS", self.check()["outcome"])
        self.assertEqual("historical original, no manufactured time\n", prior.read_text())

    def test_no_unnecessary_empty_evidence_package_is_required(self):
        self.assertEqual("PASS", self.check()["outcome"])
        self.assertFalse(self.boundary.exists())

    def test_selected_hcrl_version_and_iteration_must_be_identifiable(self):
        with self.assertRaisesRegex(ValueError, "No corresponding HCRL"):
            evidence.check_iteration(self.root, "9.8.7", "005")
        with self.assertRaises(ValueError):
            evidence.check_iteration(self.root, "9.8.7/other", "004")
        with self.assertRaises(ValueError):
            evidence.check_iteration(self.root, "9.8.7", "../004")

    def test_emission_preserves_supplied_task_event_and_captures_report_write_event(self):
        task = evidence.emit_record(self.root, '9.8.7', '004', 'ai-agent-tasks', 'explicit-task',
            'Operative instruction', event='2026-10-21T17:43:22.583+11:00', source='Synthetic instruction event')
        self.assertEqual(task.name, '20261021T064322Z-explicit-task.md')
        report = evidence.emit_record(self.root, '9.8.7', '004', 'ai-agent-reports', 'explicit-report', 'Outcome facts')
        self.assertIsNone(evidence.check_event(report, 'Report-Produced-At'))
        self.assertIn('completed initial Report emission', report.read_text())
        unresolved = evidence.emit_record(self.root, '9.8.7', '004', 'ai-agent-tasks', 'unknown-event', 'Instruction')
        self.assertEqual(unresolved.name, 'unresolved-unknown-event.md')
        self.assertIsNotNone(evidence.check_event(unresolved, 'Task-Issued-At'))
        with self.assertRaises(FileExistsError):
            evidence.emit_record(self.root, '9.8.7', '004', 'ai-agent-tasks', 'explicit-task',
                'Changed instruction', event='2026-10-21T17:43:22.583+11:00', source='Synthetic instruction event')

    def preparation_fixture(self):
        task, _ = self.pair()
        (self.design / 'review/9.8.7/hcrl-9.8.7-004.pending.md').unlink()
        source = self.root / 'input.xml'; source.write_bytes(b'Pinned fixture input')
        item = dict(path=str(source), bytes=source.stat().st_size, sha256=hashlib.sha256(source.read_bytes()).hexdigest())
        group = dict(added=[], removed=[], changed=[])
        delta = dict(schemaVersion=2, inputs=dict(baseline=item, submitted=item),
                     exceptionalConditions=[], elements=group, relationships=group, views=group,
                     viewNavigation=dict(changedViews=[]))
        path = self.boundary / 'after/architectural-delta.json'
        path.write_text(json.dumps(delta))
        return task, path, source

    def test_preparation_links_layout_event_and_human_checkpoint_without_overwrite(self):
        task, delta, _ = self.preparation_fixture()
        result = evidence.prepare_review(self.root, '9.8.7', '004', delta, task)
        self.assertEqual(result['outcome'], 'PASS')
        self.assertEqual(result['preparation'], 'TARGETED_INTERPRETATION_REQUIRED')
        for key in ('review', 'hcrl', 'report'):
            self.assertEqual(evidence.check_links(Path(result[key])), [])
        self.assertIn('On-Hold', Path(result['review']).read_text())
        self.assertIn('Minutiae-Accepted: [awaiting', Path(result['hcrl']).read_text())
        before = Path(result['hcrl']).read_bytes()
        with self.assertRaisesRegex(ValueError, 'no automatic overwrite'):
            evidence.prepare_review(self.root, '9.8.7', '004', delta, task)
        self.assertEqual(Path(result['hcrl']).read_bytes(), before)

    def test_preparation_rejects_changed_input_and_wrong_evidence_boundary(self):
        task, delta, source = self.preparation_fixture()
        with self.assertRaisesRegex(ValueError, 'selected iteration'):
            evidence.prepare_review(self.root, '9.8.7', '005', delta, task)
        source.write_bytes(b'Tampered')
        with self.assertRaisesRegex(ValueError, 'Pinned input changed'):
            evidence.prepare_review(self.root, '9.8.7', '004', delta, task)
        self.assertFalse((self.design / 'review/9.8.7/change-review.md').exists())

    def test_preparation_preserves_exception_and_unresolved_timestamp(self):
        task, delta, _ = self.preparation_fixture()
        task.write_text('Task-Issued-At: UNRESOLVED\nOriginal event unavailable\n')
        task = task.rename(task.with_name('unresolved-task.md'))
        data = json.loads(delta.read_text()); data['exceptionalConditions'] = [dict(kind='IDENTITY', detail='Ambiguous')]
        delta.write_text(json.dumps(data))
        result = evidence.prepare_review(self.root, '9.8.7', '004', delta, task)
        self.assertEqual(result['outcome'], 'UNRESOLVED')
        self.assertTrue(any('IDENTITY' in x for x in result['validation']['unresolved']))
