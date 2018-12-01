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
     * {@see ContentDispositionHeaderValueConverter}
     */
    public static HeaderValueConverter<ContentDisposition> contentDisposition() {
        return ContentDispositionHeaderValueConverter.INSTANCE;
    }
    
    /**
     * {@see ContentDispositionFilenameHeaderValueConverter}
     */
    public static HeaderValueConverter<ContentDispositionFilename> contentDispositionFilename() {
        return ContentDispositionFilenameHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see EmailAddressHeaderValueConverter}
     */
    public static HeaderValueConverter<EmailAddress> emailAddress() {
        return EmailAddressHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see FloatHeaderValueConverter}
     */
    public static HeaderValueConverter<Float> floatConverter() {
        return FloatHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see HeaderValueTokenListHeaderValueConverter}
     */
    public static HeaderValueConverter<List<HeaderValueToken>> headerValueTokenList() {
        return HeaderValueTokenListHeaderValueConverter.INSTANCE;
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
     * {@see MediaTypeStringHeaderValueConverter}
     */
    public static HeaderValueConverter<String> mediaTypeString() {
        return MediaTypeStringHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see OffsetDateTimeHeaderValueConverter}
     */
    public static HeaderValueConverter<OffsetDateTime> offsetDateTime() {
        return OffsetDateTimeHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see RelativeUrlHeaderValueConverter}
     */
    public static HeaderValueConverter<RelativeUrl> relativeUrl() {
        return RelativeUrlHeaderValueConverter.INSTANCE;
    }

    /**
     * {@see StringHeaderValueConverter}
     */
    public static HeaderValueConverter<String> string() {
        return StringHeaderValueConverter.INSTANCE;
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
