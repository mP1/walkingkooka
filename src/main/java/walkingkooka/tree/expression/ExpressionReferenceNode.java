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

import java.util.Objects;

/**
 * A reference expression.
 */
public final class ExpressionReferenceNode extends ExpressionLeafValueNode<ExpressionReference> {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionReferenceNode.class);

    static ExpressionReferenceNode with(final ExpressionReference reference){
        Objects.requireNonNull(reference, "reference");

        return new ExpressionReferenceNode(NO_PARENT_INDEX, reference);
    }

    private ExpressionReferenceNode(final int index, final ExpressionReference reference){
        super(index, reference);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    ExpressionReferenceNode wrap1(final int index, final ExpressionReference value) {
        return new ExpressionReferenceNode(index, value);
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public boolean isText() {
        return false;
    }

    @Override
    public void accept(final ExpressionNodeVisitor visitor){
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionReferenceNode;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.value);
    }
}
