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
 */
package walkingkooka.text.cursor.parser;

import walkingkooka.tree.search.SearchNode;

import java.util.Objects;

/**
 * The parser token that substitutes for a missing (optional) token.
 */
public final class MissingParserToken extends ParserTemplateToken<ParserTokenNodeName> {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(MissingParserToken.class);

    static MissingParserToken with(final ParserTokenNodeName value, final String text) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(text, "text");

        return new MissingParserToken(value, text);
    }

    private MissingParserToken(final ParserTokenNodeName value, final String text) {
        super(value, text);
    }

    @Override
    public MissingParserToken setText(final String text){
        return this.setText0(text).cast();
    }

    @Override
    MissingParserToken replaceText(final String text) {
        return with(this.value(), text);
    }

    @Override
    public ParserTokenNodeName name() {
        return this.value();
    }

    @Override
    public boolean isMissing() {
        return true;
    }

    @Override
    public boolean isNoise() {
        return true;
    }

    @Override
    public void accept(final ParserTokenVisitor visitor){
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof MissingParserToken;
    }

    @Override
    boolean equals1(final ParserTemplateToken<?> other) {
        return true; // no extra properties to compare
    }

    // HasSearchNode.....................................................................................................

    @Override
    public SearchNode toSearchNode() {
        final String text = this.text();
        return SearchNode.text(text, text);
    }
}
