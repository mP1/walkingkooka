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

package walkingkooka.net.http.server;

import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.stream.IntStream;

/**
 * A key to a url path component ({@link walkingkooka.net.UrlPathName}.
 */
final class UrlPathNameHttpRequestAttribute implements HttpRequestAttribute, HashCodeEqualsDefined {

    /**
     * Factory that returns a {@link UrlPathNameHttpRequestAttribute}.
     */
    static UrlPathNameHttpRequestAttribute with(final int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index " + index + " < 0");
        }
        return index < CONSTANT_COUNT ?
                CACHE[index] :
                new UrlPathNameHttpRequestAttribute(index);
    }

    /**
     * The size of the cache.
     */
    final static int CONSTANT_COUNT = 32;

    /**
     * Fills the cache with instances.
     */
    private final static UrlPathNameHttpRequestAttribute[] CACHE = IntStream.range(0, CONSTANT_COUNT)
            .mapToObj(i -> new UrlPathNameHttpRequestAttribute(i))
            .toArray(i -> new UrlPathNameHttpRequestAttribute[i]);

    /**
     * Private ctor use factory.
     */
    private UrlPathNameHttpRequestAttribute(final int index) {
        super();
        this.index = index;
    }

    // Object....................................................................................................

    @Override
    public int hashCode() {
        return this.index;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof UrlPathNameHttpRequestAttribute &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final UrlPathNameHttpRequestAttribute other) {
        return this.index == other.index;
    }

    @Override
    public String toString() {
        return "path-" + this.index;
    }

    // shared with RouterHttpRequestParametersMap
    final int index;
}
