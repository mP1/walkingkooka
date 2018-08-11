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
 */

package walkingkooka.predicate.character;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

final public class EndOfLineCharPredicateTest extends CharPredicateTestCase<EndOfLineCharPredicate> {

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
        assertEquals("cr/nl", EndOfLineCharPredicate.INSTANCE.toString());
    }

    @Override
    protected EndOfLineCharPredicate createCharacterPredicate() {
        return EndOfLineCharPredicate.INSTANCE;
    }

    @Override
    protected Class<EndOfLineCharPredicate> type() {
        return EndOfLineCharPredicate.class;
    }
}
