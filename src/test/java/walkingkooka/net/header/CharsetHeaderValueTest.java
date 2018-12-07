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
import walkingkooka.net.http.HttpHeaderScope;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class CharsetHeaderValueTest extends HeaderValueWithParametersTestCase<CharsetHeaderValue,
        CharsetHeaderValueParameterName<?>> {

    private final static CharsetName VALUE = CharsetName.UTF_8;
    private final static String PARAMETER_VALUE = "v1";

    // with.........................................................................................

    @Test(expected = NullPointerException.class)
    public void testWithNullValueFails() {
        CharsetHeaderValue.with(null);
    }

    @Test
    public void testWith() {
        final CharsetHeaderValue token = this.charsetHeaderValue();
        this.check(token);
    }

    // setValue ...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetValueNullFails() {
        this.charsetHeaderValue().setValue(null);
    }

    @Test
    public void testSetValueSame() {
        final CharsetHeaderValue token = this.charsetHeaderValue();
        assertSame(token, token.setValue(VALUE));
    }

    @Test
    public void testSetValueDifferent() {
        final CharsetHeaderValue token = this.charsetHeaderValue();
        final CharsetName value = CharsetName.with("different");
        this.check(token.setValue(value), value, this.parameters());
        this.check(token);
    }

    // setParameters ...........................................................................................

    @Test(expected = HeaderValueException.class)
    public void testSetParametersInvalidParameterValueFails() {
        this.charsetHeaderValue().setParameters(this.parameters("Q", "INVALID!"));
    }

    @Test
    public void testSetParametersDifferent() {
        final CharsetHeaderValue token = this.charsetHeaderValue();
        final Map<CharsetHeaderValueParameterName<?>, Object> parameters = this.parameters("different", "2");
        this.check(token.setParameters(parameters), VALUE, parameters);
        this.check(token);
    }

    // toString ...........................................................................................

    @Test
    public void testToStringNoParameters() {
        this.toStringAndCheck(CharsetHeaderValue.with(VALUE),
                VALUE.toHeaderText());
    }

    @Test
    public void testToStringWithParameters() {
        this.toStringAndCheck(this.charsetHeaderValue(),
                VALUE + "; p1=v1");
    }

    @Test
    public void testToStringWithSeveralParameters() {
        this.toStringAndCheck(CharsetHeaderValue.with(VALUE)
                        .setParameters(this.parameters("p1", "v1", "p2", "v2")),
                VALUE + "; p1=v1; p2=v2");
    }

    private void toStringAndCheck(final CharsetHeaderValue token, final String toString) {
        assertEquals("toString", toString, token.toString());
        assertEquals("toHeaderText", toString, token.toHeaderText());
    }

    // toHeaderTextList ...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testToHeaderTextListNullFails() {
        CharsetHeaderValue.toHeaderTextList(null);
    }

    @Test
    public void testToHeaderTextListOne() {
        final String text = "a";
        this.toHeaderTextListAndCheck(text,
                CharsetHeaderValue.with(CharsetName.with(text)));
    }

    @Test
    public void testToHeaderTextListOneWithParameters() {
        this.toHeaderTextListAndCheck("a; p1=v1",
                CharsetHeaderValue.with(CharsetName.with("a"))
                        .setParameters(this.parameters()));
    }

    @Test
    public void testToHeaderTextListSeveral() {
        this.toHeaderTextListAndCheck("a, b",
                CharsetHeaderValue.with(CharsetName.with("a")),
                CharsetHeaderValue.with(CharsetName.with("b")));
    }

    private void toHeaderTextListAndCheck(final String toString, final CharsetHeaderValue... tokens) {
        assertEquals("header text of " + Arrays.toString(tokens),
                toString,
                CharsetHeaderValue.toHeaderTextList(Lists.of(tokens)));
    }

    // helpers ...........................................................................................

    @Override
    protected CharsetHeaderValue createHeaderValueWithParameters() {
        return this.charsetHeaderValue();
    }

    private CharsetHeaderValue charsetHeaderValue() {
        return CharsetHeaderValue.with(VALUE).setParameters(this.parameters());
    }

    private Map<CharsetHeaderValueParameterName<?>, Object> parameters() {
        return this.parameters("p1", PARAMETER_VALUE);
    }

    private Map<CharsetHeaderValueParameterName<?>, Object> parameters(final String name,
                                                                       final Object value) {
        return this.parameters(CharsetHeaderValueParameterName.with(name), value);
    }

    private Map<CharsetHeaderValueParameterName<?>, Object> parameters(final CharsetHeaderValueParameterName<?> name,
                                                                       final Object value) {
        return Maps.one(name, value);
    }

    private Map<CharsetHeaderValueParameterName<?>, Object> parameters(final String name1,
                                                                       final Object value1,
                                                                       final String name2,
                                                                       final Object value2) {
        return this.parameters(CharsetHeaderValueParameterName.with(name1),
                value1,
                CharsetHeaderValueParameterName.with(name2),
                value2);
    }

    private Map<CharsetHeaderValueParameterName<?>, Object> parameters(final CharsetHeaderValueParameterName<?> name1,
                                                                       final Object value1,
                                                                       final CharsetHeaderValueParameterName<?> name2,
                                                                       final Object value2) {
        final Map<CharsetHeaderValueParameterName<?>, Object> parameters = Maps.ordered();
        parameters.put(name1, value1);
        parameters.put(name2, value2);
        return parameters;
    }

    private void check(final CharsetHeaderValue charsetHeaderValue) {
        this.check(charsetHeaderValue, VALUE, charsetHeaderValue.parameters());
    }

    private void check(final CharsetHeaderValue charsetHeaderValue,
                       final CharsetName value,
                       final Map<CharsetHeaderValueParameterName<?>, Object> parameters) {
        assertEquals("value", value, charsetHeaderValue.value());
        assertEquals("parameters", parameters, charsetHeaderValue.parameters());
    }

    @Override
    protected HttpHeaderScope httpHeaderScope() {
        return HttpHeaderScope.REQUEST_RESPONSE;
    }

    @Override
    protected Class<CharsetHeaderValue> type() {
        return CharsetHeaderValue.class;
    }
}
