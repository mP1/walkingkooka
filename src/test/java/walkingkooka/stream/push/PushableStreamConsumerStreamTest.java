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

package walkingkooka.stream.push;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.Predicates;
import walkingkooka.stream.StreamTesting;
import walkingkooka.text.CharSequences;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PushableStreamConsumerStreamTest implements StreamTesting<PushableStreamConsumerStream<String>, String>,
    ToStringTesting<PushableStreamConsumerStream<String>>,
    IteratorTesting {

    @Test
    public void testStreamNullPushableStreamConsumerFails() {
        //noinspection ResultOfMethodCallIgnored
        assertThrows(NullPointerException.class, () -> PushableStreamConsumerStream.with(null));
    }

    @Test
    public void testStream() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        this.checkPushableStreamStream(PushableStreamConsumerStream.with(starter),
            starter,
            PushableStreamConsumerCloseableCollection.empty());
    }

    // Limit............................................................................................................

    @Test
    public void testStreamLimit0() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.limit(0);

        assertNotSame(stream, stream2);

        this.checkPushableStreamStream(stream2,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.limit(0));
    }

    @Test
    public void testStreamLimitLongMaxValue() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.limit(Long.MAX_VALUE);

        assertSame(stream, stream);

        this.checkPushableStreamStream(stream2,
            starter,
            PushableStreamConsumerCloseableCollection.empty());
    }

    @Test
    public void testStreamLimit() {
        final long limit = 123;

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.limit(limit);

        assertNotSame(stream, stream2);

        this.checkPushableStreamStream(stream2,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.limit(limit));
    }

    @Test
    public void testStreamLimitLimit0() {
        final long limit = 123;

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.limit(limit);

        assertNotSame(stream2, stream2.limit(0));

        this.checkPushableStreamStream(stream2,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.limit(limit));
    }

    @Test
    public void testStreamLimitSkip0() {
        final long limit = 123;

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.limit(limit);

        assertSame(stream2, stream2.skip(0));

        this.checkPushableStreamStream(stream2,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.limit(limit));
    }

    @Test
    public void testStreamLimitLimit() {
        final long limit1 = 1;

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.limit(limit1);

        assertNotSame(stream, stream2);

        final long limit2 = 20;
        final Stream<String> stream3 = stream2.limit(limit2);

        assertNotSame(stream2, stream3);

        this.checkPushableStreamStream(stream3,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.limit(limit1));
    }

    // Skip............................................................................................................

    @Test
    public void testStreamSkip() {
        final long skip = 123;

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.skip(skip);

        assertNotSame(stream, stream2);

        this.checkPushableStreamStream(stream2,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.skip(skip));
    }

    @Test
    public void testStreamSkipLimit() {
        final long skip = 123;

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.skip(skip);

        final long limit = 456;
        final Stream<String> stream3 = stream2.limit(limit);
        assertNotSame(stream2, stream3);

        this.checkPushableStreamStream(stream3,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.skip(skip),
            PushableStreamConsumerStreamIntermediate.limit(limit));
    }

    @Test
    public void testStreamSkipSkip0() {
        final long skip = 123;

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.skip(skip);

        assertSame(stream2, stream2.skip(0));

        this.checkPushableStreamStream(stream2,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.skip(skip));
    }

    @Test
    public void testStreamSkipSkip() {
        final long skip1 = 1;

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.skip(skip1);

        assertNotSame(stream, stream2);

        final long skip2 = 20;
        final Stream<String> stream3 = stream2.skip(skip2);

        assertNotSame(stream2, stream3);

        this.checkPushableStreamStream(stream3,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.skip(skip1 + skip2));
    }

    @Test
    public void testStreamSkipCollect() {
        final long skip1 = 1;

        final Consumer<PushableStreamConsumer<String>> starter = this.starter("1a", "2b", "3c");
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.skip(skip1);

        assertNotSame(stream, stream2);

        this.collectAndCheck(stream2, "2b", "3c");
    }

    @Test
    public void testStreamSkipSkipCollect() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter("1a", "2b", "3c", "4d", "5e");
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.skip(1)
            .skip(2);

        this.collectAndCheck(stream2, "4d", "5e");
    }

    // filter..........................................................................................................

    @Test
    public void testStreamFilter() {
        final Predicate<String> filter = Predicates.fake();

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.filter(filter);

        assertNotSame(stream, stream2);

        this.checkPushableStreamStream(stream2,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.filter(filter));
    }

    @Test
    public void testStreamFilterCollect() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter("1a", "2b!", "3c", "4d!");
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.filter((s) -> s.contains("!"));

        this.collectAndCheck(stream2, "2b!", "4d!");
    }

    @Test
    public void testStreamFilterFilter() {
        final Predicate<String> filter1 = Predicates.fake();

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.filter(filter1);

        assertNotSame(stream, stream2);

        final Predicate<String> filter2 = Predicates.fake();
        final Stream<String> stream3 = stream2.filter(filter2);

        this.checkPushableStreamStream(stream3,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.filter(filter1),
            PushableStreamConsumerStreamIntermediate.filter(filter2));
    }

    @Test
    public void testStreamFilterLimit() {
        final Predicate<String> filter = Predicates.fake();

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.filter(filter);

        assertNotSame(stream, stream2);

        final long limit = 123;
        final Stream<String> stream3 = stream2.limit(limit);
        assertNotSame(stream2, stream3);

        this.checkPushableStreamStream(stream3,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.filter(filter),
            PushableStreamConsumerStreamIntermediate.limit(limit));
    }

    // map..........................................................................................................

    @Test
    public void testStreamMap() {
        final Function<String, String> mapper = Function.identity();

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.map(mapper);

        assertNotSame(stream, stream2);

        this.checkPushableStreamStream(stream2,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.map(mapper));
    }

    @Test
    public void testStreamMapMap() {
        final Function<String, String> mapper1 = Function.identity();

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.map(mapper1);

        assertNotSame(stream, stream2);

        final Function<String, String> mapper2 = Function.identity();
        final Stream<String> stream3 = stream2.map(mapper2);

        this.checkPushableStreamStream(stream3,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.map(mapper1),
            PushableStreamConsumerStreamIntermediate.map(mapper2));
    }

    @Test
    public void testStreamMapMapPushCollect() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter("1", "2", "3");
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.map((s) -> s + "!")
            .map((s) -> s + "@");

        this.collectAndCheck(stream2, "1!@", "2!@", "3!@");
    }

    @Test
    public void testStreamMapLimit() {
        final Function<String, String> mapper = Function.identity();

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.map(mapper);

        assertNotSame(stream, stream2);

        final long limit = 123;
        final Stream<String> stream3 = stream2.limit(limit);
        assertNotSame(stream2, stream3);

        this.checkPushableStreamStream(stream3,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.map(mapper),
            PushableStreamConsumerStreamIntermediate.limit(limit));
    }

    @Test
    public void testStreamMapLimitCollect() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter("1a", "2b", "3c", "4f");
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.map((s) -> s + "!")
            .limit(3);

        this.collectAndCheck(stream2, "1a!", "2b!", "3c!");
    }

    // mapToInt.........................................................................................................

    @Test
    public void testStreamMapToIntNoValues() {
        this.mapToIntAndCheck();
    }

    @Test
    public void testStreamMapToIntManyValues() {
        this.mapToIntAndCheck("1", "2", "3", "4");
    }

    private void mapToIntAndCheck(final String... values) {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter(values);
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);

        assertArrayEquals(Lists.of(values).stream().mapToInt(Integer::parseInt).toArray(),
            stream.mapToInt(Integer::parseInt).toArray(),
            () -> stream + " values: " + Arrays.toString(values));
    }

    // mapToLong.........................................................................................................

    @Test
    public void testStreamMapToLongNoValues() {
        this.mapToLongAndCheck();
    }

    @Test
    public void testStreamMapToLongManyValues() {
        this.mapToLongAndCheck("1", "2", "3", "4");
    }

    private void mapToLongAndCheck(final String... values) {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter(values);
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);

        assertArrayEquals(Lists.of(values).stream().mapToLong(Long::parseLong).toArray(),
            stream.mapToLong(Long::parseLong).toArray(),
            () -> stream + " values: " + Arrays.toString(values));
    }

    // mapToDouble.........................................................................................................

    @Test
    public void testStreamMapToDoubleNoValues() {
        this.mapToDoubleAndCheck();
    }

    @Test
    public void testStreamMapToDoubleManyValues() {
        this.mapToDoubleAndCheck("1", "2", "3", "4");
    }

    private void mapToDoubleAndCheck(final String... values) {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter(values);
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);

        assertArrayEquals(Lists.of(values).stream().mapToDouble(Double::parseDouble).toArray(),
            stream.mapToDouble(Double::parseDouble).toArray(),
            () -> stream + " values: " + Arrays.toString(values));
    }

    // flatMap..........................................................................................................

    @Test
    public void testStreamFlatMap() {
        final Function<String, Stream<String>> mapper = Stream::of;

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.flatMap(mapper);

        assertNotSame(stream, stream2);

        this.checkPushableStreamStream(stream2,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.flatMap(Cast.to(mapper)));
    }

    @Test
    public void testStreamFlatMapCollect() {
        final Function<String, Stream<String>> mapper = Stream::of;

        final Stream<String> stream2 = this.createStream("a1", "b2", "c3")
            .flatMap(mapper);

        this.collectAndCheck(stream2, "a1", "b2", "c3");
    }

    @Test
    public void testStreamFlatMapToArray() {
        final Function<String, Stream<String>> mapper = Stream::of;

        final Stream<String> stream2 = this.createStream("a1", "b2", "c3")
            .flatMap(mapper);

        this.toArrayAndCheck(stream2, "a1", "b2", "c3");
    }

    @Test
    public void testStreamFlatMapCollectIncludesNull() {
        final Function<String, Stream<String>> mapper = Stream::of;

        final Stream<String> stream2 = this.createStream("a1", null, "c3")
            .flatMap(mapper);

        this.collectAndCheck(stream2, "a1", null, "c3");
    }

    @Test
    public void testStreamFlatMapCollectMultipleValues() {
        final Function<String, Stream<String>> mapper = (s) -> Arrays.stream(s.split(","));

        final Stream<String> stream2 = this.createStream("a1,a2,a3", "b", "c1,c2")
            .flatMap(mapper);

        this.collectAndCheck(stream2, "a1", "a2", "a3", "b", "c1", "c2");
    }

    // flatMapToInt.........................................................................................................

    @Test
    public void testStreamFlatMapToIntNoValues() {
        this.flatMapToIntAndCheck();
    }

    @Test
    public void testStreamFlatMapToIntManyValues() {
        this.flatMapToIntAndCheck("1", "3", "3", "4");
    }

    @Test
    public void testStreamFlatMapToIntNullStream() {
        this.flatMapToIntAndCheck("1", "", "3", "4");
    }

    @Test
    public void testStreamFlatMapToIntManyValues2() {
        this.flatMapToIntAndCheck("1,2,3", "4");
    }

    private void flatMapToIntAndCheck(final String... values) {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter(values);
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);

        assertArrayEquals(Lists.of(values).stream().flatMapToInt(this::intStream).toArray(),
            stream.flatMapToInt(this::intStream).toArray(),
            () -> stream + " values: " + Arrays.toString(values));
    }

    private IntStream intStream(final String value) {
        return CharSequences.isNullOrEmpty(value) ?
            null :
            IntStream.of(Arrays.stream(value.split(","))
                .mapToInt(Integer::parseInt)
                .toArray());
    }

    // flatMapToLong.........................................................................................................

    @Test
    public void testStreamFlatMapToLongNoValues() {
        this.flatMapToLongAndCheck();
    }

    @Test
    public void testStreamFlatMapToLongManyValues() {
        this.flatMapToLongAndCheck("1", "3", "3", "" + Long.MAX_VALUE);
    }

    @Test
    public void testStreamFlatMapToLongNullStream() {
        this.flatMapToLongAndCheck("1", "", "3", "4");
    }

    @Test
    public void testStreamFlatMapToLongManyValues2() {
        this.flatMapToLongAndCheck("1,2,3", "4");
    }

    private void flatMapToLongAndCheck(final String... values) {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter(values);
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);

        assertArrayEquals(Lists.of(values).stream().flatMapToLong(this::longStream).toArray(),
            stream.flatMapToLong(this::longStream).toArray(),
            () -> stream + " values: " + Arrays.toString(values));
    }

    private LongStream longStream(final String value) {
        return CharSequences.isNullOrEmpty(value) ?
            null :
            LongStream.of(Arrays.stream(value.split(","))
                .mapToLong(Long::parseLong)
                .toArray());
    }

    // flatMapToDouble.........................................................................................................

    @Test
    public void testStreamFlatMapToDoubleNoValues() {
        this.flatMapToDoubleAndCheck();
    }

    @Test
    public void testStreamFlatMapToDoubleManyValues() {
        this.flatMapToDoubleAndCheck("1.25", "2.5", "3", "4");
    }

    @Test
    public void testStreamFlatMapToDoubleNullStream() {
        this.flatMapToDoubleAndCheck("1", "", "3", "" + Double.MAX_VALUE);
    }

    @Test
    public void testStreamFlatMapToDoubleManyValues2() {
        this.flatMapToDoubleAndCheck("1,2,3", "4.5");
    }

    private void flatMapToDoubleAndCheck(final String... values) {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter(values);
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);

        assertArrayEquals(Lists.of(values).stream().flatMapToDouble(this::doubleStream).toArray(),
            stream.flatMapToDouble(this::doubleStream).toArray(),
            () -> stream + " values: " + Arrays.toString(values));
    }

    private DoubleStream doubleStream(final String value) {
        return CharSequences.isNullOrEmpty(value) ?
            null :
            DoubleStream.of(Arrays.stream(value.split(","))
                .mapToDouble(Double::parseDouble)
                .toArray());
    }

    // peek..........................................................................................................

    private final static Consumer<String> ACTION = (i) -> {
    };

    @Test
    public void testStreamPeek() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.peek(ACTION);

        assertNotSame(stream, stream2);

        this.checkPushableStreamStream(stream2,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.peek(ACTION));
    }

    @Test
    public void testStreamPeekPeek() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.peek(ACTION);

        assertNotSame(stream, stream2);

        final Consumer<String> action2 = (i) -> {
        };
        final Stream<String> stream3 = stream2.peek(action2);

        this.checkPushableStreamStream(stream3,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.peek(ACTION),
            PushableStreamConsumerStreamIntermediate.peek(action2));
    }

    @Test
    public void testStreamPeekLimit() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.peek(ACTION);

        assertNotSame(stream, stream2);

        final long limit = 123;
        final Stream<String> stream3 = stream2.limit(limit);
        assertNotSame(stream2, stream3);

        this.checkPushableStreamStream(stream3,
            starter,
            PushableStreamConsumerCloseableCollection.empty(),
            PushableStreamConsumerStreamIntermediate.peek(ACTION),
            PushableStreamConsumerStreamIntermediate.limit(limit));
    }

    // reduce.......................................................................................................

    @Test
    public void testStreamReduce() {
        this.reduceAndCheck(this.createStream("a1", "b2", "c3"),
            (a, b) -> a + b,
            "a1", "b2", "c3");
    }

    @Test
    public void testStreamReduce2() {
        this.reduceAndCheck(this.createStream("a1", "b2", "c3"),
            (a, b) -> a.toUpperCase() + b,
            "a1", "b2", "c3");
    }

    // reduce initial...................................................................................................

    @Test
    public void testStreamReduceInitial() {
        this.reduceAndCheck(this.createStream("a1", "b2", "c3"),
            "@",
            (a, b) -> a + b,
            "a1", "b2", "c3");
    }

    @Test
    public void testStreamReduceInitial2() {
        this.reduceAndCheck(this.createStream("a1", "b2", "c3"),
            "@",
            (a, b) -> a.toUpperCase() + b,
            "a1", "b2", "c3");
    }

    // iterator.......................................................................................................

    @Test
    public void testStreamIterator() {
        final String[] values = new String[]{"a1", "b2", "c3"};
        final PushableStreamConsumerStream<String> stream = this.createStream(values);
        this.iterateAndCheck(stream.iterator(), values);
    }

    @Test
    public void testStreamIteratorEmpty() {
        final String[] values = new String[0];
        final PushableStreamConsumerStream<String> stream = this.createStream(values);
        this.iterateAndCheck(stream.iterator(), values);
    }

    // spliterator.......................................................................................................

    @Test
    public void testStreamSpliterator() {
        final String[] values = new String[]{"a1", "b2", "c3"};
        final PushableStreamConsumerStream<String> stream = this.createStream(values);

        this.iterateAndCheck(Spliterators.iterator(stream.spliterator()), values);
    }

    @Test
    public void testStreamSpliteratorCharacteristics() {
        final Spliterator<String> spliterator = this.createStream().spliterator();
        this.checkEquals(0, spliterator.characteristics(), () -> "characteristics " + spliterator);
    }

    @Test
    public void testStreamSpliteratorEstimatedSize() {
        final Spliterator<String> spliterator = this.createStream().spliterator();
        this.checkEquals(Long.MAX_VALUE, spliterator.estimateSize(), () -> "estimateSize " + spliterator);
    }

    @Test
    public void testStreamSpliteratorComparatorFails() {
        final Spliterator<String> spliterator = this.createStream().spliterator();

        assertThrows(IllegalStateException.class, spliterator::getComparator);
    }

    @Test
    public void testStreamSpliteratorGetExactSizeIfKnown() {
        final Spliterator<String> spliterator = this.createStream().spliterator();

        this.checkEquals(-1L, spliterator.getExactSizeIfKnown(), () -> "getExactSizeIfKnown " + spliterator);
    }

    // closeable.......................................................................................................

    @Test
    public void testStreamIntermediateCloseable() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.peek(ACTION);

        assertNotSame(stream, stream2);

        final TestCloseableRunnable closeable1 = TestCloseableRunnable.with("closeable-1");
        final Stream<String> stream3 = stream2.onClose(closeable1);

        assertNotSame(stream2, stream3);

        this.checkPushableStreamStream(stream3,
            starter,
            PushableStreamConsumerCloseableCollection.empty().add(closeable1),
            PushableStreamConsumerStreamIntermediate.peek(ACTION));
    }

    @Test
    public void testStreamIntermediateCloseableCloseable() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);

        final TestCloseableRunnable closeable1 = TestCloseableRunnable.with("closeable-1");
        final Stream<String> stream2 = stream.onClose(closeable1);
        assertNotSame(stream, stream2);

        final Stream<String> stream3 = stream2.peek(ACTION);
        assertNotSame(stream2, stream3);

        final TestCloseableRunnable closeable2 = TestCloseableRunnable.with("closeable-2");
        final Stream<String> stream4 = stream3.onClose(closeable2);
        assertNotSame(stream, stream4);

        this.checkPushableStreamStream(stream4,
            starter,
            PushableStreamConsumerCloseableCollection.empty().add(closeable1).add(closeable2),
            PushableStreamConsumerStreamIntermediate.peek(ACTION));
    }

    @Test
    public void testStreamCloseableIntermediateCloseable() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final PushableStreamConsumerStream<String> stream = PushableStreamConsumerStream.with(starter);
        final Stream<String> stream2 = stream.peek(ACTION);

        assertNotSame(stream, stream2);

        final TestCloseableRunnable closeable1 = TestCloseableRunnable.with("closeable-1");
        final Stream<String> stream3 = stream2.onClose(closeable1);
        assertNotSame(stream2, stream3);

        final TestCloseableRunnable closeable2 = TestCloseableRunnable.with("closeable-2");
        final Stream<String> stream4 = stream3.onClose(closeable2);
        assertNotSame(stream3, stream4);

        this.checkPushableStreamStream(stream4,
            starter,
            PushableStreamConsumerCloseableCollection.empty().add(closeable1).add(closeable2),
            PushableStreamConsumerStreamIntermediate.peek(ACTION));
    }

    @Test
    public void testStreamCollectCloseableFired() {
        this.closeableFired = 0;

        final Stream<String> stream = this.createStream()
            .onClose(() -> this.closeableFired++);
        this.collectAndCheck(stream, this.values());
        this.checkEquals(1, this.closeableFired, "Closeable not fired only once");
    }

    @Test
    public void testStreamCollectCloseableFired2() {
        this.closeableFired = 0;

        final Stream<String> stream = this.createStream()
            .onClose(() -> this.closeableFired++)
            .onClose(() -> this.closeableFired += 10);
        this.collectAndCheck(stream, this.values());
        this.checkEquals(11, this.closeableFired, "Both closeables not fired only once");
    }

    private int closeableFired;

    @Test
    public void testIsParallel() {
        this.checkEquals(false, this.createStream().isParallel());
    }

    @Test
    public void testParallel() {
        final Stream<String> stream = this.createStream();
        assertSame(stream, stream.parallel());
    }

    @Test
    public void testSequential() {
        final Stream<String> stream = this.createStream();
        assertSame(stream, stream.sequential());
    }

    @Test
    public void testUnordered() {
        final Stream<String> stream = this.createStream();
        assertSame(stream, stream.unordered());
    }

    // helpers..........................................................................................................

    private void checkPushableStreamStream(final Stream<String> pushable,
                                           final Consumer<PushableStreamConsumer<String>> starter,
                                           final PushableStreamConsumerCloseableCollection closeables,
                                           final PushableStreamConsumerStreamIntermediate... intermediates) {
        this.checkPushableStreamStream0(Cast.to(pushable),
            starter,
            closeables,
            intermediates);
    }

    private void checkPushableStreamStream0(final PushableStreamConsumerStream<String> pushable,
                                            final Consumer<PushableStreamConsumer<String>> starter,
                                            final PushableStreamConsumerCloseableCollection closeables,
                                            final PushableStreamConsumerStreamIntermediate... intermediates) {
        assertSame(starter, pushable.starter, "starter");
        this.checkEquals(Lists.of(intermediates), pushable.intermediates, "intermediates");
        this.checkEquals(closeables, pushable.closeables, "closeables");
    }

    // toString..........................................................................................................

    @Test
    public void testToString() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        this.toStringAndCheck(PushableStreamConsumerStream.with(starter), starter.toString());
    }

    @Test
    public void testToStringFilter() {
        final Predicate<String> filter = Predicates.fake();

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final Stream<String> stream = PushableStreamConsumerStream.with(starter)
            .filter(filter);

        this.toStringAndCheck(stream, starter + " " + PushableStreamConsumerStreamIntermediate.filter(filter));
    }

    @Test
    public void testToStringFilterFilter() {
        final Predicate<String> filter1 = Predicates.fake();
        final Predicate<String> filter2 = Predicates.fake();

        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final Stream<String> stream = PushableStreamConsumerStream.with(starter)
            .filter(filter1)
            .filter(filter2);

        this.toStringAndCheck(stream, starter + " filter " + filter1 + " filter " + filter2);
    }

    @Test
    public void testToStringLimit() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final Stream<String> stream = PushableStreamConsumerStream.with(starter)
            .limit(123);

        this.toStringAndCheck(stream, starter + " limit 123");
    }

    @Test
    public void testToStringSkipLimit() {
        final Consumer<PushableStreamConsumer<String>> starter = this.starter();
        final Stream<String> stream = PushableStreamConsumerStream.with(starter)
            .limit(123)
            .skip(45);

        this.toStringAndCheck(stream, starter + " limit 123 skip 45");
    }

    @Override
    public PushableStreamConsumerStream<String> createStream() {
        return PushableStreamConsumerStream.with(this.starter);
    }

    private PushableStreamConsumerStream<String> createStream(final String... values) {
        return PushableStreamConsumerStream.with(this.starter(values));
    }

    private final Consumer<PushableStreamConsumer<String>> starter = this.starter(this.values());

    private Consumer<PushableStreamConsumer<String>> starter(final String... values) {
        return this.starter(Lists.of(values));
    }

    private Consumer<PushableStreamConsumer<String>> starter(final List<String> values) {
        return (c) -> {
            final Iterator<String> i = values.iterator();
            while (false == c.isFinished() && i.hasNext()) {
                final String value = i.next();
                c.accept(value);
            }
        };
    }

    @Override
    public List<String> values() {
        return this.values("10,20,30,40,50,60,70,80");
    }

    private List<String> values(final String commaSeparated) {
        return commaSeparated.isEmpty() ?
            Lists.empty() :
            Arrays.asList(commaSeparated.split(","));
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<PushableStreamConsumerStream<String>> type() {
        return Cast.to(PushableStreamConsumerStream.class);
    }
}
