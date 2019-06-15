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

import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharacterConstant;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Name} that holds a URI/URL scheme and when testing for equality case is insignificant.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication>Mozilla HTTP Authentication</a>
 */
public final class UrlCredentials implements HashCodeEqualsDefined, Serializable {

    /**
     * A constant useful for a url without credentials.
     */
    public final static Optional<UrlCredentials> NO_CREDENTIALS = Optional.empty();

    /**
     * The character that separates user and password components.
     */
    public final static CharacterConstant SEPARATOR = CharacterConstant.with(':');

    private final static char SUFFIX = '@';

    /**
     * Factory that creates a {@link UrlCredentials}
     */
    public static UrlCredentials with(final String user, final String password) {
        Objects.requireNonNull(user, "user");
        Objects.requireNonNull(password, "password");

        return new UrlCredentials(user, password);
    }

    /**
     * package private constructor.
     */
    UrlCredentials(final String user, final String password) {
        super();
        this.user = user;
        this.password = password;
    }

    public String user() {
        return this.user;
    }

    private final String user;

    public String password() {
        return this.password;
    }

    private final String password;

    // Object

    @Override
    public int hashCode() {
        return Objects.hash(this.user, this.password);
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) ||
                other instanceof UrlCredentials &&
                        this.equals0((UrlCredentials) other);
    }

    private boolean equals0(final UrlCredentials other) {
        return this.user.equals(other.user) &&
                this.password.equals(other.password);
    }

    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();
        this.toString0(b);
        return b.toString();
    }

    final void toString0(final StringBuilder b) {
        b.append(this.user);
        b.append(SEPARATOR.character());
        b.append(this.password);
        b.append(SUFFIX);
    }

    // Serializable

    private final static long serialVersionUID = 1L;
}
