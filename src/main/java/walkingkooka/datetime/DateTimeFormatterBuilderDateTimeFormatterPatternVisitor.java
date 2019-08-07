/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.datetime;

import walkingkooka.text.CharSequences;

import java.time.format.DateTimeFormatterBuilder;
import java.util.Objects;

/**
 * A {DateTimeFormatterPatternVisitor} that performs an almost equivalent action as {@link java.time.format.DateTimeFormatter#ofPattern(String)},
 * calling each of the #visit methods for each and every individual component.
 */
public class DateTimeFormatterBuilderDateTimeFormatterPatternVisitor extends DateTimeFormatterPatternVisitor {

    protected DateTimeFormatterBuilderDateTimeFormatterPatternVisitor(final DateTimeFormatterBuilder builder) {
        super();
        Objects.requireNonNull(builder, "builder");
        this.builder = builder;
    }

    @Override
    protected void visitEra(final int width,
                            final DateTimeFormatterPatternComponentKind kind) {
        this.appendComponent(DateTimeFormatterPatternVisitor.ERA, width);
    }

    @Override
    protected void visitYear(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.YEAR, width);
    }

    @Override
    protected void visitYearOfEra(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.YEAR_OF_ERA, width);
    }

    @Override
    protected void visitDayOfYear(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.DAY_OF_YEAR, width);
    }

    @Override
    protected void visitMonthOfYear(final int width,
                                    final DateTimeFormatterPatternComponentKind kind) {
        this.appendComponent(DateTimeFormatterPatternVisitor.MONTH_OF_YEAR, width);
    }

    @Override
    protected void visitStandaloneMonthOfYear(final int width,
                                              final DateTimeFormatterPatternComponentKind kind) {
        this.appendComponent(DateTimeFormatterPatternVisitor.STANDALONE_MONTH_OF_YEAR, width);
    }

    @Override
    protected void visitDayOfMonth(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.DAY_OF_MONTH, width);
    }

    @Override
    protected void visitModifiedJulianDay(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.MODIFIED_JULIAN_DAY, width);
    }

    @Override
    protected void visitQuarterOfYear(final int width,
                                      final DateTimeFormatterPatternComponentKind kind) {
        this.appendComponent(DateTimeFormatterPatternVisitor.QUARTER_OF_YEAR, width);
    }

    @Override
    protected void visitStandaloneQuarterOfYear(final int width,
                                                final DateTimeFormatterPatternComponentKind kind) {
        this.appendComponent(DateTimeFormatterPatternVisitor.STANDALONE_QUARTER_OF_YEAR, width);
    }

    @Override
    protected void visitWeekBasedYear(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.WEEK_BASED_YEAR, width);
    }

    @Override
    protected void visitWeekOfWeekBasedYear(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.WEEK_OF_WEEK_BASED_YEAR, width);
    }

    @Override
    protected void visitWeekOfMonthW(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.WEEK_OF_MONTH, width);
    }

    @Override
    protected void visitDayOfWeek(final int width,
                                  final DateTimeFormatterPatternComponentKind kind) {
        this.appendComponent(DateTimeFormatterPatternVisitor.DAY_OF_WEEK, width);
    }

    @Override
    protected void visitLocalizedDayOfWeek(final int width,
                                           final DateTimeFormatterPatternComponentKind kind) {
        this.appendComponent(DateTimeFormatterPatternVisitor.LOCALIZED_DAY_OF_WEEK, width);
    }

    @Override
    protected void visitStandaloneLocalizedDayOfWeek(final int width,
                                                     final DateTimeFormatterPatternComponentKind kind) {
        this.appendComponent(DateTimeFormatterPatternVisitor.STANDALONE_LOCALIZED_DAY_OF_WEEK, width);
    }

    @Override
    protected void visitWeekOfMonthF(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.WEEK_OF_MONTH_F, width);
    }

    @Override
    protected void visitAmpmOfDay(final int width,
                                  final DateTimeFormatterPatternComponentKind kind) {
        this.appendComponent(DateTimeFormatterPatternVisitor.AMPM_OF_DAY, width);
    }

    @Override
    protected void visitClockHourOfAmpm12(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.CLOCK_HOUR_OF_AMPM12, width);
    }

    @Override
    protected void visitHourOfAmpm11(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.HOUR_OF_AMPM11, width);
    }

    @Override
    protected void visitClockHourOfAmpm24(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.CLOCK_HOUR_OF_AMPM24, width);
    }

    @Override
    protected void visitHourOfDay23(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.HOUR_OF_DAY, width);
    }

    @Override
    protected void visitMinuteOfHour(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.MINUTE_OF_HOUR, width);
    }

    @Override
    protected void visitSecondOfMinute(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.SECOND_OF_MINUTE, width);
    }

    @Override
    protected void visitFractionOfSecond(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.FRACTION_OF_SECOND, width);
    }

    @Override
    protected void visitMilliOfDay(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.MILLI_OF_DAY, width);
    }

    @Override
    protected void visitNanoOfSecond(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.NANO_OF_SECOND, width);
    }

    @Override
    protected void visitNanoOfDay(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.NANO_OF_DAY, width);
    }

    @Override
    protected void visitTimeZoneId(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.TIMEZONE_ID, width);
    }

    @Override
    protected void visitGenericTimeZoneName(final int width,
                                            final DateTimeFormatterPatternComponentKind kind) {
        this.appendComponent(DateTimeFormatterPatternVisitor.GENERIC_TIMEZONE_NAME, width);
    }

    @Override
    protected void visitTimeZoneName(final int width,
                                     final DateTimeFormatterPatternComponentKind kind) {
        this.appendComponent(DateTimeFormatterPatternVisitor.TIMEZONE_NAME, width);
    }

    @Override
    protected void visitLocalizedZoneOffset(final int width,
                                            final DateTimeFormatterPatternComponentKind kind) {
        this.appendComponent(DateTimeFormatterPatternVisitor.LOCALIZED_ZONE_OFFSET, width);
    }

    @Override
    protected void visitZoneOffsetBigX(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.ZONE_OFFSET_BIGX, width);
    }

    @Override
    protected void visitZoneOffsetSmallX(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.ZONE_OFFSET_SMALLX, width);
    }

    @Override
    protected void visitZoneOffsetZ(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.ZONE_OFFSET_Z, width);
    }

    @Override
    protected void visitPad(final int width) {
        this.builder.padNext(width);
    }

    @Override
    protected void visitLiteral(final String text) {
        this.builder.appendLiteral(text);
    }

    @Override
    protected void visitOptionalStart(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.OPTIONAL_START, width);
    }

    @Override
    protected void visitOptionalEnd(final int width) {
        this.appendComponent(DateTimeFormatterPatternVisitor.OPTIONAL_END, width);
    }

    @Override
    protected void visitIllegal(final String component) {
        throw new IllegalArgumentException(component);
    }

    private void appendComponent(final char c,
                                 final int width) {
        this.builder.appendPattern(CharSequences.repeating(c, width).toString());
    }

    protected final DateTimeFormatterBuilder builder;

    @Override
    public String toString() {
        return this.builder.toFormatter().toString();
    }
}
