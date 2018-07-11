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
import java.util.List;

/**
 * A read only list view of the tokens belonging to something like {@link SequenceParser}
 */
final class SequenceParserTokenNodeList<T extends ParserToken> extends AbstractList<ParserTokenNode> {

    /**
     * Ctor called by {@link ParserTokenNode#children()}
     */
    SequenceParserTokenNodeList(final List<ParserToken> tokens, final SequenceParserTokenNode parent) {
        this.tokens = tokens;
        this.parent = parent;
        this.nodes = new ParserTokenNode[tokens.size()];
    }

    // AbstractList ...............................................................................................

    @Override
    public ParserTokenNode get(final int index) {
        ParserTokenNode node = this.nodes[index];
        if(null==node) {
            node = ParserTokenNode.with(this.tokens.get(index),
                    this.parent.childrenParent,
                    index);
            this.nodes[index]=node;
        }
        return node;
    }

    private final ParserTokenNode[] nodes;
    final SequenceParserTokenNode parent;

    @Override
    public int size() {
        return this.tokens.size();
    }

    // Object ...............................................................................................

    @Override
    public int hashCode() {
        return this.tokens.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof SequenceParserTokenNodeList ?
                this.equals0(Cast.to(other)) :
                super.equals(other);
    }

    /**
     * Optimisation if the other list is also a {@link SequenceParserTokenNodeList} save the trouble of wrapping nodes and compare
     * the tokens directly.
     */
    private boolean equals0(final SequenceParserTokenNodeList other) {
        return this.tokens.equals(other.tokens);
    }

    @Override
    public String toString() {
        return this.tokens.toString();
    }

    private final List<ParserToken> tokens;
}
