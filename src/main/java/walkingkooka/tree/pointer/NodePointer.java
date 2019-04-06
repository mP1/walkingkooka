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
public abstract class NodePointer<N extends Node<N, NAME, ?, ?>, NAME extends Name> {

    private final static String NONE = "-";

    /**
     * Accepts and parses a {@link String} holding a pointer.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> parse(final String pointer, final Function<String, NAME> nameFactory, final Class<N> nodeType) {
        Objects.requireNonNull(pointer, "pointer");
        Objects.requireNonNull(nameFactory, "name factory function");
        checkNodeType(nodeType);

        final String[] components = pointer.split(SEPARATOR.string());
        NodePointer<N, NAME> result = any(nodeType);
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
                       indexed(number, nodeType));
            } catch (final NumberFormatException mustBeName) {
                if(relative) {
                    throw new IllegalArgumentException("Relative pointer expected number but got=" + CharSequences.quote(pointer));
                }
                if(NONE.equals(component)) {
                    result = result.none();
                } else {
                    final NAME name = nameFactory.apply(component.replace("~1", "/")
                            .replace("~0", "~"));
                    result = result.named(name);
                }
            }
            relative = false;
        }

        return result;
    }

    /**
     * Type safe null for use to mark no next. Intended for use only within this package.
     */
    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> absent() {
        return null;
    }

    /**
     * Creates a match all.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> any(final Class<N> nodeType) {
        checkNodeType(nodeType);
        return AnyNodePointer.get();
    }

    /**
     * Creates an array element pointer.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> indexed(final int index, final Class<N> nodeType) {
        checkNodeType(nodeType);
        return IndexedChildNodePointer.with(index);
    }

    /**
     * Creates an object property pointer
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> named(final NAME name, final Class<N> nodeType) {
        checkNodeType(nodeType);
        return NamedChildNodePointer.with(name);
    }

    /**
     * Creates pointer that never matches anything.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> none(final Class<N> nodeType) {
        checkNodeType(nodeType);
        return NoneNodePointer.with(NONE);
    }

    /**
     * Creates an relative.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> relative(final int ancestor, final Class<N> nodeType) {
        checkNodeType(nodeType);
        return RelativeNodePointer.with(ancestor, false);
    }

    /**
     * Creates an relative.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePointer<N, NAME> relativeHash(final int ancestor, final Class<N> nodeType) {
        checkNodeType(nodeType);
        return RelativeNodePointer.with(ancestor, true);
    }

    /**
     * Package private ctor to limit sub classing.
     */
    NodePointer(final NodePointer<N, NAME> next) {
        this.next = next;
    }

    /**
     * Appends a child name to this pointer.
     */
    public final NodePointer<N, NAME> named(final NAME name) {
        return this.append(NamedChildNodePointer.with(name));
    }

    /**
     * Appends a none pointer, equivalent to the "-" token within a string pointer.
     */
    public final NodePointer<N, NAME> none() {
        return this.append(NoneNodePointer.with("/" + NONE));
    }

    /**
     * Appends an index to this pointer.
     */
    public final NodePointer<N, NAME> indexed(final int index) {
        return this.append(IndexedChildNodePointer.with(index));
    }

    /**
     * Appends an index to this pointer.
     */
    abstract NodePointer<N, NAME> append(final NodePointer<N, NAME> pointer);

    final NodePointer<N, NAME> appendToNext(final NodePointer<N, NAME> pointer) {
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
        NodePointer<N, NAME> pointer = this;

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
    private final NodePointer<N, NAME> next;

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

        NodePointer<N, NAME> pointer = this;
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

    private static <N extends Node<N, ?, ?, ?>> void checkNodeType(Class<N> nodeType) {
        Objects.requireNonNull(nodeType, "nodeType");
    }
}
