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

package walkingkooka.net;

import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;

/**
 * Represents an invalid character.
 */
final class HostAddressInvalidCharacterProblem extends HostAddressProblem implements HashCodeEqualsDefined {

    static HostAddressInvalidCharacterProblem with(final int at) {
        return new HostAddressInvalidCharacterProblem(at);
    }

    /**
     * Private constructor use static factory
     */
    private HostAddressInvalidCharacterProblem(final int at) {
        super();
        this.at = at;
    }

    @Override
    public String message(final String address) {
        final int at = this.at;
        return "Host contains invalid character " + CharSequences.quoteAndEscape(address.charAt(at)) + " at " + at;
    }

    /**
     * Was not a name or ip4 should try the next parse method.
     */
    @Override
    boolean stopTrying() {
        return false;
    }

    /**
     * Uses the position as the priority
     */
    @Override
    int priority() {
        return this.at;
    }

    private final int at;

    @Override
    public int hashCode() {
        return this.at;
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other)
                || ((other instanceof HostAddressInvalidCharacterProblem)
                && this.equals0((HostAddressInvalidCharacterProblem) other));
    }

    private boolean equals0(final HostAddressInvalidCharacterProblem other) {
        return this.at == other.at;
    }

    @Override
    public String toString() {
        return "Invalid character at " + this.at;
    }
}
