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

package walkingkooka.net.http;

/**
 * Meta data about a header.<br>
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers"></a>
 */
public enum HttpHeaderScope {
    /**
     * The header is only valid in a request
     */
    REQUEST {
        @Override
        void requestHeader(final HttpHeaderName<?> name) {
            // nop
        }

        @Override
        void responseHeader(final HttpHeaderName<?> name) {
            throw new IllegalArgumentException("Cannot add header to response using request header " + name);
        }
    },

    /**
     * The header is only valid in a response.
     */
    RESPONSE {
        @Override
        void requestHeader(final HttpHeaderName<?> name) {
            throw new IllegalArgumentException("Cannot get header value using request header " + name);
        }

        @Override
        void responseHeader(final HttpHeaderName<?> name) {
            // nop
        }
    },

    /**
     * THe header may appear in either request or response.
     */
    REQUEST_RESPONSE {
        @Override
        void requestHeader(final HttpHeaderName<?> name) {
            // nop
        }

        @Override
        void responseHeader(final HttpHeaderName<?> name) {
            // nop
        }
    },

    /**
     * The header scope is unknown. All headers that are not constants
     * will have this value.
     */
    UNKNOWN {
        @Override
        void requestHeader(final HttpHeaderName<?> name) {
            // nop
        }

        @Override
        void responseHeader(final HttpHeaderName<?> name) {
            // nop
        }
    };

    abstract void requestHeader(final HttpHeaderName<?> name);

    abstract void responseHeader(final HttpHeaderName<?> name);
}
