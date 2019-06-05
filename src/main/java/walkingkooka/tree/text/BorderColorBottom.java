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

public final class BorderColorBottom extends BorderColor {

    public static BorderColorBottom parse(final String text) {
        return with(Color.parse(text));
    }

    public static BorderColorBottom with(final ColorHslOrHsv color) {
        check(color);

        return new BorderColorBottom(color);
    }

    private BorderColorBottom(final ColorHslOrHsv color) {
        super(color);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof BorderColorBottom;
    }

    // HasJsonNode.....................................................................................................

    public static BorderColorBottom fromJsonNode(final JsonNode node) {
        return with(Color.fromJsonNode(node));
    }

    static {
        HasJsonNode.register("border-color-bottom",
                BorderColorBottom::fromJsonNode,
                BorderColorBottom.class);
    }
}
