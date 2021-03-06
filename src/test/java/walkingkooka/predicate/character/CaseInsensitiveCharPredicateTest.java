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

public final class CaseInsensitiveCharPredicateTest implements CharPredicateTesting<CaseInsensitiveCharPredicate> {

    @Test
    public void testWithNullCharPredicateFails() {
        assertThrows(NullPointerException.class, () -> CaseInsensitiveCharPredicate.with(null));
    }

    @Test
    public void testTestTrue() {
        this.testTrue('A');
        this.testTrue('b');
    }

    @Test
    public void testTestFalse() {
        this.testFalse('d');
        this.testFalse('1');
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCharPredicate(), "\"abc\" (CaseInsensitive)");
    }

    @Override
    public CaseInsensitiveCharPredicate createCharPredicate() {
        return Cast.to(CaseInsensitiveCharPredicate.with(CharPredicates.any("abc")));
    }

    @Override
    public Class<CaseInsensitiveCharPredicate> type() {
        return CaseInsensitiveCharPredicate.class;
    }
}
