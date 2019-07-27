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

package walkingkooka.tree.pointer;

import walkingkooka.naming.Name;
import walkingkooka.test.Fake;
import walkingkooka.tree.Node;
import walkingkooka.visit.Visiting;

public class FakeNodePointerVisitor<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePointerVisitor<N, NAME> implements Fake {
    protected FakeNodePointerVisitor() {
        super();
    }

    @Override
    protected Visiting startVisit(final NodePointer<N, NAME> node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final NodePointer<N, NAME> node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final AnyNodePointer<N, NAME> node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final AppendNodePointer<N, NAME> node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final IndexedChildNodePointer<N, NAME> node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final IndexedChildNodePointer<N, NAME> node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final NamedChildNodePointer<N, NAME> node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final NamedChildNodePointer<N, NAME> node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final RelativeNodePointer<N, NAME> node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final RelativeNodePointer<N, NAME> node) {
        throw new UnsupportedOperationException();
    }
}
