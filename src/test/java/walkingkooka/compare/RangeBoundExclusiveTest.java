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
import walkingkooka.Cast;

public final class RangeBoundExclusiveTest extends RangeBoundTestCase<RangeBoundExclusive<Integer>> {

    private final static int VALUE = 123;

    @Test
    public void testMinExclusive() {
        this.minAndCheck(RangeBound.exclusive(VALUE), RangeBound.exclusive(VALUE));
    }

    // 1, 2, 3      inc
    // 1, 2 , x     ex
    @Test
    public void testMinInclusive() {
        this.minAndCheck(RangeBound.inclusive(VALUE), RangeBound.inclusive(VALUE));
    }

    @Test
    public void testMaxExclusive() {
        this.maxAndCheck(RangeBound.exclusive(VALUE), RangeBound.exclusive(VALUE));
    }

    // 1, 2, 3 inc
    // 1, 2, x ex
    @Test
    public void testMaxInclusive() {
        this.maxAndCheck(RangeBound.inclusive(VALUE), RangeBound.inclusive(VALUE));
    }

    @Override
    RangeBoundExclusive<Integer> createRangeBound() {
        return RangeBoundExclusive.with(VALUE);
    }

    @Override
    protected Class<RangeBoundExclusive<Integer>> type() {
        return Cast.to(RangeBoundExclusive.class);
    }
}
