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
 * The {@link Cookie} secure attribute.
 */
public enum CookieSecure {

    /**
     * The secure attribute is present.
     */
    PRESENT {
        @Override
        boolean toJavaxServletCookieSecure() {
            return true;
        }

        @Override
        void appendToCookieToString(final ToStringBuilder builder) {
            builder.label("");
            builder.value(Cookie.SECURE);
        }

    }, //

    /**
     * The secure attribute is absent.
     */
    ABSENT {
        @Override
        boolean toJavaxServletCookieSecure() {
            return false;
        }

        @Override
        void appendToCookieToString(final ToStringBuilder builder) {
            // nop
        }
    };

    /**
     * Factory that returns the appropriate {@link CookieSecure} from the boolean taken from a {@link Cookie}.
     */
    static CookieSecure fromJavaxSecureCookie(final boolean secure) {
        return secure ? PRESENT : ABSENT;
    }

    /**
     * Returns the boolean value used to set {@link ServerCookie#setSecure}.
     */
    abstract boolean toJavaxServletCookieSecure();

    /**
     * Updates a {@link StringBuilder} used to build a {@link Cookie#toString()}
     */
    abstract void appendToCookieToString(final ToStringBuilder builder);
}
