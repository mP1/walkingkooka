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
 * A {@link HeaderValueHandler} that handles parsing a quoted boundary string into its raw form.<br>
 * <pre>
 * Accept-Encoding: gzip
 * Accept-Encoding: compress
 * Accept-Encoding: deflate
 * Accept-Encoding: br
 * Accept-Encoding: identity
 * Accept-Encoding: *
 *
 * // Multiple algorithms, weighted with the quality value syntax:
 * Accept-Encoding: deflate, gzip;q=1.0, *;q=0.5
 * </pre>
 */
final class AcceptEncodingHeaderValueHandler extends NonStringHeaderValueHandler<AcceptEncoding> {

    /**
     * Singleton
     */
    final static AcceptEncodingHeaderValueHandler INSTANCE = new AcceptEncodingHeaderValueHandler();

    /**
     * Private ctor use singleton.
     */
    private AcceptEncodingHeaderValueHandler() {
        super();
    }

    @Override
    AcceptEncoding parse0(final String text, final Name name) {
        return AcceptEncodingHeaderValueParser.parseAcceptEncoding(text);
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkType(value, AcceptEncoding.class, name);
    }

    @Override
    String toText0(final AcceptEncoding acceptEncoding, final Name name) {
        return acceptEncoding.toHeaderText();
    }

    @Override
    public String toString() {
        return AcceptEncoding.class.getSimpleName();
    }
}
