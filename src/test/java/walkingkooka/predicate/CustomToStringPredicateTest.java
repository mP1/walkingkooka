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
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CustomToStringPredicateTest extends PredicateTestCase<CustomToStringPredicate<String>, String>
    implements HashCodeEqualsDefinedTesting2<CustomToStringPredicate<String>> {

    private final static String STRING = "abc";
    private final static Predicate<String> WRAPPED = Predicates.is(STRING);
    private final static String CUSTOM_TO_STRING = "!!abc!!";

    @Test
    public void testWrapNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> CustomToStringPredicate.wrap(null, CUSTOM_TO_STRING));
    }

    @Test
    public void testWrapNullToStringFails() {
        assertThrows(NullPointerException.class, () -> CustomToStringPredicate.wrap(WRAPPED, null));
    }

    @Test
    public void testDoesntWrapEquivalentToString() {
        assertSame(WRAPPED, CustomToStringPredicate.wrap(WRAPPED, WRAPPED.toString()));
    }

    @Test
    public void testUnwrapOtherCustomToStringPredicate() {
        final Predicate<String> first = CustomToStringPredicate.wrap(WRAPPED, "different");
        final CustomToStringPredicate<String> wrapped = Cast.to(CustomToStringPredicate.wrap(first, CUSTOM_TO_STRING));
        assertNotSame(first, wrapped);
        assertSame(WRAPPED, wrapped.predicate, "wrapped predicate");
        assertSame(CUSTOM_TO_STRING, wrapped.toString, "wrapped toString");
    }

    @Test
    public void testWithEmptyStringCustomString() {
        final Predicate<String> first = CustomToStringPredicate.wrap(
            WRAPPED,
            ""
        );
        final CustomToStringPredicate<String> wrapped = Cast.to(CustomToStringPredicate.wrap(first, CUSTOM_TO_STRING));
        assertNotSame(first, wrapped);
        assertSame(WRAPPED, wrapped.predicate, "wrapped predicate");
        assertSame(CUSTOM_TO_STRING, wrapped.toString, "wrapped toString");

        this.checkEquals(
            "",
            first.toString()
        );
    }

    @Test
    public void testTestNull() {
        this.testFalse(null);
    }

    @Test
    public void testTestTrue() {
        this.testTrue(STRING);
    }

    @Test
    public void testTestFalse() {
        this.testFalse("different");
    }

    @Test
    public void testEqualsDifferentWrappedPredicate() {
        this.checkNotEquals(CustomToStringPredicate.wrap(Predicate.isEqual("different"), CUSTOM_TO_STRING));
    }

    @Test
    public void testEqualsDifferentCustomToString() {
        this.checkNotEquals(CustomToStringPredicate.wrap(WRAPPED, "different"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPredicate(), CUSTOM_TO_STRING);
    }

    @Override
    public CustomToStringPredicate<String> createPredicate() {
        return Cast.to(CustomToStringPredicate.wrap(WRAPPED, CUSTOM_TO_STRING));
    }

    @Override
    public Class<CustomToStringPredicate<String>> type() {
        return Cast.to(CustomToStringPredicate.class);
    }

    @Override
    public CustomToStringPredicate<String> createObject() {
        return this.createPredicate();
    }
}
