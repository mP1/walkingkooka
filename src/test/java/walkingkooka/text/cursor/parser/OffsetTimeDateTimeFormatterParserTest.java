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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

public final class OffsetTimeDateTimeFormatterParserTest extends OffsetDateTimeFormatterParserTestCase<OffsetTimeDateTimeFormatterParser<FakeParserContext>, OffsetTimeParserToken>{

//    The ISO date formatter that formats or parses a date without an
//     * offset, such as '12:58:59.123-10:00'

    // Hours seconds...............................................................................................

    @Test
    public void testHourInvalidFails() {
        this.parseFailAndCheck2("HH:mmx", "AB:59");
    }

    @Test
    public void testHourSeparatorIncompleteFails() {
        this.parseFailAndCheck2("HH:mmx", "12:");
    }

    @Test
    public void testHourSeparatorInvalidFails() {
        this.parseFailAndCheck2("HH:mmx","12A59");
    }

    @Test
    public void testHourSeparatorMinuteOffset() {
        this.parseAndCheck2("HH:mmx", "12:59+10", "");
    }

    @Test
    public void testHourSeparatorMinuteOffset2() {
        this.parseAndCheck2("HH:mmx", "12:59+1000", "");
    }

    @Test
    public void testHourSeparatorMinuteOffset3() {
        this.parseAndCheck2("HH:mmx", "12:59-1000", "");
    }

    @Test
    public void testHourSeparatorMinuteOffsetAfter() {
        this.parseAndCheck2("HH:mmx", "12:59-1000", "   ");
    }

    @Test
    public void testHourSeparatorMinuteOffsetAfter2() {
        this.parseAndCheck2("HH:mmx", "12:59-1234", "qqq");
    }

    @Test
    public void testHourSeparatorMinuteOffsetWithoutSeparator() {
        this.parseAndCheck2("HHmmx", "1259-1000", "qqq");
    }

    // HoursMinutesSeconds................................................................................................

    @Test
    public void testHourSeparatorMinuteSeparatorInvalid() {
        this.parseThrows2("HH:mm:ssO","12:59A");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsInvalidFails() {
        this.parseThrows2("HH:mm:ssO","12:59:AA");
    }

    @Test
    public void testHourSeparatorMinuteIncompleteFail() {
        this.parseThrows2("HH:mm:ssO","12:59");
    }

    @Test
    public void testHourSeparatorMinuteSeparatorIncomplete() {
        this.parseThrows2("HH:mm:ssx","12:59:");
    }

    @Test
    public void testHoursSeparatorMinutesIncompleteFails() {
        this.parseThrows2("HH:mm:ssx","12:59");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsOffset() {
        this.parseAndCheck2("HH:mm:ssX", "12:58:59+10", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsOffset2() {
        this.parseAndCheck2("HH:mm:ssx", "12:58:59+1000", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsOffset3() {
        this.parseAndCheck2("HH:mm:ssx", "12:58:59-1000", "");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsOffsetAfter() {
        this.parseAndCheck2("HH:mm:ssx", "12:58:59-1000", "   ");
    }

    @Test
    public void testHoursSeparatorMinutesSeparatorSecondsOffsetAfter2() {
        this.parseAndCheck2("HH:mm:ssx", "12:58:59-1000", "zzz");
    }

    @Test
    public void testHoursMinutesSecondsW() {
        this.parseAndCheck2("HHmmssx", "125859-1000", "zzz");
    }

    @Test
    public void testHoursLiteralMinutesLiteralSecondsW() {
        this.parseAndCheck2("HH':'mm':'ssx", "12:58:59-1000", "zzz");
    }

    @Override
    protected OffsetTimeDateTimeFormatterParser<FakeParserContext> createParser(final DateTimeFormatter formatter, final String pattern) {
        return OffsetTimeDateTimeFormatterParser.with(formatter, pattern);
    }

    @Override
    String pattern() {
        return "HH:mm:ss.SSSx";
    }

    @Override
    OffsetTimeParserToken createParserToken(final DateTimeFormatter formatter, final String text) {
        return OffsetTimeParserToken.with(OffsetTime.parse(text, formatter), text);
    }

    @Override
    public Class<OffsetTimeDateTimeFormatterParser<FakeParserContext>> type() {
        return Cast.to(OffsetTimeDateTimeFormatterParser.class);
    }
}
