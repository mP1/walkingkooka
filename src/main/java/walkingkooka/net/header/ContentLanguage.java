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

import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Content language
 * <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html"></a>
 * <pre>
 * 14.12 Content-Language
 * The Content-Language entity-header field describes the natural language(s) of the intended audience for the enclosed entity. Note that this might not be equivalent to all the languages used within the entity-body.
 *
 *        Content-Language  = "Content-Language" ":" 1#language-tag
 * Language tags are defined in section 3.10. The primary purpose of Content-Language is to allow a user to identify and differentiate entities according to the user's own preferred language. Thus, if the body content is intended only for a Danish-literate audience, the appropriate field is
 *
 *        Content-Language: da
 * If no Content-Language is specified, the default is that the content is intended for all language audiences. This might mean that the sender does not consider it to be specific to any natural language, or that the sender does not know for which language it is intended.
 *
 * Multiple languages MAY be listed for content that is intended for multiple audiences. For example, a rendition of the "Treaty of Waitangi," presented simultaneously in the original Maori and English versions, would call for
 *
 *        Content-Language: mi, en
 * However, just because multiple languages are present within an entity does not mean that it is intended for multiple linguistic audiences. An example would be a beginner's language primer, such as "A First Lesson in Latin," which is clearly intended to be used by an English-literate audience. In this case, the Content-Language would properly only include "en".
 *
 * Content-Language MAY be applied to any media type -- it is not limited to textual documents.
 * </pre>
 */
public final class ContentLanguage extends HeaderValue2<List<LanguageName>> {

    /**
     * Parses a header value that contains one or more encodings.
     */
    public static ContentLanguage parse(final String text) {
        return ContentLanguageHeaderValueParser.parseContentLanguage(text);
    }

    /**
     * Factory that creates a new {@link ContentLanguage}
     */
    public static ContentLanguage with(final List<LanguageName> languages) {
        Objects.requireNonNull(languages, "languages");

        final List<LanguageName> copy = languages.stream()
                .map(v -> Objects.requireNonNull(v, "languages includes null"))
                .collect(Collectors.toList());

        return new ContentLanguage(copy);
    }

    /**
     * Private ctor use factory
     */
    private ContentLanguage(final List<LanguageName> values) {
        super(Lists.immutable(values));
    }

    // HeaderValue.....................................................................................................

    @Override
    public String toHeaderText() {
        return this.value.stream()
                .map(t -> t.toHeaderText())
                .collect(Collectors.joining(SEPARATOR));
    }

    final static String SEPARATOR = HeaderValue.SEPARATOR.string().concat(" ");

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
        return false;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    // Equals...........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ContentLanguage;
    }
}
