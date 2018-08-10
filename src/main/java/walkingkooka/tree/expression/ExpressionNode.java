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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.naming.PathSeparator;
import walkingkooka.tree.Node;
import walkingkooka.tree.select.NodeSelectorBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class ExpressionNode implements Node<ExpressionNode, ExpressionNodeName, Name, Object> {

    public final static List<ExpressionNode> NO_CHILDREN = Lists.empty();

    /**
     * {@see ExpressionAdditionNode}
     */
    public static ExpressionAdditionNode addition(final ExpressionNode left, final ExpressionNode right){
        return ExpressionAdditionNode.with(left, right);
    }

    /**
     * {@see ExpressionAndNode}
     */
    public static ExpressionAndNode and(final ExpressionNode left, final ExpressionNode right){
        return ExpressionAndNode.with(left, right);
    }

    /**
     * {@see ExpressionBooleanNode}
     */
    public static ExpressionBooleanNode booleanNode(final boolean value) {
        return ExpressionBooleanNode.with(value);
    }

    /**
     * {@see ExpressionDivisionNode}
     */
    public static ExpressionDivisionNode division(final ExpressionNode left, final ExpressionNode right){
        return ExpressionDivisionNode.with(left, right);
    }

    /**
     * {@see ExpressionEqualsNode}
     */
    public static ExpressionEqualsNode equalsNode(final ExpressionNode left, final ExpressionNode right){
        return ExpressionEqualsNode.with(left, right);
    }
    
    /**
     * {@see ExpressionFunctionNode}
     */
    public static ExpressionFunctionNode function(final ExpressionNodeName name, final List<ExpressionNode> expressions) {
        return ExpressionFunctionNode.with(name, expressions);
    }

    /**
     * {@see ExpressionGreaterThanNode}
     */
    public static ExpressionGreaterThanNode greaterThan(final ExpressionNode left, final ExpressionNode right){
        return ExpressionGreaterThanNode.with(left, right);
    }

    /**
     * {@see ExpressionGreaterThanEqualsNode}
     */
    public static ExpressionGreaterThanEqualsNode greaterThanEquals(final ExpressionNode left, final ExpressionNode right){
        return ExpressionGreaterThanEqualsNode.with(left, right);
    }

    /**
     * {@see ExpressionGroupNode}
     */
    public static ExpressionGroupNode group(final ExpressionNode expression){
        return ExpressionGroupNode.with(expression);
    }

    /**
     * {@see ExpressionLessThanNode}
     */
    public static ExpressionLessThanNode lessThan(final ExpressionNode left, final ExpressionNode right){
        return ExpressionLessThanNode.with(left, right);
    }

    /**
     * {@see ExpressionLessThanEqualsNode}
     */
    public static ExpressionLessThanEqualsNode lessThanEquals(final ExpressionNode left, final ExpressionNode right){
        return ExpressionLessThanEqualsNode.with(left, right);
    }
    
    /**
     * {@see ExpressionModuloNode}
     */
    public static ExpressionModuloNode modulo(final ExpressionNode left, final ExpressionNode right){
        return ExpressionModuloNode.with(left, right);
    }

    /**
     * {@see ExpressionMultiplicationNode}
     */
    public static ExpressionMultiplicationNode multiplication(final ExpressionNode left, final ExpressionNode right){
        return ExpressionMultiplicationNode.with(left, right);
    }

    /**
     * {@see ExpressionNegativeNode}
     */
    public static ExpressionNegativeNode negative(final ExpressionNode expression){
        return ExpressionNegativeNode.with(expression);
    }

    /**
     * {@see ExpressionNotNode}
     */
    public static ExpressionNotNode not(final ExpressionNode expression){
        return ExpressionNotNode.with(expression);
    }

    /**
     * {@see ExpressionNotEqualsNode}
     */
    public static ExpressionNotEqualsNode notEquals(final ExpressionNode left, final ExpressionNode right){
        return ExpressionNotEqualsNode.with(left, right);
    }

    /**
     * {@see ExpressionNumberNode}
     */
    public static ExpressionNumberNode number(final Number value) {
        return ExpressionNumberNode.with(value);
    }
    
    /**
     * {@see ExpressionOrNode}
     */
    public static ExpressionOrNode or(final ExpressionNode left, final ExpressionNode right){
        return ExpressionOrNode.with(left, right);
    }

    /**
     * {@see ExpressionPowerNode}
     */
    public static ExpressionPowerNode power(final ExpressionNode left, final ExpressionNode right){
        return ExpressionPowerNode.with(left, right);
    }

    /**
     * {@see ExpressionSubtractionNode}
     */
    public static ExpressionSubtractionNode subtraction(final ExpressionNode left, final ExpressionNode right){
        return ExpressionSubtractionNode.with(left, right);
    }

    /**
     * {@see ExpressionTextNode}
     */
    public static ExpressionTextNode text(final String value) {
        return ExpressionTextNode.with(value);
    }

    /**
     * {@see ExpressionXorNode}
     */
    public static ExpressionXorNode xor(final ExpressionNode left, final ExpressionNode right){
        return ExpressionXorNode.with(left, right);
    }
    
    /**
     * Absolute {@see NodeSelectorBuilder}
     */
    public static NodeSelectorBuilder<ExpressionNode, ExpressionNodeName, Name, Object> absoluteNodeSelectorBuilder() {
        return NodeSelectorBuilder.absolute(PathSeparator.notRequiredAtStart('/'));
    }

    /**
     * Relative {@see NodeSelectorBuilder}
     */
    public static NodeSelectorBuilder<ExpressionNode, ExpressionNodeName, Name, Object> relativeNodeSelectorBuilder() {
        return NodeSelectorBuilder.relative(PathSeparator.notRequiredAtStart('/'));
    }

    final static Optional<ExpressionNode> NO_PARENT = Optional.empty();
    final static int NO_PARENT_INDEX = -1;

    /**
     * Package private ctor to limit sub classing.
     */
    ExpressionNode(final int index) {
        this.parent = NO_PARENT;
        this.index = index;
    }

    // parent ..................................................................................................

    @Override
    public final Optional<ExpressionNode> parent() {
        return this.parent;
    }

    /**
     * This setter is used to recreate the entire graph including parents of parents receiving new children.
     * It is only ever called by a parent node and is used to adopt new children.
     */
    final ExpressionNode setParent(final Optional<ExpressionNode> parent, final int index) {
        final ExpressionNode copy = this.wrap(index);
        copy.parent = parent;
        return copy;
    }

    Optional<ExpressionNode> parent;

//    abstract ExpressionNode

    /**
     * Sub classes must create a new copy of the parent and replace the identified child using its index or similar,
     * and also sets its parent after creation, returning the equivalent child at the same index.
     */
    abstract ExpressionNode setChild(final ExpressionNode newChild);

    /**
     * Only ever called after during the completion of a setChildren, basically used to recreate the parent graph
     * containing this child.
     */
    final ExpressionNode replaceChild(final Optional<ExpressionNode> previousParent) {
        return previousParent.isPresent() ?
                previousParent.get()
                        .setChild(this)
                        .children()
                        .get(this.index()) :
                this;
    }

    // index........................................................................................................

    @Override
    public final int index() {
        return this.index;
    }

    int index;

    /**
     * Would be setter that returns an instance with the index, creating a new instance if necessary.
     */
    final ExpressionNode setIndex(final int index) {
        return this.index == index ?
                this :
                this.replaceIndex(index);
    }

    private ExpressionNode replaceIndex(final int index) {
        return this.wrap(index);
    }

    abstract ExpressionNode wrap(final int index);

    // attributes.......................................................................................................

    @Override
    public final Map<Name, Object> attributes() {
        return Maps.empty();
    }

    @Override
    public final ExpressionNode setAttributes(final Map<Name, Object> attributes) {
        throw new UnsupportedOperationException();
    }

    // is............................................................................................................

    /**
     * Only {@link ExpressionAdditionNode} returns true
     */
    public abstract boolean isAddition();

    /**
     * Only {@link ExpressionAndNode} returns true
     */
    public abstract boolean isAnd();

    /**
     * Only {@link ExpressionBooleanNode} returns true
     */
    public abstract boolean isBoolean();

    /**
     * Only {@link ExpressionDivisionNode} returns true
     */
    public abstract boolean isDivision();

    /**
     * Only {@link ExpressionEqualsNode} returns true
     */
    public abstract boolean isEquals();

    /**
     * Only {@link ExpressionFunctionNode} returns true
     */
    public abstract boolean isFunction();

    /**
     * Only {@link ExpressionGreaterThanNode} returns true
     */
    public abstract boolean isGreaterThan();
    
    /**
     * Only {@link ExpressionGreaterThanEqualsNode} returns true
     */
    public abstract boolean isGreaterThanEquals();

    /**
     * Only {@link ExpressionGroupNode} returns true
     */
    public abstract boolean isGroup();
    
    /**
     * Only {@link ExpressionLessThanNode} returns true
     */
    public abstract boolean isLessThan();

    /**
     * Only {@link ExpressionLessThanEqualsNode} returns true
     */
    public abstract boolean isLessThanEquals();

    /**
     * Only {@link ExpressionModuloNode} returns true
     */
    public abstract boolean isModulo();

    /**
     * Only {@link ExpressionMultiplicationNode} returns true
     */
    public abstract boolean isMultiplication();

    /**
     * Only {@link ExpressionNegativeNode} returns true
     */
    public abstract boolean isNegative();

    /**
     * Only {@link ExpressionNotNode} returns true
     */
    public abstract boolean isNot();

    /**
     * Only {@link ExpressionNotEqualsNode} returns true
     */
    public abstract boolean isNotEquals();

    /**
     * Only {@link ExpressionNumberNode} returns true
     */
    public abstract boolean isNumber();

    /**
     * Only {@link ExpressionOrNode} returns true
     */
    public abstract boolean isOr();

    /**
     * Only {@link ExpressionPowerNode} returns true
     */
    public abstract boolean isPower();

    /**
     * Only {@link ExpressionReferenceNode} returns true
     */
    public abstract boolean isReference();

    /**
     * Only {@link ExpressionSubtractionNode} returns true
     */
    public abstract boolean isSubtraction();

    /**
     * Only {@link ExpressionTextNode} returns true
     */
    public abstract boolean isText();

    /**
     * Only {@link ExpressionXorNode} returns true
     */
    public abstract boolean isXor();
    // helper............................................................................................................

    final <T extends ExpressionNode> T cast() {
        return Cast.to(this);
    }

    // Visitor............................................................................................................

    abstract void accept(final ExpressionNodeVisitor visitor);

    // Object .......................................................................................................

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final ExpressionNode other) {
        return this.equalsAncestors(other) &&
               this.equalsDescendants0(other);
    }

    private boolean equalsAncestors(final ExpressionNode other) {
        boolean result = this.equalsIgnoringParentAndChildren(other);

        if(result) {
            final Optional<ExpressionNode> parent = this.parent();
            final Optional<ExpressionNode> otherParent = other.parent();
            final boolean hasParent = parent.isPresent();
            final boolean hasOtherParent = otherParent.isPresent();

            if (hasParent) {
                if (hasOtherParent) {
                    result = parent.get().equalsAncestors(otherParent.get());
                }
            } else {
                // result is only true if other is false
                result = !hasOtherParent;
            }
        }

        return result;
    }

    final boolean equalsDescendants(final ExpressionNode other) {
        return this.equalsIgnoringParentAndChildren(other) &&
                this.equalsDescendants0(other);
    }

    abstract boolean equalsDescendants0(final ExpressionNode other);

    /**
     * Sub classes should do equals but ignore the parent and children properties.
     */
    abstract boolean equalsIgnoringParentAndChildren(final ExpressionNode other);

    // Object .......................................................................................................

    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();
        this.toString0(b);
        return b.toString();
    }

    abstract void toString0(final StringBuilder b);
}
