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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.predicate.Predicates;
import walkingkooka.test.Testing;
import walkingkooka.text.CharSequences;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface StreamTesting<S extends Stream<T>, T> extends Testing {

    @Test
    default void testValuesNotEmpty() {
        final List<T> values = this.values();
        this.checkNotEquals(
            0,
            values,
            () -> "" + values
        );
    }

    // allMatch..........................................................................................................

    @Test
    default void testAllMatchNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> this.createStream().allMatch(null));
    }

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
        this.allMatchAndCheck(this.createStream(),
            (v) -> values.indexOf(v) < values.size() / 2,
            false);
    }

    // anyMatch..........................................................................................................

    @Test
    default void testAnyMatchNullPredicateFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStream()
                .anyMatch(null)
        );
    }

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
        this.anyMatchAndCheck(this.createStream(),
            (v) -> values.indexOf(v) == 0,
            true);
    }

    @Test
    default void testAnyMatchMid() {
        final List<T> values = this.values();
        this.anyMatchAndCheck(this.createStream(),
            (v) -> values.indexOf(v) == values.size() / 2,
            true);
    }

    @Test
    default void testAnyMatchLast() {
        final List<T> values = this.values();
        this.anyMatchAndCheck(this.createStream(),
            (v) -> values.indexOf(v) == values.size() - 1,
            true);
    }

    // noneMatch........................................................................................................

    @Test
    default void testNoneMatchNullPredicateFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStream()
                .noneMatch(null)
        );
    }

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
        this.noneMatchAndCheck(this.createStream(),
            (v) -> values.indexOf(v) == 0,
            false);
    }

    @Test
    default void testNoneMatchMid() {
        final List<T> values = this.values();
        this.noneMatchAndCheck(this.createStream(),
            (v) -> values.indexOf(v) == values.size() / 2,
            false);
    }

    @Test
    default void testNoneMatchLast() {
        final List<T> values = this.values();
        this.noneMatchAndCheck(this.createStream(),
            (v) -> values.indexOf(v) == values.size() - 1,
            false);
    }

    // collect..........................................................................................................

    @Test
    default void testCollectNullCollectorFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStream()
                .collect(null)
        );
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
        assertThrows(
            NullPointerException.class,
            () -> this.createStream()
                .filter(null)
        );
    }

    @Test
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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
        assertThrows(NullPointerException.class, () -> this.createStream().forEach(null));
    }

    @Test
    default void testForEach() {
        this.forEachAndCheck(this.createStream(), this.values());
    }

    // forEachOrdered..........................................................................................................

    @Test
    default void testForEachOrderedNullConsumerFails() {
        assertThrows(NullPointerException.class, () -> this.createStream().forEachOrdered(null));
    }

    @Test
    default void testForEachOrdered() {
        this.forEachOrderedAndCheck(this.createStream(), this.values());
    }

    // iterator..........................................................................................................

    @Test
    default void testIterate() {
        this.iteratorAndCheck(this.createStream(), this.values());
    }

    // peek..........................................................................................................

    @Test
    default void testPeekNullConsumerFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStream()
                .peek(null)
        );
    }

    @Test
    default void testPeek() {
        this.peekAndCheck(this.createStream(), this.values());
    }

    // limit..........................................................................................................

    @Test
    default void testLimitInvalidMaxSizeFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createStream()
                .limit(-1)
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    default void testLimitZero() {
        this.limitAndCheck(this.createStream()
            .limit(0));
    }

    @Test
    @SuppressWarnings("unchecked")
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

        this.limitAndCheck(this::createStream,
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
    @SuppressWarnings("unchecked")
    default void testSkipAll() {
        final List<T> values = this.values();

        this.skipAndCheck(this.createStream()
            .skip(values.size()));
    }

    // skip.limit..........................................................................................................

    @Test
    default void testSkipInvalidMaxSizeFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createStream()
                .skip(-1)
        );
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
    @SuppressWarnings("unchecked")
    default void testSkipAllLimitZero() {
        final List<T> values = this.values();

        this.skipAndCheck(this.createStream()
            .skip(values.size())
            .limit(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    default void testSkipAllLimitOne() {
        final List<T> values = this.values();

        this.skipAndCheck(this.createStream()
            .skip(values.size())
            .limit(1));
    }

    @Test
    default void testSkip() {
        this.skipAndCheck(this::createStream, this.values());
    }

    // max..........................................................................................................

    @Test
    default void testMaxNullComparatorFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStream()
                .max(null)
        );
    }

    // min..........................................................................................................

    @Test
    default void testMinNullComparatorFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStream()
                .min(null)
        );
    }

    // factory..........................................................................................................

    /**
     * Creates a new {@link Stream} without any operations. The stream should have several items, 5 is a good number.
     */
    S createStream();

    /**
     * The values in the stream in order.
     */
    List<T> values();

    // match...........................................................................................................

    default void allMatchAndCheck(final Stream<T> stream,
                                  final Predicate<? super T> predicate,
                                  final boolean result) {
        this.checkEquals(
            result,
            stream.allMatch(predicate),
            () -> stream + " allMatch"
        );
    }

    default void anyMatchAndCheck(final Stream<T> stream,
                                  final Predicate<? super T> predicate,
                                  final boolean result) {
        this.checkEquals(
            result,
            stream.anyMatch(predicate),
            () -> stream + " anyMatch"
        );
    }

    default void noneMatchAndCheck(final Stream<T> stream,
                                   final Predicate<? super T> predicate,
                                   final boolean result) {
        this.checkEquals(
            result,
            stream.noneMatch(predicate),
            () -> stream + " anyMatch"
        );
    }

    // collect........................................................................................................

    @SuppressWarnings("unchecked")
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

        this.iteratorAndCheck(stream, values);

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

    @SuppressWarnings("unchecked")
    default <U> void collectAndCheck(final Stream<T> stream, final U... values) {
        this.collectAndCheck(stream, Lists.of(values));
    }

    default <U> void collectAndCheck(final Stream<T> stream, final List<U> values) {
        this.checkEquals(values,
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
//        this.checkEquals(values,
//                stream.collect(supplier, accumulator, combiner),
//                () -> "collect(Supplier, BiConumer, BinaryOperator) from " + stream);
//    }

    // count........................................................................................................

    default void countAndCheck(final Stream<T> stream, final long count) {
        this.checkEquals(
            count,
            stream.count(),
            () -> "count from " + stream
        );
    }

    // filter........................................................................................................

    @SuppressWarnings("unchecked")
    default void filterAndCheck(final Supplier<Stream<T>> stream, final T... values) {
        this.filterAndCheck(stream, Lists.of(values));
    }

    default void filterAndCheck(final Supplier<Stream<T>> stream, final List<T> values) {
        this.checkEquals(
            values,
            stream.get()
                .filter(v -> {
                    this.checkEquals(true, values.contains(v), "predicate argument not in values");
                    return true;
                })
                .collect(Collectors.toList()),
            () -> "filter(true) then collect(Collector.toList) from " + stream
        );
        this.checkEquals(
            Lists.empty(),
            stream.get()
                .filter(Predicates.never())
                .collect(Collectors.toList()),
            () -> "filter(false) then collect(Collector.toList) from " + stream
        );

        final Predicate<T> keepOdd = (t) -> {
            final int index = values.indexOf(t);
            this.checkNotEquals(-1, index, () -> "unknown value=" + CharSequences.quoteIfChars(t));
            return (index & 1) == 0;
        };

        this.checkEquals(
            values.stream()
                .filter(keepOdd)
                .collect(Collectors.toList()),
            stream.get()
                .filter(keepOdd)
                .collect(Collectors.toList()),
            () -> "filter(odds) then collect(Collector.toList) from " + stream
        );

        final Predicate<T> keepEven = (t) -> {
            final int index = values.indexOf(t);
            this.checkNotEquals(-1, index, () -> "unknown value=" + CharSequences.quoteIfChars(t));
            return (index & 1) == 1;
        };

        this.checkEquals(
            values.stream()
                .filter(keepEven)
                .collect(Collectors.toList()),
            stream.get()
                .filter(keepEven)
                .collect(Collectors.toList()),
            () -> "filter(evens) then collect(Collector.toList) from " + stream
        );
    }

    // findFirst........................................................................................................

    default void findFirstAndCheckNone(final Stream<T> stream) {
        this.findFirstAndCheck(stream, Optional.empty());
    }

    default void findFirstAndCheck(final Stream<T> stream, final T first) {
        this.findFirstAndCheck(stream, Optional.of(first));
    }

    default void findFirstAndCheck(final Stream<T> stream, final Optional<T> first) {
        this.checkEquals(
            first,
            stream.findFirst(),
            () -> "findFirst from " + stream
        );
    }

    // findAny........................................................................................................

    default void findAnyAndCheckNone(final Stream<T> stream) {
        this.checkEquals(
            Optional.empty(),
            stream.findAny(),
            () -> "findAny from " + stream
        );
    }

    default void findAnyAndCheck(final Stream<T> stream, final List<T> values) {
        final Optional<T> any = stream.findAny();

        this.checkNotEquals(
            Optional.empty(),
            any,
            () -> "findAny " + stream + " values: " + values
        );
        this.checkEquals(
            true,
            values.contains(any.orElse(null)),
            () -> "findAny from " + stream + " found " + any
        );
    }

    // forEach........................................................................................................

    @SuppressWarnings("unchecked")
    default void forEachAndCheck(final Stream<T> stream, final T... values) {
        this.forEachAndCheck(stream, Lists.of(values));
    }

    default void forEachAndCheck(final Stream<T> stream, final List<T> values) {
        final Set<T> collected = Sets.ordered();

        stream.forEach(collected::add);

        final Set<T> expected = Sets.ordered();
        expected.addAll(values);

        this.checkEquals(
            expected,
            collected,
            () -> "for each from " + stream
        );
    }

    // forEachOrdered........................................................................................................

    @SuppressWarnings("unchecked")
    default void forEachOrderedAndCheck(final Stream<T> stream, final T... values) {
        this.forEachOrderedAndCheck(stream, Lists.of(values));
    }

    default void forEachOrderedAndCheck(final Stream<T> stream, final List<T> values) {
        final List<T> collected = Lists.array();

        stream.forEachOrdered(collected::add);

        this.checkEquals(
            values,
            collected,
            () -> "for each from " + stream
        );
    }

    // iterator........................................................................................................

    @SuppressWarnings("unchecked")
    default <U> void iteratorAndCheck(final Supplier<Stream<T>> stream, final U... values) {
        this.iteratorAndCheck(stream, Lists.of(values));
    }

    default <U> void iteratorAndCheck(final Supplier<Stream<T>> stream, final List<U> values) {
        final int to = values.size();

        for (int i = 0; i < to; i++) {
            this.iteratorAndCheck(stream.get().skip(i), values.subList(i, to));
        }

        for (int i = 0; i < to; i++) {
            this.iteratorAndCheck(stream.get().limit(i), values.subList(0, i));
        }
    }

    @SuppressWarnings("unchecked")
    default <U> void iteratorAndCheck(final Stream<T> stream, final U... values) {
        this.iteratorAndCheck(stream, Lists.of(values));
    }

    default <U> void iteratorAndCheck(final Stream<T> stream, final List<U> values) {
        final List<T> iteratorValues = Lists.array();

        for (Iterator<T> i = stream.iterator(); i.hasNext(); ) {
            iteratorValues.add(i.next());
        }

        this.checkEquals(
            values,
            iteratorValues,
            () -> "iterator from " + stream
        );
    }

    // limit........................................................................................................

    @SuppressWarnings("unchecked")
    default void limitAndCheck(final Supplier<Stream<T>> stream, final T... values) {
        this.limitAndCheck(stream, Lists.of(values));
    }

    default void limitAndCheck(final Supplier<Stream<T>> stream, final List<T> values) {
        final int to = values.size();

        for (int i = 0; i < to; i++) {
            final int limit = i;

            this.checkEquals(
                values.subList(0, limit),
                stream.get()
                    .limit(i)
                    .collect(Collectors.toList()),
                () -> "limit " + limit + " then collect from " + stream
            );
        }
    }

    @SuppressWarnings("unchecked")
    default void limitAndCheck(final Stream<T> stream, final T... values) {
        this.limitAndCheck(stream, Lists.of(values));
    }

    default void limitAndCheck(final Stream<T> stream, final List<T> values) {
        this.checkEquals(
            values,
            stream.collect(Collectors.toList()),
            () -> "limit then collect from " + stream
        );
    }

    // max........................................................................................................

    @SuppressWarnings("unchecked")
    default void maxAndCheck(final Stream<T> stream,
                             final Comparator<T> comparator,
                             final T... values) {
        this.maxAndCheck(stream, comparator, Lists.of(values));
    }

    default void maxAndCheck(final Stream<T> stream,
                             final Comparator<T> comparator,
                             final List<T> values) {
        final Optional<T> expected = values.stream()
            .max(comparator);
        this.checkEquals(
            expected,
            stream.max(comparator),
            () -> "max " + comparator + " from " + stream + " with values: " + values
        );
    }

    // min........................................................................................................

    @SuppressWarnings("unchecked")
    default void minAndCheck(final Stream<T> stream,
                             final Comparator<T> comparator,
                             final T... values) {
        this.minAndCheck(stream, comparator, Lists.of(values));
    }

    default void minAndCheck(final Stream<T> stream,
                             final Comparator<T> comparator,
                             final List<T> values) {
        final Optional<T> expected = values.stream()
            .min(comparator);
        this.checkEquals(
            expected,
            stream.min(comparator),
            () -> "min " + comparator + " from " + stream + " with values: " + values
        );
    }

    // peek........................................................................................................

    @SuppressWarnings("unchecked")
    default void peekAndCheck(final Stream<T> stream, final T... values) {
        this.peekAndCheck(stream, Lists.of(values));
    }

    default void peekAndCheck(final Stream<T> stream, final List<T> values) {
        final List<T> collected = Lists.array();

        this.collectAndCheck(stream.peek(collected::add), values);

        this.checkEquals(
            values,
            collected,
            () -> "peek from " + stream
        );
    }

    // reduce(BinaryOperator)........................................................................................................

    @SuppressWarnings("unchecked")
    default <TT> void reduceAndCheck(final Stream<TT> stream,
                                     final BinaryOperator<TT> reducer,
                                     final TT... values) {
        this.reduceAndCheck(stream,
            reducer,
            Lists.of(values));
    }

    default <TT> void reduceAndCheck(final Stream<TT> stream,
                                     final BinaryOperator<TT> reducer,
                                     final List<TT> values) {
        this.checkEquals(
            values.stream().reduce(reducer),
            stream.reduce(reducer),
            () -> "reduce " + stream
        );
    }

    // reduce(T, BinaryOperator)........................................................................................................

    @SuppressWarnings("unchecked")
    default <TT> void reduceAndCheck(final Stream<TT> stream,
                                     final TT initial,
                                     final BinaryOperator<TT> reducer,
                                     final TT... values) {
        this.reduceAndCheck(stream,
            initial,
            reducer,
            Lists.of(values));
    }

    default <TT> void reduceAndCheck(final Stream<TT> stream,
                                     final TT initial,
                                     final BinaryOperator<TT> reducer,
                                     final List<TT> values) {
        this.checkEquals(
            values.stream().reduce(initial, reducer),
            stream.reduce(initial, reducer),
            () -> "reduce " + CharSequences.quoteIfChars(initial) + " " + stream
        );
    }

    // reduce(T, BinaryFunction, BinaryOperator)........................................................................

    @SuppressWarnings("unchecked")
    default <TT> void reduceAndCheck(final Stream<TT> stream,
                                     final TT initial,
                                     final BiFunction<TT, TT, TT> reducer,
                                     final BinaryOperator<TT> combiner,
                                     final TT... values) {
        this.reduceAndCheck(stream, initial, reducer, combiner, Lists.of(values));
    }

    default <TT> void reduceAndCheck(final Stream<TT> stream,
                                     final TT initial,
                                     final BiFunction<TT, TT, TT> reducer,
                                     final BinaryOperator<TT> combiner,
                                     final List<TT> values) {
        this.checkEquals(
            values.stream().reduce(initial, reducer, combiner),
            stream.reduce(initial, reducer, combiner),
            () -> "reduce " + CharSequences.quoteIfChars(initial) + " " + stream
        );
    }

    // skip........................................................................................................

    @SuppressWarnings("unchecked")
    default void skipAndCheck(final Supplier<Stream<T>> stream, final T... values) {
        this.skipAndCheck(stream, Lists.of(values));
    }

    default void skipAndCheck(final Supplier<Stream<T>> stream, final List<T> values) {
        final int to = values.size();

        for (int i = 0; i < to; i++) {
            final int skip = i;

            this.checkEquals(
                values.subList(skip, to),
                stream.get()
                    .skip(skip)
                    .collect(Collectors.toList()),
                () -> "skip " + skip + " then collect from " + stream
            );
        }
    }

    @SuppressWarnings("unchecked")
    default void skipAndCheck(final Stream<T> stream, final T... values) {
        this.skipAndCheck(stream, Lists.of(values));
    }

    default void skipAndCheck(final Stream<T> stream, final List<T> values) {
        this.checkEquals(
            values,
            stream.collect(Collectors.toList()),
            () -> "skip  then collect from " + stream
        );
    }

    // toArray........................................................................................................

    @SuppressWarnings("unchecked")
    default void toArrayAndCheck(final Stream<T> stream, final T... values) {
        this.toArrayAndCheck(stream, Lists.of(values));
    }

    default void toArrayAndCheck(final Stream<T> stream, final List<T> values) {
        assertArrayEquals(
            values.toArray(),
            stream.toArray(),
            () -> "toArray from " + stream
        );
    }

    // toArray(IntFunction)........................................................................................................

    @SuppressWarnings("unchecked")
    default void toArrayIntFunctionAndCheck(final Stream<T> stream,
                                            final IntFunction<T[]> generator,
                                            final T... values) {
        assertArrayEquals(
            values,
            stream.toArray(),
            () -> "toArray(IntFunction) from " + stream + " intFunction: " + generator
        );
    }
}
