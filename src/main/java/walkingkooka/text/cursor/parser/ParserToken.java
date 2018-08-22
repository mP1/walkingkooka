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
import walkingkooka.text.HasText;

/**
 * Represents a result of a parser attempt to consume a {@link walkingkooka.text.cursor.TextCursor}
 */
public interface ParserToken extends HasText {

    /**
     * Returns the raw text that produced the token.
     */
    String text();

    /**
     * Would be setter that creates if necessary a token with the new text.
     */
    ParserToken setText(final String text);

    /**
     * Returns a {@link ParserTokenNode} for this {@link ParserToken}
     */
    default ParserTokenNode asNode() {
        return ParserTokenNode.with(this);
    }

    /**
     * Returns the name of the token
     */
    ParserTokenNodeName name();

    /**
     * Only returns true for missing tokens.
     */
    default boolean isMissing() {
       return false;
    }

    /**
     * Only returns true for noise tokens including missing and whitespace.
     */
    default boolean isNoise() {
        return false;
    }

    /**
     * Called by the visitor responsible for this group of tokens, which typically resides in the same package.
     * The token must then call the appropriate visit or start/end visit and also visit any child token values as appropriate.
     */
    void accept(final ParserTokenVisitor visitor);

    /**
     * Useful to get help reduce casting noise.
     */
    default <T extends ParserToken> T cast() {
        return Cast.to(this);
    }
}
