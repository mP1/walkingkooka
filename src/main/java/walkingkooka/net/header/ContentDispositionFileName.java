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

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicates;

/**
 * The value of the filename parameter within a content disposition.
 */
final public class ContentDispositionFileName implements Name, HeaderValue {

    /**
     * Factory that creates a {@link ContentDispositionFileName}.
     */
    public static ContentDispositionFileName with(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", CharPredicates.rfc2045Token());

        return new ContentDispositionFileName(name);
    }

    /**
     * Private constructor use factory.
     */
    private ContentDispositionFileName(final String name) {
        super();
        this.name = name;
    }

    // Value .................................................................................

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    // HeaderValue .................................................................................

    @Override
    public String toHeaderText() {
        return ContentDispositionFileNameHeaderValueConverter.INSTANCE.toText(this, HttpHeaderName.CONTENT_DISPOSITION);
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    // HasHeaderScope....................................................................

    @Override
    public final boolean isMultipart() {
        return true;
    }

    @Override
    public final boolean isRequest() {
        return true;
    }

    @Override
    public final boolean isResponse() {
        return true;
    }

    // Object .................................................................................

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof ContentDispositionFileName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final ContentDispositionFileName name) {
        return this.name.equals(name.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
