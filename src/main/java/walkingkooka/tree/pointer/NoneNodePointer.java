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
import walkingkooka.tree.Node;

/**
 * Represents a reference to an invalid array element.
 * <a href="https://tools.ietf.org/html/rfc6901#page-5"></a>
 * <pre>
 * Note that the use of the "-" character to index an array will always
 * result in such an error condition because by definition it refers to
 * a nonexistent array element.  Thus, applications of JSON Pointer need
 * to specify how that character is to be handled, if it is to be
 * useful.
 * ...
 * </pre>
 */
final class NoneNodePointer<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> extends NodePointer<N, NAME, ANAME, AVALUE>{

    /**
     * Creates a {@link NoneNodePointer}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NoneNodePointer<N, NAME, ANAME, AVALUE> with(final String toString) {
        return new NoneNodePointer(toString);
    }

    /**
     * Private ctor.
     */
    NoneNodePointer(final String toString) {
        super(null);
        this.toString = toString;
    }

    @Override
    NodePointer<N, NAME, ANAME, AVALUE> append(final NodePointer<N, NAME, ANAME, AVALUE> pointer) {
        final StringBuilder b = new StringBuilder();
        b.append(this.toString);
        pointer.toString0(b);
        pointer.lastToString(b);

        return new NoneNodePointer(b.toString());
    }

    @Override
    final N nextNodeOrNull(final N node) {
        return null;
    }

    @Override
    public final boolean isRelative(){
        return false;
    }

    @Override
    final void toString0(final StringBuilder b) {
        b.append(this.toString);
    }

    private final String toString;

    @Override
    final void lastToString(final StringBuilder b){
    }
}
