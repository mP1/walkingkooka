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
import walkingkooka.collect.list.Lists;
import walkingkooka.compare.Range;

public final class HttpHeaderRangeHttpHeaderValueConverterTest extends
        HttpHeaderValueConverterTestCase<HttpHeaderRangeHttpHeaderValueConverter, HttpHeaderRange> {

    private final static String TEXT = "bytes=123-456, 789-";

    @Override
    protected String requiredPrefix() {
        return HttpHeaderRange.class.getSimpleName();
    }

    @Test
    public void testRangeHeader() {
        this.parseAndCheck(TEXT, this.range());
    }

    @Test
    public void testContentRangeFormat() {
        this.formatAndCheck(this.range(), TEXT);
    }

    private HttpHeaderRange range() {
        return HttpHeaderRange.with("bytes",
                Lists.of(Range.greaterThanEquals(123L).and(Range.lessThanEquals(456L)),
                        Range.greaterThanEquals(789L)));
    }

    @Override
    protected HttpHeaderRangeHttpHeaderValueConverter converter() {
        return HttpHeaderRangeHttpHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<HttpHeaderRange> name() {
        return HttpHeaderName.RANGE;
    }

    @Override
    protected String invalidHeaderValue() {
        return "http://example.com";
    }

    @Override
    protected HttpHeaderRange value() {
        return HttpHeaderRange.parse(TEXT);
    }

    @Override
    protected String converterToString() {
        return HttpHeaderRange.class.getSimpleName();
    }

    @Override
    protected Class<HttpHeaderRangeHttpHeaderValueConverter> type() {
        return HttpHeaderRangeHttpHeaderValueConverter.class;
    }
}
