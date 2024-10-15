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

import java.util.function.Supplier;

/**
 * Mixing interface that provides methods to test a {@link CanBeEmpty}
 */
public interface CanBeEmptyTesting extends Testing {

    // isEmpty..........................................................................................................

    default void isEmptyAndCheck(final CanBeEmpty canBeEmpty,
                                 final boolean expected) {
        this.isEmptyAndCheck(
                canBeEmpty,
                expected,
                () -> canBeEmpty.toString()
        );
    }

    default void isEmptyAndCheck(final CanBeEmpty canBeEmpty,
                                 final boolean expected,
                                 final String message) {
        this.isEmptyAndCheck(
                canBeEmpty,
                expected,
                () -> message
        );
    }

    default void isEmptyAndCheck(final CanBeEmpty canBeEmpty,
                                 final boolean expected,
                                 final Supplier<String> message) {
        this.checkEquals(
                expected,
                canBeEmpty.isEmpty(),
                message
        );
    }

    // isNotEmpty..........................................................................................................

    default void isNotEmptyAndCheck(final CanBeEmpty canBeEmpty,
                                    final boolean expected) {
        this.isNotEmptyAndCheck(
                canBeEmpty,
                expected,
                () -> canBeEmpty.toString()
        );
    }

    default void isNotEmptyAndCheck(final CanBeEmpty canBeEmpty,
                                    final boolean expected,
                                    final String message) {
        this.isNotEmptyAndCheck(
                canBeEmpty,
                expected,
                () -> message
        );
    }

    default void isNotEmptyAndCheck(final CanBeEmpty canBeEmpty,
                                    final boolean expected,
                                    final Supplier<String> message) {
        this.checkEquals(
                expected,
                canBeEmpty.isNotEmpty(),
                message
        );
    }
}
