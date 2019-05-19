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
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ContentDispositionTest extends HeaderValueWithParametersTestCase<ContentDisposition,
        ContentDispositionParameterName<?>>
        implements ParseStringTesting<ContentDisposition> {

    private final static ContentDispositionType TYPE = ContentDispositionType.ATTACHMENT;
    private final static String PARAMETER_VALUE = "v1";

    // with.........................................................................................

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            ContentDisposition.with(null);
        });
    }

    @Test
    public void testWith() {
        final ContentDisposition token = this.createHeaderValueWithParameters();
        this.check(token);
    }

    // setType ...........................................................................................

    @Test
    public void testSetTypeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createHeaderValueWithParameters().setType(null);
        });
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

    // parse ...........................................................................................

    @Test
    public void testParse() {
        this.parseAndCheck("attachment; filename=\"abc.jpg\"",
                ContentDispositionType.ATTACHMENT.setParameters(Maps.of(ContentDispositionParameterName.FILENAME, ContentDispositionFileName.notEncoded("abc.jpg"))));
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

    // isWildcard ..................................................................................................

    @Test
    public void testIsWildcard() {
        this.isWildcardAndCheck(false);
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsDifferentContentDispositionType() {
        this.checkNotEquals2("inline; p1=\"v1\";");
    }

    @Test
    public void testEqualsDifferentParameters() {
        this.checkNotEquals2("attachment; p2=\"v2\";");
    }

    @Test
    public void testEqualsDifferentParameters2() {
        this.checkNotEquals2("attachment; p1=\"v1\"; p2=\"v2\";");
    }

    private void checkNotEquals2(final String disposition2) {
        this.checkNotEquals(ContentDisposition.parse("attachment; p1=\"v1\";"),
                ContentDisposition.parse(disposition2));
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

    // helpers ...........................................................................................

    @Override
    public ContentDisposition createHeaderValueWithParameters() {
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
        return Maps.of(name, value);
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
        return Maps.of(name1, value1, name2, value2);
    }

    private void check(final ContentDisposition token) {
        this.check(token, TYPE, token.parameters());
    }

    private void check(final ContentDisposition token,
                       final ContentDispositionType type,
                       final Map<ContentDispositionParameterName<?>, Object> parameters) {
        assertEquals(type, token.type(), "type");
        assertEquals(parameters, token.parameters(), "parameters");
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
    public Class<ContentDisposition> type() {
        return ContentDisposition.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // ParseStringTesting ........................................................................................

    @Override
    public ContentDisposition parse(final String text) {
        return ContentDisposition.parse(text);
    }
}
