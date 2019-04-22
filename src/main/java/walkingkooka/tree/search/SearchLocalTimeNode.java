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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * A {@link SearchNode} that holds a {@link LocalTime} value.
 */
public final class SearchLocalTimeNode extends SearchLeafNode<LocalTime> {

    public final static SearchNodeName NAME = SearchNodeName.fromClass(SearchLocalTimeNode.class);

    static SearchLocalTimeNode with(final String text, final LocalTime value) {
        check(text, value);

        return new SearchLocalTimeNode(NO_INDEX, NAME, text, value);
    }

    private SearchLocalTimeNode(final int index, final SearchNodeName name, final String text, final LocalTime value) {
        super(index, name, text, value);
    }

    @Override
    SearchNodeName defaultName() {
        return NAME;
    }

    @Override
    public SearchLocalTimeNode setName(final SearchNodeName name) {
        return super.setName0(name).cast();
    }

    @Override
    public SearchLocalTimeNode setValue(final LocalTime value) {
        return this.setValue0(value).cast();
    }

    @Override
    SearchLocalTimeNode replace0(final int index, final SearchNodeName name, final String text, final LocalTime value) {
        return new SearchLocalTimeNode(index, name, text, value);
    }

    @Override
    public SearchLocalTimeNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public boolean isBigDecimal() {
        return false;
    }

    @Override
    public boolean isBigInteger() {
        return false;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public boolean isLocalDate() {
        return false;
    }

    @Override
    public boolean isLocalDateTime() {
        return false;
    }

    @Override
    public boolean isLocalTime() {
        return true;
    }

    @Override
    public boolean isLong() {
        return false;
    }

    @Override
    public boolean isText() {
        return false;
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
        return other instanceof SearchLocalTimeNode;
    }

    @Override
    final void toString1(final StringBuilder b) {
        b.append(DateTimeFormatter.ISO_LOCAL_TIME.format(this.value()));
    }
}
