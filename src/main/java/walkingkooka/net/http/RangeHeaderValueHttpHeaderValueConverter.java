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

import walkingkooka.compare.Range;
import walkingkooka.naming.Name;

import java.util.List;

/**
 * A {@link HttpHeaderValueConverter} that parses a header value into a {@link List<Range<Long>>}.
 * This is useful for headers such as {@link HttpHeaderName#RANGE}.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Range"></a>
 * <pre>
 * Range: <unit>=<range-start>-
 * Range: <unit>=<range-start>-<range-end>
 * Range: <unit>=<range-start>-<range-end>, <range-start>-<range-end>
 * Range: <unit>=<range-start>-<range-end>, <range-start>-<range-end>, <range-start>-<range-end>
 * Range: bytes=200-1000, 2000-6576, 19000-
 * </pre>
 */
final class RangeHeaderValueHttpHeaderValueConverter extends HttpHeaderValueConverter<RangeHeaderValue> {

    /**
     * Singleton
     */
    final static RangeHeaderValueHttpHeaderValueConverter INSTANCE = new RangeHeaderValueHttpHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private RangeHeaderValueHttpHeaderValueConverter() {
        super();
    }

    @Override
    RangeHeaderValue parse0(final String value, final Name name) {
        return RangeHeaderValue.parse(value);
    }

    @Override
    void check0(final Object value) {
        this.checkType(value, RangeHeaderValue.class);
    }

    @Override
    String toText0(final RangeHeaderValue value, final Name name) {
        return value.toString();
    }

    @Override
    public String toString() {
        return this.toStringType(RangeHeaderValue.class);
    }
}
