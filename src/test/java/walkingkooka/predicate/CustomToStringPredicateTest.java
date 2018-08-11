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
 */
package walkingkooka.predicate;

import org.junit.Test;
import walkingkooka.Cast;

import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public final class CustomToStringPredicateTest extends PredicateTestCase<CustomToStringPredicate<String>, String>{

    private final static String STRING = "abc";
    private final static Predicate<String> WRAPPED = Predicates.is(STRING);
    private final static String CUSTOM_TO_STRING = "!!abc!!";

    @Test(expected = NullPointerException.class)
    public void testWrapNullPredicateFails() {
        CustomToStringPredicate.wrap(null, CUSTOM_TO_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testWrapNullToStringFails() {
        CustomToStringPredicate.wrap(WRAPPED, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapEmptyToStringFails() {
        CustomToStringPredicate.wrap(WRAPPED, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapWhitespaceToStringFails() {
        CustomToStringPredicate.wrap(WRAPPED, " \t");
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
        assertSame("wrapped predicate", WRAPPED, wrapped.predicate);
        assertSame("wrapped toString", CUSTOM_TO_STRING, wrapped.toString);
    }

    @Test
    public void testTest() {
        this.testTrue(STRING);
    }

    @Test
    public void testTestFalse() {
        this.testFalse("different");
    }

    @Test
    public void testToString() {
        assertEquals(CUSTOM_TO_STRING, this.createPredicate().toString());
    }

    @Override
    public void testNullFails() {
        // nop
    }

    @Override
    protected CustomToStringPredicate<String> createPredicate() {
        return Cast.to(CustomToStringPredicate.wrap(WRAPPED, CUSTOM_TO_STRING));
    }

    @Override
    protected Class<CustomToStringPredicate<String>> type() {
        return Cast.to(CustomToStringPredicate.class);
    }
}
