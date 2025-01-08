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

import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.CharSequences;

import java.util.function.Function;

/**
 * Mixin interface for testing {@link Function}
 */
public interface FunctionTesting<F extends Function<T, R>, T, R> extends TypeNameTesting<F> {

    default void applyAndCheck(final T input,
                               final R result) {
        this.applyAndCheck(this.createFunction(), input, result);
    }

    default <TT, RR> void applyAndCheck(final Function<TT, RR> function,
                                        final TT input,
                                        final RR result) {
        this.checkEquals(result,
            function.apply(input),
            () -> "Wrong result for " + function + " for params: " + CharSequences.quoteIfChars(input));
    }

    F createFunction();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Function.class.getSimpleName();
    }
}
