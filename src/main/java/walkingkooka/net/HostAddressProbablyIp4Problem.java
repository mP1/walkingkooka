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

package walkingkooka.net;

import walkingkooka.text.CharSequences;

/**
 * Represents an address that most likely an ip4 without actually validating
 */
final class HostAddressProbablyIp4Problem extends HostAddressProblem {

    /**
     * Singleton
     */
    final static HostAddressProbablyIp4Problem INSTANCE = new HostAddressProbablyIp4Problem();

    /**
     * Private constructor use singleton
     */
    private HostAddressProbablyIp4Problem() {
        super();
    }

    /**
     * While parsing a name which failed an ip4 was probably found. This problem notifies that only digits and dots were found.
     */
    @Override
    boolean stopTrying() {
        return false;
    }

    @Override
    void report(final String address) {
        throw new IllegalArgumentException(this.message(address));
    }

    private final static String MESSAGE = "Host probably an ip4 dot notation address";

    @Override
    public String message(final String address) {
        return MESSAGE + "=" + CharSequences.quoteAndEscape(address);
    }

    /**
     * Let other problems take precedence
     */
    @Override
    int priority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public String toString() {
        return MESSAGE;
    }
}
