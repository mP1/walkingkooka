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

import java.util.Map;

import static org.junit.Assert.assertEquals;

public abstract class TokenHeaderValueHeaderParserTestCase<P extends TokenHeaderValueHeaderParser, V> extends
        HeaderParserTestCase<P,
                TokenHeaderValueParameterName<?>,
                V> {

    TokenHeaderValueHeaderParserTestCase() {
        super();
    }

    @Test
    public final void testValue() {
        this.parseAndCheck2("A",
                this.token("A"));
    }

    @Test
    public final void testValueInvalidCharacterFails() {
        this.parseFails("A<");
    }

    @Test
    public final void testValueInvalidCharacterFails2() {
        this.parseFails("ABC<");
    }

    @Test
    public final void testValueEqualsFails() {
        this.parseFails("A;b=",
                "Missing parameter value at 3 in \"A;b=\"");
    }

    @Test
    public final void testValueSpaceEqualsFails() {
        this.parseFails("A;b =",
                "Missing parameter value at 4 in \"A;b =\"");
    }

    @Test
    public final void testValueTabEqualsFails() {
        this.parseFails("A;b =",
                "Missing parameter value at 4 in \"A;b =\"");
    }

    @Test
    public final void testValueSpaceTabSpaceTabEqualsFails() {
        this.parseFails("A;b \t \t=",
                "Missing parameter value at 7 in \"A;b \\t \\t=\"");
    }

    @Test
    public final void testValueEqualsSpaceFails() {
        this.parseFails("A;b= ",
                "Missing parameter value at 4 in \"A;b= \"");
    }

    @Test
    public final void testValueEqualsTabFails() {
        this.parseFails("A;b=\t",
                "Missing parameter value at 4 in \"A;b=\\t\"");
    }

    @Test
    public final void testValueParameterSeparatorEqualsParameterValue() {
        this.parseAndCheck2("A;b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testValueSemiColonSeparatorFails() {
        this.parseFails("A;,", ',');
    }

    @Test
    public final void testValue2() {
        this.parseAndCheck2("ABC",
                this.token("ABC"));
    }

    @Test
    public final void testValueSpace() {
        this.parseAndCheck2("A ",
                this.token("A"));
    }

    @Test
    public final void testValueTab() {
        this.parseAndCheck2("A\t",
                this.token("A"));
    }

    @Test
    public final void testValueSpaceTabSpaceTab() {
        this.parseAndCheck2("A \t \t",
                this.token("A"));
    }

    @Test
    public final void testValueParameterNameInvalidCharFails() {
        this.parseFails("A;b>=c",
                '>');
    }

    @Test
    public final void testValueParameterNameSpaceInvalidCharFails() {
        this.parseFails("A;b >=c",
                '>');
    }

    @Test
    public final void testValueParameterNameTabInvalidCharFails() {
        this.parseFails("A;b\t>=c",
                '>');
    }

    @Test
    public final void testValueParameterNameEqualsInvalidCharFails() {
        this.parseFails("A;b=\0c",
                '\0');
    }

    @Test
    public final void testValueParameterNameEqualsParameterValue() {
        this.parseAndCheck2("A;b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testValueParameterNameSpaceEqualsParameterValue() {
        this.parseAndCheck2("A;b =c",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testValueParameterNameTabEqualsParameterValue() {
        this.parseAndCheck2("A;b\t=c",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testValueParameterNameSpaceTabSpaceTabEqualsParameterValue() {
        this.parseAndCheck2("A;b \t \t=c",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testValueParameterNameEqualsSpaceParameterValue() {
        this.parseAndCheck2("A;b= c",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testValueParameterNameEqualsTabParameterValue() {
        this.parseAndCheck2("A;b=\tc",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testValueParameterNameEqualsSpaceTabSpaceTabParameterValue() {
        this.parseAndCheck2("A;b= \t \tc",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testValueParameterValueInvalidCharFails() {
        this.parseFails("A;b=c>",
                '>');
    }

    @Test
    public final void testValueParameterValueSpaceInvalidCharFails() {
        this.parseFails("A;b=c Q",
                'Q');
    }

    @Test
    public final void testValueParameterValueSpaceInvalidCharFails2() {
        this.parseFails("A;b=c >",
                '>');
    }

    @Test
    public final void testValueParameterSpace() {
        this.parseAndCheck2("A;bcd=123 ",
                this.token("A", "bcd", "123"));
    }

    @Test
    public final void testValueParameterTab() {
        this.parseAndCheck2("A;bcd=123\t",
                this.token("A", "bcd", "123"));
    }

    @Test
    public final void testValueParameterSpaceTabSpaceTab() {
        this.parseAndCheck2("A;bcd=123 \t \t",
                this.token("A", "bcd", "123"));
    }

    @Test
    public final void testValueSemiColonSpaceParameter() {
        this.parseAndCheck2("A; b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testValueSemiColonTabParameter() {
        this.parseAndCheck2("A;\tb=c",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testValueSpaceSemiColonParameter() {
        this.parseAndCheck2("A ;b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testValueSpaceSemiColonSpaceParameter() {
        this.parseAndCheck2("A ; b=c",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testValueTabSemiColonTabParameter() {
        this.parseAndCheck2("A\t;\tb=c",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testValueSpaceTabSpaceTabSemiColonSpaceTabParameter() {
        this.parseAndCheck2("A \t \t; \t \tb=c",
                this.token("A", "b", "c"));
    }

    @Test
    public final void testInvalidParameterValueFails() {
        this.parseFails("V;q=XYZ",
                "Failed to convert \"Q\" value \"XYZ\", message: For input string: \"XYZ\"");
    }

    abstract void parseAndCheck2(final String headerValue, final TokenHeaderValue token);

    final void parseAndCheck3(final String headerValue, final V expected) {
        assertEquals("Incorrect result parsing " + CharSequences.quote(headerValue),
                expected,
                this.parse(headerValue));
    }

    final TokenHeaderValue token(final String value) {
        return TokenHeaderValue.with(value);
    }

    final TokenHeaderValue token(final String value,
                                 final String parameterName,
                                 final Object parameterValue) {
        return TokenHeaderValue.with(value)
                .setParameters(this.parameters(parameterName, parameterValue));
    }

    final TokenHeaderValue token(final String value,
                                 final String parameterName1,
                                 final Object parameterValue1,
                                 final String parameterName2,
                                 final Object parameterValue2) {
        return TokenHeaderValue.with(value)
                .setParameters(this.parameters(parameterName1, parameterValue1, parameterName2, parameterValue2));
    }

    final Map<TokenHeaderValueParameterName<?>, Object> parameters(final String name,
                                                                   final Object value) {
        return this.parameters(TokenHeaderValueParameterName.with(name), value);
    }

    final Map<TokenHeaderValueParameterName<?>, Object> parameters(final TokenHeaderValueParameterName<?> name,
                                                                   final Object value) {
        return Maps.one(name, value);
    }

    final Map<TokenHeaderValueParameterName<?>, Object> parameters(final String name1,
                                                                   final Object value1,
                                                                   final String name2,
                                                                   final Object value2) {
        return this.parameters(TokenHeaderValueParameterName.with(name1),
                value1,
                TokenHeaderValueParameterName.with(name2),
                value2);
    }

    final Map<TokenHeaderValueParameterName<?>, Object> parameters(final TokenHeaderValueParameterName<?> name1,
                                                                   final Object value1,
                                                                   final TokenHeaderValueParameterName<?> name2,
                                                                   final Object value2) {
        final Map<TokenHeaderValueParameterName<?>, Object> parameters = Maps.ordered();
        parameters.put(name1, value1);
        parameters.put(name2, value2);
        return parameters;
    }

    @Override
    abstract P createHeaderParser(final String text);

    @Override
    abstract V parse(final String text);
}
