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

package walkingkooka.collect;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.IsMethodTesting;

import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class RangeBoundTestCase2<B extends RangeBound<Integer>> extends RangeBoundTestCase<B>
    implements IsMethodTesting<B>,
    ToStringTesting<B> {

    RangeBoundTestCase2() {
        super();
    }

    final void valueAndCheck(final Optional<Integer> value) {
        this.valueAndCheck(this.createRangeBound(), value);
    }

    final void valueAndCheck(final B bound, final Optional<Integer> value) {
        this.checkEquals(value, bound.value(), () -> "value of " + bound);
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
        this.checkEquals(expected, bound.min(other), () -> bound + " lower " + other);
        this.checkEquals(expected, other.min(bound), () -> bound + " lower " + other);
    }

    final void maxAndCheck(final RangeBound<Integer> other, final RangeBound<Integer> expected) {
        this.maxAndCheck(this.createRangeBound(), other, expected);
    }

    final void maxAndCheck(final B bound, final RangeBound<Integer> other, final RangeBound<Integer> expected) {
        this.checkEither(bound, other, expected);
        this.checkEquals(expected, bound.max(other), () -> bound + " upper " + other);
        this.checkEquals(expected, other.max(bound), () -> bound + " upper " + other);
    }

    private void checkEither(final B bound, final RangeBound<Integer> other, final RangeBound<Integer> expected) {
        if (!bound.equals(expected) && !other.equals(expected)) {
            fail("Expected " + expected + " must be either " + bound + " | " + other);
        }
    }

    // IsMethodTesting.................................................................................................

    @Override
    public final B createIsMethodObject() {
        return this.createRangeBound();
    }

    @Override
    public String toIsMethodName(final String typeName) {
        return this.toIsMethodNameWithPrefixSuffix(
            typeName,
            RangeBound.class.getSimpleName(), // drop-prefix
            "" // drop-suffix
        );
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> false;
    }
}
