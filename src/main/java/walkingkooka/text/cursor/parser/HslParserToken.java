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
package walkingkooka.text.cursor.parser;

import walkingkooka.color.Hsl;
import walkingkooka.tree.search.SearchNode;

import java.util.Objects;

/**
 * The parser token for a hsl with the value contained within a {@link Hsl}
 */
public final class HslParserToken extends ParserToken2<Hsl> implements LeafParserToken<Hsl> {

    public static HslParserToken with(final Hsl value, final String text) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(text, "text");

        return new HslParserToken(value, text);
    }

    private HslParserToken(final Hsl value, final String text) {
        super(value, text);
    }

    @Override
    public void accept(final ParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof HslParserToken;
    }

    @Override
    boolean equals1(final ParserToken2<?> other) {
        return true; // no extra properties to compare
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