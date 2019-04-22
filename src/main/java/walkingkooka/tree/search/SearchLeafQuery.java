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

/**
 * Base class for all leaf query types.
 */
abstract class SearchLeafQuery<V extends SearchQueryValue> extends SearchQuery {

    SearchLeafQuery(final V value) {
        this.value = value;
    }

    final V value;

    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();
        this.toStringPrefix(b);
        b.append(this.value);
        return b.toString();
    }

    abstract void toStringPrefix(final StringBuilder b);
}
