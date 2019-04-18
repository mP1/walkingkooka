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

import walkingkooka.naming.Name;

/**
 * A {@link HeaderValueConverter} that parses a header value into a {@link &lt;Range&lt;Long&gt;&gt;}.
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
final class RangeHeaderValueHeaderValueConverter extends HeaderValueConverter2<RangeHeaderValue> {

    /**
     * Singleton
     */
    final static RangeHeaderValueHeaderValueConverter INSTANCE = new RangeHeaderValueHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private RangeHeaderValueHeaderValueConverter() {
        super();
    }

    @Override
    RangeHeaderValue parse0(final String text, final Name name) {
        return RangeHeaderValue.parse(text);
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkType(value, RangeHeaderValue.class, name);
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
