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

import java.util.Objects;

/**
 * This {@link Error} should only be thrown by code that should literally never happen.
 */
public class ShouldNeverHappenError extends Error {

    private static final long serialVersionUID = -4521229079992257870L;

    static String check(final String message) {
        Objects.requireNonNull(message, "message");
        return message;
    }

    private static Throwable check(final Throwable cause) {
        Objects.requireNonNull(cause, "cause");
        return cause;
    }

    protected ShouldNeverHappenError() {
        super();
    }

    public ShouldNeverHappenError(final String message) {
        super(ShouldNeverHappenError.check(message));
    }

    public ShouldNeverHappenError(final Throwable cause) {
        super(ShouldNeverHappenError.check(cause));
    }

    public ShouldNeverHappenError(final String message, final Throwable cause) {
        super(ShouldNeverHappenError.check(message), ShouldNeverHappenError.check(cause));
    }
}
