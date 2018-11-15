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

package walkingkooka.net.http.cookie;

import walkingkooka.Cast;

import java.time.LocalDateTime;

/**
 * The max-age attribute of a {@link Cookie}
 */
final public class CookieMaxAge extends CookieDeletion {

    /**
     * Creates a new {@link CookieMaxAge}
     */
    public static CookieMaxAge with(final int seconds) {
        return CookieDeletion.DELETED.seconds() == seconds ? CookieDeletion.DELETED : //
                CookieDeletion.SESSION_ONLY.seconds() == seconds ? CookieDeletion.SESSION_ONLY : //
                        new CookieMaxAge(seconds);
    }

    /**
     * Private constructor use factory, only constants use this ctor directly.
     */
    CookieMaxAge(final int seconds) {
        super();
        this.seconds = seconds;
    }

    // CookieDeletion

    @Override
    public boolean isMaxAge() {
        return true;
    }

    @Override
    public boolean isExpires() {
        return false;
    }

    @Override
    int toMaxAgeSeconds(final LocalDateTime ignored) {
        return this.seconds;
    }

    /**
     * Returns the number of seconds valid.
     */
    public int seconds() {
        return this.seconds;
    }

    private final int seconds;

    // Object ...............................................................................

    @Override
    public int hashCode() {
        return this.seconds;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof CookieMaxAge && this.equals0(Cast.to(other));
    }

    private boolean equals0(final CookieMaxAge other) {
        return this.seconds == other.seconds;
    }

    @Override
    public String toString() {
        return Cookie.MAX_AGE + Cookie.NAME_VALUE_SEPARATOR + this.seconds;
    }
}
