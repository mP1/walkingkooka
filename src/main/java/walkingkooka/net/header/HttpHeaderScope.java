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

import java.util.List;
import java.util.Objects;

/**
 * Meta data about a header.<br>
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers"></a>
 */
public enum HttpHeaderScope {

    /**
     * Multipart scope
     */
    MULTIPART {
        @Override
        boolean isNotAcceptable(final HasHeaderScope other) {
            return !other.isMultipart();
        }
    },

    /**
     * Request scope
     */
    REQUEST {
        @Override
        boolean isNotAcceptable(final HasHeaderScope other) {
            return !other.isRequest();
        }
    },

    /**
     * Response scope.
     */
    RESPONSE {
        @Override
        boolean isNotAcceptable(final HasHeaderScope other) {
            return !other.isResponse();
        }
    };

    /**
     * Checks that the header's scope is for requests, if not a {@link HeaderValueException} will be thrown.
     */
    public final void check(final HttpHeaderName<?> name) {
        Objects.requireNonNull(name, "name");

        if (this.isNotAcceptable(name)) {
            throw new NotAcceptableHeaderException("Invalid header " + name);
        }
    }

    /**
     * Checks that the header and values if they implement {@link HttpHeaderScope} are all response scope.
     */
    public final <T> void check(final HttpHeaderName<T> name, final T value) {
        this.check(name);

        Objects.requireNonNull(value, "value");

        if (value instanceof HasHeaderScope) {
            if (this.isNotAcceptable(HasHeaderScope.class.cast(value))) {
                this.failInvalidHeader(name, value);
            }
        }
        if (value instanceof List) {
            final List<?> list = List.class.cast(value);
            if (list.stream()
                    .anyMatch(this::isNotAcceptable)) {
                this.failInvalidHeader(name, value);
            }
        }
    }

    private boolean isNotAcceptable(final Object has) {
        return has instanceof HasHeaderScope &&
                this.isNotAcceptable(HasHeaderScope.class.cast(has));
    }

    abstract boolean isNotAcceptable(final HasHeaderScope other);

    final <T> void failInvalidHeader(final HttpHeaderName<T> name, final T value) {
        throw new NotAcceptableHeaderException(invalidHeader(name, value));
    }

    static <T> String invalidHeader(final HttpHeaderName<T> name, final T value) {
        return "Invalid header " + name + "=" + name.headerText(value);
    }
}
