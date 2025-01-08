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
import walkingkooka.text.Ascii;

final public class AsciiPrintableCharPredicateTest
    implements CharPredicateTesting<AsciiPrintableCharPredicate> {

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
        this.toStringAndCheck(AsciiPrintableCharPredicate.INSTANCE, "ASCII printable");
    }

    @Override
    public AsciiPrintableCharPredicate createCharPredicate() {
        return AsciiPrintableCharPredicate.INSTANCE;
    }

    @Override
    public Class<AsciiPrintableCharPredicate> type() {
        return AsciiPrintableCharPredicate.class;
    }
}
