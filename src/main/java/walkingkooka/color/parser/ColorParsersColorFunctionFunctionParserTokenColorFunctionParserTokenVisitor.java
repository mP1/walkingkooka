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

package walkingkooka.color.parser;

import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

/**
 * Accepts a parser token graph which is assumed to only contain {@link ColorFunctionParserToken tokens} and makes a {@link ColorFunctionFunctionParserToken}.
 */
final class ColorParsersColorFunctionFunctionParserTokenColorFunctionParserTokenVisitor extends ColorFunctionParserTokenVisitor {

    static ColorFunctionFunctionParserToken transform(final ParserToken token, final ParserContext context) {
        final ColorParsersColorFunctionFunctionParserTokenColorFunctionParserTokenVisitor visitor = new ColorParsersColorFunctionFunctionParserTokenColorFunctionParserTokenVisitor();
        visitor.accept(token);
        return ColorFunctionParserToken.function(visitor.tokens, ParserToken.text(visitor.tokens));
    }

    ColorParsersColorFunctionFunctionParserTokenColorFunctionParserTokenVisitor() {
        super();
    }

    // ColorFunctionParserTokenVisitor..................................................................................

    @Override 
    protected void visit(final ColorFunctionDegreesUnitSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final ColorFunctionFunctionNameParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final ColorFunctionNumberParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final ColorFunctionParenthesisCloseSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final ColorFunctionParenthesisOpenSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final ColorFunctionPercentageParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final ColorFunctionSeparatorSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final ColorFunctionWhitespaceParserToken token) {
        this.add(token);
    }

    // helpers..........................................................................................................

    private void add(final ColorFunctionParserToken token) {
        this.tokens.add(token);
    }

    private final List<ParserToken> tokens = Lists.array();

    @Override
    public String toString() {
        return this.tokens.toString();
    }
}
