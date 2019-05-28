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

import walkingkooka.color.Hsv;
import walkingkooka.tree.search.SearchNode;

import java.util.Objects;

/**
 * The parser token for a hsv with the value contained within a {@link Hsv}
 */
public final class HsvParserToken extends ParserToken2<Hsv> implements LeafParserToken<Hsv> {

    public static HsvParserToken with(final Hsv value, final String text) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(text, "text");

        return new HsvParserToken(value, text);
    }

    private HsvParserToken(final Hsv value, final String text) {
        super(value, text);
    }

    @Override
    public HsvParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    HsvParserToken replaceText(final String text) {
        return with(this.value(), text);
    }

    @Override
    public void accept(final ParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof HsvParserToken;
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
