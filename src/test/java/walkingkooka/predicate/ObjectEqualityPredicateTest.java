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
import walkingkooka.text.CharSequences;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ObjectEqualityPredicateTest
    extends PredicateTestCase<ObjectEqualityPredicate<String>, String>
    implements HashCodeEqualsDefinedTesting2<ObjectEqualityPredicate<String>> {

    final private static String MAGIC = "magic\n";

    // tests

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> ObjectEqualityPredicate.with(null));
    }

    @Test
    public void testWith() {
        final ObjectEqualityPredicate<Object> predicate = ObjectEqualityPredicate.with("String");
        assertNotNull(predicate);
    }

    @Test
    public void testTestNull() {
        this.testFalse(null);
    }

    @Test
    public void testMatched() {
        this.testTrue(MAGIC);
    }

    @Test
    public void testTestButDifferentInstance() {
        //noinspection StringOperationCanBeSimplified
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
        this.toStringAndCheck(ObjectEqualityPredicate.with(1L), "1");
    }

    @Test
    public void testToStringWithString() {
        this.toStringAndCheck(this.createPredicate(),
            CharSequences.quoteAndEscape(MAGIC).toString());
    }

    @Override
    public ObjectEqualityPredicate<String> createPredicate() {
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
}
