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
 * A {@link HeaderValueHandler} that parses a content header value into a {@link ContentDispositionFileNameNotEncoded}.
 */
final class ContentDispositionFileNameNotEncodedHeaderValueHandler extends ContentDispositionFileNameHeaderValueHandler<ContentDispositionFileNameNotEncoded> {

    /**
     * Singleton
     */
    final static ContentDispositionFileNameNotEncodedHeaderValueHandler INSTANCE = new ContentDispositionFileNameNotEncodedHeaderValueHandler();

    /**
     * Private ctor use singleton.
     */
    private ContentDispositionFileNameNotEncodedHeaderValueHandler() {
        super();
    }

    @Override
    ContentDispositionFileName parse0(final String text, final Name name) {
        return ContentDispositionFileName.notEncoded(QUOTED_UNQUOTED_STRING.parse(text, name));
    }

    @Override
    String toText0(final ContentDispositionFileName filename, final Name name) {
        return QUOTED_UNQUOTED_STRING.toText(filename.value(), name);
    }

    private final static HeaderValueHandler<String> QUOTED_UNQUOTED_STRING = HeaderValueHandler.quotedUnquotedString(
            CharPredicates.asciiPrintable(),
            false,
            CharPredicates.rfc2045Token());

    @Override
    Class<ContentDispositionFileNameNotEncoded> type() {
        return ContentDispositionFileNameNotEncoded.class;
    }
}
