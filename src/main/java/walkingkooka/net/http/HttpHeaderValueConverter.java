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

import walkingkooka.naming.Name;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.net.http.cookie.ClientCookie;
import walkingkooka.net.media.MediaType;
import walkingkooka.text.CharSequences;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Base class and contract to assist {@link HttpHeaderName#headerValue(HttpRequest)}
 */
abstract class HttpHeaderValueConverter<T> {

    /**
     * {@see HttpHeaderValueAbsoluteUrlConverter}
     */
    static HttpHeaderValueConverter<AbsoluteUrl> absoluteUrl() {
        return HttpHeaderValueAbsoluteUrlConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueClientCookieListConverter}
     */
    static HttpHeaderValueConverter<List<ClientCookie>> clientCookieList() {
        return HttpHeaderValueClientCookieListConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueEmailAddressConverter}
     */
    static HttpHeaderValueConverter<EmailAddress> emailAddress() {
        return HttpHeaderValueEmailAddressConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueFloatConverter}
     */
    static HttpHeaderValueConverter<Float> floatConverter() {
        return HttpHeaderValueFloatConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueHttpHeaderNameListConverter}
     */
    static HttpHeaderValueConverter<List<HttpHeaderName<?>>> httpHeaderNameList() {
        return HttpHeaderValueHttpHeaderNameListConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueLocalDateTimeConverter}
     */
    static HttpHeaderValueConverter<LocalDateTime> localDateTime() {
        return HttpHeaderValueLocalDateTimeConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueLongConverter}
     */
    static HttpHeaderValueConverter<Long> longConverter() {
        return HttpHeaderValueLongConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueManyMediaTypeConverter}
     */
    static HttpHeaderValueConverter<List<MediaType>> manyMediaType() {
        return HttpHeaderValueManyMediaTypeConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueHttpMethodListConverter}
     */
    static HttpHeaderValueConverter<List<HttpMethod>> methodList() {
        return HttpHeaderValueHttpMethodListConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueOneMediaTypeConverter}
     */
    static HttpHeaderValueConverter<MediaType> oneMediaType() {
        return HttpHeaderValueOneMediaTypeConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueRangeConverter}
     */
    static HttpHeaderValueConverter<HttpHeaderRange> range() {
        return HttpHeaderValueRangeConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueRelativeUrlConverter}
     */
    static HttpHeaderValueConverter<RelativeUrl> relativeUrl() {
        return HttpHeaderValueRelativeUrlConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueStringConverter}
     */
    static HttpHeaderValueConverter<String> string() {
        return HttpHeaderValueStringConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueHttpHeaderTokenListConverter}
     */
    static HttpHeaderValueConverter<List<HttpHeaderToken>> tokenList() {
        return HttpHeaderValueHttpHeaderTokenListConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueUrlConverter}
     */
    static HttpHeaderValueConverter<Url> url() {
        return HttpHeaderValueUrlConverter.INSTANCE;
    }

    /**
     * Package private to limit sub classing.
     */
    HttpHeaderValueConverter() {
        super();
    }

    /**
     * The entry point that accepts a value and tries to parse it.
     */
    final T parse(final String value, final Name name) {
        try {
            return this.parse0(value, name);
        } catch (final HttpHeaderValueException cause) {
            throw cause;
        } catch (final RuntimeException cause) {
            throw new HttpHeaderValueException("Failed to convert " + CharSequences.quote(name.value()) +
                    " value " + CharSequences.quote(value) +
                    ", message: " + cause.getMessage(),
                    cause);
        }
    }

    /**
     * Sub classes parse the {@link String} value.
     */
    abstract T parse0(final String value, final Name name) throws HttpHeaderValueException, RuntimeException;

    /**
     * Only {@link HttpHeaderValueStringConverter} returns true.
     */
    abstract boolean isString();

    @Override
    abstract public String toString();

    final String toStringType(final Class<?> type) {
        return type.getSimpleName();
    }

    final String toStringListOf(final Class<?> type) {
        return "List<" + type.getSimpleName() + ">";
    }
}
