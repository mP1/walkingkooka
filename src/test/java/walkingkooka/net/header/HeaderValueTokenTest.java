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

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class HeaderValueTokenTest extends HeaderValueWithParametersTestCase<HeaderValueToken,
        HeaderValueTokenParameterName<?>> {

    private final static String VALUE = "abc";
    private final static String PARAMETER_VALUE = "v1";

    // with.........................................................................................

    @Test(expected = NullPointerException.class)
    public void testWithNullValueFails() {
        HeaderValueToken.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyValueFails() {
        HeaderValueToken.with("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidCharactersValueFails() {
        HeaderValueToken.with("<");
    }

    @Test
    public void testWith() {
        final HeaderValueToken token = this.token();
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
        final HeaderValueToken token = this.token();
        assertSame(token, token.setValue(VALUE));
    }

    @Test
    public void testSetValueDifferent() {
        final HeaderValueToken token = this.token();
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
        final HeaderValueToken token = this.token();
        final Map<HeaderValueTokenParameterName<?>, Object> parameters = this.parameters("different", "2");
        this.check(token.setParameters(parameters), VALUE, parameters);
        this.check(token);
    }

    // toString ...........................................................................................

    @Test
    public void testToStringNoParameters() {
        this.toStringAndCheck(HeaderValueToken.with(VALUE),
                "abc");
    }

    @Test
    public void testToStringWithParameters() {
        this.toStringAndCheck(this.token(),
                "abc; p1=v1");
    }

    @Test
    public void testToStringWithSeveralParameters() {
        this.toStringAndCheck(HeaderValueToken.with(VALUE)
                .setParameters(this.parameters("p1", "v1", "p2", "v2")),
                "abc; p1=v1; p2=v2");
    }

    private void toStringAndCheck(final HeaderValueToken token, final String toString) {
        assertEquals("toString", toString, token.toString());
        assertEquals("headerValue", toString, token.headerValue());
    }

    // format ...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testFormatNullFails() {
        HeaderValueToken.format(null);
    }

    @Test
    public void testFormatOne() {
        this.formatAndCheck("A",
                HeaderValueToken.with("A"));
    }

    @Test
    public void testFormatOneWithParameters() {
        this.formatAndCheck("A; p1=v1",
                HeaderValueToken.with("A")
                        .setParameters(this.parameters()));
    }

    @Test
    public void testFormatMany() {
        this.formatAndCheck("A, B",
                HeaderValueToken.with("A"),
                HeaderValueToken.with("B"));
    }

    private void formatAndCheck(final String toString, final HeaderValueToken...tokens) {
        assertEquals("Format of " + Arrays.toString(tokens),
                toString,
                HeaderValueToken.format(Lists.of(tokens)));
    }

    // helpers ...........................................................................................

    @Override
    protected HeaderValueToken createHeaderValueWithParameters() {
        return this.token();
    }

    private HeaderValueToken token() {
        return HeaderValueToken.with(VALUE)
                .setParameters(this.parameters());
    }

    private Map<HeaderValueTokenParameterName<?>, Object> parameters() {
        return this.parameters("p1", PARAMETER_VALUE);
    }

    private Map<HeaderValueTokenParameterName<?>, Object> parameters(final String name,
                                                                     final Object value) {
        return this.parameters(HeaderValueTokenParameterName.with(name), value);
    }

    private Map<HeaderValueTokenParameterName<?>, Object> parameters(final HeaderValueTokenParameterName<?> name,
                                                                     final Object value) {
        return Maps.one(name, value);
    }

    private Map<HeaderValueTokenParameterName<?>, Object> parameters(final String name1,
                                                                     final Object value1,
                                                                     final String name2,
                                                                     final Object value2) {
        return this.parameters(HeaderValueTokenParameterName.with(name1),
                value1,
                HeaderValueTokenParameterName.with(name2),
                value2);
    }

    private Map<HeaderValueTokenParameterName<?>, Object> parameters(final HeaderValueTokenParameterName<?> name1,
                                                                     final Object value1,
                                                                     final HeaderValueTokenParameterName<?> name2,
                                                                     final Object value2) {
        final Map<HeaderValueTokenParameterName<?>, Object> parameters = Maps.ordered();
        parameters.put(name1, value1);
        parameters.put(name2, value2);
        return parameters;
    }

    private void check(final HeaderValueToken token) {
        this.check(token, VALUE, token.parameters());
    }

    private void check(final HeaderValueToken token,
                       final String value,
                       final Map<HeaderValueTokenParameterName<?>, Object> parameters) {
        assertEquals("value", value, token.value());
        assertEquals("parameters", parameters, token.parameters());
        assertEquals("is wildcard", value.equals(HeaderValueToken.WILDCARD), token.isWildcard());
    }

    @Override
    protected Class<HeaderValueToken> type() {
        return HeaderValueToken.class;
    }
}
