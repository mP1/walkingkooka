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
import walkingkooka.Value;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.HasQFactorWeight;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.Whitespace;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


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
final public class MediaType implements Value<String>,
        HeaderValueWithParameters<MediaTypeParameterName<?>>,
        HasQFactorWeight,
        Serializable {

    /**
     * Wildcard
     */
    public final static String WILDCARD = "*";

    /**
     * The separator character that separates the type and secondary portions within a mime type {@link String}.
     */
    public final static char TYPE_SUBTYPE_SEPARATOR = '/';

    /**
     * The separator between parameter name and value.
     */
    public final static char PARAMETER_NAME_VALUE_SEPARATOR = '=';

    /**
     * The separator character that separates parameters from the main mime type
     */
    public final static char PARAMETER_SEPARATOR = ';';

    /**
     * The separator character that separates header types within a header value.
     */
    public final static char MEDIATYPE_SEPARATOR = ',';

    /**
     * No parameters.
     */
    public final static Map<MediaTypeParameterName<?>, Object> NO_PARAMETERS = Maps.empty();

    // MediaType constants.................................................................................................

    /**
     * Holds all constants.
     */
    private final static Map<String, MediaType> CONSTANTS = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

    /**
     * Holds a {@link MediaType} that matches all {@link MediaType text types}.
     */
    public final static MediaType WILDCARD_WILDCARD = registerConstant(WILDCARD, WILDCARD);

    /**
     * Holds a {@link MediaType} for binary.
     */
    public final static MediaType BINARY = registerConstant("application", "octet-stream");

    /**
     * Holds a {@link MediaType} that matches all {@link MediaType text types}.
     */
    public final static MediaType ANY_TEXT = registerConstant("text", WILDCARD);

    /**
     * Holds a {@link MediaType} for plain text.
     */
    public final static MediaType TEXT_PLAIN = registerConstant("text", "plain");

    /**
     * Holds a {@link MediaType} for text/richtext
     */
    public final static MediaType TEXT_RICHTEXT = registerConstant("text", "richtext");

    /**
     * Holds a {@link MediaType} for HTML text/html
     */
    public final static MediaType TEXT_HTML = registerConstant("text", "html");

    /**
     * Holds a {@link MediaType} for XML text/xml
     */
    public final static MediaType TEXT_XML = registerConstant("text", "xml");

    /**
     * Holds a {@link MediaType} for MIME MULTIPART FORM DATA that contains <code>multipart/form-data</code>.
     */
    public final static MediaType MIME_MULTIPART_FORM_DATA = registerConstant("multipart", "form-data");

    /**
     * Holds a {@link MediaType} that matches all {@link MediaType image types}.
     */
    public final static MediaType ANY_IMAGE = registerConstant("image", WILDCARD);

    /**
     * Holds a {@link MediaType} that contains <code>image/bmp</code>
     */
    public final static MediaType IMAGE_BMP = registerConstant("image", "bmp");

    /**
     * Holds a {@link MediaType} that contains <code>image/gif</code>
     */
    public final static MediaType IMAGE_GIF = registerConstant("image", "gif");

    /**
     * Holds a {@link MediaType} that contains <code>image/jpeg</code>
     */
    public final static MediaType IMAGE_JPEG = registerConstant("image", "jpeg");

    /**
     * Holds a {@link MediaType} that contains <code>image/vnd.microsoft.icon</code>
     */
    public final static MediaType IMAGE_MICROSOFT_ICON = registerConstant("image", "vnd.microsoft.icon");
    /**
     * Holds a {@link MediaType} that contains <code>image/png</code>
     */
    public final static MediaType IMAGE_PNG = registerConstant("image", "png");

    /**
     * Holds a {@link MediaType} that contains <code>image/text</code>
     */
    public final static MediaType IMAGE_TEXT = registerConstant("image", "text");

    /**
     * Holds a {@link MediaType} that contains <code>image/x-bmp</code>
     */
    public final static MediaType IMAGE_XBMP = registerConstant("image", "x-bmp");
    /**
     * Holds a {@link MediaType} that contains PDF <code>application/pdf</code>
     */
    public final static MediaType APPLICATION_PDF = registerConstant("application", "pdf");

    /**
     * Holds a {@link MediaType} that contains ZIP <code>application/zip</code>
     */
    public final static MediaType APPLICATION_ZIP = registerConstant("application", "zip");

    /**
     * Holds a {@link MediaType} that contains EXCEL <code>application/ms-excel</code>
     */
    public final static MediaType APPLICATION_MICROSOFT_EXCEL = registerConstant("application", "ms-excel");

    /**
     * Holds a {@link MediaType} that contains EXCEL <code>application/vnd.openxmlformats-officedocument.spreadsheetml.sheet</code>
     */
    public final static MediaType APPLICATION_MICROSOFT_EXCEL_XML = registerConstant(
            "application",
            "vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    /**
     * Holds a {@link MediaType} that contains POWERPOINT <code>application/ms-powerpoint</code>
     */
    public final static MediaType APPLICATION_MICROSOFT_POWERPOINT = registerConstant("application", "ms-powerpoint");

    /**
     * Holds a {@link MediaType} that contains WORD <code>application/ms-word</code>
     */
    public final static MediaType APPLICATION_MICROSOFT_WORD = registerConstant("application", "ms-word");

    /**
     * Holds a {@link MediaType} that contains OUTLOOK <code>application/vnd.ms-outlook</code>
     */
    public final static MediaType APPLICATION_MICROSOFT_OUTLOOK = registerConstant("application", "vnd.ms-outlook");

    /**
     * Holds a {@link MediaType} that contains JAVASCRIPT <code>application/javascript</code>
     */
    public final static MediaType APPLICATION_JAVASCRIPT = registerConstant("application", "javascript");

    /**
     * Creates and then registers the constant.
     */
    static private MediaType registerConstant(final String type, final String subType) {
        final String toString = type + TYPE_SUBTYPE_SEPARATOR + subType;
        final MediaType mimeType = new MediaType(type, subType, NO_PARAMETERS, toString);
        CONSTANTS.put(toString, mimeType);

        return mimeType;
    }

    /**
     * Creates a {@link MediaType} breaking up the {@link String text} into type and sub types, ignoring any optional
     * parameters if they are present.
     */
    public static MediaType parse(final String text) {
        checkText(text);

        return MediaTypeHeaderParserOne.parseMediaType(text);
    }


    /**
     * Creates a list of {@link MediaType}. If the text contains a single header type the results of this will be
     * identical to {@link #parse(String)} except the result will be in a list, which will also be
     * sorted by the q factor of each header type.
     */
    public static List<MediaType> parseList(final String text) {
        checkText(text);

        return MediaTypeHeaderParserList.parseMediaTypeList(text);
    }

    private static void checkText(final String text) {
        Whitespace.failIfNullOrWhitespace(text, "text");
    }

    /**
     * Creates a {@link MediaType} using the already broken type and sub types. It is not possible to pass parameters with or without values.
     */
    public static MediaType with(final String type, final String subType) {
        checkType(type);
        checkSubType(subType);

        final String toString = type + TYPE_SUBTYPE_SEPARATOR + subType;
        final MediaType mediaType = CONSTANTS.get(toString);
        return null != mediaType ?
                mediaType :
                new MediaType(type, subType, NO_PARAMETERS, toString);
    }

    /**
     * Factory method called by various setters and parsers, that tries to match constants before creating an actual new
     * instance.
     */
    static MediaType withParameters(final String type,
                                    final String subType,
                                    final Map<MediaTypeParameterName<?>, Object> parameters,
                                    final String toString) {
        final MediaType result = parameters.isEmpty() ?
                CONSTANTS.get(type + TYPE_SUBTYPE_SEPARATOR + subType) :
                null;
        return null != result ?
                result :
                new MediaType(type,
                        subType,
                        parameters,
                        toString);
    }

    /**
     * Recomputes the toString for a {@link MediaType} from its components.
     */
    private static String toStringMimeType(final String type,
                                           final String subType,
                                           final Map<MediaTypeParameterName<?>, Object> parameters) {
        return type +
                TYPE_SUBTYPE_SEPARATOR +
                subType +
                parameters.entrySet()
                        .stream()
                        .map(MediaType::toStringParameter)
                        .collect(Collectors.joining());
    }

    private static String toStringParameter(final Entry<MediaTypeParameterName<?>, Object> nameAndValue) {
        final MediaTypeParameterName<?> name = nameAndValue.getKey();
        return PARAMETER_SEPARATOR +
                " " +
                name.value() +
                PARAMETER_NAME_VALUE_SEPARATOR +
                name.valueConverter.format(Cast.to(nameAndValue.getValue()), name);
    }

    /**
     * Formats or converts a list of media types back to a String. Basically
     * an inverse of {@link #parseList(String)}.
     */
    public static String format(final List<MediaType> mediaTypes) {
        Objects.requireNonNull(mediaTypes, "mediaTypes");

        return mediaTypes.stream()
                .map(m -> m.toString())
                .collect(Collectors.joining(TOSTRING_MEDIATYPE_SEPARATOR));
    }

    private final static String TOSTRING_MEDIATYPE_SEPARATOR = MEDIATYPE_SEPARATOR + " ";

    // ctor ...................................................................................................

    /**
     * Private constructor
     */
    private MediaType(final String type,
                      final String subType,
                      final Map<MediaTypeParameterName<?>, Object> parameters,
                      final String toString) {
        super();

        this.type = type;
        this.subType = subType;
        this.parameters = parameters;
        this.toString = toString;
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
        return this.type.equalsIgnoreCase(type) ?
                this :
                this.replace(type, this.subType, this.parameters);
    }

    private final transient String type;

    static String checkType(final String type) {
        return check(type, "type");
    }

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
        return this.subType.equalsIgnoreCase(subType) ?
                this :
                this.replace(this.type, subType, this.parameters);
    }

    private final transient String subType;

    static String checkSubType(final String subType) {
        return check(subType, "subType");
    }

    /**
     * Checks that the value contains valid token characters.
     */
    static String check(final String value, final String label) {
        CharPredicates.failIfNullOrEmptyOrFalse(value, label, MediaTypeHeaderParser.RFC2045TOKEN);
        return value;
    }

    // parameters ...............................................................................................

    /**
     * Retrieves the parameters.
     */
    @Override
    public Map<MediaTypeParameterName<?>, Object> parameters() {
        return this.parameters;
    }

    @Override
    public MediaType setParameters(final Map<MediaTypeParameterName<?>, Object> parameters) {
        final Map<MediaTypeParameterName<?>, Object> copy = checkParameters(parameters);
        return this.parameters.equals(copy) ?
                this :
                this.replace(this.type, this.subType, copy);
    }

    /**
     * Package private for testing.
     */
    private transient final Map<MediaTypeParameterName<?>, Object> parameters;

    /**
     * While checking the parameters (name and value) makes a defensive copy.
     */
    private static Map<MediaTypeParameterName<?>, Object> checkParameters(final Map<MediaTypeParameterName<?>, Object> parameters) {
        Objects.requireNonNull(parameters, "parameters");

        final Map<MediaTypeParameterName<?>, Object> copy = Maps.sorted();
        for(Entry<MediaTypeParameterName<?>, Object> nameAndValue  : parameters.entrySet()) {
            final MediaTypeParameterName name = nameAndValue.getKey();
            final Object value = nameAndValue.getValue();
            name.checkValue(value);
            copy.put(name, value);
        }
        return copy;
    }

    // replace .......................................................................

    private MediaType replace(final String type, final String subType, final Map<MediaTypeParameterName<?>, Object> parameters) {
        return withParameters(type,
                subType,
                parameters,
                toStringMimeType(type, subType, parameters));
    }

    // value ...................................................................

    @Override
    public String value() {
        return this.type + TYPE_SUBTYPE_SEPARATOR + this.subType;
    }

    // qWeight ...................................................................

    /**
     * Retrieves the q-weight for this value. If the value is not a number a {@link IllegalStateException} will be thrown.
     */
    public Optional<Float> qFactorWeight() {
        return Optional.ofNullable(Float.class.cast(this.parameters()
                .get(MediaTypeParameterName.Q_FACTOR)));
    }

    // misc .......................................................................

    /**
     * Tests if the given {@link MediaType} is compatible and understand wildcards that may appear in the type or sub type components. The
     * {@link MediaType#WILDCARD_WILDCARD} will of course be compatible with any other {@link MediaType}.
     */
    public boolean isCompatible(final MediaType mimeType) {
        Objects.requireNonNull(mimeType, "mimeType");

        boolean compatible = true;

        if (this != mimeType) {
            final String type = this.type();
            if (false == WILDCARD.equals(type)) {
                compatible = type.equalsIgnoreCase(mimeType.type);
                if (compatible) {
                    final String subType = this.subType;
                    if (false == WILDCARD.equals(subType)) {
                        compatible = subType.equalsIgnoreCase(mimeType.subType);
                    }
                }
            }
        }

        return compatible;
    }

    // HeaderValue................................................................................................................

    @Override
    public String headerValue() {
        return this.toString();
    }

    // Object................................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(CaseSensitivity.INSENSITIVE.hash(this.type),
                CaseSensitivity.INSENSITIVE.hash(this.subType),
                this.parameters);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof MediaType &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final MediaType other) {
        return this.type.equalsIgnoreCase(other.type) && //
                this.subType.equalsIgnoreCase(other.subType) && //
                this.parameters.equals(other.parameters);
    }

    /**
     * If sourced or created by parsing, the original text is returned, if built using setters a toString is constructed.
     */
    @Override
    public String toString() {
        return this.toString;
    }

    private final String toString;

    // Serializable............................................................................

    /**
     * Only the {@link #toString()} is serialized thus on deserialization we need to parse to reconstruct other fields.
     */
    private Object readResolve() {
        return MediaTypeHeaderParserOne.parseMediaType(this.toString);
    }

    private final static long serialVersionUID = 1L;
}
