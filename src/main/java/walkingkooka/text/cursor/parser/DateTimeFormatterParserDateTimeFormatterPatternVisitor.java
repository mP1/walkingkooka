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

package walkingkooka.text.cursor.parser;

import walkingkooka.InvalidCharacterException;
import walkingkooka.NeverError;
import walkingkooka.datetime.DateTimeFormatterPatternComponentKind;
import walkingkooka.datetime.DateTimeFormatterPatternVisitor;
import walkingkooka.visit.Visiting;

final class DateTimeFormatterParserDateTimeFormatterPatternVisitor extends DateTimeFormatterPatternVisitor {

    static int checkPatternAndGuessLength(final DateTimeFormatterParser<?> parser,
                              final String pattern) {
        final DateTimeFormatterParserDateTimeFormatterPatternVisitor visitor = new DateTimeFormatterParserDateTimeFormatterPatternVisitor(parser, pattern);
        visitor.accept(pattern);
        return visitor.length;
    }

    DateTimeFormatterParserDateTimeFormatterPatternVisitor(final DateTimeFormatterParser<?> parser,
                                                           final String pattern) {
        super();
        this.parser = parser;
        this.pattern = pattern;
    }

    @Override
    protected Visiting startVisitComponent(final int position,
                                           final String text) {
        this.position = position;
        return Visiting.CONTINUE;
    }

    @Override
    protected void visitEra(final int width,
                            final DateTimeFormatterPatternComponentKind kind) {
        this.date(width, kind);
    }

    @Override
    protected void visitYear(final int width) {
        this.date(width);
    }

    @Override
    protected void visitYearOfEra(final int width) {
        this.date(width);
    }

    @Override
    protected void visitDayOfYear(final int width) {
        this.date(width);
    }

    @Override
    protected void visitMonthOfYear(final int width,
                                    final DateTimeFormatterPatternComponentKind kind) {
        this.date(width, kind);
    }

    @Override
    protected void visitStandaloneMonthOfYear(final int width,
                                              final DateTimeFormatterPatternComponentKind kind) {
        this.date(width, kind);
    }

    @Override
    protected void visitDayOfMonth(final int width) {
        this.date(width);
    }

    @Override
    protected void visitModifiedJulianDay(final int width) {
        this.date(width);
    }

    @Override
    protected void visitQuarterOfYear(final int width,
                                      final DateTimeFormatterPatternComponentKind kind) {
        this.date(width, kind);
    }

    @Override
    protected void visitStandaloneQuarterOfYear(final int width,
                                                final DateTimeFormatterPatternComponentKind kind) {
        this.date(width, kind);
    }

    @Override
    protected void visitWeekBasedYear(final int width) {
        this.date(width);
    }

    @Override
    protected void visitWeekOfWeekBasedYear(final int width) {
        this.date(width);
    }

    @Override
    protected void visitWeekOfMonthW(final int width) {
        this.date(width);
    }

    @Override
    protected void visitDayOfWeek(final int width,
                                  final DateTimeFormatterPatternComponentKind kind) {
        this.date(width, kind);
    }

    @Override
    protected void visitLocalizedDayOfWeek(final int width,
                                           final DateTimeFormatterPatternComponentKind kind) {
        this.date(width, kind);
    }

    @Override
    protected void visitStandaloneLocalizedDayOfWeek(final int width,
                                                     final DateTimeFormatterPatternComponentKind kind) {
        this.date(width, kind);
    }

    @Override
    protected void visitWeekOfMonthF(final int width) {
        this.date(width);
    }

    @Override
    protected void visitAmpmOfDay(final int width,
                                  final DateTimeFormatterPatternComponentKind kind) {
        this.time(width, kind);
    }

    @Override
    protected void visitClockHourOfAmpm12(final int width) {
        this.time(width);
    }

    @Override
    protected void visitHourOfAmpm11(final int width) {
        this.time(width);
    }

    @Override
    protected void visitClockHourOfAmpm24(final int width) {
        this.time(width);
    }

    @Override
    protected void visitHourOfDay23(final int width) {
        this.time(width);
    }

    @Override
    protected void visitMinuteOfHour(final int width) {
        this.time(width);
    }

    @Override
    protected void visitSecondOfMinute(final int width) {
        this.time(width);
    }

    @Override
    protected void visitFractionOfSecond(final int width) {
        this.time(width);
    }

    @Override
    protected void visitMilliOfDay(final int width) {
        this.time(width);
    }

    @Override
    protected void visitNanoOfSecond(final int width) {
        this.time(width);
    }

    @Override
    protected void visitNanoOfDay(final int width) {
        this.time(width);
    }

    @Override
    protected void visitTimeZoneId(final int width) {
        this.timeZoneIdOrName();
    }

    @Override
    protected void visitGenericTimeZoneName(final int width,
                                            final DateTimeFormatterPatternComponentKind kind) {
        this.timeZoneIdOrName();
    }

    @Override
    protected void visitTimeZoneName(final int width,
                                     final DateTimeFormatterPatternComponentKind kind) {
        this.timeZoneIdOrName();
    }

    @Override
    protected void visitLocalizedZoneOffset(final int width,
                                            final DateTimeFormatterPatternComponentKind kind) {
        this.timeZoneOffset();
    }

    @Override
    protected void visitLiteral(final String text) {
        this.addWidth(text.length());
    }

    @Override
    protected void visitZoneOffsetBigX(final int width) {
        this.timeZoneOffset();
    }

    @Override
    protected void visitZoneOffsetSmallX(final int width) {
        this.timeZoneOffset();
    }

    @Override
    protected void visitZoneOffsetZ(final int width) {
        this.timeZoneOffset();
    }

    private void date(final int width) {
        this.parser.date(this);
        this.addWidth(width);
    }

    private void date(final int width, final DateTimeFormatterPatternComponentKind kind) {
        this.parser.date(this);
        this.addWidth(width, kind);
    }

    private void time(final int width) {
        this.parser.time(this);
        this.addWidth(width);
    }

    private void time(final int width, final DateTimeFormatterPatternComponentKind kind) {
        this.parser.time(this);
        this.addWidth(width, kind);
    }

    private void timeZoneIdOrName() {
        this.parser.timeZone(this);
        this.addWidth(10);
    }

    private void timeZoneOffset() {
        this.parser.timeZoneOffset(this);
        this.addWidth(15);
    }

    final void invalidPatternLetter() {
        throw new InvalidCharacterException(this.pattern, this.position);
    }

    private int position;

    private final DateTimeFormatterParser<?> parser;

    private void addWidth(final int width) {
        this.length += width;
    }

    private void addWidth(final int width, final DateTimeFormatterPatternComponentKind kind) {
        int add;
        switch(kind) {
            case NUMBER:
                add = width;
                break;
            case LONG_TEXT:
                add = 15;
                break;
            case SHORT_TEXT:
                add = 5;
                break;
            case NARROW_TEXT:
                add = 5;
                break;
            default:
                NeverError.unhandledEnum(kind, DateTimeFormatterPatternComponentKind.values());
                add = 0;
        }
        this.length += add;
    }

    private int length;

    @Override
    public String toString() {
        return this.pattern;
    }

    private final String pattern;
}
