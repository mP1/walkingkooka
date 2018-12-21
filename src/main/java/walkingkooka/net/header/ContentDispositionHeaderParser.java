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

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

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
 *    NOTE ON PARAMETER MODE_VALUE LENGHTS: A short (length <= 78 characters)
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
final class ContentDispositionHeaderParser extends HeaderParserWithParameters<ContentDisposition,
        ContentDispositionParameterName<?>> {

    static ContentDisposition parseContentDisposition(final String text) {
        final ContentDispositionHeaderParser parser = new ContentDispositionHeaderParser(text);
        parser.parse();
        return parser.disposition;
    }

    private ContentDispositionHeaderParser(final String text) {
        super(text);
    }


    @Override
    boolean allowMultipleValues() {
        return true;
    }

    @Override
    ContentDisposition wildcardValue() {
        return this.failInvalidCharacter();
    }

    @Override
    ContentDisposition value() {
        return ContentDisposition.with(this.token(RFC2045TOKEN, ContentDispositionType::with));
    }

    @Override
    void missingValue() {
        this.failMissingValue(TYPE);
    }

    final static String TYPE = "type";

    @Override
    ContentDispositionParameterName<?> parameterName() {
        return this.token(PARAMETER_NAME, ContentDispositionParameterName::with);
    }

    final static CharPredicate PARAMETER_NAME = RFC2045TOKEN;

    @Override
    String quotedParameterValue(final ContentDispositionParameterName<?> parameterName) {
        return this.quotedText(QUOTED_PARAMETER_VALUE, true);
    }

    final static CharPredicate QUOTED_PARAMETER_VALUE = CharPredicates.ascii();

    @Override
    String unquotedParameterValue(final ContentDispositionParameterName<?> parameterName) {
        return this.token(UNQUOTED_PARAMETER_VALUE);
    }

    final static CharPredicate UNQUOTED_PARAMETER_VALUE = RFC2045TOKEN;

    @Override
    void valueComplete(final ContentDisposition disposition) {
        this.disposition = disposition;
    }

    ContentDisposition disposition;
}
