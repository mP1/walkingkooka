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
import walkingkooka.tree.json.JsonNode;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NonStringTextOverflowTest extends TextOverflowTestCase<NonStringTextOverflow> {

    private final static String TEXT = "abc123";

    @Test
    public void testWith() {
        final StringTextOverflow textOverflow = StringTextOverflow.with(TEXT);
        assertEquals(Optional.of(TEXT), textOverflow.value());
    }

    @Test
    public void testIsClipClip() {
        assertEquals(true, TextOverflow.CLIP.isClip());
    }

    @Test
    public void testIsClipEllipse() {
        assertEquals(false, TextOverflow.ELLIPSIS.isClip());
    }

    @Test
    public void testIsEllipseClip() {
        assertEquals(false, TextOverflow.CLIP.isEllipse());
    }

    @Test
    public void testIsEllipseEllipse() {
        assertEquals(true, TextOverflow.ELLIPSIS.isEllipse());
    }

    @Test
    public void testIsString() {
        assertEquals(false, TextOverflow.ELLIPSIS.isString());
    }

    @Test
    public void testDifferent() {
        this.checkNotEquals(TextOverflow.CLIP, TextOverflow.ELLIPSIS);
    }

    @Test
    public void testToStringClip() {
        this.toStringAndCheck(TextOverflow.CLIP, "clip");
    }

    @Test
    public void testToStringEllipsis() {
        this.toStringAndCheck(TextOverflow.ELLIPSIS, "ellipsis");
    }

    @Test
    public void testFromJsonNodeClip() {
        this.fromJsonNodeAndCheck(JsonNode.string("clip"), TextOverflow.CLIP);
    }

    @Test
    public void testFromJsonNodeEllispsis() {
        this.fromJsonNodeAndCheck(JsonNode.string("ellipsis"), TextOverflow.ELLIPSIS);
    }

    @Test
    public void testToJsonNodeClip() {
        this.toJsonNodeAndCheck(TextOverflow.CLIP, JsonNode.string("clip"));
    }

    @Test
    public void testToJsonNodeEllipsis() {
        this.toJsonNodeAndCheck(TextOverflow.ELLIPSIS, JsonNode.string("ellipsis"));
    }

    @Override
    TextOverflow createTextStylePropertyValue() {
        return TextOverflow.CLIP;
    }

    @Override
    Class<NonStringTextOverflow> textOverflowType() {
        return NonStringTextOverflow.class;
    }
}
