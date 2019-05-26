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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TextTest extends TextLeafNodeTestCase<Text, String>{

    @Override
    public void testTypeNaming() {
    }

    @Test
    public void testWithEmpty() {
        this.createTextNodeAndCheck("");
    }

    @Test
    public void testWith2() {
        final String value = "abc123";
        final Text text = Text.with(value);
        assertEquals(value, text.text(), "text");
        assertEquals(value, text.value(), "value");
    }

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(Text.with("ABC123"));
    }

    @Test
    public void testToStringEmpty() {
        this.toStringAndCheck(Text.with(""), "\"\"");
    }

    @Test
    public void testToStringNotEmpty() {
        this.toStringAndCheck(Text.with("abc"), "\"abc\"");
    }

    @Test
    public void testToStringNotEmptyEscaping() {
        this.toStringAndCheck(Text.with("abc\tdef"), "\"abc\\tdef\"");
    }

    @Override
    Text createTextNode(final String value) {
        return Text.with(value);
    }

    @Override
    String value() {
        return "abc123";
    }

    @Override
    Class<Text> textNodeType() {
        return Text.class;
    }
}
