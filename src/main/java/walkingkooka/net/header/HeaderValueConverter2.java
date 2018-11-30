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

import walkingkooka.naming.Name;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpRequest;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Base class and contract to assist {@link HttpHeaderName#headerValue(HttpRequest)}
 */
abstract class HeaderValueConverter2<T> implements HeaderValueConverter<T> {

    /**
     * Package private to limit sub classing.
     */
    HeaderValueConverter2() {
        super();
    }

    // parse ....................................................................................................

    /**
     * The entry point that accepts a value and tries to parse it.
     */
    public final T parse(final String value, final Name name) {
        try {
            return this.parse0(value, name);
        } catch (final HeaderValueException cause) {
            throw cause;
        } catch (final RuntimeException cause) {
            throw new HeaderValueException("Failed to convert " + CharSequences.quote(name.value()) +
                    " value " + CharSequences.quote(value) +
                    ", message: " + cause.getMessage(),
                    cause);
        }
    }

    /**
     * Sub classes parse the {@link String} value.
     */
    abstract T parse0(final String value, final Name name) throws HeaderValueException, RuntimeException;

    // checkValue...........................................................

    public final void check(final Object value) {
        Objects.requireNonNull(value, "value");
        this.check0(value);
    }

    abstract void check0(final Object value);

    // format ....................................................................................................

    /**
     * Accepts a typed value and formats it into a http response header string.
     */
    public final String format(final T value, final Name name) {
        try {
            return this.format0(value, name);
        } catch (final HeaderValueException cause) {
            throw cause;
        } catch (final RuntimeException cause) {
            throw new HeaderValueException("Failed to convert " + CharSequences.quote(name.value()) +
                    " value " + CharSequences.quoteIfChars(value) +
                    ", message: " + cause.getMessage(),
                    cause);
        }
    }

    abstract String format0(final T value, final Name name);

    // Object ..........................................................................................

    @Override
    abstract public String toString();

    final String toStringType(final Class<?> type) {
        return type.getSimpleName();
    }

    final String toStringListOf(final Class<?> type) {
        return "List<" + type.getSimpleName() + ">";
    }
}
