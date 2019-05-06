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

import java.util.Optional;

/**
 * Base class for all NodeSelectorParserToken symbol parser tokens.
 */
abstract class NodeSelectorSymbolParserToken extends NodeSelectorLeafParserToken<String> {

    NodeSelectorSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    void checkText(final String text) {
        checkTextNullOrWhitespace(text);
    }

    @Override
    public final Optional<NodeSelectorParserToken> withoutSymbols() {
        return Optional.empty();
    }

    // is..............................................................................................

    @Override
    public final boolean isAbsolute() {
        return false;
    }

    @Override
    public final boolean isAncestor() {
        return false;
    }

    @Override
    public final boolean isAncestorOrSelf() {
        return false;
    }

    @Override
    public final boolean isAttributeName() {
        return false;
    }

    @Override
    public final boolean isChild() {
        return false;
    }

    @Override
    public final boolean isDescendant() {
        return false;
    }

    @Override
    public final boolean isDescendantOrSelf() {
        return false;
    }

    @Override
    public final boolean isFirstChild() {
        return false;
    }

    @Override
    public final boolean isFollowing() {
        return false;
    }

    @Override
    public final boolean isFollowingSibling() {
        return false;
    }

    @Override
    public final boolean isFunctionName() {
        return false;
    }

    @Override
    public final boolean isLastChild() {
        return false;
    }

    @Override
    public final boolean isNodeName() {
        return false;
    }

    @Override
    public final boolean isNumber() {
        return false;
    }

    @Override
    public final boolean isParentOf() {
        return false;
    }

    @Override
    public final boolean isPreceding() {
        return false;
    }

    @Override
    public final boolean isPrecedingSibling() {
        return false;
    }

    @Override
    public final boolean isQuotedText() {
        return false;
    }

    @Override
    public final boolean isSelf() {
        return false;
    }

    @Override
    public final boolean isSymbol() {
        return true;
    }

    @Override
    public final boolean isWildcard() {
        return false;
    }

    @Override
    public final boolean isNoise() {
        return true;
    }
}
