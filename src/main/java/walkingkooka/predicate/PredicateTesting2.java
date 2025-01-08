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

package walkingkooka.predicate;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.TypeNameTesting;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing that helps test a {@link Predicate} and an argument and some additional tests.
 */
public interface PredicateTesting2<P extends Predicate<T>, T>
    extends PredicateTesting,
    ToStringTesting<P>,
    TypeNameTesting<P> {

    @Test
    default void testTestNullFails() {
        assertThrows(NullPointerException.class, () -> this.test(null));
    }

    P createPredicate();

    default boolean test(final T value) {
        return this.createPredicate().test(value);
    }

    default void testTrue(final T value) {
        this.testTrue(this.createPredicate(), value);
    }

    default void testFalse(final T value) {
        this.testFalse(this.createPredicate(), value);
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
