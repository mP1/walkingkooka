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

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MappingIteratorTest extends IteratorTestCase<MappingIterator<String, Integer>, Integer> {

    @Test
    public void testWithNullIteratorFails() {
        assertThrows(NullPointerException.class, () -> MappingIterator.<String, Integer>with(null, Integer::parseInt));
    }

    @Test
    public void testWithNullMapperFunctionFails() {
        assertThrows(NullPointerException.class, () -> MappingIterator.with(Iterators.array("11", "22", "33"), null));
    }

    @Test
    public void testIterateAndCheck() {
        this.iterateAndCheck(this.createIterator(), 11, 22, 33);
    }

    @Test
    public void testRemove() {
        final List<String> values = Lists.array();
        values.add("1");
        values.add("2");
        values.add("3");

        final Iterator<Integer> iterator = MappingIterator.with(values.iterator(), Integer::parseInt);
        iterator.next();
        iterator.remove();

        this.checkEquals(Lists.of("2", "3"), values);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createIterator(), this.wrappedIterator().toString());
    }

    @Override
    public MappingIterator<String, Integer> createIterator() {
        return MappingIterator.with(this.wrappedIterator(), Integer::parseInt);
    }

    private Iterator<String> wrappedIterator() {
        return Iterators.array("11", "22", "33");
    }

    @Override
    public Class<MappingIterator<String, Integer>> type() {
        return Cast.to(MappingIterator.class);
    }
}
