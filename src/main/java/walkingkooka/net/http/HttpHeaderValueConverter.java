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

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.net.header.HeaderValueConverter;
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.net.http.cookie.ClientCookie;
import walkingkooka.net.http.cookie.ServerCookie;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;

/**
 * Base class and contract to assist {@link HttpHeaderName#toValue(String)} and other header
 * text to/from values.
 */
abstract class HttpHeaderValueConverter<T> implements HeaderValueConverter<T> {

    /**
     * {@see CacheControlDirectiveListHeaderValueConverter}
     */
    static HttpHeaderValueConverter<List<CacheControlDirective<?>>> cacheControlDirectiveList() {
        return CacheControlDirectiveListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ClientCookieListHttpHeaderValueConverter}
     */
    static HttpHeaderValueConverter<List<ClientCookie>> clientCookieList() {
        return ClientCookieListHttpHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ContentRangeHttpHeaderValueConverter}
     */
    static HttpHeaderValueConverter<ContentRange> contentRange() {
        return ContentRangeHttpHeaderValueConverter.INSTANCE;
    }
    
    /**
     * {@see HttpETagHeaderValueConverter}
     */
    static HttpHeaderValueConverter<HttpETag> httpETag() {
        return HttpETagHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see HttpETagListHttpHeaderValueConverter}
     */
    static HttpHeaderValueConverter<List<HttpETag>> httpETagList() {
        return HttpETagListHttpHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderNameListHttpHeaderValueConverter}
     */
    static HttpHeaderValueConverter<List<HttpHeaderName<?>>> httpHeaderNameList() {
        return HttpHeaderNameListHttpHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderRangeUnitHttpHeaderValueConverter}
     */
    static HttpHeaderValueConverter<HttpHeaderRangeUnit> httpHeaderRangeUnit() {
        return HttpHeaderRangeUnitHttpHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see HttpMethodListHttpHeaderValueConverter}
     */
    static HttpHeaderValueConverter<List<HttpMethod>> methodList() {
        return HttpMethodListHttpHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderRangeHttpHeaderValueConverter}
     */
    static HttpHeaderValueConverter<HttpHeaderRange> range() {
        return HttpHeaderRangeHttpHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ServerCookieHttpHeaderValueConverter}
     */
    static HttpHeaderValueConverter<ServerCookie> serverCookie() {
        return ServerCookieHttpHeaderValueConverter.INSTANCE;
    }

    /**
     * Package private to limit sub classing.
     */
    HttpHeaderValueConverter() {
        super();
    }

    // parse ....................................................................................................

    /**
     * The entry point that accepts a value and tries to parse it.
     */
    public final T parse(final String value, final Name name) {
        try {
            return this.parse0(value, name);
        } catch (final HeaderValueException cause) {
            throw cause;
        } catch (final RuntimeException cause) {
            throw new HeaderValueException("Failed to convert " + CharSequences.quote(name.value()) +
                    " value " + CharSequences.quote(value) +
                    ", message: " + cause.getMessage(),
                    cause);
        }
    }

    /**
     * Sub classes parse the {@link String} value.
     */
    abstract T parse0(final String value, final Name name) throws HeaderValueException, RuntimeException;

    // checkValue...........................................................

    public final T check(final Object value) {
        Objects.requireNonNull(value, "value");
        this.check0(value);
        return Cast.to(value);
    }

    abstract void check0(final Object value);

    // format ....................................................................................................

    /**
     * Accepts a typed value and formats it into a http response header string.
     */
    public final String toText(final T value, final Name name) {
        try {
            return this.toText0(value, name);
        } catch (final HeaderValueException cause) {
            throw cause;
        } catch (final RuntimeException cause) {
            throw new HeaderValueException("Failed to convert " + CharSequences.quote(name.value()) +
                    " value " + CharSequences.quoteIfChars(value) +
                    " to text, message: " + cause.getMessage(),
                    cause);
        }
    }

    abstract String toText0(final T value, final Name name);

    @Override
    abstract public String toString();

    final String toStringType(final Class<?> type) {
        return type.getSimpleName();
    }

    final String toStringListOf(final Class<?> type) {
        return "List<" + type.getSimpleName() + ">";
    }
}
