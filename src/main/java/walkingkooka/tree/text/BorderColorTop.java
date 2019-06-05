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

import walkingkooka.color.Color;
import walkingkooka.color.ColorHslOrHsv;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;

public final class BorderColorTop extends BorderColor {

    public static BorderColorTop parse(final String text) {
        return with(Color.parse(text));
    }

    public static BorderColorTop with(final ColorHslOrHsv color) {
        check(color);

        return new BorderColorTop(color);
    }

    private BorderColorTop(final ColorHslOrHsv color) {
        super(color);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof BorderColorTop;
    }

    // HasJsonNode.....................................................................................................

    public static BorderColorTop fromJsonNode(final JsonNode node) {
        return with(ColorHslOrHsv.fromJsonNode(node));
    }

    static {
        HasJsonNode.register("border-color-top",
                BorderColorTop::fromJsonNode,
                BorderColorTop.class);
    }
}
