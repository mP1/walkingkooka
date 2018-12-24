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
import walkingkooka.Cast;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

import static org.junit.Assert.assertEquals;

public final class HeaderParserWithParametersTest extends HeaderParserWithParametersTestCase<HeaderParserWithParameters<?, ?>,
        Void> {

    @Test
    public void testNaming() {
        this.checkNaming(HeaderParserWithParameters.class);
    }

    @Test
    public void testParseQuotedFails() {
        this.parseInvalidCharacterFails("\"quoted\"", '"');
    }

    @Test
    public void testParseParameterSeparatorFails() {
        this.parseMissingValueFails(";");
    }

    @Test
    public void testParseValueSeparatorFails() {
        this.parseInvalidCharacterFails(",");
    }

    @Test
    public void testParseKeyValueSeparatorFails() {
        this.parseInvalidCharacterFails("=");
    }

    @Test
    public void testParseValue() {
        this.parseAndCheck("v", "[value-v]v");
    }

    @Test
    public void testParseWildcard() {
        this.parseAndCheck("*", "[wildcard]*");
    }

    @Test
    public void testParseSlash() {
        this.parseInvalidCharacterFails("/");
    }

    @Test
    public void testParseValueSpace() {
        this.parseAndCheck("v ", "[value-v]v");
    }

    @Test
    public void testParseValueTab() {
        this.parseAndCheck("v\t", "[value-v]v");
    }

    @Test
    public void testParseValueCrNlSpace() {
        this.parseAndCheck("v\r\n ", "[value-v]v");
    }

    @Test
    public void testParseValueCrNlTab() {
        this.parseAndCheck("v\r\n\t", "[value-v]v");
    }

    @Test
    public void testParseValueCrFails() {
        this.parseInvalidCharacterFails("v\r");
    }

    @Test
    public void testParseValueCrNlFails() {
        this.parseInvalidCharacterFails("v\r\n");
    }

    @Test
    public void testParseValueCrNlNonWhitespaceFails() {
        this.parseInvalidCharacterFails("v\r\n!");
    }

    @Test
    public void testParseValueParameterSeparator() {
        this.parseAndCheck("v;", "[value-v]v");
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameFails() {
        this.parseMissingParameterValueFails("v;p");
    }

    @Test
    public void testParseValueParameterSeparatorParameterInvalidCharacterFails() {
        this.parseInvalidCharacterFails("v;p\0");
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorFails() {
        this.parseMissingParameterValueFails("v;p=");
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorSpaceFails() {
        this.parseMissingParameterValueFails("v;p= ");
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorTabFails() {
        this.parseMissingParameterValueFails("v;p=\t");
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorCrFails() {
        this.parseInvalidCharacterFails("v;p=\r");
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorCrNlFails() {
        this.parseInvalidCharacterFails("v;p=\r\n");
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorCrNlNonSpaceFails() {
        this.parseInvalidCharacterFails("v;p=\r\nq");
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorUnquotedParameterValue() {
        this.parseAndCheck("v;p=u", "[value-v][parameter-name-p][parameter-value-unquoted-u]v; p=u");
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorUnquotedParameterValueParameterSeparator() {
        this.parseAndCheck("v;p=u;", "[value-v][parameter-name-p][parameter-value-unquoted-u]v; p=u");
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorUnquotedParameterValueValueSeparatorFails() {
        this.parseInvalidCharacterFails("v;p=u,");
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorQuotedParameterValueNonAscii() {
        this.parseInvalidCharacterFails("v;p=\"\0\"", '\0');
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorQuotedParameterValueNonAscii2() {
        this.parseInvalidCharacterFails("v;p=\"\u0080\"", '\u0080');
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorQuotedParameterValueMissing() {
        this.parseMissingClosingQuoteFails("v;p=\"q");
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorQuotedParameterValue() {
        this.parseAndCheck("v;p=\"q\"", "[value-v][parameter-name-p][parameter-value-quoted-q]v; p=q");
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorQuotedParameterValueQuotedParameterValueFails() {
        final String text = "v;p=\"q\"\"r\"";
        this.parseInvalidCharacterFails(text, text.indexOf('r') - 1);
    }

    @Test
    public void testParseValueParameterSeparatorParameterNameKeyValueSeparatorParameterValueParameterSeparatorParameterNameKeyValueSeparatorParameterValue() {
        this.parseAndCheck("v;p=\"q\";r=\"s\"",
                "[value-v][parameter-name-p][parameter-value-quoted-q][parameter-name-r][parameter-value-quoted-s]v; p=q; r=s");
    }

    @Test
    public void testParseValueValueSeparatorValue() {
        this.parseAndCheck("v,w",
                "[value-v]v[value-w]w");
    }

    @Test
    public void testParseValueValueSeparatorValueValueSeparatorValue() {
        this.parseAndCheck("v,w,x",
                "[value-v]v[value-w]w[value-x]x");
    }

    @Test
    public void testParseValueParametersValueSeparatorValueValueSeparatorValue() {
        this.parseAndCheck("v;p=q,w,x",
                "[value-v][parameter-name-p][parameter-value-unquoted-q]v; p=q[value-w]w[value-x]x");
    }

    @Test
    public void testParseWildcardValueSeparatorValue() {
        this.parseAndCheck("*,v",
                "[wildcard]*[value-v]v");
    }

    @Test
    public void testParseWildcardValueSeparatorParameterNameKeyValueSeparatorParameterValue() {
        this.parseAndCheck("*;p=q",
                "[wildcard][parameter-name-p][parameter-value-unquoted-q]*; p=q");
    }

    private void parseAndCheck(final String text,
                               final String events) {
        final StringBuilder recorded = new StringBuilder();

        new HeaderParserWithParameters<CharsetHeaderValue, CharsetHeaderValueParameterName<?>>(text) {

            @Override
            boolean allowMultipleValues() {
                return true;
            }

            @Override
            CharsetHeaderValue wildcardValue() {
                recorded.append("[wildcard]");
                this.position++;
                return CharsetHeaderValue.WILDCARD_VALUE;
            }

            @Override
            CharsetHeaderValue value() {
                final String text = this.token(CharPredicates.letterOrDigit());
                recorded.append("[value-" + text + "]");
                return CharsetHeaderValue.with(CharsetName.with(text));
            }

            @Override
            CharsetHeaderValueParameterName<?> parameterName() {
                final String text = this.token(CharPredicates.letterOrDigit());
                recorded.append("[parameter-name-" + text + "]");
                return CharsetHeaderValueParameterName.with(text);
            }

            @Override
            String quotedParameterValue(CharsetHeaderValueParameterName<?> parameterName) {
                final String text = this.quotedText(CharPredicates.letterOrDigit(), false);
                recorded.append("[parameter-value-quoted-" + text.substring(1, text.length() - 1) + ']');
                return text;
            }

            @Override
            String unquotedParameterValue(CharsetHeaderValueParameterName<?> parameterName) {
                final String text = this.token(CharPredicates.letterOrDigit());
                recorded.append("[parameter-value-unquoted-" + text + ']');
                return text;
            }

            @Override
            void valueComplete(final CharsetHeaderValue value) {
                recorded.append(value.toString());
            }

            @Override
            void missingValue() {
                this.failMissingValue(VALUE_LABEL);
            }
        }.parse();

        assertEquals("recorded events for " + CharSequences.quoteAndEscape(text),
                events,
                recorded.toString());
    }

    @Override
    Void parse(final String text) {
        new HeaderParserWithParameters<CharsetHeaderValue, CharsetHeaderValueParameterName<?>>(text) {

            @Override
            boolean allowMultipleValues() {
                return false;
            }

            @Override
            CharsetHeaderValue wildcardValue() {
                return this.token(CharPredicates.letterOrDigit(), s -> CharsetHeaderValue.with(CharsetName.with(s)));
            }

            @Override
            CharsetHeaderValue value() {
                return this.token(CharPredicates.letterOrDigit(), s -> CharsetHeaderValue.with(CharsetName.with(s)));
            }

            @Override
            CharsetHeaderValueParameterName<?> parameterName() {
                return CharsetHeaderValueParameterName.with(this.token(CharPredicates.letterOrDigit()));
            }

            @Override
            String quotedParameterValue(CharsetHeaderValueParameterName<?> parameterName) {
                return this.quotedText(CharPredicates.letterOrDigit(), false);
            }

            @Override
            String unquotedParameterValue(CharsetHeaderValueParameterName<?> parameterName) {
                return this.token(CharPredicates.letterOrDigit());
            }

            @Override
            void valueComplete(final CharsetHeaderValue value) {

            }

            @Override
            void missingValue() {
                this.failMissingValue(VALUE_LABEL);
            }
        }.parse();
        return null;
    }

    @Override
    String valueLabel() {
        return VALUE_LABEL;
    }

    private final static String VALUE_LABEL = "Value";

    @Override
    protected Class<HeaderParserWithParameters<?, ?>> type() {
        return Cast.to(HeaderParserWithParameters.class);
    }
}
