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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.predicate.Predicates;
import walkingkooka.test.Testing;
import walkingkooka.text.CharSequences;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface StreamTesting<S extends Stream<T>, T> extends Testing {

    @Test
    default void testValuesNotEmpty() {
        final List<T> values = this.values();
        assertNotEquals(0,
                values,
                () -> "" + values);
    }

    // allMatch..........................................................................................................

    @Test
    default void testAllMatchAllTrue() {
        this.allMatchAndCheck(this.createStream(),
                Predicates.always(),
                true);
    }

    @Test
    default void testAllMatchNoneFalse() {
        this.allMatchAndCheck(this.createStream(),
                Predicates.never(),
                false);
    }

    @Test
    default void testAllMatchNoneSome() {
        final List<T> values = this.values();
        this.allMatchAndCheck(values.stream(),
                (v) -> values.indexOf(v) < values.size() / 2,
                false);
    }

    // anyMatch..........................................................................................................

    @Test
    default void testAnyMatchAllTrue() {
        this.anyMatchAndCheck(this.createStream(),
                Predicates.always(),
                true);
    }

    @Test
    default void testAnyMatchNoneFalse() {
        this.anyMatchAndCheck(this.createStream(),
                Predicates.never(),
                false);
    }

    @Test
    default void testAnyMatchFirst() {
        final List<T> values = this.values();
        this.anyMatchAndCheck(values.stream(),
                (v) -> values.indexOf(v) == 0,
                true);
    }

    @Test
    default void testAnyMatchMid() {
        final List<T> values = this.values();
        this.anyMatchAndCheck(values.stream(),
                (v) -> values.indexOf(v) == values.size() / 2,
                true);
    }

    @Test
    default void testAnyMatchLast() {
        final List<T> values = this.values();
        this.anyMatchAndCheck(values.stream(),
                (v) -> values.indexOf(v) == values.size() - 1,
                true);
    }

    // noneMatch..........................................................................................................

    @Test
    default void testNoneMatchAllTrue() {
        this.noneMatchAndCheck(this.createStream(),
                Predicates.always(),
                false);
    }

    @Test
    default void testNoneMatchNoneFalse() {
        this.noneMatchAndCheck(this.createStream(),
                Predicates.never(),
                true);
    }

    @Test
    default void testNoneMatchFirst() {
        final List<T> values = this.values();
        this.noneMatchAndCheck(values.stream(),
                (v) -> values.indexOf(v) == 0,
                false);
    }

    @Test
    default void testNoneMatchMid() {
        final List<T> values = this.values();
        this.noneMatchAndCheck(values.stream(),
                (v) -> values.indexOf(v) == values.size() / 2,
                false);
    }

    @Test
    default void testNoneMatchLast() {
        final List<T> values = this.values();
        this.noneMatchAndCheck(values.stream(),
                (v) -> values.indexOf(v) == values.size() - 1,
                false);
    }

    // collect..........................................................................................................

    @Test
    default void testCollectNullCollectorFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createStream().collect(null);
        });
    }

    @Test
    default void testCollect() {
        this.collectAndCheck(this.createStream(), this.values());
    }

    // count..........................................................................................................

    @Test
    default void testCountNotEmpty() {
        this.countAndCheck(this.createStream(), this.values().size());
    }

    // filter............................................................................................................

    @Test
    default void testFilterNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createStream().filter(null);
        });
    }

    @Test
    default void testFilterKeepFirst() {
        final List<T> values = this.values();
        final T keep = values.get(0);

        this.collectAndCheck(this.createStream()
                        .filter(v -> v.equals(keep)),
                keep);
    }

    @Test
    default void testFilterRemoveFirst() {
        final List<T> values = this.values();
        final T removed = values.get(0);

        this.collectAndCheck(this.createStream()
                        .filter(v -> !v.equals(removed)),
                values.stream()
                        .filter(v -> !v.equals(removed))
                        .collect(Collectors.toList()));
    }

    @Test
    default void testFilterKeepMid() {
        final List<T> values = this.values();
        final T keep = values.get(values.size() / 2);

        this.collectAndCheck(this.createStream()
                        .filter(v -> v.equals(keep)),
                keep);
    }

    @Test
    default void testFilterRemoveMid() {
        final List<T> values = this.values();
        final T removed = values.get(values.size() / 2);

        this.collectAndCheck(this.createStream()
                        .filter(v -> !v.equals(removed)),
                values.stream()
                        .filter(v -> !v.equals(removed))
                        .collect(Collectors.toList()));
    }

    @Test
    default void testFilterKeepLast() {
        final List<T> values = this.values();
        final T keep = values.get(values.size() - 1);

        this.collectAndCheck(this.createStream()
                        .filter(v -> v.equals(keep)),
                keep);
    }

    @Test
    default void testFilterRemoveLast() {
        final List<T> values = this.values();
        final T removed = values.get(values.size() - 1);

        this.collectAndCheck(this.createStream()
                        .filter(v -> !v.equals(removed)),
                values.stream()
                        .filter(v -> !v.equals(removed))
                        .collect(Collectors.toList()));
    }

    // findFirst............................................................................................................

    @Test
    default void testFindFirstFirst() {
        final List<T> values = this.values();
        final T find = values.get(0);

        this.findFirstAndCheck(this.createStream()
                        .filter(Predicates.is(find)),
                find);
    }

    @Test
    default void testFindFirstMid() {
        final List<T> values = this.values();
        final T find = values.get(values.size() / 2);

        this.findFirstAndCheck(this.createStream()
                        .filter(Predicates.is(find)),
                find);
    }

    @Test
    default void testFindFirstLast() {
        final List<T> values = this.values();
        final T find = values.get(values.size() - 1);

        this.findFirstAndCheck(this.createStream()
                        .filter(Predicates.is(find)),
                find);
    }

    @Test
    default void testFindFirstNone() {
        this.findFirstAndCheckNone(this.createStream()
                .filter(Predicates.never()));
    }

    // findAny............................................................................................................

    @Test
    default void testFindAnyFirst() {
        final List<T> values = this.values();
        final T find = values.get(0);

        this.findAnyAndCheck(this.createStream()
                        .filter(Predicates.is(find)),
                values);
    }

    @Test
    default void testFindAnyMid() {
        final List<T> values = this.values();
        final T find = values.get(values.size() / 2);

        this.findAnyAndCheck(this.createStream()
                        .filter(Predicates.is(find)),
                values);
    }

    @Test
    default void testFindAnyLast() {
        final List<T> values = this.values();
        final T find = values.get(values.size() - 1);

        this.findAnyAndCheck(this.createStream()
                        .filter(Predicates.is(find)),
                values);
    }

    @Test
    default void testFindAnyNone() {
        this.findAnyAndCheckNone(this.createStream()
                .filter(Predicates.never()));
    }

    // forEach..........................................................................................................

    @Test
    default void testForEachNullConsumerFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createStream().forEach(null);
        });
    }

    @Test
    default void testForEach() {
        this.forEachAndCheck(this.createStream(), this.values());
    }

    // forEachOrdered..........................................................................................................

    @Test
    default void testForEachOrderedNullConsumerFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createStream().forEachOrdered(null);
        });
    }

    @Test
    default void testForEachOrdered() {
        this.forEachOrderedAndCheck(this.createStream(), this.values());
    }

    // peek..........................................................................................................

    @Test
    default void testPeekNullConsumerFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createStream().peek(null);
        });
    }

    @Test
    default void testPeek() {
        this.peekAndCheck(this.createStream(), this.values());
    }

    // limit..........................................................................................................

    @Test
    default void testLimitInvalidMaxSizeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createStream().limit(-1);
        });
    }

    @Test
    default void testLimitZero() {
        this.limitAndCheck(this.createStream()
                .limit(0));
    }

    @Test
    default void testLimitOne() {
        final List<T> values = this.values();

        this.limitAndCheck(this.createStream()
                        .limit(1),
                values.get(0));
    }

    @Test
    default void testLimitAllButLast() {
        final List<T> values = this.values();
        final int last = values.size() - 1;

        this.limitAndCheck(this.createStream()
                        .limit(last),
                values.subList(0, last));
    }

    @Test
    default void testLimitAll() {
        final List<T> values = this.values();

        this.limitAndCheck(this.createStream()
                        .limit(values.size()),
                values);
    }

    @Test
    default void testLimit2() {
        final List<T> values = this.values();

        this.limitAndCheck(() -> this.createStream(),
                values);
    }

    // skip..........................................................................................................

    @Test
    default void testSkipZero() {
        this.skipAndCheck(this.createStream()
                        .skip(0),
                this.values());
    }

    @Test
    default void testSkipOne() {
        final List<T> values = this.values();

        final int skip = 1;
        this.skipAndCheck(this.createStream()
                        .skip(skip),
                values.subList(skip, values.size()));
    }

    @Test
    default void testSkipAllButLast() {
        final List<T> values = this.values();

        final int skip = values.size() - 1;
        this.skipAndCheck(this.createStream()
                        .skip(skip),
                values.subList(skip, values.size()));
    }

    @Test
    default void testSkipAll() {
        final List<T> values = this.values();

        this.skipAndCheck(this.createStream()
                .skip(values.size()));
    }

    // skip.limit..........................................................................................................

    @Test
    default void testSkipInvalidMaxSizeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createStream().skip(-1);
        });
    }

    @Test
    default void testSkipZeroLimitRemainder() {
        final List<T> values = this.values();

        this.skipAndCheck(this.createStream()
                        .skip(0)
                        .limit(values.size()),
                this.values());
    }

    @Test
    default void testSkipOneLimitRemainder() {
        final List<T> values = this.values();

        final int skip = 1;
        this.skipAndCheck(this.createStream()
                        .skip(skip)
                        .limit(values.size() - skip),
                values.subList(skip, values.size()));
    }

    @Test
    default void testSkipAllButLastLimitOne() {
        final List<T> values = this.values();

        final int skip = values.size() - 1;
        this.skipAndCheck(this.createStream()
                        .skip(skip)
                        .limit(values.size() - skip),
                values.subList(skip, values.size()));
    }

    @Test
    default void testSkipAllLimitZero() {
        final List<T> values = this.values();

        this.skipAndCheck(this.createStream()
                .skip(values.size())
                .limit(0));
    }

    @Test
    default void testSkipAllLimitOne() {
        final List<T> values = this.values();

        this.skipAndCheck(this.createStream()
                .skip(values.size())
                .limit(1));
    }

    @Test
    default void testSkip() {
        this.skipAndCheck(() -> this.createStream(), this.values());
    }

    // factory..........................................................................................................

    /**
     * Creates a new {@link Stream} without any operations. The stream should have several items, 5 is a good number.
     */
    abstract S createStream();

    /**
     * The values in the stream in order.
     */
    List<T> values();

    // match...........................................................................................................

    default void allMatchAndCheck(final Stream<T> stream,
                                  final Predicate<? super T> predicate,
                                  final boolean result) {
        assertEquals(result,
                stream.allMatch(predicate),
                () -> stream + " allMatch");
    }

    default void anyMatchAndCheck(final Stream<T> stream,
                                  final Predicate<? super T> predicate,
                                  final boolean result) {
        assertEquals(result,
                stream.anyMatch(predicate),
                () -> stream + " anyMatch");
    }

    default void noneMatchAndCheck(final Stream<T> stream,
                                   final Predicate<? super T> predicate,
                                   final boolean result) {
        assertEquals(result,
                stream.noneMatch(predicate),
                () -> stream + " anyMatch");
    }

    // collect........................................................................................................

    default void collectAndCheck(final Supplier<Stream<T>> stream, final T... values) {
        this.collectAndCheck(stream.get(), Lists.of(values));
        this.toArrayIntFunctionAndCheck(stream.get(),
                (int size) -> Cast.to(Array.newInstance(values.getClass().getComponentType(), size)),
                values);
    }

    default void collectAndCheck(final Supplier<Stream<T>> stream, final List<T> values) {
        this.collectAndCheck(stream.get(), values);
        //this.collectAndCheck2(stream.get(), values);
        this.toArrayAndCheck(stream.get(), values);

        this.countAndCheck(stream.get(), values.size());

        this.peekAndCheck(stream.get(), values);

        this.skipAndCheck(stream.get(), values);
        this.limitAndCheck(stream.get(), values);

        this.findFirstAndCheck(stream.get(), values.stream()
                .findFirst());
        this.findAnyAndCheck(stream.get(), values);
        this.forEachAndCheck(stream.get(), values);
        this.forEachOrderedAndCheck(stream.get(), values);

        this.allMatchAndCheck(stream.get(),
                values::contains,
                true);
        this.anyMatchAndCheck(stream.get(),
                values::contains,
                true);

        this.filterAndCheck(stream, values);
    }

    // collect........................................................................................................

    default <U> void collectAndCheck(final Stream<T> stream, final U... values) {
        this.collectAndCheck(stream, Lists.of(values));
    }

    default <U> void collectAndCheck(final Stream<T> stream, final List<U> values) {
        assertEquals(values,
                stream.collect(Collectors.toList()),
                () -> "collect(Collector.toList) from " + stream);
    }

//    default <U> void collectAndCheck2(final Stream<T> stream, final List<U> values) {
//        final Collector<T, ?, List<T>> collector = Collectors.toList();
//
//        final Supplier<T> supplier = Cast.to(collector.supplier());
//        final BiConsumer<U, ? super T> accumulator = Cast.to(collector.accumulator());
//        final BiConsumer<U, U> combiner = Cast.to(collector.combiner());
//
//        assertEquals(values,
//                stream.collect(supplier, accumulator, combiner),
//                () -> "collect(Supplier, BiConumer, BinaryOperator) from " + stream);
//    }

    // count........................................................................................................

    default void countAndCheck(final Stream<T> stream, final int count) {
        assertEquals(count,
                stream.count(),
                () -> "count from " + stream);
    }

    // filter........................................................................................................

    default void filterAndCheck(final Supplier<Stream<T>> stream, final T... values) {
        this.filterAndCheck(stream, Lists.of(values));
    }

    default void filterAndCheck(final Supplier<Stream<T>> stream, final List<T> values) {
        assertEquals(values,
                stream.get()
                        .filter(v -> {
                            assertEquals(true, values.contains(v), () -> "predicate argument not in values");
                            return true;
                        })
                        .collect(Collectors.toList()),
                () -> "filter(true) then collect(Collector.toList) from " + stream);
        assertEquals(Lists.empty(),
                stream.get()
                        .filter(Predicates.never())
                        .collect(Collectors.toList()),
                () -> "filter(false) then collect(Collector.toList) from " + stream);

        final Predicate<T> keepOdd = (t) -> {
            final int index = values.indexOf(t);
            assertNotEquals(-1, index, () -> "unknown value=" + CharSequences.quoteIfChars(t));
            return index % 1 == 0;
        };

        assertEquals(values.stream()
                        .filter(keepOdd)
                        .collect(Collectors.toList()),
                stream.get()
                        .filter(keepOdd)
                        .collect(Collectors.toList()),
                () -> "filter(odds) then collect(Collector.toList) from " + stream);

        final Predicate<T> keepEven = (t) -> {
            final int index = values.indexOf(t);
            assertNotEquals(-1, index, () -> "unknown value=" + CharSequences.quoteIfChars(t));
            return index % 1 == 1;
        };

        assertEquals(values.stream()
                        .filter(keepEven)
                        .collect(Collectors.toList()),
                stream.get()
                        .filter(keepEven)
                        .collect(Collectors.toList()),
                () -> "filter(evens) then collect(Collector.toList) from " + stream);
    }

    // findFirst........................................................................................................

    default void findFirstAndCheckNone(final Stream<T> stream) {
        this.findFirstAndCheck(stream, Optional.empty());
    }

    default void findFirstAndCheck(final Stream<T> stream, final T first) {
        this.findFirstAndCheck(stream, Optional.of(first));
    }

    default void findFirstAndCheck(final Stream<T> stream, final Optional<T> first) {
        assertEquals(first,
                stream.findFirst(),
                () -> "findFirst from " + stream);
    }

    // findAny........................................................................................................

    default void findAnyAndCheckNone(final Stream<T> stream) {
        assertEquals(Optional.empty(),
                stream.findAny(),
                () -> "findAny from " + stream);
    }

    default void findAnyAndCheck(final Stream<T> stream, final List<T> values) {
        final Optional<T> any = stream.findAny();
        assertNotEquals(Optional.empty(),
                any,
                () -> "findAny " + stream + " values: " + values);
        assertTrue(values.contains(any.get()),
                () -> "findAny from " + stream + " found " + any);
    }

    // forEach........................................................................................................

    default void forEachAndCheck(final Stream<T> stream, final T... values) {
        this.forEachAndCheck(stream, Lists.of(values));
    }

    default void forEachAndCheck(final Stream<T> stream, final List<T> values) {
        final Set<T> collected = Sets.ordered();

        stream.forEach(collected::add);

        final Set<T> expected = Sets.ordered();
        expected.addAll(values);

        assertEquals(expected,
                collected,
                () -> "for each from " + stream);
    }

    // forEachOrdered........................................................................................................

    default void forEachOrderedAndCheck(final Stream<T> stream, final T... values) {
        this.forEachOrderedAndCheck(stream, Lists.of(values));
    }

    default void forEachOrderedAndCheck(final Stream<T> stream, final List<T> values) {
        final List<T> collected = Lists.array();

        stream.forEachOrdered(collected::add);

        assertEquals(values,
                collected,
                () -> "for each from " + stream);
    }

    // limit........................................................................................................

    default void limitAndCheck(final Supplier<Stream<T>> stream, final T... values) {
        this.limitAndCheck(stream, Lists.of(values));
    }

    default void limitAndCheck(final Supplier<Stream<T>> stream, final List<T> values) {
        final int to = values.size();

        for (int i = 0; i < to; i++) {
            final int limit = i;

            assertEquals(values.subList(0, limit),
                    stream.get()
                            .limit(i)
                            .collect(Collectors.toList()),
                    () -> "limit " + limit + " then collect from " + stream);
        }
    }

    default void limitAndCheck(final Stream<T> stream, final T... values) {
        this.limitAndCheck(stream, Lists.of(values));
    }

    default void limitAndCheck(final Stream<T> stream, final List<T> values) {
        assertEquals(values,
                stream.collect(Collectors.toList()),
                () -> "limit then collect from " + stream);
    }

    // peek........................................................................................................

    default void peekAndCheck(final Stream<T> stream, final T... values) {
        this.peekAndCheck(stream, Lists.of(values));
    }

    default void peekAndCheck(final Stream<T> stream, final List<T> values) {
        final List<T> collected = Lists.array();

        this.collectAndCheck(stream.peek(collected::add), values);

        assertEquals(values,
                collected,
                () -> "peek from " + stream);
    }

    // skip........................................................................................................

    default void skipAndCheck(final Supplier<Stream<T>> stream, final T... values) {
        this.skipAndCheck(stream, Lists.of(values));
    }

    default void skipAndCheck(final Supplier<Stream<T>> stream, final List<T> values) {
        final int to = values.size();

        for (int i = 0; i < to; i++) {
            final int skip = i;

            assertEquals(values.subList(skip, to),
                    stream.get()
                            .skip(skip)
                            .collect(Collectors.toList()),
                    () -> "skip " + skip + " then collect from " + stream);
        }
    }

    default void skipAndCheck(final Stream<T> stream, final T... values) {
        this.skipAndCheck(stream, Lists.of(values));
    }

    default void skipAndCheck(final Stream<T> stream, final List<T> values) {
        assertEquals(values,
                stream.collect(Collectors.toList()),
                () -> "skip  then collect from " + stream);
    }

    // toArray........................................................................................................

    default void toArrayAndCheck(final Stream<T> stream, final T... values) {
        this.toArrayAndCheck(stream, Lists.of(values));
    }

    default void toArrayAndCheck(final Stream<T> stream, final List<T> values) {
        assertArrayEquals(values.toArray(),
                stream.toArray(),
                () -> "toArray from " + stream);
    }

    // toArray(IntFunction)........................................................................................................

    default void toArrayIntFunctionAndCheck(final Stream<T> stream,
                                            final IntFunction<T[]> generator,
                                            final T... values) {
        assertArrayEquals(values,
                stream.toArray(),
                () -> "toArray(IntFunction) from " + stream + " intFunction: " + generator);
    }
}
