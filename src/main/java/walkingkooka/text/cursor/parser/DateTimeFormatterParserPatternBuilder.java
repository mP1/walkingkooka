/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package walkingkooka.text.cursor.parser;

import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A builder that assists in gathering and consolidating {@link java.time.format.DateTimeFormatter} patterns
 * into integer values.
 */
final class DateTimeFormatterParserPatternBuilder {

    DateTimeFormatterParserPatternBuilder() {
        super();
    }

    void text() {
        this.addPattern(DateTimeFormatterParser.TEXT);
    }

    void digits() {
        this.addPattern(DateTimeFormatterParser.DIGITS);
    }

    void fraction() {
        this.addPattern(DateTimeFormatterParser.FRACTION);
    }

    void textOrDigits() {
        this.addPattern(DateTimeFormatterParser.TEXT_DIGITS);
    }

    void whitespace() {
        this.addPattern(DateTimeFormatterParser.WHITESPACE);
    }

    void zoneId() {
        this.addPattern(DateTimeFormatterParser.ZONEID);
    }

    void zoneName() {
        this.addPattern(DateTimeFormatterParser.ZONENAME);
    }

    void localisedZoneOffset() {
        this.addPattern(DateTimeFormatterParser.LOCALISED_ZONE_OFFSET);
    }

    void zoneOffset() {
        this.addPattern(DateTimeFormatterParser.ZONE_OFFSET);
    }

    void zOrZoneOffset() {
        this.addPattern(DateTimeFormatterParser.Z_OR_ZONE_OFFSET);
    }

    void constant(final char c) {
        this.add(c);
    }

    private void addPattern(final int c) {
        if(this.patterns.isEmpty()) {
            this.add(c);
        } else {
            final DateTimeFormatterParserPatternBuilderToken pattern = this.patterns.get(this.patterns.size()-1);
            if(pattern.requiresNew(c)) {
                this.add(c);
            } else {
                pattern.increaseCount();
            }
        }
    }

    private void add(final int pattern) {
        this.patterns.add(new DateTimeFormatterParserPatternBuilderToken(pattern));
    }

    private final List<DateTimeFormatterParserPatternBuilderToken> patterns = Lists.array();

    int[] build() {
        final List<DateTimeFormatterParserPatternBuilderToken> finished = Lists.array();

        DateTimeFormatterParserPatternBuilderToken previous = null;
        for(DateTimeFormatterParserPatternBuilderToken pattern : this.patterns) {
            DateTimeFormatterParserPatternBuilderToken p = pattern.finish();
            if(null==previous) {
                finished.add(p);
                previous = p;
                continue;
            }
            if(previous.isDifferent(p)){
                finished.add(p);
            }
            previous = p;
        }

        return finished.stream()
                .mapToInt(p -> p.pattern)
                .toArray();
    }

    @Override
    public String toString() {
        return this.patterns.stream()
                .map(m-> m.toString())
                .collect(Collectors.joining(","));
    }
}
