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

package walkingkooka.tree.search;

import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;

import java.util.Objects;

/**
 * A query that may be used to search {@link SearchNode} for matches.
 */
public abstract class SearchQuery implements HashCodeEqualsDefined {

    static SearchQueryParentBinaryAnd and(final SearchQuery left, final SearchQuery right) {
        return SearchQueryParentBinaryAnd.with(left, right);
    }

    /**
     * {@see SearchQueryLeafAttributeValueContains}
     */
    static SearchQueryLeafAttributeValueContains attributeValueContains(final SearchTextQueryValue value, final SearchNodeAttributeName attributeName, final CaseSensitivity caseSensitivity) {
        return SearchQueryLeafAttributeValueContains.with(value, attributeName, caseSensitivity);
    }

    /**
     * {@see SearchQueryLeafAttributeValueDoesntContains}
     */
    static SearchQueryLeafAttributeValueDoesntContains attributeValueDoesntContains(final SearchTextQueryValue value, final SearchNodeAttributeName attributeName, final CaseSensitivity caseSensitivity) {
        return SearchQueryLeafAttributeValueDoesntContains.with(value, attributeName, caseSensitivity);
    }

    /**
     * {@see SearchQueryLeafAttributeValueEquals}
     */
    static SearchQueryLeafAttributeValueEquals attributeValueEquals(final SearchTextQueryValue value, final SearchNodeAttributeName attributeName, final CaseSensitivity caseSensitivity) {
        return SearchQueryLeafAttributeValueEquals.with(value, attributeName, caseSensitivity);
    }

    /**
     * {@see SearchQueryLeafAttributeValueNotEquals}
     */
    static SearchQueryLeafAttributeValueNotEquals attributeValueNotEquals(final SearchTextQueryValue value, final SearchNodeAttributeName attributeName, final CaseSensitivity caseSensitivity) {
        return SearchQueryLeafAttributeValueNotEquals.with(value, attributeName, caseSensitivity);
    }

    static SearchQueryLeafValueContains contains(final SearchTextQueryValue value, final CaseSensitivity caseSensitivity) {
        return SearchQueryLeafValueContains.with(value, caseSensitivity);
    }

    static SearchQueryLeafValueEquals equalsQuery(final SearchQueryValue value, final SearchQueryTester tester) {
        return SearchQueryLeafValueEquals.with(value, tester);
    }

    static SearchQueryLeafValueGreaterThanEquals greaterThanEquals(final SearchQueryValue value, final SearchQueryTester tester) {
        return SearchQueryLeafValueGreaterThanEquals.with(value, tester);
    }

    static SearchQueryLeafValueGreaterThan greaterThan(final SearchQueryValue value, final SearchQueryTester tester) {
        return SearchQueryLeafValueGreaterThan.with(value, tester);
    }

    static SearchQueryLeafValueLessThanEquals lessThanEquals(final SearchQueryValue value, final SearchQueryTester tester) {
        return SearchQueryLeafValueLessThanEquals.with(value, tester);
    }

    static SearchQueryLeafValueLessThan lessThan(final SearchQueryValue value, final SearchQueryTester tester) {
        return SearchQueryLeafValueLessThan.with(value, tester);
    }

    static SearchQueryParentUnaryNot not(final SearchQuery query) {
        return SearchQueryParentUnaryNot.with(query);
    }

    static SearchQueryLeafValueNotEquals notEquals(final SearchQueryValue value, final SearchQueryTester tester) {
        return SearchQueryLeafValueNotEquals.with(value, tester);
    }

    static SearchQueryParentBinaryOr or(final SearchQuery left, final SearchQuery right) {
        return SearchQueryParentBinaryOr.with(left, right);
    }

    /**
     * Package private to limit sub classing.
     */
    SearchQuery() {
        super();
    }

    public final SearchQuery and(final SearchQuery query) {
        return and(this, query);
    }

    public final SearchQuery or(final SearchQuery query) {
        return or(this, query);
    }

    public abstract SearchQuery not();

    /**
     * Searches the nodes starting at {@link SearchNode} using this query returning a new nodes with {@link SearchSelectNode} to select matches.
     */
    public final SearchNode select(final SearchNode node) {
        Objects.requireNonNull(node, "node");

        final SearchQueryContext context = SearchQueryContext2.with(node);
        node.select(this, context);
        return context.finish();
    }

    abstract void visit(final SearchBigDecimalNode node, final SearchQueryContext context);

    abstract void visit(final SearchBigIntegerNode node, final SearchQueryContext context);

    abstract void visit(final SearchDoubleNode node, final SearchQueryContext context);

    abstract void visit(final SearchLocalDateNode node, final SearchQueryContext context);

    abstract void visit(final SearchLocalDateTimeNode node, final SearchQueryContext context);

    abstract void visit(final SearchLocalTimeNode node, final SearchQueryContext context);

    abstract void visit(final SearchLongNode node, final SearchQueryContext context);

    abstract void visit(final SearchMetaNode node, final SearchQueryContext context);

    abstract void visit(final SearchTextNode node, final SearchQueryContext context);

    @Override
    abstract public int hashCode();

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    abstract boolean equals0(final SearchQuery other);
}
