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

import walkingkooka.build.tostring.ToStringBuilder;

/**
 * The {@link Cookie} http only attribute.
 */
public enum CookieHttpOnly {

    /**
     * The http only attribute is present.
     */
    PRESENT {
        @Override
        void appendToCookieToString(final ToStringBuilder toString) {
            toString.label("");
            toString.value(Cookie.HTTP_ONLY);
        }

        @Override
        boolean toJavaxServletCookieHttpOnly() {
            return true;
        }

    }, //

    /**
     * The http only attribute is absent.
     */
    ABSENT {
        @Override
        void appendToCookieToString(final ToStringBuilder toString) {
            // nop
        }

        @Override
        boolean toJavaxServletCookieHttpOnly() {
            return false;
        }
    };

    final static CookieHttpOnly DEFAULT = ABSENT;

    /**
     * Factory that returns the appropriate {@link CookieHttpOnly} from the boolean taken from a {@link Cookie}.
     */
    static CookieHttpOnly fromJavaxSecureCookie(final boolean secure) {
        return secure ? PRESENT : ABSENT;
    }

    /**
     * Returns the boolean value used to set {@link ServerCookie#setHttpOnly(CookieHttpOnly)}.
     */
    abstract boolean toJavaxServletCookieHttpOnly();

    /**
     * Updates a {@link StringBuilder} used to build a {@link Cookie#toString()}
     */
    abstract void appendToCookieToString(final ToStringBuilder toString);
}
