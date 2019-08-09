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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public final class DateTimeFormatterParserLocalDateTest extends DateTimeFormatterParserLocalTestCase<DateTimeFormatterParserLocalDate<ParserContext>, LocalDateParserToken> {

    // fail/incomplete...................................................................................................

    @Test
    public void testYearInvalidFails() {
        this.parseFailAndCheck2("yyyy-MM-dd", "200X-12-31");
    }

    @Test
    public void testMonthInvalidFails() {
        this.parseFailAndCheck2("MM-dd-yyyy", "0X-31-2000");
    }

    @Test
    public void testYearMonthInvalidFails() {
        this.parseFailAndCheck2("uuuu-MM-dd", "2000-0X-31");
    }

    @Test
    public void testDayInvalidFails() {
        this.parseFailAndCheck2("dd-MM-yyyy", "0X-12-2000");
    }

    @Test
    public void testMonthDayInvalidFails() {
        this.parseFailAndCheck2("MM-dd-yyyy", "12-3X-2000");
    }

    @Test
    public void testDayMonthYearMissingFails() {
        this.parseFailAndCheck2("dd-MM-yyyy", "31-12");
    }

    @Test
    public void testDayMonthYearMissingFails2() {
        this.parseFailAndCheck2("dd-MM-yyyy", "31-12 ");
    }

    // invalid.............................................................................................................

    @Test
    public void testYearMonthDayInvalidMonthThrows() {
        this.parseThrows2("yyyy-MM-dd", "2001-99-11");
    }

    @Test
    public void testYearMonthDayInvalidMonthThrows2() {
        this.parseThrows2("yyyy-MM-dd", "2001-99-11!");
    }

    @Test
    public void testYearMonthDayInvalidDayThrows() {
        this.parseThrows2("yyyy-MM-dd", "2001-12-99");
    }

    @Test
    public void testYearSeparatorMonthNameSeparatorDayInvalidDaysFails() {
        this.parseThrows2("yyyy-MMMM-dd", "2001-December-99");
    }

    @Test
    public void testYearSeparatorMonthNameSeparatorDayInvalidDaysFails2() {
        this.parseThrows2("yyyy-MMMM-dd", "2001-December-99X");
    }

    // pass.............................................................................................................

    @Test
    public void testYearSeparatorMonthSeparatorDay() {
        LocalDate.parse("2001-12-31", this.formatter());

        this.parseAndCheck2("yyyy-MM-dd", "2000-12-31");
    }

    @Test
    public void testYearMonthDay() {
        this.parseAndCheck2("yyyyMMdd", "20011231", "");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayWhitespace() {
        this.parseAndCheck2("yyyy-MM-dd", "2001-12-31", "  ");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayLetters() {
        this.parseAndCheck2("yyyy-MM-dd", "2001-12-31", "ZZ");
    }

    @Test
    public void testYearSeparatorMonthNameSeparatorDayLetters() {
        this.parseAndCheck2("yyyy-MMMM-dd", "2001-December-31", "ZZ");
    }

    // helper...........................................................................................................

    @Override
    DateTimeFormatterParserLocalDate<ParserContext> createParser(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterParserLocalDate.with(formatter);
    }

    @Override
    String pattern() {
        return "yyyy-MM-dd";
    }

    @Override
    LocalDateParserToken createParserToken(final DateTimeFormatter formatter, final String text) {
        return LocalDateParserToken.with(LocalDate.parse(text, formatter), text);
    }

    @Override
    public Class<DateTimeFormatterParserLocalDate<ParserContext>> type() {
        return Cast.to(DateTimeFormatterParserLocalDate.class);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNameSuffix() {
        return LocalDate.class.getSimpleName();
    }
}
