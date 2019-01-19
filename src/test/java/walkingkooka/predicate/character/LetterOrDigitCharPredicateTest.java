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
import walkingkooka.test.SerializationTesting;

import static org.junit.Assert.assertEquals;

final public class LetterOrDigitCharPredicateTest
        extends CharPredicateTestCase<LetterOrDigitCharPredicate> implements SerializationTesting<LetterOrDigitCharPredicate> {

    @Test
    public void testWhitespace() {
        this.testFalse(' ');
    }

    @Test
    public void testLetter() {
        this.testTrue('A');
    }

    @Test
    public void testDigit() {
        this.testTrue('1');
    }

    @Test
    public void testToString() {
        assertEquals("Letter/Digit", this.createCharPredicate().toString());
    }

    @Override
    protected LetterOrDigitCharPredicate createCharPredicate() {
        return LetterOrDigitCharPredicate.INSTANCE;
    }

    @Override
    public Class<LetterOrDigitCharPredicate> type() {
        return LetterOrDigitCharPredicate.class;
    }

    @Override
    public LetterOrDigitCharPredicate serializableInstance() {
        return LetterOrDigitCharPredicate.INSTANCE;
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }
}
