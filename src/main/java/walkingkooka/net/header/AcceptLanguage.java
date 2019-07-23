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

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html"></a>
 * <pre>
 * 14.4 Accept-Language
 * The Accept-Language request-header field is similar to Accept, but restricts the set of natural languages that are preferred as a response to the request. Language tags are defined in section 3.10.
 *
 *        Accept-Language = "Accept-Language" ":"
 *                          1#( language-range [ ";" "q" "=" qvalue ] )
 *        language-range  = ( ( 1*8ALPHA *( "-" 1*8ALPHA ) ) | "*" )
 * Each language-range MAY be given an associated quality value which represents an estimate of the user's preference for the languages specified by that range. The quality value defaults to "q=1". For example,
 *
 *        Accept-Language: da, en-gb;q=0.8, en;q=0.7
 * would mean: "I prefer Danish, but will accept British English and other types of English." A language-range matches a language-tag if it exactly equals the tag, or if it exactly equals a prefix of the tag such that the first tag character following the prefix is "-". The special range "*", if present in the Accept-Language field, matches every tag not matched by any other range present in the Accept-Language field.
 *
 *       Note: This use of a prefix matching rule does not imply that
 *       language tags are assigned to languages in such a way that it is
 *       always true that if a user understands a language with a certain
 *       tag, then this user will also understand all languages with tags
 *       for which this tag is a prefix. The prefix rule simply allows the
 *       use of prefix tags if this is the case.
 * The language quality factor assigned to a language-tag by the Accept-Language field is the quality value of the longest language- range in the field that matches the language-tag. If no language- range in the field matches the tag, the language quality factor assigned is 0. If no Accept-Language header is present in the request, the server
 *
 * SHOULD assume that all languages are equally acceptable. If an Accept-Language header is present, then all languages which are assigned a quality factor greater than 0 are acceptable.
 *
 * It might be contrary to the privacy expectations of the user to send an Accept-Language header with the complete linguistic preferences of the user in every request. For a discussion of this issue, see section 15.1.4.
 *
 * As intelligibility is highly dependent on the individual user, it is recommended that client applications make the choice of linguistic preference available to the user. If the choice is not made available, then the Accept-Language header field MUST NOT be given in the request.
 *
 *       Note: When making the choice of linguistic preference available to
 *       the user, we remind implementors of  the fact that users are not
 *       familiar with the details of language matching as described above,
 *       and should provide appropriate guidance. As an example, users
 *       might assume that on selecting "en-gb", they will be served any
 *       kind of English document if British English is not available. A
 *       user agent might suggest in such a case to add "en" to get the
 *       best matching behavior.
 * </pre>
 */
public final class AcceptLanguage extends HeaderValue2<List<LanguageWithParameters>>
        implements Predicate<ContentLanguage> {

    /**
     * Parses a header value that contains one or more encodings.
     */
    public static AcceptLanguage parse(final String text) {
        return AcceptLanguageHeaderValueParser.parseAcceptLanguage(text);
    }

    /**
     * Factory that creates a new {@link AcceptLanguage}
     */
    public static AcceptLanguage with(final List<LanguageWithParameters> values) {
        Objects.requireNonNull(values, "values");

        return new AcceptLanguage(values.stream()
                .map(v -> Objects.requireNonNull(v, "values includes null"))
                .collect(Collectors.toList()));
    }

    /**
     * Package private ctor use factory. Only called directly by factory or {@link AcceptEncodingHeaderValueParser}
     */
    AcceptLanguage(final List<LanguageWithParameters> values) {
        super(values);
    }

    // Predicate........................................................................................................

    /**
     * Returns true if any of the language belong to this accept-encoding matches the given {@link ContentLanguage}.
     */
    @Override
    public boolean test(final ContentLanguage contentLanguage) {
        Objects.requireNonNull(contentLanguage, "contentLanguage");

        return this.value.stream()
                .filter(e -> e.testContentLanguage(contentLanguage))
                .limit(1)
                .count() == 1;
    }

    // HeaderValue.....................................................................................................

    @Override
    public String toHeaderText() {
        return HeaderValue.toHeaderTextList(value, SEPARATOR);
    }

    private final static String SEPARATOR = HeaderValue.SEPARATOR.string().concat(" ");
    
    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return false;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof AcceptLanguage;
    }
}
