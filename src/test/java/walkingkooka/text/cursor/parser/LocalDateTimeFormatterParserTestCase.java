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

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class LocalDateTimeFormatterParserTestCase<P extends DateTimeFormatterParser<FakeParserContext>,
        T extends ParserToken>
        extends DateTimeFormatterParserTestCase<P, T> {

    LocalDateTimeFormatterParserTestCase() {
        super();
    }

    @Test
    public final void testWithTimeZoneIdFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createParser(this.pattern() + "VV");
        });
    }

    @Test
    public final void testWithTimeZoneNameFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createParser(this.pattern() + "z");
        });
    }

    @Test
    public final void testWithLocalizedZoneOffsetFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createParser(this.pattern() + "O");
        });
    }

    @Test
    public final void testWithZoneOffsetBigXFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createParser(this.pattern() + "X");
        });
    }

    @Test
    public final void testWithZoneOffsetLittleXFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createParser(this.pattern() + "x");
        });
    }

    @Test
    public final void testWithZoneOffsetZFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createParser(this.pattern() + "Z");
        });
    }

    // TypeNameTesting...........................................................................

    @Override
    public final String typeNamePrefix() {
        return "Local";
    }
}
