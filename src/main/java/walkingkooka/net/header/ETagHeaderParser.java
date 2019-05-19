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

package walkingkooka.net.header;

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

/**
 * Base parser for both etag parsers.
 */
abstract class ETagHeaderParser extends HeaderParser {

    /**
     * Package private to limit sub classing.
     */
    ETagHeaderParser(final String text) {
        super(text);
    }

    @Override
    final void whitespace() {
        this.whitespace0(); // skip whitespace
    }

    @Override
    final void keyValueSeparator() {
        this.failInvalidCharacter();
    }

    @Override
    final void wildcard() {
        if (!this.requireValue) {
            this.failInvalidCharacter();
        }
        this.etag(ETag.wildcard());
        this.position++;
        this.requireValue = false;
    }

    @Override
    void slash() {
        this.failInvalidCharacter();
    }

    @Override
    void quotedText() {
        if (!this.requireValue) {
            this.failInvalidCharacter();
        }

        final String quotedText = this.quotedText(ETAG_VALUE, ESCAPING_UNSUPPORTED);
        this.etag(this.validator.setValue(quotedText.substring(1, quotedText.length() - 1)));
        this.requireValue = false;
    }

    /**
     * Comments are not allowed within ETAGS.
     */
    @Override
    final void comment() {
        this.failInvalidCharacter();
    }

    @Override
    void token() {
        if ('W' != this.character()) {
            this.failInvalidCharacter();
        }
        // W must be following etag in quotes...
        if (!this.requireValue) {
            this.failInvalidCharacter();
        }
        this.position++;

        if (!this.hasMoreCharacters()) {
            fail(incompleteWeakIndicator(text));
        }
        if ('/' != this.character()) {
            this.failInvalidCharacter();
        }
        this.position++;
        this.validator = ETagValidator.WEAK;
        this.requireValue = true;
    }

    @Override
    void endOfText() {
        if (this.requireValue) {
            this.missingValue();
        }
    }

    abstract void etag(final ETag etag);

    boolean requireValue = true;
    String value = null;
    ETagValidator validator = ETagValidator.STRONG;

    /**
     * A {@link CharPredicate} that match the content within an ETAG quoted string.<br>
     * <a href="https://tools.ietf.org/html/rfc7232#section-2.3></a>
     * <pre>
     *  etagc      = %x21 / %x23-7E / obs-text
     *             ; VCHAR except double quotes, plus obs-text
     * </pre>
     */
    final static CharPredicate ETAG_VALUE = CharPredicates.builder()//
            .or(CharPredicates.is('\u0021'))//
            .or(CharPredicates.range('\u0023', '\u007e'))
            .toString("e tag quoted value character")//
            .build();

    @Override
    void missingValue() {
        this.failMissingValue(VALUE);
    }

    final static String VALUE = "value";

    /**
     * Reports an incomplete weak indicator.
     */
    static String incompleteWeakIndicator(final String text) {
        return "Incomplete weak indicator " + CharSequences.quote(text);
    }
}
