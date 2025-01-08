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

public final class EitherRightTest extends EitherTestCase2<EitherRight<String, Integer>, String, Integer> {

    private final static Integer VALUE = 1123;

    // value............................................................................................................

    @Test
    public void testLeftValueFails() {
        assertThrows(NoSuchElementException.class, () -> this.createEither().leftValue());
    }

    @Test
    public void testRightValueFails() {
        this.checkEquals(VALUE, this.createEither().rightValue());
    }

    // setValue.........................................................................................................

    @Test
    public void testSetLeftValueDifferent() {
        final EitherRight<String, Integer> either = this.createEither();
        final String value2 = "different-left";
        final Either<String, Integer> different = either.setLeftValue(value2);
        assertNotSame(either, different);
        checkValue(different, value2);
    }

    @Test
    public void testSetRightValueNullSame() {
        final EitherRight<String, Integer> either = EitherRight.withRight(null);
        assertSame(either, either.setRightValue(null));
    }

    @Test
    public void testSetRightValueSame() {
        final EitherRight<String, Integer> either = this.createEither();
        assertSame(either, either.setRightValue(VALUE));
    }

    @Test
    public void testSetRightValueDifferent() {
        final EitherRight<String, Integer> either = this.createEither();
        final Integer value2 = 999;
        final Either<String, Integer> different = either.setRightValue(value2);
        assertNotSame(either, different);
        checkValue(different, value2);
    }

    // orElse...........................................................................................................

    @Test
    public void testOrElseLeftNull() {
        this.orElseLeftAndCheck(this.createEither(), null, null);
    }

    @Test
    public void testOrElseLeft() {
        this.orElseLeftAndCheck(this.createEither(), "abc123", "abc123");
    }

    @Test
    public void testOrElseRightNull() {
        this.orElseRightAndCheck(this.createEither(), null, VALUE);
    }

    @Test
    public void testOrElseRight() {
        this.orElseRightAndCheck(this.createEither(), VALUE, VALUE);
    }

    // orElseGet.........................................................................................................

    @Test
    public void testOrElseLeftGet() {
        this.orElseLeftGetAndCheck(this.createEither(), "abc123", "abc123");
    }

    @Test
    public void testOrElseLeftGetNull() {
        this.orElseLeftGetAndCheck(this.createEither(), null, null);
    }

    @Test
    public void testOrElseRightGet() {
        this.orElseRightGetAndCheck(this.createEither(), VALUE, VALUE);
    }

    @Test
    public void testOrElseRightGetNull() {
        this.orElseRightGetAndCheck(this.createEither(), null, VALUE);
    }

    // orElseThrows.....................................................................................................

    @Test
    public void testOrElseLeftThrowFails() {
        assertThrows(Exception.class, () -> this.createEither().orElseLeftThrow(Exception::new));
    }

    @Test
    public void testOrElseRightThrow() {
        this.orElseRightThrowAndCheck(this.createEither(), VALUE);
    }

    // swap.............................................................................................................

    @Test
    public void testSwap() {
        final Either<String, Integer> right = this.createEither();
        final Either<Integer, String> swap = right.swap();
        this.checkValue(swap, VALUE);
        this.checkEquals(EitherLeft.class, swap.getClass(), "class");
    }

    // map..............................................................................................................

    @Test
    public void testMapLeft() {
        final Either<String, Integer> right = this.createEither();
        assertSame(right, right.mapLeft((v) -> v));
    }

    @Test
    public void testMapRight() {
        final Either<String, Integer> right = this.createEither();

        final Integer different = -1;
        final Either<String, Integer> mapped = right.mapRight((v) -> {
            this.checkEquals(VALUE, v);
            return different;
        });
        this.checkValue(mapped, different);
    }

    // ifPresent........................................................................................................

    @Test
    public void testIfPresentLeft() {
        this.createEither().ifLeftPresent((v) -> {
            throw new UnsupportedOperationException();
        });
    }

    @Test
    public void testIfPresentRight() {
        this.createEither().ifRightPresent(this::consumeRight);
        this.checkEquals(VALUE, this.consumed, () -> "" + VALUE);
    }

    // accept............................................................................................................

    @Test
    public void testAcceptLeft() {
        this.createEither().accept(this::acceptLeft, this::consumeRight);
        this.checkEquals(VALUE, this.consumed, "value");
    }

    private void consumeRight(final Integer right) {
        this.consumed = right;
    }

    private Integer consumed;

    // equality.........................................................................................................

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(EitherRight.withRight("different"));
    }

    @Test
    public void testEqualsSameValueLeft() {
        this.checkNotEquals(EitherRight.withRight(VALUE), Either.left(VALUE));
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createEither(), "Right: 1123");
    }

    // helper...........................................................................................................

    @Override
    EitherRight<String, Integer> createEither() {
        return EitherRight.withRight(VALUE);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<EitherRight<String, Integer>> type() {
        return Cast.to(EitherRight.class);
    }
}
