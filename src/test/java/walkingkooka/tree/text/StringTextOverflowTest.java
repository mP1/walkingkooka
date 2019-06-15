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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class StringTextOverflowTest extends TextOverflowTestCase<StringTextOverflow> {

    private final static String TEXT = "abc123";

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            StringTextOverflow.with(null);
        });
    }

    @Test
    public void testWithEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            StringTextOverflow.with("");
        });
    }

    @Test
    public void testWith() {
        final StringTextOverflow textOverflow = StringTextOverflow.with(TEXT);
        assertEquals(Optional.of(TEXT), textOverflow.value());
    }

    @Test
    public void testIsClip() {
        assertEquals(false, StringTextOverflow.with(TEXT).isClip());
    }

    @Test
    public void testIsEllipse() {
        assertEquals(false, StringTextOverflow.with(TEXT).isEllipse());
    }

    @Test
    public void testIsString() {
        assertEquals(true, StringTextOverflow.with(TEXT).isString());
    }

    @Test
    public void testDifferentEllipsis() {
        this.checkNotEquals(StringTextOverflow.with("abc123"), TextOverflow.ELLIPSIS);
    }

    @Test
    public void testDifferentString() {
        this.checkNotEquals(StringTextOverflow.with("abc123"), StringTextOverflow.with("different"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createTextStylePropertyValue(), CharSequences.quoteAndEscape(TEXT).toString());
    }

    @Test
    public void testFromJsonNode() {
        this.fromJsonNodeAndCheck(JsonNode.string("string-abc123"), this.createTextStylePropertyValue());
    }

    @Test
    public void testToJsonNode() {
        this.toJsonNodeAndCheck(this.createTextStylePropertyValue(), JsonNode.string("string-abc123"));
    }

    @Override
    TextOverflow createTextStylePropertyValue() {
        return StringTextOverflow.with(TEXT);
    }

    @Override
    Class<StringTextOverflow> textOverflowType() {
        return StringTextOverflow.class;
    }
}
