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

import walkingkooka.test.HashCodeEqualsDefined;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * The {@link Cookie} deletion policy. Currently this is limited to <code>Expires</code> and <code>Max-age</code>.
 */
abstract public class CookieDeletion implements HashCodeEqualsDefined {

    /**
     * The value of age when this cookie is marked to be deleted.
     */
    public final static CookieMaxAge DELETED = new CookieMaxAge(0);

    /**
     * The value of age when this cookie is marked as a session only cookie
     */
    public final static CookieMaxAge SESSION_ONLY = new CookieMaxAge(-1);

    /**
     * {@see CookieExpires}
     */
    public static CookieExpires expires(final LocalDateTime dateTime) {
        return CookieExpires.with(dateTime);
    }

    /**
     * {@see CookieMaxAge}
     */
    public static Optional<CookieDeletion> maxAge(final int seconds) {
        return -1 != seconds ?
                Optional.of(CookieMaxAge.with(seconds)) :
                Optional.empty();
    }

    /**
     * Package private to limit sub-classing.
     */
    CookieDeletion() {
        super();
    }

    /**
     * Only {@link CookieMaxAge} returns true.
     */
    abstract public boolean isMaxAge();

    /**
     * Only {@link CookieExpires} returns true.
     */
    abstract public boolean isExpires();

    /**
     * Helper used by {@link ServerCookie#toJavaxServletCookie(LocalDateTime)}
     */
    abstract int toMaxAgeSeconds(LocalDateTime now);
}
