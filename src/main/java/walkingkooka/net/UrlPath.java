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


import walkingkooka.naming.Path;
import walkingkooka.naming.PathSeparator;
import walkingkooka.test.HashCodeEqualsDefined;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Path} which may be part of a {@link Url} after the host and port but before any present query string or anchor.
 */
public final class UrlPath implements Path<UrlPath, UrlPathName>, Comparable<UrlPath>, HashCodeEqualsDefined, Serializable {

    /**
     * {@link PathSeparator} instance
     */
    public final static PathSeparator SEPARATOR = PathSeparator.requiredAtStart('/');

    /**
     * Constant used to indicate no parent.
     */
    private final static Optional<UrlPath> NO_PARENT = Optional.empty();

    /**
     * Singleton {@link UrlPath} with a {@link PathSeparator#string}.
     */
    public final static UrlPath ROOT = new UrlPath(UrlPath.SEPARATOR.string(), UrlPathName.ROOT, NO_PARENT);

    /**
     * Singleton {@link UrlPath} with an empty {@link String path}.
     */
    public final static UrlPath EMPTY = new UrlPath("", UrlPathName.ROOT, NO_PARENT);

    /**
     * Creates a new {@link UrlPath}. Note that the entire ancestor hierarchy is created while breaking the path up into components.
     */
    public static UrlPath parse(final String path) {
        Objects.requireNonNull(path, "path");

        return path.isEmpty() ?
               EMPTY :
               path.equals(SEPARATOR.string()) ?
               ROOT :
               parse0(path);
    }

    /**
     * Parses and creates the path chain.
     */
    private static UrlPath parse0(final String value) {
        return parse1(value,
               value.charAt(0) == SEPARATOR.character() ? 1 : 0,
               ROOT);
    }

    private static UrlPath parse1(final String value, final int start, final UrlPath parent) {
        UrlPath path = parent;

        final char separator = SEPARATOR.character();
        final int length = value.length();
        int s = start;

        for(;;) {
            final int end = value.indexOf(separator, s);
            if(-1 == end) {
                path = path.append(UrlPathName.with(value.substring(s, length)));
                break;
            }
            path = path.append(UrlPathName.with(value.substring(s, end)));
            s = end + 1;
            if(start >= length) {
                break;
            }
        }

        return path;
    }

    /**
     * Private constructor
     */
    private UrlPath(final String path, final UrlPathName name, final Optional<UrlPath> parent) {
        super();

        this.path = path;
        this.name = name;
        this.parent = parent;
    }

    // Path

    @Override
    public UrlPath append(final UrlPathName name) {
        Objects.requireNonNull(name, "name");

        if (UrlPathName.ROOT.equals(name)) {
            throw new IllegalArgumentException(UrlPath.CANNOT_APPEND_ROOT_NAME);
        }

        final StringBuilder path = new StringBuilder();
        if (false == this.isRoot()) {
            path.append(this.path);
        }
        path.append(UrlPath.SEPARATOR.character());
        path.append(name.value());

        return new UrlPath(path.toString(), name, Optional.of(this));
    }

    /**
     * Thrown when attempting to add the root name to this {@link UrlPath}.
     */
    final static String CANNOT_APPEND_ROOT_NAME = "Cannot append root name.";

    @Override
    public UrlPath append(final UrlPath path) {
        Objects.requireNonNull(path, "path");

        UrlPath result = this;

        for (;;) {
            if (this.isEmpty()) {
                result = path;
                break;
            }
            // ignore $path if its empty or root.
            if (path.isRoot() || path.isEmpty()) {
                break;
            }
            if (this.isRoot()) {
                result = path;
                break;
            }
            result = parse1(this.path + path.value(),
                    this.path.length() + 1,
                    this);
            break;
        }

        return result;
    }

    @Override
    public UrlPathName name() {
        return this.name;
    }

    transient private UrlPathName name;

    @Override
    public Optional<UrlPath> parent() {
        return this.parent;
    }

    private transient final Optional<UrlPath> parent;

    @Override
    public String value() {
        return this.path;
    }

    private final String path;

    /**
     * {@link PathSeparator} getter.
     */
    @Override
    public PathSeparator separator() {
        return UrlPath.SEPARATOR;
    }

    @Override
    public boolean isRoot() {
        return this == UrlPath.ROOT;
    }

    /**
     * Tests if this path is empty.
     */
    private boolean isEmpty() {
        return this == UrlPath.EMPTY;
    }

    // Comparable......................................................................................................

    @Override
    public int compareTo(final UrlPath path) {
        return this.path.compareTo(path.path);
    }

    // Object.........................................................................................................

    @Override
    public int hashCode() {
        return this.path.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
               other instanceof UrlPath &&
               this.equals0((UrlPath) other);
    }

    private boolean equals0(final UrlPath other) {
        return this.path.equals(other.path);
    }

    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();
        this.toString0(b);
        return b.toString();
    }

    void toString0(final StringBuilder b) {
        b.append(this.path);
    }

    // Serialization....................................................................................................

    private Object readResolve() throws ObjectStreamException {
        return UrlPath.parse(this.path);
    }

    private final static long serialVersionUID = 1L;
}
