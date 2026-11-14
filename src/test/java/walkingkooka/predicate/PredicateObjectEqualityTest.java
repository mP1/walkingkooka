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

final public class PredicateObjectEqualityTest
    extends PredicateTestCase<PredicateObjectEquality<String>, String>
    implements HashCodeEqualsDefinedTesting2<PredicateObjectEquality<String>> {

    final private static String MAGIC = "magic\n";

    // tests

    @Test
    public void testWith() {
        final PredicateObjectEquality<Object> predicate = PredicateObjectEquality.with("String");
        assertNotNull(predicate);
    }

    @Test
    public void testWithNull() {
        final PredicateObjectEquality<Object> predicate = PredicateObjectEquality.with(null);
        assertNotNull(predicate);
    }

    @Test
    public void testTestNull() {
        this.testFalse(null);
    }

    @Test
    public void testTestNullWhenNull() {
        this.testTrue(
            PredicateObjectEquality.with(null),
            null
        );
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
        this.checkNotEquals(PredicateObjectEquality.with("different"));
    }

    @Test
    public void testToStringWithNoneString() {
        this.toStringAndCheck(PredicateObjectEquality.with(1L), "1");
    }

    @Test
    public void testToStringWithString() {
        this.toStringAndCheck(this.createPredicate(),
            CharSequences.quoteAndEscape(MAGIC).toString());
    }

    @Override
    public PredicateObjectEquality<String> createPredicate() {
        return Cast.to(PredicateObjectEquality.with(MAGIC));
    }

    @Override
    public Class<PredicateObjectEquality<String>> type() {
        return Cast.to(PredicateObjectEquality.class);
    }

    @Override
    public PredicateObjectEquality<String> createObject() {
        return this.createPredicate();
    }
}
