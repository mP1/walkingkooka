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

import walkingkooka.NeverError;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

/**
 * Base parser for both tag parsers.
 */
abstract class ETagHeaderParser extends HeaderParser {

    /**
     * Package private to limit sub classing.
     */
    ETagHeaderParser(final String text) {
        super(text);
    }

    /**
     * Parses a ETAG header value.
     * <pre>
     * ETag: "xyzzy"
     * ETag: W/"xyzzy"
     * ETag: ""
     * </pre>
     *
     * <pre>
     * ETag       = entity-tag
     *
     * entity-tag = [ weak ] opaque-tag
     * weak       = %x57.2F ; "W/", case-sensitive
     * opaque-tag = DQUOTE *etagc DQUOTE
     * etagc      = %x21 / %x23-7E / obs-text
     *            ; VCHAR except double quotes, plus obs-text
     * </pre>
     */
    final ETag parse(final int startMode) {
        final char wildcard = ETag.WILDCARD_VALUE.character();

        String value = null;
        ETagValidator validator = ETagValidator.STRONG;

        int mode = startMode;
        int start = -1;

        while (this.hasMoreCharacters()) {
            final char c = this.character();

            switch (mode) {
                case MODE_SEPARATOR:
                    if (ETag.SEPARATOR.character() == c) {
                        mode = MODE_WHITESPACE;
                        break;
                    }
                    failInvalidCharacter();
                case MODE_WHITESPACE:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    // fall thru intentional...
                    mode = MODE_WEAK_OR_WILDCARD_OR_QUOTE_BEGIN;
                case MODE_WEAK_OR_WILDCARD_OR_QUOTE_BEGIN:
                    if ('W' == c) {
                        mode = MODE_WEAK;
                        break;
                    }
                    if (DOUBLE_QUOTE == c) {
                        mode = MODE_VALUE;
                        start = this.position + 1;
                        break;
                    }
                    if (wildcard == c) {
                        mode = MODE_FINISHED;
                        break;
                    }
                    failInvalidCharacter();
                case MODE_WEAK:
                    if ('/' == c) {
                        mode = MODE_QUOTE_BEGIN;
                        validator = ETagValidator.WEAK;
                        break;
                    }
                    failInvalidCharacter();
                case MODE_QUOTE_BEGIN:
                    if (DOUBLE_QUOTE == c) {
                        mode = MODE_VALUE;
                        start = this.position + 1;
                        break;
                    }
                    if (ETAG_VALUE.test(c)) {
                        break;
                    }
                    failInvalidCharacter();
                case MODE_VALUE:
                    if (DOUBLE_QUOTE == c) {
                        value = this.text.substring(start, this.position);
                        mode = MODE_FINISHED;
                        break;
                    }
                    if (ETAG_VALUE.test(c)) {
                        break;
                    }
                    failInvalidCharacter();
                case MODE_FINISHED:
                    failInvalidCharacter();
                default:
                    NeverError.unhandledCase(mode,
                            MODE_WHITESPACE, MODE_WEAK_OR_WILDCARD_OR_QUOTE_BEGIN, MODE_WEAK, MODE_QUOTE_BEGIN, MODE_VALUE, MODE_FINISHED);
            }
            this.position++;

            if (mode == MODE_FINISHED) {
                break;
            }
        }

        switch (mode) {
            case MODE_WHITESPACE:
                this.fail(missingETagValue(text));
            case MODE_WEAK_OR_WILDCARD_OR_QUOTE_BEGIN:
                this.fail(missingETagValue(text));
            case MODE_WEAK:
                this.fail(incompleteWeakIndicator(text));
            case MODE_QUOTE_BEGIN:
                this.fail(missingETagValue(text));
            case MODE_VALUE:
                this.fail(missingClosingQuote(text));
            case MODE_FINISHED:
                break;
            default:
                NeverError.unhandledCase(mode, MODE_WEAK_OR_WILDCARD_OR_QUOTE_BEGIN, MODE_WEAK, MODE_VALUE, MODE_FINISHED);
        }

        return null == value ?
                ETag.wildcard() :
                ETag.with(value, validator);
    }

    final static int MODE_SEPARATOR = 1;
    private final static int MODE_WHITESPACE = MODE_SEPARATOR + 1;
    final static int MODE_WEAK_OR_WILDCARD_OR_QUOTE_BEGIN = MODE_WHITESPACE + 1;
    private final static int MODE_WEAK = MODE_WEAK_OR_WILDCARD_OR_QUOTE_BEGIN + 1;
    private final static int MODE_QUOTE_BEGIN = MODE_WEAK + 1;
    private final static int MODE_VALUE = MODE_QUOTE_BEGIN + 1;
    private final static int MODE_FINISHED = MODE_VALUE + 1;

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

    /**
     * Reports a missing etag value.
     */
    static String missingETagValue(final String text) {
        return "Missing etag " + CharSequences.quote(text);
    }

    /**
     * Reports an incomplete weak indicator.
     */
    static String incompleteWeakIndicator(final String text) {
        return "Incomplete weak indicator " + CharSequences.quote(text);
    }

    /**
     * Called whenever a separator is encountered.
     */
    abstract void separator();
}
