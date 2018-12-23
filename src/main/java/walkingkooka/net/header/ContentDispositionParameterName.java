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
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
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
final public class ContentDispositionParameterName<T> implements HeaderParameterName<T>,
        Comparable<ContentDispositionParameterName<?>> {

    /**
     * Constant returned when a parameter value is absent.
     */
    public final static Optional<?> VALUE_ABSENT = Optional.empty();

    // constants

    /**
     * A read only cache of already prepared {@link ContentDispositionParameterName names}. These constants are incomplete.
     */
    final static Map<String, ContentDispositionParameterName> CONSTANTS = Maps.sorted();

    /**
     * Creates and adds a new {@link ContentDispositionParameterName} to the cache being built that handles float header values.
     */
    private static ContentDispositionParameterName<OffsetDateTime> registerOffsetDateTimeConstant(final String header) {
        return registerConstant(header, HeaderValueConverter.offsetDateTime());
    }

    /**
     * Creates and adds a new {@link ContentDispositionParameterName} to the cache being built.
     */
    private static <T> ContentDispositionParameterName<T> registerConstant(final String name,
                                                                           final HeaderValueConverter<T> headerValue) {
        final ContentDispositionParameterName<T> header = new ContentDispositionParameterName<T>(name, headerValue);
        ContentDispositionParameterName.CONSTANTS.put(name, header);
        return header;
    }

    /**
     * A {@link ContentDispositionParameterName} holding <code>creation-date</code>
     */
    public final static ContentDispositionParameterName<OffsetDateTime> CREATION_DATE = registerOffsetDateTimeConstant("creation-date");

    /**
     * A {@link ContentDispositionParameterName} holding <code>filename</code>
     */
    public final static ContentDispositionParameterName<ContentDispositionFileName> FILENAME = registerConstant("filename",
            ContentDispositionFileNameHeaderValueConverter.INSTANCE);

    /**
     * A {@link ContentDispositionParameterName} holding <code>modification-date</code>
     */
    public final static ContentDispositionParameterName<OffsetDateTime> MODIFICATION_DATE = registerOffsetDateTimeConstant("modification-date");

    /**
     * A {@link ContentDispositionParameterName} holding <code>read-date</code>
     */
    public final static ContentDispositionParameterName<OffsetDateTime> READ_DATE = registerOffsetDateTimeConstant("read-date");

    /**
     * A {@link ContentDispositionParameterName} holding <code>size</code>
     */
    public final static ContentDispositionParameterName<Long> SIZE = registerConstant("size",
            HeaderValueConverter.longConverter());

    // factory ......................................................................................................

    /**
     * Factory that creates a {@link ContentDispositionParameterName}. If the {@link #toValue} is not a constant
     * it will return a parameter name with value of type {@link String}.
     */
    public static ContentDispositionParameterName<?> with(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", CharPredicates.rfc2045Token());

        final ContentDispositionParameterName<?> headerValueParameterName = CONSTANTS.get(name);
        return null != headerValueParameterName ?
                headerValueParameterName :
                new ContentDispositionParameterName<String>(name, QUOTED_UNQUOTED_STRING);
    }

    /**
     * Allow quoted and unquoted strings.
     */
    private final static HeaderValueConverter<String> QUOTED_UNQUOTED_STRING = HeaderValueConverter.quotedUnquotedString(
            ContentDispositionHeaderParser.QUOTED_PARAMETER_VALUE,
            true,
            ContentDispositionHeaderParser.UNQUOTED_PARAMETER_VALUE
    );

    /**
     * Private constructor use factory.
     */
    private ContentDispositionParameterName(final String name,
                                            final HeaderValueConverter<T> converter) {
        super();
        this.name = name;
        this.converter = converter;
    }

    // Value ....................................................................................................

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    /**
     * Accepts text and converts it into its value.
     */
    @Override
    public T toValue(final String text) {
        Objects.requireNonNull(text, "text");

        return this.converter.parse(text, this);
    }

    /**
     * Validates the value and casts it to its type.
     */
    @Override
    public T checkValue(final Object value) {
        return this.converter.check(value);
    }

    private final HeaderValueConverter<T> converter;

    // Comparable......................................................................................................

    @Override
    public int compareTo(final ContentDispositionParameterName<?> other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    // Object.....................................................................................................

    @Override
    public int hashCode() {
        return CaseSensitivity.INSENSITIVE.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof ContentDispositionParameterName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final ContentDispositionParameterName name) {
        return this.name.equalsIgnoreCase(name.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
