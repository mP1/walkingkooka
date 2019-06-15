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

package walkingkooka.net.header;

import walkingkooka.predicate.character.CharPredicate;

/**
 * A {@link HeaderValueParser} that rebuilds the {@link String} with everything but comments.
 */
final class CommentRemovingHeaderValueParser extends HeaderValueParser {

    static String removeComments(final String text) {
        return text.isEmpty() ?
                text :
                removeCommentsNonEmptyText(text);
    }

    private static String removeCommentsNonEmptyText(final String text) {
        final CommentRemovingHeaderValueParser parser = new CommentRemovingHeaderValueParser(text);
        parser.parse();
        return parser.without.toString();
    }

    private CommentRemovingHeaderValueParser(final String text) {
        super(text);
    }

    @Override
    void whitespace() {
        final int start = this.position;

        this.skipWhitespace();

        this.text(this.text.substring(start, this.position));
    }

    @Override
    void tokenSeparator() {
        this.character(TOKEN_SEPARATOR);
    }

    @Override
    void keyValueSeparator() {
        this.character(KEYVALUE_SEPARATOR);
    }

    @Override
    void multiValueSeparator() {
        this.character(MULTIVALUE_SEPARATOR);
    }

    @Override
    void wildcard() {
        this.character(WILDCARD);
        this.position++;
    }

    @Override
    void slash() {
        this.character(SLASH);
    }

    @Override
    void quotedText() {
        this.text(this.quotedText(QUOTED_PARAMETER_VALUE, true));
    }

    final static CharPredicate QUOTED_PARAMETER_VALUE = ASCII;

    @Override
    void comment() {
        this.skipComment();
    }

    @Override
    void token() {
        this.character(this.character());
        this.position++;
    }

    @Override
    void endOfText() {
        // nothing extra to do
    }

    @Override
    void missingValue() {
        // ignore
    }

    /**
     * Records a character, typically invoked from one of the callbacks.
     */
    private void character(final char c) {
        this.without.append(c);
    }

    private void text(final CharSequence text) {
        this.without.append(text);
    }

    private final StringBuilder without = new StringBuilder();
}
