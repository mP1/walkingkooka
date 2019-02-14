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
 */
package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.naming.NameTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ParserTokenNodeNameTest implements ClassTesting2<ParserTokenNodeName>,
        NameTesting<ParserTokenNodeName, ParserTokenNodeName> {

    @Test
    public void testFromClassNullFails() {
        assertThrows(NullPointerException.class, () -> {
            ParserTokenNodeName.fromClass(null);
        });
    }

    @Test
    public void testFromClass() {
        final ParserTokenNodeName name = ParserTokenNodeName.fromClass(StringParserToken.class);
        assertEquals("String", name.value(), "name.value");
    }

    @Test
    public void testWithInvalidInitialFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ParserTokenNodeName.with("1");
        });
    }

    @Test
    public void testWithInvalidPartFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ParserTokenNodeName.with("Hello Goodbye");
        });
    }

    @Test
    public void testWithNegativeIndexFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ParserTokenNodeName.with(-1);
        });
    }

    @Test
    public void testWithIndex() {
        final ParserTokenNodeName name = ParserTokenNodeName.with(1);
        this.checkValue(name, "1");
    }

    @Override
    public ParserTokenNodeName createName(final String name) {
        return ParserTokenNodeName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "Hello";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "A";
    }

    @Override
    public Class<ParserTokenNodeName> type() {
        return ParserTokenNodeName.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
