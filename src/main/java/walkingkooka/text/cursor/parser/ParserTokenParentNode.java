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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.Node;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A {@link Node} wrapper around a {@link ParentParserToken} which means it includes children.
 */
final class ParserTokenParentNode extends ParserTokenNode {

    static ParserTokenParentNode with(final ParentParserToken<?> token, final Optional<ParserTokenNode> parent, final int index) {
        return new ParserTokenParentNode(token, parent, index);
    }

    private ParserTokenParentNode(final ParentParserToken<?> token, final Optional<ParserTokenNode> parent, final int index) {
        super(token, parent, index);
        this.childrenParent = Optional.of(this);
    }

    @Override
    ParserTokenNode removeParent0() {
        return new ParserTokenParentNode(this.token.cast(), NO_PARENT, NO_INDEX);
    }

    // children ...........................................................................................................

    @Override
    public List<ParserTokenNode> children() {
        if (null == this.children) {
            this.children = ParserTokenParentNodeList.with(this);
        }
        return this.children;
    }

    @Override
    public ParserTokenNode setChildren(final List<ParserTokenNode> children) {
        Objects.requireNonNull(children, "children");

        final List<ParserToken> childrenTokens = children instanceof ParserTokenParentNodeList ?
                ParserTokenParentNodeList.class.cast(children).parent.valueAsList() :
                children.stream()
                        .map(n -> n.token)
                        .collect(Collectors.toList());
        return this.setChildrenValues0(childrenTokens);
    }

    private ParserTokenParentNodeList children;

    final Optional<ParserTokenNode> childrenParent;

    @Override
    public List<ParserToken> childrenValues() {
        return this.valueAsList();
    }

    @Override
    public ParserTokenNode setChildrenValues(final List<ParserToken> children) {
        Objects.requireNonNull(children, "children");

        final List<ParserToken> copy = Lists.array();
        copy.addAll(children);
        return this.setChildrenValues0(copy);
    }

    private ParserTokenNode setChildrenValues0(final List<ParserToken> children) {
        return this.valueAsList().equals(children) ?
                this :
                Cast.to(this.replaceChildren(children));
    }

    private ParserTokenNode replaceChildren(final List<ParserToken> children) {
        return new ParserTokenParentNode(this.token().setValue(children).cast(),
                null,
                this.index())
                .replaceChild0(this.parent());
    }

    /**
     * Uses the new given child and updates only that property and if the instance is different returns a new node.
     */
    @Override final ParserTokenNode replaceChild1(final ParserTokenNode child) {
        final List<ParserToken> newChildren = Lists.array();
        newChildren.addAll(this.valueAsList());
        newChildren.set(child.index(), child.value());

        return new ParserTokenParentNode(this.token().setValue(newChildren).cast(),
                null,
                this.index())
                .replaceChild0(this.parent());
    }

    @Override
    ParserTokenNode replaceText(final String text) {
        return new ParserTokenParentNode(this.token.setText(text).cast(),
                null,
                this.index())
                .replaceChild0(this.parent());
    }

    private ParentParserToken<?> token() {
        return ParentParserToken.class.cast(this.token);
    }
}
