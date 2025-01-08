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

package walkingkooka;

import walkingkooka.test.Testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A mixin that contains helpers for testing two objects {@link Object#equals(Object)} and {@link Object#hashCode()}}
 */
public interface HashCodeEqualsDefinedTesting extends Testing {

    default void checkEqualsAndHashCode(final Object expected, final Object actual) {
        checkEquals(expected, actual);

        this.checkEquals(
            expected.hashCode(),
            actual.hashCode(),
            "objects that are equal should have equal hashcodes"
        );
    }

    default void checkHashCode(final Object expected, final Object actual) {
        assertEquals(
            expected.hashCode(),
            actual.hashCode(),
            () -> expected + "\n" + actual
        );
    }
}
