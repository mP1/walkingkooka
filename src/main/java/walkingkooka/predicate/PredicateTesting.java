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

package walkingkooka.predicate;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Base class for testing a {@link Predicate} with mostly parameter checking tests and various
 * utility methods toassert matching and non matching.
 */
public interface PredicateTesting<P extends Predicate<T>, T>
        extends ToStringTesting<P>,
        TypeNameTesting<P> {

    @Test
    default void testTestNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.test(null);
        });
    }

    P createPredicate();

    default boolean test(final T value) {
        return this.createPredicate().test(value);
    }

    default void testTrue(final T value) {
        this.testTrue(this.createPredicate(), value);
    }

    default <TT> void testTrue(final Predicate<TT> predicate, final TT value) {
        if (false == predicate.test(value)) {
            fail(predicate + " did not match=" + value);
        }
    }

    default void testFalse(final T value) {
        this.testFalse(this.createPredicate(), value);
    }

    default <TT> void testFalse(final Predicate<TT> predicate, final TT value) {
        if (predicate.test(value)) {
            fail(predicate + " should not have matched=" + value);
        }
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Predicate.class.getSimpleName();
    }
}
