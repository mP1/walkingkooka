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

import walkingkooka.text.CaseSensitivity;

import java.util.Objects;

/**
 * Searches the text of a {@link SearchQueryValue} if it contains text.
 */
final class SearchAttributeValueNotEqualsQuery extends SearchAttributeLeafQuery {

    static SearchAttributeValueNotEqualsQuery with(final SearchTextQueryValue value, final SearchNodeAttributeName attributeName, final CaseSensitivity caseSensitivity) {
        Objects.requireNonNull(attributeName, "attributeName");
        Objects.requireNonNull(caseSensitivity, "caseSensitivity");

        return new SearchAttributeValueNotEqualsQuery(value, attributeName, caseSensitivity);
    }

    private SearchAttributeValueNotEqualsQuery(final SearchTextQueryValue value, final SearchNodeAttributeName attributeName, final CaseSensitivity caseSensitivity) {
        super(value, attributeName, caseSensitivity);
    }

    @Override
    public SearchQuery not() {
        return SearchQuery.attributeValueEquals(this.value, this.attributeName, this.caseSensitivity);
    }

    @Override
    boolean test(final String text, final String attributeValue) {
        return false == this.caseSensitivity.equals(text, attributeValue);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SearchAttributeValueNotEqualsQuery;
    }

    @Override
    void toStringPrefix(final StringBuilder b) {
        b.append(this.attributeName).append('=').append(this.value.text());
    }
}
