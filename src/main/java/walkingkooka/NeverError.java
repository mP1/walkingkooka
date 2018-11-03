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
 */

package walkingkooka;

import walkingkooka.text.CharSequences;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This {@link Error} should only be thrown by code that should literally never happen.
 */
public class NeverError extends Error {

    private static final long serialVersionUID = 1L;

    /**
     * Reports an unexpected {@link UnsupportedEncodingException}.
     */
    public static NeverError unsupportedEncodingException(final UnsupportedEncodingException cause) {
        throw new NeverError(cause.getMessage(), cause);
    }

    /**
     * Reports an unexpected method call, including dumping the parameters.
     */
    public static <T> T unexpectedMethodCall(final Object instance, final String method, final Object...parameters) {
        throw new NeverError(instance.getClass().getName() + "." + method + "(" + Arrays.stream(parameters)
                .map(p -> CharSequences.quoteIfChars(p))
                .collect(Collectors.joining(","))+ ")");
    }

    /**
     * Useful to report unexpected enums in a switch.
     */
    public static <E extends Enum<E>> NeverError unhandledCase(final Object value, final Object... expected) {
        throw new NeverError("Unhandled value: " + CharSequences.quoteIfChars(value) + " only expected: " + Arrays.stream(expected)
                .map(m -> CharSequences.quoteIfChars(m))
                .collect(Collectors.joining(",")));
    }

    /**
     * Useful to report unexpected enums in a switch.
     */
    public static <E extends Enum<E>> NeverError unhandledEnum(final E value, final E... expected) {
        throw new NeverError("Unhandled enum value: " + value + " only expected: " + Arrays.stream(expected)
                .map(e -> e.name())
                .collect(Collectors.joining(",")));
    }

    static String check(final String message) {
        Objects.requireNonNull(message, "message");
        return message;
    }

    private static Throwable check(final Throwable cause) {
        Objects.requireNonNull(cause, "cause");
        return cause;
    }

    protected NeverError() {
        super();
    }

    public NeverError(final String message) {
        super(NeverError.check(message));
    }

    public NeverError(final Throwable cause) {
        super(NeverError.check(cause));
    }

    public NeverError(final String message, final Throwable cause) {
        super(NeverError.check(message), NeverError.check(cause));
    }
}
