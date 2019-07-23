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
import walkingkooka.predicate.character.CharPredicates;

import java.util.List;

/**
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
final class ContentLanguageHeaderValueParser extends HeaderValueParser {

    static ContentLanguage parseContentLanguage(final String text) {
        final ContentLanguageHeaderValueParser parser = new ContentLanguageHeaderValueParser(text);
        parser.parse();
        return ContentLanguage.with(parser.languages);
    }

    private ContentLanguageHeaderValueParser(final String text) {
        super(text);
    }

    @Override
    void comment() {
        this.failCommentPresent();
    }

    @Override
    void endOfText() {
        if (this.languages.isEmpty()) {
            this.missingValue();
        }
    }

    @Override
    void keyValueSeparator() {
        this.failInvalidCharacter();
    }

    @Override
    void missingValue() {
        this.failMissingValue("Content-Language");
    }

    @Override
    void multiValueSeparator() {
        // skip
    }

    @Override
    void quotedText() {
        this.failInvalidCharacter();
    }

    @Override
    void slash() {
        this.failInvalidCharacter();
    }

    @Override
    void token() {
        this.languages.add(LanguageName.with(this.token(RFC2045TOKEN)));
    }

    @Override
    void tokenSeparator() {
        this.failInvalidCharacter();
    }

    @Override
    void whitespace() {
        this.token(CharPredicates.whitespace());
    }

    @Override
    void wildcard() {
        this.failInvalidCharacter();
    }

    private List<LanguageName> languages = Lists.array();
}
