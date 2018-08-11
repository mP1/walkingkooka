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

import org.junit.Test;
import walkingkooka.naming.NameTestCase;

import static org.junit.Assert.assertEquals;

public final class ParserTokenNodeNameTest extends NameTestCase<ParserTokenNodeName> {

    @Test(expected = NullPointerException.class)
    public void testFromClassNullFails() {
        ParserTokenNodeName.fromClass(null);
    }

    @Test
    public void testFromClass() {
        final ParserTokenNodeName name = ParserTokenNodeName.fromClass(StringParserToken.class);
        assertEquals("name.value", "String", name.value());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullFails() {
        ParserTokenNodeName.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyFails() {
        ParserTokenNodeName.with("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidInitialFails() {
        ParserTokenNodeName.with("1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidPartFails() {
        ParserTokenNodeName.with("Hello Goodbye");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNegativeIndexFails() {
        ParserTokenNodeName.with(-1);
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("Hello");
    }

    @Test
    public void testWithIndex() {
        final ParserTokenNodeName name = ParserTokenNodeName.with(1);
        this.checkValue(name, "1");
    }

    @Test
    public void testToString() {
        final ParserTokenNodeName name = ParserTokenNodeName.with("Hello");
        assertEquals("name.value", "Hello", name.toString());
    }

    @Override
    protected ParserTokenNodeName createName(final String name) {
        return ParserTokenNodeName.with(name);
    }

    @Override
    protected Class<ParserTokenNodeName> type() {
        return ParserTokenNodeName.class;
    }
}
