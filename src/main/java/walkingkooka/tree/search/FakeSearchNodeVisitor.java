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

package walkingkooka.tree.search;

import walkingkooka.test.Fake;
import walkingkooka.visit.Visiting;

public class FakeSearchNodeVisitor extends SearchNodeVisitor implements Fake {

    public FakeSearchNodeVisitor() {
    }

    @Override
    protected Visiting startVisit(final SearchNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final SearchNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final SearchIgnoredNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final SearchIgnoredNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final SearchMetaNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final SearchMetaNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final SearchSelectNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final SearchSelectNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final SearchSequenceNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final SearchSequenceNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final SearchBigDecimalNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final SearchBigIntegerNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final SearchDoubleNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final SearchLocalDateNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final SearchLocalDateTimeNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final SearchLocalTimeNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final SearchLongNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final SearchTextNode node) {
        throw new UnsupportedOperationException();
    }
}
