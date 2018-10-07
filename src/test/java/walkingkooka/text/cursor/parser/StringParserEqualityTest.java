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

public final class StringParserEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<StringParser<ParserContext>> {

    private final static String TEXT = "text123";
    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;

    @Test
    public void testDifferentText() {
        this.checkNotEquals(this.createObject("different", CASE_SENSITIVITY));
    }

    @Test
    public void testDifferentCaseSensitivity() {
        this.checkNotEquals(this.createObject(TEXT, CaseSensitivity.INSENSITIVE));
    }

    @Override
    protected StringParser<ParserContext> createObject() {
        return this.createObject(TEXT, CASE_SENSITIVITY);
    }

    protected StringParser<ParserContext> createObject(final String text, final CaseSensitivity caseSensitivity) {
        return StringParser.with(text, caseSensitivity).cast();
    }
}
