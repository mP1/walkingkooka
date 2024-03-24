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

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EitherLeftTest extends EitherTestCase2<EitherLeft<String, Integer>, String, Integer> {

    private final static String VALUE = "++123";

    // value............................................................................................................

    @Test
    public void testLeftValueFails() {
        this.checkEquals(VALUE, this.createEither().leftValue());
    }

    @Test
    public void testRightValueFails() {
        assertThrows(NoSuchElementException.class, () -> this.createEither().rightValue());
    }

    // setValue.........................................................................................................

    @Test
    public void testSetLeftValueNullSame() {
        final EitherLeft<String, Integer> either = EitherLeft.withLeft(null);
        assertSame(either, either.setLeftValue(null));
    }

    @Test
    public void testSetLeftValueSame() {
        final EitherLeft<String, Integer> either = this.createEither();
        assertSame(either, either.setLeftValue(VALUE));
    }

    @Test
    public void testSetLeftValueDifferent() {
        final EitherLeft<String, Integer> either = this.createEither();
        final String value2 = "different";
        final Either<String, Integer> different = either.setLeftValue(value2);
        assertNotSame(either, different);
        checkValue(different, value2);
    }

    @Test
    public void testSetRightValueDifferent() {
        final EitherLeft<String, Integer> either = this.createEither();
        final Integer value2 = 999;
        final Either<String, Integer> different = either.setRightValue(value2);
        assertNotSame(either, different);
        checkValue(different, value2);
    }

    // orElse............................................................................................................

    @Test
    public void testOrElseLeftNull() {
        this.orElseLeftAndCheck(this.createEither(), null, VALUE);
    }

    @Test
    public void testOrElseLeft() {
        this.orElseLeftAndCheck(this.createEither(), "never", VALUE);
    }

    @Test
    public void testOrElseRightNull() {
        this.orElseRightAndCheck(this.createEither(), null, null);
    }

    @Test
    public void testOrElseRight() {
        this.orElseRightAndCheck(this.createEither(), 123, 123);
    }

    // orElseGet.........................................................................................................

    @Test
    public void testOrElseLeftGet() {
        this.orElseLeftGetAndCheck(this.createEither(), "never", VALUE);
    }

    @Test
    public void testOrElseRightGet() {
        this.orElseRightGetAndCheck(this.createEither(), 123, 123);
    }

    // orElseThrow......................................................................................................

    @Test
    public void testOrElseLeftThrow() {
        this.orElseLeftThrowAndCheck(this.createEither(), VALUE);
    }

    @Test
    public void testOrElseRightThrowFails() {
        assertThrows(Exception.class, () -> this.createEither().orElseRightThrow(Exception::new));
    }

    // swap.............................................................................................................

    @Test
    public void testSwap() {
        final Either<String, Integer> left = this.createEither();
        final Either<Integer, String> swap = left.swap();
        this.checkValue(swap, VALUE);
        this.checkEquals(EitherRight.class, swap.getClass(), "class");
    }

    // map..............................................................................................................

    @Test
    public void testMapLeft() {
        final Either<String, Integer> left = this.createEither();

        final String different = "different-left";
        final Either<String, Integer> mapped = left.mapLeft((v) -> {
            this.checkEquals(VALUE, v);
            return different;
        });
        this.checkValue(mapped, different);
    }

    @Test
    public void testMapRight() {
        final Either<String, Integer> left = this.createEither();
        assertSame(left, left.mapRight((v) -> v));
    }

    // ifPresent.........................................................................................................

    @Test
    public void testIfPresentLeft() {
        this.createEither().ifLeftPresent(this::consumeLeft);
        this.checkEquals(VALUE, this.consumed, "value");
    }

    @Test
    public void testIfPresentRight() {
        this.createEither().ifRightPresent((v) -> {
            throw new UnsupportedOperationException();
        });
    }

    // accept............................................................................................................

    @Test
    public void testAcceptLeft() {
        this.createEither().accept(this::consumeLeft, this::acceptRight);
        this.checkEquals(VALUE, this.consumed, "value");
    }

    private void consumeLeft(final String left) {
        this.consumed = left;
    }

    private String consumed;

    // equality.........................................................................................................

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(EitherLeft.withLeft("different"));
    }

    @Test
    public void testEqualsSameValueRight() {
        this.checkNotEquals(EitherLeft.withLeft(VALUE), Either.right(VALUE));
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createEither(), "Left: \"++123\"");
    }

    // helper..........................................................................................................

    @Override
    EitherLeft<String, Integer> createEither() {
        return EitherLeft.withLeft(VALUE);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<EitherLeft<String, Integer>> type() {
        return Cast.to(EitherLeft.class);
    }
}
