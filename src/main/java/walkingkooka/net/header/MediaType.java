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

import walkingkooka.Value;
import walkingkooka.collect.map.Maps;
import walkingkooka.io.serialize.SerializationProxy;
import walkingkooka.net.HasQFactorWeight;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.Whitespace;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;


/**
 * A {@link Value} that represents a MIME Type with possible optional parameters.
 * Note parameter order is not important when comparing for equality or calculating the hash code.
 * Note any suffix that may be present in the sub type is not validated in anyway except for valid characters.
 * <a href="https://en.wikipedia.org/wiki/Media_type"></a>.
 * <br>
 * The type and sub types are case insensitive.
 * <a href="http://www.w3.org/Protocols/rfc1341/4_Content-Type.html"></a>
 * <pre>
 * The type, subtype, and parameter names are not case sensitive. For example, TEXT, Text, and TeXt are all equivalent.
 * Parameter values are normally case sensitive, but certain parameters are interpreted to be case- insensitive,
 * depending on the intended use. (For example, multipart boundaries are case-sensitive, but the "access- type" for
 * message/External-body is not case-sensitive.)
 * </pre>
 */
final public class MediaType extends HeaderValueWithParameters2<MediaType,
        MediaTypeParameterName<?>,
        String>
        implements HasQFactorWeight,
        Predicate<MediaType>,
        Serializable {

    private final static CharPredicate RFC2045TOKEN = CharPredicates.rfc2045Token();

    /**
     * The separator character that separates the type and secondary portions within a mime type {@link String}.
     */
    public final static CharacterConstant TYPE_SUBTYPE_SEPARATOR = CharacterConstant.with('/');

    /**
     * No parameters.
     */
    public final static Map<MediaTypeParameterName<?>, Object> NO_PARAMETERS = Maps.empty();

    // MediaType constants.................................................................................................

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    /**
     * Holds all constants.
     */
    private final static Map<String, MediaType> CONSTANTS = Maps.sorted(CASE_SENSITIVITY.comparator());

    /**
     * Holds a {@link MediaType} that matches all {@link MediaType text types}.
     */
    public final static MediaType ALL = registerConstant("*/*");

    /**
     * Holds a {@link MediaType} for binary.
     */
    public final static MediaType BINARY = registerConstant("application/octet-stream");

    /**
     * Holds a {@link MediaType} that matches all {@link MediaType text types}.
     */
    public final static MediaType ANY_TEXT = registerConstant("text/*");

    /**
     * Holds a {@link MediaType} for plain text.
     */
    public final static MediaType TEXT_PLAIN = registerConstant("text/plain");

    /**
     * Holds a {@link MediaType} for text/richtext
     */
    public final static MediaType TEXT_RICHTEXT = registerConstant("text/richtext");

    /**
     * Holds a {@link MediaType} for HTML text/html
     */
    public final static MediaType TEXT_HTML = registerConstant("text/html");

    /**
     * Holds a {@link MediaType} for XML text/xml
     */
    public final static MediaType TEXT_XML = registerConstant("text/xml");

    /**
     * Holds a {@link MediaType} for MULTIPART BYTE RANGES that contains <code>multipart/byteranges</code>.
     */
    public final static MediaType MULTIPART_BYTE_RANGES = registerConstant("multipart/byteranges");

    /**
     * Holds a {@link MediaType} for MULTIPART FORM DATA that contains <code>multipart/form-data</code>.
     */
    public final static MediaType MULTIPART_FORM_DATA = registerConstant("multipart/form-data");

    /**
     * Holds a {@link MediaType} that matches all {@link MediaType image types}.
     */
    public final static MediaType ANY_IMAGE = registerConstant("image/*");

    /**
     * Holds a {@link MediaType} that contains <code>image/bmp</code>
     */
    public final static MediaType IMAGE_BMP = registerConstant("image/bmp");

    /**
     * Holds a {@link MediaType} that contains <code>image/gif</code>
     */
    public final static MediaType IMAGE_GIF = registerConstant("image/gif");

    /**
     * Holds a {@link MediaType} that contains <code>image/jpeg</code>
     */
    public final static MediaType IMAGE_JPEG = registerConstant("image/jpeg");

    /**
     * Holds a {@link MediaType} that contains <code>image/vnd.microsoft.icon</code>
     */
    public final static MediaType IMAGE_MICROSOFT_ICON = registerConstant("image/vnd.microsoft.icon");
    /**
     * Holds a {@link MediaType} that contains <code>image/png</code>
     */
    public final static MediaType IMAGE_PNG = registerConstant("image/png");

    /**
     * Holds a {@link MediaType} that contains <code>image/text</code>
     */
    public final static MediaType IMAGE_TEXT = registerConstant("image/text");

    /**
     * Holds a {@link MediaType} that contains <code>image/x-bmp</code>
     */
    public final static MediaType IMAGE_XBMP = registerConstant("image/x-bmp");

    /**
     * Holds a {@link MediaType} that contains PDF <code>application/pdf</code>
     */
    public final static MediaType APPLICATION_PDF = registerConstant("application/pdf");

    /**
     * Holds a {@link MediaType} that contains ZIP <code>application/zip</code>
     */
    public final static MediaType APPLICATION_ZIP = registerConstant("application/zip");

    /**
     * Holds a {@link MediaType} that contains EXCEL <code>application/ms-excel</code>
     */
    public final static MediaType APPLICATION_MICROSOFT_EXCEL = registerConstant("application/ms-excel");

    /**
     * Holds a {@link MediaType} that contains EXCEL <code>application/vnd.openxmlformats-officedocument.spreadsheetml.sheet</code>
     */
    public final static MediaType APPLICATION_MICROSOFT_EXCEL_XML = registerConstant("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    /**
     * Holds a {@link MediaType} that contains POWERPOINT <code>application/ms-powerpoint</code>
     */
    public final static MediaType APPLICATION_MICROSOFT_POWERPOINT = registerConstant("application/ms-powerpoint");

    /**
     * Holds a {@link MediaType} that contains WORD <code>application/ms-word</code>
     */
    public final static MediaType APPLICATION_MICROSOFT_WORD = registerConstant("application/ms-word");

    /**
     * Holds a {@link MediaType} that contains OUTLOOK <code>application/vnd.ms-outlook</code>
     */
    public final static MediaType APPLICATION_MICROSOFT_OUTLOOK = registerConstant("application/vnd.ms-outlook");

    /**
     * Holds a {@link MediaType} that contains JAVASCRIPT <code>application/javascript</code>
     */
    public final static MediaType APPLICATION_JAVASCRIPT = registerConstant("application/javascript");

    /**
     * Creates and then registers the constant.
     */
    static private MediaType registerConstant(final String text) {
        final MediaType mimeType = parse(text);
        CONSTANTS.put(text, mimeType);
        return mimeType;
    }

    /**
     * Creates a {@link MediaType} breaking up the {@link String text} into type and sub types, ignoring any optional
     * parameters if they are present.
     */
    public static MediaType parse(final String text) {
        return MediaTypeOneHeaderValueParser.parseMediaType(text);
    }

    /**
     * Creates a charsets of {@link MediaType}. If the text contains a single header type the results of this will be
     * identical to {@link #parse(String)} except the result will be in a charsets, which will also be
     * sorted by the q factor of each header type.
     */
    public static List<MediaType> parseList(final String text) {
        checkText(text);

        return MediaTypeListHeaderValueParser.parseMediaTypeList(text);
    }

    private static void checkText(final String text) {
        Whitespace.failIfNullOrEmptyOrWhitespace(text, "text");
    }

    /**
     * Creates a {@link MediaType} using the already broken type and sub types. It is not possible to pass parameters with or without values.
     */
    public static MediaType with(final String type, final String subType) {
        checkType(type);
        checkSubType(subType);

        return withParameters(type, subType, NO_PARAMETERS);
    }

    /**
     * Factory method called by various setters and parsers, that tries to match constants before creating an actual new
     * instance.
     */
    static MediaType withParameters(final String type,
                                    final String subType,
                                    final Map<MediaTypeParameterName<?>, Object> parameters) {
        final MediaType result = parameters.isEmpty() ?
                CONSTANTS.get(type + TYPE_SUBTYPE_SEPARATOR.character() + subType) :
                null;
        return null != result ?
                result :
                new MediaType(type,
                        subType,
                        parameters);
    }

    // ctor ...................................................................................................

    /**
     * Private constructor
     */
    private MediaType(final String type,
                      final String subType,
                      final Map<MediaTypeParameterName<?>, Object> parameters) {
        super(type + TYPE_SUBTYPE_SEPARATOR.character() + subType, parameters);

        this.type = type;
        this.subType = subType;
    }

    // type .......................................................................................................

    /**
     * Getter that returns the primary component.
     */
    public String type() {
        return this.type;
    }

    /**
     * Would be setter that returns an instance with the new type, creating a new instance if required.
     */
    public MediaType setType(final String type) {
        checkType(type);
        return TYPE_CASE_SENSITIVITY.equals(this.type, type) ?
                this :
                this.replace(type, this.subType, this.parameters);
    }

    private final String type;

    private static String checkType(final String type) {
        return check(type, "type");
    }

    private final static CaseSensitivity TYPE_CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    // sub type ...................................................................................................

    /**
     * Getter that returns the sub component.
     */
    public String subType() {
        return this.subType;
    }

    /**
     * Would be setter that returns an instance with the new subType, creating a new instance if required.
     */
    public MediaType setSubType(final String subType) {
        checkSubType(subType);
        return SUBTYPE_CASE_SENSITIVITY.equals(this.subType, subType) ?
                this :
                this.replace(this.type, subType, this.parameters);
    }

    private final String subType;

    private static String checkSubType(final String subType) {
        return check(subType, "subType");
    }

    /**
     * Checks that the value contains valid token characters.
     */
    private static String check(final String value, final String label) {
        CharPredicates.failIfNullOrEmptyOrFalse(value, label, RFC2045TOKEN);
        return value;
    }

    private final static CaseSensitivity SUBTYPE_CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    // parameters ...............................................................................................

    /**
     * Retrieves the q-weight for this value.
     */
    public final Optional<Float> qFactorWeight() {
        return this.qFactorWeight(MediaTypeParameterName.Q_FACTOR);
    }

    // replace .......................................................................

    @Override
    MediaType replace(final Map<MediaTypeParameterName<?>, Object> parameters) {
        return this.replace(this.type,
                this.subType,
                parameters);
    }

    private MediaType replace(final String type, final String subType, final Map<MediaTypeParameterName<?>, Object> parameters) {
        return withParameters(type,
                subType,
                parameters);
    }

    // setCharset .................................................................................................

    /**
     * Returns a {@link MediaType} with the given charset creating a new instance if necessary.
     */
    public MediaType setCharset(final CharsetName charset) {
        Objects.requireNonNull(charset, "charset");

        return charset.equals(this.parameters.get(MediaTypeParameterName.CHARSET)) ?
                this :
                this.setCharset0(charset);
    }

    private MediaType setCharset0(final CharsetName charset) {
        Objects.requireNonNull(charset, "charset");

        final Map<MediaTypeParameterName<?>, Object> parameters = Maps.ordered();
        parameters.putAll(this.parameters);
        parameters.put(MediaTypeParameterName.CHARSET, charset);

        return this.replace(this.type,
                this.subType,
                Maps.readOnly(parameters));
    }

    // misc .......................................................................

    /**
     * Tests if the given {@link MediaType} is compatible and understand wildcards that may appear in the type or sub type components. The
     * {@link MediaType#ALL} will of course be compatible with any other {@link MediaType}.
     */
    @Override
    public boolean test(final MediaType mediaType) {
        Objects.requireNonNull(mediaType, "mimeType");

        boolean compatible = true;

        if (this != mediaType) {
            final String type = this.type();
            if (false == WILDCARD.string().equals(type)) {
                compatible = TYPE_CASE_SENSITIVITY.equals(type, mediaType.type);
                if (compatible) {
                    final String subType = this.subType;
                    if (false == WILDCARD.string().equals(subType)) {
                        compatible = SUBTYPE_CASE_SENSITIVITY.equals(subType, mediaType.subType);
                    }
                }
            }
        }

        return compatible;
    }

    // HeaderValue................................................................................................................

    @Override
    String toHeaderTextValue() {
        return this.value;
    }

    @Override
    String toHeaderTextParameterSeparator() {
        return TO_HEADERTEXT_PARAMETER_SEPARATOR;
    }

    @Override
    public boolean isWildcard() {
        return ALL == this;
    }

    // HasHeaderScope ....................................................................................................

    final static boolean IS_MULTIPART = true;

    @Override
    public final boolean isMultipart() {
        return IS_MULTIPART;
    }

    final static boolean IS_REQUEST = true;

    @Override
    public final boolean isRequest() {
        return IS_REQUEST;
    }

    final static boolean IS_RESPONSE = true;

    @Override
    public final boolean isResponse() {
        return IS_RESPONSE;
    }

    // Object................................................................................................................

    @Override
    int hashCode0(final String value) {
        return CASE_SENSITIVITY.hash(value);
    }

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof MediaType;
    }

    @Override
    boolean equals1(final String value, final String otherValue) {
        return CASE_SENSITIVITY.equals(this.value, otherValue);
    }

    // Serializable.....................................................................................................

    /**
     * Returns either of the two {@link SerializationProxy}
     */
    // @VisibleForTesting
    final Object writeReplace() {
        return MediaTypeSerializationProxy.with(this);
    }

    /**
     * Should never be called expect a serialization proxy
     */
    private Object readResolve() {
        throw new UnsupportedOperationException();
    }

    private final static long serialVersionUID = 1L;
}
