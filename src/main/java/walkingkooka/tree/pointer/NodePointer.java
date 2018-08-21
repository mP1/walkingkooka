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
 * Because the return value is a {@link Node} any relative pointer terminating with a trailing will ignore the hash itself and return the result as if it was absent.
 * <br>
 * <a href="http://json-schema.org/latest/relative-json-pointer.html#RFC4627">spec</a>
 */
public abstract class NodePointer<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> {

    final static String NONE = "-";

    /**
     * Accepts and parses a {@link String} holding a pointer.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodePointer<N, NAME, ANAME, AVALUE> parse(final String pointer, final Function<String, NAME> nameFactory, final Class<N> nodeType) {
        Objects.requireNonNull(pointer, "pointer");
        Objects.requireNonNull(nameFactory, "name factory function");
        Objects.requireNonNull(nodeType, "nodeType");

        final String[] components = pointer.split(SEPARATOR.string());
        NodePointer<N, NAME, ANAME, AVALUE> result = all(nodeType);
        boolean relative = true;
        boolean hash = false;

        for(String component : components) {
            if(relative){
                if(component.isEmpty()) {
                    relative = false; // found a slash...
                    continue;
                }
                if(component.endsWith("#")) {
                    component = component.substring(0, component.length() - 1);
                    hash = true;
                }
            }
            if(component.isEmpty()) {
                throw new IllegalArgumentException("Empty component found within pointer=" + CharSequences.quote(pointer));
            }

            try{
                final int number = Integer.parseInt(component);
                result = result.append(relative ?
                       RelativeNodePointer.with(number, hash):
                       index(number, nodeType));
            } catch (final NumberFormatException mustBeName) {
                if(relative) {
                    throw new IllegalArgumentException("Relative pointer expected number but got=" + CharSequences.quote(pointer));
                }
                if(NONE.equals(component)) {
                    result = result.none();
                } else {
                    final NAME name = nameFactory.apply(component.replace("~1", "/")
                            .replace("~0", "~"));
                    if (relative) {
                        throw new IllegalArgumentException("Expected number with relative pointer=" + CharSequences.quote(pointer));
                    }
                    result = result.named(name);
                }
            }
            relative = false;
        }

        return result;
    }

    /**
     * Creates a match all.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodePointer<N, NAME, ANAME, AVALUE> all(final Class<N> nodeType) {
        Objects.requireNonNull(nodeType, "nodeType");
        return AllNodePointer.get();
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
     * Creates pointer that never matches anything.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodePointer<N, NAME, ANAME, AVALUE> none(final Class<N> nodeType) {
        Objects.requireNonNull(nodeType, "nodeType");
        return NoneNodePointer.with(NONE);
    }

    /**
     * Creates an relative.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodePointer<N, NAME, ANAME, AVALUE> relative(final int ancestor, final Class<N> nodeType) {
        Objects.requireNonNull(nodeType, "nodeType");
        return RelativeNodePointer.with(ancestor, false);
    }

    /**
     * Creates an relative.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodePointer<N, NAME, ANAME, AVALUE> relativeHash(final int ancestor, final Class<N> nodeType) {
        Objects.requireNonNull(nodeType, "nodeType");
        return RelativeNodePointer.with(ancestor, true);
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
     * Appends a none pointer, equivalent to the "-" token within a string pointer.
     */
    public final NodePointer<N, NAME, ANAME, AVALUE> none() {
        return this.append(NoneNodePointer.with("/" + NONE));
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

        Optional<N> result;

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
     * Tests if this is a absolute pointer, basically the opposite of {@link #isRelative()}
     */
    public final boolean isAbsolute() {
        return !this.isRelative();
    }

    /**
     * Tests if this is a relative pointer.
     */
    abstract public boolean isRelative();

    /**
     * Return the full node pointer string.
     */
    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();

        NodePointer<N, NAME, ANAME, AVALUE> pointer = this;
        for(;;) {
            pointer.toString0(b);

            if(null==pointer.next) {
                pointer.lastToString(b);
                break;
            }
            pointer = pointer.next;
        }

        return b.toString();
    }

    final static CharacterConstant SEPARATOR = CharacterConstant.with('/');

    abstract void toString0(final StringBuilder b);

    abstract void lastToString(final StringBuilder b);
}
