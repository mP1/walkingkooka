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

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public final class HeaderValueTokenTest extends HeaderValueTestCase<HeaderValueToken> {

    private final static String VALUE = "abc";
    private final static String PARAMETER_VALUE = "v1";

    // with.........................................................................................

    @Test(expected = NullPointerException.class)
    public void testWithNullValueFails() {
        HeaderValueToken.with(null, HeaderValueToken.NO_PARAMETERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyValueFails() {
        HeaderValueToken.with("", HeaderValueToken.NO_PARAMETERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidCharactersValueFails() {
        HeaderValueToken.with("<", HeaderValueToken.NO_PARAMETERS);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullParametersFails() {
        HeaderValueToken.with(VALUE, null);
    }

    @Test(expected = HeaderValueException.class)
    public void testWithInvalidParametersValueFails() {
        HeaderValueToken.with(VALUE,
                this.parameters("Q", "INVALID!"));
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

    @Test(expected = NullPointerException.class)
    public void testSetParametersNullFails() {
        this.token().setParameters(null);
    }

    @Test(expected = HeaderValueException.class)
    public void testSetParametersInvalidParameterValueFails() {
        this.token().setParameters(this.parameters("Q", "INVALID!"));
    }

    @Test
    public void testSetParametersSame() {
        final HeaderValueToken token = this.token();
        assertSame(token, token.setParameters(this.parameters()));
    }

    @Test
    public void testSetParametersDifferent() {
        final HeaderValueToken token = this.token();
        final Map<HeaderValueTokenParameterName<?>, Object> parameters = this.parameters("different", "2");
        this.check(token.setParameters(parameters), VALUE, parameters);
        this.check(token);
    }

    // parse ...................................................................................................

    @Test(expected = NullPointerException.class)
    public void testParseNullFails() {
        HeaderValueToken.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyFails() {
        HeaderValueToken.parse("");
    }

    @Test
    public void testParseValue() {
        this.parseAndCheck("A", this.token("A"));
    }

    @Test
    public void testParseValueInvalidCharacterFails() {
        this.parseFails("A<");
    }

    @Test
    public void testParseValueInvalidCharacterFails2() {
        this.parseFails("ABC<");
    }

    @Test
    public void testParseValueEqualsFails() {
        this.parseFails("A;b=",
                "Missing parameter value at 3 in \"A;b=\"");
    }

    @Test
    public void testParseValueSpaceEqualsFails() {
        this.parseFails("A;b =",
                "Missing parameter value at 4 in \"A;b =\"");
    }

    @Test
    public void testParseValueTabEqualsFails() {
        this.parseFails("A;b =",
                "Missing parameter value at 4 in \"A;b =\"");
    }

    @Test
    public void testParseValueSpaceTabSpaceTabEqualsFails() {
        this.parseFails("A;b \t \t=",
                "Missing parameter value at 7 in \"A;b \\t \\t=\"");
    }

    @Test
    public void testParseValueEqualsSpaceFails() {
        this.parseFails("A;b= ",
                "Missing parameter value at 4 in \"A;b= \"");
    }

    @Test
    public void testParseValueEqualsTabFails() {
        this.parseFails("A;b=\t",
                "Missing parameter value at 4 in \"A;b=\\t\"");
    }

    @Test
    public void testParseValueSeparatorEquals() {
        this.parseFails("A;b=c,", "Missing value at 6 in \"A;b=c,\"");
    }

    @Test
    public void testParseValueSemiColonSeparatorFails() {
        this.parseFails("A;,", "Missing value at 2 in \"A;,\"");
    }

    @Test
    public void testParseValue2() {
        this.parseAndCheck("ABC", this.token("ABC"));
    }

    @Test
    public void testParseValueSpace() {
        this.parseAndCheck("A ", this.token("A"));
    }

    @Test
    public void testParseValueTab() {
        this.parseAndCheck("A\t", this.token("A"));
    }

    @Test
    public void testParseValueSpaceTabSpaceTab() {
        this.parseAndCheck("A \t \t", this.token("A"));
    }

    @Test
    public void testParseValueParameterNameInvalidCharFails() {
        this.parseFails("A;b>=c", '>');
    }

    @Test
    public void testParseValueParameterNameSpaceInvalidCharFails() {
        this.parseFails("A;b >=c", '>');
    }

    @Test
    public void testParseValueParameterNameTabInvalidCharFails() {
        this.parseFails("A;b\t>=c", '>');
    }

    @Test
    public void testParseValueParameterNameEqualsInvalidCharFails() {
        this.parseFails("A;b=\0c", '\0');
    }

    @Test
    public void testParseValueParameterNameEqualsParameterValue() {
        this.parseAndCheck("A;b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueParameterNameSpaceEqualsParameterValue() {
        this.parseAndCheck("A;b =c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueParameterNameTabEqualsParameterValue() {
        this.parseAndCheck("A;b\t=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueParameterNameSpaceTabSpaceTabEqualsParameterValue() {
        this.parseAndCheck("A;b \t \t=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueParameterNameEqualsSpaceParameterValue() {
        this.parseAndCheck("A;b= c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueParameterNameEqualsTabParameterValue() {
        this.parseAndCheck("A;b=\tc",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueParameterNameEqualsSpaceTabSpaceTabParameterValue() {
        this.parseAndCheck("A;b= \t \tc",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueParameterValueInvalidCharFails() {
        this.parseFails("A;b=c>", '>');
    }

    @Test
    public void testParseValueParameterValueSpaceInvalidCharFails() {
        this.parseFails("A;b=c Q", 'Q');
    }

    @Test
    public void testParseValueParameterValueSpaceInvalidCharFails2() {
        this.parseFails("A;b=c >", '>');
    }

    @Test
    public void testParseValueParameterSpace() {
        this.parseAndCheck("A;bcd=123 ",
                HeaderValueToken.with("A", this.parameters("bcd", "123")));
    }

    @Test
    public void testParseValueParameterTab() {
        this.parseAndCheck("A;bcd=123\t",
                HeaderValueToken.with("A", this.parameters("bcd", "123")));
    }

    @Test
    public void testParseValueParameterSpaceTabSpaceTab() {
        this.parseAndCheck("A;bcd=123 \t \t",
                HeaderValueToken.with("A", this.parameters("bcd", "123")));
    }

    @Test
    public void testParseValueSemiColonSpaceParameter() {
        this.parseAndCheck("A; b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueSemiColonTabParameter() {
        this.parseAndCheck("A;\tb=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueSpaceSemiColonParameter() {
        this.parseAndCheck("A ;b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueSpaceSemiColonSpaceParameter() {
        this.parseAndCheck("A ; b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueTabSemiColonTabParameter() {
        this.parseAndCheck("A\t;\tb=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueSpaceTabSpaceTabSemiColonSpaceTabParameter() {
        this.parseAndCheck("A \t \t; \t \tb=c",
                this.token("A", "b", "c"));
    }

    @Test
    public void testParseValueParameterSeparatorValue() {
        this.parseAndCheck("A;b=c,D",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testParseValueParameterSeparatorSpaceValue() {
        this.parseAndCheck("A;b=c, D",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testParseValueParameterSeparatorTabValue() {
        this.parseAndCheck("A;b=c,\tD",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testParseValueParameterSeparatorSpaceTabSpaceTabValue() {
        this.parseAndCheck("A;b=c, \t \tD",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testParseValueParameterSeparatorValueSpaceValue() {
        this.parseAndCheck("A;b=c, D;e=f",
                this.token("A", "b", "c"),
                this.token("D", "e", "f"));
    }

    @Test
    public void testParseValueParameterSeparatorValueTabValue() {
        this.parseAndCheck("A;b=c,\tD;e=f",
                this.token("A", "b", "c"),
                this.token("D", "e", "f"));
    }

    @Test
    public void testParseValueSeparatorValue() {
        this.parseAndCheck("A,B",
                this.token("A"),
                this.token("B"));
    }

    @Test
    public void testParseValueSeparatorValue2() {
        this.parseAndCheck("ABC,DEF",
                this.token("ABC"),
                this.token("DEF"));
    }

    @Test
    public void testParseValueSeparatorValueSeparatorValue() {
        this.parseAndCheck("A,B,C",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testParseValueSpaceSeparatorValueSpaceSeparatorValueSpace() {
        this.parseAndCheck("A ,B ,C ",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testParseValueTabSeparatorValueTabSeparatorValueTab() {
        this.parseAndCheck("A\t,B\t,C\t",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testParseValueSpaceTabSpaceTabSeparatorValueSpaceTabSeparatorValueSpaceTab() {
        this.parseAndCheck("A \t \t,B \t \t,C \t \t",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testParseValueSeparatorSpaceValueSeparatorSpaceValue() {
        this.parseAndCheck("A, B, C",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testParseValueSeparatorTabValueSeparatorTabValue() {
        this.parseAndCheck("A,\tB,\tC",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testParseValueSeparatorSpaceTabSpaceTabValueSeparatorSpaceTabValue() {
        this.parseAndCheck("A, \t \tB, \t \tC",
                this.token("A"),
                this.token("B"),
                this.token("C"));
    }

    @Test
    public void testParseValueParameters() {
        this.parseAndCheck("V1;p1=v1;p2=v2",
                this.token("V1", "p1", "v1", "p2", "v2"));
    }

    @Test
    public void testParseValueParametersSeparatorValueParametersSeparatorValueParameters() {
        this.parseAndCheck("V1;p1=v1,V2;p2=v2,V3;p3=v3",
                this.token("V1", "p1", "v1"),
                this.token("V2", "p2", "v2"),
                this.token("V3", "p3", "v3"));
    }

    @Test
    public void testParseValueParametersWhitespaceSeparatorValueParametersWhitespaceSeparatorValueParametersWhitespace() {
        this.parseAndCheck("V1;p1=v1 ,V2;p2=v2 ,V3;p3=v3 ",
                this.token("V1", "p1", "v1"),
                this.token("V2", "p2", "v2"),
                this.token("V3", "p3", "v3"));
    }

    @Test
    public void testParseValueParameterWhitespaceSemiParameter() {
        this.parseAndCheck("V1;p1=v1 ;p2=v2",
                this.token("V1", "p1", "v1", "p2", "v2"));
    }

    @Test
    public void testParseValueParameterSemiColonWhitespaceParameter() {
        this.parseAndCheck("V1;p1=v1; p2=v2",
                this.token("V1", "p1", "v1", "p2", "v2"));
    }

    @Test
    public void testParseValueParametersSeparatorWhitespaceValueParametersSeparatorWhitespaceValueParameters() {
        this.parseAndCheck("V1;p1=v1, V2;p2=v2, V3;p3=v3",
                this.token("V1", "p1", "v1"),
                this.token("V2", "p2", "v2"),
                this.token("V3", "p3", "v3"));
    }

    @Test
    public void testParseSortedByQFactorWeight() {
        this.parseAndCheck("V1;q=0.5, V2;q=1.0, V3;q=0.25",
                this.token("V2", "q", 1.0f),
                this.token("V1", "q", 0.5f),
                this.token("V3", "q", 0.25f));
    }

    @Test
    public void testParseInvalidParameterValueFails() {
        this.parseFails("V;q=XYZ",
                "Failed to convert \"Q\" value \"XYZ\", message: For input string: \"XYZ\"");
    }

    private void parseAndCheck(final String headerValue, final HeaderValueToken... tokens) {
        assertEquals("Incorrect result parsing " + CharSequences.quote(headerValue),
                Lists.of(tokens),
                HeaderValueToken.parse(headerValue));
    }

    final void parseFails(final String text) {
        this.parseFails(text, text.length() - 1);
    }

    final void parseFails(final String text, final char pos) {
        this.parseFails(text, text.indexOf(pos));
    }

    final void parseFails(final String text, final int pos) {
        parseFails(text, HeaderValueToken.invalidCharacter(pos, text));
    }

    final void parseFails(final String text, final String message) {
        try {
            HeaderValueToken.parse(text);
            fail();
        } catch (final HeaderValueException expected) {
            assertEquals("Incorrect failure message", message, expected.getMessage());
        }
    }

    // toString ...........................................................................................

    @Test
    public void testToStringNoParameters() {
        this.toStringAndCheck(HeaderValueToken.with(VALUE, HeaderValueToken.NO_PARAMETERS), "abc");
    }

    @Test
    public void testToStringWithParameters() {
        this.toStringAndCheck(this.token(), "abc; p1=v1");
    }

    @Test
    public void testToStringWithSeveralParameters() {
        this.toStringAndCheck(HeaderValueToken.with(VALUE, this.parameters("p1", "v1", "p2", "v2")), "abc; p1=v1; p2=v2");
    }

    private void toStringAndCheck(final HeaderValueToken token, final String toString) {
        assertEquals(toString, token.toString());
    }

    // format ...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testFormatNullFails() {
        HeaderValueToken.format(null);
    }

    @Test
    public void testFormatOne() {
        this.formatAndCheck("A",
                HeaderValueToken.with("A", HeaderValueToken.NO_PARAMETERS));
    }

    @Test
    public void testFormatOneWithParameters() {
        this.formatAndCheck("A; p1=v1",
                HeaderValueToken.with("A", this.parameters()));
    }

    @Test
    public void testFormatMany() {
        this.formatAndCheck("A, B",
                HeaderValueToken.with("A", HeaderValueToken.NO_PARAMETERS),
                HeaderValueToken.with("B", HeaderValueToken.NO_PARAMETERS));
    }

    private void formatAndCheck(final String toString, final HeaderValueToken...tokens) {
        assertEquals("Format of " + Arrays.toString(tokens),
                toString,
                HeaderValueToken.format(Lists.of(tokens)));
    }

    // helpers ...........................................................................................

    private HeaderValueToken token() {
        return HeaderValueToken.with(VALUE, this.parameters());
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
