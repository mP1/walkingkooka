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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserTestCase3;
import walkingkooka.text.cursor.parser.ParserToken;

public final class JsonNodeParsersTest extends ParserTestCase3<Parser<JsonNodeParserToken, JsonNodeParserContext>,
        JsonNodeParserToken,
        JsonNodeParserContext> {

    @Test
    public void testBooleanFalse() {
        final String text = "false";

        this.parseAndCheck(text, booleanFalse(), text);
    }

    @Test
    public void testBooleanTrue() {
        final String text = "true";

        this.parseAndCheck(text, booleanTrue(), text);
    }

    @Test
    public void testNull() {
        final String text = "null";

        this.parseAndCheck(text, nul(), text);
    }

    @Test
    public void testNumber() {
        final String text = "123";

        this.parseAndCheck(text, number(123), text);
    }

    @Test
    public void testNumber2() {
        final String text = "-123";

        this.parseAndCheck(text, number(-123), text);
    }

    @Test
    public void testString() {
        final String text = "\"abc-123\"";

        this.parseAndCheck(text, string("abc-123"), text);
    }

    @Test
    public void testEmptyArray() {
        final String text = "[]";

        this.parseAndCheck(text, array(arrayBegin(), arrayEnd()), text);
    }

    @Test
    public void testEmptyArrayWhitespace() {
        final String text = "[  ]";

        this.parseAndCheck(text, array(arrayBegin(), whitespace(), arrayEnd()), text);
    }

    @Test
    public void testArrayBooleanFalse() {
        final String text = "[false]";

        this.parseAndCheck(text, array(arrayBegin(), booleanFalse(), arrayEnd()), text);
    }

    @Test
    public void testArrayBooleanTrue() {
        final String text = "[true]";

        this.parseAndCheck(text, array(arrayBegin(), booleanTrue(), arrayEnd()), text);
    }

    @Test
    public void testArrayWhitespaceBooleanWhitespaceTrue() {
        final String text = "[  true  ]";

        this.parseAndCheck(text, array(arrayBegin(), whitespace(), booleanTrue(), whitespace(), arrayEnd()), text);
    }

    @Test
    public void testArrayNull() {
        final String text = "[null]";

        this.parseAndCheck(text, array(arrayBegin(), nul(), arrayEnd()), text);
    }

    @Test
    public void testArrayWhitespaceNullWhitespaceTrue() {
        final String text = "[  null  ]";

        this.parseAndCheck(text, array(arrayBegin(), whitespace(), nul(), whitespace(), arrayEnd()), text);
    }

    @Test
    public void testArrayNumber() {
        final String text = "[123]";

        this.parseAndCheck(text, array(arrayBegin(), number(123), arrayEnd()), text);
    }

    @Test
    public void testArrayWhitespaceNumberWhitespaceTrue() {
        final String text = "[  123  ]";

        this.parseAndCheck(text, array(arrayBegin(), whitespace(), number(123), whitespace(), arrayEnd()), text);
    }

    @Test
    public void testArrayString() {
        final String text = "[\"abc\"]";

        this.parseAndCheck(text, array(arrayBegin(), string("abc"), arrayEnd()), text);
    }

    @Test
    public void testArrayWhitespaceStringWhitespaceTrue() {
        final String text = "[  \"abc\"  ]";

        this.parseAndCheck(text, array(arrayBegin(), whitespace(), string("abc"), whitespace(), arrayEnd()), text);
    }

    @Test
    public void testArrayArrayString() {
        final String text = "[[\"abc\"]]";

        this.parseAndCheck(text, array(arrayBegin(), array(arrayBegin(), string("abc"), arrayEnd()), arrayEnd()), text);
    }

    @Test
    public void testArrayNumberNumber() {
        final String text = "[123,456]";

        this.parseAndCheck(text, array(arrayBegin(), number(123), separator(), number(456), arrayEnd()), text);
    }

    @Test
    public void testArrayNumberWhitespaceNumber() {
        final String text = "[123  ,  456]";

        this.parseAndCheck(text, array(arrayBegin(), number(123), whitespace(), separator(), whitespace(), number(456), arrayEnd()), text);
    }

    @Test
    public void testArrayNumberNumberBooleanTrue() {
        final String text = "[123,456,true]";

        this.parseAndCheck(text, array(arrayBegin(), number(123), separator(), number(456), separator(), booleanTrue(), arrayEnd()), text);
    }

    @Test
    public void testArrayNumberNumberBooleanTrueString() {
        final String text = "[123,456,true,\"abc\"]";

        this.parseAndCheck(text, array(arrayBegin(), number(123), separator(), number(456), separator(), booleanTrue(), separator(), string("abc"), arrayEnd()), text);
    }

    @Test
    public void testEmptyObject() {
        final String text = "{}";

        this.parseAndCheck(text, object(objectBegin(), objectEnd()), text);
    }

    @Test
    public void testEmptyObjectWhitespace() {
        final String text = "{  }";

        this.parseAndCheck(text, object(objectBegin(), whitespace(), objectEnd()), text);
    }

    @Test
    public void testObjectBooleanTrue() {
        final String text = "{\"key1\":true}";

        this.parseAndCheck(text, object(objectBegin(), key1(), objectAssignment(), booleanTrue(), objectEnd()), text);
    }

    @Test
    public void testObjectBooleanFalse() {
        final String text = "{\"key1\":false}";

        this.parseAndCheck(text, object(objectBegin(), key1(), objectAssignment(), booleanFalse(), objectEnd()), text);
    }

    @Test
    public void testObjectNull() {
        final String text = "{\"key1\":null}";

        this.parseAndCheck(text, object(objectBegin(), key1(), objectAssignment(), nul(), objectEnd()), text);
    }

    @Test
    public void testObjectNumber() {
        final String text = "{\"key1\":123}";

        this.parseAndCheck(text, object(objectBegin(), key1(), objectAssignment(), number(123), objectEnd()), text);
    }

    @Test
    public void testObjectString() {
        final String text = "{\"key1\":\"abc\"}";

        this.parseAndCheck(text, object(objectBegin(), key1(), objectAssignment(), string("abc"), objectEnd()), text);
    }

    @Test
    public void testObjectArrayTrue() {
        final String text = "{\"key1\":[true]}";

        final JsonNodeParserToken array = array(arrayBegin(), booleanTrue(), arrayEnd());
        this.parseAndCheck(text, object(objectBegin(), key1(), objectAssignment(), array, objectEnd()), text);
    }

    @Test
    public void testObjectNested() {
        final String text = "{\"key1\":{\"key2\":true}}";

        final JsonNodeParserToken nested = object(objectBegin(), key2(), objectAssignment(), booleanTrue(), objectEnd());
        this.parseAndCheck(text, object(objectBegin(), key1(), objectAssignment(), nested, objectEnd()), text);
    }

    @Test
    public void testObjectNestedNested() {
        final String text = "{\"key1\":{\"key2\":{\"key3\":true}}}";

        final JsonNodeParserToken nested2 = object(objectBegin(), key3(), objectAssignment(), booleanTrue(), objectEnd());
        final JsonNodeParserToken nested = object(objectBegin(), key2(), objectAssignment(), nested2, objectEnd());
        this.parseAndCheck(text, object(objectBegin(), key1(), objectAssignment(), nested, objectEnd()), text);
    }

    @Test
    public void testObjectBooleanTrueBooleanFalse() {
        final String text = "{\"key1\":true,\"key2\":false}";

        this.parseAndCheck(text, object(objectBegin(), key1(), objectAssignment(), booleanTrue(), separator(), key2(), objectAssignment(), booleanFalse(), objectEnd()), text);
    }

    @Test
    public void testObjectWhitespaceKeyWhitespaceAssignmentWhitespaceValueWhitespace() {
        final String text = "{  \"key1\"  :  null  }";

        this.parseAndCheck(text,
                object(objectBegin(),
                        whitespace(), key1(), whitespace(), objectAssignment(), whitespace(), nul(), whitespace(),
                        objectEnd()),
                text);
    }

    @Test
    public void testObjectBooleanTrueBooleanFalseNul() {
        final String text = "{\"key1\":true,\"key2\":false,\"key3\":null}";

        this.parseAndCheck(text, object(objectBegin(),
                key1(), objectAssignment(), booleanTrue(), separator(),
                key2(), objectAssignment(), booleanFalse(), separator(),
                key3(), objectAssignment(), nul(),
                objectEnd()), text);
    }

    @Test
    public void testInvalidJsonReported() {
        this.parseThrows("!INVALID", '!', 1, 1);
    }

    @Test
    public void testInvalidObjectPropertyKeyReported() {
        this.parseThrows("{!INVALID}", '!', 2, 1);
    }

    @Test
    public void testInvalidObjectPropertyValueReported() {
        this.parseThrows("{\"key1\":!INVALID}", '!', 9, 1);
    }

    @Test
    public void testInvalidObjectPropertyReportedValue2() {
        this.parseThrows("{\"key1\":true,\"key2\":false,\"key3\":!INVALID}", '!', 34, 1);
    }

    @Test
    public void testInvalidObjectPropertyAssignmentSymbolReported() {
        this.parseThrows("{\"key1\":true,\"key2\":false,\"key3\"!true}", '"', 27, 1);
    }

    @Test
    public void testInvalidArrayElementReported() {
        this.parseThrows("[!ABC]", '!', 2, 1);
    }

    @Test
    public void testInvalidArrayElementReported2() {
        this.parseThrows("[true, 123, !ABC]", '!', 13, 1);
    }

    @Test
    public void testInvalidArrayElementSeparatorReported() {
        // is complaining that the token 123 <space> <exclaimation point> abc is invalid rather than the missing separator
        this.parseThrows("[123 !ABC]", '1', 2, 1);
    }

    @Override
    protected Parser<JsonNodeParserToken, JsonNodeParserContext> createParser() {
        return JsonNodeParsers.value()
                .orReport(ParserReporters.basic())
                .cast();
    }

    @Override
    protected JsonNodeParserContext createContext() {
        return JsonNodeParserContexts.basic();
    }

    private JsonNodeParserToken arrayBegin() {
        return JsonNodeParserToken.arrayBeginSymbol("[", "[");
    }

    private JsonNodeParserToken arrayEnd() {
        return JsonNodeParserToken.arrayEndSymbol("]", "]");
    }

    private JsonNodeParserToken array(final JsonNodeParserToken... tokens) {
        return JsonNodeParserToken.array(Lists.of(tokens), text(tokens));
    }

    private JsonNodeParserToken booleanFalse() {
        return booleanToken(false);
    }

    private JsonNodeParserToken booleanTrue() {
        return booleanToken(true);
    }

    private JsonNodeParserToken booleanToken(final boolean value) {
        return JsonNodeParserToken.booleanParserToken(value, String.valueOf(value));
    }

    private JsonNodeParserToken nul() {
        return JsonNodeParserToken.nullParserToken("null");
    }

    private JsonNodeParserToken number(final int value) {
        // accept only int, keeps the creation of the matching text simple.
        return JsonNodeParserToken.number(value, String.valueOf(value));
    }

    private JsonNodeParserToken objectAssignment() {
        return JsonNodeParserToken.objectAssignmentSymbol(":", ":");
    }

    private JsonNodeParserToken objectBegin() {
        return JsonNodeParserToken.objectBeginSymbol("{", "{");
    }

    private JsonNodeParserToken objectEnd() {
        return JsonNodeParserToken.objectEndSymbol("}", "}");
    }

    private JsonNodeParserToken object(final JsonNodeParserToken... tokens) {
        return JsonNodeParserToken.object(Lists.of(tokens), text(tokens));
    }

    private JsonNodeParserToken separator() {
        return JsonNodeParserToken.separatorSymbol(",", ",");
    }

    private JsonNodeParserToken string(final String value) {
        return JsonNodeParserToken.string(value, CharSequences.quoteAndEscape(value).toString());
    }

    private JsonNodeParserToken whitespace() {
        return JsonNodeParserToken.whitespace("  ", "  ");
    }

    private JsonNodeParserToken key1() {
        return string("key1");
    }

    private JsonNodeParserToken key2() {
        return string("key2");
    }

    private JsonNodeParserToken key3() {
        return string("key3");
    }

    private static String text(final JsonNodeParserToken... tokens) {
        return ParserToken.text(Lists.of(tokens));
    }

    @Override
    protected String toString(final ParserToken token) {
        return JsonParserPrettyJsonNodeParserTokenVisitor.toString(token);
    }
}
