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

import org.junit.jupiter.api.Test;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenTestCase;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetParserTokenTestCase<T extends SpreadsheetParserToken> extends ParserTokenTestCase<T> {

    @Test
    public final void testPublicStaticFactoryMethod()  {
        this.publicStaticFactoryCheck(SpreadsheetParserToken.class, "Spreadsheet", ParserToken.class);
    }

    @Test
    public void testWithEmptyTextFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken("");
        });
    }

    @Test
    public void testWithWhitespaceTextFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken("   ");
        });
    }

    @Test
    public void testSetTextEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken().setText("");
        });
    }

    @Test
    public void testSetTextWhitespaceFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken().setText("   ");
        });
    }

    @Test
    public void testIsMethods() throws Exception {
        final String prefix = "Spreadsheet";
        final String suffix = ParserToken.class.getSimpleName();

        final T token = this.createToken();
        final String name = token.getClass().getSimpleName();
        assertEquals(true, name.startsWith(prefix), name + " starts with " + prefix);
        assertEquals(true, name.endsWith(suffix), name + " ends with " + suffix);

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
            assertEquals(methodName.equals(isMethodName),
                    method.invoke(token),
                    method + " returned");
        }
    }

    @Test
    public void testWithoutCommentsSymbolsOrWhitespacePropertiesNullCheck() throws Exception {
        final Optional<SpreadsheetParserToken> without = this.createToken().withoutSymbols();
        if(without.isPresent()){
            this.propertiesNeverReturnNullCheck(without.get());
        }
    }

    final void toExpressionNodeAndFail() {
        this.toExpressionNodeAndFail(this.createToken());
    }

    final void toExpressionNodeAndFail(final T token) {
        final Optional<ExpressionNode> node = token.expressionNode();
        assertEquals(Optional.empty(), node, "toExpressionNode");
    }

    final void toExpressionNodeAndCheck(final ExpressionNode expected) {
        this.toExpressionNodeAndCheck(this.createToken(), expected);
    }

    final void toExpressionNodeAndCheck(final T token, final ExpressionNode expected) {
        final Optional<ExpressionNode> node = this.createToken().expressionNode();
        assertEquals(Optional.of(expected), node, "toExpressionNode");
    }
}
