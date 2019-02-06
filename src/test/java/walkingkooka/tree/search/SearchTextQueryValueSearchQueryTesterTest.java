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

public final class SearchTextQueryValueSearchQueryTesterTest extends SearchComparableQueryValueSearchQueryTesterTestCase<SearchTextQueryValueSearchQueryTester, String> {

    private final static CaseSensitivity SENSITIVITY = CaseSensitivity.SENSITIVE;

    @Test
    public void testDifferentCaseSensitivity() {
        this.checkNotEquals(SearchTextQueryValueSearchQueryTester.with(this.value(), SENSITIVITY.invert(), this.predicate()));
    }

    @Override
    SearchTextQueryValueSearchQueryTester createSearchQueryTester(final String value,
                                                                  final SearchQueryValueSearchQueryTesterComparisonPredicate predicate) {
        return SearchTextQueryValueSearchQueryTester.with(value, SENSITIVITY, predicate);
    }

    @Override
    String value() {
        return "abc";
    }

    @Override
    String differentValue() {
        return "xyz";
    }

    @Override
    public Class<SearchTextQueryValueSearchQueryTester> type() {
        return SearchTextQueryValueSearchQueryTester.class;
    }
}
