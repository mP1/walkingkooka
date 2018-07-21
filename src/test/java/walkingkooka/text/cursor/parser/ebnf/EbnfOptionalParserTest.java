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
package walkingkooka.text.cursor.parser.ebnf;

import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

public final class EbnfOptionalParserTest extends EbnfParserTestCase4<EbnfOptionalParserToken> {

    @Override
    protected Parser<ParserToken, EbnfParserContext> createParser() {
        return EbnfGrammarParser.OPTIONAL;
    }

    @Override
    char beginChar() {
        return '[';
    }

    @Override
    char endChar() {
        return ']';
    }

    @Override
    EbnfOptionalParserToken token(final String text, final List<EbnfParserToken> tokens) {
        return EbnfParserToken.optional(tokens, text);
    }
}
