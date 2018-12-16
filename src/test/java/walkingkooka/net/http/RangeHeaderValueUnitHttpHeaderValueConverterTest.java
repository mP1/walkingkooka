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

public final class RangeHeaderValueUnitHttpHeaderValueConverterTest extends
        HttpHeaderValueConverterTestCase<RangeHeaderValueUnitHttpHeaderValueConverter, RangeHeaderValueUnit> {

    private final static String TEXT = "bytes";

    @Override
    protected String requiredPrefix() {
        return RangeHeaderValueUnit.class.getSimpleName();
    }

    @Test
    public void testParseRangeHeader() {
        this.parseAndCheck(TEXT, this.range());
    }

    @Test
    public void testToTextContentRange() {
        this.toTextAndCheck(this.range(), TEXT);
    }

    private RangeHeaderValueUnit range() {
        return RangeHeaderValueUnit.BYTES;
    }

    @Override
    protected RangeHeaderValueUnitHttpHeaderValueConverter converter() {
        return RangeHeaderValueUnitHttpHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<RangeHeaderValueUnit> name() {
        return HttpHeaderName.ACCEPT_RANGES;
    }

    @Override
    protected String invalidHeaderValue() {
        return "http://example.com";
    }

    @Override
    protected RangeHeaderValueUnit value() {
        return RangeHeaderValueUnit.parse(TEXT);
    }

    @Override
    protected String converterToString() {
        return RangeHeaderValueUnit.class.getSimpleName();
    }

    @Override
    protected Class<RangeHeaderValueUnitHttpHeaderValueConverter> type() {
        return RangeHeaderValueUnitHttpHeaderValueConverter.class;
    }
}
