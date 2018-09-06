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

import org.junit.Test;

public abstract class SearchComparableQueryValueSearchQueryTesterEqualityTestCase<T extends SearchComparableQueryValueSearchQueryTester<V>, V extends Comparable<V>> extends SearchQueryTesterEqualityTestCase<T, V> {

    @Test
    public final void testDifferentResultPredicate() {
        this.checkNotEquals(this.createSearchQueryTester(this.value(), this.differentResult()));
    }

    @Override
    final T createSearchQueryTester(final V value) {
        return this.createSearchQueryTester(this.value(), this.result());
    }

    abstract T createSearchQueryTester(final V value, final SearchQueryValueSearchQueryTesterComparisonPredicate result);

    private SearchQueryValueSearchQueryTesterComparisonPredicate result() {
        return SearchQueryValueSearchQueryTesterComparisonPredicate.EQUALS;
    }

    private SearchQueryValueSearchQueryTesterComparisonPredicate differentResult() {
        return SearchQueryValueSearchQueryTesterComparisonPredicate.NOT_EQUALS;
    }
}
