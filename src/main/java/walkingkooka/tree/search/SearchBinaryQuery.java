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

import walkingkooka.Cast;

import java.util.Objects;

abstract class SearchBinaryQuery extends SearchParentQuery {

    static void check(final SearchQuery left, final SearchQuery right) {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
    }

    SearchBinaryQuery(final SearchQuery left, final SearchQuery right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public final SearchQuery not() {
        return SearchQuery.not(this);
    }

    @Override
    final void visit(final SearchBigDecimalNode node, final SearchQueryContext context) {
        this.left.visit(node, this.context(context));
    }

    @Override
    final void visit(final SearchBigIntegerNode node, final SearchQueryContext context) {
        this.left.visit(node, this.context(context));
    }

    @Override
    final void visit(final SearchDoubleNode node, final SearchQueryContext context) {
        this.left.visit(node, this.context(context));
    }

    @Override
    final void visit(final SearchLocalDateNode node, final SearchQueryContext context) {
        this.left.visit(node, this.context(context));
    }

    @Override
    final void visit(final SearchLocalDateTimeNode node, final SearchQueryContext context) {
        this.left.visit(node, this.context(context));
    }

    @Override
    final void visit(final SearchLocalTimeNode node, final SearchQueryContext context) {
        this.left.visit(node, this.context(context));
    }

    @Override
    final void visit(final SearchLongNode node, final SearchQueryContext context) {
        this.left.visit(node, this.context(context));
    }

    @Override
    final void visit(final SearchTextNode node, final SearchQueryContext context) {
        this.left.visit(node, this.context(context));
    }

    private final SearchQuery left;

    /**
     * Factory that creates the binary query custom {@link SearchQueryContext}.
     */
    abstract SearchBinaryQueryContext context(final SearchQueryContext context);

    final SearchQuery right;

    @Override
    public final int hashCode() {
        return Objects.hash(this.left, this.right);
    }

    @Override
    final boolean equals0(final SearchQuery other) {
        return this.equals1(Cast.to(other));
    }

    private boolean equals1(final SearchBinaryQuery other) {
        return this.left.equals(other.left) &&
                this.right.equals(other.right);
    }

    @Override
    public final String toString() {
        return this.left + this.toStringBinaryOperator() + this.right;
    }

    abstract String toStringBinaryOperator();
}
