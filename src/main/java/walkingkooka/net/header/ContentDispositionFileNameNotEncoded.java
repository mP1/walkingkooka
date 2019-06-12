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

import java.util.Optional;

/**
 * The value of the filename parameter within a content disposition.
 */
final class ContentDispositionFileNameNotEncoded extends ContentDispositionFileName {

    /**
     * Factory that creates a {@link ContentDispositionFileNameNotEncoded}.
     */
    static ContentDispositionFileNameNotEncoded with(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", FILENAME);

        return new ContentDispositionFileNameNotEncoded(name);
    }

    private final static CharPredicate FILENAME = CharPredicates.rfc2045Token().or(CharPredicates.is('/'));

    /**
     * Private constructor use factory.
     */
    private ContentDispositionFileNameNotEncoded(final String name) {
        super();
        this.name = name;
    }
    // Value .................................................................................

    @Override
    public final String value() {
        return this.name;
    }

    private final String name;

    @Override
    public Optional<CharsetName> charsetName() {
        return NO_CHARSET;
    }

    @Override
    public Optional<LanguageTagName> language() {
        return NO_LANGUAGE;
    }

    @Override
    final ContentDispositionFileNameNotEncoded computeWithoutPath() {
        final String value = this.name;
        final String without = removePathIfNecessaryOrNull(value);
        
        return null == without ?
                this :
                new ContentDispositionFileNameNotEncoded(without);
    }

    // HeaderValue .................................................................................

    @Override
    public String toHeaderText() {
        return ContentDispositionFileNameNotEncodedHeaderValueHandler.INSTANCE.toText(this, HttpHeaderName.CONTENT_DISPOSITION);
    }

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof ContentDispositionFileNameNotEncoded;
    }

    @Override
    boolean equals0(final ContentDispositionFileName other) {
        return this.value().equals(other.value());
    }
}
