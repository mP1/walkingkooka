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

import walkingkooka.InvalidCharacterException;
import walkingkooka.NeverError;

/**
 * Assumes currently positioned at the start of a comment and consumes the entire comment including the closing right parenthesis.
 * The returned text does not include the surround parens.
 * <a href="https://tools.ietf.org/html/rfc7230#section-3.2.6">Field Value Comments</a>
 * <br>
 * <pre>
 * 3.2.6.  Field Value Components
 *
 *    Most HTTP header field values are defined using common syntax
 *    components (token, quoted-string, and comment) separated by
 *    whitespace or specific delimiting characters.  Delimiters are chosen
 *    from the set of US-ASCII visual characters not allowed in a token
 *    (DQUOTE and "(),/:;<=>?@[\]{}").
 *
 *      token          = 1*tchar
 *
 *      tchar          = "!" / "#" / "$" / "%" / "&" / "'" / "*"
 *                     / "+" / "-" / "." / "^" / "_" / "`" / "|" / "~"
 *                     / DIGIT / ALPHA
 *                     ; any VCHAR, except delimiters
 *
 *    A string of text is parsed as a single value if it is quoted using
 *    double-quote marks.
 *
 *      quoted-string  = DQUOTE *( qdtext / quoted-pair ) DQUOTE
 *      qdtext         = HTAB / SP /%x21 / %x23-5B / %x5D-7E / obs-text
 *      obs-text       = %x80-FF
 *
 *    Comments can be included in some HTTP header fields by surrounding
 *    the comment text with parentheses.  Comments are only allowed in
 *    fields containing "comment" as part of their field value definition.
 *
 *      comment        = "(" *( ctext / quoted-pair / comment ) ")"
 *      ctext          = HTAB / SP / %x21-27 / %x2A-5B / %x5D-7E / obs-text
 *
 *    The backslash octet ("\") can be used as a single-octet quoting
 *    mechanism within quoted-string and comment constructs.  Recipients
 *    that process the value of a quoted-string MUST handle a quoted-pair
 *    as if it were replaced by the octet following the backslash.
 *
 *      quoted-pair    = "\" ( HTAB / SP / VCHAR / obs-text )
 *
 *    A sender SHOULD NOT generate a quoted-pair in a quoted-string except
 *    where necessary to quote DQUOTE and backslash octets occurring within
 *    that string.  A sender SHOULD NOT generate a quoted-pair in a comment
 *    except where necessary to quote parentheses ["(" and ")"] and
 *    backslash octets occurring within that comment.
 * </pre>
 */
enum HeaderParserComments {
    COMMENT_TEXT {
        @Override
        HeaderParserComments singleQuote() {
            return SINGLE_QUOTES;
        }

        @Override
        HeaderParserComments doubleQuote() {
            return DOUBLE_QUOTES;
        }

        @Override
        HeaderParserComments backslash(final String text, final int position) {
            throw new InvalidCharacterException(text, position);
        }

        @Override
        HeaderParserComments parensClose() {
            return FINISHED;
        }

        @Override
        HeaderParserComments character(final char c) {
            return COMMENT_TEXT;
        }

        @Override
        String endOfText(final String text) {
            return HeaderParser.missingClosingParens(text);
        }
    },
    SINGLE_QUOTES {
        @Override
        HeaderParserComments singleQuote() {
            return COMMENT_TEXT;
        }

        @Override
        HeaderParserComments doubleQuote() {
            return SINGLE_QUOTES;
        }

        @Override
        HeaderParserComments backslash(final String text, final int position) {
            return SINGLE_QUOTES_BACKSLASH_ESCAPING;
        }

        @Override
        HeaderParserComments parensClose() {
            return this;
        }

        @Override
        HeaderParserComments character(final char c) {
            return SINGLE_QUOTES;
        }

        @Override
        String endOfText(final String text) {
            return HeaderParser.missingClosingQuote(text);
        }
    },
    SINGLE_QUOTES_BACKSLASH_ESCAPING {
        @Override
        HeaderParserComments singleQuote() {
            return SINGLE_QUOTES;
        }

        @Override
        HeaderParserComments doubleQuote() {
            return SINGLE_QUOTES;
        }

        @Override
        HeaderParserComments backslash(final String text, final int position) {
            return SINGLE_QUOTES;
        }

        @Override
        HeaderParserComments parensClose() {
            return SINGLE_QUOTES;
        }

        @Override
        HeaderParserComments character(final char c) {
            return SINGLE_QUOTES;
        }

        @Override
        String endOfText(final String text) {
            return HeaderParser.missingClosingQuote(text);
        }
    },
    DOUBLE_QUOTES {
        @Override
        HeaderParserComments singleQuote() {
            return DOUBLE_QUOTES;
        }

        @Override
        HeaderParserComments doubleQuote() {
            return COMMENT_TEXT;
        }

        @Override
        HeaderParserComments backslash(final String text, final int position) {
            return DOUBLE_QUOTES_BACKSLASH_ESCAPING;
        }

        @Override
        HeaderParserComments parensClose() {
            return FINISHED;
        }

        @Override
        HeaderParserComments character(final char c) {
            return DOUBLE_QUOTES;
        }

        @Override
        String endOfText(final String text) {
            return HeaderParser.missingClosingQuote(text);
        }
    },
    DOUBLE_QUOTES_BACKSLASH_ESCAPING {
        @Override
        HeaderParserComments singleQuote() {
            return DOUBLE_QUOTES;
        }

        @Override
        HeaderParserComments doubleQuote() {
            return DOUBLE_QUOTES;
        }

        @Override
        HeaderParserComments backslash(final String text, final int position) {
            return DOUBLE_QUOTES;
        }

        @Override
        HeaderParserComments parensClose() {
            return DOUBLE_QUOTES;
        }

        @Override
        HeaderParserComments character(final char c) {
            return DOUBLE_QUOTES;
        }

        @Override
        String endOfText(final String text) {
            return HeaderParser.missingClosingQuote(text);
        }
    },

    /**
     * This mode indicates the end of a comment has been reached.
     */
    FINISHED {
        @Override
        HeaderParserComments singleQuote() {
            return NeverError.unhandledCase(this, HeaderParserComments.values());
        }

        @Override
        HeaderParserComments doubleQuote() {
            return NeverError.unhandledCase(this, HeaderParserComments.values());
        }

        @Override
        HeaderParserComments backslash(final String text, final int position) {
            return NeverError.unhandledCase(this, HeaderParserComments.values());
        }

        @Override
        HeaderParserComments parensClose() {
            return NeverError.unhandledCase(this, HeaderParserComments.values());
        }

        @Override
        HeaderParserComments character(final char c) {
            return NeverError.unhandledCase(this, HeaderParserComments.values());
        }

        @Override
        String endOfText(final String text) {
            return NeverError.unhandledCase(this, HeaderParserComments.values());
        }
    };

    /**
     * Assumes a starting position just after the opening parens and consumes the comment entirely returning
     * the position at the end of the comment.
     */
    static int consume(final String text, final int position) {
        int p = position + 1;

        HeaderParserComments mode = HeaderParserComments.COMMENT_TEXT;

        final int end = text.length();
        do {
            if (p >= end) {
                HeaderParser.fail(mode.endOfText(text));
            }
            mode = mode.accept(text, p);
            p++;

            if (mode.isFinished()) {
                break;
            }
        } while (true);

        return p;
    }

    /**
     * Accepts a single character and returns the new mode or fails if invalid.
     */
    final HeaderParserComments accept(final String text, final int position) {

        HeaderParserComments mode = this;

        final char c = text.charAt(position);

        //comment        = "(" *( ctext / quoted-pair / comment ) ")"
        //ctext          = HTAB / SP / %x21-27 / %x2A-5B / %x5D-7E / obs-text
        switch (c) {
            case 0x0:
            case 0x1:
            case 0x2:
            case 0x3:
            case 0x4:
            case 0x5:
            case 0x6:
            case 0x7:
            case 0x8:
            case 0xA:
            case 0xB:
            case 0xC:
            case 0xD:
            case 0xE:

            case 0x10:
            case 0x11:
            case 0x12:
            case 0x13:
            case 0x14:
            case 0x15:
            case 0x16:
            case 0x17:
            case 0x18:
            case 0x19:
            case 0x1A:
            case 0x1B:
            case 0x1C:
            case 0x1D:
            case 0x1E:
            case 0x28:
                throw new InvalidCharacterException(text, position);

            case '\t': // HTAB
                break;
            case ' ': // SP
                break;
            case '\'':
                mode = this.singleQuote();
                break;
            case '"':
                mode = this.doubleQuote();
                break;
            case '\\':
                mode = this.backslash(text, position);
                break;
            case ')':
                mode = this.parensClose();
                break;
            // other visible characters are ok!
            default:
                mode = this.character(c);
        }

        return mode;
    }

    /**
     * Callback when a single quote is encountered.
     */
    abstract HeaderParserComments singleQuote();

    /**
     * Callback when a double quote is encountered.
     */
    abstract HeaderParserComments doubleQuote();

    /**
     * Callback when a backslash is encountered.
     */
    abstract HeaderParserComments backslash(final String text, final int position);

    /**
     * Callback when a closing parenthesis is encountered.
     */
    abstract HeaderParserComments parensClose();

    /**
     * Callback when a visible character is encountered.
     */
    abstract HeaderParserComments character(final char c);

    /**
     * Returns true if the state machine is finished.
     */
    final boolean isFinished() {
        return this == FINISHED;
    }

    /**
     * Returns the appropriate error message text when the end of text is reached.
     */
    abstract String endOfText(final String text);
}
