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
package walkingkooka.text.cursor.parser.select;

import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

/**
 * A parser token that holds an entire expression.
 */
public final class NodeSelectorExpressionParserToken extends NodeSelectorParentParserToken<NodeSelectorExpressionParserToken> {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(NodeSelectorExpressionParserToken.class);

    static NodeSelectorExpressionParserToken with(final List<ParserToken> value, final String text) {
        return new NodeSelectorExpressionParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text),
                WITHOUT_COMPUTE_REQUIRED);
    }

    private NodeSelectorExpressionParserToken(final List<ParserToken> value, final String text, final List<ParserToken> valueWithout) {
        super(value, text, valueWithout);
    }

    @Override
    public NodeSelectorExpressionParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    NodeSelectorExpressionParserToken replaceText(final String text) {
        return new NodeSelectorExpressionParserToken(this.value, text, this.valueIfWithoutSymbolsOrNull());
    }

    @Override
    public NodeSelectorExpressionParserToken setValue(final List<ParserToken> value) {
        return this.setValue0(value).cast();
    }

    @Override
    NodeSelectorParentParserToken replaceValue(final List<ParserToken> tokens, final List<ParserToken> without) {
        return new NodeSelectorExpressionParserToken(tokens, this.text(), without);
    }

    // name................................................................................................

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    // is................................................................................................

    @Override
    public boolean isAddition() {
        return false;
    }

    @Override
    public boolean isAnd() {
        return false;
    }

    @Override
    public boolean isAttribute() {
        return false;
    }

    @Override
    public boolean isDivision() {
        return false;
    }

    @Override
    public boolean isEquals() {
        return false;
    }

    @Override
    public boolean isExpression() {
        return true;
    }

    @Override
    public boolean isFunction() {
        return false;
    }

    @Override
    public boolean isGreaterThan() {
        return false;
    }

    @Override
    public boolean isGreaterThanEquals() {
        return false;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public boolean isLessThan() {
        return false;
    }

    @Override
    public boolean isLessThanEquals() {
        return false;
    }

    @Override
    public boolean isModulo() {
        return false;
    }

    @Override
    public boolean isMultiplication() {
        return false;
    }

    @Override
    public boolean isNegative() {
        return false;
    }

    @Override
    public boolean isNotEquals() {
        return false;
    }

    @Override
    public boolean isOr() {
        return false;
    }

    @Override
    public boolean isPredicate() {
        return false;
    }

    @Override
    public boolean isSubtraction() {
        return false;
    }

    // Visitor........................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Object........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorExpressionParserToken;
    }
}
