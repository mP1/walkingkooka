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
import walkingkooka.text.CaseSensitivity;

import java.util.Objects;

/**
 * Base class for all leaf query types that operate on values and not attributes.
 */
abstract class SearchQueryLeafAttribute extends SearchQueryLeaf<SearchTextQueryValue> {

    SearchQueryLeafAttribute(final SearchTextQueryValue value, final SearchNodeAttributeName name, final CaseSensitivity caseSensitivity) {
        super(value);
        this.attributeName = name;
        this.caseSensitivity = caseSensitivity;
    }

    final SearchNodeAttributeName attributeName;
    final CaseSensitivity caseSensitivity;


    @Override
    final void visit(final SearchBigDecimalNode node, final SearchQueryContext context) {
        // nop
    }

    @Override
    final void visit(final SearchBigIntegerNode node, final SearchQueryContext context) {
        // nop
    }

    @Override
    final void visit(final SearchDoubleNode node, final SearchQueryContext context) {
        // nop
    }

    @Override
    final void visit(final SearchLocalDateNode node, final SearchQueryContext context) {
        // nop
    }

    @Override
    final void visit(final SearchLocalDateTimeNode node, final SearchQueryContext context) {
        // nop
    }

    @Override
    final void visit(final SearchLocalTimeNode node, final SearchQueryContext context) {
        // nop
    }

    @Override
    final void visit(final SearchLongNode node, final SearchQueryContext context) {
        // nop
    }

    @Override
    final void visit(final SearchMetaNode node, final SearchQueryContext context) {
        if(this.test(this.value.text(), node.attributes().get(this.attributeName))) {
            context.success(node);
        } else {
            context.failure(node);
        }
    }

    abstract boolean test(final String text, final String attributeValue);

    @Override
    final void visit(final SearchTextNode node, final SearchQueryContext context) {
        // nop
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.attributeName, this.caseSensitivity);
    }

    abstract boolean canBeEqual(final Object other);

    @Override
    final boolean equals0(final SearchQuery other) {
        return this.equals1(Cast.to(other));
    }

    private boolean equals1(final SearchQueryLeafAttribute other) {
        return this.attributeName.equals(other.attributeName) &&
                this.caseSensitivity.equals(other.caseSensitivity);
    }
}
