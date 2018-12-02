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
import walkingkooka.predicate.character.CharPredicate;
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
        return registerConstant(header,
                DATE,
                HeaderValueConverters.offsetDateTime());
    }

    /**
     * <a href="https://tools.ietf.org/html/rfc822"></a>
     * <pre>
     * date-time   =  [ day "," ] date time        ; dd mm yy
     *                                                  ;  hh:mm:ss zzz
     *
     *      day         =  "Mon"  / "Tue" /  "Wed"  / "Thu"
     *                  /  "Fri"  / "Sat" /  "Sun"
     *
     *      date        =  1*2DIGIT month 2DIGIT        ; day month year
     *                                                  ;  e.g. 20 Jun 82
     *
     *      month       =  "Jan"  /  "Feb" /  "Mar"  /  "Apr"
     *                  /  "May"  /  "Jun" /  "Jul"  /  "Aug"
     *                  /  "Sep"  /  "Oct" /  "Nov"  /  "Dec"
     *
     *      time        =  hour zone                    ; ANSI and Military
     *
     *      hour        =  2DIGIT ":" 2DIGIT [":" 2DIGIT]
     *                                                  ; 00:00:00 - 23:59:59
     *
     *      zone        =  "UT"  / "GMT"                ; Universal Time
     *                                                  ; North American : UT
     *                  /  "EST" / "EDT"                ;  Eastern:  - 5/ - 4
     *                  /  "CST" / "CDT"                ;  Central:  - 6/ - 5
     *                  /  "MST" / "MDT"                ;  Mountain: - 7/ - 6
     *                  /  "PST" / "PDT"                ;  Pacific:  - 8/ - 7
     *                  /  1ALPHA                       ; Military: Z = UT;
     *                                                  ;  A:-1; (J not used)
     *                                                  ;  M:-12; N:+1; Y:+12
     *                  / ( ("+" / "-") 4DIGIT )        ; Local differential
     *                                                  ;  hours+min. (HHMM)
     * </pre>
     */
    private final static CharPredicate DATE = CharPredicates.builder()
            .any("MonTuesWedThuFriSatSun,JanFebMarAprMayJunJulAugSepOctNovDec 0123456789:+-")
            .build()
            .setToString("RFC822 Date");

    /**
     * Creates and adds a new {@link ContentDispositionParameterName} to the cache being built.
     */
    private static <T> ContentDispositionParameterName<T> registerConstant(final String name,
                                                                           final CharPredicate charPredicate,
                                                                           final HeaderValueConverter<T> headerValue) {
        final ContentDispositionParameterName<T> header = new ContentDispositionParameterName<T>(name,
                charPredicate,
                headerValue);
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
    public final static ContentDispositionParameterName<ContentDispositionFilename> FILENAME = registerConstant("filename",
            ContentDispositionHeaderParser.RFC2045TOKEN,
            ContentDispositionFilenameHeaderValueConverter.INSTANCE);

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
            CharPredicates.digit(),
            HeaderValueConverters.longConverter());

    // factory ......................................................................................................

    /**
     * Factory that creates a {@link ContentDispositionParameterName}. If the {@link #toValue} is not a constant
     * it will return a parameter name with value of type {@link String}.
     */
    public static ContentDispositionParameterName<?> with(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", CharPredicates.rfc2045Token());

        final ContentDispositionParameterName headerValueParameterName = CONSTANTS.get(name);
        return null != headerValueParameterName ?
                headerValueParameterName :
                new ContentDispositionParameterName<String>(name,
                        ContentDispositionHeaderParser.RFC2045TOKEN,
                        HeaderValueConverters.string());
    }

    /**
     * Private constructor use factory.
     */
    private ContentDispositionParameterName(final String name,
                                            final CharPredicate valueCharPredicate,
                                            final HeaderValueConverter<T> valueConverter) {
        super();
        this.name = name;
        this.valueCharPredicate = valueCharPredicate;
        this.valueConverter = valueConverter;
    }

    // Value ....................................................................................................

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    /**
     * Used during parsing to validate characters belonging to a parameter value.
     */
    final CharPredicate valueCharPredicate;

    /**
     * Accepts text and converts it into its value.
     */
    @Override
    public T toValue(final String text) {
        Objects.requireNonNull(text, "text");

        return this.valueConverter.parse(text, this);
    }

    /**
     * Validates the value.
     */
    @Override
    public void checkValue(final Object value) {
        this.valueConverter.check(value);
    }

    private final HeaderValueConverter<T> valueConverter;

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
