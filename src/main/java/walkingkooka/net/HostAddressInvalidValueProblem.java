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

/**
 * Represents an invalid octet or atom value.
 */
final class HostAddressInvalidValueProblem extends HostAddressProblem implements HashCodeEqualsDefined {

    static HostAddressInvalidValueProblem with(final int at) {
        return new HostAddressInvalidValueProblem(at);
    }

    /**
     * Private constructor use static factory
     */
    private HostAddressInvalidValueProblem(final int at) {
        super();
        this.at = at;
    }

    /**
     * Was almost a name/ip4/ip6, but something syntactically was wrong.
     */
    @Override
    boolean stopTrying() {
        return true;
    }

    @Override
    public String message(final String address) {
        return this.toString();
    }

    /**
     * The first character of the invalid value
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
                || ((other instanceof HostAddressInvalidValueProblem)
                && this.equals((HostAddressInvalidValueProblem) other));
    }

    private boolean equals(final HostAddressInvalidValueProblem other) {
        return this.at == other.at;
    }

    @Override
    public String toString() {
        return "Host contains invalid value at " + this.at;
    }
}
