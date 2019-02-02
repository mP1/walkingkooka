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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.text.CharSequences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ObjectEqualityPredicateTest
        extends PredicateTestCase<ObjectEqualityPredicate<String>, String>
        implements HashCodeEqualsDefinedTesting<ObjectEqualityPredicate<String>>,
        SerializationTesting<ObjectEqualityPredicate<String>> {

    final private static String MAGIC = "magic\n";

    // tests

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            ObjectEqualityPredicate.with(null);
        });
    }

    @Test
    public void testWith() {
        final ObjectEqualityPredicate<Object> predicate = ObjectEqualityPredicate.with("String");
        assertNotNull(predicate);
    }

    @Override
    public void testTestNullFails() {
        // nop
    }

    @Test
    public void testMatched() {
        this.testTrue(MAGIC);
    }

    @Test
    public void testTestButDifferentInstance() {
        this.testTrue(new String(MAGIC));
    }

    @Test
    public void testTestEqualsDifferent() {
        this.testFalse("Unknown");
    }

    @Test
    public void testTestEqualsDifferentValue() {
        this.checkNotEquals(ObjectEqualityPredicate.with("different"));
    }

    @Test
    public void testToStringWithNoneString() {
        assertEquals("1", ObjectEqualityPredicate.with(1L).toString());
    }

    @Test
    public void testToStringWithString() {
        assertEquals(CharSequences.quoteAndEscape(MAGIC)
                .toString(), this.createPredicate().toString());
    }

    @Override
    protected ObjectEqualityPredicate<String> createPredicate() {
        return Cast.to(ObjectEqualityPredicate.with(MAGIC));
    }

    @Override
    public Class<ObjectEqualityPredicate<String>> type() {
        return Cast.to(ObjectEqualityPredicate.class);
    }

    @Override
    public ObjectEqualityPredicate<String> createObject() {
        return this.createPredicate();
    }

    @Override
    public ObjectEqualityPredicate<String> serializableInstance() {
        return ObjectEqualityPredicate.with("*string*");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
