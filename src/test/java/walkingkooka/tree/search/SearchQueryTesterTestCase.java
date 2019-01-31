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
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.type.MemberVisibility;

public abstract class SearchQueryTesterTestCase<T extends SearchQueryTester<V>, V> extends ClassTestCase<T>
        implements HashCodeEqualsDefinedTesting<T> {

    SearchQueryTesterTestCase() {
        super();
    }

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(this.createSearchQueryTester(this.differentValue()));
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public final T createObject() {
        return this.createSearchQueryTester();
    }

    private T createSearchQueryTester() {
        return this.createSearchQueryTester(this.value());
    }

    abstract T createSearchQueryTester(final V value);

    abstract V value();

    abstract V differentValue();
}
