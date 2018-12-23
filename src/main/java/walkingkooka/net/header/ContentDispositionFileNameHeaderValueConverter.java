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


import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicates;

/**
 * A {@link HeaderValueConverter} that parses a content header value into a {@link ContentDispositionFileName}.
 */
final class ContentDispositionFileNameHeaderValueConverter extends HeaderValueConverter2<ContentDispositionFileName> {

    /**
     * Singleton
     */
    final static ContentDispositionFileNameHeaderValueConverter INSTANCE = new ContentDispositionFileNameHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private ContentDispositionFileNameHeaderValueConverter() {
        super();
    }

    @Override
    ContentDispositionFileName parse0(final String value, final Name name) {
        return ContentDispositionFileName.with(QUOTED_UNQUOTED_STRING.parse(value, name));
    }

    @Override
    void check0(final Object value) {
        this.checkType(value, ContentDispositionFileName.class);
    }

    @Override
    String toText0(final ContentDispositionFileName value, final Name name) {
        return QUOTED_UNQUOTED_STRING.toText(value.value(), name);
    }

    private final static HeaderValueConverter<String> QUOTED_UNQUOTED_STRING = HeaderValueConverters.quotedUnquotedString(
            CharPredicates.asciiPrintable(),
            false,
            CharPredicates.rfc2045Token());

    @Override
    public String toString() {
        return ContentDispositionFileName.class.getSimpleName();
    }
}
