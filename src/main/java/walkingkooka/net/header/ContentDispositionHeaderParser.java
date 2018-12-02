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
import walkingkooka.text.CharSequences;

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
final class ContentDispositionHeaderParser extends HeaderParser<ContentDispositionParameterName<?>>{

    static ContentDisposition parse(final String text) {
        final ContentDispositionHeaderParser parser = new ContentDispositionHeaderParser(text);
        parser.parse();
        return parser.disposition;
    }

    // @VisibleForTesting
    ContentDispositionHeaderParser(final String text) {
        super(text);
    }

    @Override
    void value() {
        this.disposition = ContentDisposition.with(
                this.parseValue(RFC2045TOKEN, TYPE, ContentDispositionType::with),
                ContentDisposition.NO_PARAMETERS);
    }

    private final static String TYPE = "type";

    @Override
    void parameterName() {
        this.parseParameterName(RFC2045TOKEN, ContentDispositionParameterName::with);
    }

    @Override
    void parameterValue() {
        final char c = this.character();
        if(DOUBLE_QUOTE ==c) {
            this.position++;
            this.quotedParameterValue();
        } else {
            this.parseParameterValue(RFC2045TOKEN);
        }
    }

    private void quotedParameterValue() {
        final int start = this.position;

        for(;;) {
            if(!this.hasMoreCharacters()) {
                fail("Missing closing " + CharSequences.quoteAndEscape(DOUBLE_QUOTE) + " in " + CharSequences.quoteAndEscape(this.text));
            }
            final char c = this.character();
            if(DOUBLE_QUOTE == c){
                this.addParameter(this.text.substring(start, this.position));
                this.position++;
                break;
            }
            if(!this.parameterName.valueCharPredicate.test(c)) {
                failInvalidCharacter();
            }
            this.position++;
        }
    }

    private final static char DOUBLE_QUOTE = '"';

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

    @Override
    void separator() {
        this.failInvalidCharacter();
    }

    @Override
    void missingParameterValue() {
        this.failEmptyParameterValue();
    }

    @Override
    void tokenEnd() {
            this.disposition = this.disposition.setParameters(this.parameters);
    }

    ContentDisposition disposition;
}
