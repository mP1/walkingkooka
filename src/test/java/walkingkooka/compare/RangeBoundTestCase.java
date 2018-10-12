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

package walkingkooka.compare;

import org.junit.Test;
import walkingkooka.test.PackagePrivateClassTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class RangeBoundTestCase<B extends RangeBound<Integer>> extends PackagePrivateClassTestCase<B> {

    RangeBoundTestCase() {
        super();
    }

    @Test
    public final void testMinAll() {
        this.minAndCheck(RangeBound.all(), this.createRangeBound());
    }

    @Test
    public final void testMaxAll() {
        this.maxAndCheck(RangeBound.all(), this.createRangeBound());
    }

    abstract B createRangeBound();

    final void minAndCheck(final RangeBound<Integer> other, final RangeBound<Integer> expected) {
        this.minAndCheck(this.createRangeBound(), other, expected);
    }

    final void minAndCheck(final B bound, final RangeBound<Integer> other, final RangeBound<Integer> expected) {
        this.checkEither(bound, other, expected);
        assertEquals(bound + " lower " + other, expected, bound.min(other));
        assertEquals(bound + " lower " + other, expected, other.min(bound));
    }

    final void maxAndCheck(final RangeBound<Integer> other, final RangeBound<Integer> expected) {
        this.maxAndCheck(this.createRangeBound(), other, expected);
    }

    final void maxAndCheck(final B bound, final RangeBound<Integer> other, final RangeBound<Integer> expected) {
        this.checkEither(bound, other, expected);
        assertEquals(bound + " upper " + other, expected, bound.max(other));
        assertEquals(bound + " upper " + other, expected, other.max(bound));
    }

    private void checkEither(final B bound, final RangeBound<Integer> other, final RangeBound<Integer> expected) {
        if (!bound.equals(expected) && !other.equals(expected)) {
            fail("Expected " + expected + " must be either " + bound + " | " + other);
        }
    }
}
