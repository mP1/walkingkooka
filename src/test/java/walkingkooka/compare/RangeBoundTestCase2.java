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

package walkingkooka.compare;

import org.junit.jupiter.api.Test;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.marshall.FromJsonNodeContext;
import walkingkooka.tree.json.marshall.FromJsonNodeContexts;
import walkingkooka.tree.json.marshall.FromJsonNodeException;

import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public final void testFromJsonInvalidProperty() {
        assertThrows(FromJsonNodeException.class, () -> {
            RangeBound.fromJsonNode(JsonNode.object()
                    .setChild(JsonNodeName.with("invalid"), JsonNode.booleanNode(true)),
                    this.fromJsonNodeContext());
        });
    }

    abstract B createRangeBound();

    final FromJsonNodeContext fromJsonNodeContext() {
        return FromJsonNodeContexts.basic();
    }

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
