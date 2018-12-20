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
 * An enum that contains all possible cookie versions.
 */
public enum CookieVersion {

    /**
     * The original cookie version used by Netscape and older browsers.
     */
    VERSION_0(0),

    /**
     * A version 1.0 cookie
     */
    VERSION_1(1);

    /**
     * The default version when none is present.
     */
    static final CookieVersion DEFAULT = VERSION_0;

    /**
     * Private constructor which stores the value.
     */
    private CookieVersion(final int value) {
        this.value = value;
    }

    /**
     * Getter that returns the version integer value.
     */
    public int value() {
        return this.value;
    }

    private final int value;

    /**
     * Converts a int version into a {@link CookieVersion}.
     */
    static CookieVersion from(final int version) {
        CookieVersion cookieVersion;

        switch (version) {
            case 0:
                cookieVersion = VERSION_0;
                break;
            case 1:
                cookieVersion = VERSION_1;
                break;
            default:
                throw new IllegalArgumentException("Unknown version=" + version);
        }

        return cookieVersion;
    }
}
