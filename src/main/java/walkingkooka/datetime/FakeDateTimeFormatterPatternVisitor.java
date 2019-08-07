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

import walkingkooka.test.Fake;
import walkingkooka.visit.Visiting;

public class FakeDateTimeFormatterPatternVisitor extends DateTimeFormatterPatternVisitor implements Fake {
    @Override
    protected Visiting startVisitComponent(final int position,
                                           final String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitComponent(final int position,
                                     final String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitEra(final int width,
                            final DateTimeFormatterPatternComponentKind kind) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitYear(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitYearOfEra(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitDayOfYear(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMonthOfYear(final int width,
                                    final DateTimeFormatterPatternComponentKind kind) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitStandaloneMonthOfYear(final int width,
                                              final DateTimeFormatterPatternComponentKind kind) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitDayOfMonth(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitModifiedJulianDay(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitQuarterOfYear(final int width,
                                      final DateTimeFormatterPatternComponentKind kind) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitStandaloneQuarterOfYear(final int width,
                                                final DateTimeFormatterPatternComponentKind kind) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitWeekBasedYear(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitWeekOfWeekBasedYear(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitWeekOfMonthW(int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitDayOfWeek(final int width,
                                  final DateTimeFormatterPatternComponentKind kind) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitLocalizedDayOfWeek(final int width,
                                           final DateTimeFormatterPatternComponentKind kind) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitStandaloneLocalizedDayOfWeek(final int width,
                                                     final DateTimeFormatterPatternComponentKind kind) {
        super.visitStandaloneLocalizedDayOfWeek(width, kind);
    }

    @Override
    protected void visitWeekOfMonthF(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitAmpmOfDay(final int width,
                                  final DateTimeFormatterPatternComponentKind kind) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitClockHourOfAmpm12(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitHourOfAmpm11(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitClockHourOfAmpm24(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitHourOfDay23(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMinuteOfHour(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitSecondOfMinute(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitFractionOfSecond(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMilliOfDay(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitNanoOfSecond(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitNanoOfDay(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTimeZoneId(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitGenericTimeZoneName(final int width,
                                            final DateTimeFormatterPatternComponentKind kind) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTimeZoneName(final int width,
                                     final DateTimeFormatterPatternComponentKind kind) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitLocalizedZoneOffset(final int width,
                                            final DateTimeFormatterPatternComponentKind kind) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitZoneOffsetBigX(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitZoneOffsetSmallX(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitZoneOffsetZ(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPad(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitLiteral(final String unescapedText) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitOptionalStart(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitOptionalEnd(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitIllegal(final String component) {
        throw new UnsupportedOperationException();
    }
}
