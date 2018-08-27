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

abstract class SearchQueryTester<T> {

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
}
