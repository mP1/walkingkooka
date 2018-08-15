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
 * Represents a function with zero or more parameters.
 */
public final class ExpressionFunctionNode extends ExpressionVariableNode {

    /**
     * Creates a new {@link ExpressionFunctionNode}
     */
    static ExpressionFunctionNode with(final ExpressionNodeName name, final List<ExpressionNode> expressions) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(expressions, "expressions");

        return new ExpressionFunctionNode(NO_PARENT_INDEX, name, expressions);
    }

    /**
     * Private ctor
     */
    private ExpressionFunctionNode(final int index, ExpressionNodeName name, final List<ExpressionNode> expressions) {
        super(index, expressions);
        this.name = name;
    }

    @Override
    ExpressionParentNode wrap0(final int index, final List<ExpressionNode> children) {
        return new ExpressionFunctionNode(index, this.name, children);
    }

    @Override
    public ExpressionNodeName name() {
        return this.name;
    }

    private final ExpressionNodeName name;

    public ExpressionFunctionNode setName(final ExpressionNodeName name) {
        Objects.requireNonNull(name, "name");
        return this.name().equals(name) ?
               this :
               new ExpressionFunctionNode(this.index, name, this.value());
    }

    @Override
    public ExpressionNode setChildren(final List<ExpressionNode> children) {
        return this.setChildren0(children).cast();
    }

    // Value.........................................................................................................

    @Override
    public List<ExpressionNode> value() {
        return this.children();
    }

    // Is.........................................................................................................

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
    public boolean isFunction() {
        return true;
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
    public boolean isNot() {
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

    // Visitor.........................................................................................................

    @Override
    public void accept(final ExpressionNodeVisitor visitor){
        if(Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Object.........................................................................................................

    @Override
    final boolean equalsIgnoringParentAndChildren(final ExpressionNode other) {
        return this.name.equals(other.name());
    }

    @Override
    void toString0(StringBuilder b) {
        b.append(this.name());
        b.append('(');

        final List<ExpressionNode> expressions = this.value();
        int last = expressions.size() - 1;
        for(ExpressionNode parameter : expressions) {
            parameter.toString0(b);
            last--;
            if(last >= 0) {
                b.append(',');
            }
        }

        b.append(')');
    }
}
