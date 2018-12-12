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

/**
 * A {@link HeaderValueConverter} that handles parsing a quoted boundary string into its raw form.<br>
 * <pre>
 * Content-type: multipart/mixed; boundary="abcdefGHIJK"
 * </pre>
 */
final class MediaTypeBoundaryHeaderValueConverter extends QuotedHeaderValueConverter<MediaTypeBoundary> {

    /**
     * Singleton
     */
    final static MediaTypeBoundaryHeaderValueConverter INSTANCE = new MediaTypeBoundaryHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private MediaTypeBoundaryHeaderValueConverter() {
        super();
    }

    @Override
    boolean allowBackslashEscaping() {
        return false;
    }

    @Override
    boolean isCharacterWithinQuotes(final char c) {
        return MediaTypeBoundary.PREDICATE.test(c);
    }

    @Override
    MediaTypeBoundary createQuotedValue(final String raw, final String text) {
        return MediaTypeBoundary.with0(raw, text);
    }

    @Override
    boolean isCharacterWithoutQuotes(final char c) {
        return MediaTypeBoundary.PREDICATE.test(c);
    }

    @Override
    MediaTypeBoundary createUnquotedValue(final String raw) {
        return MediaTypeBoundary.with0(raw, raw);
    }

    @Override
    void check0(final Object value) {
        this.checkType(value, MediaTypeBoundary.class);
    }

    /**
     * Delegates to the {@link MediaTypeBoundary#toHeaderText()}.
     */
    @Override
    String toText0(final MediaTypeBoundary boundary, final Name name) {
        return boundary.toHeaderText();
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public String toString() {
        return MediaTypeBoundary.class.getSimpleName();
    }
}
