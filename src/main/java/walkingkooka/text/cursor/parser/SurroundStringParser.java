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

import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.util.Optional;

/**
 * A {@link Parser} that only requires an opening string and is terminated by another closing string.
 */
final class SurroundStringParser<C extends ParserContext> extends Parser2<StringParserToken, C> {

    static <C extends ParserContext> SurroundStringParser<C> with(final String open, final String close) {
        CharSequences.failIfNullOrEmpty(open, "open");
        CharSequences.failIfNullOrEmpty(close, "close");

        return new SurroundStringParser<>(open, close);
    }

    private SurroundStringParser(final String open, final String close) {
        this.open = open;
        this.close = close;
    }

    @Override
    Optional<StringParserToken> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint start) {
        Optional<StringParserToken> result = Optional.empty();
        int matched = 0;
        final String open = this.open;

        for(;;) {
            if(cursor.isEmpty() || open.charAt(matched) != cursor.at()) {
                break;
            }
            matched++;
            cursor.next();

            if(open.length() == matched) {
                final String close = this.close;
                matched = 0;

                // try matching close...
                for(;;) {
                    if(cursor.isEmpty()) {
                        break;
                    }
                    final char at = cursor.at();
                    if(close.charAt(matched) == at){
                        matched++;
                        cursor.next();

                        if(close.length() == matched) {
                            // close found, match!!!
                            final String text = start.textBetween().toString();
                            result = StringParserToken.with(text, text)
                                    .success();
                            break;
                        }
                        continue;
                    }
                    matched = 0;
                    if(close.charAt(0) == at) {
                        matched++;

                        if(close.length() == matched) {
                            // close found, match!!!
                            final String text = start.textBetween().toString();
                            result = StringParserToken.with(text, text)
                                    .success();
                            break;
                        }
                    }

                    cursor.next();
                }
                // end
                break;
            }
        }

        return result;
    }

    private final String open;

    private final String close;

    @Override
    public String toString() {
        return CharSequences.quoteAndEscape(this.open) + "*" + CharSequences.quoteAndEscape(this.close);
    }
}
