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

package walkingkooka.math;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;

import java.util.Optional;

public final class RangeBoundAllTest extends RangeBoundTestCase2<RangeBoundAll<Integer>> {

    @Test
    public void testValue() {
        this.valueAndCheck(this.createRangeBound(), Optional.empty());
    }

    @Test
    public void testLowerExclusive() {
        this.minAndCheck(RangeBound.exclusive(123), RangeBound.exclusive(123));
    }

    @Test
    public void testLowerInclusive() {
        this.minAndCheck(RangeBound.inclusive(123), RangeBound.inclusive(123));
    }

    @Test
    public void testUpperExclusive() {
        this.maxAndCheck(RangeBound.exclusive(123), RangeBound.exclusive(123));
    }

    @Test
    public void testUpperInclusive() {
        this.maxAndCheck(RangeBound.inclusive(123), RangeBound.inclusive(123));
    }

    @Override
    RangeBoundAll<Integer> createRangeBound() {
        return RangeBoundAll.instance();
    }

    @Override
    public Class<RangeBoundAll<Integer>> type() {
        return Cast.to(RangeBoundAll.class);
    }
}
