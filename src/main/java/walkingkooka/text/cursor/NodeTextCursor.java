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

package walkingkooka.text.cursor;

import walkingkooka.naming.Name;
import walkingkooka.text.HasText;
import walkingkooka.tree.Node;

import java.util.Iterator;
import java.util.Objects;

/**
 * A {@link TextCursor} that consumes the text belonging to a {@link Node} and all its descendants.
 * This will be useful to walk over a tree of nodes, where some or all or perhaps none contain text.
 * Note that any parent node, that is a node with one or more children will have its text if any ignored. It is assumed
 * that the text of a parent node, may be reconstructed from the text from child nodes.
 */
final class NodeTextCursor<N extends Node<N, NAME, ANAME, AVALUE> & HasText,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> implements TextCursor {

    /**
     * Factory that creates a {@link TextCursor} using the provided {@link Node}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE> & HasText,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeTextCursor<N, NAME, ANAME, AVALUE> with(final N node) {
        Objects.requireNonNull(node, "node");

        return new NodeTextCursor<>(node);
    }

    /**
     * Private ctor use factory
     */
    private NodeTextCursor(final N node) {
        this.nodes = node.treeIterator();
        this.cursor = TextCursors.charSequence(this.text);
    }

    @Override
    public boolean isEmpty() throws TextCursorException {
        this.fillIfCursorEmpty();
        return this.cursor.isEmpty();
    }

    @Override
    public char at() throws TextCursorException {
        this.fillIfCursorEmpty();
        return this.cursor.at();
    }

    @Override
    public TextCursor next() throws TextCursorException {
        this.fillIfCursorEmpty();
        this.cursor.next();
        return this;
    }

    private void fillIfCursorEmpty() {
        if (this.cursor.isEmpty()) {
            // grab next node, and fill StringBuilder $text.
            for (; ; ) {
                if (!this.nodes.hasNext()) {
                    break;
                }
                final N node = this.nodes.next();

                // only consume text from nodes without any children...
                if (node.children().isEmpty()) {
                    this.text.append(node.text());
                    break;
                }
            }
        }
    }

    @Override
    public TextCursorSavePoint save() {
        return cursor.save();
    }

    @Override
    public TextCursorLineInfo lineInfo() {
        return this.cursor.lineInfo();
    }

    /**
     * Provides the next {@link Node} to add more text to {@link #text}
     */
    private final Iterator<N> nodes;

    /**
     * Filled with text whenever the wrapped cursor becomes empty.
     */
    private final StringBuilder text = new StringBuilder();

    /**
     * The wrapped {@link TextCursor} that provides individual characters, save points, line info etc.
     */
    private final TextCursor cursor;

    @Override
    public String toString() {
        return this.cursor.toString();
    }
}
