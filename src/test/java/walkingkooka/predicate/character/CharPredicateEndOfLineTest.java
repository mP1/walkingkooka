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

final public class CharPredicateEndOfLineTest extends CharPredicateTestCase<CharPredicateEndOfLine> {

    @Test
    public void testCarriageReturn() {
        this.testTrue('\r');
    }

    @Test
    public void testNewLine() {
        this.testTrue('\n');
    }

    @Test
    public void testWhitespace() {
        this.testFalse(' ');
    }

    @Test
    public void testOther() {
        this.testFalse('A');
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(CharPredicateEndOfLine.INSTANCE, "cr/nl");
    }

    @Override
    public CharPredicateEndOfLine createCharPredicate() {
        return CharPredicateEndOfLine.INSTANCE;
    }

    @Override
    public Class<CharPredicateEndOfLine> type() {
        return CharPredicateEndOfLine.class;
    }
}
