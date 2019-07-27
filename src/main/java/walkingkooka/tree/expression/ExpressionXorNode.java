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

package walkingkooka.tree.expression;

import walkingkooka.tree.json.JsonNode;
import walkingkooka.visit.Visiting;

import java.math.BigInteger;
import java.util.List;

/**
 * A xor expression.
 */
public final class ExpressionXorNode extends ExpressionLogicalBinaryNode {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionXorNode.class);

    public final static String SYMBOL = "^";

    static ExpressionXorNode with(final ExpressionNode left, final ExpressionNode right) {
        check(left, right);
        return new ExpressionXorNode(NO_INDEX, left, right);
    }

    private ExpressionXorNode(final int index, final ExpressionNode left, final ExpressionNode right) {
        super(index, left, right);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    public ExpressionXorNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public ExpressionXorNode setChildren(final List<ExpressionNode> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    ExpressionXorNode replace1(final int index, final ExpressionNode left, final ExpressionNode right) {
        return new ExpressionXorNode(index, left, right);
    }

    // is .........................................................................................................

    @Override
    public boolean isAnd() {
        return false;
    }

    @Override
    public boolean isOr() {
        return false;
    }

    @Override
    public boolean isXor() {
        return true;
    }

    // Visitor .........................................................................................................

    @Override
    public void accept(final ExpressionNodeVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // evaluation .....................................................................................................

    @Override
    ExpressionNode applyBigInteger(final BigInteger left, final BigInteger right, final ExpressionEvaluationContext context) {
        return ExpressionNode.bigInteger(left.xor(right));
    }

    @Override
    boolean applyBoolean0(final boolean left, final boolean right) {
        return left ^ right;
    }

    @Override
    ExpressionNode applyLong(final long left, final long right, final ExpressionEvaluationContext context) {
        return ExpressionNode.longNode(left ^ right);
    }

    // HasJsonNode....................................................................................................

    // @VisibleForTesting
    static ExpressionXorNode fromJsonNode(final JsonNode node) {
        return fromJsonNode0(node, ExpressionXorNode::with);
    }

    static {
        register(SYMBOL, ExpressionXorNode::fromJsonNode, ExpressionXorNode.class);
    }

    // Object ........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionXorNode;
    }

    @Override
    void appendSymbol(final StringBuilder b) {
        b.append(SYMBOL);
    }
}
