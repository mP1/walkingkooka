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
import walkingkooka.text.Ascii;

import static org.junit.Assert.assertEquals;

final public class AsciiPrintableCharPredicateTest
        extends CharPredicateTestCase<AsciiPrintableCharPredicate> {

    @Test
    public void testNull() {
        this.testFalse(Ascii.NUL);
    }

    @Test
    public void testLetter() {
        this.testTrue('A');
    }

    @Test
    public void testToString() {
        assertEquals("ASCII printable", AsciiPrintableCharPredicate.INSTANCE.toString());
    }

    @Override
    protected AsciiPrintableCharPredicate createCharacterPredicate() {
        return AsciiPrintableCharPredicate.INSTANCE;
    }

    @Override
    protected Class<AsciiPrintableCharPredicate> type() {
        return AsciiPrintableCharPredicate.class;
    }
}
