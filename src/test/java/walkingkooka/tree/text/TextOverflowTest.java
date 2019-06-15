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
import walkingkooka.test.ClassTesting2;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.JavaVisibility;

public final class TextOverflowTest implements ClassTesting2<TextOverflow>, HasJsonNodeTesting<TextOverflow> {

    @Test
    public void testClipJsonRoundtrip() {
        this.toJsonNodeRoundTripTwiceAndCheck(TextOverflow.CLIP);
    }

    @Test
    public void testEllipsisJsonRoundtrip() {
        this.toJsonNodeRoundTripTwiceAndCheck(TextOverflow.ELLIPSIS);
    }

    @Test
    public void testStringJsonRoundtrip() {
        this.toJsonNodeRoundTripTwiceAndCheck(TextOverflow.string("abc123"));
    }

    @Test
    public void testStringJsonRoundtrip2() {
        this.toJsonNodeRoundTripTwiceAndCheck(TextOverflow.string("clip"));
    }

    @Test
    public void testStringJsonRoundtrip3() {
        this.toJsonNodeRoundTripTwiceAndCheck(TextOverflow.string("ellipsis"));
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TextOverflow> type() {
        return TextOverflow.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public TextOverflow fromJsonNode(final JsonNode from) {
        return TextOverflow.fromJsonNode(from);
    }

    @Override
    public TextOverflow createHasJsonNode() {
        return TextOverflow.string("hello-123");
    }
}
