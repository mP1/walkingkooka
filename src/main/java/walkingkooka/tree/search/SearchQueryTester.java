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

import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefined;

abstract class SearchQueryTester<T> implements HashCodeEqualsDefined {

    SearchQueryTester(final T value) {
        super();
        this.value = value;
    }

    abstract SearchQueryTester not();

    abstract boolean test(final SearchBigDecimalNode node);

    abstract boolean test(final SearchBigIntegerNode node);

    abstract boolean test(final SearchDoubleNode node);

    abstract boolean test(final SearchLocalDateNode node);

    abstract boolean test(final SearchLocalDateTimeNode node);

    abstract boolean test(final SearchLocalTimeNode node);

    abstract boolean test(final SearchLongNode node);

    abstract boolean test(final SearchTextNode node);

    final T value;

    @Override
    public final int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final SearchQueryTester other) {
        return this.value.equals(other.value) &&
                this.equals1(other);
    }

    abstract boolean equals1(final SearchQueryTester other);
}
