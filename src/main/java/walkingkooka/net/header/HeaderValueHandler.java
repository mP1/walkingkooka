/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 * Base handler that provides support for converting header text to values and back.
 */
abstract class HeaderValueHandler<T> {

    /**
     * {@see AbsoluteUrlHeaderValueHandler}
     */
    static HeaderValueHandler<AbsoluteUrl> absoluteUrl() {
        return AbsoluteUrlHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see AcceptCharsetHeaderValueHandler}
     */
    static HeaderValueHandler<AcceptCharset> acceptCharset() {
        return AcceptCharsetHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see AcceptEncodingHeaderValueHandler}
     */
    static HeaderValueHandler<AcceptEncoding> acceptEncoding() {
        return AcceptEncodingHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see CacheControlDirectiveListHeaderValueHandler}
     */
    static HeaderValueHandler<List<CacheControlDirective<?>>> cacheControlDirectiveList() {
        return CacheControlDirectiveListHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see ContentEncodingListHeaderValueHandler}
     */
    static HeaderValueHandler<List<ContentEncoding>> contentEncodingList() {
        return ContentEncodingListHeaderValueHandler.INSTANCE;
    }
    
    /**
     * {@see CharsetNameHeaderValueHandler}
     */
    static HeaderValueHandler<CharsetName> charsetName() {
        return CharsetNameHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see ClientCookieListHeaderValueHandler}
     */
    static HeaderValueHandler<List<ClientCookie>> clientCookieList() {
        return ClientCookieListHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see ContentDispositionHeaderValueHandler}
     */
    static HeaderValueHandler<ContentDisposition> contentDisposition() {
        return ContentDispositionHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see ContentDispositionFileNameEncodedHeaderValueHandler}
     */
    static HeaderValueHandler<ContentDispositionFileName> contentDispositionFilename() {
        return ContentDispositionFileNameEncodedHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see ContentRangeHeaderValueHandler}
     */
    static HeaderValueHandler<ContentRange> contentRange() {
        return ContentRangeHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see EmailAddressHeaderValueHandler}
     */
    static HeaderValueHandler<EmailAddress> emailAddress() {
        return EmailAddressHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see EncodedTextHeaderValueHandler}
     */
    static HeaderValueHandler<EncodedText> encodedText() {
        return EncodedTextHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see ETagHeaderValueHandler}
     */
    static HeaderValueHandler<ETag> eTag() {
        return ETagHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see ETagListHeaderValueHandler}
     */
    static HeaderValueHandler<List<ETag>> eTagList() {
        return ETagListHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see HttpHeaderNameListHeaderValueHandler}
     */
    static HeaderValueHandler<List<HttpHeaderName<?>>> httpHeaderNameList() {
        return HttpHeaderNameListHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see IfRangeHeaderValueHandler}
     */
    static HeaderValueHandler<IfRange<?>> ifRange() {
        return IfRangeHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see LanguageTagHeaderValueHandler}
     */
    static HeaderValueHandler<LanguageTag> languageTag() {
        return LanguageTagHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see LanguageTagListHeaderValueHandler}
     */
    static HeaderValueHandler<List<LanguageTag>> languageTagList() {
        return LanguageTagListHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see LanguageTagNameHeaderValueHandler}
     */
    static HeaderValueHandler<LanguageTagName> languageTagName() {
        return LanguageTagNameHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see LinkHeaderValueHandler}
     */
    static HeaderValueHandler<List<Link>> link() {
        return LinkHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see LocalDateTimeHeaderValueHandler}
     */
    static HeaderValueHandler<LocalDateTime> localDateTime() {
        return LocalDateTimeHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see LongHeaderValueHandler}
     */
    static HeaderValueHandler<Long> longHandler() {
        return LongHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see MediaTypeHeaderValueHandler}
     */
    static HeaderValueHandler<MediaType> mediaType() {
        return MediaTypeHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see MediaTypeListHeaderValueHandler}
     */
    static HeaderValueHandler<List<MediaType>> mediaTypeList() {
        return MediaTypeListHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see HttpMethodHeaderValueHandler}
     */
    static HeaderValueHandler<HttpMethod> method() {
        return HttpMethodHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see HttpMethodListHeaderValueHandler}
     */
    static HeaderValueHandler<List<HttpMethod>> methodList() {
        return HttpMethodListHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see OffsetDateTimeHeaderValueHandler}
     */
    static HeaderValueHandler<OffsetDateTime> offsetDateTime() {
        return OffsetDateTimeHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see QuotedUnquotedStringHeaderValueHandler}
     */
    static HeaderValueHandler<String> quotedUnquotedString(final CharPredicate quotedPredicate,
                                                           final boolean supportBackslashEscaping,
                                                           final CharPredicate unquotedPredicate) {
        return QuotedUnquotedStringHeaderValueHandler.with(quotedPredicate, supportBackslashEscaping, unquotedPredicate);
    }

    /**
     * {@see QuotedStringHeaderValueHandler}
     */
    static HeaderValueHandler<String> quoted(final CharPredicate predicate,
                                             final boolean supportBackslashEscaping) {
        return QuotedStringHeaderValueHandler.with(predicate, supportBackslashEscaping);
    }

    /**
     * {@see QWeightHeaderValueHandler}
     */
    static HeaderValueHandler<Float> qWeight() {
        return QWeightHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see RangeHeaderValueHeaderValueHandler}
     */
    static HeaderValueHandler<RangeHeaderValue> range() {
        return RangeHeaderValueHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see RangeHeaderValueUnitHeaderValueHandler}
     */
    static HeaderValueHandler<RangeHeaderValueUnit> rangeUnit() {
        return RangeHeaderValueUnitHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see RelativeUrlHeaderValueHandler}
     */
    static HeaderValueHandler<RelativeUrl> relativeUrl() {
        return RelativeUrlHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see LinkRelationHeaderValueHandler}
     */
    static HeaderValueHandler<List<LinkRelation<?>>> relation() {
        return LinkRelationHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see ServerCookieHeaderValueHandler}
     */
    static HeaderValueHandler<ServerCookie> serverCookie() {
        return ServerCookieHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see UnalteredStringHeaderValueHandler}
     */
    static HeaderValueHandler<String> string() {
        return UnalteredStringHeaderValueHandler.INSTANCE;
    }

    /**
     * {@see UnquotedStringHeaderValueHandler}
     */
    static HeaderValueHandler<String> unquoted(final CharPredicate predicate) {
        return UnquotedStringHeaderValueHandler.with(predicate);
    }

    /**
     * {@see UrlHeaderValueHandler}
     */
    static HeaderValueHandler<Url> url() {
        return UrlHeaderValueHandler.INSTANCE;
    }

    /**
     * Separator to be used when producing the text form from a list of header values.
     */
    final static String SEPARATOR = HeaderValue.SEPARATOR.string().concat(" ");

    /**
     * Package private to limit sub classing.
     */
    HeaderValueHandler() {
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
            throw new HeaderValueException(invalidTypeNameMessage(name, value, type.getSimpleName()));
        }
        return type.cast(value);
    }

    private final static String JAVA_LANG = "java.lang";
    private final static String PACKAGE = HeaderValueHandler.class.getPackage().getName();

    /**
     * Checks the type of the given value and throws a {@link HeaderValueException} if this test fails.
     */
    final <U> List<U> checkListOfType(final Object value, final Class<U> type, final Name name) {
        if (!(value instanceof List)) {
            throw new HeaderValueException(invalidTypeNameMessage(name, value, "List of " + type.getSimpleName()));
        }

        final List<U> list = Cast.to(value);
        for (Object element : list) {
            if (null == element) {
                throw new HeaderValueException(header(name, value) + " includes a null");
            }
            if (!type.isInstance(element)) {
                throw new HeaderValueException(header(name, value) + " includes an element " + CharSequences.quoteIfChars(element) + "(" + typeName(element) + ") that is not a " + type.getName());
            }
        }
        return Cast.to(list);
    }

    private static String invalidTypeNameMessage(final Name name,
                                                 final Object value,
                                                 final String requiredType) {
        return header(name, value) + " value type(" + typeName(value) + ") is not a " + requiredType;
    }

    private static String header(final Name name, final Object value) {
        return "Header " + CharSequences.quote(name + ": " + value);
    }

    private static String typeName(final Object value) {
        // try and use simple name otherwise include fqn in message.
        String typeName = value.getClass().getName();
        if (typeName.startsWith(JAVA_LANG)) {
            typeName = typeName.substring(JAVA_LANG.length() + 1);
        } else {
            if (typeName.startsWith(PACKAGE) && typeName.indexOf(PACKAGE.length() + 1) == -1) {
                typeName = typeName.substring(PACKAGE.length() + 1);
            }
        }
        return typeName;
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
