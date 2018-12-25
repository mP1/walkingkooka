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

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.type.MemberVisibility;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class TokenHeaderValueTest extends HeaderValueWithParametersTestCase<TokenHeaderValue,
        TokenHeaderValueParameterName<?>> {

    private final static String VALUE = "abc";
    private final static String PARAMETER_VALUE = "v1";

    // with.........................................................................................

    @Test(expected = NullPointerException.class)
    public void testWithNullValueFails() {
        TokenHeaderValue.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyValueFails() {
        TokenHeaderValue.with("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidCharactersValueFails() {
        TokenHeaderValue.with("<");
    }

    @Test
    public void testWith() {
        final TokenHeaderValue token = this.token();
        this.check(token);
    }

    // setValue ...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetValueNullFails() {
        this.token().setValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueEmptyFails() {
        this.token().setValue("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueInvalidCharactersFails() {
        this.token().setValue("<");
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

    @Test(expected = HeaderValueException.class)
    public void testSetParametersInvalidParameterValueFails() {
        this.token().setParameters(this.parameters("Q", "INVALID!"));
    }

    @Test
    public void testSetParametersDifferent() {
        final TokenHeaderValue token = this.token();
        final Map<TokenHeaderValueParameterName<?>, Object> parameters = this.parameters("different", "2");
        this.check(token.setParameters(parameters), VALUE, parameters);
        this.check(token);
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


    // isWildcard ..................................................................................................

    @Test
    public void testIsWildcard() {
        this.isWildcardAndCheck(false);
    }

    // toString ...........................................................................................

    @Test
    public void testToStringNoParameters() {
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

    private void toStringAndCheck(final TokenHeaderValue token, final String toString) {
        assertEquals("toString", toString, token.toString());
    }

    // toHeaderTextList ...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testToHeaderTextListNullFails() {
        TokenHeaderValue.toHeaderTextList(null);
    }

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

    private void toHeaderTextListAndCheck(final String toString, final TokenHeaderValue... tokens) {
        assertEquals("toheaderTextList of " + Arrays.toString(tokens),
                toString,
                TokenHeaderValue.toHeaderTextList(Lists.of(tokens)));
    }

    // helpers ...........................................................................................

    @Override
    protected TokenHeaderValue createHeaderValueWithParameters() {
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
        assertEquals("value", value, token.value());
        assertEquals("parameters", parameters, token.parameters());
        assertEquals("is wildcard", value.equals(TokenHeaderValue.WILDCARD), token.isWildcard());
    }

    @Override
    protected boolean isMultipart() {
        return true;
    }

    @Override
    protected boolean isRequest() {
        return true;
    }

    @Override
    protected boolean isResponse() {
        return true;
    }

    @Override
    protected Class<TokenHeaderValue> type() {
        return TokenHeaderValue.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
