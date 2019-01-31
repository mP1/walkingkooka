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
import walkingkooka.compare.Range;

public final class RangeHeaderValueHeaderValueConverterTest extends
        HeaderValueConverterTestCase<RangeHeaderValueHeaderValueConverter, RangeHeaderValue> {

    private final static String TEXT = "bytes=123-456, 789-";

    @Override
    protected String requiredPrefix() {
        return RangeHeaderValue.class.getSimpleName();
    }

    @Test
    public void testParseRangeHeader() {
        this.parseAndCheck(TEXT, this.range());
    }

    @Test
    public void testToTextContentRange() {
        this.toTextAndCheck(this.range(), TEXT);
    }

    private RangeHeaderValue range() {
        return RangeHeaderValue.with(RangeHeaderValueUnit.BYTES,
                Lists.of(Range.greaterThanEquals(123L).and(Range.lessThanEquals(456L)),
                        Range.greaterThanEquals(789L)));
    }

    @Override
    RangeHeaderValueHeaderValueConverter converter() {
        return RangeHeaderValueHeaderValueConverter.INSTANCE;
    }

    @Override
    HttpHeaderName<RangeHeaderValue> name() {
        return HttpHeaderName.RANGE;
    }

    @Override
    String invalidHeaderValue() {
        return "http://example.com";
    }

    @Override
    RangeHeaderValue value() {
        return RangeHeaderValue.parse(TEXT);
    }

    @Override
    String valueType() {
        return this.valueType(RangeHeaderValue.class);
    }

    @Override
    String converterToString() {
        return RangeHeaderValue.class.getSimpleName();
    }

    @Override
    protected Class<RangeHeaderValueHeaderValueConverter> type() {
        return RangeHeaderValueHeaderValueConverter.class;
    }
}
