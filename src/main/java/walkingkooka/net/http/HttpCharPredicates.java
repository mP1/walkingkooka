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

package walkingkooka.net.http;

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.type.PublicStaticHelper;

final public class HttpCharPredicates implements PublicStaticHelper {

    // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie

    /**
     * A {@link CharPredicate} that match tany character in a cookie name
     */
    public static CharPredicate cookieName() {
        return HttpCharPredicates.COOKIE_NAME;
    }

    /**
     * A <cookie-name> can be any US-ASCII characters except control characters (CTLs), spaces, or tabs.
     * It also must not contain a separator character like the following: ( ) < > @ , ; : \ " /  [ ] ? = { }.
     */
    private final static CharPredicate COOKIE_NAME = CharPredicates.builder()//
            .or(CharPredicates.ascii())//
            .andNot(CharPredicates.any("()<>@,;:\\\"/[]?={})"))
            .toString("cookie name")//
            .build();


    /**
     * A {@link CharPredicate} that match the content within an ETAG quoted string.<br>
     * <a href="https://tools.ietf.org/html/rfc7232#section-2.3></a>
     */
    public static CharPredicate etagQuotedCharacter() {
        return E_TAG_QUOTED_CHARACTER;
    }

    /**
     *  etagc      = %x21 / %x23-7E / obs-text
     *             ; VCHAR except double quotes, plus obs-text
     */
    private final static CharPredicate E_TAG_QUOTED_CHARACTER = CharPredicates.builder()//
            .or(CharPredicates.is('\u0021'))//
            .or(CharPredicates.range('\u0023', '\u007e'))
            .toString("e tag quoted value")//
            .build();

    /**
     * A {@link CharPredicate} that match any character in a header name
     * <a href="https://www.ietf.org/rfc/rfc822.txt">RFC822</a>
     * <pre>
     * The field-name must be composed of printable ASCII characters (position.e., characters that have values between 33. and 126., decimal, except colon).
     * </pre>
     */
    public static CharPredicate httpHeaderName() {
        return HttpCharPredicates.HTTP_HEADER;
    }

    private final static CharPredicate HTTP_HEADER = CharPredicates.builder()//
            .or(CharPredicates.ascii())//
            .and(CharPredicates.range((char) 33, (char) 126))//
            .andNot(CharPredicates.any(":."))
            .toString("http header name")//
            .build();

    /**
     * {@see Rfc2045TokenCharPredicate}
     */
    public static CharPredicate rf2045Token() {
        return Rfc2045TokenCharPredicate.INSTANCE;
    }

    /**
     * Matches any whitespace character within a header.<br>
     * <a href="https://en.wikipedia.org/wiki/Augmented_Backus%E2%80%93Naur_form"></a>
     */
    public static CharPredicate whitespace() {
        return WHITESPACE;
    }

    /**
     * HS | SP
     */
    private final static CharPredicate WHITESPACE = CharPredicates.builder()//
            .or(CharPredicates.is('\u0009'))
            .or(CharPredicates.is('\u0020'))//
            .toString("SP|HTAB")//
            .build();

    /**
     * Stop creation
     */
    private HttpCharPredicates() {
        throw new UnsupportedOperationException();
    }
}
