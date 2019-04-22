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
 */
package walkingkooka.text.cursor.parser;

import walkingkooka.Cast;

import java.util.AbstractList;

/**
 * A read only list view of the tokens belonging to something like {@link SequenceParser}
 */
final class ParserTokenParentNodeList extends AbstractList<ParserTokenNode> {

    /**
     * Ctor called by {@link ParserTokenNode#children()}
     */
    static ParserTokenParentNodeList with(final ParserTokenParentNode parent) {
        return new ParserTokenParentNodeList(parent);
    }

    private ParserTokenParentNodeList(final ParserTokenParentNode parent) {
        super();

        this.parent = parent;
        this.nodes = new ParserTokenNode[parent.valueAsList().size()];
    }

    // AbstractList ...............................................................................................

    @Override
    public ParserTokenNode get(final int index) {
        ParserTokenNode node = this.nodes[index];
        if (null == node) {
            node = ParserTokenNode.with(this.parent.valueAsList().get(index),
                    this.parent.childrenParent,
                    index);
            this.nodes[index] = node;
        }
        return node;
    }

    private final ParserTokenNode[] nodes;
    final ParserTokenParentNode parent;

    @Override
    public int size() {
        return this.parent.valueAsList().size();
    }

    // Object ...............................................................................................

    @Override
    public int hashCode() {
        return this.parent.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof ParserTokenParentNodeList ?
                this.equals0(Cast.to(other)) :
                super.equals(other);
    }

    /**
     * Optimisation if the other list is also a {@link ParserTokenParentNodeList} save the trouble of wrapping nodes and compare
     * the tokens directly.
     */
    private boolean equals0(final ParserTokenParentNodeList other) {
        return this.parent.equals(other.parent);
    }

    @Override
    public String toString() {
        return this.parent.toString();
    }
}
