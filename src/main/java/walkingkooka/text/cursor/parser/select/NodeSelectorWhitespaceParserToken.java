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
package walkingkooka.text.cursor.parser.select;

import walkingkooka.text.cursor.parser.ParserTokenNodeName;

import java.util.Optional;

/**
 * Holds the combination any whitespace that may appear within a selector.
 */
public final class NodeSelectorWhitespaceParserToken extends NodeSelectorNonSymbolParserToken<String> {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(NodeSelectorWhitespaceParserToken.class);

    static NodeSelectorWhitespaceParserToken with(final String value, final String text) {
        checkValue(value);
        checkTextNullOrEmpty(text);

        return new NodeSelectorWhitespaceParserToken(value, text);
    }

    private NodeSelectorWhitespaceParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public NodeSelectorWhitespaceParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    void checkText(final String text) {
        checkTextNullOrEmpty(text);
    }

    @Override
    NodeSelectorWhitespaceParserToken replaceText(final String text) {
        return new NodeSelectorWhitespaceParserToken(this.value, text);
    }

    @Override
    public Optional<NodeSelectorParserToken> withoutSymbolsOrWhitespace() {
        return Optional.empty();
    }

    // name................................................................................................

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    // is................................................................................................

    @Override
    public boolean isAbsolute() {
        return false;
    }

    @Override
    public boolean isAncestor() {
        return false;
    }

    @Override
    public boolean isAttributeName() {
        return false;
    }

    @Override
    public boolean isChild() {
        return false;
    }

    @Override
    public boolean isDescendant() {
        return false;
    }

    @Override
    public boolean isFirstChild() {
        return false;
    }

    @Override
    public boolean isFollowing() {
        return false;
    }

    @Override
    public boolean isFollowingSibling() {
        return false;
    }

    @Override
    public boolean isFunctionName() {
        return false;
    }

    @Override
    public boolean isLastChild() {
        return false;
    }

    @Override
    public boolean isNodeName() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isParentOf() {
        return false;
    }

    @Override
    public boolean isPreceding() {
        return false;
    }

    @Override
    public boolean isPrecedingSibling() {
        return false;
    }

    @Override
    public boolean isQuotedText() {
        return false;
    }

    @Override
    public boolean isSelf() {
        return false;
    }

    @Override
    public boolean isWhitespace() {
        return true;
    }

    @Override
    public boolean isNoise() {
        return true;
    }

    // Visitor................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorWhitespaceParserToken;
    }
}
