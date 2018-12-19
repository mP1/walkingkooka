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
import walkingkooka.collect.map.Maps;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class ContentDispositionTest extends HeaderValueWithParametersTestCase<ContentDisposition,
        ContentDispositionParameterName<?>> {

    private final static ContentDispositionType TYPE = ContentDispositionType.ATTACHMENT;
    private final static String PARAMETER_VALUE = "v1";

    // with.........................................................................................

    @Test(expected = NullPointerException.class)
    public void testWithNullValueFails() {
        ContentDisposition.with(null);
    }

    @Test
    public void testWith() {
        final ContentDisposition token = this.createHeaderValueWithParameters();
        this.check(token);
    }

    // setType ...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetTypeNullFails() {
        this.createHeaderValueWithParameters().setType(null);
    }

    @Test
    public void testSetTypeSame() {
        final ContentDisposition disposition = this.createHeaderValueWithParameters();
        assertSame(disposition, disposition.setType(TYPE));
    }

    @Test
    public void testSetTypeDifferent() {
        final ContentDisposition disposition = this.createHeaderValueWithParameters();
        final ContentDispositionType type = ContentDispositionType.INLINE;
        this.check(disposition.setType(type), type, this.parameters());
        this.check(disposition);
    }

    // setParameters ...........................................................................................

    @Test
    public void testSetParametersDifferent() {
        final ContentDisposition disposition = this.createHeaderValueWithParameters();
        final Map<ContentDispositionParameterName<?>, Object> parameters = this.parameters("different", "2");
        this.check(disposition.setParameters(parameters), TYPE, parameters);
        this.check(disposition);
    }

    // toHeaderText ...........................................................................................

    @Test
    public void testToHeaderTextNoParameters() {
        this.toHeaderTextAndCheck(ContentDisposition.with(TYPE),
                "attachment");
    }

    @Test
    public void testToHeaderTextWithParameters() {
        this.toHeaderTextAndCheck(this.createHeaderValueWithParameters(),
                "attachment; p1=v1");
    }

    @Test
    public void testToHeaderTextWithSeveralParameters() {
        this.toHeaderTextAndCheck(ContentDisposition.with(TYPE)
                        .setParameters(this.parameters("p1", "v1", "p2", "v2")),
                "attachment; p1=v1; p2=v2");
    }

    // toString ...........................................................................................

    @Test
    public void testToStringNoParameters() {
        this.toStringAndCheck(ContentDisposition.with(TYPE),
                "attachment");
    }

    @Test
    public void testToStringWithParameters() {
        this.toStringAndCheck(this.createHeaderValueWithParameters(),
                "attachment; p1=v1");
    }

    @Test
    public void testToStringWithSeveralParameters() {
        this.toStringAndCheck(ContentDisposition.with(TYPE)
                        .setParameters(this.parameters("p1", "v1", "p2", "v2")),
                "attachment; p1=v1; p2=v2");
    }

    private void toStringAndCheck(final ContentDisposition token, final String toString) {
        assertEquals("toString", toString, token.toString());
    }

    // helpers ...........................................................................................

    @Override
    protected ContentDisposition createHeaderValueWithParameters() {
        return ContentDisposition.with(TYPE)
                .setParameters(this.parameters());
    }

    private Map<ContentDispositionParameterName<?>, Object> parameters() {
        return this.parameters("p1", PARAMETER_VALUE);
    }

    private Map<ContentDispositionParameterName<?>, Object> parameters(final String name, final Object value) {
        return this.parameters(ContentDispositionParameterName.with(name), value);
    }

    private Map<ContentDispositionParameterName<?>, Object> parameters(final ContentDispositionParameterName<?> name,
                                                                       final Object value) {
        return Maps.one(name, value);
    }

    private Map<ContentDispositionParameterName<?>, Object> parameters(final String name1,
                                                                       final Object value1,
                                                                       final String name2,
                                                                       final Object value2) {
        return this.parameters(ContentDispositionParameterName.with(name1),
                value1,
                ContentDispositionParameterName.with(name2),
                value2);
    }

    private Map<ContentDispositionParameterName<?>, Object> parameters(final ContentDispositionParameterName<?> name1,
                                                                       final Object value1,
                                                                       final ContentDispositionParameterName<?> name2,
                                                                       final Object value2) {
        final Map<ContentDispositionParameterName<?>, Object> parameters = Maps.ordered();
        parameters.put(name1, value1);
        parameters.put(name2, value2);
        return parameters;
    }

    private void check(final ContentDisposition token) {
        this.check(token, TYPE, token.parameters());
    }

    private void check(final ContentDisposition token,
                       final ContentDispositionType type,
                       final Map<ContentDispositionParameterName<?>, Object> parameters) {
        assertEquals("type", type, token.type());
        assertEquals("parameters", parameters, token.parameters());
    }

    @Override
    protected boolean isMultipart() {
        return false;
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
    protected Class<ContentDisposition> type() {
        return ContentDisposition.class;
    }
}
