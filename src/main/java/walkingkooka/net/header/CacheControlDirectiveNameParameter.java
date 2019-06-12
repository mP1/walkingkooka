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

import walkingkooka.Cast;
import walkingkooka.text.CharSequences;

import java.util.Optional;

/**
 * Additional meta data for a particular cache control directive.
 */
enum CacheControlDirectiveNameParameter {

    REQUIRED {
        @Override
        <T> Optional<T> check0(final Optional<T> value, final CacheControlDirectiveName<T> name) {
            if (value.isPresent()) {
                name.handler.check(value.get(), name);
            } else {
                fail("Directive " + name + " missing required parameter.");
            }
            return value;
        }
    },
    OPTIONAL {
        @Override
        <T> Optional<T> check0(final Optional<T> value, final CacheControlDirectiveName<T> name) {
            value.ifPresent((v) -> {name.handler.check(v, name);});

            return value;
        }
    },
    ABSENT {
        @Override
        <T> Optional<T> check0(final Optional<T> value, final CacheControlDirectiveName<T> name) {
            if (value.isPresent()) {
                fail("Directive " + name + " does not accept parameter " + CharSequences.quoteIfChars(value));
            }
            return value;
        }
    };

    final <T> Optional<T> check(final Object value, final CacheControlDirectiveName<T> name) {
        if (false == value instanceof Optional) {
            fail("Incorrect parameter type " + CharSequences.quoteIfChars(value) + " for " + name);
        }
        return this.check0(Cast.to(value), name);
    }

    abstract <T> Optional<T> check0(Optional<T> value, final CacheControlDirectiveName<T> name);

    final <T> Optional<T> fail(final String message) {
        throw new HeaderValueException(message);
    }
}
