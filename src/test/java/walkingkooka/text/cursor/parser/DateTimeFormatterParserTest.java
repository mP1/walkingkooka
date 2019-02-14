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
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import java.time.ZoneId;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class DateTimeFormatterParserTest implements ClassTesting2<DateTimeFormatterParser> {

    @Test
    public void testIsZoneId() {
        this.checkAllZoneIds((c) -> DateTimeFormatterParser.isZoneId(c, '+', '-'));
    }

    @Test
    public void testIsZoneName() {
        this.checkAllZoneIds((c) -> DateTimeFormatterParser.isZoneName(c, '+', '-'));
    }

    private void checkAllZoneIds(final Predicate<Character> predicate) {
        for(String zoneId : ZoneId.getAvailableZoneIds()){
            for(char c : zoneId.toCharArray()){
                assertTrue(predicate.test(c),
                        () -> "Chars " + CharSequences.quoteAndEscape(c) + " of zoneId " + CharSequences.quote(zoneId) + " failed");
            }
        }
    }

    @Override
    public Class<DateTimeFormatterParser> type() {
        return DateTimeFormatterParser.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
