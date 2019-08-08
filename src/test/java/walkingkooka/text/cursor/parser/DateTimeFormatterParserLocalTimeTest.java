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
import walkingkooka.datetime.DateTimeContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public final class DateTimeFormatterParserLocalTimeTest extends DateTimeFormatterParserLocalTestCase<DateTimeFormatterParserLocalTime<ParserContext>, LocalTimeParserToken> {

//    The ISO date formatter that formats or parses a date without an
//     * offset, such as '2011-12-03'

    // hours............................................................................................................

    @Test
    public void testHoursFails() {
        this.parseFailAndCheck2("HH:mm","A");
    }

    @Test
    public void testHoursFails2() {
        this.parseFailAndCheck2("HH:mm","9A");
    }

    @Test
    public void testHoursFails3() {
        this.parseFailAndCheck2("HH:mm","AB:59");
    }

    @Test
    public void testHoursInvalidFails() {
        this.parseFailAndCheck2("HH:mm", "12:AB");
    }

    // minutes..........................................................................................................

    @Test
    public void testHoursSeparatorMinutesFails() {
        this.parseFailAndCheck2( "HH:mm", "12:A9");
    }

    @Test
    public void testHoursSeparatorMinutesFails2() {
        this.parseFailAndCheck2("HH:mm", "12:9A");
    }

    @Test
    public void testHoursSeparatorMinutesInvalidFails() {
        this.parseThrows2("HH:mm", "12:99");
    }

    @Test
    public void testHoursSeparatorMinutesInvalidFails2() {
        this.parseThrows2("HH:mm", "12:99 ");
    }

    @Test
    public void testHoursSeparatorMinutesInvalidFails3() {
        this.parseThrows2("HH:mm", "12:99Q");
    }

    // seconds...........................................................................................................

    @Test
    public void testHoursSeparatorSecondsFails() {
        this.parseFailAndCheck2("HH:ss","12:A9");
    }

    @Test
    public void testHoursSeparatorSecondsFails2() {
        this.parseFailAndCheck2("HH:ss","12:9A");
    }

    @Test
    public void testHoursSeparatorSecondsInvalidFails() {
        this.parseThrows2("HH:ss", "12:99");
    }

    @Test
    public void testHoursSeparatorSecondsInvalidFails2() {
        this.parseThrows2("HH:ss", "12:99 ");
    }

    @Test
    public void testHoursSeparatorSecondsInvalidFails3() {
        this.parseThrows2("HH:ss", "12:99Q");
    }

    // secondsMillis....................................................................................................

    @Test
    public void testHoursSeparatorSecondsMillisFails() {
        this.parseFailAndCheck2("HH:ss.SSS", "12:59.");
    }

    @Test
    public void testHoursSeparatorSecondsMillisFails2() {
        this.parseFailAndCheck2("HH:ss.SSS","12:59.Q");
    }

    @Test
    public void testHoursSeparatorSecondsMillisInvalidFails() {
        this.parseThrows2("HH:ss", "12:99");
    }

    @Test
    public void testHoursSeparatorSecondsMillisInvalidFails2() {
        this.parseThrows2("HH:ss", "12:99 ");
    }

    @Test
    public void testHoursSeparatorSecondsMillisInvalidFails3() {
        this.parseThrows2("HH:ss", "12:99Q");
    }

    // pass.............................................................................................................

    @Test
    public void testHoursSeparatorMinutes() {
        this.parseAndCheck2("HH:mm", "12:58", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorAmpm() {
        this.parseAndCheck2("hh:mm a", "12:58 AM", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsSeparatorAmpm() {
        this.parseAndCheck2("hh:mm:ss a", "12:58:59 AM", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsSeparatorAmpm2() {
        this.parseAndCheck2("hh:mm:ss a", "12:58:59 PM", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSeconds() {
        this.parseAndCheck2("HH:mm:ss", "12:58:59", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsMillis() {
        this.parseAndCheck2("HH:mm:ss.SSS", "12:58:59.123", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsMillis2() {
        this.parseAndCheck2("HH:mm:ss.SSS", "12:58:59.123", " ");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsMillis3() {
        this.parseAndCheck2("HH:mm:ss.SSS", "12:58:59.123", "!");
    }

    @Test
    public void testAmpmSeparatorHoursSeparatorMinutesSeparatorSeconds() {
        this.parseAndCheck2("a hh:mm:ss", "AM 12:58:59", "");
    }

    @Test
    public void testHoursSeparatorAmpmSeparatorMinutesSeparatorSeconds() {
        this.parseAndCheck2("hh a mm:ss", "12 AM 58:59", "");
    }

    @Test
    public void testSecondsSeparatorNanoOfSecondSeparatorMinutesSeparatorHour() {
        this.parseAndCheck2("nnn ss:mm:HH", "123 59:58:12", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsSeparatorNanoOfSecond() {
        this.parseAndCheck2("HH:mm:ss nnn", "12:58:59 123", "");
    }

    // helpers..........................................................................................................

    @Override
    DateTimeFormatterParserLocalTime<ParserContext> createParser(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterParserLocalTime.with(formatter);
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
    public Class<DateTimeFormatterParserLocalTime<ParserContext>> type() {
        return Cast.to(DateTimeFormatterParserLocalTime.class);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNameSuffix() {
        return LocalTime.class.getSimpleName();
    }
}
