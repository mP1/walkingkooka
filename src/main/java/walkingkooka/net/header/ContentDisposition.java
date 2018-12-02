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
import walkingkooka.NeverError;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.build.tostring.UsesToStringBuilder;
import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Represents a content disposition header and its component values.<br>
 * <a href="https://en.wikipedia.org/wiki/MIME#Content-Disposition"></a>
 */
public final class ContentDisposition implements HeaderValueWithParameters<ContentDispositionParameterName<?>>,
        UsesToStringBuilder {

    /**
     * A constants with no parameters.
     */
    public final static Map<ContentDispositionParameterName<?>, Object> NO_PARAMETERS = Maps.empty();

    /**
     * The separator between parameter name and value.
     */
    public final static CharacterConstant PARAMETER_NAME_VALUE_SEPARATOR = CharacterConstant.with('=');

    /**
     * The separator character that separates multiple parameters.
     */
    public final static CharacterConstant SEPARATOR = CharacterConstant.with(';');

    /**
     * Parses a header value into tokens, which aill also be sorted using their q factor weights.
     * <pre>
     * Content-Disposition: inline
     * Content-Disposition: attachment
     * Content-Disposition: attachment; filename="filename.jpg"
     * ...
     * Content-Disposition: form-data
     * Content-Disposition: form-data; name="fieldName"
     * Content-Disposition: form-data; name="fieldName"; filename="filename.jpg"
     * </pre>
     */
    public static ContentDisposition parse(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        final char separator = SEPARATOR.character();
        final char parameterNameValueSeparator = PARAMETER_NAME_VALUE_SEPARATOR.character();

        ContentDisposition content = null;

        int mode = MODE_TYPE;
        int start = 0;

        ContentDispositionType type = null;
        ContentDispositionParameterName<?> parameterName = null;
        Map<ContentDispositionParameterName<?>, Object> parameters = NO_PARAMETERS;

        final int length = text.length();
        int i = 0;
        while (i < length) {
            final char c = text.charAt(i);

            switch (mode) {
                case MODE_TYPE:
                    if (TOKEN.test(c)) {
                        break;
                    }
                    if (separator == c) {
                        type = type(text, start, i);
                        parameters = Maps.ordered();
                        mode = MODE_PARAMETER_SEPARATOR_WHITESPACE;
                        start = i + 1;
                        break;
                    }
                    if (WHITESPACE.test(c)) {
                        type = type(text, start, i);
                        parameters = Maps.ordered();
                        mode = MODE_TYPE_WHITESPACE;
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_TYPE_WHITESPACE:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    mode = MODE_PARAMETER_SEPARATOR;
                    start = i;
                    // intentional fall thru...
                case MODE_PARAMETER_SEPARATOR:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    if (separator == c) {
                        content = new ContentDisposition(type, ContentDisposition.NO_PARAMETERS);
                        mode = MODE_PARAMETER_SEPARATOR_WHITESPACE;
                        start = i + 1;
                        break;
                    }
                    mode = MODE_PARAMETER_NAME;
                    start = i;
                    i--; // try char again as NAME
                    break;
                case MODE_PARAMETER_SEPARATOR_WHITESPACE:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    mode = MODE_PARAMETER_NAME;
                    start = i;
                    // intentional fall thru...
                case MODE_PARAMETER_NAME:
                    if (TOKEN.test(c)) {
                        break;
                    }
                    if (parameterNameValueSeparator == c) {
                        parameterName = parameterName(text, start, i);
                        mode = MODE_PARAMETER_EQUALS_WHITESPACE;
                        break;
                    }
                    if (WHITESPACE.test(c)) {
                        parameterName = parameterName(text, start, i);
                        mode = MODE_PARAMETER_NAME_WHITESPACE;
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_PARAMETER_NAME_WHITESPACE:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    if (parameterNameValueSeparator == c) {
                        mode = MODE_PARAMETER_EQUALS_WHITESPACE;
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_PARAMETER_EQUALS_WHITESPACE:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    if (DOUBLE_QUOTE == c) {
                        start = i + 1;
                        mode = MODE_PARAMETER_QUOTED_VALUE;
                        break;
                    }
                    mode = MODE_PARAMETER_VALUE;
                    start = i;
                    // fall thru intentional
                case MODE_PARAMETER_VALUE:
                    // allow any valid token character...
                    if (parameterName.testValueCharacter(c)) {
                        break;
                    }
                    if (WHITESPACE.test(c)) {
                        addParameterValue(text, start, i, parameterName, parameters);
                        mode = MODE_PARAMETER_VALUE_WHITESPACE;
                        start = i + 1;
                        break;
                    }
                    if (separator == c) {
                        addParameterValue(text, start, i, parameterName, parameters);
                        content = new ContentDisposition(type, parameters);
                        mode = MODE_PARAMETER_SEPARATOR_WHITESPACE;
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_PARAMETER_QUOTED_VALUE:
                    if (DOUBLE_QUOTE == c) {
                        // without the enclosing quotes
                        addParameterValue(text, start, i, parameterName, parameters);
                        mode = MODE_PARAMETER_VALUE_WHITESPACE;
                        break;
                    }
                    if (parameterName.testValueCharacter(c)) {
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_PARAMETER_VALUE_WHITESPACE:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    if (separator == c) {
                        mode = MODE_PARAMETER_SEPARATOR_WHITESPACE;
                        start = i + 1;
                        break;
                    }
                    failInvalidCharacter(i, text);
                default:
                    NeverError.unhandledCase(mode,
                            MODE_TYPE,
                            MODE_TYPE_WHITESPACE,
                            MODE_PARAMETER_SEPARATOR,
                            MODE_PARAMETER_SEPARATOR_WHITESPACE,
                            MODE_PARAMETER_NAME,
                            MODE_PARAMETER_NAME_WHITESPACE,
                            MODE_PARAMETER_EQUALS_WHITESPACE,
                            MODE_PARAMETER_VALUE,
                            MODE_PARAMETER_QUOTED_VALUE,
                            MODE_PARAMETER_VALUE_WHITESPACE);
            }

            i++;
        }

        switch (mode) {
            case MODE_TYPE:
                content = new ContentDisposition(type(text, start, i), ContentDisposition.NO_PARAMETERS);
                break;
            case MODE_TYPE_WHITESPACE:
            case MODE_PARAMETER_SEPARATOR:
            case MODE_PARAMETER_SEPARATOR_WHITESPACE:
                content = new ContentDisposition(type, parameters);
                break;
            case MODE_PARAMETER_NAME:
            case MODE_PARAMETER_NAME_WHITESPACE:
            case MODE_PARAMETER_EQUALS_WHITESPACE:
                parameterValue(text, i, i);
            case MODE_PARAMETER_VALUE:
                addParameterValue(text, start, i, parameterName, parameters);
                content = new ContentDisposition(type, parameters);
                break;
            case MODE_PARAMETER_QUOTED_VALUE:
                throw new IllegalArgumentException("Missing closing quote " + CharSequences.quoteIfChars(DOUBLE_QUOTE) +
                        " from " + CharSequences.quote(text));
            case MODE_PARAMETER_VALUE_WHITESPACE:
                content = new ContentDisposition(type, parameters);
                break;
            default:
                break;
        }

        return content;
    }

    private final static int MODE_TYPE = 1;
    private final static int MODE_TYPE_WHITESPACE = MODE_TYPE + 1;
    private final static int MODE_PARAMETER_SEPARATOR = MODE_TYPE_WHITESPACE + 1;
    private final static int MODE_PARAMETER_SEPARATOR_WHITESPACE = MODE_PARAMETER_SEPARATOR + 1;
    private final static int MODE_PARAMETER_NAME = MODE_PARAMETER_SEPARATOR_WHITESPACE + 1;
    private final static int MODE_PARAMETER_NAME_WHITESPACE = MODE_PARAMETER_NAME + 1;
    private final static int MODE_PARAMETER_EQUALS_WHITESPACE = MODE_PARAMETER_NAME_WHITESPACE + 1;
    private final static int MODE_PARAMETER_VALUE = MODE_PARAMETER_EQUALS_WHITESPACE + 1;
    private final static int MODE_PARAMETER_QUOTED_VALUE = MODE_PARAMETER_VALUE + 1;
    private final static int MODE_PARAMETER_VALUE_WHITESPACE = MODE_PARAMETER_QUOTED_VALUE + 1;

    private final static String TYPE = "type";
    final static String PARAMETER_NAME = "parameter name";
    final static String PARAMETER_VALUE = "parameter value";

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
    final static CharPredicate DATE = CharPredicates.builder()
            .any("MonTuesWedThuFriSatSun,JanFebMarAprMayJunJulAugSepOctNovDec 0123456789:+-")
            .build()
            .setToString("RFC822 Date");

    /**
     * Only allow digits in a SIZE parameter value.
     */
    final static CharPredicate DIGIT = CharPredicates.digit();

    private final static char DOUBLE_QUOTE = '"';
    /**
     * <a href="https://tools.ietf.org/html/rfc2183"></a>
     * <pre>
     *    In the extended BNF notation of [RFC 822], the Content-Disposition
     *    header field is defined as follows:
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
     *
     *
     *
     *    NOTE ON PARAMETER VALUE LENGHTS: A short (length <= 78 characters)
     *    parameter value containing only non-`tspecials' characters SHOULD be
     *    represented as a single `token'.  A short parameter value containing
     *    only ASCII characters, but including `tspecials' characters, SHOULD
     *    be represented as `quoted-string'.  Parameter values longer than 78
     *    characters, or which contain non-ASCII characters, MUST be encoded as
     *    specified in [RFC 2184].
     *
     *    `Extension-token', `parameter', `tspecials' and `value' are defined
     *    according to [RFC 2045] (which references [RFC 822] in the definition
     *    of some of these tokens).  `quoted-string' and `DIGIT' are defined in
     *    [RFC 822].
     * </pre>
     */
    final static CharPredicate TOKEN = CharPredicates.rfc2045Token();

    private final static CharPredicate WHITESPACE = CharPredicates.any("\u0009\u0020")
            .setToString("SP|HTAB");

    /**
     * Reports an invalid character within the unparsed text.
     */
    private static void failInvalidCharacter(final int i, final String text) {
        throw new IllegalArgumentException(invalidCharacter(i, text));
    }

    /**
     * Builds a message to report an invalid or unexpected character.
     */
    static String invalidCharacter(final int i, final String text) {
        return "Invalid character " + CharSequences.quoteIfChars(text.charAt(i)) + " at " + i + " in " + CharSequences.quoteAndEscape(text);
    }

    /**
     * Creates a {@link ContentDispositionType} from the token.
     */
    private static ContentDispositionType type(final String text, final int start, final int end) {
        return ContentDispositionType.with(token(TYPE, text, start, end));
    }

    /**
     * Creates a {@link ContentDispositionParameterName} from the token.
     */
    private static ContentDispositionParameterName<?> parameterName(final String text, final int start, final int end) {
        return ContentDispositionParameterName.with(token(PARAMETER_NAME, text, start, end));
    }

    /**
     * Extracts, converts and adds the given parameter pair to other parameters.
     */
    private static void addParameterValue(
            final String text,
            final int start,
            final int end,
            final ContentDispositionParameterName<?> parameterName,
            final Map<ContentDispositionParameterName<?>, Object> parameters) {
        parameters.put(parameterName, parameterName.toValue(parameterValue(text, start, end)));
    }

    /**
     * Extracts the parameter value token.
     */
    private static String parameterValue(final String text, final int start, final int end) {
        return token(PARAMETER_VALUE, text, start, end);
    }

    /**
     * Extracts the token failing if it is empty.
     */
    private static String token(final String tokenName, final String text, final int start, final int end) {
        if (start == end) {
            failEmptyToken(tokenName, end, text);
        }
        return text.substring(start, end);
    }

    /**
     * Reports an empty token.
     */
    private static void failEmptyToken(final String token, final int i, final String text) {
        throw new IllegalArgumentException(emptyToken(token, i, text));
    }

    /**
     * The message when a token is empty.
     */
    static String emptyToken(final String token, final int i, final String text) {
        return "Missing " + token + " at " + i + " in " + CharSequences.quoteAndEscape(text);
    }

    /**
     * Factory that creates a new {@link ContentDisposition}
     */
    public static ContentDisposition with(final ContentDispositionType type,
                                          final Map<ContentDispositionParameterName<?>, Object> parameters) {
        checkType(type);

        return new ContentDisposition(type, checkParameters(parameters));
    }

    /**
     * Private ctor use factory
     */
    private ContentDisposition(final ContentDispositionType type, final Map<ContentDispositionParameterName<?>, Object> parameters) {
        super();
        this.type = type;
        this.parameters = parameters;
    }

    // type .....................................................

    /**
     * Getter that retrieves the type
     */
    public ContentDispositionType type() {
        return this.type;
    }

    /**
     * Would be setter that returns a {@link ContentDisposition} with the given type creating a new instance if necessary.
     */
    public ContentDisposition setType(final ContentDispositionType type) {
        checkType(type);
        return this.type.equals(type) ?
                this :
                this.replace(type, this.parameters);
    }

    private final ContentDispositionType type;

    private static void checkType(final ContentDispositionType type) {
        Objects.requireNonNull(type, "type");
    }

    // parameters.........................................................................................

    /**
     * A map view of all parameters to their text or string value.
     */
    @Override
    public Map<ContentDispositionParameterName<?>, Object> parameters() {
        return this.parameters;
    }

    @Override
    public ContentDisposition setParameters(final Map<ContentDispositionParameterName<?>, Object> parameters) {
        final Map<ContentDispositionParameterName<?>, Object> copy = checkParameters(parameters);
        return this.parameters.equals(copy) ?
                this :
                this.replace(this.type, copy);
    }

    private final Map<ContentDispositionParameterName<?>, Object> parameters;

    /**
     * Makes a copy of the parameters and also checks the value.
     */
    private static Map<ContentDispositionParameterName<?>, Object> checkParameters(final Map<ContentDispositionParameterName<?>, Object> parameters) {
        final Map<ContentDispositionParameterName<?>, Object> copy = Maps.ordered();
        for(Entry<ContentDispositionParameterName<?>, Object> nameAndValue  : parameters.entrySet()) {
            final ContentDispositionParameterName name = nameAndValue.getKey();
            final Object value = nameAndValue.getValue();
            name.checkValue(value);
            copy.put(name, value);
        }
        return copy;
    }

    // replace ...........................................................................................................

    private ContentDisposition replace(final ContentDispositionType type, final Map<ContentDispositionParameterName<?>, Object> parameters) {
        return new ContentDisposition(type, parameters);
    }

    // HeaderValue.................................................................

    @Override
    public String headerValue() {
        return this.toString();
    }

    // Object .............................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.parameters);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof ContentDisposition &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final ContentDisposition other) {
        return this.type.equals(other.type) &&
                this.parameters.equals(other.parameters);
    }

    /**
     * Dumps the raw header value without quotes.
     */
    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    @Override
    public void buildToString(final ToStringBuilder builder) {
        builder.disable(ToStringBuilderOption.QUOTE);
        builder.value(this.type);

        builder.separator(TO_STRING_PARAMETER_SEPARATOR);
        builder.valueSeparator(TO_STRING_PARAMETER_SEPARATOR);
        builder.labelSeparator(PARAMETER_NAME_VALUE_SEPARATOR.string());
        builder.value(this.parameters);
    }

    /**
     * Separator between parameters used by {@link #toString()}.
     */
    private final static String TO_STRING_PARAMETER_SEPARATOR = SEPARATOR.string().concat(" ");
}
