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

package walkingkooka.tree.patch;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;
import walkingkooka.tree.pointer.NodePointer;

import java.util.Objects;

/**
 * Base class for all non empty patch containing several helpers and a template for the function.
 */
abstract class NonEmptyNodePatch<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePatch<N, NAME> {

    // helpers......................................................................

    static void checkPath(final NodePointer path) {
        Objects.requireNonNull(path, "path");
    }

    /**
     * Package private to limit sub-classing.
     */
    NonEmptyNodePatch(final NodePointer<N, NAME> path, final NodePatch<N, NAME> next) {
        super();
        this.path = path;
        this.next = next;
    }

    // Function............................................................................................

    @Override
    final N apply0(final N node, final NodePointer<N, NAME> start) {
        try {
            return this.apply1(node, start);
        } catch (final ApplyNodePatchException cause) {
            throw cause;
        } catch (final RuntimeException cause) {
            throw new ApplyNodePatchException("Patch failed: " + cause.getMessage(), this, cause);
        }
    }

    abstract N apply1(final N node, final NodePointer<N, NAME> start);

    /**
     * Adds one node to the other and returns the node in the start position.
     */
    final N add0(final N node,
                 final N add,
                 final NodePointer<N, NAME> start) {
        return this.traverseStartOrFail(this.path.add(node, add), start);
    }

    /**
     * Removes the {@link Node} at the given {@link NodePointer}.
     */
    final N remove0(final N node,
                    final NodePointer<N, NAME> path,
                    final NodePointer<N, NAME> start) {
        return this.traverseStartOrFail(path.remove(node), start);
    }

    /**
     * Reports an operation failure.
     */
    final ApplyNodePatchException failed() {
        return new ApplyNodePatchException("Patch failed", this);
    }

    /**
     * Creates the {@link #failed()} exception and throws it.
     */
    final void throwFailed() {
        throw this.failed();
    }

    // helpers............................................................................................

    final NodePointer<N, NAME> path;

    /**
     * Returns the next component of the patch which could be null if this is the last.
     */
    @Override
    final NodePatch<N, NAME> nextOrNull() {
        return this.next;
    }

    /**
     * A null marks the last component in a patch sequence.
     */
    final NodePatch<N, NAME> next;

    // HashCodeEqualsDefined................................................................................................

    @Override
    public final boolean equals(final Object other) {
        return this == other || this.canBeEqual(other) && this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final NonEmptyNodePatch<?, ?> other) {
        return this.equals1(other) && Objects.equals(this.next, other.next);
    }

    abstract boolean equals1(final NonEmptyNodePatch<?, ?> other);

    // Object............................................................................................

    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();

        NodePatch<N, NAME> patch = this;
        String separator = "";

        do {
            b.append(separator);
            patch.toString0(b);

            patch = patch.nextOrNull();
            separator = ", ";
        } while (null != patch);

        return b.toString();
    }

    final static String toString(final NodePointer<?, ?> path) {
        return CharSequences.quote(path.toString()).toString();
    }
}
