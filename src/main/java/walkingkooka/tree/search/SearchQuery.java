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

package walkingkooka.tree.search;

import java.util.Objects;

/**
 * A query that may be used to search {@link SearchNode} for matches.
 */
public abstract class SearchQuery {

    static SearchAndQuery and(final SearchQuery left, final SearchQuery right) {
        return SearchAndQuery.with(left, right);
    }

    static SearchEqualsQuery equalsQuery(final SearchQueryValue value, final SearchQueryTester tester) {
        return SearchEqualsQuery.with(value, tester);
    }

    static SearchGreaterThanEqualsQuery greaterThanEquals(final SearchQueryValue value, final SearchQueryTester tester) {
        return SearchGreaterThanEqualsQuery.with(value, tester);
    }

    static SearchGreaterThanQuery greaterThan(final SearchQueryValue value, final SearchQueryTester tester) {
        return SearchGreaterThanQuery.with(value, tester);
    }

    static SearchLessThanEqualsQuery lessThanEquals(final SearchQueryValue value, final SearchQueryTester tester) {
        return SearchLessThanEqualsQuery.with(value, tester);
    }

    static SearchLessThanQuery lessThan(final SearchQueryValue value, final SearchQueryTester tester) {
        return SearchLessThanQuery.with(value, tester);
    }

    static SearchNotQuery not(final SearchQuery query) {
        return SearchNotQuery.with(query);
    }

    static SearchNotEqualsQuery notEquals(final SearchQueryValue value, final SearchQueryTester tester) {
        return SearchNotEqualsQuery.with(value, tester);
    }

    static SearchOrQuery or(final SearchQuery left, final SearchQuery right) {
        return SearchOrQuery.with(left, right);
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
    public SearchNode select(final SearchNode node) {
        Objects.requireNonNull(node, "node");

        final SearchQueryContext context = SearchQueryContext.create();
        context.set(node);
        node.select(this, context);

        return context.finish();
    }

    final void visit(final SearchBigDecimalNode node, final SearchQueryContext context) {
        if(this.test(node)) {
            context.replace(node);
        }
    }

    final void visit(final SearchBigIntegerNode node, final SearchQueryContext context) {
        if(this.test(node)) {
            context.replace(node);
        }
    }

    final void visit(final SearchDoubleNode node, final SearchQueryContext context) {
        if(this.test(node)) {
            context.replace(node);
        }
    }

    final void visit(final SearchLocalDateNode node, final SearchQueryContext context) {
        if(this.test(node)) {
            context.replace(node);
        }
    }

    final void visit(final SearchLocalDateTimeNode node, final SearchQueryContext context) {
        if(this.test(node)) {
            context.replace(node);
        }
    }

    final void visit(final SearchLocalTimeNode node, final SearchQueryContext context) {
        if(this.test(node)) {
            context.replace(node);
        }
    }

    final void visit(final SearchLongNode node, final SearchQueryContext context) {
        if(this.test(node)) {
            context.replace(node);
        }
    }

    final void visit(final SearchTextNode node, final SearchQueryContext context) {
        if(this.test(node)) {
            context.replace(node);
        }
    }

    abstract boolean test(final SearchBigDecimalNode node);

    abstract boolean test(final SearchBigIntegerNode node);

    abstract boolean test(final SearchDoubleNode node);

    abstract boolean test(final SearchLocalDateNode node);

    abstract boolean test(final SearchLocalDateTimeNode node);

    abstract boolean test(final SearchLocalTimeNode node);

    abstract boolean test(final SearchLongNode node);

    abstract boolean test(final SearchTextNode node);
}
