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

final public class WhitespaceCharPredicateTest implements CharPredicateTesting<WhitespaceCharPredicate> {

    @Test
    public void testWhitespace() {
        this.testTrue(' ');
    }

    @Test
    public void testNotWhitespace() {
        this.testFalse('A');
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(WhitespaceCharPredicate.INSTANCE, "whitespace");
    }

    @Override
    public WhitespaceCharPredicate createCharPredicate() {
        return WhitespaceCharPredicate.INSTANCE;
    }

    @Override
    public Class<WhitespaceCharPredicate> type() {
        return WhitespaceCharPredicate.class;
    }
}
