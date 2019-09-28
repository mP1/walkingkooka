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

import walkingkooka.collect.map.Maps;

import java.util.List;
import java.util.Map;

/**
 * Base class for all {@link SearchNode} that hold more chidren.
 */
abstract class SearchParentNode2 extends SearchParentNode {

    SearchParentNode2(final int index, final SearchNodeName name, final List<SearchNode> children) {
        super(index, name, children);
    }

    // attributes........................................................................................

    @Override
    public final Map<SearchNodeAttributeName, String> attributes() {
        return Maps.empty();
    }

    @Override
    SearchMetaNode setAttributes0(final Map<SearchNodeAttributeName, String> attributes) {
        return SearchMetaNode.with0(this, attributes);
    }

    // HasText .........................................................................................................

    @Override
    public final String text() {
        final StringBuilder b = new StringBuilder();
        this.appendText(b);
        return b.toString();
    }

    @Override
    final void appendText(final StringBuilder b) {
        for (SearchNode child : this.children()) {
            child.appendText(b);
        }
    }

    // HasTextLength...............................................................................................

    @Override
    public final int textLength() {
        return this.children().stream()
                .mapToInt(c -> c.textLength())
                .sum();
    }

    // equals ...............................................................................................

    @Override
    final boolean equalsIgnoringParentAndChildren0(final SearchNode other) {
        return this.canBeEqual(other);
    }

    abstract String toStringPrefix();

    @Override
    final void toStringSuffix(final StringBuilder b) {
        b.append(this.toStringSuffix());
    }

    abstract String toStringSuffix();
}
