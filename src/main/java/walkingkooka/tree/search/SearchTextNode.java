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

import walkingkooka.text.CharSequences;

/**
 * A {@link SearchNode} that holds a {@link String} value.
 */
public final class SearchTextNode extends SearchLeafNode<String> {

    public final static SearchNodeName NAME = SearchNodeName.fromClass(SearchTextNode.class);

    static SearchTextNode with(final String text, final String value) {
        check(text, value);

        return new SearchTextNode(NO_INDEX, NAME, text, value);
    }

    private SearchTextNode(final int index, final SearchNodeName name, final String text, final String value) {
        super(index, name, text, value);
    }

    @Override
    SearchNodeName defaultName() {
        return NAME;
    }

    @Override
    public SearchTextNode setName(final SearchNodeName name) {
        return super.setName0(name).cast();
    }

    @Override
    public SearchTextNode setValue(final String value) {
        return this.setValue0(value).cast();
    }

    @Override
    SearchTextNode replace0(final int index, final SearchNodeName name, final String text, final String value) {
        return new SearchTextNode(index, name, text, value);
    }

    @Override
    public SearchTextNode removeParent() {
        return this.removeParent0().cast();
    }

    // SearchQuery......................................................................................................

    @Override
    void select(final SearchQuery query, final SearchQueryContext context) {
        query.visit(this, context);
    }

    // Visitor ..........................................................................................................

    @Override
    public void accept(final SearchNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    final boolean canBeEqual(final Object other) {
        return other instanceof SearchTextNode;
    }

    @Override
    final void toString1(final StringBuilder b) {
        b.append(CharSequences.quoteAndEscape(this.value()));
    }
}
