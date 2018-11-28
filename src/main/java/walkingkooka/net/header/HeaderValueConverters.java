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
import java.util.List;

public final class HeaderValueConverters implements PublicStaticHelper {

    /**
     * {@see HeaderValueAbsoluteUrlConverter}
     */
    public static HeaderValueConverter<AbsoluteUrl> absoluteUrl() {
        return HeaderValueAbsoluteUrlConverter.INSTANCE;
    }

    /**
     * {@see HeaderValueEmailAddressConverter}
     */
    public static HeaderValueConverter<EmailAddress> emailAddress() {
        return HeaderValueEmailAddressConverter.INSTANCE;
    }

    /**
     * {@see HeaderValueFloatConverter}
     */
    public static HeaderValueConverter<Float> floatConverter() {
        return HeaderValueFloatConverter.INSTANCE;
    }

    /**
     * {@see HeaderValueLocalDateTimeConverter}
     */
    public static HeaderValueConverter<LocalDateTime> localDateTime() {
        return HeaderValueLocalDateTimeConverter.INSTANCE;
    }

    /**
     * {@see HeaderValueLongConverter}
     */
    public static HeaderValueConverter<Long> longConverter() {
        return HeaderValueLongConverter.INSTANCE;
    }

    /**
     * {@see HeaderValueManyMediaTypeConverter}
     */
    public static HeaderValueConverter<List<MediaType>> manyMediaType() {
        return HeaderValueManyMediaTypeConverter.INSTANCE;
    }

    /**
     * {@see HeaderValueOneMediaTypeConverter}
     */
    public static HeaderValueConverter<MediaType> oneMediaType() {
        return HeaderValueOneMediaTypeConverter.INSTANCE;
    }

    /**
     * {@see HeaderValueRelativeUrlConverter}
     */
    public static HeaderValueConverter<RelativeUrl> relativeUrl() {
        return HeaderValueRelativeUrlConverter.INSTANCE;
    }

    /**
     * {@see HeaderValueStringConverter}
     */
    public static HeaderValueConverter<String> string() {
        return HeaderValueStringConverter.INSTANCE;
    }

    /**
     * {@see HttpHeaderValueHeaderTokenListConverter}
     */
    public static HeaderValueConverter<List<HeaderToken>> tokenList() {
        return HeaderValueHeaderTokenListConverter.INSTANCE;
    }

    /**
     * {@see HeaderValueUrlConverter}
     */
    public static HeaderValueConverter<Url> url() {
        return HeaderValueUrlConverter.INSTANCE;
    }

    /**
     * Stop creation
     */
    private HeaderValueConverters() {
        throw new UnsupportedOperationException();
    }
}
