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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.net.http.HttpHeaderName;

public final class MediaTypeParameterHeaderValueConverterTest extends
        HeaderValueConverterTestCase<MediaTypeParameterHeaderValueConverter, String> {

    private final static String TEXT = "abc123";

    @Override
    protected String requiredPrefix() {
        return MediaType.class.getSimpleName() + "Parameter";
    }

    @Test
    @Ignore
    public void testInvalidHeaderValueFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testValue() {
        this.parseAndToTextAndCheck(TEXT, TEXT);
    }

    @Test(expected = HeaderValueException.class)
    public void testParseUnquotedInvalidCharacterFail() {
        this.parse("abc,123");
    }

    @Test(expected = HeaderValueException.class)
    public void testParseUnquotedBackslashFail() {
        this.parse("abc\\123");
    }

    @Test(expected = HeaderValueException.class)
    public void testParseUnquotedDoubleQuoteFail() {
        this.parse("abc\"123");
    }

    @Test
    public void testParseUnquoted() {
        this.parseAndCheck("abc", "abc");
    }

    @Test
    public void testParseUnquoted2() {
        this.parseAndCheck("abc123", "abc123");
    }

    @Test(expected = HeaderValueException.class)
    public void testParseQuotedMissingClosingFail() {
        this.parse("\"abc");
    }

    @Test
    public void testParseQuoted() {
        this.parseAndCheck("\"abc\"", "abc");
    }

    @Test
    public void testParseQuoted2() {
        this.parseAndCheck("\"abc123\"", "abc123");
    }

    @Test
    public void testParseQuotedBackslash() {
        this.parseAndCheck("\"abc\\\\123\"", "abc\\123");
    }

    @Test
    public void testParseQuotedDoubleQuote() {
        this.parseAndCheck("\"abc\\\"123\"", "abc\"123");
    }

    @Test
    public void testToTextQuotesAdded() {
        this.toTextAndCheck("abc def", "\"abc def\"");
    }

    @Test
    public void testToTextIncludesBackslash() {
        this.toTextAndCheck("abc\\def", "\"abc\\\\def\"");
    }

    @Test
    public void testToTextIncludesBackslash2() {
        this.toTextAndCheck("abc\\def", "\"abc\\\\def\"");
    }

    @Test
    public void testToTextIncludesDoubleQuote() {
        this.toTextAndCheck("abc\"def", "\"abc\\\"def\"");
    }

    @Test
    public void testToTextIncludesDoubleQuote2() {
        this.toTextAndCheck("abc\"def", "\"abc\\\"def\"");
    }

    @Test
    public void testRoundtripUnquoted() {
        this.parseAndToTextAndCheck("abc", "abc");
    }

    @Test
    public void testRoundtripUnquoted2() {
        this.parseAndToTextAndCheck("abc123", "abc123");
    }

    @Test
    public void testRoundtripQuotedIncludesBackslash() {
        this.parseAndToTextAndCheck("\"abc\\\\123\"", "abc\\123");
    }

    @Test
    public void testRoundtripQuotedIncludesDoubleQuote() {
        this.parseAndToTextAndCheck("\"abc\\\"123\"", "abc\"123");
    }

    @Test
    public void testRoundtripQuotedIncludesSpecials() {
        this.parseAndToTextAndCheck("\"abc(),'123\"", "abc(),'123");
    }

    @Override
    protected MediaTypeParameterHeaderValueConverter converter() {
        return MediaTypeParameterHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<String> name() {
        return HttpHeaderName.SERVER;
    }

    @Override
    protected String invalidHeaderValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String value() {
        return TEXT;
    }

    @Override
    protected String converterToString() {
        return "MediaTypeParameter";
    }

    @Override
    protected Class<MediaTypeParameterHeaderValueConverter> type() {
        return MediaTypeParameterHeaderValueConverter.class;
    }
}
