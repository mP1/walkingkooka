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

package walkingkooka.predicate.character;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RangeCharPredicateTest implements CharPredicateTesting<RangeCharPredicate> {

    @Test
    public void testWithStartAfterEndFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createCharPredicate('z', 'a'));
    }

    @Test
    public void testWithSameStartAndEnd() {
        this.checkEquals(CharPredicates.is('a'), this.createCharPredicate('a', 'a'));
    }

    @Test
    public void testBeforeRange() {
        this.testFalse('l');
    }

    @Test
    public void testRangeStart() {
        this.testTrue('m');
    }

    @Test
    public void testBetween() {
        this.testTrue('n');
    }

    @Test
    public void testRangeEnd() {
        this.testTrue('p');
    }

    @Test
    public void testAfterRange() {
        this.testFalse('q');
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCharPredicate(), "'m'..'p'");
    }

    @Override
    public RangeCharPredicate createCharPredicate() {
        return Cast.to(this.createCharPredicate('m', 'p'));
    }

    private CharPredicate createCharPredicate(final char start, final char end) {
        return RangeCharPredicate.with(start, end);
    }

    @Override
    public Class<RangeCharPredicate> type() {
        return RangeCharPredicate.class;
    }
}
