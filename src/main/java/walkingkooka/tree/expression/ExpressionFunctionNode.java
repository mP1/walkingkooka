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

import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.visit.Visiting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        return new ExpressionFunctionNode(NO_INDEX, name, expressions);
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
    public ExpressionFunctionNode removeParent() {
        return this.removeParent0().cast();
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

    // evaluation .....................................................................................................

    @Override
    public final BigDecimal toBigDecimal(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, BigDecimal.class);
    }

    @Override
    public final BigInteger toBigInteger(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, BigInteger.class);
    }

    @Override
    public final boolean toBoolean(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, Boolean.class);
    }

    @Override
    public final double toDouble(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, Double.class);
    }

    @Override
    public final LocalDate toLocalDate(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, LocalDate.class);
    }

    @Override
    public final LocalDateTime toLocalDateTime(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, LocalDateTime.class);
    }

    @Override
    public final LocalTime toLocalTime(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, LocalTime.class);
    }
    
    @Override
    public final long toLong(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, Long.class);
    }

    @Override
    public final Number toNumber(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, Number.class);
    }

    @Override
    public final String toText(final ExpressionEvaluationContext context) {
        return this.executeFunction(context, String.class);
    }

    @Override
    public final Object toValue(final ExpressionEvaluationContext context) {
        return this.executeFunction(context);
    }

    private <T> T executeFunction(final ExpressionEvaluationContext context, final Class<T> target) {
        return context.convert(this.executeFunction(context), target);
    }

    private Object executeFunction(final ExpressionEvaluationContext context) {
        return context.function(this.name(),
                this.value()
                        .stream()
                        .map(v -> v.toValue(context))
                        .collect(Collectors.toList()));
    }

    // HasJsonNode....................................................................................................

    // @VisibleForTesting
    static ExpressionFunctionNode fromJsonNode(final JsonNode node) {
        final JsonArrayNode array = node.arrayOrFail();

        return ExpressionFunctionNode.with(
                ExpressionNodeName.fromJsonNode(array.get(0)),
                array.get(1).fromJsonNodeWithTypeList());
    }

    /**
     * Converts all children into a {@link walkingkooka.tree.json.JsonArrayNode} with their types.
     */
    @Override
    public final JsonNode toJsonNode() {
        return JsonNode.array()
                .appendChild(this.name.toJsonNode())
                .appendChild(HasJsonNode.toJsonNodeWithTypeList(this.children()));
    }

    static {
        register("-fn", ExpressionFunctionNode::fromJsonNode, ExpressionFunctionNode.class);
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
