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

package walkingkooka;

import walkingkooka.reflect.ClassTesting2;

public abstract class EitherTestCase<E extends Either<L, R>, L, R> implements ClassTesting2<E> {

    EitherTestCase() {
        super();
    }

    abstract E createEither();

    // helpers..........................................................................................................

    final void checkValue(final Either<?, ?> either, final Object value) {
        this.checkEquals(value, either.value(), "value");
    }

    final <LL, RR> void orElseLeftAndCheck(final Either<LL, RR> either,
                                           final LL elseValue,
                                           final LL expected) {
        this.checkEquals(expected, either.orElseLeft(elseValue), () -> either + " orElseLeft");
    }

    final <LL, RR> void orElseLeftGetAndCheck(final Either<LL, RR> either,
                                              final LL elseValue,
                                              final LL expected) {
        this.checkEquals(expected, either.orElseLeftGet(() -> elseValue), () -> either + " orElseLeftGet");
    }

    final <LL, RR> void orElseLeftThrowAndCheck(final Either<LL, RR> either,
                                                final LL expected) {
        this.checkEquals(expected, either.orElseLeftThrow(IllegalStateException::new), () -> either + " orElseLeftThrow");
    }

    final <LL, RR> void orElseRightAndCheck(final Either<LL, RR> either,
                                            final RR elseValue,
                                            final RR expected) {
        this.checkEquals(expected, either.orElseRight(elseValue), () -> either + " orElseRight");
    }

    final <LL, RR> void orElseRightGetAndCheck(final Either<LL, RR> either,
                                               final RR elseValue,
                                               final RR expected) {
        this.checkEquals(expected, either.orElseRightGet(() -> elseValue), () -> either + " orElseRightGet");
    }

    final <LL, RR> void orElseRightThrowAndCheck(final Either<LL, RR> either,
                                                 final RR expected) {
        this.checkEquals(expected, either.orElseRightThrow(IllegalStateException::new), () -> either + " orElseLeftThrow");
    }
}
