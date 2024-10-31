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

import walkingkooka.Cast;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A {@link Predicate} that returns true if any of the provided {@link Predicate#test(Object)} return true.
 */
final class AnyPredicate<T> implements Predicate<T> {

    static <T> AnyPredicate<T> with(final Collection<Predicate<T>> predicates) {
        return new AnyPredicate<>(
                Objects.requireNonNull(predicates, "predicates")
        );
    }

    private AnyPredicate(final Collection<Predicate<T>> predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean test(final T value) {
        boolean result = false;

        for(final Predicate<T> predicate : this.predicates) {
            result = predicate.test(value);
            if(result) {
                break;
            }
        }

        return result;
    }

    private final Collection<Predicate<T>> predicates;

    // Object ..........................................................................................................

    @Override
    public int hashCode() {
        return this.predicates.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof AnyPredicate && this.equals0(Cast.to(other));
    }

    private boolean equals0(final AnyPredicate<?> other) {
        return this.predicates.equals(other.predicates);
    }

    @Override
    public String toString() {
        return this.predicates.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" | "));
    }
}
