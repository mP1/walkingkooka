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

public abstract class SearchQueryTesterEqualityTestCase<T extends SearchQueryTester<V>, V> extends HashCodeEqualsDefinedEqualityTestCase<T> {

    @Test
    @Ignore
    public final void testHashCodeAndEqualsInPairs() {
        throw new UnsupportedOperationException();
    }

    public final T createObject() {
        return this.createSearchQueryTester(this.value());
    }

    abstract T createSearchQueryTester(final V value);

    abstract V value();

    abstract V differentValue();
}
