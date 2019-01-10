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

import java.util.NoSuchElementException;

/**
 * A {@link HttpRequestAttribute} to be used as a key for several misc {@link HttpRequest} attributes.
 */
public enum HttpRequestAttributes implements HttpRequestAttribute {

    TRANSPORT {
        @Override
        Object value(final HttpRequest request) {
            return request.transport();
        }
    },
    METHOD {
        @Override
        Object value(final HttpRequest request) {
            return request.method();
        }
    },
    HTTP_PROTOCOL_VERSION {
        @Override
        Object value(final HttpRequest request) {
            return request.protocolVersion();
        }
    };

    /**
     * {@see UrlPathNameHttpRequestAttribute}
     */
    public static HttpRequestAttribute pathComponent(final int index) {
        return UrlPathNameHttpRequestAttribute.with(index);
    }

    /**
     * Retrieves the request value associated with this attribute. Different attributes match up with different
     * getters of a {@link HttpRequest}.
     */
    abstract Object value(final HttpRequest request);

    /**
     * The number of values
     */
    final static int size() {
        return VALUES.length;
    }

    /**
     * Returns an entry holding this attribute as the key and the actual request value as the value.
     */
    final static RouterHttpRequestParametersMapEntry entry(final int position, final HttpRequest request) {
        if (position >= VALUES.length) {
            throw new NoSuchElementException();
        }
        final HttpRequestAttributes key = VALUES[position];
        return RouterHttpRequestParametersMapEntry.with(key, key.value(request));
    }

    /**
     * Returns the {@link #toString()} for the iterator entry.
     */
    final static String iteratorEntryToString(final int position, final HttpRequest request) {
        return position < VALUES.length ?
                VALUES[position] + "=" + VALUES[position].value(request) :
                "";
    }

    /**
     * Constant cached to save the defensive copy every time {@link #values()} is called.
     */
    private final static HttpRequestAttributes[] VALUES = HttpRequestAttributes.values();
}
