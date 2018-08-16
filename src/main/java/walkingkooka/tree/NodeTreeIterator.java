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

package walkingkooka.tree;

import walkingkooka.collect.stack.Stack;
import walkingkooka.collect.stack.Stacks;
import walkingkooka.naming.Name;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * An {@link Iterator} returned by {@link Node#treeIterator()} that walks all the descendants starting a the beginning {@link Node}.
 */
final class NodeTreeIterator<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> implements Iterator<N> {

    /**
     * Package private ctor only called by default method {@link Node#treeIterator()}
     */
    NodeTreeIterator(final N node) {
        final Stack<N> unprocessed = Stacks.arrayList();
        this.unprocessed = unprocessed;

        unprocessed.push(node);
    }

    @Override
    public boolean hasNext() {
        boolean has = null != this.next;
        if(!has){
            this.next = this.nextOrNull();
            has = null != this.next;
        }

        return has;
    }

    @Override
    public N next() {
        N give = this.next;
        this.next = null;
        if(null==give) {
            this.next = this.nextOrNull();
            if(null==this.next) {
                throw new NoSuchElementException();
            }
            give = this.next;
            this.next = null;
        }
        return give;
    }

    /**
     * Cache of the next node or null.
     */
    private N next;

    private N nextOrNull()  {
        N node = null;

        final Stack<N> stack = this.unprocessed;
        if (false == stack.isEmpty()) {
            node = stack.peek();
            stack.pop();

            this.pushNextSibling(node);
            this.pushFirstChild(node);
        }

        return node;
    }

    /**
     * A {@link Stack} of {@link Node nodes} that remain unprocessed and are immediate candidates for match testing.
     */
    Stack<N> unprocessed;

    /**
     * Starting at the given {@link Node} pushes the next sibling.
     */
    private void pushNextSibling(final N node) {
        final Optional<N> nextSibling = node.nextSibling();
        if (nextSibling.isPresent()) {
            this.unprocessed.push(nextSibling.get());
        }
    }

    /**
     * Pushes the first child of the {@link Node} if it is a parent.
     */
    private void pushFirstChild(final N node) {
        //final Optional<N> parent = node.parent();
        //if (parent.isPresent()) {
            final Optional<N> firstChild = node.firstChild();
            if (firstChild.isPresent()) {
                this.unprocessed.push(firstChild.get());
            }
        //}
    }

    @Override
    public String toString() {
        return null != this.next ?
                this.next.toString() :
                this.unprocessed.isEmpty() ?
                        "???" :
                        this.unprocessed.peek().toString();
    }
}
