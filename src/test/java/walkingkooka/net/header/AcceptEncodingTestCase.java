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
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.predicate.PredicateTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.MemberVisibility;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public abstract class AcceptEncodingTestCase<A extends AcceptEncoding> extends HeaderValueWithParametersTestCase<AcceptEncoding, AcceptEncodingParameterName<?>>
        implements ComparableTesting<AcceptEncoding>,
        ParseStringTesting<List<AcceptEncoding>>,
        PredicateTesting<AcceptEncoding, ContentEncoding> {

    AcceptEncodingTestCase() {
        super();
    }

    @Test
    public void testWith() {
        this.checkValue(this.createHeaderValue());
    }

    @Test
    public void testWith2() {
        final String text = "unknown";
        this.checkValue(AcceptEncoding.with(text),
                text,
                AcceptEncoding.NO_PARAMETERS);
    }

    @Test
    public final void testSetParametersDifferent() {
        final A acceptEncoding = this.createHeaderValueWithParameters();

        final Map<AcceptEncodingParameterName<?>, Object> parameters = Maps.of(AcceptEncodingParameterName.Q_FACTOR, 0.5f);
        final AcceptEncoding different = acceptEncoding.setParameters(parameters);
        assertNotSame(parameters, different);

        this.checkValue(different, acceptEncoding.value(), parameters);
        this.checkValue(acceptEncoding);
    }

    final void checkValue(final AcceptEncoding acceptEncoding) {
        this.checkValue(acceptEncoding, this.value(), AcceptEncoding.NO_PARAMETERS);
    }

    final void checkValue(final AcceptEncoding acceptEncoding,
                          final String value,
                          final Map<AcceptEncodingParameterName<?>, Object> parameters) {
        assertEquals(value, acceptEncoding.value(), "value");
        assertEquals(parameters, acceptEncoding.parameters(), "parameters");
    }

    @Test
    public final void testHeaderText() {
        final String text = this.value();
        this.toHeaderTextAndCheck(AcceptEncoding.with(text), text);
    }

    @Test
    public final void testHeaderTextWithParameters() {
        final String text = this.value();
        this.toHeaderTextAndCheck(AcceptEncoding.with(text).setParameters(Maps.of(AcceptEncodingParameterName.Q_FACTOR, 0.5f)),
                text + "; q=0.5");
    }

    // HashCodeEqualsDefined ...........................................................................................

    @Test
    public final void testEqualsNonWildcardDifferentValue() {
        this.checkNotEquals(AcceptEncoding.with("different"));
    }

    @Test
    public final void testEqualsDifferentParameters() {
        this.checkNotEquals(this.createHeaderValueWithParameters().setParameters(Maps.of(AcceptEncodingParameterName.Q_FACTOR, 0.5f)));
    }

    @Test
    public final void testEqualsParameters() {
        final Map<AcceptEncodingParameterName<?>, Object> parameters = Maps.of(AcceptEncodingParameterName.Q_FACTOR, 0.5f);
        final A acceptEncoding = this.createHeaderValueWithParameters();

        this.checkEquals(acceptEncoding.setParameters(parameters), acceptEncoding.setParameters(parameters));
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.createHeaderValue(), this.value());
    }

    @Test
    public final void testToStringWithParameters() {
        final String value = this.value();
        this.toStringAndCheck(AcceptEncoding.with(value).setParameters(Maps.of(AcceptEncodingParameterName.Q_FACTOR, 0.5f)),
                value + "; q=0.5");
    }

    public abstract A createHeaderValueWithParameters();

    abstract String value();

    @Override
    final AcceptEncodingParameterName<?> parameterName() {
        return AcceptEncodingParameterName.with("xyz");
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
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public final Class<AcceptEncoding> type() {
        return Cast.to(this.acceptEncodingType());
    }

    abstract Class<A> acceptEncodingType();

    // ComparableTesting................................................................................................

    @Override
    public final A createObject() {
        return this.createComparable();
    }

    @Override
    public final A createComparable() {
        return this.createHeaderValueWithParameters();
    }

    // ParseStringTesting...............................................................................................

    @Override
    public final List<AcceptEncoding> parse(final String text) {
        return AcceptEncoding.parse(text);
    }

    // PredicateTesting...............................................................................................

    @Override
    public final AcceptEncoding createPredicate() {
        return this.createHeaderValueWithParameters();
    }

    // TypeName..Testing...............................................................................................

    @Override
    public final String typeNamePrefix() {
        return AcceptEncoding.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
