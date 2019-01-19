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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;

import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public final class CustomToStringPredicateTest extends PredicateTestCase<CustomToStringPredicate<String>, String>
        implements HashCodeEqualsDefinedTesting<CustomToStringPredicate<String>>,
        SerializationTesting<CustomToStringPredicate<String>> {

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
    @Ignore
    @Override
    public void testTestNullFails() {
        throw new UnsupportedOperationException();
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
        this.checkNotEquals(CustomToStringPredicate.wrap(Predicate.isEqual("different"),CUSTOM_TO_STRING));
    }

    @Test
    public void testEqualsDifferentCustomToString() {
        this.checkNotEquals(CustomToStringPredicate.wrap(WRAPPED,"different"));
    }

    @Test
    public void testToString() {
        assertEquals(CUSTOM_TO_STRING, this.createPredicate().toString());
    }

    @Override
    protected CustomToStringPredicate<String> createPredicate() {
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

    @Override
    public CustomToStringPredicate<String> serializableInstance() {
        return Cast.to(CustomToStringPredicate.wrap(Predicates.is("abc123"), "custom-to-string"));
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
