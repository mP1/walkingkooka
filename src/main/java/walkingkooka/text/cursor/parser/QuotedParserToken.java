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

import walkingkooka.tree.search.SearchNode;

/**
 * The parser token for a matched quoted string.
 */
public abstract class QuotedParserToken extends ParserTemplateToken<String> implements LeafParserToken<String> {

    /**
     * Ctor only called by {@link CharacterCharPredicateParser}
     */
    QuotedParserToken(final String value, final String text) {
        super(value, text);
    }

    abstract char quotedCharacter();

    @Override
    final boolean equals1(final ParserTemplateToken<?> other) {
        return true; // no extra properties to compare
    }

    // HasSearchNode ...............................................................................................

    @Override
    public final SearchNode toSearchNode()  {
        final String text = this.text();
        return SearchNode.text(text, text);
    }
}

