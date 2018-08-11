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
package walkingkooka.text.cursor.parser.spreadsheet;

import org.junit.Test;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenTestCase;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class SpreadsheetParserTokenTestCase<T extends SpreadsheetParserToken> extends ParserTokenTestCase<T> {

    @Test
    public final void testPublicStaticFactoryMethod()  {
        this.publicStaticFactoryCheck(SpreadsheetParserToken.class, "Spreadsheet", ParserToken.class);
    }

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
        final String prefix = "Spreadsheet";
        final String suffix = ParserToken.class.getSimpleName();

        final T token = this.createToken();
        final String name = token.getClass().getSimpleName();
        assertEquals(name + " starts with " + prefix, true, name.startsWith(prefix));
        assertEquals(name + " ends with " + suffix, true, name.endsWith(suffix));

        final String isMethodName = "is" + CharSequences.capitalize(name.substring(prefix.length(), name.length() - suffix.length()));

        for(Method method : token.getClass().getMethods()) {
            if(MethodAttributes.STATIC.is(method)) {
                continue;
            }
            final String methodName = method.getName();
            if(methodName.equals("isNoise")) {
                continue;
            }
            if(methodName.equals("isSymbol")) {
                continue;
            }

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

    final void toExpressionNodeAndFail() {
        this.toExpressionNodeAndFail(this.createToken());
    }

    final void toExpressionNodeAndFail(final T token) {
        final Optional<ExpressionNode> node = token.expressionNode();
        assertEquals("toExpressionNode", Optional.empty(), node);
    }

    final void toExpressionNodeAndCheck(final ExpressionNode expected) {
        this.toExpressionNodeAndCheck(this.createToken(), expected);
    }

    final void toExpressionNodeAndCheck(final T token, final ExpressionNode expected) {
        assertNotNull( "token", token);
        assertNotNull( "expected", expected);

        final Optional<ExpressionNode> node = this.createToken().expressionNode();
        assertEquals("toExpressionNode", Optional.of(expected), node);
    }
}
