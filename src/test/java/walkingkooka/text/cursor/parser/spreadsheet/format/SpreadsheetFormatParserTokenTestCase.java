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
package walkingkooka.text.cursor.parser.spreadsheet.format;

import org.junit.Test;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenTestCase;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public abstract class SpreadsheetFormatParserTokenTestCase<T extends SpreadsheetFormatParserToken> extends ParserTokenTestCase<T> {

    @Test
    public final void testPublicStaticFactoryMethod() {
        this.publicStaticFactoryCheck(SpreadsheetFormatParserToken.class, "SpreadsheetFormat", ParserToken.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyTextFails() {
        this.createToken("");
    }

    @Test
    public final void testWithWhitespace() {
        final String text = " ";
        final T token = this.createToken(text);
        this.checkText(token, text);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTextEmptyFails() {
        this.createToken().setText("");
    }

    @Test
    public void testIsMethods() throws Exception {
        final String prefix = "SpreadsheetFormat";
        final String suffix = ParserToken.class.getSimpleName();

        final T token = this.createToken();
        final String name = token.getClass().getSimpleName();
        assertEquals(name + " starts with " + prefix, true, name.startsWith(prefix));
        assertEquals(name + " ends with " + suffix, true, name.endsWith(suffix));

        final String isMethodName = "is" + CharSequences.capitalize(name.substring(prefix.length(), name.length() - suffix.length()));

        for (Method method : token.getClass().getMethods()) {
            if (MethodAttributes.STATIC.is(method)) {
                continue;
            }
            final String methodName = method.getName();
            if (methodName.equals("isCondition")) {
                continue;
            }
            if (methodName.equals("isNoise")) {
                continue;
            }
            if (methodName.equals("isSymbol")) {
                continue;
            }

            if (!methodName.startsWith("is")) {
                continue;
            }
            assertEquals(method + " returned",
                    methodName.equals(isMethodName),
                    method.invoke(token));
        }
    }

    @Test
    public void testWithoutSymbolsPropertiesNullCheck() throws Exception {
        final Optional<SpreadsheetFormatParserToken> without = this.createToken().withoutSymbols();
        if (without.isPresent()) {
            this.propertiesNeverReturnNullCheck(without.get());
        }
    }
}
