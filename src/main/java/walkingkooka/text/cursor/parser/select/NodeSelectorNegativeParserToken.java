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

import walkingkooka.Cast;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

/**
 * Holds a unary negative token with an argument such as a number or function.
 */
public final class NodeSelectorNegativeParserToken extends NodeSelectorParentParserToken<NodeSelectorNegativeParserToken> {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(NodeSelectorNegativeParserToken.class);

    static NodeSelectorNegativeParserToken with(final List<ParserToken> value, final String text) {
        return new NodeSelectorNegativeParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text),
                WITHOUT_COMPUTE_REQUIRED);
    }

    private NodeSelectorNegativeParserToken(final List<ParserToken> value, final String text, final List<ParserToken> valueWithout) {
        super(value, text, valueWithout);

        final List<ParserToken> without = Cast.to(NodeSelectorParentParserToken.class.cast(this.withoutSymbols().get()).value());
        final int count = without.size();
        if (1 != count) {
            throw new IllegalArgumentException("Expected 1 tokens but got " + count + "=" + without);
        }

        this.parameter = without.get(0).cast();
    }

    public NodeSelectorParserToken parameter() {
        return this.parameter;
    }

    private final NodeSelectorParserToken parameter;

    @Override
    public NodeSelectorNegativeParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    NodeSelectorNegativeParserToken replaceText(final String text) {
        return new NodeSelectorNegativeParserToken(this.value, text, this.valueIfWithoutSymbolsOrNull());
    }

    @Override
    public NodeSelectorNegativeParserToken setValue(final List<ParserToken> value) {
        return this.setValue0(value).cast();
    }

    @Override
    NodeSelectorParentParserToken replaceValue(final List<ParserToken> tokens, final List<ParserToken> without) {
        return new NodeSelectorNegativeParserToken(tokens, this.text(), without);
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
        return false;
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
        return true;
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
        return other instanceof NodeSelectorNegativeParserToken;
    }
}