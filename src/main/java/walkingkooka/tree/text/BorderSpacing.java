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

import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;

public final class BorderSpacing extends LengthTextStylePropertyValue implements HasJsonNode {

    public static BorderSpacing parse(final String text) {
        return with(Length.parse(text));
    }

    public static BorderSpacing with(final Length<?> length) {
        check(length);
        length.normalOrPixelOrFail();

        return new BorderSpacing(length);
    }

    private BorderSpacing(final Length<?> length) {
        super(length);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof BorderSpacing;
    }

    // HasJsonNode.....................................................................................................

    public static BorderSpacing fromJsonNode(final JsonNode node) {
        return with(Length.fromJsonNode(node));
    }

    @Override
    public JsonNode toJsonNode() {
        return this.length.toJsonNode();
    }

    static {
        HasJsonNode.register("border-spacing",
                BorderSpacing::fromJsonNode,
                BorderSpacing.class);
    }
}
