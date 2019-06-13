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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.enumeration.Enumerations;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.util.Enumeration;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class EnumerationIterableTest implements ClassTesting2<EnumerationIterable<Object>>,
        IterableTesting<EnumerationIterable<Object>, Object> {

    @Test
    public void testWithNullEnumerationFails() {
        assertThrows(NullPointerException.class, () -> {
            EnumerationIterable.with(null);
        });
    }

    @Test
    public void testIterable() {
        final Enumeration<Object> enumeration = this.createEnumeration();
        final EnumerationIterable<Object> iterable = EnumerationIterable.with(enumeration);
        final Iterator<Object> iterator = iterable.iterator();
        assertEquals("1", iterator.next());
        assertEquals("2", iterator.next());
        assertFalse(iterator.hasNext(), "iterator should be empty=" + iterator);
    }

    @Test
    public void testIterableTwiceFails() {
        final Iterable<Object> iterable = this.createIterable();
        iterable.iterator();
        assertThrows(IllegalStateException.class, () -> {
            iterable.iterator();
        });
    }

    @Test
    public void testToString() {
        final Enumeration<Object> enumeration = this.createEnumeration();
        this.toStringAndCheck(EnumerationIterable.with(enumeration), enumeration.toString());
    }

    @Override
    public EnumerationIterable<Object> createIterable() {
        return EnumerationIterable.with(this.createEnumeration());
    }

    private Enumeration<Object> createEnumeration() {
        return Enumerations.array(new Object[]{"1", "2"});
    }

    @Override
    public Class<EnumerationIterable<Object>> type() {
        return Cast.to(EnumerationIterable.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
