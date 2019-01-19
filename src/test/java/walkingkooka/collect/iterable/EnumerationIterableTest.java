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

package walkingkooka.collect.iterable;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.enumeration.Enumerations;

import java.util.Enumeration;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

final public class EnumerationIterableTest
        extends IterableTestCase<EnumerationIterable<Object>, Object> {

    @Test
    public void testWithNullEnumerationFails() {
        try {
            EnumerationIterable.with(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testIterable() {
        final Enumeration<Object> enumeration = this.createEnumeration();
        final EnumerationIterable<Object> iterable = EnumerationIterable.with(enumeration);
        final Iterator<Object> iterator = iterable.iterator();
        assertEquals("1", iterator.next());
        assertEquals("2", iterator.next());
        assertFalse("iterator should be empty=" + iterator, iterator.hasNext());
    }

    @Test
    public void testIterableTwiceFails() {
        final Iterable<Object> iterable = this.createIterable();
        iterable.iterator();
        try {
            iterable.iterator();
            Assert.fail();
        } catch (final IllegalStateException expected) {
        }
    }

    @Test
    public void testToString() {
        final Enumeration<Object> enumeration = this.createEnumeration();
        assertEquals(enumeration.toString(),
                EnumerationIterable.with(enumeration).toString());
    }

    @Override
    protected EnumerationIterable<Object> createIterable() {
        return EnumerationIterable.with(this.createEnumeration());
    }

    private Enumeration<Object> createEnumeration() {
        return Enumerations.array(new Object[]{"1", "2"});
    }

    @Override
    protected Class<EnumerationIterable<Object>> type() {
        return Cast.to(EnumerationIterable.class);
    }
}
