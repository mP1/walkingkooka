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

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;

public final class EitherTest extends EitherTestCase<Either<String, Integer>, String, Integer> {

    // with.............................................................................................................

    @Test
    public void testLeftNullValue() {
        final Either<Void, Void> either = Either.left(null);
        this.checkEquals(null, either.leftValue(), "left");
    }

    @Test
    public void testRightNullValue() {
        final Either<Void, Void> either = Either.right(null);
        this.checkEquals(null, either.rightValue(), "right");
    }

    @Test
    public void testLeftThenMapLeftThenLeftValue() {
        this.checkEquals("v1!",
            Either.left("v1")
                .mapLeft(v -> v + "!")
                .mapRight(v -> 555)
                .leftValue());
    }

    @Test
    public void testRightThenMapRightThenRightValue() {
        this.checkEquals("v1!",
            Either.right("v1")
                .mapRight(v -> v + "!")
                .mapLeft(v -> 555)
                .rightValue());
    }

    @Test
    public void testLeftSwapAndSwap() {
        final Either<String, Integer> either = Either.left("abc");
        this.checkEquals(either,
            either.swap()
                .swap());
    }

    @Test
    public void testRightSwapAndSwap() {
        final Either<Integer, String> either = Either.right("abc");
        this.checkEquals(either,
            either.swap()
                .swap());
    }

    @Override
    Either<String, Integer> createEither() {
        return Either.left("value");
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Class<Either<String, Integer>> type() {
        return Cast.to(Either.class);
    }
}
