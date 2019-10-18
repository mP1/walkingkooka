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

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CharSequences;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DateTimeFormatterBuilderDateTimeFormatterPatternVisitorTest implements DateTimeFormatterPatternVisitorTesting<DateTimeFormatterBuilderDateTimeFormatterPatternVisitor> {

    private final static OffsetDateTime DATETIME1 = OffsetDateTime.of(LocalDateTime.of(2003, 2, 1, 4, 5, 6, 789), ZoneOffset.UTC);
    private final static OffsetDateTime DATETIME2 = OffsetDateTime.of(LocalDateTime.of(2004, 12, 31, 15, 16, 17, 890), ZoneOffset.UTC);

    @Test
    public void testNewNullDateTimeFormatterBuilderFails() {
        assertThrows(NullPointerException.class, () -> new DateTimeFormatterBuilderDateTimeFormatterPatternVisitor(null));
    }

    @Test
    public void testAllLettersOfAllLengths() {
        for (int i = 'A'; i <= 'Z'; i++) {
            final char c = (char) i;
            this.tryLetter(c);
            this.tryLetter(Character.toLowerCase(c));
        }
    }

    private void tryLetter(final char c) {
        if (DateTimeFormatterPatternVisitor.PAD != c) {
            for (int i = 1; i < 10; i++) {
                final String pattern = CharSequences.repeating(c, i).toString();
                if (validPattern(pattern)) {
                    this.acceptPatternAndFormat(pattern);
                    this.acceptPatternAndFormat(pattern);
                }
            }
        }
    }

    @Test
    public void testPadHour() {
        this.acceptPatternAndFormat("pH");
    }

    @Test
    public void testPad3Hour() {
        this.acceptPatternAndFormat("pppH");
    }

    @Test
    public void testDayOfMonth() {
        this.acceptPatternAndFormat("DD");
    }

    @Test
    public void testMonth2() {
        this.acceptPatternAndFormat("MM");
    }

    @Test
    public void testYear2() {
        this.acceptPatternAndFormat("uu");
    }

    @Test
    public void testDayOfMonth2Month2Year2() {
        this.acceptPatternAndFormat("DDMMuu");
    }

    @Test
    public void testDayOfMonth2Month2Year4() {
        this.acceptPatternAndFormat("DDMMuuuu");
    }

    @Test
    public void testHour2Minute2Seconds2Millis3() {
        this.acceptPatternAndFormat("HHmmssSSS");
    }

    @Test
    public void testDayOfMonth2Month2Year2Hour2Minute2Seconds2Millis3() {
        this.acceptPatternAndFormat("DDMMuuHHmmssSSS");
    }

    @Test
    public void testDayOfMonth3Month3Year2Hour2Minute2Seconds2() {
        this.acceptPatternAndFormat("DDDMMMuuHHmmss");
    }

    @Test
    public void testDayOfMonth3Month3Year4Hour2Minute2Seconds2Ampm() {
        this.acceptPatternAndFormat("DDDMMMMuuuuHHmmssa");
    }

    @Test
    public void testHour2Minute2Seconds2Millis4Ampm() {
        this.acceptPatternAndFormat("HHmmssSSSSa");
    }

    @Test
    public void testOptionalStartHourOptionalEnd() {
        this.acceptPatternAndFormat("[HH]");
    }

    @Test
    public void testLiteral() {
        this.acceptPatternAndFormat("'text-literal-abc123'");
    }

    private boolean validPattern(final String pattern) {
        boolean valid;
        try {
            DateTimeFormatter.ofPattern(pattern);
            valid = true;
        } catch (final IllegalArgumentException cause) {
            valid = false;
        }

        return valid;
    }

    private void acceptPatternAndFormat(final String pattern) {
        this.acceptPatternAndFormat(pattern, DATETIME1);
        this.acceptPatternAndFormat(pattern, DATETIME2);
    }

    private void acceptPatternAndFormat(final String pattern,
                                        final OffsetDateTime dateTime) {
        final DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        final DateTimeFormatterBuilderDateTimeFormatterPatternVisitor visitor = new DateTimeFormatterBuilderDateTimeFormatterPatternVisitor(builder);
        visitor.accept(pattern);

        final Locale locale = Locale.ENGLISH;

        final DateTimeFormatter expectedFormatter = DateTimeFormatter.ofPattern(pattern).withLocale(locale);
        final DateTimeFormatter formatter = builder.toFormatter(locale);

        assertEquals(expectedFormatter.toString(),
                formatter.toString(),
                () -> "Pattern " + CharSequences.quoteAndEscape(pattern));

        String expected;
        try {
            expected = expectedFormatter.format(dateTime);
        } catch (final DateTimeException ignore) {
            expected = null; // eg java.time.DateTimeException: Unable to extract ZoneId from temporal 2003-02-01T04:05:06.000000789Z
        }

        if (null != expected) {
            assertEquals(expected,
                    formatter.format(dateTime),
                    () -> "Pattern " + CharSequences.quoteAndEscape(pattern) + " value=" + dateTime);
        }
    }

    @Test
    public void testVisitIllegalFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createVisitor().accept("#"));
    }

    @Test
    public void testToString() {
        final String pattern = "HHmmssDDMMyy";

        final DateTimeFormatterBuilderDateTimeFormatterPatternVisitor visitor = this.createVisitor();
        visitor.accept(pattern);
        this.toStringAndCheck(visitor, DateTimeFormatter.ofPattern(pattern).toString());
    }

    @Override
    public DateTimeFormatterBuilderDateTimeFormatterPatternVisitor createVisitor() {
        return new DateTimeFormatterBuilderDateTimeFormatterPatternVisitor(new DateTimeFormatterBuilder());
    }

    @Override
    public Class<DateTimeFormatterBuilderDateTimeFormatterPatternVisitor> type() {
        return DateTimeFormatterBuilderDateTimeFormatterPatternVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public String typeNamePrefix() {
        return DateTimeFormatterBuilder.class.getSimpleName();
    }
}
