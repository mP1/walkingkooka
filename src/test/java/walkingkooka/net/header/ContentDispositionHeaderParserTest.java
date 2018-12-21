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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

public final class ContentDispositionHeaderParserTest extends HeaderParserWithParametersTestCase<ContentDispositionHeaderParser,
        ContentDisposition> {

    // parse ...................................................................................................

    @Test
    public void testType() {
        this.parseAndCheck("A", "A");
    }

    @Test
    public void testTypeSeparatorFails() {
        this.parseMissingValueFails("A,");
    }

    @Test
    public void testTypeSeparatorSpaceFails() {
        this.parseMissingValueFails("A, ");
    }

    @Test
    public void testTypeSeparatorTabFails() {
        this.parseMissingValueFails("A,\t");
    }

    @Test
    public void testTypeInvalidCharacterFails() {
        this.parseInvalidCharacterFails("A<");
    }

    @Test
    public void testTypeInvalidCharacterFails2() {
        this.parseInvalidCharacterFails("ABC<");
    }

    @Test
    public void testTypeEqualsFails() {
        this.parseMissingParameterValueFails("A;b=");
    }

    @Test
    public void testTypeSpaceEqualsFails() {
        this.parseMissingParameterValueFails("A;b =");
    }

    @Test
    public void testTypeTabEqualsFails() {
        this.parseMissingParameterValueFails("A;b =");
    }

    @Test
    public void testTypeSpaceTabSpaceTabEqualsFails() {
        this.parseMissingParameterValueFails("A;b \t \t=");
    }

    @Test
    public void testTypeEqualsSpaceFails() {
        parseMissingParameterValueFails("A;b= ");
    }

    @Test
    public void testTypeEqualsTabFails() {
        parseMissingParameterValueFails("A;b=\t");
    }

    @Test
    public void testTypeEqualsCrNlSpaceFails() {
        parseMissingParameterValueFails("A;b=\r\n ");
    }

    @Test
    public void testTypeEqualsCrNlTabFails() {
        parseMissingParameterValueFails("A;b=\r\n\t");
    }

    @Test
    public void testTypeParameterSeparatorSeparatorFails() {
        this.parseInvalidCharacterFails("A;;", 2);
    }

    @Test
    public void testType2() {
        this.parseAndCheck("ABC", "ABC");
    }

    @Test
    public void testTypeSpace() {
        this.parseAndCheck("A ", "A");
    }

    @Test
    public void testTypeTab() {
        this.parseAndCheck("A\t", "A");
    }

    @Test
    public void testTypeCrFails() {
        this.parseInvalidCharacterFails("A\r");
    }

    @Test
    public void testTypeCrNlFails() {
        this.parseInvalidCharacterFails("A\r\n");
    }

    @Test
    public void testTypeCrNlNonWhitespaceFails() {
        this.parseInvalidCharacterFails("A\r\n.");
    }

    @Test
    public void testTypeCrNlSpace() {
        this.parseAndCheck("A\r\n ", "A");
    }

    @Test
    public void testTypeCrNlTab() {
        this.parseAndCheck("A\r\n\t", "A");
    }

    @Test
    public void testTypeSpaceTabSpaceTab() {
        this.parseAndCheck("A \t \t", "A");
    }

    @Test
    public void testTypeSeparator() {
        this.parseAndCheck("A;", "A");
    }

    @Test
    public void testTypeSeparatorSpace() {
        this.parseAndCheck("A; ", "A");
    }

    @Test
    public void testTypeSeparatorTab() {
        this.parseAndCheck("A;\t", "A");
    }

    @Test
    public void testTypeParameterSeparatorEqualsFails() {
        this.parseInvalidCharacterFails("A;=");
    }

    @Test
    public void testTypeParameterNameInvalidCharFails() {
        this.parseInvalidCharacterFails("A;b>=c", '>');
    }

    @Test
    public void testTypeParameterNameSpaceInvalidCharFails() {
        this.parseInvalidCharacterFails("A;b >=c", '>');
    }

    @Test
    public void testTypeParameterNameTabInvalidCharFails() {
        this.parseInvalidCharacterFails("A;b\t>=c", '>');
    }

    @Test
    public void testTypeParameterNameEqualsInvalidCharFails() {
        this.parseInvalidCharacterFails("A;b=\0c", '\0');
    }

    @Test
    public void testTypeMultiValueSeparatorFails() {
        this.parseMissingValueFails("A,");
    }

    @Test
    public void testTypeParameterNameEqualsParameterValue() {
        this.parseAndCheck("A;b=c",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeParameterNameSpaceEqualsParameterValue() {
        this.parseAndCheck("A;b =c",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeParameterNameTabEqualsParameterValue() {
        this.parseAndCheck("A;b\t=c",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeParameterNameSpaceTabSpaceTabEqualsParameterValue() {
        this.parseAndCheck("A;b \t \t=c",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeParameterNameEqualsSpaceParameterValue() {
        this.parseAndCheck("A;b= c",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeParameterNameEqualsTabParameterValue() {
        this.parseAndCheck("A;b=\tc",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeParameterNameEqualsSpaceTabSpaceTabParameterValue() {
        this.parseAndCheck("A;b= \t \tc",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeParameterQuotedParameterValue() {
        final String text = "A;b=c\"d\"";
        this.parseInvalidCharacterFails(text, text.indexOf('d') - 1);
    }

    @Test
    public void testTypeParameterNameEqualsQuotedParameterValue() {
        this.parseAndCheck("A;b=\"c\"",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeParameterValueInvalidCharFails() {
        this.parseInvalidCharacterFails("A;b=c>", '>');
    }

    @Test
    public void testTypeParameterValueSpaceInvalidCharFails() {
        this.parseInvalidCharacterFails("A;b=c Q", 'Q');
    }

    @Test
    public void testTypeParameterValueSpaceInvalidCharFails2() {
        this.parseInvalidCharacterFails("A;b=c >", '>');
    }

    @Test
    public void testTypeParameterSpace() {
        this.parseAndCheck("A;bcd=123 ",
                "A",
                "bcd", "123");
    }

    @Test
    public void testTypeParameterSeparator() {
        this.parseAndCheck("A;bcd=123;",
                "A",
                "bcd", "123");
    }

    @Test
    public void testTypeParameterTab() {
        this.parseAndCheck("A;bcd=123\t",
                "A",
                "bcd", "123");
    }

    @Test
    public void testTypeParameterSpaceTabSpaceTab() {
        this.parseAndCheck("A;bcd=123 \t \t",
                "A",
                "bcd", "123");
    }

    @Test
    public void testTypeParameterSeparatorSpaceParameter() {
        this.parseAndCheck("A; b=c",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeParameterSeparatorTabParameter() {
        this.parseAndCheck("A;\tb=c",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeSpaceParameterSeparatorParameter() {
        this.parseAndCheck("A ;b=c",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeSpaceParameterSeparatorSpaceParameter() {
        this.parseAndCheck("A ; b=c",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeTabParameterSeparatorTabParameter() {
        this.parseAndCheck("A\t;\tb=c",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeSpaceTabSpaceTabParameterSeparatorSpaceTabParameter() {
        this.parseAndCheck("A \t \t; \t \tb=c",
                "A",
                "b", "c");
    }

    @Test
    public void testTypeParameterSeparatorParameterNameFails() {
        final String text = "A;b=c;D";
        this.parseMissingParameterValueFails(text);
    }

    @Test
    public void testTypeParameter() {
        this.parseAndCheck("V1;p1=v1;",
                "V1",
                "p1", "v1");
    }

    @Test
    public void testTypeParameterSeparatorParameter() {
        this.parseAndCheck("V1;p1=v1;p2=v2",
                "V1",
                "p1", "v1",
                "p2", "v2");
    }

    @Test
    public void testTypeParameterWhitespaceSeparatorParameter() {
        this.parseAndCheck("V1;p1=v1 ;p2=v2",
                "V1",
                "p1", "v1",
                "p2", "v2");
    }

    @Test
    public void testTypeParameterParameterSeparatorWhitespaceParameter() {
        this.parseAndCheck("V1;p1=v1; p2=v2",
                "V1",
                "p1", "v1",
                "p2", "v2");
    }

    @Test
    public void testTypeParameterSeparatorQuoteFails() {
        this.parseMissingClosingQuoteFails("V1;p1=\"");
    }

    // parse creation-date............................................................................................

    @Test
    public void testCreationDateInvalidFails() {
        this.parseFails("V; creation-date=123",
                "Failed to convert \"creation-date\" value \"123\", message: Invalid character '1' at 0 in \"123\"");
    }

    @Test
    public void testCreationDate() {
        this.parseAndCheck("attachment; creation-date=\"Wed, 12 Feb 1997 16:29:51 -0500\"",
                "attachment",
                "creation-date",
                OffsetDateTime.of(1997, 2, 12, 16, 29, 51, 0, ZoneOffset.ofHours(-5)));
    }

    // parse filename............................................................................................

    @Test
    public void testFilenameMissingFails() {
        this.parseFails("V; filename=\"\"",
                "Failed to convert \"filename\" value \"\"\"\", message: name is empty");
    }

    @Test
    public void testFilenameMissingFails2() {
        this.parseMissingParameterValueFails("V; filename=");
    }

    @Test
    public void testFilename() {
        this.parseAndCheck("attachment; filename=readme.txt",
                "attachment",
                "filename",
                ContentDispositionFileName.with("readme.txt"));
    }

    @Test
    public void testFilenameQuoted() {
        this.parseAndCheck("attachment; filename=\"readme.txt\"",
                "attachment",
                "filename",
                ContentDispositionFileName.with("readme.txt"));
    }

    // parse modification-date............................................................................................

    @Test
    public void testModificationDateInvalidFails() {
        this.parseFails("V; modification-date=123",
                "Failed to convert \"modification-date\" value \"123\", message: Invalid character '1' at 0 in \"123\"");
    }

    @Test
    public void testModificationDate() {
        this.parseAndCheck("attachment; modification-date=\"Wed, 12 Feb 1997 16:29:51 -0500\"",
                "attachment",
                "modification-date",
                OffsetDateTime.of(1997, 2, 12, 16, 29, 51, 0, ZoneOffset.ofHours(-5)));
    }

    // parse read-date............................................................................................

    @Test
    public void testReadDateInvalidFails() {
        this.parseFails("V; read-date=123",
                "Failed to convert \"read-date\" value \"123\", message: Invalid character '1' at 0 in \"123\"");
    }

    @Test
    public void testReadDate() {
        this.parseAndCheck("attachment; read-date=\"Wed, 12 Feb 1997 16:29:51 -0500\"",
                "attachment",
                "read-date",
                OffsetDateTime.of(1997, 2, 12, 16, 29, 51, 0, ZoneOffset.ofHours(-5)));
    }

    // parse filename............................................................................................

    @Test
    public void testSizeInvalidFails() {
        this.parseFails("V; size=A",
                "Failed to convert \"size\" value \"A\", message: For input string: \"A\"");
    }

    @Test
    public void testSize() {
        this.parseAndCheck("attachment; size=123",
                "attachment",
                "size",
                123L);
    }

    // helpers...................................................................................................

    @Override
    ContentDisposition parse(final String text) {
        return ContentDispositionHeaderParser.parseContentDisposition(text);
    }

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
                ContentDispositionType.with(type).setParameters(parameters));
    }

    @Override
    String valueLabel() {
        return ContentDispositionHeaderParser.TYPE;
    }

    @Override
    protected Class<ContentDispositionHeaderParser> type() {
        return ContentDispositionHeaderParser.class;
    }
}
