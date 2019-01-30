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


import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A {@link LocalDateTime} number value.
 */
public final class ExpressionLocalDateTimeNode extends ExpressionValueNode<LocalDateTime> {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionLocalDateTimeNode.class);

    static ExpressionLocalDateTimeNode with(final LocalDateTime value) {
        Objects.requireNonNull(value, "value");
        return new ExpressionLocalDateTimeNode(NO_INDEX, value);
    }

    private ExpressionLocalDateTimeNode(final int index, final LocalDateTime value){
        super(index, value);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    ExpressionLocalDateTimeNode wrap1(final int index, final LocalDateTime value) {
        return new ExpressionLocalDateTimeNode(index, value);
    }

    @Override
    public boolean isBigDecimal() {
        return false;
    }

    @Override
    public boolean isBigInteger() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public boolean isLocalDate() {
        return false;
    }

    @Override
    public boolean isLocalDateTime() {
        return true;
    }

    @Override
    public boolean isLocalTime() {
        return false;
    }

    @Override
    public boolean isLong() {
        return false;
    }

    @Override
    public boolean isReference() {
        return false;
    }

    @Override
    public boolean isText() {
        return false;
    }

    @Override
    public void accept(final ExpressionNodeVisitor visitor){
        visitor.visit(this);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionLocalDateTimeNode;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.value);
    }
}
