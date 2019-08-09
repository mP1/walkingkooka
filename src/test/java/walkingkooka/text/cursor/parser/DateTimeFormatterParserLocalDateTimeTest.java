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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public final class DateTimeFormatterParserLocalDateTimeTest extends DateTimeFormatterParserLocalTestCase<DateTimeFormatterParserLocalDateTime<ParserContext>, LocalDateTimeParserToken> {

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
    public void testHourInvalidFails() {
        this.parseFailAndCheck2("HH mm ss", "1X 58 59");
    }

    @Test
    public void testMinutesInvalidFails() {
        this.parseFailAndCheck2("mm HH ss", "5X 12 59");
    }

    @Test
    public void testMinutesHourInvalidFails() {
        this.parseFailAndCheck2("mm HH ss", "58 1x 59");
    }

    @Test
    public void testSecondsInvalidFails() {
        this.parseFailAndCheck2("ss HH mm", "5X 12 58");
    }

    @Test
    public void testHourSeparatorMinutesSeparatorSecondsFails() {
        this.parseFailAndCheck2("HH mm ss", "12 58 5X");
    }

    @Test
    public void testDayMonthYearMissingFails() {
        this.parseFailAndCheck2("dd-MM-yyyy", "31-12");
    }

    @Test
    public void testDayMonthYearMissingFails2() {
        this.parseFailAndCheck2("dd-MM-yyyy", "31-12 ");
    }

    @Test
    public void testDayMonthYearHourMinuteMissingFails() {
        this.parseFailAndCheck2("dd-MM-yyyy HH:mm", "31-12-2000 12:XX");
    }

    // fail.............................................................................................................

    @Test
    public void testYearSeparatorMonthInvalidFails() {
        this.parseThrows2("yyyy-MM-dd HH:mm", "2000-99-31 12:58");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayInvalidFails() {
        this.parseThrows2("yyyy-MM-dd HH:mm", "2000-12-99 12:58");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDaySeparatorHourInvalidFails() {
        this.parseThrows2("yyyy-MM-dd HH:mm", "2000-12-31 99:58");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDaySeparatorHourSeparatorMinuteInvalidFails() {
        this.parseThrows2("yyyy-MM-dd HH:mm", "2000-12-31 12:99");
    }

    // pass.............................................................................................................

    @Test
    public void testYearSeparatorMonthSeparatorDaySeparatorHourSeparatorMinutes() {
        this.parseAndCheck2("dd-MM-yyyy HH:mm", "31-12-2000 12:58");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDaySeparatorHourSeparatorMinutesSeparatorSeconds() {
        this.parseAndCheck2("dd-MM-yyyy HH:mm:ss", "31-12-2000 12:58:59", "");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDaySeparatorHourSeparatorMinutesSeparatorSecondsSeparatorMillis() {
        this.parseAndCheck2("dd-MM-yyyy HH:mm:ss.SSS", "31-12-2000 12:58:59.789", "");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDaySeparatorHourSeparatorMinutesSeparatorAmpm() {
        this.parseAndCheck2("dd-MM-yyyy hh:mm:ss a", "31-12-2000 12:58:59 AM", "!");
    }

    // DateTimeFormatterParserLocalTestCase..............................................................................

    @Override
    DateTimeFormatterParserLocalDateTime<ParserContext> createParser(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterParserLocalDateTime.with(formatter);
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
    public Class<DateTimeFormatterParserLocalDateTime<ParserContext>> type() {
        return Cast.to(DateTimeFormatterParserLocalDateTime.class);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNameSuffix() {
        return LocalDateTime.class.getSimpleName();
    }
}
