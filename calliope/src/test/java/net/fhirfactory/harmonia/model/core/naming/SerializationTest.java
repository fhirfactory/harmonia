/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;
import org.junit.jupiter.api.Test;

/** Verifies Java serialization of the Naming models through validating proxies. */
class SerializationTest {

    @Test
    void topicRoundTripPreservesIdentityEntriesVersionsAndExactText() throws Exception {
        Topic original = Topic.builder().addEntry("System", "Harmonia")
                .addEntry("Path", "a.b=c\\d 患者😀", "01.002").build();

        Topic restored = roundTrip(original);

        assertEquals(original, restored);
        assertEquals(original.getEntries(), restored.getEntries());
        assertEquals("01.002", restored.getEntries().get(1).getVersion().orElseThrow());
        assertEquals(original.getCanonicalNameWithVersion(), restored.getCanonicalNameWithVersion());
        assertEquals(original.getEntries().get(1), roundTrip(original.getEntries().get(1)));
    }

    @Test
    void ordinaryMaskRoundTripPreservesKindContainmentConstraintPresenceAndMatching() throws Exception {
        TopicMask original = TopicMask.builder().addPatterns("Role", "LAB*", ">=1.2,<2")
                .addPatterns("*", "\\*", "").addPatterns("Kind", "Exact").includeContainedElements(true).build();

        TopicMask restored = roundTrip(original);

        assertEquals(original, restored);
        assertFalse(restored.isUniversal());
        assertTrue(restored.isIncludeContainedElements());
        assertEquals("", restored.getEntries().get(1).getVersionConstraint().orElseThrow());
        assertTrue(restored.getEntries().get(2).getVersionConstraint().isEmpty());
        assertEquals(original.getCanonicalExpression(), restored.getCanonicalExpression());
        Topic candidate = Topic.builder().addEntry("Role", "LAB-1", "1.10").addEntry("Q", "*", "v")
                .addEntry("Kind", "Exact").addEntry("Extra", "Trailing").build();
        assertFalse(original.matches(candidate));
        assertFalse(restored.matches(candidate));
        TopicMask matching = roundTrip(TopicMask.builder().addPatterns("Role", "LAB*", ">=1.2,<2")
                .addPatterns("*", "\\*", "v").addPatterns("Kind", "Exact").includeContainedElements(true).build());
        assertTrue(matching.matches(candidate));
        assertEquals(original.getEntries().get(0), roundTrip(original.getEntries().get(0)));
    }

    @Test
    void universalMaskDeserializesToTheSharedExplicitInstance() throws Exception {
        assertSame(TopicMask.all(), roundTrip(TopicMask.all()));
    }

    @Test
    void validationExceptionRoundTripPreservesSafeMetadata() throws Exception {
        NamingValidationException original = assertThrows(NamingValidationException.class,
                () -> TopicMask.builder().addAny().addPatterns("Role", "LAB", ">=x"));

        NamingValidationException restored = roundTrip(original);

        assertEquals(NamingFailureCategory.INVALID_FIELD, restored.getCategory());
        assertEquals(NamingOperation.MASK_BUILD, restored.getOperation());
        assertEquals(NamingFieldKind.VERSION_CONSTRAINT, restored.getFieldKind());
        assertEquals(1, restored.getEntryIndex().orElseThrow());
        assertEquals(original.getMessage(), restored.getMessage());
    }

    @Test
    void craftedProxyStreamsAreRevalidatedAndRejectedWithoutEchoingContent() throws Exception {
        String secret = "Bad\u0001Secret";
        Serializable[] invalidForms = {
                new Topic.SerialForm(new String[]{secret}, new String[]{"LAB"}, new String[]{null}),
                new Topic.SerialForm(new String[]{"Role"}, new String[]{"LAB"}, new String[]{""}),
                new Topic.SerialForm(new String[]{"Role"}, new String[]{"LAB", "X"}, new String[]{null}),
                new Topic.SerialForm(new String[0], new String[0], new String[0]),
                new Topic.SerialForm(new String[17], new String[17], new String[17]),
                new Topic.SerialForm(null, new String[]{"LAB"}, new String[]{null}),
                new TopicEntry.SerialForm("", "LAB", null),
                new TopicMaskEntry.SerialForm("**", "LAB", null),
                new TopicMask.SerialForm(false, false, new String[]{"Role"}, new String[]{"LAB"},
                        new String[]{">=1.x"}),
                new TopicMask.SerialForm(false, false, new String[0], new String[0], new String[0]),
                new TopicMask.SerialForm(true, true, new String[0], new String[0], new String[0]),
                new TopicMask.SerialForm(true, false, new String[]{"*"}, new String[]{"*"}, new String[]{null})
        };
        for (Serializable form : invalidForms) {
            InvalidObjectException failure = assertThrows(InvalidObjectException.class,
                    () -> deserialize(serialize(form)), form.getClass().getName());
            assertFalse(failure.getMessage().contains("Secret"));
        }
    }

    @Test
    void modelsRejectStreamsThatBypassTheirValidatingProxies() throws Exception {
        Topic topic = Topic.builder().addEntry("Role", "LAB").build();
        Object[] instances = {topic, topic.getEntries().get(0), TopicMask.all(), TopicMaskEntry.any()};
        for (Object instance : instances) {
            Class<?> type = instance.getClass();
            assertTrue(Serializable.class.isAssignableFrom(type), type.getName());
            Method readObject = type.getDeclaredMethod("readObject", ObjectInputStream.class);
            readObject.setAccessible(true);
            InvocationTargetException failure = assertThrows(InvocationTargetException.class,
                    () -> readObject.invoke(instance, (Object) null));
            assertInstanceOf(InvalidObjectException.class, failure.getCause(), type.getName());
        }
        assertFalse(Serializable.class.isAssignableFrom(TopicBuilder.class));
        assertFalse(Serializable.class.isAssignableFrom(TopicMaskBuilder.class));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Serializable> T roundTrip(T value) throws IOException, ClassNotFoundException {
        return (T) deserialize(serialize(value));
    }

    private static byte[] serialize(Serializable value) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream output = new ObjectOutputStream(bytes)) {
            output.writeObject(value);
        }
        return bytes.toByteArray();
    }

    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return input.readObject();
        }
    }
}
