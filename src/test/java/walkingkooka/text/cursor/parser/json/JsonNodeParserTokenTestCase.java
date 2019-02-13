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
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.PublicStaticFactoryTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenTestCase;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class JsonNodeParserTokenTestCase<T extends JsonNodeParserToken> extends ParserTokenTestCase<T>
        implements IsMethodTesting<T> {

    @Test
    public final void testPublicStaticFactoryMethod() {
        PublicStaticFactoryTesting.check(JsonNodeParserToken.class,
                "JsonNode",
                ParserToken.class,
                this.type());
    }

    @Test
    public final void testEmptyTextFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken("");
        });
    }

    final JsonNodeParserToken arrayBegin() {
        return JsonNodeParserToken.arrayBeginSymbol("[", "[");
    }

    final JsonNodeParserToken arrayEnd() {
        return JsonNodeParserToken.arrayEndSymbol("]", "]");
    }

    final JsonNodeParserToken array(final JsonNodeParserToken... tokens) {
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

    final JsonNodeParserToken object(final JsonNodeParserToken... tokens) {
        return JsonNodeParserToken.object(Lists.of(tokens), text(tokens));
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

    private static String text(final JsonNodeParserToken... tokens) {
        return ParserToken.text(Lists.of(tokens));
    }

    // isMethodTesting2.....................................................................................

    @Override
    public T createIsMethodObject() {
        return this.createToken();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "JsonNode";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return ParserToken.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> m.equals("isNoise") || m.equals("isSymbol"); // skip isNoise
    }

}
