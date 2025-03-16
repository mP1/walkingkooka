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

package walkingkooka.text;

import java.util.Objects;

/**
 * A {@link java.util.function.Predicate} that test any {@link CharSequence} that ends with a {@link
 * String} which may or may not ignoring the case.
 */
final class CaseSensitivityCharSequencePredicateEndsWith<C extends CharSequence>
    extends CaseSensitivityCharSequencePredicate<C> {

    /**
     * Creates a {@link CaseSensitivityCharSequencePredicateEndsWith}
     */
    static <C extends CharSequence> CaseSensitivityCharSequencePredicateEndsWith<C> with(
        final C startsWith, final CaseSensitivity sensitivity) {
        CharSequences.failIfNullOrEmpty(startsWith, "startsWith");
        Objects.requireNonNull(sensitivity, "sensitivity");

        return new CaseSensitivityCharSequencePredicateEndsWith<>(startsWith, sensitivity);
    }

    /**
     * Private constructor use static factory.
     */
    private CaseSensitivityCharSequencePredicateEndsWith(final C startsWith,
                                                         final CaseSensitivity sensitivity) {
        super(startsWith, sensitivity);
    }

    @Override
    public boolean test(final C value) {
        return this.sensitivity.endsWith(value, this.chars);
    }

    @Override
    String toStringPrefix() {
        return "ends with ";
    }
}
