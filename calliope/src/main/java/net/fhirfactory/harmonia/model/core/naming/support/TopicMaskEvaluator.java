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

package net.fhirfactory.harmonia.model.core.naming.support;

import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicEntry;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicMaskEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Stateless, fail-closed evaluator for positional TopicMask criteria.
 * <p>
 * Depth is checked before any field evaluation; qualifier, value, and present
 * version constraints are evaluated in that order. Patterns are immutable and
 * shared, while RE2/J matcher state remains local to each call. Diagnostics
 * contain only bounded structural metadata, never field values or expressions.
 * <p>
 * This evaluator never implements universal selection: null, empty, oversized,
 * or null-containing criterion lists are rejected as evaluation errors and return
 * {@code false}. Universal intent exists only as the explicit
 * {@link net.fhirfactory.harmonia.model.core.naming.TopicMask#all()} kind, which
 * {@code TopicMask} handles before delegating ordinary criteria here.
 * These ordering and failure rules implement {@code DRP-Topic-and-TopicMask.Final.001.001.md} §6.
 */
public final class TopicMaskEvaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicMaskEvaluator.class);
    private static final TopicMaskEvaluator DEFAULT = new TopicMaskEvaluator(TopicMaskEvaluator::matchField, LOGGER);

    /** Fields that can be independently evaluated for a mask entry. */
    public enum Field { QUALIFIER, VALUE, VERSION }

    /** Injectable matcher used to exercise defensive failure behavior. */
    @FunctionalInterface
    public interface FieldMatcher {
        /** @return true when the field satisfies the compiled criterion */
        boolean matches(TopicMaskEntry entry, Field field, String candidate);
    }

    private final FieldMatcher fieldMatcher;
    private final Logger logger;

    /** @return shared evaluator backed by immutable RE2/J patterns and SLF4J diagnostics */
    public static TopicMaskEvaluator defaultEvaluator() {
        return DEFAULT;
    }

    /**
     * Creates an evaluator with a replaceable field matcher and diagnostic logger.
     *
     * @param fieldMatcher matcher implementation
     * @param logger logger receiving safe TRACE/ERROR diagnostics
     */
    public TopicMaskEvaluator(FieldMatcher fieldMatcher, Logger logger) {
        this.fieldMatcher = Objects.requireNonNull(fieldMatcher, "fieldMatcher");
        this.logger = Objects.requireNonNull(logger, "logger");
    }

    /**
     * Tests an ordinary criterion list against a Topic. Universal intent is
     * represented only by {@link net.fhirfactory.harmonia.model.core.naming.TopicMask#all()}.
     *
     * @param criteria nonempty ordered entry criteria
     * @param includeContainedElements whether trailing Topic entries are allowed
     * @param candidate candidate Topic; null always fails
     * @return true only when every required criterion matches
     */
    public boolean matches(List<TopicMaskEntry> criteria, boolean includeContainedElements, Topic candidate) {
        if (criteria == null || criteria.size() > TopicLimits.MAX_DEPTH) {
            error("criteria", -1, candidate == null ? 0 : candidate.getDepth(),
                    criteria == null ? 0 : criteria.size());
            return false;
        }
        if (criteria.isEmpty()) {
            // Empty ordinary criteria must never be interpreted as universal selection.
            error("empty-criteria", -1, candidate == null ? 0 : candidate.getDepth(), 0);
            return false;
        }
        for (TopicMaskEntry criterion : criteria) {
            if (criterion == null) {
                error("criteria", -1, candidate == null ? 0 : candidate.getDepth(), criteria.size());
                return false;
            }
        }
        if (candidate == null) {
            trace(false, "null-candidate", -1, 0, criteria.size());
            return false;
        }
        int maskDepth = criteria.size();
        int topicDepth = candidate.getDepth();
        if (topicDepth < maskDepth || (!includeContainedElements && topicDepth != maskDepth)) {
            trace(false, "depth", -1, topicDepth, maskDepth);
            return false;
        }
        for (int index = 0; index < maskDepth; index++) {
            TopicMaskEntry criterion = criteria.get(index);
            TopicEntry entry = candidate.getEntries().get(index);
            if (!evaluate(criterion, Field.QUALIFIER, entry.getQualifier(), index, topicDepth, maskDepth)) {
                return false;
            }
            if (!evaluate(criterion, Field.VALUE, entry.getValue(), index, topicDepth, maskDepth)) {
                return false;
            }
            Optional<String> versionExpression = criterion.getVersionRegex();
            if (versionExpression.isPresent()) {
                Optional<String> candidateVersion = entry.getVersion();
                if (candidateVersion.isEmpty()) {
                    trace(false, "required-version-absent", index, topicDepth, maskDepth);
                    return false;
                }
                if (!evaluate(criterion, Field.VERSION, candidateVersion.get(), index, topicDepth, maskDepth)) {
                    return false;
                }
            }
        }
        trace(true, "matched", maskDepth - 1, topicDepth, maskDepth);
        return true;
    }

    /** Evaluates one field and converts unexpected matcher exceptions into a logged mismatch. */
    private boolean evaluate(TopicMaskEntry criterion, Field field, String value,
                             int index, int topicDepth, int maskDepth) {
        try {
            if (fieldMatcher.matches(criterion, field, value)) {
                return true;
            }
            String category = field == Field.VERSION ? "version-mismatch" : field.name().toLowerCase(Locale.ROOT);
            trace(false, category, index, topicDepth, maskDepth);
            return false;
        } catch (RuntimeException exception) {
            error("evaluation-failure", field.name().toLowerCase(Locale.ROOT), index, topicDepth, maskDepth);
            return false;
        }
    }

    /** Default field matcher delegating to the entry's precompiled RE2/J patterns. */
    private static boolean matchField(TopicMaskEntry entry, Field field, String candidate) {
        return switch (field) {
            case QUALIFIER -> entry.matchesQualifier(candidate);
            case VALUE -> entry.matchesValue(candidate);
            case VERSION -> entry.matchesVersion(candidate);
        };
    }

    /** Emits conditional TRACE with structural metadata only; sink failures do not affect matching. */
    private void trace(boolean matched, String category, int index, int topicDepth, int maskDepth) {
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("TopicMask result={} category={} index={} topicDepth={} maskDepth={}",
                        matched ? "match" : "mismatch", category, index < 0 ? "none" : index,
                        topicDepth, maskDepth);
            }
        } catch (RuntimeException ignored) {
            // A diagnostic failure cannot change the matching result.
        }
    }

    /** Emits a safe ERROR diagnostic without the candidate, expression, or exception payload. */
    private void error(String category, int index, int topicDepth, int maskDepth) {
        error(category, "none", index, topicDepth, maskDepth);
    }

    /** Emits a safe ERROR diagnostic naming the failed field category only. */
    private void error(String category, String field, int index, int topicDepth, int maskDepth) {
        try {
            logger.error("TopicMask evaluation failed category={} field={} index={} topicDepth={} maskDepth={}",
                    category, field, index, topicDepth, maskDepth);
        } catch (RuntimeException ignored) {
            // An ERROR-sink failure must not convert a failed evaluation into a match.
        }
    }
}