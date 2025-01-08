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

package walkingkooka.collect.iterator;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.util.Enumeration;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class EnumerationIteratorTest
    extends IteratorTestCase<EnumerationIterator<Integer>, Integer> {

    @Test
    public void testWithNullEnumerationFails() {
        assertThrows(NullPointerException.class, () -> EnumerationIterator.adapt(null));
    }

    @Test
    public void testIterate() {
        final Vector<String> vector = Lists.vector();
        vector.add("1");
        vector.add("2");
        vector.add("3");

        this.iterateUsingHasNextAndCheck(EnumerationIterator.adapt(vector.elements()),
            "1",
            "2",
            "3");
    }

    @Test
    public void testRemove() {
        final Vector<String> vector = Lists.vector();
        vector.add("1");

        final EnumerationIterator<String> iterator = EnumerationIterator.adapt(vector.elements());
        iterator.next();
        this.removeUnsupportedFails(iterator);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(EnumerationIterator.adapt(ENUMERATION), ENUMERATION.toString());
    }

    @Override
    public EnumerationIterator<Integer> createIterator() {
        return EnumerationIterator.adapt(ENUMERATION);
    }

    private static final Enumeration<Integer> ENUMERATION = new Enumeration<>() {
        @Override
        public boolean hasMoreElements() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Integer nextElement() {
            throw new UnsupportedOperationException();
        }
    };

    @Override
    public Class<EnumerationIterator<Integer>> type() {
        return Cast.to(EnumerationIterator.class);
    }
}
