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

package walkingkooka.net.http;

import org.junit.Test;

public final class HttpHeaderRangeUnitHttpHeaderValueConverterTest extends
        HttpHeaderValueConverterTestCase<HttpHeaderRangeUnitHttpHeaderValueConverter, HttpHeaderRangeUnit> {

    private final static String TEXT = "bytes";

    @Override
    protected String requiredPrefix() {
        return HttpHeaderRangeUnit.class.getSimpleName();
    }

    @Test
    public void testParseRangeHeader() {
        this.parseAndCheck(TEXT, this.range());
    }

    @Test
    public void testToTextContentRange() {
        this.toTextAndCheck(this.range(), TEXT);
    }

    private HttpHeaderRangeUnit range() {
        return HttpHeaderRangeUnit.BYTES;
    }

    @Override
    protected HttpHeaderRangeUnitHttpHeaderValueConverter converter() {
        return HttpHeaderRangeUnitHttpHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<HttpHeaderRangeUnit> name() {
        return HttpHeaderName.ACCEPT_RANGES;
    }

    @Override
    protected String invalidHeaderValue() {
        return "http://example.com";
    }

    @Override
    protected HttpHeaderRangeUnit value() {
        return HttpHeaderRangeUnit.parse(TEXT);
    }

    @Override
    protected String converterToString() {
        return HttpHeaderRangeUnit.class.getSimpleName();
    }

    @Override
    protected Class<HttpHeaderRangeUnitHttpHeaderValueConverter> type() {
        return HttpHeaderRangeUnitHttpHeaderValueConverter.class;
    }
}
