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

import org.junit.jupiter.api.Test;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenTestCase;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class EbnfParserTokenTestCase<T extends EbnfParserToken> extends ParserTokenTestCase<T> {

    @Test
    public final void testPublicStaticFactoryMethod()  {
        this.publicStaticFactoryCheck(EbnfParserToken.class, "Ebnf", ParserToken.class);
    }

    @Test
    public final void testEmptyTextFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken("");
        });
    }

    @Test
    public void testIsMethods() throws Exception {
        final T token = this.createToken();
        final String name = token.getClass().getSimpleName();
        assertEquals(true, name.startsWith("Ebnf"), name + " starts with Ebnf");
        assertEquals(true, name.endsWith("ParserToken"), name + " ends with ParserToken");

        final String isMethodName = "is" + CharSequences.capitalize(name.substring(4, name.length() - "ParserToken".length()));

        for(Method method : token.getClass().getMethods()) {
            if(MethodAttributes.STATIC.is(method)) {
                continue;
            }
            final String methodName = method.getName();
            if(methodName.equals("isNoise")) {
                continue;
            }

            if(!methodName.startsWith("is")) {
                continue;
            }
            assertEquals(methodName.equals(isMethodName),
                    method.invoke(token),
                    method + " returned");
        }
    }

    @Test
    public void testWithoutCommentsSymbolsOrWhitespacePropertiesNullCheck() throws Exception {
        final Optional<EbnfParserToken> without = this.createToken().withoutCommentsSymbolsOrWhitespace();
        if(without.isPresent()){
            this.propertiesNeverReturnNullCheck(without.get());
        }
    }

    static EbnfSymbolParserToken symbol(final String s) {
        return EbnfParserToken.symbol(s, s);
    }
}
