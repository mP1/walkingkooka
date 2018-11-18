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

package walkingkooka.net.media;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Whitespace;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * A {@link Value} that represents a MIME Type with possible optional parameters.
 * Note parameter order is not important when comparing for equality or calculating the hash code.
 */
final public class MediaType implements Value<String>, HashCodeEqualsDefined, Serializable {

    /**
     * Wildcard
     */
    public final static String WILDCARD = "*";

    /**
     * The separator character that separates the type and secondary portions within a mime type {@link String}.
     */
    public final static char SEPARATOR = '/';

    /**
     * The separator between parameter name and value.
     */
    public final static char PARAMETER_NAME_VALUE_SEPARATOR = '=';

    /**
     * The separator character that separates parameters from the main mime type
     */
    public final static char PARAMETER_SEPARATOR = ';';

    /**
     * No parameters.
     */
    public final static Map<MediaTypeParameterName, String> NO_PARAMETERS = Maps.empty();

    // MediaType constants.................................................................................................

    /**
     * Holds all constants.
     */
    private final static Map<String, MediaType> CONSTANTS = Maps.sorted();

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
        final String toString = type + SEPARATOR + subType;
        final MediaType mimeType = new MediaType(type, subType, NO_PARAMETERS, toString);
        CONSTANTS.put(toString, mimeType);

        return mimeType;
    }

    // parse ...........................................................................................................

    /**
     * Creates a {@link MediaType} breaking up the {@link String text} into type and sub types, ignoring any optional
     * parameters if they are present.
     */
    public static MediaType parse(final String text) {
        Whitespace.failIfNullOrWhitespace(text, "text");

        int mode = MODE_TYPE;
        int start = 0;
        String type = null;
        String subType = null;
        MediaTypeParameterName parameterName = null;
        Map<MediaTypeParameterName, String> parameters = Maps.ordered();

        int i = 0;
        for (char c : text.toCharArray()) {
            switch (mode) {
                case MODE_TYPE:
                    if (isTokenCharacter(c)) {
                        break;
                    }
                    if (SEPARATOR == c) {
                        type = checkType(token("type", 0, i, text));
                        start = i + 1;
                        mode = MODE_SUBTYPE;
                        break;
                    }
                    failInvalidCharacter(c, i, text);
                    break;
                case MODE_SUBTYPE:
                    if (isTokenCharacter(c)) {
                        break;
                    }
                    if (PARAMETER_SEPARATOR == c) {
                        subType = checkSubType(token("sub type", start, i, text));
                        mode = MODE_PARAMETER_SEPARATOR_WHITESPACE;
                        break;
                    }
                    failInvalidCharacter(c, i, text);
                    break;
                case MODE_PARAMETER_SEPARATOR_WHITESPACE:
                    if (' ' == c) {
                        break;
                    }
                    // end of (optional) leading whitespace must be parameter name.
                    mode = MODE_PARAMETER_NAME;
                    start = i;
                case MODE_PARAMETER_NAME:
                    if (isTokenCharacter(c)) {
                        break;
                    }
                    if (PARAMETER_NAME_VALUE_SEPARATOR == c) {
                        parameterName = MediaTypeParameterName.with(token("parameter name", start, i, text));
                        start = i + 1;
                        mode = MODE_PARAMETER_VALUE_INITIAL;
                        break;
                    }
                    failInvalidCharacter(c, i, text);
                    break;
                case MODE_PARAMETER_VALUE_INITIAL:
                    if ('"' == c) {
                        mode = MODE_PARAMETER_QUOTES;
                        break;
                    }
                    // delibrate fall thru must be a normal parameter value token character.
                case MODE_PARAMETER_VALUE:
                    if (isTokenCharacter(c)) {
                        break;
                    }
                    if (PARAMETER_SEPARATOR == c) {
                        parameters.put(parameterName, token("parameter value", start, i, text));
                        parameterName = null;
                        start = i + 1;
                        mode = MODE_PARAMETER_SEPARATOR_WHITESPACE;
                        break;
                    }
                    failInvalidCharacter(c, i, text);
                    break;
                case MODE_PARAMETER_QUOTES:
                    if ('\\' == c) {
                        mode = MODE_PARAMETER_ESCAPE;
                        break;
                    }
                    if ('"' == c) {
                        parameters.put(parameterName, text.substring(start + 1, i)); // allow empty quoted strings!
                        parameterName = null;
                        start = i + 1;
                        mode = MODE_PARAMETER_SEPARATOR;
                    }
                    break;
                case MODE_PARAMETER_ESCAPE:
                    // only accept proper escaping...
                    if ('\\' == c || '"' == c) {
                        mode = MODE_PARAMETER_QUOTES;
                        break;
                    }
                    failInvalidCharacter(c, i, text);
                    break;
                case MODE_PARAMETER_SEPARATOR:
                    if (PARAMETER_SEPARATOR == c) {
                        mode = MODE_PARAMETER_SEPARATOR_WHITESPACE;
                        break;
                    }
                    failInvalidCharacter(c, i, text);
                    break;
                default:
                    break;
            }

            i++;
        }

        switch (mode) {
            case MODE_TYPE:
                failEmptyToken("sub type", i, text);
            case MODE_SUBTYPE:
                subType = checkSubType(token("sub type", start, i, text));
                break;
            case MODE_PARAMETER_NAME:
                failEmptyToken("parameter value", i, text);
            case MODE_PARAMETER_VALUE_INITIAL:
            case MODE_PARAMETER_VALUE:
                parameters.put(parameterName, token("parameter value", start, i, text));
                break;
            case MODE_PARAMETER_QUOTES:
            case MODE_PARAMETER_ESCAPE:
                final int last = text.length() - 1;
                failInvalidCharacter(text.charAt(last), last, text);
            default:
                break;
        }

        return with(type, subType, parameters, text);
    }

    /**
     * Reports an invalid character within the unparsed media type.
     */
    private static void failInvalidCharacter(final char c, final int i, final String text) {
        throw new IllegalArgumentException(invalidCharacter(c, i, text));
    }

    /**
     * Builds a message to report an invalid or unexpected character.
     */
    static String invalidCharacter(final char c, final int i, final String text) {
        return "Invalid character " + CharSequences.quoteIfChars(c) + " at " + i + " in " + CharSequences.quoteAndEscape(text);
    }

    private static String token(final String tokenName, final int start, final int end, final String text) {
        if (start == end) {
            failEmptyToken(tokenName, end, text);
        }
        return text.substring(start, end);
    }

    /**
     * Reports an invalid character within the unparsed media type.
     */
    private static void failEmptyToken(final String token, final int i, final String text) {
        throw new IllegalArgumentException("Missing " + token + " at " + i + " in " + CharSequences.quoteAndEscape(text));
    }

    /**
     * <a href="https://tools.ietf.org/html/rfc2045#page-5"></a>
     * <pre>
     * token := 1*<any (US-ASCII) CHAR except SPACE, CTLs,
     *                  or tspecials>
     *
     *      tspecials :=  "(" / ")" / "<" / ">" / "@" /
     *                    "," / ";" / ":" / "\" / <">
     *                    "/" / "[" / "]" / "?" / "="
     *                    ; Must be in quoted-string,
     *                    ; to use within parameter values
     * </pre>
     */
    private static boolean isTokenCharacter(final char c) {
        return c > ' ' && c < 127 && false == isTSpecials(c);
    }

    private static boolean isTSpecials(final char c) {
        return "()<>@,;:|\"/[]?=".indexOf(c) != -1;
    }

    private final static int MODE_TYPE = 1;
    private final static int MODE_SUBTYPE = MODE_TYPE + 1;
    private final static int MODE_PARAMETER_SEPARATOR_WHITESPACE = MODE_SUBTYPE + 1;
    private final static int MODE_PARAMETER_NAME = MODE_PARAMETER_SEPARATOR_WHITESPACE + 1;
    private final static int MODE_PARAMETER_VALUE_INITIAL = MODE_PARAMETER_NAME + 1;
    private final static int MODE_PARAMETER_VALUE = MODE_PARAMETER_VALUE_INITIAL + 1;
    private final static int MODE_PARAMETER_QUOTES = MODE_PARAMETER_VALUE + 1;
    private final static int MODE_PARAMETER_ESCAPE = MODE_PARAMETER_QUOTES + 1;
    private final static int MODE_PARAMETER_SEPARATOR = MODE_PARAMETER_ESCAPE + 1;

    /**
     * Creates a {@link MediaType} using the already broken type and sub types. It is not possible to pass parameters with or without values.
     */
    public static MediaType with(final String type, final String subType) {
        checkType(type);
        checkSubType(subType);

        return new MediaType(type, subType, NO_PARAMETERS, type + SEPARATOR + subType);
    }

    /**
     * Factory method called by various setters that tries to match constants before creating an actual new instance.
     */
    // @VisibleForTesting
    static MediaType with(final String type,
                          final String subType,
                          final Map<MediaTypeParameterName, String> parameters,
                          final String toString) {
        final MediaType result = CONSTANTS.get(type + SEPARATOR + subType);
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
                                           final Map<MediaTypeParameterName, String> parameters) {
        return type + SEPARATOR + subType + parameters.entrySet()
                .stream()
                .map(MediaType::toStringParameter)
                .collect(Collectors.joining());
    }

    private static String toStringParameter(final Entry<MediaTypeParameterName, String> nameAndValue) {
        return PARAMETER_SEPARATOR + " " + nameAndValue.getKey().value() + PARAMETER_NAME_VALUE_SEPARATOR + toStringParameterValue(nameAndValue.getValue());
    }

    /**
     * <a href="https://tools.ietf.org/html/rfc1341"></a>
     * <pre>
     * tspecials :=  "(" / ")" / "<" / ">" / "@"  ; Must be in
     *                        /  "," / ";" / ":" / "\" / <">  ; quoted-string,
     *                        /  "/" / "[" / "]" / "?" / "."  ; to use within
     *                        /  "="                        ; parameter values
     * </pre>
     * Backslashes and double quote characters are escaped.
     */
    private static String toStringParameterValue(final String value) {
        StringBuilder b = new StringBuilder();
        b.append('"');
        boolean quoteRequired = false;

        for (char c : value.toCharArray()) {
            if ('\\' == c || '"' == c) {
                b.append('\\');
                b.append(c);
                quoteRequired = true;
                continue;
            }
            if (!isTokenCharacter(c)) {
                b.append(c);
                quoteRequired = true;
                continue;
            }
            b.append(c);
        }

        return quoteRequired ?
                b.append('"').toString() :
                value;
    }

    // ctor ...................................................................................................

    /**
     * Private constructor
     */
    private MediaType(final String type,
                      final String subType,
                      final Map<MediaTypeParameterName, String> parameters,
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
        return this.type.equals(type) ?
                this :
                this.replace(type, this.subType, this.parameters);
    }

    private final transient String type;

    private static String checkType(final String type) {
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
        return this.subType.equals(subType) ?
                this :
                this.replace(this.type, subType, this.parameters);
    }

    private final transient String subType;

    private static String checkSubType(final String subType) {
        return check(subType, "subType");
    }

    /**
     * Checks that the value contains valid token characters.
     */
    static String check(final String value, final String label) {
        CharSequences.failIfNullOrEmpty(value, label);

        int i = 0;
        for (char c : value.toCharArray()) {
            if (!isTokenCharacter(c)) {
                failInvalidCharacter(c, i, value);
            }
            i++;
        }

        return value;
    }

    // parameters ...............................................................................................

    /**
     * Retrieves the parameters.
     */
    public Map<MediaTypeParameterName, String> parameters() {
        return this.parameters;
    }

    public MediaType setParameters(final Map<MediaTypeParameterName, String> parameters) {
        final Map<MediaTypeParameterName, String> copy = checkParameters(parameters);
        return this.parameters.equals(copy) ?
                this :
                this.replace(this.type, this.subType, copy);
    }

    /**
     * Package private for testing.
     */
    private transient final Map<MediaTypeParameterName, String> parameters;

    private static Map<MediaTypeParameterName, String> checkParameters(final Map<MediaTypeParameterName, String> parameters) {
        Objects.requireNonNull(parameters, "parameters");

        final Map<MediaTypeParameterName, String> copy = Maps.ordered();
        copy.putAll(parameters);
        return Maps.readOnly(copy);
    }

    // replace .......................................................................

    private MediaType replace(final String type, final String subType, final Map<MediaTypeParameterName, String> parameters) {
        return new MediaType(type,
                subType,
                parameters,
                toStringMimeType(type, subType, parameters));
    }

    // value ...................................................................

    @Override
    public String value() {
        return this.type + SEPARATOR + this.subType;
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
            final String primary = this.type();
            if (false == WILDCARD.equals(primary)) {
                compatible = primary.equals(mimeType.type);
                if (compatible) {
                    final String subType = this.subType;
                    if (false == WILDCARD.equals(subType)) {
                        compatible = subType.equals(mimeType.subType);
                    }
                }
            }
        }

        return compatible;
    }

    // Object................................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.subType, this.parameters);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof MediaType &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final MediaType other) {
        return this.type.equals(other.type) && //
                this.subType.equals(other.subType) && //
                this.parameters.equals(other.parameters);
    }

    /**
     * Dumps the complete mime type by invoking {@link #value()}.
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
        return parse(this.toString);
    }

    private final static long serialVersionUID = 1L;
}
