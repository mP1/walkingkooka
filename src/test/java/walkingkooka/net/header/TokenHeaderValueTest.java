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

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TokenHeaderValueTest extends HeaderValueWithParametersTestCase<TokenHeaderValue,
        TokenHeaderValueParameterName<?>> implements ParseStringTesting<TokenHeaderValue> {

    private final static String VALUE = "abc";
    private final static String PARAMETER_VALUE = "v1";

    // with.........................................................................................

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            TokenHeaderValue.with(null);
        });
    }

    @Test
    public void testWithEmptyValueFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            TokenHeaderValue.with("");
        });
    }

    @Test
    public void testWithInvalidCharactersValueFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            TokenHeaderValue.with("<");
        });
    }

    @Test
    public void testWith() {
        final TokenHeaderValue token = this.token();
        this.check(token);
    }

    // setValue ...........................................................................................

    @Test
    public void testSetValueNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.token().setValue(null);
        });
    }

    @Test
    public void testSetValueEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.token().setValue("");
        });
    }

    @Test
    public void testSetValueInvalidCharactersFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.token().setValue("<");
        });
    }

    @Test
    public void testSetValueSame() {
        final TokenHeaderValue token = this.token();
        assertSame(token, token.setValue(VALUE));
    }

    @Test
    public void testSetValueDifferent() {
        final TokenHeaderValue token = this.token();
        final String value = "different";
        this.check(token.setValue(value), value, this.parameters());
        this.check(token);
    }

    // setParameters ...........................................................................................

    @Test
    public void testSetParametersInvalidParameterValueFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.token().setParameters(this.parameters("Q", "INVALID!"));
        });
    }

    @Test
    public void testSetParametersDifferent() {
        final TokenHeaderValue token = this.token();
        final Map<TokenHeaderValueParameterName<?>, Object> parameters = this.parameters("different", "2");
        this.check(token.setParameters(parameters), VALUE, parameters);
        this.check(token);
    }

    // ParseStringTesting ........................................................................................

    @Test
    public void testParse() {
        this.parseAndCheck("A;b=c",
                TokenHeaderValue.with("A")
                        .setParameters(Maps.one(TokenHeaderValueParameterName.with("b"), "c")));
    }

    @Override
    public TokenHeaderValue parse(final String text) {
        return TokenHeaderValue.parse(text);
    }

    // ParseList ........................................................................................

    @Test
    public void testParseList() {
        final String text = "A;b=c, DEF;ghi=jkl";

        assertEquals(Lists.of(TokenHeaderValue.with("A")
                        .setParameters(Maps.one(TokenHeaderValueParameterName.with("b"), "c")),
                TokenHeaderValue.with("DEF")
                        .setParameters(Maps.one(TokenHeaderValueParameterName.with("ghi"), "jkl"))),
                TokenHeaderValue.parseList(text),
                "Incorrect result parsing " + CharSequences.quoteAndEscape(text));
    }

    // toHeaderText ...........................................................................................

    @Test
    public void testToHeaderTextNoParameters() {
        this.toHeaderTextAndCheck(TokenHeaderValue.with(VALUE),
                "abc");
    }

    @Test
    public void testToHeaderTextWithParameters() {
        this.toHeaderTextAndCheck(this.token(),
                "abc; p1=v1");
    }

    @Test
    public void testToHeaderTextWithSeveralParameters() {
        this.toHeaderTextAndCheck(TokenHeaderValue.with(VALUE)
                        .setParameters(this.parameters("p1", "v1", "p2", "v2")),
                "abc; p1=v1; p2=v2");
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(this.createObject().setValue("different"));
    }

    @Test
    public void testEqualsDifferentParameters() {
        this.checkNotEquals(this.createObject().setParameters(this.parameters("different", PARAMETER_VALUE)));
    }

    @Test
    public void testEqualsDifferentParameters2() {
        this.checkNotEquals(this.createObject().setParameters(TokenHeaderValue.NO_PARAMETERS));
    }

    // isWildcard ..................................................................................................

    @Test
    public void testIsWildcard() {
        this.isWildcardAndCheck(false);
    }

    // toString ...........................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(TokenHeaderValue.with(VALUE),
                "abc");
    }

    @Test
    public void testToStringWithParameters() {
        this.toStringAndCheck(this.token(),
                "abc; p1=v1");
    }

    @Test
    public void testToStringWithSeveralParameters() {
        this.toStringAndCheck(TokenHeaderValue.with(VALUE)
                        .setParameters(this.parameters("p1", "v1", "p2", "v2")),
                "abc; p1=v1; p2=v2");
    }

    // toHeaderTextList ...........................................................................................

    @Test
    public void testToHeaderTextListOne() {
        this.toHeaderTextListAndCheck("A",
                TokenHeaderValue.with("A"));
    }

    @Test
    public void testToHeaderTextListOneWithParameters() {
        this.toHeaderTextListAndCheck("A; p1=v1",
                TokenHeaderValue.with("A")
                        .setParameters(this.parameters()));
    }

    @Test
    public void testToHeaderTextListMany() {
        this.toHeaderTextListAndCheck("A, B",
                TokenHeaderValue.with("A"),
                TokenHeaderValue.with("B"));
    }

    // helpers ...........................................................................................

    @Override
    public TokenHeaderValue createHeaderValueWithParameters() {
        return this.token();
    }

    private TokenHeaderValue token() {
        return TokenHeaderValue.with(VALUE)
                .setParameters(this.parameters());
    }

    private Map<TokenHeaderValueParameterName<?>, Object> parameters() {
        return this.parameters("p1", PARAMETER_VALUE);
    }

    private Map<TokenHeaderValueParameterName<?>, Object> parameters(final String name,
                                                                     final Object value) {
        return this.parameters(TokenHeaderValueParameterName.with(name), value);
    }

    private Map<TokenHeaderValueParameterName<?>, Object> parameters(final TokenHeaderValueParameterName<?> name,
                                                                     final Object value) {
        return Maps.one(name, value);
    }

    private Map<TokenHeaderValueParameterName<?>, Object> parameters(final String name1,
                                                                     final Object value1,
                                                                     final String name2,
                                                                     final Object value2) {
        return this.parameters(TokenHeaderValueParameterName.with(name1),
                value1,
                TokenHeaderValueParameterName.with(name2),
                value2);
    }

    private Map<TokenHeaderValueParameterName<?>, Object> parameters(final TokenHeaderValueParameterName<?> name1,
                                                                     final Object value1,
                                                                     final TokenHeaderValueParameterName<?> name2,
                                                                     final Object value2) {
        final Map<TokenHeaderValueParameterName<?>, Object> parameters = Maps.ordered();
        parameters.put(name1, value1);
        parameters.put(name2, value2);
        return parameters;
    }

    private void check(final TokenHeaderValue token) {
        this.check(token, VALUE, token.parameters());
    }

    private void check(final TokenHeaderValue token,
                       final String value,
                       final Map<TokenHeaderValueParameterName<?>, Object> parameters) {
        assertEquals(value, token.value(), "value");
        assertEquals(parameters, token.parameters(), "parameters");
        assertEquals(value.equals(TokenHeaderValue.WILDCARD), token.isWildcard(), "is wildcard");
    }

    @Override
    public boolean isMultipart() {
        return true;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    @Override
    public Class<TokenHeaderValue> type() {
        return TokenHeaderValue.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
