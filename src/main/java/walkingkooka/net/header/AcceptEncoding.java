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

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The accept encoding header.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Encoding"></a>
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
public final class AcceptEncoding extends HeaderValue2<List<Encoding>>
        implements Predicate<ContentEncoding> {

    /**
     * Parses a header value that contains one or more encodings.
     */
    public static AcceptEncoding parse(final String text) {
        return AcceptEncodingHeaderValueParser.parseAcceptEncoding(text);
    }

    /**
     * Factory that creates a new {@link AcceptEncoding}
     */
    public static AcceptEncoding with(final List<Encoding> values) {
        Objects.requireNonNull(values, "values");

        return new AcceptEncoding(values.stream()
                .map(v -> Objects.requireNonNull(v, "values includes null"))
                .collect(Collectors.toList()));
    }

    /**
     * Package private ctor use factory. Only called directly by factory or {@link AcceptEncodingHeaderValueParser}
     */
    AcceptEncoding(final List<Encoding> values) {
        super(values);
    }

    // Predicate........................................................................................................

    /**
     * Returns true if any of the encodings belong to this accept-encoding matches the given {@link ContentEncoding}.
     */
    @Override
    public boolean test(final ContentEncoding contentEncoding) {
        Objects.requireNonNull(contentEncoding, "contentEncoding");

        return this.value.stream()
                .filter(e -> e.test(contentEncoding))
                .limit(1)
                .count() == 1;
    }

    // HeaderValue.....................................................................................................

    @Override
    public String toHeaderText() {
        return HeaderValue.toHeaderTextList(value, SEPARATOR);
    }

    private final static String SEPARATOR = HeaderValue.SEPARATOR.string().concat(" ");


    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return false;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof AcceptEncoding;
    }
}
