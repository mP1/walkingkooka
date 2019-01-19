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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.test.SerializationTesting;

import static org.junit.Assert.assertEquals;

final public class LetterCharPredicateTest extends CharPredicateTestCase<LetterCharPredicate> implements SerializationTesting<LetterCharPredicate> {

    @Test
    public void testLetter() {
        Assert.assertTrue(this.test('A'));
    }

    @Test
    public void testLowerCaseLetter() {
        this.testTrue('a');
    }

    @Test
    public void testDigit() {
        this.testFalse('0');
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
        assertEquals("letter", LetterCharPredicate.INSTANCE.toString());
    }

    @Override
    protected LetterCharPredicate createCharPredicate() {
        return LetterCharPredicate.INSTANCE;
    }

    @Override
    public Class<LetterCharPredicate> type() {
        return LetterCharPredicate.class;
    }

    @Override
    public LetterCharPredicate serializableInstance() {
        return LetterCharPredicate.INSTANCE;
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }
}
