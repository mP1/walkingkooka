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
 *
 */

package walkingkooka.util.stream;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;

import java.util.Arrays;
import java.util.List;
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
        this.collectAndCheck(() -> this.createStream(), this.values());
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
        return Arrays.asList(values).stream();
    }
}
