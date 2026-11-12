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

package walkingkooka.predicate.character;

import org.junit.jupiter.api.Assertions;
import walkingkooka.test.Testing;
import walkingkooka.text.CharSequences;

/**
 * Mixin for any tests involving a {@link CharPredicate} that includes helpers to invoke and check
 * {@link CharPredicate#test(char)} results.
 */
public interface CharPredicateTesting extends Testing {

    default void testTrue(final CharPredicate predicate,
                          final char c) {
        if (false == predicate.test(c)) {
            Assertions.fail(predicate + " did not match=" + CharSequences.quoteAndEscape(c));
        }
    }

    default void testFalse(final CharPredicate predicate,
                           final char c) {
        if (predicate.test(c)) {
            Assertions.fail(predicate + " should not have matched=" + CharSequences.quoteAndEscape(c));
        }
    }
}
