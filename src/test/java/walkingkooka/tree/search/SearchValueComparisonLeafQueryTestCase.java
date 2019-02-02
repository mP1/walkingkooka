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

import org.junit.jupiter.api.Test;
import walkingkooka.text.CaseSensitivity;

public abstract class SearchValueComparisonLeafQueryTestCase<Q extends SearchValueComparisonLeafQuery> extends SearchLeafQueryTestCase<Q> {

    SearchValueComparisonLeafQueryTestCase() {
        super();
    }

    @Test
    public final void testEqualsDifferentQueryValue() {
        this.checkNotEquals(this.createSearchQuery(this.textQueryValue("different"), this.searchQueryTester()));
    }

    @Test
    public final void testEqualsDifferentSearchQueryTester() {
        this.checkNotEquals(this.createSearchQuery(this.queryValue(), this.differentSearchQueryTester()));
    }

    final Q createSearchQuery() {
        return this.createSearchQuery(this.queryValue(), this.searchQueryTester());
    }

    final SearchTextQueryValue queryValue() {
        return this.textQueryValue("value");
    }

    final SearchQueryTester searchQueryTester() {
        return SearchTextQueryValueSearchQueryTester.with(this.queryValue().text(), CaseSensitivity.SENSITIVE, this.predicate());
    }

    final SearchQueryTester differentSearchQueryTester() {
        return SearchTextQueryValueSearchQueryTester.with("different-text", CaseSensitivity.SENSITIVE, this.predicate());
    }

    abstract SearchQueryValueSearchQueryTesterComparisonPredicate predicate();

    abstract Q createSearchQuery(final SearchTextQueryValue value, final SearchQueryTester tester);
}
