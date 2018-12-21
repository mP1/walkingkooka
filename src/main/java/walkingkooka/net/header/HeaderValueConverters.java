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

import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.type.PublicStaticHelper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public final class HeaderValueConverters implements PublicStaticHelper {

    /**
     * {@see AbsoluteUrlHeaderValueConverter}
     */
    public static HeaderValueConverter<AbsoluteUrl> absoluteUrl() {
        return AbsoluteUrlHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see CacheControlDirectiveListHeaderValueConverter}
     */
    public static HeaderValueConverter<List<CacheControlDirective<?>>> cacheControlDirectiveList() {
        return CacheControlDirectiveListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see CharsetHeaderValueListHeaderValueConverter}
     */
    public static HeaderValueConverter<List<CharsetHeaderValue>> charsetHeaderValueList() {
        return CharsetHeaderValueListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see CharsetNameHeaderValueConverter}
     */
    public static HeaderValueConverter<CharsetName> charsetName() {
        return CharsetNameHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ClientCookieListHeaderValueConverter}
     */
    public static HeaderValueConverter<List<ClientCookie>> clientCookieList() {
        return ClientCookieListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ContentDispositionHeaderValueConverter}
     */
    public static HeaderValueConverter<ContentDisposition> contentDisposition() {
        return ContentDispositionHeaderValueConverter.INSTANCE;
    }
    
    /**
     * {@see ContentDispositionFileNameHeaderValueConverter}
     */
    public static HeaderValueConverter<ContentDispositionFileName> contentDispositionFilename() {
        return ContentDispositionFileNameHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ContentRangeHeaderValueConverter}
     */
    public static HeaderValueConverter<ContentRange> contentRange() {
        return ContentRangeHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see EmailAddressHeaderValueConverter}
     */
    public static HeaderValueConverter<EmailAddress> emailAddress() {
        return EmailAddressHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ETagHeaderValueConverter}
     */
    public static HeaderValueConverter<ETag> eTag() {
        return ETagHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ETagListHeaderValueConverter}
     */
    public static HeaderValueConverter<List<ETag>> eTagList() {
        return ETagListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderNameListHeaderValueConverter}
     */
    public static HeaderValueConverter<List<HttpHeaderName<?>>> httpHeaderNameList() {
        return HttpHeaderNameListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see IfRangeHeaderValueConverter}
     */
    public static HeaderValueConverter<IfRange<?>> ifRange() {
        return IfRangeHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see LocalDateTimeHeaderValueConverter}
     */
    public static HeaderValueConverter<LocalDateTime> localDateTime() {
        return LocalDateTimeHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see LongHeaderValueConverter}
     */
    public static HeaderValueConverter<Long> longConverter() {
        return LongHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see MediaTypeHeaderValueConverter}
     */
    public static HeaderValueConverter<MediaType> mediaType() {
        return MediaTypeHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see MediaTypeListHeaderValueConverter}
     */
    public static HeaderValueConverter<List<MediaType>> mediaTypeList() {
        return MediaTypeListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see MediaTypeParameterHeaderValueConverter}
     */
    public static HeaderValueConverter<String> mediaTypeAutoQuotingString() {
        return MediaTypeParameterHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see HttpMethodListHeaderValueConverter}
     */
    public static HeaderValueConverter<List<HttpMethod>> methodList() {
        return HttpMethodListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see OffsetDateTimeHeaderValueConverter}
     */
    public static HeaderValueConverter<OffsetDateTime> offsetDateTime() {
        return OffsetDateTimeHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see QWeightHeaderValueConverter}
     */
    public static HeaderValueConverter<Float> qWeight() {
        return QWeightHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see RangeHeaderValueHeaderValueConverter}
     */
    public static HeaderValueConverter<RangeHeaderValue> range() {
        return RangeHeaderValueHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see RangeHeaderValueUnitHeaderValueConverter}
     */
    public static HeaderValueConverter<RangeHeaderValueUnit> rangeUnit() {
        return RangeHeaderValueUnitHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see RelativeUrlHeaderValueConverter}
     */
    public static HeaderValueConverter<RelativeUrl> relativeUrl() {
        return RelativeUrlHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see ServerCookieHeaderValueConverter}
     */
    public static HeaderValueConverter<ServerCookie> serverCookie() {
        return ServerCookieHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see StringHeaderValueConverter}
     */
    public static HeaderValueConverter<String> string(final CharPredicate predicate,
                                                      final StringHeaderValueConverterFeature...features) {
        return StringHeaderValueConverter.with(predicate, features);
    }

    /**
     * {@see TokenHeaderValueHeaderValueConverter}
     */
    public static HeaderValueConverter<TokenHeaderValue> token() {
        return TokenHeaderValueHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see TokenHeaderValueListHeaderValueConverter}
     */
    public static HeaderValueConverter<List<TokenHeaderValue>> tokenList() {
        return TokenHeaderValueListHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see UrlHeaderValueConverter}
     */
    public static HeaderValueConverter<Url> url() {
        return UrlHeaderValueConverter.INSTANCE;
    }

    /**
     * Stop creation
     */
    private HeaderValueConverters() {
        throw new UnsupportedOperationException();
    }
}
