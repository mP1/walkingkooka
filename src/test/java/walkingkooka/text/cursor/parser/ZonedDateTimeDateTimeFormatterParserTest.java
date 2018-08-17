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

import org.junit.Test;
import walkingkooka.Cast;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class ZonedDateTimeDateTimeFormatterParserTest extends DateTimeFormatterParserTestCase<ZonedDateTimeDateTimeFormatterParser<FakeParserContext>, ZonedDateTimeParserToken>{

    // YearMonthDayHoursMinutes...............................................................................

    @Test
    public void testYearInvalidFails() {
        this.parseFailAndCheck2("yyyy-MM-dd HH:mmVV", "20Q");
    }

    @Test
    public void testYearInvalidFails2() {
        this.parseFailAndCheck2("yyyy-MM-dd HH:mmz", "20Q");
    }

    @Test
    public void testYearSeparatorInvalidFails() {
        this.parseFailAndCheck2("yyyy-MM-dd HH:mmVV", "2001Q");
    }

    @Test
    public void testYearSeparatorInvalidFails2() {
        this.parseFailAndCheck2("yyyy-MM-dd HH:mmz", "2001Q");
    }

    @Test
    public void testYearSeparatorMonthInvalidFails() {
        this.parseThrows2("yyyy-MM-dd HH:mmVV", "2001-1Q");
    }

    @Test
    public void testYearSeparatorMonthInvalidFails2() {
        this.parseThrows2("yyyy-MM-dd HH:mmz", "2001-1Q");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayInvalidFails() {
        this.parseThrows2("yyyy-MM-dd HH:mmVV", "2001-12-Q");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayInvalidFails2() {
        this.parseThrows2("yyyy-MM-dd HH:mmz", "2001-12-Q");
    }

    // YearMonthDayHoursMinutes...............................................................................

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHourInvalidFails() {
        this.parseThrows2("yyyy-MM-dd HH:mmx", "2001-12-31 AB:59");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHourSeparatorIncompleteFails() {
        this.parseThrows2("yyyy-MM-dd HH:mmx", "2001-12-31 12:");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHourSeparatorInvalidFails() {
        this.parseThrows2("yyyy-MM-dd HH:mmx","2001-12-31 12A59");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHourSeparatorMinuteOffset() {
        this.parseAndCheck2("yyyy-MM-dd HH:mmx", "2001-12-31 12:59+10", "");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHourSeparatorMinuteOffset2() {
        this.parseAndCheck2("yyyy-MM-dd HH:mmx", "2001-12-31 12:59+1000", "");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHourSeparatorMinuteOffset3() {
        this.parseAndCheck2("yyyy-MM-dd HH:mmx", "2001-12-31 12:59-1000", "");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHourSeparatorMinuteOffsetAfter() {
        this.parseAndCheck2("yyyy-MM-dd HH:mmx", "2001-12-31 12:59-1000", "   ");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHourSeparatorMinuteOffsetAfter2() {
        this.parseAndCheck2("yyyy-MM-dd HH:mmx", "2001-12-31 12:59-1234", "qqq");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHourSeparatorMinuteOffsetWithoutSeparator() {
        this.parseAndCheck2("yyyy-MM-dd Hmmx", "2001-12-31 1259-1000", "qqq");
    }

    // YearMonthDayHoursMinutesSeconds................................................................................

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHourSeparatorMinuteSeparatorInvalid() {
        this.parseThrows2("yyyy-MM-dd HH:mm:ssO","2001-12-31 12:59A");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHoursSeparatorMinutesSeparatorSecondsInvalidFails() {
        this.parseThrows2("yyyy-MM-dd HH:mm:ssO","2001-12-31 12:59:AA");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHourSeparatorMinuteIncompleteFail() {
        this.parseThrows2("yyyy-MM-dd HH:mm:ssO","2001-12-31 12:59");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHourSeparatorMinuteSeparatorIncomplete() {
        this.parseThrows2("yyyy-MM-dd HH:mm:ssx","2001-12-31 12:59:");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHoursSeparatorMinutesIncompleteFails() {
        this.parseThrows2("yyyy-MM-dd HH:mm:ssx","2001-12-31 12:59");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHoursSeparatorMinutesSeparatorSecondsOffset() {
        this.parseAndCheck2("yyyy-MM-dd HH:mm:ssX", "2001-12-31 12:58:59+10", "");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHoursSeparatorMinutesSeparatorSecondsOffset2() {
        this.parseAndCheck2("yyyy-MM-dd HH:mm:ssx", "2001-12-31 12:58:59+1000", "");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHoursSeparatorMinutesSeparatorSecondsOffset3() {
        this.parseAndCheck2("yyyy-MM-dd HH:mm:ssx", "2001-12-31 12:58:59-1000", "");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHoursSeparatorMinutesSeparatorSecondsOffsetAfter() {
        this.parseAndCheck2("yyyy-MM-dd HH:mm:ssx", "2001-12-31 12:58:59-1000", "   ");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHoursSeparatorMinutesSeparatorSecondsOffsetAfter2() {
        this.parseAndCheck2("yyyy-MM-dd HH:mm:ssx", "2001-12-31 12:58:59-1000", "zzz");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHoursMinutesSecondsV() {
        this.parseAndCheck2("yyyy-MM-dd HmmssVV", "2001-12-31 125859Z", " zzz");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHoursMinutesSecondsV2() {
        this.parseAndCheck2("yyyy-MM-dd HmmssVV", "2001-12-31 125859Australia/NSW", " zzz");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHoursMinutesSecondsLittleX() {
        this.parseAndCheck2("yyyy-MM-dd Hmmssx", "2001-12-31 125859-1000", "zzz");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespaceHoursLiteralMinutesLiteralSeconds() {
        this.parseAndCheck2("yyyy-MM-dd H':'mm':'ssx", "2001-12-31 12:58:59-1000", "zzz");
    }

    @Test
    public void testYearSeparatorMonthNameSeparatorDayWhitespaceHoursSeparatorMinutesSeparatorSecondsOffset() {
        this.parseAndCheck2("yyyy-MMMM-dd HH:mm:ssX", "2001-December-31 12:58:59+10", "");
    }

    @Test
    public void testYearSeparatorMonthNameSeparatorDayWhitespaceHoursSeparatorMinutesSeparatorSecondsTimeZoneId() {
        this.parseAndCheck2("yyyy-MM-dd HH:mm:ssVV", "2001-12-31 12:58:59Australia/NSW", "");
    }

    @Test
    public void testYearSeparatorMonthNameSeparatorDayWhitespaceHoursSeparatorMinutesSeparatorSecondsTimeZoneName() {
        this.parseAndCheck2("yyyy-MM-dd HH:mm:ssz", "2001-12-31 12:58:59AEST", "");
    }

    @Override
    protected ZonedDateTimeDateTimeFormatterParser<FakeParserContext> createParser(final DateTimeFormatter formatter, final String pattern) {
        return ZonedDateTimeDateTimeFormatterParser.with(formatter, pattern);
    }

    @Override
    String pattern() {
        return "yyyy-MM-dd HH:mm:ss.SSSZ";
    }

    @Override
    ZonedDateTimeParserToken createParserToken(final DateTimeFormatter formatter, final String text) {
        return ZonedDateTimeParserToken.with(ZonedDateTime.parse(text, formatter), text);
    }

    @Override
    protected Class<ZonedDateTimeDateTimeFormatterParser<FakeParserContext>> type() {
        return Cast.to(ZonedDateTimeDateTimeFormatterParser.class);
    }
}
