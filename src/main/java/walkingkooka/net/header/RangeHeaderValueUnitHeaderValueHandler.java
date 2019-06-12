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
 * A {@link HeaderValueHandler} that parses a header value into a {@link RangeHeaderValueUnit >}.
 * This is useful for headers such as {@link HttpHeaderName#ACCEPT_RANGES}.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Ranges"></a>
 * <pre>
 * Accept-Ranges: bytes
 * Accept-Ranges: none
 * </pre>
 */
final class RangeHeaderValueUnitHeaderValueHandler extends NonStringHeaderValueHandler<RangeHeaderValueUnit> {

    /**
     * Singleton
     */
    final static RangeHeaderValueUnitHeaderValueHandler INSTANCE = new RangeHeaderValueUnitHeaderValueHandler();

    /**
     * Private ctor use singleton.
     */
    private RangeHeaderValueUnitHeaderValueHandler() {
        super();
    }

    @Override
    RangeHeaderValueUnit parse0(final String text, final Name name) {
        return RangeHeaderValueUnit.parse(text);
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkType(value, RangeHeaderValueUnit.class, name);
    }

    @Override
    String toText0(final RangeHeaderValueUnit value, final Name name) {
        return value.toHeaderText();
    }

    @Override
    public String toString() {
        return this.toStringType(RangeHeaderValueUnit.class);
    }
}
