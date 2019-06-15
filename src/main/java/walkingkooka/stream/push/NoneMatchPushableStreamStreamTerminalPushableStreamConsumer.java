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

import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * The NONE MATCH TERMINAL.
 */
final class NoneMatchPushableStreamStreamTerminalPushableStreamConsumer<T> extends PredicatePushableStreamStreamTerminalPushableStreamConsumer<T> {

    static <T> NoneMatchPushableStreamStreamTerminalPushableStreamConsumer<T> with(final Predicate<? super T> predicate,
                                                                                   final CloseableCollection closeables) {
        Objects.requireNonNull(predicate, "predicate");

        return new NoneMatchPushableStreamStreamTerminalPushableStreamConsumer<T>(predicate, closeables);
    }

    private NoneMatchPushableStreamStreamTerminalPushableStreamConsumer(final Predicate<? super T> predicate,
                                                                        final CloseableCollection closeables) {
        super(true, predicate, closeables);
    }

    /**
     * Stop whenever a predicate test is true.
     */
    @Override
    public boolean isFinished() {
        return false == this.result;
    }

    @Override
    public void accept(final T value) {
        if (this.predicate.test(value)) {
            if (this.isFinished()) {
                throw new PushStreamException("Second predicate false when none match is already true, second value=" + CharSequences.quoteIfChars(value));
            }
            this.result = false;
        }
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NoneMatchPushableStreamStreamTerminalPushableStreamConsumer;
    }

    @Override
    String label() {
        return "noneMatch";
    }
}
