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

package walkingkooka.text.cursor.parser.ebnf;

import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.Optional;

/**
 * Handles parsing a terminal literal, including support for backslash escape sequence and unicode sequences.
 */
final class EbnfTerminalParser implements Parser<EbnfParserContext> {

    /**
     * Singleton instance
     */
    final static Parser<EbnfParserContext> INSTANCE = new EbnfTerminalParser().cast();

    private EbnfTerminalParser() {
        super();
    }

    @Override
    public Optional<ParserToken> parse(final TextCursor cursor, final EbnfParserContext context) {
        EbnfTerminalParserToken result = null;

        for (; ; ) {
            if (cursor.isEmpty()) {
                break;
            }
            final TextCursorSavePoint start = cursor.save();
            final StringBuilder raw = new StringBuilder();

            final char open = cursor.at();
            if (open != '\'' && open != '"') {
                break;
            }

            cursor.next();
            boolean backslash = false;
            char unicodeChar = 0;
            int unicodeIndex = -1;

            for (; ; ) {
                if (cursor.isEmpty()) {
                    break;
                }
                final char c = cursor.at();
                cursor.next();

                if (backslash) {
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
                            unicodeChar = 0;
                            unicodeIndex = 0;
                            break;
                        default:
                            throw new EbnfTerminalParserException("Invalid backslash sequence '" + c + "'");
                    }
                    backslash = false;
                    continue;
                }
                if (unicodeIndex >= 0) {
                    final int digit = Character.digit(c, 16);
                    if (-1 == digit) {
                        throw new EbnfTerminalParserException("Invalid unicode sequence '" + c + "'");
                    }
                    unicodeChar = (char) (unicodeChar * 16 + digit);
                    unicodeIndex++;
                    if (unicodeIndex == 4) {
                        unicodeIndex = -1;
                        raw.append(unicodeChar);
                    }
                    continue;
                }

                // closing quote character...
                if (open == c) {
                    result = EbnfTerminalParserToken.with(
                            raw.toString(),
                            start.textBetween().toString());
                    break;
                }
                if ('\\' == c) {
                    backslash = true;
                } else {
                    raw.append(c);
                }
            }
            break;
        }

        return Optional.ofNullable(result);
    }

    public String toString() {
        return "terminal";
    }
}
