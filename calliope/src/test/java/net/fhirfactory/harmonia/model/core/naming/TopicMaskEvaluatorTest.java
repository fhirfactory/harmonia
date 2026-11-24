/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.model.core.naming;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicMaskEntry;
import net.fhirfactory.harmonia.model.core.naming.support.TopicMaskEvaluator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TopicMask evaluation safety, concurrency, and diagnostics")
class TopicMaskEvaluatorTest {

    @Test
    @DisplayName("Evaluation short-circuits on the first qualifier field")
    void evaluationShortCircuitsInFieldOrder() {
        List<TopicMaskEvaluator.Field> fields = new ArrayList<>();
        TopicMaskEvaluator evaluator = new TopicMaskEvaluator((entry, field, value) -> {
            fields.add(field);
            return false;
        }, LoggerFactory.getLogger("topic-mask-short-circuit-test"));
        Topic candidate = Topic.builder().addEntry("Q", "sensitive-value").build();

        assertThat(evaluator.matches(List.of(TopicMaskEntry.any()), false, candidate)).isFalse();
        assertThat(fields).containsExactly(TopicMaskEvaluator.Field.QUALIFIER);

        fields.clear();
        Topic deeperCandidate = Topic.builder().addEntry("Q", "sensitive-value")
                .addEntry("Extra", "tail").build();
        assertThat(evaluator.matches(List.of(TopicMaskEntry.any()), false, deeperCandidate)).isFalse();
        assertThat(fields).isEmpty();
    }

    @Test
    @DisplayName("TRACE and ERROR diagnostics are bounded and omit fields, expressions, and candidate payloads")
    void diagnosticsAreSafeAndFailClosed() {
        String loggerName = "topic-mask-evaluator-diagnostics-test";
        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        Level previousLevel = logger.getLevel();
        boolean previousAdditive = logger.isAdditive();
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.setAdditive(false);
        logger.setLevel(Level.TRACE);
        logger.addAppender(appender);
        try {
            TopicMaskEntry entry = TopicMaskEntry.of("Q", "SENSITIVE-PAYLOAD");
            Topic candidate = Topic.builder().addEntry("Q", "SENSITIVE-PAYLOAD").build();
            Topic versionedCandidate = Topic.builder().addEntry("Q", "SENSITIVE-PAYLOAD", "v1").build();
            TopicMaskEvaluator matching = new TopicMaskEvaluator((criterion, field, value) -> switch (field) {
                case QUALIFIER -> criterion.matchesQualifier(value);
                case VALUE -> criterion.matchesValue(value);
                case VERSION -> criterion.matchesVersion(value);
            }, logger);
            TopicMaskEvaluator failing = new TopicMaskEvaluator((criterion, field, value) -> {
                throw new IllegalStateException("test-only failure");
            }, logger);

            assertThat(matching.matches(List.of(entry), false, candidate)).isTrue();
            assertThat(matching.matches(List.of(entry), false,
                    Topic.builder().addEntry("WRONG", "SENSITIVE-PAYLOAD").build())).isFalse();
            assertThat(matching.matches(List.of(TopicMaskEntry.of("Q", "SENSITIVE-PAYLOAD", "v2")),
                    false, versionedCandidate)).isFalse();
            assertThat(failing.matches(List.of(entry), false, candidate)).isFalse();

            List<String> messages = appender.list.stream().map(ILoggingEvent::getFormattedMessage).toList();
            assertThat(messages).anyMatch(message -> message.contains("result=match")
                    && message.contains("index=0") && message.contains("topicDepth=1"));
            assertThat(messages).anyMatch(message -> message.contains("category=qualifier")
                    && message.contains("index=0"));
            assertThat(messages).anyMatch(message -> message.contains("category=version-mismatch")
                    && message.contains("index=0"));
            assertThat(messages).anyMatch(message -> message.contains("evaluation failed")
                    && message.contains("category=evaluation-failure") && message.contains("field=qualifier")
                    && message.contains("index=0"));
            assertThat(messages).noneMatch(message -> message.contains("SENSITIVE-PAYLOAD"));
        } finally {
            logger.detachAppender(appender);
            logger.setLevel(previousLevel);
            logger.setAdditive(previousAdditive);
            appender.stop();
        }
    }

    @Test
    @DisplayName("TRACE diagnostics are conditional on the configured logger level")
    void traceDiagnosticsAreConditional() {
        String loggerName = "topic-mask-evaluator-trace-disabled-test";
        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        Level previousLevel = logger.getLevel();
        boolean previousAdditive = logger.isAdditive();
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.setAdditive(false);
        logger.setLevel(Level.INFO);
        logger.addAppender(appender);
        try {
            TopicMaskEvaluator evaluator = new TopicMaskEvaluator((entry, field, value) -> true, logger);
            assertThat(evaluator.matches(List.of(TopicMaskEntry.any()), false,
                    Topic.builder().addEntry("Q", "value").build())).isTrue();
            assertThat(appender.list).isEmpty();
        } finally {
            logger.detachAppender(appender);
            logger.setLevel(previousLevel);
            logger.setAdditive(previousAdditive);
            appender.stop();
        }
    }

    @Test
    @DisplayName("Empty ordinary criteria fail closed instead of acting as universal")
    void emptyOrdinaryCriteriaFailClosed() {
        String loggerName = "topic-mask-empty-criteria-test";
        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        Level previousLevel = logger.getLevel();
        boolean previousAdditive = logger.isAdditive();
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.setAdditive(false);
        logger.setLevel(Level.ERROR);
        logger.addAppender(appender);
        try {
            TopicMaskEvaluator evaluator = new TopicMaskEvaluator((entry, field, value) -> true, logger);
            assertThat(evaluator.matches(List.of(), true,
                    Topic.builder().addEntry("Q", "value").build())).isFalse();
            assertThat(appender.list).singleElement().satisfies(event -> {
                assertThat(event.getLevel()).isEqualTo(Level.ERROR);
                assertThat(event.getFormattedMessage()).contains("category=empty-criteria");
            });
        } finally {
            logger.detachAppender(appender);
            logger.setLevel(previousLevel);
            logger.setAdditive(previousAdditive);
            appender.stop();
        }
    }

    @Test
    @DisplayName("A diagnostic sink failure cannot turn a matcher failure into a match")
    void errorSinkFailureStillFailsClosed() {
        org.slf4j.Logger brokenLogger = (org.slf4j.Logger) Proxy.newProxyInstance(
                org.slf4j.Logger.class.getClassLoader(), new Class<?>[]{org.slf4j.Logger.class}, (proxy, method, args) -> {
                    if (method.getName().equals("error")) {
                        throw new IllegalStateException("test-only logger failure");
                    }
                    if (method.getReturnType() == boolean.class) {
                        return false;
                    }
                    return null;
                });
        TopicMaskEvaluator evaluator = new TopicMaskEvaluator((entry, field, value) -> {
            throw new IllegalStateException("test-only matcher failure");
        }, brokenLogger);

        assertThat(evaluator.matches(List.of(TopicMaskEntry.any()), false,
                Topic.builder().addEntry("Q", "value").build())).isFalse();
    }

    @Test
    @DisplayName("Invalid RE2/J construction logs an ERROR without exposing the original expression")
    void constructionDiagnosticsDoNotExposeExpressions() {
        Logger logger = (Logger) LoggerFactory.getLogger(TopicMaskEntry.class);
        Level previousLevel = logger.getLevel();
        boolean previousAdditive = logger.isAdditive();
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.setAdditive(false);
        logger.setLevel(Level.ERROR);
        logger.addAppender(appender);
        try {
            assertThatThrownBy(() -> TopicMaskEntry.of("SYNTHETIC-SENSITIVE(", "value"))
                    .isInstanceOf(IllegalArgumentException.class);
            List<String> messages = appender.list.stream().map(ILoggingEvent::getFormattedMessage).toList();
            assertThat(messages).anyMatch(message -> message.contains("field=qualifier"));
            assertThat(messages).noneMatch(message -> message.contains("SYNTHETIC-SENSITIVE"));
        } finally {
            logger.detachAppender(appender);
            logger.setLevel(previousLevel);
            logger.setAdditive(previousAdditive);
            appender.stop();
        }
    }

    @Test
    @DisplayName("A compiled immutable mask is safe for concurrent evaluation")
    void concurrentEvaluationReusesCompiledPatterns() throws Exception {
        TopicMask mask = TopicMask.builder().addRegex("Q[0-9]+", "value-(A|B)", ".*")
                .includeContainedElements(true).build();
        Topic candidate = Topic.builder().addEntry("Q42", "value-A", "version-1")
                .addEntry("Extra", "tail").build();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        try {
            List<Future<Boolean>> results = new ArrayList<>();
            for (int worker = 0; worker < 8; worker++) {
                results.add(executor.submit(() -> {
                    for (int iteration = 0; iteration < 500; iteration++) {
                        if (!mask.matches(candidate)) {
                            return false;
                        }
                    }
                    return true;
                }));
            }
            for (Future<Boolean> result : results) {
                assertThat(result.get(5, TimeUnit.SECONDS)).isTrue();
            }
        } finally {
            executor.shutdownNow();
            assertThat(executor.awaitTermination(2, TimeUnit.SECONDS)).isTrue();
        }
    }

    @Test
    @DisplayName("RE2/J input and field resource limits are enforced at construction")
    void regexLengthsAreBounded() {
        assertThat(TopicMaskEntry.of("a".repeat(1024), "v").getQualifierRegex()).hasSize(1024);
        assertThatThrownBy(() -> TopicMaskEntry.of("a".repeat(1025), "v"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(TopicMaskEntry.forLiteralValue(".".repeat(256)).getValueRegex()).hasSize(512);
    }

    @Test
    @DisplayName("Representative RE2/J compilation and matching workloads remain bounded")
    void representativeWorkloadsAreMeasured() {
        long compilationStart = System.nanoTime();
        List<TopicMaskEntry> compiled = new ArrayList<>();
        for (int index = 0; index < 500; index++) {
            compiled.add(TopicMaskEntry.of("Q[0-9]+", "value-(A|B)", "version-[0-9]+"));
        }
        long compilationNanos = System.nanoTime() - compilationStart;

        TopicMask mask = TopicMask.builder().addRegex("Q[0-9]+", "value-(A|B)", "version-[0-9]+")
                .build();
        Topic candidate = Topic.builder().addEntry("Q42", "value-A", "version-1").build();
        long matchingStart = System.nanoTime();
        for (int iteration = 0; iteration < 10_000; iteration++) {
            assertThat(mask.matches(candidate)).isTrue();
        }
        long matchingNanos = System.nanoTime() - matchingStart;

        assertThat(compiled).hasSize(500);
        assertThat(compilationNanos).isLessThan(TimeUnit.SECONDS.toNanos(30));
        assertThat(matchingNanos).isLessThan(TimeUnit.SECONDS.toNanos(30));
        System.out.printf("TopicMask workload: 500 compiles %.3f ms; 10000 matches %.3f ms%n",
                compilationNanos / 1_000_000.0, matchingNanos / 1_000_000.0);
    }
}