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

final class SearchAndQuery extends SearchBinaryQuery {

    static SearchAndQuery with(final SearchQuery left, final SearchQuery right) {
        check(left, right);

        return new SearchAndQuery(left, right);
    }

    private SearchAndQuery(final SearchQuery left, final SearchQuery right) {
        super(left, right);
    }

    @Override
    boolean test(final SearchBigDecimalNode node) {
        return this.left.test(node) &&
                this.right.test(node);
    }

    @Override
    boolean test(final SearchBigIntegerNode node) {
        return this.left.test(node) &&
                this.right.test(node);
    }

    @Override
    boolean test(final SearchDoubleNode node) {
        return this.left.test(node) &&
                this.right.test(node);
    }

    @Override
    boolean test(final SearchLocalDateNode node) {
        return this.left.test(node) &&
                this.right.test(node);
    }

    @Override
    boolean test(final SearchLocalDateTimeNode node) {
        return this.left.test(node) &&
                this.right.test(node);
    }

    @Override
    boolean test(final SearchLocalTimeNode node) {
        return this.left.test(node) &&
                this.right.test(node);
    }

    @Override
    boolean test(final SearchLongNode node) {
        return this.left.test(node) &&
                this.right.test(node);
    }

    @Override
    boolean test(final SearchTextNode node) {
        return this.left.test(node) &&
                this.right.test(node);
    }

    @Override
    String toStringBinaryOperator() {
        return "&&";
    }
}
