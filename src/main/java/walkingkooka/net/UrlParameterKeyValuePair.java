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

package walkingkooka.net;

import walkingkooka.ShouldNeverHappenError;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * A query string parameter.
 */
final class UrlParameterKeyValuePair {

    static UrlParameterKeyValuePair nameAndValue(final UrlParameterName name, final String value) {
        String encoded;
        try {
            encoded = name.value() + Url.QUERY_NAME_VALUE_SEPARATOR.character() + URLEncoder.encode(value, "UTF-8");
        } catch ( final UnsupportedEncodingException never) {
            throw new Error(never);
        }
        return new UrlParameterKeyValuePair(encoded, name, value, Url.QUERY_PARAMETER_SEPARATOR.character(), false);
    }

    static UrlParameterKeyValuePair encodedWithSeparator(final String encodedWithoutSeparator, final char separator) {
        return encoded(encodedWithoutSeparator, separator, true);
    }

    static UrlParameterKeyValuePair encodedWithoutSeparator(final String encodedWithoutSeparator) {
        return encoded(encodedWithoutSeparator, Url.QUERY_PARAMETER_SEPARATOR.character(), false);
    }

    private static UrlParameterKeyValuePair encoded(final String encodedWithoutSeparator, final char separator, final boolean separatorIncluded) {
        final int equalsSign = encodedWithoutSeparator.indexOf(Url.QUERY_NAME_VALUE_SEPARATOR.character());
        String name = encodedWithoutSeparator;
        String value = "";
        if(-1 != equalsSign) {
            name = name.substring(0, equalsSign);
            try {
                value = URLDecoder.decode(encodedWithoutSeparator.substring(equalsSign + 1), "UTF-8");
            } catch (final UnsupportedEncodingException never) {
                throw new ShouldNeverHappenError(never.getMessage(), never);
            }
        }

        return new UrlParameterKeyValuePair(encodedWithoutSeparator,
                UrlParameterName.with(name),
                value,
                separator, separatorIncluded);
    }

    /**
     * Private ctor use factories.
     */
    private UrlParameterKeyValuePair(final String encoded, final UrlParameterName name, final String value, final char separator, final boolean separatorIncluded) {
        this.encoded = encoded;
        this.name = name;
        this.value = value;
        this.separator = separator;
        this.separatorIncluded = separatorIncluded;
    }

    final UrlParameterName name;
    final String value;

    public String toString() {
        return this.encoded;
    }

    /**
     * Note the encoded string will not include any separator that followed.
     */
    final String encoded;

    final boolean separatorIncluded;
    final char separator;
}
