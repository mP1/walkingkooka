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
import walkingkooka.text.Ascii;

import static org.junit.jupiter.api.Assertions.assertEquals;

final public class AsciiControlCharPredicateTest
        extends CharPredicateTestCase<AsciiControlCharPredicate> implements SerializationTesting<AsciiControlCharPredicate> {

    @Test
    public void testControl() {
        this.testTrue(Ascii.NUL);
    }

    @Test
    public void testLetter() {
        this.testFalse('A');
    }

    @Test
    public void testToString() {
        assertEquals("ASCII control", AsciiControlCharPredicate.INSTANCE.toString());
    }

    @Override
    protected AsciiControlCharPredicate createCharPredicate() {
        return AsciiControlCharPredicate.INSTANCE;
    }

    @Override
    public Class<AsciiControlCharPredicate> type() {
        return AsciiControlCharPredicate.class;
    }

    @Override
    public AsciiControlCharPredicate serializableInstance() {
        return AsciiControlCharPredicate.INSTANCE;
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }
}
