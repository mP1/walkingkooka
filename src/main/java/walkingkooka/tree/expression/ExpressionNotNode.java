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
import java.util.Objects;

/**
 * A not expression.
 */
public final class ExpressionNotNode extends ExpressionUnaryNode {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionNotNode.class);

    static ExpressionNotNode with(final ExpressionNode value){
        Objects.requireNonNull(value, "value");
        return new ExpressionNotNode(NO_PARENT_INDEX, value);
    }

    private ExpressionNotNode(final int index, final ExpressionNode value){
        super(index, value);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    public ExpressionNotNode setChildren(final List<ExpressionNode> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    ExpressionNotNode wrap1(final int index, final ExpressionNode expression) {
        return new ExpressionNotNode(index, expression);
    }

    @Override
    public boolean isNegative() {
        return false;
    }

    @Override
    public boolean isNot() {
        return true;
    }

    @Override
    public void accept(final ExpressionNodeVisitor visitor){
        if(Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionNotNode;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append('!');
        this.value().toString0(b);
    }
}
