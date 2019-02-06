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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LocalDateTimeDateTimeFormatterParserTest extends LocalDateTimeFormatterParserTestCase<LocalDateTimeDateTimeFormatterParser<FakeParserContext>, LocalDateTimeParserToken>{

//    The ISO date formatter that formats or parses a date without an
//     * offset, such as '2011-12-03'

    @Test
    public void testYearInvalidFail() {
        this.parseFailAndCheck("200X-12-31");
    }

    @Test
    public void testYearSeparatorInvalidFail() {
        this.parseFailAndCheck("2001/12-31");
    }

    @Test
    public void testYearSeparatorMonthInvalidFail() {
        this.parseThrows("2001-XY-31");
    }

    @Test
    public void testYearSeparatorMonthSeparatorInvalidFail() {
        this.parseThrows("2001-12/31");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayInvalidFail() {
        this.parseThrows("2001-12-XY");
    }

    @Test
    public void testYearIncompleteFail() {
        this.parseFailAndCheck("2001");
    }

    @Test
    public void testYearSeparatorMonthIncompleteFail() {
        this.parseFailAndCheck("2001-");
    }

    @Test
    public void testYearSeparatorMonthSeparatorMissingFail() {
        this.parseThrows("2001-12");
    }

    @Test
    public void testYearSeparatorMonthSeparatorIncompleteFail() {
        this.parseThrows("2001-12-");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayIncompleteFail() {
        this.parseThrows("2001-12-3");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDaySeparatorHoursSeparatorMinutes() {
        this.parseAndCheck2("2001-12-31T12:58:59.987");
    }

    @Test
    public void testYearMonthDayHoursMinutes() {
        this.parseAndCheck2("yyyyMMddHHmm","200112311259", "");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDaySeparatorHoursSeparatorMinutesWhitespace() {
        this.parseAndCheck2("yyyy-MM-dd'T'HH:mm", "2001-12-31T12:59", "  ");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDaySeparatorHoursSeparatorMinutesLetters() {
        this.parseAndCheck2("yyyy-MM-dd'T'HH:mm","2001-12-31T12:59", "ZZ");
    }

    @Override
    protected LocalDateTimeDateTimeFormatterParser<FakeParserContext> createParser(final DateTimeFormatter formatter, final String pattern) {
        return LocalDateTimeDateTimeFormatterParser.with(formatter, pattern);
    }

    @Override
    String pattern() {
        return "yyyy-MM-dd'T'HH:mm:ss.SSS";
    }

    @Override
    LocalDateTimeParserToken createParserToken(final DateTimeFormatter formatter, final String text) {
        return LocalDateTimeParserToken.with(LocalDateTime.parse(text, formatter), text);
    }

    @Override
    public Class<LocalDateTimeDateTimeFormatterParser<FakeParserContext>> type() {
        return Cast.to(LocalDateTimeDateTimeFormatterParser.class);
    }
}
