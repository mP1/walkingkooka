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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class LocalTimeDateTimeFormatterParserTest extends LocalDateTimeFormatterParserTestCase<LocalTimeDateTimeFormatterParser<FakeParserContext>, LocalTimeParserToken> {

//    The ISO date formatter that formats or parses a date without an
//     * offset, such as '2011-12-03'

    @Test
    public void testHourInvalidFails() {
        this.parseFailAndCheck("AB:59");
    }

    @Test
    public void testHourMinuteSeparatorInvalid() {
        this.parseFailAndCheck("12A59");
    }

    @Test
    public void testHourSeparatorMinuteInvalidFails() {
        this.parseThrows("12:5X");
    }

    @Test
    public void testHourSeparatorMinuteSeparatorInvalid() {
        this.parseThrows("12:59A");
    }

    @Test
    public void testHourSeparatorMinuteSeparatorSecondsInvalidFails() {
        this.parseThrows("12:59:AA");
    }

    @Test
    public void testHourSeparatorMinuteSeparatorSecondsFractionThrows() {
        this.parseThrows("12:59:58.78A");
    }

    @Test
    public void tesHourSeparatortIncompleteFail() {
        this.parseFailAndCheck("12:");
    }

    @Test
    public void testHourSeparatorMinuteIncompleteFail() {
        this.parseThrows("12:59");
    }

    @Test
    public void testHourSeparatorMinuteSeparatorIncomplete() {
        this.parseThrows("12:59:58.");
    }

    @Test
    public void testHoursSeparatorMinutes() {
        this.parseAndCheck2("HH:mm", "12:59", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSeconds() {
        this.parseAndCheck2("HH:mm:ss", "12:59:58", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsDecimalFraction() {
        this.parseAndCheck2("HH:mm:ss.SSS", "12:59:58.123", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsLiteralFraction() {
        this.parseAndCheck2("HH:mm:ss'.'SSS", "12:59:58.123", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsFraction() {
        this.parseAndCheck2("HHmmssSSS", "125958123", "");
    }

    @Test
    public void testHourSeparatorMinuteSeparatorSecondWhitespace() {
        this.parseAndCheck2("12:59:58.123", "  ");
    }

    @Test
    public void testHourSeparatorMinuteSeparatorSecondLetters() {
        this.parseAndCheck2("12:59:58.123", "ZZ");
    }

    @Override
    protected LocalTimeDateTimeFormatterParser<FakeParserContext> createParser(final DateTimeFormatter formatter, final String pattern) {
        return LocalTimeDateTimeFormatterParser.with(formatter, pattern);
    }

    @Override
    String pattern() {
        return "HH:mm:ss.SSS";
    }

    @Override
    LocalTimeParserToken createParserToken(final DateTimeFormatter formatter, final String text) {
        return LocalTimeParserToken.with(LocalTime.parse(text, formatter), text);
    }

    @Override
    public Class<LocalTimeDateTimeFormatterParser<FakeParserContext>> type() {
        return Cast.to(LocalTimeDateTimeFormatterParser.class);
    }
}
