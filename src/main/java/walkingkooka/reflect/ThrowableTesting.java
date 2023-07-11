/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.reflect;

import walkingkooka.test.Testing;

/**
 * An interface with default methods which may be mixed into a test.
 */
public interface ThrowableTesting extends Testing {

    default void checkThrowable(final Throwable throwable, final String message, final Throwable cause) {
        this.checkMessage(throwable, message);
        this.checkCause(throwable, cause);
    }

    default void checkMessage(final Throwable throwable, final String message) {
        this.checkEquals(message, throwable.getMessage(), "message");
    }

    default void checkCause(final Throwable throwable, final Throwable cause) {
        this.checkEquals(cause, throwable.getCause(), () -> "cause of " + throwable);
    }
}
