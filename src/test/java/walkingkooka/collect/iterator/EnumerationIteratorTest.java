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

package walkingkooka.collect.iterator;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.enumeration.Enumerations;
import walkingkooka.collect.list.Lists;

import java.util.Enumeration;
import java.util.Vector;

final public class EnumerationIteratorTest
        extends IteratorTestCase<EnumerationIterator<Integer>, Integer> {

    @Test
    public void testWithNullEnumeration() {
        try {
            EnumerationIterator.adapt(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
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
        this.checkRemoveFails(iterator);
    }

    @Test
    public void testToString() {
        final Enumeration<Object> enumeration = Enumerations.fake();
        Assert.assertEquals(enumeration.toString(),
                EnumerationIterator.adapt(enumeration).toString());
    }

    @Override
    protected EnumerationIterator<Integer> createIterator() {
        return EnumerationIterator.adapt(Enumerations.fake());
    }

    @Override
    protected Class<EnumerationIterator<Integer>> type() {
        return Cast.to(EnumerationIterator.class);
    }
}
