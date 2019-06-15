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

import java.nio.charset.Charset;
import java.util.Optional;

/**
 * A named {@link CharsetName} that refers to an unknown or unsupported charset.
 */
final class CharsetNameUnsupportedCharset extends CharsetName {

    static CharsetNameUnsupportedCharset unsupportedCharset(final String name) {
        return new CharsetNameUnsupportedCharset(name);
    }

    private CharsetNameUnsupportedCharset(final String name) {
        super(name);
    }

    /**
     * Always empty
     */
    @Override
    public final Optional<Charset> charset() {
        return NO_CHARSET;
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    boolean matches0(final CharsetName possible) {
        return possible.matches1(this);
    }

    @Override
    boolean matches1(final CharsetNameSupportedCharset contentType) {
        return false;
    }

    @Override
    boolean matches1(final CharsetNameUnsupportedCharset contentType) {
        return false;
    }
}
