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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;
import walkingkooka.text.CaseSensitivity;

public abstract class SearchQueryEqualityTestCase<Q extends SearchQuery> extends HashCodeEqualsDefinedEqualityTestCase<Q> {

    final static String DIFFERENT_TEXT = "different";

    @Test
    @Ignore
    public final void testHashCodeAndEqualsInPairs() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    public void testEqualsOnlyOverridesAbstractOrObject() {
        throw new UnsupportedOperationException();
    }

    public final Q createObject() {
        return this.createSearchQuery();
    }

    abstract Q createSearchQuery();

    final SearchTextQueryValue textQueryValue(final String value) {
        return SearchQueryValue.text(value);
    }

    final SearchQuery differentQuery() {
        return this.textQueryValue(DIFFERENT_TEXT).equalsQuery(CaseSensitivity.SENSITIVE);
    }
}
