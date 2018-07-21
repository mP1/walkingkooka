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

public final class EbnfWhitespaceParserTokenTest extends EbnfLeafParserTokenTestCase<EbnfWhitespaceParserToken, String> {

    @Override
    String text() {
        return " ";
    }

    String value() {
        return this.text();
    }

    @Override
    protected EbnfWhitespaceParserToken createToken(final String value, final String text) {
        return EbnfWhitespaceParserToken.with(value, text);
    }

    @Override
    protected EbnfWhitespaceParserToken createDifferentToken() {
        return EbnfWhitespaceParserToken.with("\n\r", "\n\r");
    }

    @Override
    protected Class<EbnfWhitespaceParserToken> type() {
        return EbnfWhitespaceParserToken.class;
    }
}
