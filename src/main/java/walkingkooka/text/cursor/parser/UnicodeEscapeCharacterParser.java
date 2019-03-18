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
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.util.Optional;

/**
 * A {@link Parser} that matches unicode escape sequences returning the decoded character.
 */
final class UnicodeEscapeCharacterParser<C extends ParserContext> extends Parser2<C> {

    /**
     * Type safe singleton getter.
     */
    static <C extends ParserContext> UnicodeEscapeCharacterParser<C> get() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static UnicodeEscapeCharacterParser INSTANCE = new UnicodeEscapeCharacterParser();

    private UnicodeEscapeCharacterParser() {
    }

    @Override
    Optional<ParserToken> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint save) {
        Optional<ParserToken> result = null;

        int value = 0;
        for(int i = 0; i < 6; i++) {
            if(i < 0 || cursor.isEmpty()){
                result = Optional.empty();
                break;
            }
            final char c = cursor.at();
            cursor.next();

            switch(i) {
                case 0:
                    if('\\' != c){
                        i = -1;
                    }
                    break;
                case 1:
                    if('u' != c){
                        i = -1;
                    }
                    break;
                default:
                    final int digitValue = Character.digit(c, 16);
                    if(-1 == digitValue) {
                        i = -1;
                        break;
                    }
                    value = value * 16 + digitValue;
                    break;
            }
        }

        if(null==result) {
            result = ParserTokens.character((char) value, save.textBetween().toString())
                    .success();
        }

        return result;
    }

    @Override
    public String toString() {
        return "Unicode escape char sequence";
    }
}
