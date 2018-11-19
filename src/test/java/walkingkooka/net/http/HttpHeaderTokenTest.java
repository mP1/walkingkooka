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

package walkingkooka.net.http;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.PublicClassTestCase;
import walkingkooka.text.CharSequences;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public final class HttpHeaderTokenTest extends PublicClassTestCase<HttpHeaderToken> {

    private final static String VALUE = "abc";
    private final static String PARAMETER_VALUE = "v1";

    // with.........................................................................................

    @Test(expected = NullPointerException.class)
    public void testWithNullValueFails() {
        HttpHeaderToken.with(null, HttpHeaderToken.NO_PARAMETERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyValueFails() {
        HttpHeaderToken.with("", HttpHeaderToken.NO_PARAMETERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidCharactersValueFails() {
        HttpHeaderToken.with("<", HttpHeaderToken.NO_PARAMETERS);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullParametersFails() {
        HttpHeaderToken.with(VALUE, null);
    }

    @Test
    public void testWith() {
        final HttpHeaderToken token = this.token();
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
        final HttpHeaderToken token = this.token();
        assertSame(token, token.setValue(VALUE));
    }

    @Test
    public void testSetValueDifferent() {
        final HttpHeaderToken token = this.token();
        final String value = "different";
        this.check(token.setValue(value), value, this.parameters());
        this.check(token);
    }

    // setParameters ...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetParametersNullFails() {
        this.token().setParameters(null);
    }

    @Test
    public void testSetParametersSame() {
        final HttpHeaderToken token = this.token();
        assertSame(token, token.setParameters(this.parameters()));
    }

    @Test
    public void testSetParametersDifferent() {
        final HttpHeaderToken token = this.token();
        final Map<HttpHeaderParameterName<?>, String> parameters = this.parameters("different", "2");
        this.check(token.setParameters(parameters), VALUE, parameters);
        this.check(token);
    }

    // parse ...................................................................................................

    @Test(expected = NullPointerException.class)
    public void testParseNullFails() {
        HttpHeaderToken.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyFails() {
        HttpHeaderToken.parse("");
    }

    @Test
    public void testParseValue() {
        this.parseAndCheck("A", this.token("A"));
    }

    @Test
    public void testParseValueInvalidCharacterFails() {
        this.parseFails("A<");
    }

    @Test
    public void testParseValueInvalidCharacterFails2() {
        this.parseFails("ABC<");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseValueTrailingEquals() {
        HttpHeaderToken.parse("A;b=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseValueCommaEquals() {
        HttpHeaderToken.parse("A;b=c,");
    }

    @Test
    public void testParseValue2() {
        this.parseAndCheck("ABC", this.token("ABC"));
    }

    @Test
    public void testParseValueWhitespaceFails() {
        this.parseFails("A ");
    }

    @Test
    public void testParseValueParameter() {
        this.parseAndCheck("A;b=c",
                HttpHeaderToken.with("A", this.parameters("b", "c")));
    }

    @Test
    public void testParseValueParameter2() {
        this.parseAndCheck("A;bcd=123",
                HttpHeaderToken.with("A", this.parameters("bcd", "123")));
    }

    @Test
    public void testParseValueWhitespaceParameter() {
        this.parseAndCheck("A; b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueParameterCommaValue() {
        this.parseAndCheck("A;b=c,D",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testParseValueParameterCommaWhitespaceValue() {
        this.parseAndCheck("A;b=c, D",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testParseValueParameterCommaValueWhitespaceValue() {
        this.parseAndCheck("A;b=c, D;e=f",
                this.token("A", "b", "c"),
                this.token("D", "e", "f"));
    }

    @Test
    public void testParseValueCommaValue() {
        this.parseAndCheck("A,B",
                this.token("A"),
                this.token("B"));
    }

    @Test
    public void testParseValueCommaValue2() {
        this.parseAndCheck("ABC,DEF",
                this.token("ABC"),
                this.token("DEF"));
    }

    @Test
    public void testParseValueCommaValueCommaValue() {
        this.parseAndCheck("A,B,C",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testParseValueCommaWhitespaceValueCommaWhitespaceValue() {
        this.parseAndCheck("A, B, C",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testParseValueParametersCommaValueParametersCommaValueParameters() {
        this.parseAndCheck("V1;p1=v1,V2;p2=v2,V3;p3=v3",
                this.token("V1", "p1", "v1"),
                this.token("V2", "p2", "v2"),
                this.token("V3", "p3", "v3"));
    }

    @Test
    public void testParseValueParametersCommaWhitespaceValueParametersCommaWhitespaceValueParameters() {
        this.parseAndCheck("V1;p1=v1, V2;p2=v2, V3;p3=v3",
                this.token("V1", "p1", "v1"),
                this.token("V2", "p2", "v2"),
                this.token("V3", "p3", "v3"));
    }

    private void parseAndCheck(final String headerValue, final HttpHeaderToken... tokens) {
        assertEquals("Incorrect result parsing " + CharSequences.quote(headerValue),
                Lists.of(tokens),
                HttpHeaderToken.parse(headerValue));
    }

    final void parseFails(final String text) {
        this.parseFails(text, text.length() - 1);
    }

    final void parseFails(final String text, final char pos) {
        this.parseFails(text, text.indexOf(pos));
    }

    final void parseFails(final String text, final int pos) {
        parseFails(text, HttpHeaderToken.invalidCharacter(pos, text));
    }

    final void parseFails(final String text, final String message) {
        try {
            HttpHeaderToken.parse(text);
            fail();
        } catch (final IllegalArgumentException expected) {
            expected.printStackTrace();
            assertEquals("Incorrect failure message", message, expected.getMessage());
        }
    }

    // toString ...........................................................................................

    @Test
    public void testToStringNoParameters() {
        this.toStringAndCheck(HttpHeaderToken.with(VALUE, HttpHeaderToken.NO_PARAMETERS), "abc");
    }

    @Test
    public void testToStringWithParameters() {
        this.toStringAndCheck(this.token(), "abc; p1=v1");
    }

    @Test
    public void testToStringWithSeveralParameters() {
        this.toStringAndCheck(HttpHeaderToken.with(VALUE, this.parameters("p1", "v1", "p2", "v2")), "abc; p1=v1; p2=v2");
    }

    private void toStringAndCheck(final HttpHeaderToken token, final String toString) {
        assertEquals(toString, token.toString());
    }

    // helpers ...........................................................................................


    private HttpHeaderToken token() {
        return HttpHeaderToken.with(VALUE, this.parameters());
    }

    private HttpHeaderToken token(final String value) {
        return HttpHeaderToken.with(value, HttpHeaderToken.NO_PARAMETERS);
    }

    private HttpHeaderToken token(final String value, final String parameterName, final String parameterValue) {
        return HttpHeaderToken.with(value, this.parameters(parameterName, parameterValue));
    }

    private Map<HttpHeaderParameterName<?>, String> parameters() {
        return this.parameters("p1", PARAMETER_VALUE);
    }

    private Map<HttpHeaderParameterName<?>, String> parameters(final String name, final String value) {
        return this.parameters(HttpHeaderParameterName.with(name), value);
    }

    private Map<HttpHeaderParameterName<?>, String> parameters(final HttpHeaderParameterName<?> name, final String value) {
        return Maps.one(name, value);
    }

    private Map<HttpHeaderParameterName<?>, String> parameters(final String name1,
                                                               final String value1,
                                                               final String name2,
                                                               final String value2) {
        return this.parameters(HttpHeaderParameterName.with(name1),
                value1,
                HttpHeaderParameterName.with(name2),
                value2);
    }

    private Map<HttpHeaderParameterName<?>, String> parameters(final HttpHeaderParameterName<?> name1,
                                                               final String value1,
                                                               final HttpHeaderParameterName<?> name2,
                                                               final String value2) {
        final Map<HttpHeaderParameterName<?>, String> parameters = Maps.ordered();
        parameters.put(name1, value1);
        parameters.put(name2, value2);
        return parameters;
    }

    private void check(final HttpHeaderToken token) {
        this.check(token, VALUE, token.parameters());
    }

    private void check(final HttpHeaderToken token, final String value, final Map<HttpHeaderParameterName<?>, String> parameters) {
        assertEquals("value", value, token.value());
        assertEquals("parameters", parameters, token.parameters());
    }

    @Override
    protected Class<HttpHeaderToken> type() {
        return HttpHeaderToken.class;
    }
}
