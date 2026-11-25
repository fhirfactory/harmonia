/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.TreeSet;
import org.junit.jupiter.api.Test;

/** Behavioural coverage for Topic identity, ordering, hierarchy, and canonical forms. */
class TopicTest {

    @Test
    void identityIgnoresVersionsAndPreservesOrderedQualifiedPath() {
        Topic first = Topic.builder()
                .addEntry("System", "Harmonia", "1")
                .addEntry(null, "Petasos.Receiver", "2.4")
                .build();
        Topic sameIdentity = Topic.builder()
                .addEntry("System", "Harmonia", "9")
                .addEntry("N1", "Petasos.Receiver")
                .build();

        assertEquals(first, sameIdentity);
        assertEquals(first.hashCode(), sameIdentity.hashCode());
        assertEquals(0, first.compareTo(sameIdentity));
        assertEquals("System=Harmonia.N1=Petasos\\.Receiver", first.getCanonicalName());
        assertEquals("System:1=Harmonia.N1:2\\.4=Petasos\\.Receiver",
                first.getCanonicalNameWithVersion());
        assertEquals("System:1=Harmonia.N1:2\\.4=Petasos\\.Receiver", first.toString());
        assertEquals("Harmonia.Petasos.Receiver", first.getName());
        assertEquals("Petasos.Receiver", first.getFriendlyName());
        assertEquals("Harmonia-Petasos.Receiver", first.getDisplayName());
        assertEquals("N1=Petasos\\.Receiver", first.getRelativeName());
        assertNotEquals(first, Topic.builder().addEntry("System", "Harmonia")
                .addEntry("Component", "Petasos.Receiver").build());
    }

    @Test
    void canonicalFormsRoundTripIdentityAndAllVersionData() {
        Topic original = Topic.builder()
                .addEntry("System", "Harmonia", "1")
                .addEntry("Component", "Petasos.Receiver", "2.4")
                .build();

        Topic identityRoundTrip = Topic.fromCanonicalName(original.getCanonicalName());
        Topic completeRoundTrip = Topic.fromCanonicalNameWithVersion(original.getCanonicalNameWithVersion());

        assertEquals(original, identityRoundTrip);
        assertTrue(identityRoundTrip.getEntries().stream().allMatch(entry -> entry.getVersion().isEmpty()));
        assertEquals(original.getCanonicalNameWithVersion(), completeRoundTrip.getCanonicalNameWithVersion());
        assertEquals(original.getEntries(), completeRoundTrip.getEntries());
    }

    @Test
    void parentAncestorAndStrictAncestryPreserveVersions() {
        Topic root = Topic.builder().addEntry("System", "Harmonia", "root-v").build();
        Topic child = Topic.builder().addEntry("System", "Harmonia", "child-v")
                .addEntry("Component", "Petasos").build();

        assertNull(root.getParent());
        assertEquals(root, child.getParent());
        assertEquals("child-v", child.getParent().getEntries().get(0).getVersion().orElseThrow());
        assertEquals("child-v", child.getAncestor(1).getEntries().get(0).getVersion().orElseThrow());
        assertTrue(root.isAncestorOf(child));
        assertFalse(child.isAncestorOf(root));
        assertFalse(root.isAncestorOf(root));
        assertFalse(root.isAncestorOf(null));
        assertThrows(IllegalArgumentException.class, () -> child.getAncestor(0));
        assertThrows(IllegalArgumentException.class, () -> child.getAncestor(3));
    }

    @Test
    void naturalOrderingUsesPairwiseFieldsAndProperPrefix() {
        Topic root = Topic.builder().addEntry("System", "Harmonia").build();
        Topic child = Topic.builder().addEntry("System", "Harmonia")
                .addEntry("Component", "Calliope").build();
        Topic caseVariant = Topic.builder().addEntry("system", "Harmonia").build();
        TreeSet<Topic> topics = new TreeSet<>();
        topics.add(child);
        topics.add(root);
        topics.add(caseVariant);

        assertTrue(root.compareTo(child) < 0);
        assertTrue(root.compareTo(caseVariant) < 0);
        assertEquals(3, topics.size());
        assertEquals(root, topics.first());
    }

    @Test
    void repeatedQualifiersAndDisplayDepthDoNotCollapseTypedPathData() {
        Topic repeated = Topic.builder().addEntry("Layer", "Surgical Ward")
                .addEntry("Layer", "Clinical Services")
                .addEntry("Layer", "Canberra").build();
        Topic differentQualifier = Topic.builder().addEntry("Location", "Surgical Ward")
                .addEntry("Service", "Clinical Services").addEntry("City", "Canberra").build();
        Topic root = Topic.builder().addEntry("Layer", "Surgical Ward").build();

        assertNotEquals(repeated, differentQualifier);
        assertEquals("Surgical Ward.Clinical Services.Canberra", repeated.getName());
        assertEquals("Clinical Services-Canberra", repeated.getDisplayName());
        assertEquals("Surgical Ward", root.getDisplayName());
        assertEquals("Layer=Canberra", repeated.getRelativeName());
    }

    @Test
    void allowedComposedAndDecomposedTextIsPreservedWithoutNormalization() {
        String composed = "é";
        String decomposed = "e\u0301";
        Topic composedTopic = Topic.builder().addEntry("Name", composed).build();
        Topic decomposedTopic = Topic.builder().addEntry("Name", decomposed).build();

        assertNotEquals(composedTopic, decomposedTopic);
        assertEquals(composed, composedTopic.getFriendlyName());
        assertEquals(decomposed, decomposedTopic.getFriendlyName());
    }

    @Test
    void topicEntryEqualityIncludesOptionalVersionWhileTopicIdentityDoesNot() {
        TopicEntry absent = Topic.builder().addEntry("Role", "LAB").build().getEntries().get(0);
        TopicEntry present = Topic.builder().addEntry("Role", "LAB", "opaque").build()
                .getEntries().get(0);
        TopicEntry samePresent = Topic.builder().addEntry("Role", "LAB", "opaque").build()
                .getEntries().get(0);

        assertNotEquals(absent, present);
        assertEquals(present, samePresent);
        assertEquals(present.hashCode(), samePresent.hashCode());
        assertEquals(Topic.builder().addEntry("Role", "LAB").build(),
                Topic.builder().addEntry("Role", "LAB", "opaque").build());
    }
}