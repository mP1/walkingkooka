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

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.text.CharSequences;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Base converter that provides support for converting header text to values and back.
 */
abstract class HeaderValueConverter<T> {

    /**
     * {@see AbsoluteUrlHeaderValueConverter}
     */
    static HeaderValueConverter<AbsoluteUrl> absoluteUrl() {
        return AbsoluteUrlHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see CacheControlDirectiveListHeaderValueConverter}
     */
    static HeaderValueConverter<List<CacheControlDirective<?>>> cacheControlDirectiveList() {
        return CacheControlDirectiveListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see CharsetHeaderValueListHeaderValueConverter}
     */
    static HeaderValueConverter<List<CharsetHeaderValue>> charsetHeaderValueList() {
        return CharsetHeaderValueListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see CharsetNameHeaderValueConverter}
     */
    static HeaderValueConverter<CharsetName> charsetName() {
        return CharsetNameHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ClientCookieListHeaderValueConverter}
     */
    static HeaderValueConverter<List<ClientCookie>> clientCookieList() {
        return ClientCookieListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ContentDispositionHeaderValueConverter}
     */
    static HeaderValueConverter<ContentDisposition> contentDisposition() {
        return ContentDispositionHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ContentDispositionFileNameEncodedHeaderValueConverter}
     */
    static HeaderValueConverter<ContentDispositionFileName> contentDispositionFilename() {
        return ContentDispositionFileNameEncodedHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ContentRangeHeaderValueConverter}
     */
    static HeaderValueConverter<ContentRange> contentRange() {
        return ContentRangeHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see EmailAddressHeaderValueConverter}
     */
    static HeaderValueConverter<EmailAddress> emailAddress() {
        return EmailAddressHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see EncodedTextHeaderValueConverter}
     */
    static HeaderValueConverter<EncodedText> encodedText() {
        return EncodedTextHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ETagHeaderValueConverter}
     */
    static HeaderValueConverter<ETag> eTag() {
        return ETagHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ETagListHeaderValueConverter}
     */
    static HeaderValueConverter<List<ETag>> eTagList() {
        return ETagListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderNameListHeaderValueConverter}
     */
    static HeaderValueConverter<List<HttpHeaderName<?>>> httpHeaderNameList() {
        return HttpHeaderNameListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see IfRangeHeaderValueConverter}
     */
    static HeaderValueConverter<IfRange<?>> ifRange() {
        return IfRangeHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see LanguageTagHeaderValueConverter}
     */
    static HeaderValueConverter<LanguageTag> languageTag() {
        return LanguageTagHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see LanguageTagListHeaderValueConverter}
     */
    static HeaderValueConverter<List<LanguageTag>> languageTagList() {
        return LanguageTagListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see LanguageTagNameHeaderValueConverter}
     */
    static HeaderValueConverter<LanguageTagName> languageTagName() {
        return LanguageTagNameHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see LinkHeaderValueConverter}
     */
    static HeaderValueConverter<List<Link>> link() {
        return LinkHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see LocalDateTimeHeaderValueConverter}
     */
    static HeaderValueConverter<LocalDateTime> localDateTime() {
        return LocalDateTimeHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see LongHeaderValueConverter}
     */
    static HeaderValueConverter<Long> longConverter() {
        return LongHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see MediaTypeHeaderValueConverter}
     */
    static HeaderValueConverter<MediaType> mediaType() {
        return MediaTypeHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see MediaTypeListHeaderValueConverter}
     */
    static HeaderValueConverter<List<MediaType>> mediaTypeList() {
        return MediaTypeListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see HttpMethodHeaderValueConverter}
     */
    static HeaderValueConverter<HttpMethod> method() {
        return HttpMethodHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see HttpMethodListHeaderValueConverter}
     */
    static HeaderValueConverter<List<HttpMethod>> methodList() {
        return HttpMethodListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see OffsetDateTimeHeaderValueConverter}
     */
    static HeaderValueConverter<OffsetDateTime> offsetDateTime() {
        return OffsetDateTimeHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see QuotedUnquotedStringHeaderValueConverter}
     */
    static HeaderValueConverter<String> quotedUnquotedString(final CharPredicate quotedPredicate,
                                                             final boolean supportBackslashEscaping,
                                                             final CharPredicate unquotedPredicate) {
        return QuotedUnquotedStringHeaderValueConverter.with(quotedPredicate, supportBackslashEscaping, unquotedPredicate);
    }

    /**
     * {@see QuotedStringHeaderValueConverter}
     */
    static HeaderValueConverter<String> quoted(final CharPredicate predicate,
                                               final boolean supportBackslashEscaping) {
        return QuotedStringHeaderValueConverter.with(predicate, supportBackslashEscaping);
    }

    /**
     * {@see QWeightHeaderValueConverter}
     */
    static HeaderValueConverter<Float> qWeight() {
        return QWeightHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see RangeHeaderValueHeaderValueConverter}
     */
    static HeaderValueConverter<RangeHeaderValue> range() {
        return RangeHeaderValueHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see RangeHeaderValueUnitHeaderValueConverter}
     */
    static HeaderValueConverter<RangeHeaderValueUnit> rangeUnit() {
        return RangeHeaderValueUnitHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see RelativeUrlHeaderValueConverter}
     */
    static HeaderValueConverter<RelativeUrl> relativeUrl() {
        return RelativeUrlHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see LinkRelationHeaderValueConverter}
     */
    static HeaderValueConverter<List<LinkRelation<?>>> relation() {
        return LinkRelationHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ServerCookieHeaderValueConverter}
     */
    static HeaderValueConverter<ServerCookie> serverCookie() {
        return ServerCookieHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see TokenHeaderValueHeaderValueConverter}
     */
    static HeaderValueConverter<TokenHeaderValue> token() {
        return TokenHeaderValueHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see TokenHeaderValueListHeaderValueConverter}
     */
    static HeaderValueConverter<List<TokenHeaderValue>> tokenList() {
        return TokenHeaderValueListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see UnquotedStringHeaderValueConverter}
     */
    static HeaderValueConverter<String> unquoted(final CharPredicate predicate) {
        return UnquotedStringHeaderValueConverter.with(predicate);
    }

    /**
     * {@see UrlHeaderValueConverter}
     */
    static HeaderValueConverter<Url> url() {
        return UrlHeaderValueConverter.INSTANCE;
    }

    /**
     * Separator to be used when producing the text form from a list of header values.
     */
    final static String SEPARATOR = HeaderValue.SEPARATOR.string().concat(" ");

    /**
     * Package private to limit sub classing.
     */
    HeaderValueConverter() {
        super();
    }

    // parse ....................................................................................................

    /**
     * The entry point that accepts a value and tries to parse it.
     */
    final T parse(final String text, final Name name) {
        Objects.requireNonNull(text, "text");

        try {
            return this.parse0(text, name);
        } catch (final HeaderValueException cause) {
            throw cause;
        } catch (final RuntimeException cause) {
            throw new HeaderValueException("Failed to convert " + CharSequences.quote(name.value()) +
                    " value " + CharSequences.quote(text) +
                    ", message: " + cause.getMessage(),
                    cause);
        }
    }

    /**
     * Sub classes parse the {@link String} value.
     */
    abstract T parse0(final String text, final Name name) throws HeaderValueException, RuntimeException;

    // checkValue...........................................................

    final T check(final Object value, final Name name) {
        Objects.requireNonNull(value, "value");
        this.check0(value, name);
        return Cast.to(value);
    }

    abstract void check0(final Object value, final Name name);

    /**
     * Checks the type of the given value and throws a {@link HeaderValueException} if this test fails.
     */
    final <U> U checkType(final Object value, final Class<U> type, final Name name) {
        if (!type.isInstance(value)) {
            throw new HeaderValueException(name + " value " + CharSequences.quoteIfChars(value) + " is not a " + type.getName());
        }
        return type.cast(value);
    }

    /**
     * Checks the type of the given value and throws a {@link HeaderValueException} if this test fails.
     */
    final <U> List<U> checkListOfType(final Object value, final Class<U> type, final Name name) {
        if (!(value instanceof List)) {
            throw new HeaderValueException(name + " value " + CharSequences.quoteIfChars(value) + " is not a List of " + type.getName());
        }

        final List<U> list = Cast.to(value);
        for (Object element : list) {
            if (!type.isInstance(element)) {
                throw new HeaderValueException(name + " list value " + CharSequences.quoteIfChars(value) +
                        " includes an element that is not a " + type.getName());
            }
        }
        return Cast.to(list);
    }

    // toText ....................................................................................................

    /**
     * Accepts a typed value and formats it into a http response header string.
     */
    final String toText(final T value, final Name name) {
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

    // httpHeaderNameCast ....................................................................................................

    /**
     * Casts this {@link HttpHeaderName} only if it is already giving {@link String} values.
     */
    abstract HttpHeaderName<String> httpHeaderNameCast(final HttpHeaderName<?> headerName);

    // Object ..........................................................................................

    @Override
    abstract public String toString();

    final String toStringType(final Class<?> type) {
        return type.getSimpleName();
    }

    final String toStringListOf(final Class<?> type) {
        return "List<" + type.getSimpleName() + ">";
    }
}
