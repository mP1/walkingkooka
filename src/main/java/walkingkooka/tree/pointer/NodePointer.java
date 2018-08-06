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

package walkingkooka.tree.pointer;

import walkingkooka.naming.Name;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;
import walkingkooka.tree.Node;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A node pointer may be thought of as a JsonPointer that operates on any type of {@link Node}.
 * THe only difference is an element index will always match any first child, for something like a json object this will be the first key/value.
 */
public abstract class NodePointer<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> {

    /**
     * Accepts and parses a {@link String} holding a pointer.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodePointer<N, NAME, ANAME, AVALUE> parse(final String pointer, final Function<String, NAME> nameFactory, final Class<N> nodeType) {
        Objects.requireNonNull(pointer, "pointer");
        Objects.requireNonNull(nameFactory, "name factory function");
        Objects.requireNonNull(nodeType, "nodeType");

        final String[] components = pointer.split(SEPARATOR.string());
        NodePointer<N, NAME, ANAME, AVALUE> result = null;

        int i = 0;
        for(String component : components) {
            if(0 == i){
                if(!component.isEmpty()) {
                    throw new IllegalArgumentException("Point missing initial '/'=" + CharSequences.quote(pointer));
                }
                i++;
                continue;
            }
            if(component.isEmpty()) {
                throw new IllegalArgumentException("Empty component found within pointer=" + CharSequences.quote(pointer));
            }

            i++;

            try{
                final int index = Integer.parseInt(component);
                if(null==result){
                   result = index(index, nodeType);
                } else {
                   result = result.index(index);
                }
            } catch (final NumberFormatException mustBeName) {
                final NAME name = nameFactory.apply(component);
                if(null==result){
                    result = named(name, nodeType);
                } else {
                    result = result.named(name);
                }
            }
        }

        return result;
    }

    /**
     * Creates an array element pointer.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodePointer<N, NAME, ANAME, AVALUE> index(final int index, final Class<N> nodeType) {
        Objects.requireNonNull(nodeType, "nodeType");
        return NodeChildElementNodePointer.with(index);
    }

    /**
     * Creates an object property pointer
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodePointer<N, NAME, ANAME, AVALUE> named(final NAME name, final Class<N> nodeType) {
        Objects.requireNonNull(nodeType, "nodeType");
        return NodeChildNamedNodePointer.with(name);
    }

    /**
     * Named constant marking a terminal in a pointer.
     */
    final static NodePointer NO_NEXT = null;

    /**
     * Package private ctor to limit sub classing.
     */
    NodePointer(final NodePointer<N, NAME, ANAME, AVALUE> next) {
        this.next = next;
    }

    /**
     * Appends a child name to this pointer.
     */
    public final NodePointer<N, NAME, ANAME, AVALUE> named(final NAME name) {
        return this.append(NodeChildNamedNodePointer.with(name));
    }

    /**
     * Appends an index to this pointer.
     */
    public final NodePointer<N, NAME, ANAME, AVALUE> index(final int index) {
        return this.append(NodeChildElementNodePointer.with(index));
    }

    /**
     * Appends an index to this pointer.
     */
    abstract NodePointer<N, NAME, ANAME, AVALUE> append(final NodePointer<N, NAME, ANAME, AVALUE> pointer);

    final NodePointer<N, NAME, ANAME, AVALUE> appendToNext(final NodePointer<N, NAME, ANAME, AVALUE> pointer) {
        return null == this.next ?
               pointer :
               this.next.append(pointer);
    }

    /**
     * Walks this pointer and returns the matching {@link Node}
     */
    public final Optional<N> traverse(final N node) {
        Objects.requireNonNull(node, "node");

        Optional<N> result = null;

        N current = node;
        NodePointer<N, NAME, ANAME, AVALUE> pointer = this;

        for(;;) {
            N next = pointer.nextNodeOrNull(current);
            if(null==next){
                result = Optional.empty();
                break;
            }
            pointer = pointer.next;
            if(null==pointer){
                result = Optional.of(next);
                break;
            }
            current = next;
        }

        return result;
    }

    abstract N nextNodeOrNull(final N node);

    /**
     * The next part in the pointer or null.
     */
    private final NodePointer<N, NAME, ANAME, AVALUE> next;

    /**
     * Return the full node pointer string.
     */
    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();

        NodePointer<N, NAME, ANAME, AVALUE> pointer = this;
        do {
            b.append(SEPARATOR.character());
            pointer.toString0(b);

            pointer = pointer.next;
        } while(null != pointer);

        return b.toString();
    }

    private final static CharacterConstant SEPARATOR = CharacterConstant.with('/');

    abstract void toString0(final StringBuilder b);
}
