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

import org.junit.Test;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserTokenTestCase;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;

public abstract class EbnfParserTokenTestCase<T extends EbnfParserToken> extends ParserTokenTestCase<T> {

    @Test(expected =  NullPointerException.class)
    public final void testNullTextFails() {
        this.createToken(null);
    }

    @Test(expected =  IllegalArgumentException.class)
    public final void testEmptyTextFails() {
        this.createToken("");
    }

    @Test
    public void testText() {
        assertEquals(this.text(), this.createToken().text());
    }

    @Test
    public void testIsMethods() throws Exception {
        final T token = this.createToken();
        final String name = token.getClass().getSimpleName();
        assertEquals(name + " starts with Ebnf", true, name.startsWith("Ebnf"));
        assertEquals(name + " ends with ParserToken", true, name.endsWith("ParserToken"));

        final String isMethodName = "is" + CharSequences.capitalize(name.substring(4, name.length() - "ParserToken".length()));

        for(Method method : token.getClass().getMethods()) {
            if(MethodAttributes.STATIC.is(method)) {
                continue;
            }
            final String methodName = method.getName();
            if(!methodName.startsWith("is")) {
                continue;
            }
            assertEquals(method + " returned",
                    methodName.equals(isMethodName),
                    method.invoke(token));
        }
    }

    @Test
    public final void testToString() {
        assertEquals(this.text(), this.createToken().toString());
    }

    @Override
    protected final T createToken() {
        return this.createToken(this.text());
    }

    abstract T createToken(final String text);

    abstract String text();

    static EbnfSymbolParserToken symbol(final String s) {
        return EbnfParserToken.symbol(s, s);
    }
}
