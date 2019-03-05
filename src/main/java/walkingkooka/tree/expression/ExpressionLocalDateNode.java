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


import walkingkooka.tree.json.JsonNode;

import java.time.LocalDate;
import java.util.Objects;

/**
 * A {@link LocalDate} value.
 */
public final class ExpressionLocalDateNode extends ExpressionValueNode<LocalDate> {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionLocalDateNode.class);

    static ExpressionLocalDateNode with(final LocalDate value) {
        Objects.requireNonNull(value, "value");
        return new ExpressionLocalDateNode(NO_INDEX, value);
    }

    private ExpressionLocalDateNode(final int index, final LocalDate value){
        super(index, value);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    ExpressionLocalDateNode wrap1(final int index, final LocalDate value) {
        return new ExpressionLocalDateNode(index, value);
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
        return true;
    }

    @Override
    public boolean isLocalDateTime() {
        return false;
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

    // HasJsonNode....................................................................................................

    // @VisibleForTesting
    static ExpressionLocalDateNode fromJsonNode(final JsonNode node) {
        return ExpressionLocalDateNode.with(node.fromJsonNode(LocalDate.class));
    }

    static {
        register("-local-date", ExpressionLocalDateNode::fromJsonNode, ExpressionLocalDateNode.class);
    }

    // Object ....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionLocalDateNode;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.value);
    }
}
