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
import walkingkooka.text.CharSequences;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public final class ContentDispositionTest extends HeaderValueTestCase<ContentDisposition> {

    private final static ContentDispositionType TYPE = ContentDispositionType.ATTACHMENT;
    private final static String PARAMETER_VALUE = "v1";

    // with.........................................................................................

    @Test(expected = NullPointerException.class)
    public void testWithNullValueFails() {
        ContentDisposition.with(null, ContentDisposition.NO_PARAMETERS);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullParametersFails() {
        ContentDisposition.with(TYPE, null);
    }

    @Test
    public void testWith() {
        final ContentDisposition token = this.contentDisposition();
        this.check(token);
    }

    // setType ...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetTypeNullFails() {
        this.contentDisposition().setType(null);
    }

    @Test
    public void testSetTypeSame() {
        final ContentDisposition token = this.contentDisposition();
        assertSame(token, token.setType(TYPE));
    }

    @Test
    public void testSetTypeDifferent() {
        final ContentDisposition token = this.contentDisposition();
        final ContentDispositionType type = ContentDispositionType.INLINE;
        this.check(token.setType(type), type, this.parameters());
        this.check(token);
    }

    // setParameters ...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetParametersNullFails() {
        this.contentDisposition().setParameters(null);
    }

    @Test
    public void testSetParametersSame() {
        final ContentDisposition token = this.contentDisposition();
        assertSame(token, token.setParameters(this.parameters()));
    }

    @Test
    public void testSetParametersDifferent() {
        final ContentDisposition token = this.contentDisposition();
        final Map<ContentDispositionParameterName<?>, Object> parameters = this.parameters("different", "2");
        this.check(token.setParameters(parameters), TYPE, parameters);
        this.check(token);
    }

    // parse ...................................................................................................

    @Test(expected = NullPointerException.class)
    public void testParseNullFails() {
        ContentDisposition.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyFails() {
        ContentDisposition.parse("");
    }

    @Test
    public void testParseType() {
        this.parseAndCheck("A", "A");
    }

    @Test
    public void testParseTypeInvalidCharacterFails() {
        this.parseFails("A<");
    }

    @Test
    public void testParseTypeInvalidCharacterFails2() {
        this.parseFails("ABC<");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeEqualsFails() {
        ContentDisposition.parse("A;b=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeSpaceEqualsFails() {
        ContentDisposition.parse("A;b =");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeTabEqualsFails() {
        ContentDisposition.parse("A;b =");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeSpaceTabSpaceTabEqualsFails() {
        ContentDisposition.parse("A;b \t \t=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeEqualsSpaceFails() {
        ContentDisposition.parse("A;b= ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeEqualsTabFails() {
        ContentDisposition.parse("A;b=\t");
    }

    @Test
    public void testParseTypeParameterSeparatorSeparatorFails() {
        this.parseFails("A;;", 2);
    }

    @Test
    public void testParseType2() {
        this.parseAndCheck("ABC", "ABC");
    }

    @Test
    public void testParseTypeSpace() {
        this.parseAndCheck("A ", "A");
    }

    @Test
    public void testParseTypeTab() {
        this.parseAndCheck("A\t", "A");
    }

    @Test
    public void testParseTypeSpaceTabSpaceTab() {
        this.parseAndCheck("A \t \t", "A");
    }

    @Test
    public void testParseTypeSeparator() {
        this.parseAndCheck("A;", "A");
    }

    @Test
    public void testParseTypeSeparatorSpace() {
        this.parseAndCheck("A; ", "A");
    }

    @Test
    public void testParseTypeSeparatorTab() {
        this.parseAndCheck("A;\t", "A");
    }

    @Test
    public void testParseTypeParameterSeparatorFails() {
        final String text = "A;=";
        this.parseFails(text,
                ContentDisposition.emptyToken(ContentDisposition.PARAMETER_NAME, 2, text));
    }

    @Test
    public void testParseTypeParameterNameInvalidCharFails() {
        this.parseFails("A;b>=c", '>');
    }

    @Test
    public void testParseTypeParameterNameSpaceInvalidCharFails() {
        this.parseFails("A;b >=c", '>');
    }

    @Test
    public void testParseTypeParameterNameTabInvalidCharFails() {
        this.parseFails("A;b\t>=c", '>');
    }

    @Test
    public void testParseTypeParameterNameEqualsInvalidCharFails() {
        this.parseFails("A;b=\0c", '\0');
    }

    @Test
    public void testParseTypeParameterNameEqualsParameterValue() {
        this.parseAndCheck("A;b=c",
                "A",
                "b", "c");
    }

    @Test
    public void testParseTypeParameterNameSpaceEqualsParameterValue() {
        this.parseAndCheck("A;b =c",
                "A",
                "b", "c");
    }

    @Test
    public void testParseTypeParameterNameTabEqualsParameterValue() {
        this.parseAndCheck("A;b\t=c",
                "A",
                "b", "c");
    }

    @Test
    public void testParseTypeParameterNameSpaceTabSpaceTabEqualsParameterValue() {
        this.parseAndCheck("A;b \t \t=c",
                "A",
                "b", "c");
    }

    @Test
    public void testParseTypeParameterNameEqualsSpaceParameterValue() {
        this.parseAndCheck("A;b= c",
                "A",
                "b", "c");
    }

    @Test
    public void testParseTypeParameterNameEqualsTabParameterValue() {
        this.parseAndCheck("A;b=\tc",
                "A",
                "b", "c");
    }

    @Test
    public void testParseTypeParameterNameEqualsSpaceTabSpaceTabParameterValue() {
        this.parseAndCheck("A;b= \t \tc",
                "A",
                "b", "c");
    }

    @Test
    public void testParseTypeParameterValueInvalidCharFails() {
        this.parseFails("A;b=c>", '>');
    }

    @Test
    public void testParseTypeParameterValueSpaceInvalidCharFails() {
        this.parseFails("A;b=c Q", 'Q');
    }

    @Test
    public void testParseTypeParameterValueSpaceInvalidCharFails2() {
        this.parseFails("A;b=c >", '>');
    }

    @Test
    public void testParseTypeParameterSpace() {
        this.parseAndCheck("A;bcd=123 ",
                "A",
                "bcd", "123");
    }

    @Test
    public void testParseTypeParameterTab() {
        this.parseAndCheck("A;bcd=123\t",
                "A",
                "bcd", "123");
    }

    @Test
    public void testParseTypeParameterSpaceTabSpaceTab() {
        this.parseAndCheck("A;bcd=123 \t \t",
                "A",
                "bcd", "123");
    }

    @Test
    public void testParseTypeParameterSeparatorSpaceParameter() {
        this.parseAndCheck("A; b=c",
                "A",
                "b", "c");
    }

    @Test
    public void testParseTypeParameterSeparatorTabParameter() {
        this.parseAndCheck("A;\tb=c",
                "A",
                "b", "c");
    }

    @Test
    public void testParseTypeSpaceParameterSeparatorParameter() {
        this.parseAndCheck("A ;b=c",
                "A",
                "b", "c");
    }

    @Test
    public void testParseTypeSpaceParameterSeparatorSpaceParameter() {
        this.parseAndCheck("A ; b=c",
                "A",
                "b", "c");
    }

    @Test
    public void testParseTypeTabParameterSeparatorTabParameter() {
        this.parseAndCheck("A\t;\tb=c",
                "A",
                "b", "c");
    }

    @Test
    public void testParseTypeSpaceTabSpaceTabParameterSeparatorSpaceTabParameter() {
        this.parseAndCheck("A \t \t; \t \tb=c",
                "A",
                "b", "c");
    }

    @Test
    public void testParseTypeParameterSeparatorValue() {
        final String text = "A;b=c;D";
        this.parseFails(text,
                ContentDisposition.emptyToken(ContentDisposition.PARAMETER_VALUE,
                        7,
                        text));
    }

    @Test
    public void testParseTypeParameters() {
        this.parseAndCheck("V1;p1=v1;p2=v2",
                "V1",
                "p1", "v1",
                "p2", "v2");
    }

    @Test
    public void testParseTypeParameterWhitespaceSemiParameter() {
        this.parseAndCheck("V1;p1=v1 ;p2=v2",
                "V1",
                "p1", "v1",
                "p2", "v2");
    }

    @Test
    public void testParseTypeParameterParameterSeparatorWhitespaceParameter() {
        this.parseAndCheck("V1;p1=v1; p2=v2",
                "V1",
                "p1", "v1",
                "p2", "v2");
    }

    @Test
    public void testParseTypeParameterSeparatorQuoteFails() {
        this.parseFails("V1;p1=\"", "Missing closing quote '\\\"' from \"V1;p1=\"\"");
    }

    @Test
    public void testParseTypeParameterSeparatorQuoteInvalidCharacterFails() {
        this.parseFails("V1;p1=\"/", "Invalid character '/' at 7 in \"V1;p1=\\\"/\"");
    }

    // parse creation-date............................................................................................

    @Test
    public void testParseCreationDateInvalidFails() {
        this.parseFails("V; creation-date=123",
                "Failed to convert \"creation-date\" value \"123\", message: Text '123' could not be parsed at index 3");
    }

    @Test
    public void testParseCreationDate() {
        this.parseAndCheck("attachment; creation-date=\"Wed, 12 Feb 1997 16:29:51 -0500\"",
                "attachment",
                "creation-date",
                OffsetDateTime.of(1997, 2, 12, 16, 29, 51, 0, ZoneOffset.ofHours(-5)));
    }

    // parse filename............................................................................................

    @Test
    public void testParseFilenameInvalidFails() {
        this.parseFails("V; filename=\0");
    }

    @Test
    public void testParseFilename() {
        this.parseAndCheck("attachment; filename=readme.txt",
                "attachment",
                "filename",
                ContentDispositionFilename.with("readme.txt"));
    }

    @Test
    public void testParseFilenameQuoted() {
        this.parseAndCheck("attachment; filename=\"readme.txt\"",
                "attachment",
                "filename",
                ContentDispositionFilename.with("readme.txt"));
    }

    // parse modification-date............................................................................................

    @Test
    public void testParseModificationDateInvalidFails() {
        this.parseFails("V; modification-date=123",
                "Failed to convert \"modification-date\" value \"123\", message: Text '123' could not be parsed at index 3");
    }

    @Test
    public void testParseModificationDate() {
        this.parseAndCheck("attachment; modification-date=\"Wed, 12 Feb 1997 16:29:51 -0500\"",
                "attachment",
                "modification-date",
                OffsetDateTime.of(1997, 2, 12, 16, 29, 51, 0, ZoneOffset.ofHours(-5)));
    }

    // parse read-date............................................................................................

    @Test
    public void testParseReadDateInvalidFails() {
        this.parseFails("V; read-date=123",
                "Failed to convert \"read-date\" value \"123\", message: Text '123' could not be parsed at index 3");
    }

    @Test
    public void testParseReadDate() {
        this.parseAndCheck("attachment; read-date=\"Wed, 12 Feb 1997 16:29:51 -0500\"",
                "attachment",
                "read-date",
                OffsetDateTime.of(1997, 2, 12, 16, 29, 51, 0, ZoneOffset.ofHours(-5)));
    }

    // parse filename............................................................................................

    @Test
    public void testParseSizeInvalidFails() {
        this.parseFails("V; filename=\0");
    }

    @Test
    public void testParseSize() {
        this.parseAndCheck("attachment; size=123",
                "attachment",
                "size",
                123L);
    }

    // helpers...................................................................................................

    private void parseAndCheck(final String headerValue, final String type) {
        this.parseAndCheck(headerValue, type, ContentDisposition.NO_PARAMETERS);
    }

    private void parseAndCheck(final String headerValue,
                               final String type,
                               final String parameterName, final Object parameterValue) {
        this.parseAndCheck(headerValue, type, Maps.one(ContentDispositionParameterName.with(parameterName), parameterValue));
    }

    private void parseAndCheck(final String headerValue,
                               final String type,
                               final String parameterName1, final Object parameterValue1,
                               final String parameterName2, final Object parameterValue2) {
        final Map<ContentDispositionParameterName<?>, Object> parameters = Maps.ordered();
        parameters.put(ContentDispositionParameterName.with(parameterName1), parameterValue1);
        parameters.put(ContentDispositionParameterName.with(parameterName2), parameterValue2);

        this.parseAndCheck(headerValue, type, parameters);
    }


    private void parseAndCheck(final String headerValue,
                               final String type,
                               final Map<ContentDispositionParameterName<?>, Object> parameters) {
        this.parseAndCheck(headerValue,
                ContentDisposition.with(
                        ContentDispositionType.with(type),
                        parameters));
    }

    private void parseAndCheck(final String headerValue, final ContentDisposition disposition) {
        assertEquals("Incorrect result parsing " + CharSequences.quote(headerValue),
                disposition,
                ContentDisposition.parse(headerValue));
    }

    final void parseFails(final String text) {
        this.parseFails(text, text.length() - 1);
    }

    final void parseFails(final String text, final char pos) {
        this.parseFails(text, text.indexOf(pos));
    }

    final void parseFails(final String text, final int pos) {
        parseFails(text, ContentDisposition.invalidCharacter(pos, text));
    }

    final void parseFails(final String text, final String message) {
        try {
            ContentDisposition.parse(text);
            fail();
        } catch (final IllegalArgumentException | HeaderValueException expected) {
            assertEquals("Incorrect failure message", message, expected.getMessage());
        }
    }

    // toString ...........................................................................................

    @Test
    public void testToStringNoParameters() {
        this.toStringAndCheck(ContentDisposition.with(TYPE, ContentDisposition.NO_PARAMETERS),
                "attachment");
    }

    @Test
    public void testToStringWithParameters() {
        this.toStringAndCheck(this.contentDisposition(),
                "attachment; p1=v1");
    }

    @Test
    public void testToStringWithSeveralParameters() {
        this.toStringAndCheck(ContentDisposition.with(TYPE, this.parameters("p1", "v1", "p2", "v2")),
                "attachment; p1=v1; p2=v2");
    }

    private void toStringAndCheck(final ContentDisposition token, final String toString) {
        assertEquals("toString", toString, token.toString());
        assertEquals("headerValue", toString, token.headerValue());
    }

    // helpers ...........................................................................................

    private ContentDisposition contentDisposition() {
        return ContentDisposition.with(TYPE, this.parameters());
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
    protected Class<ContentDisposition> type() {
        return ContentDisposition.class;
    }
}
