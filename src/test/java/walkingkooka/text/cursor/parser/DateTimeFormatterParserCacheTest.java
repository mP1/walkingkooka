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
import walkingkooka.ToStringTesting;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class DateTimeFormatterParserCacheTest extends DateTimeFormatterParserTestCase<DateTimeFormatterParserCache>
        implements ToStringTesting<DateTimeFormatterParserCache> {

    @Test
    public void testToString() {
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        this.toStringAndCheck(DateTimeFormatterParserCache.with(Locale.ENGLISH, 123, formatter ),
                "en 123 " + formatter);
    }

    @Override
    public Class<DateTimeFormatterParserCache> type() {
        return DateTimeFormatterParserCache.class;
    }

    @Override
    public String typeNameSuffix() {
        return "Cache";
    }
}
