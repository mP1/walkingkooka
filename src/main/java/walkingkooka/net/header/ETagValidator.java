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

/**
 * The validator component of a {@link ETag}.<br>
 * <a href="https://en.wikipedia.org/wiki/HTTP_ETag"></a>
 */
public enum ETagValidator {

    WEAK("W/") {
        @Override
        void wildcardValidatorCheck() {
            throw new IllegalArgumentException("Wildcard etag must be " + STRONG + " not " + WEAK);
        }
    },

    STRONG("") {
        @Override
        void wildcardValidatorCheck() {
            // nop
        }
    };

    ETagValidator(final String prefix) {
        this.prefix = prefix;
    }

    /**
     * Check upon a validator performed by wildcard etag during {@link ETag#setValidator(ETagValidator)}.
     */
    abstract void wildcardValidatorCheck();

    /**
     * Prefix used by {@link ETagNonWildcard#toString()}.
     */
    final String prefix;

    /**
     * Factory that creates a {@link ETag} with this validator and the given {@link String value}.
     */
    public final ETag setValue(final String value) {
        return ETag.with(value, this);
    }
}
