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
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public final class HeaderValueTokenHeaderParserTest extends HeaderParserTestCase<HeaderValueTokenHeaderParser,
        HeaderValueTokenParameterName<?>,
        List<HeaderValueToken>> {

    // parse ...................................................................................................

    @Test
    public void testValue() {
        this.parseAndCheck("A", this.token("A"));
    }

    @Test
    public void testValueInvalidCharacterFails() {
        this.parseFails("A<");
    }

    @Test
    public void testValueInvalidCharacterFails2() {
        this.parseFails("ABC<");
    }

    @Test
    public void testValueEqualsFails() {
        this.parseFails("A;b=",
                "Missing parameter value at 3 in \"A;b=\"");
    }

    @Test
    public void testValueSpaceEqualsFails() {
        this.parseFails("A;b =",
                "Missing parameter value at 4 in \"A;b =\"");
    }

    @Test
    public void testValueTabEqualsFails() {
        this.parseFails("A;b =",
                "Missing parameter value at 4 in \"A;b =\"");
    }

    @Test
    public void testValueSpaceTabSpaceTabEqualsFails() {
        this.parseFails("A;b \t \t=",
                "Missing parameter value at 7 in \"A;b \\t \\t=\"");
    }

    @Test
    public void testValueEqualsSpaceFails() {
        this.parseFails("A;b= ",
                "Missing parameter value at 4 in \"A;b= \"");
    }

    @Test
    public void testValueEqualsTabFails() {
        this.parseFails("A;b=\t",
                "Missing parameter value at 4 in \"A;b=\\t\"");
    }

    @Test
    public void testValueSeparatorEquals() {
        this.parseAndCheck("A;b=c,",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueSemiColonSeparatorFails() {
        this.parseFails("A;,", ',');
    }

    @Test
    public void testValue2() {
        this.parseAndCheck("ABC",
                this.token("ABC"));
    }

    @Test
    public void testValueSpace() {
        this.parseAndCheck("A ",
                this.token("A"));
    }

    @Test
    public void testValueTab() {
        this.parseAndCheck("A\t",
                this.token("A"));
    }

    @Test
    public void testValueSpaceTabSpaceTab() {
        this.parseAndCheck("A \t \t",
                this.token("A"));
    }

    @Test
    public void testValueParameterNameInvalidCharFails() {
        this.parseFails("A;b>=c",
                '>');
    }

    @Test
    public void testValueParameterNameSpaceInvalidCharFails() {
        this.parseFails("A;b >=c",
                '>');
    }

    @Test
    public void testValueParameterNameTabInvalidCharFails() {
        this.parseFails("A;b\t>=c",
                '>');
    }

    @Test
    public void testValueParameterNameEqualsInvalidCharFails() {
        this.parseFails("A;b=\0c",
                '\0');
    }

    @Test
    public void testValueParameterNameEqualsParameterValue() {
        this.parseAndCheck("A;b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueParameterNameSpaceEqualsParameterValue() {
        this.parseAndCheck("A;b =c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueParameterNameTabEqualsParameterValue() {
        this.parseAndCheck("A;b\t=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueParameterNameSpaceTabSpaceTabEqualsParameterValue() {
        this.parseAndCheck("A;b \t \t=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueParameterNameEqualsSpaceParameterValue() {
        this.parseAndCheck("A;b= c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueParameterNameEqualsTabParameterValue() {
        this.parseAndCheck("A;b=\tc",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueParameterNameEqualsSpaceTabSpaceTabParameterValue() {
        this.parseAndCheck("A;b= \t \tc",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueParameterValueInvalidCharFails() {
        this.parseFails("A;b=c>",
                '>');
    }

    @Test
    public void testValueParameterValueSpaceInvalidCharFails() {
        this.parseFails("A;b=c Q",
                'Q');
    }

    @Test
    public void testValueParameterValueSpaceInvalidCharFails2() {
        this.parseFails("A;b=c >",
                '>');
    }

    @Test
    public void testValueParameterSpace() {
        this.parseAndCheck("A;bcd=123 ",
                HeaderValueToken.with("A",
                        this.parameters("bcd", "123")));
    }

    @Test
    public void testValueParameterTab() {
        this.parseAndCheck("A;bcd=123\t",
                HeaderValueToken.with("A",
                        this.parameters("bcd", "123")));
    }

    @Test
    public void testValueParameterSpaceTabSpaceTab() {
        this.parseAndCheck("A;bcd=123 \t \t",
                HeaderValueToken.with("A",
                        this.parameters("bcd", "123")));
    }

    @Test
    public void testValueSemiColonSpaceParameter() {
        this.parseAndCheck("A; b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueSemiColonTabParameter() {
        this.parseAndCheck("A;\tb=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueSpaceSemiColonParameter() {
        this.parseAndCheck("A ;b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueSpaceSemiColonSpaceParameter() {
        this.parseAndCheck("A ; b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueTabSemiColonTabParameter() {
        this.parseAndCheck("A\t;\tb=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueSpaceTabSpaceTabSemiColonSpaceTabParameter() {
        this.parseAndCheck("A \t \t; \t \tb=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testValueParameterSeparatorValue() {
        this.parseAndCheck("A;b=c,D",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testValueParameterSeparatorSpaceValue() {
        this.parseAndCheck("A;b=c, D",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testValueParameterSeparatorTabValue() {
        this.parseAndCheck("A;b=c,\tD",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testValueParameterSeparatorSpaceTabSpaceTabValue() {
        this.parseAndCheck("A;b=c, \t \tD",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testValueParameterSeparatorValueSpaceValue() {
        this.parseAndCheck("A;b=c, D;e=f",
                this.token("A", "b", "c"),
                this.token("D", "e", "f"));
    }

    @Test
    public void testValueParameterSeparatorValueTabValue() {
        this.parseAndCheck("A;b=c,\tD;e=f",
                this.token("A", "b", "c"),
                this.token("D", "e", "f"));
    }

    @Test
    public void testValueSeparatorValue() {
        this.parseAndCheck("A,B",
                this.token("A"),
                this.token("B"));
    }

    @Test
    public void testValueSeparatorValue2() {
        this.parseAndCheck("ABC,DEF",
                this.token("ABC"),
                this.token("DEF"));
    }

    @Test
    public void testValueSeparatorValueSeparatorValue() {
        this.parseAndCheck("A,B,C",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testValueSpaceSeparatorValueSpaceSeparatorValueSpace() {
        this.parseAndCheck("A ,B ,C ",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testValueTabSeparatorValueTabSeparatorValueTab() {
        this.parseAndCheck("A\t,B\t,C\t",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testValueSpaceTabSpaceTabSeparatorValueSpaceTabSeparatorValueSpaceTab() {
        this.parseAndCheck("A \t \t,B \t \t,C \t \t",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testValueSeparatorSpaceValueSeparatorSpaceValue() {
        this.parseAndCheck("A, B, C",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testValueSeparatorTabValueSeparatorTabValue() {
        this.parseAndCheck("A,\tB,\tC",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testValueSeparatorSpaceTabSpaceTabValueSeparatorSpaceTabValue() {
        this.parseAndCheck("A, \t \tB, \t \tC",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testValueParameters() {
        this.parseAndCheck("V1;p1=v1;p2=v2",
                this.token("V1", "p1", "v1", "p2", "v2"));
    }

    @Test
    public void testValueParametersSeparatorValueParametersSeparatorValueParameters() {
        this.parseAndCheck("V1;p1=v1,V2;p2=v2,V3;p3=v3",
                this.token("V1", "p1", "v1"),
                this.token("V2", "p2", "v2"),
                this.token("V3", "p3", "v3"));
    }

    @Test
    public void testValueParametersWhitespaceSeparatorValueParametersWhitespaceSeparatorValueParametersWhitespace() {
        this.parseAndCheck("V1;p1=v1 ,V2;p2=v2 ,V3;p3=v3 ",
                this.token("V1", "p1", "v1"),
                this.token("V2", "p2", "v2"),
                this.token("V3", "p3", "v3"));
    }

    @Test
    public void testValueParameterWhitespaceSemiParameter() {
        this.parseAndCheck("V1;p1=v1 ;p2=v2",
                this.token("V1", "p1", "v1", "p2", "v2"));
    }

    @Test
    public void testValueParameterSemiColonWhitespaceParameter() {
        this.parseAndCheck("V1;p1=v1; p2=v2",
                this.token("V1", "p1", "v1", "p2", "v2"));
    }

    @Test
    public void testValueParametersSeparatorWhitespaceValueParametersSeparatorWhitespaceValueParameters() {
        this.parseAndCheck("V1;p1=v1, V2;p2=v2, V3;p3=v3",
                this.token("V1", "p1", "v1"),
                this.token("V2", "p2", "v2"),
                this.token("V3", "p3", "v3"));
    }

    @Test
    public void testSortedByQFactorWeight() {
        this.parseAndCheck("V1;q=0.5, V2;q=1.0, V3;q=0.25",
                this.token("V2", "q", 1.0f),
                this.token("V1", "q", 0.5f),
                this.token("V3", "q", 0.25f));
    }

    @Test
    public void testInvalidParameterValueFails() {
        this.parseFails("V;q=XYZ",
                "Failed to convert \"Q\" value \"XYZ\", message: For input string: \"XYZ\"");
    }

    private void parseAndCheck(final String headerValue, final HeaderValueToken... tokens) {
        assertEquals("Incorrect result parsing " + CharSequences.quote(headerValue),
                Lists.of(tokens),
                HeaderValueToken.parse(headerValue));
    }

    private HeaderValueToken token(final String value) {
        return HeaderValueToken.with(value, HeaderValueToken.NO_PARAMETERS);
    }

    private HeaderValueToken token(final String value,
                                   final String parameterName,
                                   final Object parameterValue) {
        return HeaderValueToken.with(value, this.parameters(parameterName, parameterValue));
    }

    private HeaderValueToken token(final String value,
                                   final String parameterName1,
                                   final Object parameterValue1,
                                   final String parameterName2,
                                   final Object parameterValue2) {
        return HeaderValueToken.with(value,
                this.parameters(parameterName1, parameterValue1, parameterName2, parameterValue2));
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

    @Override
    HeaderValueTokenHeaderParser createHeaderParser(final String text) {
        return new HeaderValueTokenHeaderParser(text);
    }

    @Override
    List<HeaderValueToken> parse(final String text) {
        return HeaderValueTokenHeaderParser.parseHeaderValueTokenList(text);
    }

    @Override
    protected Class<HeaderValueTokenHeaderParser> type() {
        return HeaderValueTokenHeaderParser.class;
    }
}
