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

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Charset"></a>
 * <pre>
 * Accept-Charset: <charset>
 *
 * // Multiple types, weighted with the quality value syntax:
 * Accept-Charset: utf-8, iso-8859-1;q=0.5
 * </pre>
 */
public final class AcceptCharset extends HeaderValue2<List<CharsetHeaderValue>> {

    /**
     * Parses a header value that contains one or more charsets.
     */
    public static AcceptCharset parse(final String text) {
        return AcceptCharsetHeaderValueParser.parseAcceptCharset(text);
    }

    /**
     * Factory that creates a new {@link AcceptCharset}
     */
    public static AcceptCharset with(final List<CharsetHeaderValue> values) {
        Objects.requireNonNull(values, "values");

        return new AcceptCharset(values.stream()
                .map(v -> Objects.requireNonNull(v, "values includes null"))
                .collect(Collectors.toList()));
    }

    /**
     * Private ctor use factory. Only called directly by factory or {@link AcceptCharsetHeaderValueParser}
     */
    AcceptCharset(final List<CharsetHeaderValue> values) {
        super(values);
    }

    /**
     * Finds the first system supported {@link Charset}. This assumes the {@link CharsetHeaderValue} have already been
     * sorted by q factors, thus order implies importance.
     */
    public Optional<Charset> charset() {
        return this.value().stream()
                .map(chv -> chv.value().charset())
                .filter(c -> c.isPresent())
                .map(c -> c.get())
                .findFirst();
    }

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
        return other instanceof AcceptCharset;
    }
}
