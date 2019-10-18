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

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;

public abstract class SearchQueryValueTestCase<Q extends SearchQueryValue, V> implements ClassTesting2<Q>,
        HashCodeEqualsDefinedTesting2<Q>,
        ToStringTesting<Q>,
        TypeNameTesting<Q> {

    @Test
    public final void testDifferentValue() {
        this.checkNotEquals(this.createSearchQueryValue(this.differentValue()));
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.createSearchQueryValue(this.value()),
                this.searchQueryValueToString());
    }

    final Q createSearchQueryValue() {
        return this.createSearchQueryValue(this.value());
    }

    abstract Q createSearchQueryValue(final V value);

    abstract V value();

    abstract V differentValue();

    abstract String searchQueryValueToString();

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public final Q createObject() {
        return this.createSearchQueryValue();
    }

    // TypeNameTesting .........................................................................................

    @Override
    public String typeNamePrefix() {
        return "Search";
    }

    @Override
    public final String typeNameSuffix() {
        return this.subtractTypeNamePrefix();
    }
}
