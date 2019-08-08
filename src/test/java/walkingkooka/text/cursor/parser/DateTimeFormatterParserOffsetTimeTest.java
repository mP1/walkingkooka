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

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public final class DateTimeFormatterParserOffsetTimeTest extends DateTimeFormatterParserOffsetTestCase<DateTimeFormatterParserOffsetTime<ParserContext>, OffsetTimeParserToken> {

//    The ISO date formatter that formats or parses a date without an
//     * offset, such as '12:58:59.123-10:00'


    // hours............................................................................................................

    @Test
    public void testHoursFails() {
        this.parseFailAndCheck2("HH:mm x","A");
    }

    @Test
    public void testHoursFails2() {
        this.parseFailAndCheck2("HH:mm x","9A");
    }

    @Test
    public void testOffsetHoursFails() {
        this.parseFailAndCheck2("x HH:mm x","+10 Q");
    }

    // minutes..........................................................................................................

    @Test
    public void testOffsetSeparatorMinutesFails() {
        this.parseFailAndCheck2( "x mm", "+10 Q");
    }

    @Test
    public void testOffsetSeparatorMinutesFails2() {
        this.parseFailAndCheck2( "x mm", "+10 1Q");
    }

    // seconds...........................................................................................................

    @Test
    public void testOffsetSeparatorSecondsFails() {
        this.parseFailAndCheck2("x ss","+10 Q");
    }

    @Test
    public void testOffsetSeparatorSecondsFails2() {
        this.parseFailAndCheck2("x ss","+10 Q9 ");
    }

    // secondsMillis....................................................................................................

    @Test
    public void testOffsetSeparatorSecondsMillisFails() {
        this.parseFailAndCheck2("x ss.SSS","+10 12.Q");
    }

    @Test
    public void testOffsetSeparatorSecondsMillisFails2() {
        this.parseFailAndCheck2("x ss.SSS","+10 12.3Q");
    }

    // pass.............................................................................................................

    @Test
    public void testHoursSeparatorMinutesOffset() {
        this.parseAndCheck2("HH:mm x", "12:58 +10", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorAmpmOffset() {
        this.parseAndCheck2("hh:mm a x", "12:58 AM +10", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsSeparatorAmpmOffset() {
        this.parseAndCheck2("hh:mm:ss a x", "12:58:59 AM +10", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsSeparatorAmpmOffset2() {
        this.parseAndCheck2("hh:mm:ss a x", "12:58:59 PM +10", " ");
    }

    @Test
    public void testSecondsSeparatorNanoOfSecondSeparatorMinutesSeparatorHourOffset() {
        this.parseAndCheck2("nnn ss:mm:HH x", "123 59:58:12 +10", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsSeparatorNanoOfSecondOffset() {
        this.parseAndCheck2("HH:mm:ss nnn x", "12:58:59 123 +10", "");
    }

    // helper...........................................................................................................

    @Override
    DateTimeFormatterParserOffsetTime<ParserContext> createParser(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterParserOffsetTime.with(formatter);
    }

    @Override
    String pattern() {
        return "HH:mm:ss.SSS x";
    }

    @Override
    OffsetTimeParserToken createParserToken(final DateTimeFormatter formatter, final String text) {
        return OffsetTimeParserToken.with(OffsetTime.parse(text, formatter), text);
    }

    @Override
    public Class<DateTimeFormatterParserOffsetTime<ParserContext>> type() {
        return Cast.to(DateTimeFormatterParserOffsetTime.class);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNameSuffix() {
        return OffsetTime.class.getSimpleName();
    }
}
