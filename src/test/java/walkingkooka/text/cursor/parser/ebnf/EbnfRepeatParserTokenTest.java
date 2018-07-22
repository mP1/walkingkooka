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

import java.util.List;

public class EbnfRepeatParserTokenTest extends EbnfExceptionGroupOptionalRepeatParentParserTokenTestCase<EbnfRepeatedParserToken> {

    @Override
    EbnfRepeatedParserToken createToken(final String text, final List<EbnfParserToken> tokens) {
        return EbnfRepeatedParserToken.with(tokens, text);
    }

    @Override
    String openChar() {
        return "(";
    }

    @Override
    String closeChar() {
        return ")";
    }

    @Override
    protected Class<EbnfRepeatedParserToken> type() {
        return EbnfRepeatedParserToken.class;
    }
}
