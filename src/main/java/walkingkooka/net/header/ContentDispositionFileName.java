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

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.text.CaseSensitivity;

import java.io.File;
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

    /**
     * Returns a {@link ContentDispositionFileName} where the filename has no path.
     */
    public final ContentDispositionFileName withoutPath() {
        if (null == this.withoutPath) {
            final ContentDispositionFileName withoutPath = this.computeWithoutPath();
            this.withoutPath = withoutPath.withoutPath = withoutPath;
        }
        return this.withoutPath;
    }

    // VisibleForTesting
    ContentDispositionFileName withoutPath;

    /**
     * Lazily computes a {@link ContentDispositionFileName} with only the filename without any path.
     */
    abstract ContentDispositionFileName computeWithoutPath();

    /**
     * Scans the path for the first slash or filesystem separator character, or null if none was found and the original
     * filename has no path.
     */
    static String removePathIfNecessaryOrNull(final String filename) {
        String result = null;

        for (int i = filename.length() - 1; i >= 0; --i) {
            final char c = filename.charAt(i);
            if ('/' == c || File.separatorChar == c) {
                result = filename.substring(i + 1);
                break;
            }
        }

        return result;
    }

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
                this.canBeEquals(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEquals(final Object other);

    abstract boolean equals0(final ContentDispositionFileName other);

    @Override
    public final String toString() {
        return this.value();
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public final CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;
}
