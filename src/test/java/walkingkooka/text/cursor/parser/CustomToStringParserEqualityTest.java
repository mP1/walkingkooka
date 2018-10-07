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

package walkingkooka.text.cursor.parser;

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;
import walkingkooka.text.CaseSensitivity;

public final class CustomToStringParserEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<CustomToStringParser<ParserToken, ParserContext>> {

    private final static String PARSER_TEXT = "text123";
    private final static String CUSTOM_TOSTRING = "CustomToString456";

    @Test
    public void testDifferentParser() {
        this.checkNotEquals(this.createObject("different", CUSTOM_TOSTRING));
    }

    @Test
    public void testDifferentCustomToString() {
        this.checkNotEquals(this.createObject(PARSER_TEXT, "different"));
    }

    @Override
    protected CustomToStringParser<ParserToken, ParserContext> createObject() {
        return this.createObject(PARSER_TEXT, CUSTOM_TOSTRING);
    }

    protected CustomToStringParser<ParserToken, ParserContext> createObject(final String parserText, final String customToString) {
        final Parser<ParserToken, ParserContext> parser = CaseSensitivity.SENSITIVE.parser(parserText).cast();
        return CustomToStringParser.wrap(parser.cast(), customToString).cast();
    }
}
