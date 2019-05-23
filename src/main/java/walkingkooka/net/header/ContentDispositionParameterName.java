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

import walkingkooka.naming.Name;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * The {@link Name} of content disposition parameter value.<br>
 * <a href="https://tools.ietf.org/html/rfc2183"></a>
 * <pre>
 * In the extended BNF notation of [RFC 822], the Content-Disposition
 * header field is defined as follows:
 *
 *      disposition := "Content-Disposition" ":"
 *                     disposition-type
 *                     *(";" disposition-parm)
 *
 *      disposition-type := "inline"
 *                        / "attachment"
 *                        / extension-token
 *                        ; values are not case-sensitive
 *
 *      disposition-parm := filename-parm
 *                        / creation-date-parm
 *                        / modification-date-parm
 *                        / read-date-parm
 *                        / size-parm
 *                        / parameter
 *
 *      filename-parm := "filename" "=" value
 *
 *      creation-date-parm := "creation-date" "=" quoted-date-time
 *
 *      modification-date-parm := "modification-date" "=" quoted-date-time
 *
 *      read-date-parm := "read-date" "=" quoted-date-time
 *
 *      size-parm := "size" "=" 1*DIGIT
 *
 *      quoted-date-time := quoted-string
 *                       ; contents MUST be an RFC 822 `date-time'
 *                       ; numeric timezones (+HHMM or -HHMM) MUST be used
 * </pre>
 */
final public class ContentDispositionParameterName<V> extends HeaderParameterName<V> implements
        Comparable<ContentDispositionParameterName<?>> {

    /**
     * Constant returned when a parameter value is absent.
     */
    public final static Optional<?> VALUE_ABSENT = Optional.empty();

    /**
     * Holds all constants and lookup or creation.
     */
    private final static HeaderParameterNameConstants<ContentDispositionParameterName<?>> CONSTANTS = HeaderParameterNameConstants.empty(
            ContentDispositionParameterName::new,
            HeaderValueConverter.quotedUnquotedString(
                    ContentDispositionHeaderValueParser.QUOTED_PARAMETER_VALUE,
                    true,
                    ContentDispositionHeaderValueParser.UNQUOTED_PARAMETER_VALUE
            ));

    /**
     * A {@link ContentDispositionParameterName} holding <code>creation-date</code>
     */
    public final static ContentDispositionParameterName<OffsetDateTime> CREATION_DATE = CONSTANTS.register("creation-date",
            HeaderValueConverter.offsetDateTime());

    /**
     * A {@link ContentDispositionParameterName} holding <code>filename</code>
     */
    public final static ContentDispositionParameterName<ContentDispositionFileName> FILENAME = CONSTANTS.register("filename",
            ContentDispositionFileNameNotEncodedHeaderValueConverter.INSTANCE);

    /**
     * A {@link ContentDispositionParameterName} holding <code>filename*</code>
     */
    public final static ContentDispositionParameterName<ContentDispositionFileName> FILENAME_STAR = CONSTANTS.register("filename*",
            HeaderValueConverter.contentDispositionFilename());

    /**
     * A {@link ContentDispositionParameterName} holding <code>modification-date</code>
     */
    public final static ContentDispositionParameterName<OffsetDateTime> MODIFICATION_DATE = CONSTANTS.register("modification-date",
            HeaderValueConverter.offsetDateTime());

    /**
     * A {@link ContentDispositionParameterName} holding <code>read-date</code>
     */
    public final static ContentDispositionParameterName<OffsetDateTime> READ_DATE = CONSTANTS.register("read-date",
            HeaderValueConverter.offsetDateTime());
    /**
     * A {@link ContentDispositionParameterName} holding <code>size</code>
     */
    public final static ContentDispositionParameterName<Long> SIZE = CONSTANTS.register("size",
            HeaderValueConverter.longConverter());

    // factory ......................................................................................................

    /**
     * Factory that creates a {@link ContentDispositionParameterName}. If the {@link #toValue} is not a constant
     * it will return a parameter name with value of type {@link String}.
     */
    public static ContentDispositionParameterName<?> with(final String name) {
        return CONSTANTS.lookup(name);
    }

    /**
     * Private constructor use factory.
     */
    private ContentDispositionParameterName(final String name,
                                            final HeaderValueConverter<V> converter) {
        super(name, converter);
    }

    // Comparable......................................................................................................

    @Override
    public int compareTo(final ContentDispositionParameterName<?> other) {
        return this.compareTo0(other);
    }

    // HeaderName2......................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ContentDispositionParameterName;
    }
}
