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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.text.CharSequences;

final public class ObjectEqualityPredicateTest
        extends PredicateTestCase<ObjectEqualityPredicate<String>, String> {

    final private static String MAGIC = "magic\n";

    // tests

    @Test
    public void testWithNullFails() {
        try {
            ObjectEqualityPredicate.with(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testWith() {
        final ObjectEqualityPredicate<Object> predicate = ObjectEqualityPredicate.with("String");
        Assert.assertNotNull(predicate);
    }

    @Override
    @Test
    public void testNullFails() {
        // nop
    }

    @Test
    public void testMatched() {
        this.testTrue(ObjectEqualityPredicateTest.MAGIC);
    }

    @Test
    public void testMatchedButDifferentInstance() {
        this.testTrue(new String(ObjectEqualityPredicateTest.MAGIC));
    }

    @Test
    public void testDifferent() {
        this.testFalse("Unknown");
    }

    @Test
    public void testToStringWithNoneString() {
        Assert.assertEquals("1", ObjectEqualityPredicate.with(1L).toString());
    }

    @Test
    public void testToStringWithString() {
        Assert.assertEquals(CharSequences.quoteAndEscape(ObjectEqualityPredicateTest.MAGIC)
                .toString(), this.createPredicate().toString());
    }

    @Override
    protected ObjectEqualityPredicate<String> createPredicate() {
        return Cast.to(ObjectEqualityPredicate.with(ObjectEqualityPredicateTest.MAGIC));
    }

    @Override
    protected Class<ObjectEqualityPredicate<String>> type() {
        return Cast.to(ObjectEqualityPredicate.class);
    }
}
