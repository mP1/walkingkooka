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

import java.util.Optional;

/**
 * The value of the filename parameter within a content disposition.
 */
abstract public class ContentDispositionFileName implements Name, HeaderValue {

    public final static Optional<CharsetName> NO_CHARSET = Optional.empty();

    public final static Optional<LanguageTagName> NO_LANGUAGE = Optional.empty();

    /**
     * Factory that creates a {@link ContentDispositionFileName}.
     */
    public static ContentDispositionFileName encoded(final EncodedText encodedText) {
        return ContentDispositionFileNameEncoded.with(encodedText);
    }

    /**
     * Factory that creates a {@link ContentDispositionFileName}.
     */
    public static ContentDispositionFileName notEncoded(final String name) {
        return ContentDispositionFileNameNotEncoded.with(name);
    }

    /**
     * Package private constructor.
     */
    ContentDispositionFileName() {
        super();
    }

    public abstract Optional<CharsetName> charsetName();

    public abstract Optional<LanguageTagName> language();

    // HeaderValue .................................................................................

    @Override
    public final boolean isWildcard() {
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
    public final int hashCode() {
        return this.value().hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                other instanceof ContentDispositionFileName &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEquals(final Object other);

    abstract boolean equals0(final ContentDispositionFileName other);

    @Override
    public final String toString() {
        return this.value();
    }
}
