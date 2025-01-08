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
import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.CharSequences;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Mixin for any tests involving a {@link CharPredicate} that includes helpers to invoke and check
 * {@link CharPredicate#test(char)} results.
 */
public interface CharPredicateTesting<P extends CharPredicate>
    extends ToStringTesting<P>,
    TypeNameTesting<P> {

    @Test
    default void testAnd() {
        final P predicate = this.createCharPredicate();
        assertSame(predicate, predicate.and(predicate));
    }

    @Test
    default void testNotNot() {
        final P predicate = this.createCharPredicate();
        this.checkEquals(predicate, predicate.negate().negate());
    }

    @Test
    default void testOr() {
        final P predicate = this.createCharPredicate();
        assertSame(predicate, predicate.or(predicate));
    }

    @Test
    default void testSetToStringSame() {
        final P predicate = this.createCharPredicate();
        assertSame(predicate, predicate.setToString(predicate.toString()));
    }

    P createCharPredicate();

    default boolean test(final char c) {
        return this.createCharPredicate().test(c);
    }

    default void testTrue(final char c) {
        this.testTrue(this.createCharPredicate(), c);
    }

    default void testTrue(final CharPredicate predicate, final char c) {
        if (false == predicate.test(c)) {
            Assertions.fail(predicate + " did not match=" + CharSequences.quoteAndEscape(c));
        }
    }

    default void testFalse(final char c) {
        this.testFalse(this.createCharPredicate(), c);
    }

    default void testFalse(final CharPredicate predicate, final char c) {
        if (predicate.test(c)) {
            Assertions.fail(predicate + " should not have matched=" + CharSequences.quoteAndEscape(c));
        }
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return CharPredicate.class.getSimpleName();
    }
}
