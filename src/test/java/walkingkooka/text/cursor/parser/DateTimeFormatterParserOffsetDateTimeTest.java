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

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public final class DateTimeFormatterParserOffsetDateTimeTest extends DateTimeFormatterParserOffsetTestCase<DateTimeFormatterParserOffsetDateTime<ParserContext>, OffsetDateTimeParserToken> {

    // year.............................................................................................................

    @Test
    public void testYearFails() {
        this.parseFailAndCheck2("uuuu-MM-dd", "200A");
    }

    @Test
    public void testYearFails2() {
        this.parseFailAndCheck2("uuuu-MM-dd", "200A");
    }

    // month............................................................................................................

    @Test
    public void testYearSeparatorMonthFails() {
        this.parseFailAndCheck2("uuuu-MM", "2001-AB");
    }

    @Test
    public void testYearSeparatorMonthFails2() {
        this.parseFailAndCheck2("uuuu-MM-dd HH:mmx", "2001-1Z");
    }

    // day..............................................................................................................

    @Test
    public void testYearSeparatorMonthSeparatorDayFails() {
        this.parseFailAndCheck2("uuuu-MM-dd", "2001-12-Q");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDayFails2() {
        this.parseFailAndCheck2("uuuu-MM-dd", "2001-12-1Q");
    }

    @Test
    public void testYearSeparatorMonthSeparatorInvalidDayThrows() {
        this.parseThrows2("uuuu-MM-dd", "2001-12-99");
    }

    @Test
    public void testYearSeparatorMonthSeparatorInvalidDayThrows2() {
        this.parseThrows2("uuuu-MM-dd", "2001-12-99Q");
    }

    // hour.............................................................................................................

    @Test
    public void testYearSeparatorHoursFails() {
        this.parseFailAndCheck2("uuuu-HH", "2001-A");
    }

    @Test
    public void testYearSeparatorHoursFails2() {
        this.parseFailAndCheck2("uuuu-HH", "2001-AB");
    }

    @Test
    public void testYearSeparatorHoursInvalidFails() {
        this.parseThrows2("uuuu-HH", "2001-99");
    }

    // minutes..........................................................................................................

    @Test
    public void testYearSeparatorMinutesFails() {
        this.parseFailAndCheck2("uuuu-mm", "2001-A");
    }

    @Test
    public void testYearSeparatorMinutesFails2() {
        this.parseFailAndCheck2("uuuu-mm", "2001-AB");
    }

    @Test
    public void testYearSeparatorMinutesInvalidFails() {
        this.parseThrows2("uuuu-mm", "2001-99");
    }

    // seconds..........................................................................................................

    @Test
    public void testYearSeparatorSecondsFails() {
        this.parseFailAndCheck2("uuuu-ss", "2001-A");
    }

    @Test
    public void testYearSeparatorSecondsFails2() {
        this.parseFailAndCheck2("uuuu-ss", "2001-AB");
    }

    @Test
    public void testYearSeparatorSecondsInvalidFails() {
        this.parseThrows2("uuuu-ss", "2001-99");
    }

    // secondsMillis....................................................................................................

    @Test
    public void testYearSeparatorSecondsMillisFails() {
        this.parseFailAndCheck2("uuuu-ss.SS", "2001-A");
    }

    @Test
    public void testYearSeparatorSecondsMillisFails2() {
        this.parseFailAndCheck2("uuuu-ss.SS", "2001-AB");
    }

    @Test
    public void testYearSeparatorSecondsMillisInvalidFails() {
        this.parseThrows2("uuuu-ss.SS", "2001-98.7Q");
    }

    @Test
    public void testYearSeparatorSecondsMillisInvalidFails2() {
        this.parseThrows2("uuuu-ss.SS", "2001-98.76Q");
    }

    @Test
    public void testYearSeparatorSecondsMillisInvalidFails3() {
        this.parseThrows2("uuuu-ss.SS", "2001-98.76QQ");
    }

    // offset...........................................................................................................

    @Test
    public void testYearSeparatorOffsetFails() {
        this.parseFailAndCheck2("uuuu x", "2001 #");
    }

    // pass.............................................................................................................

    @Test
    public void testYearSeparatorMonthSeparatorDaySeparatorHourSeparatorMinutesSeparatorSecondsOffset() {
        this.parseAndCheck2("uuuu-MM-dd HH:mm:ss x", "2001-12-31 12:58:59 +10");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDaySeparatorHourSeparatorMinutesSeparatorSecondsOffset2() {
        this.parseAndCheck2("uuuu-MM-dd HH:mm:ss x", "2001-12-31 12:58:59 +10", " ");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDaySeparatorHourSeparatorMinutesSeparatorSecondsMillisOffset() {
        this.parseAndCheck2("uuuu-MM-dd HH:mm:ss.SSS x", "2001-12-31 12:58:59.123 +10");
    }

    @Test
    public void testYearSeparatorMonthSeparatorDaySeparatorHourSeparatorMinutesSeparatorSecondsMillisOffset2() {
        this.parseAndCheck2("uuuu-MM-dd HH:mm:ss.SSS x", "2001-12-31 12:58:59.123 +10", " ");
    }

    @Test
    public void testHourSeparatorMinutesSeparatorSecondsMillisSeparatorYearSeparatorMonthSeparatorDayOffset() {
        this.parseAndCheck2("HH:mm:ss.SSS uuuu-MM-dd x", "12:58:59.123 2001-12-31 +10", " ");
    }

    // helpers..........................................................................................................

    @Override
    DateTimeFormatterParserOffsetDateTime<ParserContext> createParser(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterParserOffsetDateTime.with(formatter);
    }

    @Override
    String pattern() {
        return "uuuu-MM-dd HH:mm:ss.SSS";
    }

    @Override
    OffsetDateTimeParserToken createParserToken(final DateTimeFormatter formatter, final String text) {
        return OffsetDateTimeParserToken.with(OffsetDateTime.parse(text, formatter), text);
    }

    @Override
    public Class<DateTimeFormatterParserOffsetDateTime<ParserContext>> type() {
        return Cast.to(DateTimeFormatterParserOffsetDateTime.class);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNameSuffix() {
        return OffsetDateTime.class.getSimpleName();
    }
}
