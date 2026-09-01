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
package walkingkooka.util;

import walkingkooka.ToStringTesting;

import java.util.function.BiFunction;

/**
 * Mixing interface that provides methods to test a {@link BiFunction}
 */
public interface BiFunctionTesting2<F extends BiFunction<T, U, R>, T, U, R> extends BiFunctionTesting,
    ToStringTesting<F> {

    default void applyAndCheck(final T in1,
                               final U in2,
                               final R expected) {
        this.applyAndCheck(
            this.createBiFunction(),
            in1,
            in2,
            expected
        );
    }

    F createBiFunction();
}
