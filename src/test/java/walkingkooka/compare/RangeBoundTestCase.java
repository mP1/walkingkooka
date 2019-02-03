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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class RangeBoundTestCase<B extends RangeBound<Integer>> extends ClassTestCase<B>
        implements IsMethodTesting<B> {

    RangeBoundTestCase() {
        super();
    }

    final void valueAndCheck(final Optional<Integer> value) {
        this.valueAndCheck(this.createRangeBound(), value);
    }

    final void valueAndCheck(final B bound, final Optional<Integer> value) {
        assertEquals(value, bound.value(), () -> "value of " + bound);
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
        assertEquals(expected, bound.min(other), () -> bound + " lower " + other);
        assertEquals(expected, other.min(bound), () -> bound + " lower " + other);
    }

    final void maxAndCheck(final RangeBound<Integer> other, final RangeBound<Integer> expected) {
        this.maxAndCheck(this.createRangeBound(), other, expected);
    }

    final void maxAndCheck(final B bound, final RangeBound<Integer> other, final RangeBound<Integer> expected) {
        this.checkEither(bound, other, expected);
        assertEquals(expected, bound.max(other), () -> bound + " upper " + other);
        assertEquals(expected, other.max(bound), () -> bound + " upper " + other);
    }

    private void checkEither(final B bound, final RangeBound<Integer> other, final RangeBound<Integer> expected) {
        if (!bound.equals(expected) && !other.equals(expected)) {
            fail("Expected " + expected + " must be either " + bound + " | " + other);
        }
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    // IsMethodTesting.................................................................................................

    @Override
    public final B createIsMethodObject() {
        return this.createRangeBound();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return RangeBound.class.getSimpleName();
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return "";
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> false;
    }
}
