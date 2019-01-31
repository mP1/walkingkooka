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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TokenHeaderValueListHeaderParserTest extends TokenHeaderValueHeaderParserTestCase<TokenHeaderValueListHeaderParser,
        List<TokenHeaderValue>> {
    @Test
    public final void testParameterSeparatorFails() {
        this.parseMissingValueFails(";", 0);
    }

    @Test
    public final void testValueValueSeparatorFails() {
        this.parseInvalidCharacterFails("A;,", 2);
    }

    @Test
    public void testValueParameterSeparatorValue() {
        this.parseAndCheck3("A;b=c,D",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testValueParameterSeparatorSpaceValue() {
        this.parseAndCheck3("A;b=c, D",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testValueParameterSeparatorTabValue() {
        this.parseAndCheck3("A;b=c,\tD",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testValueParameterSeparatorSpaceTabSpaceTabValue() {
        this.parseAndCheck3("A;b=c, \t \tD",
                this.token("A", "b", "c"),
                this.token("D"));
    }

    @Test
    public void testValueParameterSeparatorValueSpaceValue() {
        this.parseAndCheck3("A;b=c, D;e=f",
                this.token("A", "b", "c"),
                this.token("D", "e", "f"));
    }

    @Test
    public void testValueParameterSeparatorValueTabValue() {
        this.parseAndCheck3("A;b=c,\tD;e=f",
                this.token("A", "b", "c"),
                this.token("D", "e", "f"));
    }

    @Test
    public void testValueSeparatorValue() {
        this.parseAndCheck3("A,B",
                this.token("A"),
                this.token("B"));
    }

    @Test
    public void testValueSeparatorValue2() {
        this.parseAndCheck3("ABC,DEF",
                this.token("ABC"),
                this.token("DEF"));
    }

    @Test
    public void testValueSeparatorValueSeparatorValue() {
        this.parseAndCheck3("A,B,CONSTANTS",
                this.token("A"),
                this.token("B"),
                this.token("CONSTANTS"));
    }

    @Test
    public void testValueSpaceSeparatorValueSpaceSeparatorValueSpace() {
        this.parseAndCheck3("A ,B ,CONSTANTS ",
                this.token("A"),
                this.token("B"),
                this.token("CONSTANTS"));
    }

    @Test
    public void testValueTabSeparatorValueTabSeparatorValueTab() {
        this.parseAndCheck3("A\t,B\t,CONSTANTS\t",
                this.token("A"),
                this.token("B"),
                this.token("CONSTANTS"));
    }

    @Test
    public void testValueSpaceTabSpaceTabSeparatorValueSpaceTabSeparatorValueSpaceTab() {
        this.parseAndCheck3("A \t \t,B \t \t,CONSTANTS \t \t",
                this.token("A"),
                this.token("B"),
                this.token("CONSTANTS"));
    }

    @Test
    public void testValueSeparatorSpaceValueSeparatorSpaceValue() {
        this.parseAndCheck3("A, B, CONSTANTS",
                this.token("A"),
                this.token("B"),
                this.token("CONSTANTS"));
    }

    @Test
    public void testValueSeparatorTabValueSeparatorTabValue() {
        this.parseAndCheck3("A,\tB,\tCONSTANTS",
                this.token("A"),
                this.token("B"),
                this.token("CONSTANTS"));
    }

    @Test
    public void testValueSeparatorSpaceTabSpaceTabValueSeparatorSpaceTabValue() {
        this.parseAndCheck3("A, \t \tB, \t \tCONSTANTS",
                this.token("A"),
                this.token("B"),
                this.token("CONSTANTS"));
    }

    @Test
    public void testValueParameters() {
        this.parseAndCheck2("V1;p1=v1;p2=v2",
                this.token("V1", "p1", "v1", "p2", "v2"));
    }

    @Test
    public void testValueParametersSeparatorValueParametersSeparatorValueParameters() {
        this.parseAndCheck3("V1;p1=v1,V2;p2=v2,V3;p3=v3",
                this.token("V1", "p1", "v1"),
                this.token("V2", "p2", "v2"),
                this.token("V3", "p3", "v3"));
    }

    @Test
    public void testValueParametersWhitespaceSeparatorValueParametersWhitespaceSeparatorValueParametersWhitespace() {
        this.parseAndCheck3("V1;p1=v1 ,V2;p2=v2 ,V3;p3=v3 ",
                this.token("V1", "p1", "v1"),
                this.token("V2", "p2", "v2"),
                this.token("V3", "p3", "v3"));
    }

    @Test
    public void testValueParameterWhitespaceSemiParameter() {
        this.parseAndCheck2("V1;p1=v1 ;p2=v2",
                this.token("V1", "p1", "v1", "p2", "v2"));
    }

    @Test
    public void testValueParameterSemiColonWhitespaceParameter() {
        this.parseAndCheck2("V1;p1=v1; p2=v2",
                this.token("V1", "p1", "v1", "p2", "v2"));
    }

    @Test
    public void testValueParametersSeparatorWhitespaceValueParametersSeparatorWhitespaceValueParameters() {
        this.parseAndCheck3("V1;p1=v1, V2;p2=v2, V3;p3=v3",
                this.token("V1", "p1", "v1"),
                this.token("V2", "p2", "v2"),
                this.token("V3", "p3", "v3"));
    }

    @Test
    public void testSortedByQFactorWeight() {
        this.parseAndCheck3("V1;q=0.5, V2;q=1.0, V3;q=0.25",
                this.token("V2", "q", 1.0f),
                this.token("V1", "q", 0.5f),
                this.token("V3", "q", 0.25f));
    }

    @Override
    void parseAndCheck2(final String headerValue, final TokenHeaderValue token) {
        this.parseAndCheck3(headerValue, token);
    }

    private void parseAndCheck3(final String headerValue, final TokenHeaderValue... tokens) {
        assertEquals(Lists.of(tokens),
                TokenHeaderValue.parseList(headerValue),
                "Incorrect result parsing " + CharSequences.quote(headerValue));
    }

    @Override
    List<TokenHeaderValue> parse(final String text) {
        return TokenHeaderValueListHeaderParser.parseTokenHeaderValueList(text);
    }

    @Override
    protected Class<TokenHeaderValueListHeaderParser> type() {
        return TokenHeaderValueListHeaderParser.class;
    }
}
