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
package walkingkooka.text.cursor.parser.json;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenTestCase;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public abstract class JsonNodeParserTokenTestCase<T extends JsonNodeParserToken> extends ParserTokenTestCase<T> {

    @Test
    public final void testPublicStaticFactoryMethod()  {
        this.publicStaticFactoryCheck(JsonNodeParserToken.class, "JsonNode", ParserToken.class);
    }

    @Test(expected =  IllegalArgumentException.class)
    public final void testEmptyTextFails() {
        this.createToken("");
    }

    @Test
    public void testIsMethods() throws Exception {
        final String prefix = "JsonNode";
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

    final JsonNodeParserToken arrayBegin() {
        return JsonNodeParserToken.arrayBeginSymbol("[", "[");
    }

    final JsonNodeParserToken arrayEnd() {
        return JsonNodeParserToken.arrayEndSymbol("]", "]");
    }

    final JsonNodeParserToken array(final JsonNodeParserToken...tokens) {
        return JsonNodeParserToken.array(Lists.of(tokens), text(tokens));
    }

    final JsonNodeParserToken booleanFalse() {
        return booleanToken(false);
    }

    final JsonNodeParserToken booleanTrue() {
        return booleanToken(true);
    }

    final JsonNodeParserToken booleanToken(final boolean value) {
        return JsonNodeParserToken.booleanParserToken(value, String.valueOf(value));
    }

    final JsonNodeParserToken nul() {
        return JsonNodeParserToken.nullParserToken("null");
    }

    final JsonNodeParserToken number(final int value) {
        // accept only int, keeps the creation of the matching text simple.
        return JsonNodeParserToken.number(value, String.valueOf(value));
    }

    final JsonNodeParserToken objectAssignment() {
        return JsonNodeParserToken.objectAssignmentSymbol(":", ":");
    }

    final JsonNodeParserToken objectBegin() {
        return JsonNodeParserToken.objectBeginSymbol("{", "{");
    }

    final JsonNodeParserToken objectEnd() {
        return JsonNodeParserToken.objectEndSymbol("}", "}");
    }

    final JsonNodeParserToken object(final JsonNodeParserToken...tokens) {
        return JsonNodeParserToken.object(Lists.of(tokens), text(tokens));
    }

    private static String text(final JsonNodeParserToken...tokens){
        return ParserToken.text(Lists.of(tokens));
    }

    final JsonNodeParserToken separator() {
        return JsonNodeParserToken.separatorSymbol(",", ",");
    }

    final JsonNodeParserToken string(final String value) {
        return JsonNodeParserToken.string(value, CharSequences.quoteAndEscape(value).toString());
    }

    final JsonNodeParserToken whitespace() {
        return JsonNodeParserToken.whitespace("  ", "  ");
    }
}
