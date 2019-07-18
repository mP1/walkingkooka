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

import walkingkooka.text.CharSequences;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixing that helps test a {@link Predicate} and an argument.
 */
public interface PredicateTesting {

    default <TT> void testTrue(final Predicate<TT> predicate, final TT value) {
        assertEquals(true,
                predicate.test(value),
                () -> predicate + " should match=" + CharSequences.quoteIfChars(value));
    }

    default <TT> void testFalse(final Predicate<TT> predicate, final TT value) {
        assertEquals(false,
                predicate.test(value),
                () -> predicate + " should not match=" + CharSequences.quoteIfChars(value));
    }
}
