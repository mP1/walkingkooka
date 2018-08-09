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

package walkingkooka.tree.expression;

import walkingkooka.tree.visit.Visiting;

import java.util.List;

public final class ExpressionLessThanEqualsNode extends ExpressionBinaryNode {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionLessThanEqualsNode.class);

    static ExpressionLessThanEqualsNode with(final ExpressionNode left, final ExpressionNode right){
        check(left, right);
        return new ExpressionLessThanEqualsNode(NO_PARENT_INDEX, left, right);
    }

    private ExpressionLessThanEqualsNode(final int index, final ExpressionNode left, final ExpressionNode right){
        super(index, left, right);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    public ExpressionLessThanEqualsNode setChildren(final List<ExpressionNode> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    ExpressionLessThanEqualsNode wrap1(final int index, final ExpressionNode left, final ExpressionNode right) {
        return new ExpressionLessThanEqualsNode(index, left, right);
    }

    // is .........................................................................................................

    @Override
    public boolean isAddition() {
        return false;
    }

    @Override
    public boolean isAnd() {
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
    public boolean isGreaterThan() {
        return false;
    }

    @Override
    public boolean isGreaterThanEquals() {
        return false;
    }

    @Override
    public boolean isLessThan() {
        return false;
    }

    @Override
    public boolean isLessThanEquals() {
        return true;
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
    public boolean isNotEquals() {
        return false;
    }

    @Override
    public boolean isOr() {
        return false;
    }

    @Override
    public boolean isPower() {
        return false;
    }

    @Override
    public boolean isSubtraction() {
        return false;
    }

    @Override
    public boolean isXor() {
        return false;
    }

    // Visitor .........................................................................................................

    @Override
    public void accept(final ExpressionNodeVisitor visitor){
        if(Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionLessThanEqualsNode;
    }

    @Override
    void appendSymbol(final StringBuilder b) {
        b.append('&');
    }
}
