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

import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;

public final class PaddingBottom extends Padding {

    public static PaddingBottom parse(final String text) {
        return with(Length.parse(text));
    }

    public static PaddingBottom with(final Length<?> length) {
        check(length);
        length.normalOrPixelOrFail();

        return new PaddingBottom(length);
    }

    private PaddingBottom(final Length<?> length) {
        super(length);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof PaddingBottom;
    }

    // HasJsonNode.....................................................................................................

    public static PaddingBottom fromJsonNode(final JsonNode node) {
        return with(Length.fromJsonNode(node));
    }

    static {
        HasJsonNode.register("padding-bottom",
                PaddingBottom::fromJsonNode,
                PaddingBottom.class);
    }
}
