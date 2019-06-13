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
import walkingkooka.collect.map.Maps;
import walkingkooka.type.JavaVisibility;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CharsetHeaderValueTest extends HeaderValueWithParametersTestCase<CharsetHeaderValue,
        CharsetHeaderValueParameterName<?>> {

    private final static CharsetName VALUE = CharsetName.UTF_8;
    private final static String PARAMETER_VALUE = "v1";
    private final static float Q = 0.5f;

    // with.........................................................................................

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            CharsetHeaderValue.with(null);
        });
    }

    @Test
    public void testWith() {
        final CharsetHeaderValue charset = this.charsetHeaderValue();
        this.check(charset);
    }

    // setValue ...........................................................................................

    @Test
    public void testSetValueNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.charsetHeaderValue().setValue(null);
        });
    }

    @Test
    public void testSetValueSame() {
        final CharsetHeaderValue charset = this.charsetHeaderValue();
        assertSame(charset, charset.setValue(VALUE));
    }

    @Test
    public void testSetValueDifferent() {
        final CharsetHeaderValue charset = this.charsetHeaderValue();
        final CharsetName value = CharsetName.with("different");
        this.check(charset.setValue(value), value, this.parameters());
        this.check(charset);
    }

    // setParameters ...........................................................................................

    @Test
    public void testSetParametersInvalidParameterValueFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.charsetHeaderValue().setParameters(this.parameters("Q", "INVALID!"));
        });
    }

    @Test
    public void testSetParametersDifferent() {
        final CharsetHeaderValue charset = this.charsetHeaderValue();
        final Map<CharsetHeaderValueParameterName<?>, Object> parameters = this.parameters("different", "2");
        this.check(charset.setParameters(parameters), VALUE, parameters);
        this.check(charset);
    }

    @Test
    public void testWildcardSetParametersDifferent() {
        final CharsetHeaderValue charset = CharsetHeaderValue.WILDCARD_VALUE;
        final Map<CharsetHeaderValueParameterName<?>, Object> parameters = this.parameters("different", "2");
        this.check(charset.setParameters(parameters), CharsetName.WILDCARD_CHARSET, parameters);
        this.check(charset, CharsetName.WILDCARD_CHARSET, CharsetHeaderValue.NO_PARAMETERS);
    }

    @Test
    public void testWildcardSetParametersDifferent2() {
        final CharsetHeaderValue charset = CharsetHeaderValue.WILDCARD_VALUE;
        final Map<CharsetHeaderValueParameterName<?>, Object> parameters = this.parameters("different", "2");
        assertSame(charset, charset.setParameters(parameters).setParameters(CharsetHeaderValue.NO_PARAMETERS));
    }

    // toHeaderText ...........................................................................................

    @Test
    public void testToHeaderTextNoParameters() {
        this.toHeaderTextAndCheck(CharsetHeaderValue.with(VALUE),
                VALUE.toHeaderText());
    }

    @Test
    public void testToHeaderTextWithParameters() {
        this.toHeaderTextAndCheck(this.charsetHeaderValue(),
                VALUE + "; p1=v1");
    }

    @Test
    public void testToHeaderTextWithSeveralParameters() {
        this.toHeaderTextAndCheck(CharsetHeaderValue.with(VALUE)
                        .setParameters(this.parameters("p1", "v1", "p2", "v2")),
                VALUE + "; p1=v1; p2=v2");
    }

    // isWildcard ..................................................................................................

    @Test
    public void testIsWildcard() {
        this.isWildcardAndCheck(false);
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsDifferentCharset() {
        this.checkNotEquals(CharsetHeaderValue.with(CharsetName.UTF_16)
                .setParameters(this.parameters()));
    }

    @Test
    public void testEqualsDifferentParameterValue() {
        this.checkNotEquals(CharsetHeaderValue.with(VALUE)
                .setParameters(this.parameters(CharsetHeaderValueParameterName.Q_FACTOR, Q + 0.5f)));
    }

    @Test
    public void testEqualsDifferentParameter() {
        this.checkNotEquals(CharsetHeaderValue.with(VALUE)
                .setParameters(this.parameters(CharsetHeaderValueParameterName.with("different"), "xyz")));
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

    // toHeaderTextList ...........................................................................................

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

    // helpers ...........................................................................................

    @Override
    public CharsetHeaderValue createHeaderValueWithParameters() {
        return this.charsetHeaderValue();
    }

    private CharsetHeaderValue charsetHeaderValue() {
        return CharsetHeaderValue.with(VALUE).setParameters(this.parameters());
    }

    @Override
    CharsetHeaderValueParameterName<?> parameterName() {
        return CharsetHeaderValueParameterName.with("xyz");
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
        return Maps.of(name, value);
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
        return Maps.of(name1, value1, name2, value2);
    }

    private void check(final CharsetHeaderValue charsetHeaderValue) {
        this.check(charsetHeaderValue, VALUE, charsetHeaderValue.parameters());
    }

    private void check(final CharsetHeaderValue charsetHeaderValue,
                       final CharsetName value,
                       final Map<CharsetHeaderValueParameterName<?>, Object> parameters) {
        assertEquals(value, charsetHeaderValue.value(), "value");
        this.checkParameters(charsetHeaderValue, parameters);
    }

    @Override
    public boolean isMultipart() {
        return false;
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
    public Class<CharsetHeaderValue> type() {
        return CharsetHeaderValue.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
