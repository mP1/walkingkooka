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

import java.util.List;

/**
 * A {@link HeaderValueConverter} that handles parsing a quoted boundary string into its raw form.<br>
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
final class AcceptEncodingListHeaderValueConverter extends NonStringHeaderValueConverter<List<AcceptEncoding>> {

    /**
     * Singleton
     */
    final static AcceptEncodingListHeaderValueConverter INSTANCE = new AcceptEncodingListHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private AcceptEncodingListHeaderValueConverter() {
        super();
    }

    @Override
    List<AcceptEncoding> parse0(final String text, final Name name) {
        return AcceptEncodingListHeaderValueParser.parseAcceptEncodingList(text);
    }

    @Override
    void check0(final Object value, final Name name) {
        if (this.checkListOfType(value, AcceptEncoding.class, name).isEmpty()) {
            throw new HeaderValueException("Accept-Encoding list empty");
        }
    }

    @Override
    String toText0(final List<AcceptEncoding> acceptEncoding, final Name name) {
        return HeaderValue.toHeaderTextList(acceptEncoding, SEPARATOR);
    }

    @Override
    public String toString() {
        return AcceptEncoding.class.getSimpleName();
    }
}
