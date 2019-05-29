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
package walkingkooka.text.cursor.parser.color;

import walkingkooka.color.Color;
import walkingkooka.text.cursor.parser.LeafParserToken;
import walkingkooka.tree.search.SearchNode;

import java.util.Objects;

/**
 * The parser token for a color with the value contained within a {@link Color}
 */
public final class ColorParserToken extends ColorHslOrHsvParserToken<Color>
        implements LeafParserToken<Color> {

    public static ColorParserToken with(final Color value, final String text) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(text, "text");

        return new ColorParserToken(value, text);
    }

    private ColorParserToken(final Color value, final String text) {
        super(value, text);
    }

    // ColorParserTokenVisitor .........................................................................................

    @Override
    public void accept(final ColorParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object.......... ...............................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ColorParserToken;
    }

    // HasSearchNode ...............................................................................................

    /**
     * Returns a {@link SearchNode} with the original text.
     */
    @Override
    public SearchNode toSearchNode() {
        final String text = this.text();
        return SearchNode.text(text, text);
    }
}
