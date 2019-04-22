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

import walkingkooka.collect.list.Lists;
import walkingkooka.tree.Node;

import java.util.List;
import java.util.Optional;

/**
 * A {@link Node} wrapper around a {@link ParserToken}. This allows selectors to match and replace nodes typically
 * during a simplification phase.
 */
final class ParserTokenLeafNode extends ParserTokenNode {

    static ParserTokenLeafNode with(final ParserToken token, final Optional<ParserTokenNode> parent, final int index) {
        return new ParserTokenLeafNode(token, parent, index);
    }

    private ParserTokenLeafNode(final ParserToken token, final Optional<ParserTokenNode> parent, final int index) {
        super(token, parent, index);
    }

    @Override
    ParserTokenNode removeParent0() {
        return new ParserTokenLeafNode(this.token, NO_PARENT, NO_INDEX);
    }

    // children ...........................................................................................................

    @Override
    public List<ParserTokenNode> children() {
        return Lists.empty();
    }

    @Override
    public List<ParserToken> childrenValues() {
        return Lists.empty();
    }

    @Override
    public Optional<ParserTokenNode> firstChild() {
        return NO_CHILD;
    }

    @Override
    public Optional<ParserTokenNode> lastChild() {
        return NO_CHILD;
    }

    private final static Optional<ParserTokenNode> NO_CHILD = Optional.empty();

    @Override
    public ParserTokenNode setChildrenValues(final List<ParserToken> childrenValues) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParserTokenNode setChildren(final List<ParserTokenNode> children) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParserTokenNode appendChild(final ParserTokenNode child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParserTokenNode removeChild(final int child) {
        throw new UnsupportedOperationException();
    }

    @Override
    ParserTokenNode replaceChild1(final ParserTokenNode child) {
        throw new UnsupportedOperationException();
    }

    @Override
    ParserTokenNode replaceText(final String text) {
        return new ParserTokenLeafNode(this.token.setText(text),
                null,
                this.index())
                .replaceChild0(this.parent());
    }
}
