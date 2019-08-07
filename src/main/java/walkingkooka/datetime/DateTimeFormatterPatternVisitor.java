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
import walkingkooka.visit.Visiting;
import walkingkooka.visit.Visitor;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

/**
 * A {@link Visitor} for {@link java.time.format.DateTimeFormatter patterns}.
 */
public abstract class DateTimeFormatterPatternVisitor extends Visitor<String> {

    protected DateTimeFormatterPatternVisitor() {
        super();
    }

    /**
     * Called before each component visit method and contains the text and position for the first character.
     */
    protected Visiting startVisitComponent(final int position,
                                           final String text) {
        return Visiting.CONTINUE;
    }

    protected void endVisitComponent(final int position,
                                     final String text) {
    }

    /**
     * <pre>
     * G       era                         text              AD; Anno Domini; A
     * </pre>
     */
    protected void visitEra(final int width,
                            final DateTimeFormatterPatternComponentKind kind) {
    }

    /**
     * <pre>
     * u       year                        year              2004; 04
     * </pre>
     */
    protected void visitYear(final int width) {
    }

    /**
     * <pre>
     * y       year-of-era                 year              2004; 04
     * </pre>
     */
    protected void visitYearOfEra(final int width) {
    }

    /**
     * <pre>
     * D       day-of-year                 number            189
     * </pre>
     */
    protected void visitDayOfYear(final int width) {
    }

    /**
     * <pre>
     * M/L     month-of-year               number/text       7; 07; Jul; July; J
     * </pre>
     */
    protected void visitMonthOfYear(final int width,
                                    final DateTimeFormatterPatternComponentKind kind) {
    }

    protected void visitStandaloneMonthOfYear(final int width,
                                              final DateTimeFormatterPatternComponentKind kind) {
    }

    /**
     * <pre>
     * d       day-of-month                number            10
     * </pre>
     */
    protected void visitDayOfMonth(final int width) {
    }

    /**
     * <pre>
     * Q/q     quarter-of-year             number/text       3; 03; Q3; 3rd quarter
     * </pre>
     */
    protected void visitQuarterOfYear(final int width,
                                      final DateTimeFormatterPatternComponentKind kind) {
    }

    protected void visitStandaloneQuarterOfYear(final int width,
                                                final DateTimeFormatterPatternComponentKind kind) {
    }

    /**
     * <pre>
     * Y       week-based-year             year              1996; 96
     * </pre>
     */
    protected void visitWeekBasedYear(final int width) {
    }

    /**
     * <pre>
     * w       week-of-week-based-year     number            27
     * </pre>
     */
    protected void visitWeekOfWeekBasedYear(final int width) {
    }

    /**
     * <pre>
     * W       week-of-month               number            4
     * </pre>
     */
    protected void visitWeekOfMonthW(final int length) {
    }

    /**
     * <pre>
     * E       day-of-week                 text              Tue; Tuesday; T
     * </pre>
     */
    protected void visitDayOfWeek(final int width,
                                  final DateTimeFormatterPatternComponentKind kind) {
    }

    /**
     * <pre>
     * e/c     localized day-of-week       number/text       2; 02; Tue; Tuesday; T
     * </pre>
     */
    protected void visitLocalizedDayOfWeek(final int width,
                                           final DateTimeFormatterPatternComponentKind kind) {
    }

    /**
     * <pre>
     * e/c     localized day-of-week       number/text       2; 02; Tue; Tuesday; T
     * </pre>
     */
    final void traverseStandaloneLocalizedDayOfWeek(final int width,
                                                    final DateTimeFormatterPatternComponentKind kind) {
        // DateTimeFormatter.of("cc") fails.
        switch (width) {
            case 1:
            case 3:
            case 4:
            case 5:
                this.visitStandaloneLocalizedDayOfWeek(width, kind);
                break;
            default:
                this.visitIllegal('c', width);
                break;
        }
    }

    protected void visitStandaloneLocalizedDayOfWeek(final int width,
                                                     final DateTimeFormatterPatternComponentKind kind) {
    }

    final void traverseWeekOfMonthF(final int width) {
        switch (width) {
            case 1:
                this.visitWeekOfMonthF(width);
                break;
            default:
                this.visitIllegal('f', width);
                break;
        }
    }

    /**
     * <pre>
     * F       week-of-month               number            3
     * </pre>
     */
    protected void visitWeekOfMonthF(final int width) {
    }

    /**
     * <pre>
     * a       am-pm-of-day                text              PM        3
     * </pre>
     */
    protected void visitAmpmOfDay(final int width,
                                  final DateTimeFormatterPatternComponentKind kind) {
    }

    /**
     * <pre>
     * h       clock-hour-of-am-pm (1-12)  number            12
     * </pre>
     */
    protected void visitClockHourOfAmpm12(final int width) {
    }

    /**
     * <pre>
     * K       hour-of-am-pm (0-11)        number            0
     * </pre>
     */
    protected void visitHourOfAmpm11(final int width) {
    }

    /**
     * <pre>
     * k       clock-hour-of-am-pm (1-24)  number            0
     * </pre>
     */
    protected void visitClockHourOfAmpm24(final int width) {
    }

    /**
     * <pre>
     * H       hour-of-day (0-23)          number            0
     * </pre>
     */
    protected void visitHourOfDay23(final int width) {
    }

    /**
     * <pre>
     * m       minute-of-hour              number            30
     * </pre>
     */
    protected void visitMinuteOfHour(final int width) {
    }

    /**
     * <pre>
     * s       second-of-minute            number            55
     * </pre>
     */
    protected void visitSecondOfMinute(final int width) {
    }

    /**
     * <pre>
     * S       fraction-of-second          fraction          978
     * </pre>
     */
    protected void visitFractionOfSecond(final int width) {
    }

    /**
     * <pre>
     * A       milli-of-day                number            1234
     * </pre>
     */
    protected void visitMilliOfDay(final int width) {
    }

    /**
     * <pre>
     * n       nano-of-second              number            987654321
     * </pre>
     */
    protected void visitNanoOfSecond(final int width) {
    }

    /**
     * <pre>
     * N       nano-of-day                 number            1234000000
     * </pre>
     */
    protected void visitNanoOfDay(final int width) {
    }

    /**
     * <pre>
     * V       time-zone ID                zone-id           America/Los_Angeles; Z; -08:30
     * </pre>
     * <pre>
     * ZoneId: This outputs the time-zone ID, such as 'Europe/Paris'. If the count of letters is two, then the time-zone ID is output. Any other count of letters throws IllegalArgumentException.x`x``
     * </pre>
     */
    final void traverseTimezoneId(final int width) {
        switch (width) {
            case 2:
                this.visitTimeZoneId(width);
                break;
            default:
                this.visitIllegal('V', width);
        }
    }

    protected void visitTimeZoneId(final int width) {
    }

    /**
     * <pre>
     * z       time-zone name              zone-name         Pacific Standard Time; PST
     * </pre>
     */
    protected void visitTimeZoneName(final int width,
                                     final DateTimeFormatterPatternComponentKind kind) {
    }

    /**
     * <pre>
     * O       localized zone-offset       offset-O          GMT+8; GMT+08:00; UTC-08:00;
     * </pre>
     * <pre>
     * Offset O: This formats the localized offset based on the number of pattern letters.
     * One letter outputs the short form of the localized offset, which is localized offset text, such as 'GMT',
     * with hour without leading zero, optional 2-digit minute and second if non-zero, and colon, for example 'GMT+8'.
     * Four letters outputs the full form, which is localized offset text, such as 'GMT, with 2-digit hour and minute field,
     * optional second field if non-zero, and colon, for example 'GMT+08:00'. Any other count of letters throws IllegalArgumentException.
     * </pre>
     */
    final void traverseLocalizedZoneOffset(final int width,
                                           final DateTimeFormatterPatternComponentKind kind) {
        switch (width) {
            case 1:
            case 4:
                this.visitLocalizedZoneOffset(width, kind);
                break;
            default:
                this.visitIllegal('O', width);
        }
    }

    protected void visitLocalizedZoneOffset(final int width,
                                            final DateTimeFormatterPatternComponentKind kind) {
    }

    /**
     * <pre>
     * X       zone-offset 'Z' for zero    offset-X          Z; -08; -0830; -08:30; -083015; -08:30:15;
     * </pre>
     */
    protected void visitZoneOffsetBigX(final int width) {
    }

    /**
     * <pre>
     * x       zone-offset                 offset-x          +0000; -08; -0830; -08:30; -083015; -08:30:15;
     * </pre>
     */
    protected void visitZoneOffsetSmallX(final int width) {
    }

    /**
     * <pre>
     * Z       zone-offset                 offset-Z          +0000; -0800; -08:00;
     * </pre>
     */
    protected void visitZoneOffsetZ(final int width) {
    }

    /**
     * <pre>
     * p       pad next                    pad modifier      1
     * </pre>
     */
    protected void visitPad(final int width) {
    }

    /**
     * <pre>
     * '       escape for text             delimiter
     * ''      single quote                literal
     * </pre>
     */
    final int traverseEscaped(final String pattern,
                              final int position) {
        final int length = pattern.length();

        int end = position + 1;
        boolean escape = false;

        while (end < length) {
            final char c = pattern.charAt(end);
            end++;

            if (escape) {
                escape = false;
                continue;
            }
            if ('\\' == c) {
                escape = true;
                continue;
            }
            if ('\'' == c) {
                break;
            }
        }

        final String escaped = pattern.substring(position, end);
        if (Visiting.CONTINUE == this.startVisitComponent(position, escaped)) {
            this.visitLiteral(escaped.length() == 2 ?
                    "'" :
                    CharSequences.unescape(pattern.substring(position+1, end-1)).toString()
            );
        }
        this.endVisitComponent(position, escaped);
        return end;
    }

    protected void visitLiteral(final String text) {
    }

    /**
     * <pre>
     * [       optional section start
     * </pre>
     */
    protected void visitOptionalStart(final int width) {
    }

    /**
     * <pre>
     * ]       optional section end
     * </pre>
     */
    protected void visitOptionalEnd(final int width) {
    }

    // visitIllegal.....................................................................................................

    /**
     * Eventually calls {@link #visitIllegal(String)}
     */
    final int traverseIllegal(final String pattern,
                              final int position) {
        final String text = this.repeatingTextRun(position, pattern);
        if (Visiting.CONTINUE == this.startVisitComponent(position, text)) {
            this.visitIllegal(text);
        }
        this.endVisitComponent(position, text);
        return position + text.length();
    }

    private void visitIllegal(final char c,
                              final int width) {
        this.visitIllegal(CharSequences.repeating(c, width).toString());
    }

    /**
     * Called with any illegal component.
     */
    protected void visitIllegal(final String component) {
    }

    // visitLiteral.....................................................................................................

    /**
     * Eventually calls {@link #visitIllegal(String)}
     */
    final int traverseLiteral(final String pattern,
                              final int position) {
        final String text = this.repeatingTextRun(position, pattern);
        if (Visiting.CONTINUE == this.startVisitComponent(position, text)) {
            this.visitLiteral(text);
        }
        this.endVisitComponent(position, text);
        return position + text.length();
    }

    // helper...........................................................................................................

    final int traverse(final String pattern,
                       final int position,
                       final int max,
                       final IntConsumer dispatcher) {
        return this.traverseRepeatingComponentWidth(pattern,
                position,
                max,
                dispatcher);
    }

    final int traverseNumber(final String pattern,
                             final int position,
                             final int max,
                             final IntConsumer dispatcher) {
        return this.traverseRepeatingComponentWidth(pattern,
                position,
                max,
                dispatcher);
    }

    final int traverseNumberOrText(final String pattern,
                                   final int position,
                                   final int max,
                                   final BiConsumer<Integer, DateTimeFormatterPatternComponentKind> dispatcher) {
        return this.traverseRepeatingComponentWidthKind(pattern,
                position,
                max,
                DateTimeFormatterPatternComponentKindFactory.NUMBER_OR_TEXT,
                dispatcher);
    }

    final int traverseText(final String pattern,
                           final int position,
                           final int max,
                           final BiConsumer<Integer, DateTimeFormatterPatternComponentKind> dispatcher) {
        return this.traverseRepeatingComponentWidthKind(pattern,
                position,
                max,
                DateTimeFormatterPatternComponentKindFactory.TEXT,
                dispatcher);
    }

    /**
     * Finds the repeating character, then calls the visitor method with a width parameter.
     */
    private int traverseRepeatingComponentWidth(final String pattern,
                                                final int position,
                                                final int max,
                                                final IntConsumer dispatcher) {
        final String text = this.repeatingTextRun(position, pattern);
        final int width = text.length();

        if (Visiting.CONTINUE == this.startVisitComponent(position, text)) {
            if (width <= max) { // inclusive
                dispatcher.accept(width);
            } else {
                this.visitIllegal(text);
            }
        }
        this.endVisitComponent(position, text);
        return position + width;
    }

    /**
     * Finds the repeating character, then calls the visitor method with a width and kind parameter.
     */
    private int traverseRepeatingComponentWidthKind(final String pattern,
                                                    final int position,
                                                    final int max,
                                                    final DateTimeFormatterPatternComponentKindFactory kindFactory,
                                                    final BiConsumer<Integer, DateTimeFormatterPatternComponentKind> dispatcher) {
        final String text = this.repeatingTextRun(position, pattern);
        final int width = text.length();

        if (Visiting.CONTINUE == this.startVisitComponent(position, text)) {
            if (width <= max) {
                dispatcher.accept(width, kindFactory.kind(width));
            } else {
                this.visitIllegal(text);
            }
        }
        this.endVisitComponent(position, text);
        return position + width;
    }

    private String repeatingTextRun(final int position,
                                    final String pattern) {
        final int length = pattern.length();
        final char c = pattern.charAt(position);

        int end = position + 1;
        while (end < length) {
            if (c != pattern.charAt(end)) {
                break;
            }
            end++;
        }
        return pattern.substring(position, end);
    }

    // Visitor..........................................................................................................

    /**
     * Accepts a pattern and calls visit methods for each of its components.
     */
    @Override
    public void accept(final String pattern) {
        Objects.requireNonNull(pattern, "pattern");

        int i = 0;
        final int patternLength = pattern.length();

        while (i < patternLength) {
            final char c = pattern.charAt(i);
            i = DateTimeFormatterPatternComponent.ofCharacter(c)
                    .traverse(pattern,
                            i,
                    this);
        }
    }
}
