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

import walkingkooka.naming.Name;
import walkingkooka.text.CharSequences;

import java.io.Serializable;
import java.util.Objects;

/**
 * A {@link Name} is a component within a {@link UrlPath}. It is assumed that the {@link String value} is decoded and thus may include invalid
 * characters that would otherwise need encoding such as <code>?</code>.
 */
public final class UrlPathName implements Name,
        Comparable<UrlPathName>,
        Serializable {

    /**
     * The maximum length of a {@link UrlPathName}.
     */
    public final static int MAXIMUM_LENGTH = 1024;

    /**
     * Only used by {@link UrlPath} to note a root path.
     */
    final static UrlPathName ROOT = new UrlPathName("");

    /**
     * Creates a new valid {@link UrlPathName}.
     */
    public static UrlPathName with(final String name) {
        Objects.requireNonNull(name, "name");

        return name.isEmpty() ?
                ROOT :
                with0(name);
    }

    private static UrlPathName with0(final String name) {
        final char separator = UrlPath.SEPARATOR.character();
        final int index = name.indexOf(separator);
        if(-1 != index) {
            throw new IllegalArgumentException("Name contains path separator char \'" + separator + "\'=" + name);
        }

        final int length = name.length();
        if(length > MAXIMUM_LENGTH) {
            throw new IllegalArgumentException("url path component is length " + length + " > " +MAXIMUM_LENGTH);
        }

        return new UrlPathName(name);
    }

    /**
     * Private constructor
     */
    private UrlPathName(final String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    final private String name;

    // Comparable......................................................................................................

    @Override
    public int compareTo(final UrlPathName name) {
        return this.name.compareTo(name.name);
    }

    // Object.........................................................................................................

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
               other instanceof UrlPathName && this.equals0((UrlPathName) other);
    }

    private boolean equals0(final UrlPathName other) {
        return this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return CharSequences.quoteIfNecessary(this.name).toString();
    }

    // Serializable......................................................................................................

    private static final long serialVersionUID = 1;

    /**
     * Ensures singleton instance of any {@link UrlPathName#ROOT}.
     */
    private Object readResolve() {
        return this.name.equals(UrlPathName.ROOT.name) ? UrlPathName.ROOT : this;
    }
}
