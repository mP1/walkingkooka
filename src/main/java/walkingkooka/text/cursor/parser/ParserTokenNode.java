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
import walkingkooka.Value;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.PathSeparator;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.HasChildrenValues;
import walkingkooka.tree.Node;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Node} readonly wrapper around a {@link ParserToken}. This allows selectors to match and replace nodes typically
 * during a simplification phase.
 */
public abstract class ParserTokenNode implements Node<ParserTokenNode, ParserTokenNodeName, ParserTokenNodeAttributeName, String>,
        HasChildrenValues<ParserToken, ParserTokenNode>,
        Value<ParserToken> {

    /**
     * The {@link PathSeparator} for node selector paths.
     */
    public static final PathSeparator PATH_SEPARATOR = PathSeparator.requiredAtStart('/');

    /**
     * Wraps the provided node.
     */
    static ParserTokenNode with(final ParserToken token) {
        return with(token, NO_PARENT, UNKNOWN_INDEX);
    }

    static ParserTokenNode with(final ParserToken token, final Optional<ParserTokenNode> parent, final int index) {
        return token instanceof SequenceParserToken ?
                ParserTokenParentNode.with(Cast.to(token), parent, index) :
                ParserTokenLeafNode.with(token, parent, index);
    }

    ParserTokenNode(final ParserToken token, final Optional<ParserTokenNode> parent, final int index) {
        this.token = token;
        this.parent = parent;
        this.index = index;
    }

    // Node ...........................................................................................................

    // name ...........................................................................................................

    @Override
    public ParserTokenNodeName name() {
        return this.token.name();
    }

    // parent ...........................................................................................................

    @Override
    public final Optional<ParserTokenNode> parent() {
        return this.parent;
    }

    private final Optional<ParserTokenNode> parent;

    /**
     * Returns a {@link ParserTokenNode} with the same value.
     */
    @Override
    public final ParserTokenNode removeParent() {
        return this.isRoot() ?
                this :
                this.removeParent0();
    }

    abstract ParserTokenNode removeParent0();

    final static Optional<ParserTokenNode> NO_PARENT = Optional.empty();

    // index ...........................................................................................................

    /**
     * A magic value that indicates the index has not be discovered.
     */
    final static int UNKNOWN_INDEX = Integer.MIN_VALUE;

    @Override
    public final int index() {
        if (UNKNOWN_INDEX == this.index) {
            final Optional<ParserTokenNode> parent = this.parent();
            this.index = parent.isPresent() ?
                    parent.get().valueAsList().indexOf(this) :
                    -1;
        }
        return this.index;
    }

    private int index;

    // children ...........................................................................................................

    /**
     * Returns the child values as actual {@link ParserToken}
     */
    abstract public List<ParserToken> childrenValues();

    final ParserTokenNode replaceChild0(final Optional<ParserTokenNode> previousParent) {
        return previousParent.isPresent() ?
                previousParent.get()
                        .replaceChild1(this)
                        .children()
                        .get(this.index()) :
                this;
    }

    /**
     * Only really implemented by {@link ParserTokenParentNode}
     */
    abstract ParserTokenNode replaceChild1(final ParserTokenNode child);

    // attributes ...........................................................................................................

    @Override
    public final Map<ParserTokenNodeAttributeName, String> attributes() {
        if (null == this.attributes) {
            this.attributes = Maps.of(ParserTokenNodeAttributeName.TEXT, this.token.text());
        }
        return this.attributes;
    }

    @Override
    public final ParserTokenNode setAttributes(final Map<ParserTokenNodeAttributeName, String> attributes) {
        Objects.requireNonNull(attributes, "attributes");

        return this.attributes().equals(attributes) ?
                this :
                this.replaceAttributes(attributes);

    }

    private Map<ParserTokenNodeAttributeName, String> attributes;

    private ParserTokenNode replaceAttributes(final Map<ParserTokenNodeAttributeName, String> attributes) {
        final String text = attributes.get(ParserTokenNodeAttributeName.TEXT);
        if (null == text) {
            throw new IllegalArgumentException("Missing required attribute " + CharSequences.quote(ParserTokenNodeAttributeName.TEXT.value()));
        }
        return this.replaceText(text);
    }

    abstract ParserTokenNode replaceText(final String text);

    // Value ...........................................................................................................

    @Override
    public final ParserToken value() {
        return this.token;
    }

    final List<ParserToken> valueAsList() {
        return Cast.to(ParentParserToken.class.cast(this.value()).value());
    }

    final ParserToken token;

    // Object ...........................................................................................................

    @Override
    public final int hashCode() {
        return this.token.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other || other instanceof ParserTokenNode && this.equals0(Cast.to(other));
    }

    private boolean equals0(final ParserTokenNode other) {
        return this.token.equals(other.token);
    }

    @Override
    public final String toString() {
        return this.token.toString();
    }
}
