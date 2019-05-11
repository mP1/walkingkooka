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

package walkingkooka.stream.push;

import walkingkooka.Cast;
import walkingkooka.NeverError;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.UsesToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * A {@link Stream} that acts as a builder collecting intermediate operations until the terminal is executed.
 * It feeds a {@link PushableStreamConsumer} to interact with a source which will push values.
 */
final class PushableStreamStream<T> implements Stream<T>,
        HashCodeEqualsDefined,
        UsesToStringBuilder {

    /**
     * Factory that creates a new {@link PushableStreamStream} without any registered closeables and intermediate ops.
     */
    static <T> PushableStreamStream<T> with(final Consumer<PushableStreamConsumer<T>> starter) {
        Objects.requireNonNull(starter, "starter");

        return new PushableStreamStream<>(starter,
                CloseableCollection.empty(),
                Lists.empty());
    }

    // Testing
    PushableStreamStream(final Consumer<PushableStreamConsumer<T>> starter,
                         final CloseableCollection closeables,
                         final List<PushableStreamStreamIntermediate> intermediates) {
        super();
        this.starter = starter;
        this.closeables = closeables;
        this.intermediates = intermediates;
    }

    // FILTER .........................................................................................................

    @Override
    public Stream<T> filter(final Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate");

        return this.appendIntermediate(FilterPushableStreamStreamIntermediate.with(predicate)).cast();
    }

    // PEEK...........................................................................................................

    @Override
    public Stream<T> peek(final Consumer<? super T> action) {
        Objects.requireNonNull(action, "action");

        return this.appendIntermediate(PeekPushableStreamStreamIntermediate.with(action)).cast();
    }

    // LIMIT...........................................................................................................

    @Override
    public Stream<T> limit(final long limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit " + limit + " < 0");
        }

        PushableStreamStream<?> stream;
        do {
            if (Long.MAX_VALUE == limit) {
                stream = this;
                break;
            }

            final List<PushableStreamStreamIntermediate> intermediates = this.copyIntermediates();
            if (intermediates.isEmpty()) {
                stream = this.addLimit(limit);
                break;
            }

            final int lastIndex = intermediates.size() - 1;
            final long previous = intermediates.get(lastIndex).limit();

            // previous wasnt a limit just a new limit
            if (PushableStreamStreamIntermediate.NOT_LIMIT_OR_SKIP == previous) {
                stream = this.addLimit(limit);
                break;
            }

            intermediates.remove(lastIndex);
            intermediates.add(PushableStreamStreamIntermediate.limit(Math.min(previous, limit)));
            stream = this.replaceIntermediates(intermediates);
        } while (false);

        return stream.cast();
    }

    private PushableStreamStream<?> addLimit(final long limit) {
        return this.appendIntermediate(PushableStreamStreamIntermediate.limit(limit));
    }

    // SKIP...........................................................................................................

    public Stream<T> skip(final long skip) {
        if (skip < 0) {
            throw new IllegalArgumentException("Skip " + skip + " < 0");
        }

        PushableStreamStream<?> stream;
        do {
            if (0 == skip) {
                stream = this;
                break;
            }

            final List<PushableStreamStreamIntermediate> intermediates = this.copyIntermediates();
            if (intermediates.isEmpty()) {
                stream = this.addSkip(skip);
                break;
            }

            final int lastIndex = intermediates.size() - 1;
            final long previous = intermediates.get(lastIndex).skip();

            // previous wasnt a skip just a new skip
            if (PushableStreamStreamIntermediate.NOT_LIMIT_OR_SKIP == previous) {
                stream = this.addSkip(skip);
                break;
            }

            // sum the previous and new skip.
            intermediates.remove(lastIndex);
            intermediates.add(PushableStreamStreamIntermediate.skip(Math.addExact(previous, skip)));
            stream = this.replaceIntermediates(intermediates);
        } while (false);

        return stream.cast();
    }

    private PushableStreamStream<?> addSkip(final long skip) {
        return this.appendIntermediate(PushableStreamStreamIntermediate.skip(skip));
    }

    // MAP .........................................................................................................

    @Override
    public <R> Stream<R> map(final Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "mapper");

        return this.appendIntermediate(PushableStreamStreamIntermediate.map(mapper)).cast();
    }

    @Override
    public IntStream mapToInt(final ToIntFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LongStream mapToLong(final ToLongFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DoubleStream mapToDouble(final ToDoubleFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    // FLATMAP .........................................................................................................

    @Override
    public <R> Stream<R> flatMap(final Function<? super T, ? extends Stream<? extends R>> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IntStream flatMapToInt(final Function<? super T, ? extends IntStream> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LongStream flatMapToLong(final Function<? super T, ? extends LongStream> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DoubleStream flatMapToDouble(final Function<? super T, ? extends DoubleStream> mapper) {
        throw new UnsupportedOperationException();
    }

    // DISTINCT .........................................................................................................

    @Override
    public Stream<T> distinct() {
        return this.sorted().distinct();
    }

    // SORTED .........................................................................................................

    @Override
    public Stream<T> sorted() {
        return this.collect(Collectors.toCollection(() -> Sets.<T>ordered())).stream();
    }

    @Override
    public Stream<T> sorted(final Comparator<? super T> comparator) {
        throw new UnsupportedOperationException();
    }

    // TERMINAL .........................................................................................................

    @Override
    public void forEach(final Consumer<? super T> action) {
        this.forEachOrdered(action);
    }

    @Override
    public void forEachOrdered(final Consumer<? super T> action) {
        this.assembleStartAndReturnResult((closeables) -> PushableStreamStreamPushableStreamConsumer.forEach(action, closeables));
    }

    // TERMINATOR: COLLECT ............................................................................................

    @Override
    public <R> R collect(final Supplier<R> containerCreator,
                         final BiConsumer<R, ? super T> collectorAdder,
                         final BiConsumer<R, R> combiner) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R, A> R collect(final Collector<? super T, A, R> collector) {
        return this.assembleStartAndReturnResult((closeables) -> PushableStreamStreamPushableStreamConsumer.collector(collector, closeables));
    }

    /**
     * Collect into a list and dump that into an array.
     */
    @Override
    public Object[] toArray() {
        return this.collect(Collectors.toList()).toArray();
    }

    @Override
    public <A> A[] toArray(final IntFunction<A[]> generator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean allMatch(final Predicate<? super T> predicate) {
        return this.assembleStartAndReturnResult((closeables) -> PushableStreamStreamPushableStreamConsumer.allMatch(predicate, closeables));
    }

    @Override
    public boolean anyMatch(final Predicate<? super T> predicate) {
        return this.assembleStartAndReturnResult((closeables) -> PushableStreamStreamPushableStreamConsumer.anyMatch(predicate, closeables));
    }

    @Override
    public boolean noneMatch(final Predicate<? super T> predicate) {
        return this.assembleStartAndReturnResult((closeables) -> PushableStreamStreamPushableStreamConsumer.noneMatch(predicate, closeables));
    }

    @Override
    public Optional<T> findFirst() {
        return this.assembleStartAndReturnResult((closeables) -> PushableStreamStreamPushableStreamConsumer.findFirst(closeables));
    }

    /**
     * Because the stream is ordered simply delegates to {@link #findFirst()}.
     */
    @Override
    public Optional<T> findAny() {
        return this.findFirst();
    }

    // TERMINATOR: REDUCE ..............................................................................................

    @Override
    public T reduce(final T identity, BinaryOperator<T> accumulator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<T> reduce(final BinaryOperator<T> accumulator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <U> U reduce(final U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<T> min(final Comparator<? super T> comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<T> max(final Comparator<? super T> comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        return this.assembleStartAndReturnResult((closeables) -> PushableStreamStreamPushableStreamConsumer.count(closeables));
    }

    // ITERATOR ........................................................................................................

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException();
    }

    // GENERAL.........................................................................................................

    /**
     * Always returns this.
     */
    @Override
    public Stream<T> sequential() {
        return this;
    }

    @Override
    public Stream<T> unordered() {
        return this.sequential();
    }

    /**
     * Parallel streams are not supported.
     */
    @Override
    public boolean isParallel() {
        return false;
    }

    /**
     * Always returns the default {@link Stream #sequential}.
     */
    @Override
    public Stream<T> parallel() {
        return this.sequential();
    }

    // CLOSE............................................................................................................

    @Override
    public Stream<T> onClose(final Runnable closeable) {
        return this.replace(this.closeables.add(closeable), this.intermediates);
    }

    @Override
    public void close() {
        this.closeables.close();
    }

    // VisibleForTesting
    final CloseableCollection closeables;

    // build, assemble .................................................................................................

    /**
     * Accepts a factory which will assemble the intermediate ops along with the terminal, and pass the final {@link PushableStreamConsumer}
     * to the {@link Consumer #starter}. Once that returns the result from the {@link PushableStreamStreamTerminalPushableStreamConsumer#result()}
     * will be returned.
     */
    private <R> R assembleStartAndReturnResult(final Function<CloseableCollection, PushableStreamStreamTerminalPushableStreamConsumer<T, R>> terminalFactory) {
        try (final PushableStreamStreamTerminalPushableStreamConsumer<T, R> terminal = terminalFactory.apply(this.closeables)) {

            int i = intermediates.size() - 1;
            PushableStreamConsumer<?> next = terminal;
            PushableStreamConsumer<?> first = terminal;

            while (i >= 0) {
                first = intermediates.get(i).createWithNext(next);
                next = first;
                i--;
            }

            this.starter.accept(Cast.to(first));
            return terminal.result();
        } catch (final RuntimeException rethrow) {
            throw rethrow;
        } catch (final Exception should) {
            throw new NeverError(should.getMessage(), should);
        }
    }

    /**
     * The terminal method will pass a {@link PushableStreamConsumer} to this {@link Consumer#accept(Object)} to start the {@link Stream}.
     */
    // VisibleForTesting
    final Consumer<PushableStreamConsumer<T>> starter;

    /**
     * Appends another intermediate op to this stream returning a new stream, leaving the original unmodified.
     */
    final PushableStreamStream<?> appendIntermediate(final PushableStreamStreamIntermediate intermediate) {
        final List<PushableStreamStreamIntermediate> copy = this.copyIntermediates();
        copy.add(intermediate);
        return this.replace(this.closeables, copy);
    }

    private List<PushableStreamStreamIntermediate> copyIntermediates() {
        final List<PushableStreamStreamIntermediate> copy = Lists.array();
        copy.addAll(this.intermediates);
        return copy;
    }

    // VisibleForTesting
    final List<PushableStreamStreamIntermediate> intermediates;

    /**
     * Factory that creates a new {@link PushableStreamStream} with the new {@link PushableStreamStreamIntermediate}.
     */
    private PushableStreamStream<T> replaceIntermediates(final List<PushableStreamStreamIntermediate> intermediates) {
        return this.replace(closeables, intermediates);
    }

    /**
     * Factory that creates a new {@link PushableStreamStream} with the given properties sharing state where possible.
     */
    private PushableStreamStream<T> replace(final CloseableCollection closeables,
                                            final List<PushableStreamStreamIntermediate> intermediates) {
        return new PushableStreamStream<>(this.starter, closeables, Lists.readOnly(intermediates));
    }

    /**
     * Type safe casting helper.
     */
    private <R> PushableStreamStream<R> cast() {
        return Cast.to(this);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.starter, this.intermediates, this.closeables);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof PushableStreamStream && this.equals0(Cast.to(other));
    }

    private boolean equals0(final PushableStreamStream<?> other) {
        return this.starter.equals(other.starter) &&
                this.intermediates.equals(other.intermediates) &&
                this.closeables.equals(other.closeables);
    }

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    @Override
    public void buildToString(final ToStringBuilder builder) {
        builder.valueSeparator(" ");
        builder.value(this.starter);
        builder.value(this.intermediates);
        builder.value(this.closeables);
    }
}
