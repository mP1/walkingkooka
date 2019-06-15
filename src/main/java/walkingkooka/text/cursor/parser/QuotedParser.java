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
package walkingkooka.text.cursor.parser;

import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.util.Optional;

/**
 * This parser matches quoted strings, with support backslash escaping and unicode sequences in the form of backlash-u-4-hex-digits
 */
abstract class QuotedParser<C extends ParserContext> extends Parser2<C> {

    QuotedParser() {
    }

    @Override
    Optional<ParserToken> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint start) {
        return this.quoteChar() == cursor.at() ?
                this.tryParse1(cursor, start) :
                this.fail();
    }

    abstract char quoteChar();

    private Optional<ParserToken> tryParse1(final TextCursor cursor, final TextCursorSavePoint start) {
        final char quote = this.quoteChar();

        cursor.next();

        Optional<ParserToken> result = null;
        boolean backslashed = false;
        int unicodeDigitCounter = -1;
        char unicodeCharValue = 0;
        final StringBuilder raw = new StringBuilder();

        for (; !cursor.isEmpty(); cursor.next()) {
            final char c = cursor.at();
            if (backslashed) {
                backslashed = false;
                switch (c) {
                    case '0':
                        raw.append('\0');
                        break;
                    case 'f':
                        raw.append('\f');
                        break;
                    case 't':
                        raw.append('\t');
                        break;
                    case 'n':
                        raw.append('\n');
                        break;
                    case 'r':
                        raw.append('\r');
                        break;
                    case '\'':
                        raw.append('\'');
                        break;
                    case '"':
                        raw.append('"');
                        break;
                    case 'u':
                        unicodeDigitCounter = 0;
                        unicodeCharValue = 0;
                        break;
                    default:
                        throw new ParserException(invalidBackslashEscapeChar(c));
                }
                continue;
            }
            if (unicodeDigitCounter >= 0) {
                final int hex = Character.digit(c, 16);
                if (-1 == hex) {
                    throw new ParserException(invalidUnicodeEscapeChar(c));
                }
                unicodeCharValue = (char) (unicodeCharValue * 16 + hex);
                unicodeDigitCounter++;
                if (unicodeDigitCounter == 4) {
                    raw.append(unicodeCharValue);
                    unicodeDigitCounter = -1;
                }
                continue;
            }
            // closing quote found...
            if (quote == c) {
                cursor.next();
                result = this.token(raw.toString(),
                        start.textBetween().toString())
                        .success();
                break;
            }

            backslashed = '\\' == c;
            if (!backslashed) {
                raw.append(c);
            }
        }

        if (null == result) {
            throw new ParserException(missingTerminatingQuote(quote));
        }

        return result;
    }

    /**
     * Factory method that creates the token upon a successful match.
     */
    abstract QuotedParserToken token(final String content, final String rawText);

    // VisibleForTesting
    static String missingTerminatingQuote(final char quote) {
        return "Missing terminating '" + quote + "'";
    }

    // VisibleForTesting
    static String invalidBackslashEscapeChar(final char c) {
        return "Unexpected escape character '" + c + "'";
    }

    // VisibleForTesting
    static String invalidUnicodeEscapeChar(final char c) {
        return "Expected hex character in unicode escape but got '" + c + "'";
    }

    public abstract String toString();
}

