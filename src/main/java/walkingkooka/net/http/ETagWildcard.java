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

package walkingkooka.net.http;

/**
 * Holds a Wildcard ETAG.
 */
final class ETagWildcard extends ETag {

    /**
     * Instance getter.
     */
    static ETagWildcard instance() {
        return INSTANCE;
    }

    /**
     * Singleton
     */
    private final static ETagWildcard INSTANCE = new ETagWildcard();

    /**
     * Private ctor use factory
     */
    private ETagWildcard() {
        super();
    }

    /**
     * Matches any other etag except for another wildcard.
     */
    @Override
    boolean isMatch0(final ETag etag) {
        return true;
    }

    // value.........................................................................................

    @Override
    public String value() {
        return WILDCARD_VALUE.string();
    }

    // weak.........................................................................................

    /**
     * Always returns {@link ETagValidator#STRONG}
     */
    public ETagValidator validator() {
        return ETagValidator.STRONG;
    }

    @Override
    void checkValidator0(final ETagValidator validator) {
        checkValidator(validator);
        validator.wildcardValidatorCheck();
    }

    // isXXX........................................................................................................

    @Override
    public boolean isWildcard() {
        return true;
    }

    // toString........................................................................................................

    @Override
    public final String toString() {
        return this.value();
    }
}
