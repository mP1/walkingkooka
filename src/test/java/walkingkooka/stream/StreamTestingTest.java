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

package walkingkooka.stream;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Uses the {@link List#stream()} to test all the helpers in {@link StreamTesting}.
 */
public final class StreamTestingTest implements StreamTesting<Stream<Integer>, Integer> {

    // count............................................................................................................

    @Test
    public void testCountNone() {
        this.countAndCheck(empty(), 0);
    }

    // filter............................................................................................................

    @Test
    public void testFilter() {
        this.collectAndCheck(stream(11, 22, 33, 44, 55).filter(
                (v) -> v < 33),
            11, 22);
    }

    @Test
    public void testFilter2() {
        this.collectAndCheck(stream(11, 55, 22, 44, 33).filter(
                (v) -> v < 33),
            11, 22);
    }

    // filter, map, skip and collect.....................................................................................

    @Test
    public void testFilterMapSkipAndCollect() {
        this.collectAndCheck(stream(11, 22, 33, 44, 55)
                .filter((v) -> v > 22) // 33, 44, 55
                .map(v -> v * 10) // 330, 440, 550
                .skip(1), // 440, 550
            440, 550);
    }

    // collect............................................................................................................

    @Test
    public void testCollect() {
        this.collectAndCheck(this::createStream, this.values());
    }

    // iterator............................................................................................................

    @Test
    public void testIterator() {
        this.iteratorAndCheck(this.createStream(), this.values());
    }

    // max............................................................................................................

    @Test
    public void testMax() {
        this.maxAndCheck(this.createStream(),
            Comparator.<Integer>naturalOrder()
                .reversed(),
            this.values());
    }

    @Test
    public void testMax2() {
        this.checkEquals(Optional.of(555),
            stream(1, 22, 33, 44, 555).max(Comparator.naturalOrder()));
    }

    @Test
    public void testMax3() {
        this.checkEquals(Optional.of(555),
            stream(1, 22, 555, 44, 33).max(Comparator.naturalOrder()));
    }

    @SuppressWarnings("RedundantComparatorComparing")
    @Test
    public void testMax4() {
        this.checkEquals(Optional.of(1),
            stream(1, 22, 555, 44, 33).max(Comparator.<Integer>naturalOrder().reversed()));
    }

    // min............................................................................................................

    @Test
    public void testMin() {
        this.minAndCheck(this.createStream(),
            Comparator.<Integer>naturalOrder()
                .reversed(),
            this.values());
    }

    @Test
    public void testMin2() {
        this.checkEquals(Optional.of(1),
            stream(1, 22, 33, 44, 555).min(Comparator.naturalOrder()));
    }

    @Test
    public void testMin3() {
        this.checkEquals(Optional.of(1),
            stream(555, 22, 1, 44, 33).min(Comparator.naturalOrder()));
    }

    @SuppressWarnings("RedundantComparatorComparing")
    @Test
    public void testMin4() {
        this.checkEquals(Optional.of(555),
            stream(1, 22, 555, 44, 33).min(Comparator.<Integer>naturalOrder().reversed()));
    }

    // reduce............................................................................................................

    @Test
    public void testReduceNothing() {
        this.reduceAndCheck(stream(),
            StreamTestingTest::reducer);
    }

    @Test
    public void testReduceSingleValue() {
        this.reduceAndCheck(stream(1),
            StreamTestingTest::reducer,
            1);
    }

    @Test
    public void testReduceManyValues() {
        this.reduceAndCheck(stream(1, 2, 3, 4),
            StreamTestingTest::reducer,
            1, 2, 3, 4);
    }

    // reduce............................................................................................................

    @Test
    public void testReduceInitial() {
        this.reduceAndCheck(stream(),
            1000,
            StreamTestingTest::reducer);
    }

    @Test
    public void testReduceInitialAndSingleValue() {
        this.reduceAndCheck(stream(1),
            1000,
            StreamTestingTest::reducer,
            1);
    }

    @Test
    public void testReduceInitialManyValues() {
        this.reduceAndCheck(stream(1, 2, 3, 4),
            1000,
            StreamTestingTest::reducer,
            1, 2, 3, 4);
    }

    private static Integer reducer(final Integer left, final Integer right) {
        return left + right;
    }

    // helpers..........................................................................................................

    @Override
    public Stream<Integer> createStream() {
        return stream(11, 2, 333, 44, -55);
    }

    public List<Integer> values() {
        return Lists.of(11, 2, 333, 44, -55);
    }

    private static Stream<Integer> empty() {
        return Lists.<Integer>empty().stream();
    }

    private static Stream<Integer> stream(final Integer... values) {
        return Arrays.stream(values);
    }
}
