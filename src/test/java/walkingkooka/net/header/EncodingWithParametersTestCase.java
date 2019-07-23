/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.predicate.PredicateTesting2;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.JavaVisibility;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public abstract class EncodingWithParametersTestCase<A extends EncodingWithParameters> extends HeaderValueWithParametersTestCase<EncodingWithParameters, EncodingParameterName<?>>
        implements ComparableTesting<EncodingWithParameters>,
        ParseStringTesting<EncodingWithParameters>,
        PredicateTesting2<EncodingWithParameters, ContentEncoding> {

    EncodingWithParametersTestCase() {
        super();
    }

    @Test
    public void testWith() {
        this.checkValue(this.createHeaderValue());
    }

    @Test
    public void testWith2() {
        final String text = "unknown";
        this.checkValue(EncodingWithParameters.with(text),
                text,
                EncodingWithParameters.NO_PARAMETERS);
    }

    @Test
    public final void testSetParametersDifferent() {
        final A acceptEncoding = this.createHeaderValueWithParameters();

        final Map<EncodingParameterName<?>, Object> parameters = Maps.of(EncodingParameterName.Q_FACTOR, 0.5f);
        final EncodingWithParameters different = acceptEncoding.setParameters(parameters);
        assertNotSame(parameters, different);

        this.checkValue(different, acceptEncoding.value(), parameters);
        this.checkValue(acceptEncoding);
    }

    final void checkValue(final EncodingWithParameters encoding) {
        this.checkValue(encoding, this.value(), EncodingWithParameters.NO_PARAMETERS);
    }

    final void checkValue(final EncodingWithParameters encoding,
                          final String value,
                          final Map<EncodingParameterName<?>, Object> parameters) {
        assertEquals(value, encoding.value(), "value");
        assertEquals(parameters, encoding.parameters(), "parameters");
    }

    @Test
    public final void testHeaderText() {
        final String text = this.value();
        this.toHeaderTextAndCheck(EncodingWithParameters.with(text), text);
    }

    @Test
    public final void testHeaderTextWithParameters() {
        final String text = this.value();
        this.toHeaderTextAndCheck(EncodingWithParameters.with(text).setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f)),
                text + "; q=0.5");
    }

    // HashCodeEqualsDefined ...........................................................................................

    @Test
    public final void testEqualsNonWildcardDifferentValue() {
        this.checkNotEquals(EncodingWithParameters.with("different"));
    }

    @Test
    public final void testEqualsDifferentParameters() {
        this.checkNotEquals(this.createHeaderValueWithParameters().setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f)));
    }

    @Test
    public final void testEqualsParameters() {
        final Map<EncodingParameterName<?>, Object> parameters = Maps.of(EncodingParameterName.Q_FACTOR, 0.5f);
        final A acceptEncoding = this.createHeaderValueWithParameters();

        this.checkEqualsAndHashCode(acceptEncoding.setParameters(parameters), acceptEncoding.setParameters(parameters));
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.createHeaderValue(), this.value());
    }

    @Test
    public final void testToStringWithParameters() {
        final String value = this.value();
        this.toStringAndCheck(EncodingWithParameters.with(value).setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f)),
                value + "; q=0.5");
    }

    public abstract A createHeaderValueWithParameters();

    abstract String value();

    @Override
    final EncodingParameterName<?> parameterName() {
        return EncodingParameterName.with("xyz");
    }

    @Override
    public final boolean isMultipart() {
        return false;
    }

    @Override
    public final boolean isRequest() {
        return true;
    }

    @Override
    public final boolean isResponse() {
        return false;
    }

    // ClassTesting................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public final Class<EncodingWithParameters> type() {
        return Cast.to(this.encodingType());
    }

    abstract Class<A> encodingType();

    // ComparableTesting................................................................................................

    @Override
    public final A createObject() {
        return this.createComparable();
    }

    @Override
    public final A createComparable() {
        return this.createHeaderValueWithParameters();
    }


    // ParsingStringTest................................................................................................

    @Override
    public EncodingWithParameters parse(final String text) {
        return EncodingWithParameters.parse(text);
    }

    // PredicateTesting2...............................................................................................

    @Override
    public final EncodingWithParameters createPredicate() {
        return this.createHeaderValueWithParameters();
    }

    // ClassName..Testing...............................................................................................

    @Override
    public final String typeNamePrefix() {
        return EncodingWithParameters.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
