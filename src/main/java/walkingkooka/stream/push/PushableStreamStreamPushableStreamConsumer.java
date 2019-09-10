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

import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.UsesToStringBuilder;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Base class for all {@link PushableStreamConsumer} including those returned by {@link PushableStreamStreamIntermediate#createWithNext(PushableStreamConsumer)}.
 */
abstract class PushableStreamStreamPushableStreamConsumer<T> implements PushableStreamConsumer<T>,
        UsesToStringBuilder {

    /**
     * {@see AllMatchPushableStreamStreamTerminalPushableStreamConsumer}
     */
    static <T> AllMatchPushableStreamStreamTerminalPushableStreamConsumer<T> allMatch(final Predicate<? super T> predicate,
                                                                                      final CloseableCollection closeables) {
        return AllMatchPushableStreamStreamTerminalPushableStreamConsumer.with(predicate, closeables);
    }
    
    /**
     * {@see AnyMatchPushableStreamStreamTerminalPushableStreamConsumer}
     */
    static <T> AnyMatchPushableStreamStreamTerminalPushableStreamConsumer<T> anyMatch(final Predicate<? super T> predicate,
                                                                                      final CloseableCollection closeables) {
        return AnyMatchPushableStreamStreamTerminalPushableStreamConsumer.with(predicate, closeables);
    }

    /**
     * {@see CollectorPushableStreamStreamTerminalPushableStreamConsumer}
     */
    static <T, A, R> CollectorPushableStreamStreamTerminalPushableStreamConsumer<T, A, R> collector(final Collector<? super T, A, R> collector,
                                                                                                    final CloseableCollection closeables) {
        return CollectorPushableStreamStreamTerminalPushableStreamConsumer.with(collector, closeables);
    }

    /**
     * {@see CountPushableStreamStreamTerminalPushableStreamConsumer}
     */
    static <T> CountPushableStreamStreamTerminalPushableStreamConsumer<T> count(final CloseableCollection closeables) {
        return CountPushableStreamStreamTerminalPushableStreamConsumer.with(closeables);
    }

    /**
     * {@see FilterPushableStreamStreamIntermediatePushableStreamConsumer}
     */
    static <T> FilterPushableStreamStreamIntermediatePushableStreamConsumer<T> filter(final Predicate<T> predicate,
                                                                                      final PushableStreamConsumer<T> next) {
        return FilterPushableStreamStreamIntermediatePushableStreamConsumer.with(predicate, next);
    }

    /**
     * {@see FindFirstOrderedPushableStreamStreamTerminalPushableStreamConsumer}
     */
    static <T> FindFirstOrderedPushableStreamStreamTerminalPushableStreamConsumer<T> findFirst(final CloseableCollection closeables) {
        return FindFirstOrderedPushableStreamStreamTerminalPushableStreamConsumer.with(closeables);
    }

    /**
     * {@see FlatMapPushableStreamStreamIntermediatePushableStreamConsumer}
     */
    static <T, R> FlatMapPushableStreamStreamIntermediatePushableStreamConsumer<T, R> flatMap(final Function<? super T, ? extends Stream<? extends R>> mapper,
                                                                                              final PushableStreamConsumer<T> next) {
        return FlatMapPushableStreamStreamIntermediatePushableStreamConsumer.with(mapper, next);
    }

    /**
     * {@see ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer}
     */
    static <T> ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer<T> forEach(final Consumer<? super T> action,
                                                                                           final CloseableCollection closeables) {
        return ForEachOrderedPushableStreamStreamTerminalPushableStreamConsumer.with(action, closeables);
    }

    /**
     * {@see LimitPushableStreamStreamIntermediatePushableStreamConsumer}
     */
    static <T> LimitPushableStreamStreamIntermediatePushableStreamConsumer<T> limit(final long skip,
                                                                                    final PushableStreamConsumer<T> next) {
        return LimitPushableStreamStreamIntermediatePushableStreamConsumer.with(skip, next);
    }

    /**
     * {@see MapPushableStreamStreamIntermediatePushableStreamConsumer}
     */
    static <T> MapPushableStreamStreamIntermediatePushableStreamConsumer<T> map(final Function<T, ?> mapper,
                                                                                final PushableStreamConsumer<T> next) {
        return MapPushableStreamStreamIntermediatePushableStreamConsumer.with(mapper, next);
    }

    /**
     * {@see MaxPushableStreamStreamTerminalPushableStreamConsumer}
     */
    static <T> MaxPushableStreamStreamTerminalPushableStreamConsumer<T> max(final Comparator<? super T> comparator,
                                                                                                  final CloseableCollection closeables) {
        return MaxPushableStreamStreamTerminalPushableStreamConsumer.with(comparator, closeables);
    }

    /**
     * {@see MinPushableStreamStreamTerminalPushableStreamConsumer}
     */
    static <T> MinPushableStreamStreamTerminalPushableStreamConsumer<T> min(final Comparator<? super T> comparator,
                                                                                                  final CloseableCollection closeables) {
        return MinPushableStreamStreamTerminalPushableStreamConsumer.with(comparator, closeables);
    }

    /**
     * {@see NoneMatchPushableStreamStreamTerminalPushableStreamConsumer}
     */
    static <T> NoneMatchPushableStreamStreamTerminalPushableStreamConsumer<T> noneMatch(final Predicate<? super T> predicate,
                                                                                        final CloseableCollection closeables) {
        return NoneMatchPushableStreamStreamTerminalPushableStreamConsumer.with(predicate, closeables);
    }
    
    /**
     * {@see PeekPushableStreamStreamIntermediatePushableStreamConsumer}
     */
    static <T> PeekPushableStreamStreamIntermediatePushableStreamConsumer<T> peek(final Consumer<T> consumer,
                                                                                  final PushableStreamConsumer<T> next) {
        return PeekPushableStreamStreamIntermediatePushableStreamConsumer.with(consumer, next);
    }

    /**
     * {@see ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer}
     */
    static <T> ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer<T> reduce(final BiFunction<T, ? super T, T> reducer,
                                                                                             final CloseableCollection closeables) {
        return ReduceAccumulatorPushableStreamStreamTerminalPushableStreamConsumer.with(reducer, closeables);
    }

    /**
     * {@see ReduceInitialAccumulatorPushableStreamStreamTerminalPushableStreamConsumer}
     */
    static <T> ReduceInitialAccumulatorPushableStreamStreamTerminalPushableStreamConsumer<T> reduceWithInitial(final T initial,
                                                                                                               final BiFunction<T, ? super T, T> reducer,
                                                                                                               final CloseableCollection closeables) {
        return ReduceInitialAccumulatorPushableStreamStreamTerminalPushableStreamConsumer.with(initial, reducer, closeables);
    }

    /**
     * {@see SkipPushableStreamStreamIntermediatePushableStreamConsumer}
     */
    static <T> SkipPushableStreamStreamIntermediatePushableStreamConsumer<T> skip(final long skip,
                                                                                  final PushableStreamConsumer<T> next) {
        return SkipPushableStreamStreamIntermediatePushableStreamConsumer.with(skip, next);
    }

    /**
     * Package private to limit sub classing.
     */
    PushableStreamStreamPushableStreamConsumer() {
        super();
    }

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    @Override
    public final void buildToString(final ToStringBuilder builder) {
        builder.labelSeparator(" ");
        builder.separator(" ");
        builder.disable(ToStringBuilderOption.QUOTE);

        this.buildToString0(builder);
    }

    abstract void buildToString0(final ToStringBuilder builder);
}