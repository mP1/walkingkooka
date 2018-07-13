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
 * A {@link Node} wrapper around a {@link SequenceParserToken} which means it includes children.
 */
final class SequenceParserTokenNode extends ParserTokenNode{

    SequenceParserTokenNode(final SequenceParserToken token, final Optional<ParserTokenNode> parent, final int index) {
        super(token, parent, index);
        this.childrenParent = Optional.of(this);
    }

    // children ...........................................................................................................

    @Override
    public List<ParserTokenNode> children() {
        if(null == this.children) {
            this.children = new SequenceParserTokenNodeList(this.asSequenceParserToken().value(), this);
        }
        return this.children;
    }

    private SequenceParserTokenNodeList children;

    Optional<ParserTokenNode> childrenParent;

    @Override
    public SequenceParserTokenNode setChildren(final List<ParserTokenNode> children) {
        Objects.requireNonNull(children, "children");

        final List<ParserToken> childrenTokens = children instanceof SequenceParserTokenNodeList ?
                this.childrenTokens(Cast.to(children)) :
                children.stream()
                .map(n -> n.token)
                .collect(Collectors.toList());
        return this.setChildrenValues0(childrenTokens);
    }

    @Override
    public List<ParserToken> childrenValues() {
        return this.asSequenceParserToken().value();
    }

    @Override
    public SequenceParserTokenNode setChildrenValues(final List<ParserToken> children) {
        Objects.requireNonNull(children, "children");

        final List<ParserToken> copy = Lists.array();
        copy.addAll(children);
        return this.setChildrenValues0(copy);
    }

    private SequenceParserTokenNode setChildrenValues0(final List<ParserToken> children) {
        return this.asSequenceParserToken().value().equals(children) ?
                this :
                Cast.to(this.replaceChildren(children));
    }

    private List<ParserToken> childrenTokens(final SequenceParserTokenNodeList list) {
        return list.parent.asSequenceParserToken().value();
    }

    private ParserTokenNode replaceChildren(final List<ParserToken> children) {
        return new SequenceParserTokenNode(SequenceParserToken.with(children, computeText(children)),
                null,
                this.index())
                .replaceChild0(this.parent());
    }

    private ParserTokenNode replaceChild0(final Optional<ParserTokenNode> previousParent) {
        return previousParent.isPresent() ?
                previousParent.get()
                        .replaceChild1(this)
                        .children()
                        .get(this.index()) :
                this;
    }

    /**
     * Uses the new given child and updates only that property and if the instance is different returns a new node.
     */
    @Override
    final ParserTokenNode replaceChild1(final ParserTokenNode child) {
        final List<ParserToken> newChildren = Lists.array();
        newChildren.addAll(this.asSequenceParserToken().value());
        newChildren.set(child.index(), child.value());

        return new SequenceParserTokenNode(SequenceParserToken.with(newChildren, computeText(newChildren)),
                null,
                this.index())
                .replaceChild0(this.parent());
    }

    private static String computeText(final List<ParserToken> children) {
        return children.stream()
                .map(n -> n.text())
                .collect(Collectors.joining());
    }
}
