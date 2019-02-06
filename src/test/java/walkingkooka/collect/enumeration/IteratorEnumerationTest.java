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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.list.Lists;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class IteratorEnumerationTest
        extends EnumerationTestCase<IteratorEnumeration<Object>, Object> {

    // constants

    private final static Object FIRST = "1st element";

    private final static Object SECOND = "2nd element";

    private final static Object THIRD = "3rd element";

    // tests

    @Test
    public void testNullIteratorFails() {
        assertThrows(NullPointerException.class, () -> {
            IteratorEnumeration.adapt(null);
        });
    }

    @Test
    public void testConsume() {
        final IteratorEnumeration<Object> enumeration = this.createEnumeration();
        assertTrue(enumeration.hasMoreElements());
        assertSame(FIRST, enumeration.nextElement());

        assertTrue(enumeration.hasMoreElements());
        assertSame(SECOND, enumeration.nextElement());

        assertTrue(enumeration.hasMoreElements());
        assertSame(THIRD, enumeration.nextElement());

        assertFalse(enumeration.hasMoreElements(), "enumeration(iterator) should be empty");

        this.checkNextElementFails(enumeration);
    }

    @Test
    public void testToString() {
        final Iterator<Object> iterator = Iterators.fake();
        this.toStringAndCheck(IteratorEnumeration.adapt(iterator), iterator.toString());
    }

    @Override
    protected IteratorEnumeration<Object> createEnumeration() {
        return IteratorEnumeration.adapt(Lists.of(FIRST,
                SECOND,
                THIRD).iterator());
    }

    @Override
    public Class<IteratorEnumeration<Object>> type() {
        return Cast.to(IteratorEnumeration.class);
    }
}
