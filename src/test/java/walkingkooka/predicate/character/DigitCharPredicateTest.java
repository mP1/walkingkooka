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

import org.junit.jupiter.api.Test;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.TestCase;

final public class DigitCharPredicateTest extends TestCase
        implements CharPredicateTesting<DigitCharPredicate>,
        SerializationTesting<DigitCharPredicate> {

    @Test
    public void testLetter() {
        this.testFalse('A');
    }

    @Test
    public void testDigit() {
        this.testTrue('0');
    }

    @Test
    public void testWhitespace() {
        this.testFalse(' ');
    }

    @Test
    public void testSymbol() {
        this.testFalse('!');
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(DigitCharPredicate.INSTANCE, "digit");
    }

    @Override public DigitCharPredicate createCharPredicate() {
        return DigitCharPredicate.INSTANCE;
    }

    @Override
    public Class<DigitCharPredicate> type() {
        return DigitCharPredicate.class;
    }

    @Override
    public DigitCharPredicate serializableInstance() {
        return DigitCharPredicate.INSTANCE;
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }
}
