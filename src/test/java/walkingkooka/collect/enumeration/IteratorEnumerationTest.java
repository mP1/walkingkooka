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

package walkingkooka.collect.enumeration;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.list.Lists;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

final public class IteratorEnumerationTest
        extends EnumerationTestCase<IteratorEnumeration<Object>, Object> {

    // constants

    private final static Object FIRST = "1st element";

    private final static Object SECOND = "2nd element";

    private final static Object THIRD = "3rd element";

    // tests

    @Test
    public void testNullIteratorFails() {
        try {
            IteratorEnumeration.adapt(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testConsume() {
        final IteratorEnumeration<Object> enumeration = this.createEnumeration();
        Assert.assertTrue(enumeration.hasMoreElements());
        assertSame(IteratorEnumerationTest.FIRST, enumeration.nextElement());

        Assert.assertTrue(enumeration.hasMoreElements());
        assertSame(IteratorEnumerationTest.SECOND, enumeration.nextElement());

        Assert.assertTrue(enumeration.hasMoreElements());
        assertSame(IteratorEnumerationTest.THIRD, enumeration.nextElement());

        Assert.assertFalse("enumeration(iterator) should be empty", enumeration.hasMoreElements());

        try {
            enumeration.nextElement();
            Assert.fail();
        } catch (final NoSuchElementException expected) {
        }
    }

    @Test
    public void testToString() {
        final Iterator<Object> iterator = Iterators.fake();
        assertEquals(iterator.toString(), IteratorEnumeration.adapt(iterator).toString());
    }

    @Override
    protected IteratorEnumeration<Object> createEnumeration() {
        return IteratorEnumeration.adapt(Lists.of(IteratorEnumerationTest.FIRST,
                IteratorEnumerationTest.SECOND,
                IteratorEnumerationTest.THIRD).iterator());
    }

    @Override
    protected Class<IteratorEnumeration<Object>> type() {
        return Cast.to(IteratorEnumeration.class);
    }
}
