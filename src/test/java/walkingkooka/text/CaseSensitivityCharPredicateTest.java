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

package walkingkooka.text;

import org.junit.Test;
import walkingkooka.predicate.character.CharPredicateTestCase;

import static org.junit.Assert.assertEquals;

final public class CaseSensitivityCharPredicateTest
        extends CharPredicateTestCase<CaseSensitivityCharPredicate> {

    // constants

    private final static char CHAR = 'a';

    // tests

    @Test
    public void testExactCaseSensitive() {
        this.testTrueCaseSensitive(CaseSensitivityCharPredicateTest.CHAR);
    }

    @Test
    public void testWrongCaseCaseSensitive() {
        this.testFalseCaseSensitive('A');
    }

    @Test
    public void testDifferentCaseSensitive() {
        this.testFalseCaseSensitive('B');
    }

    @Test
    public void testExactCaseInsensitive() {
        this.testTrueCaseInsensitive('A');
    }

    @Test
    public void testDifferentCaseCaseInsensitive() {
        this.testTrueCaseInsensitive('a');
    }

    @Test
    public void testDifferentCaseInsensitive() {
        this.testFalseCaseInsensitive('z');
    }

    @Test
    public void testToStringCaseSensitive() {
        assertEquals("'A'", this.createCharacterPredicate().toString());
    }

    @Test
    public void testToStringCaseInsensitive() {
        assertEquals("'A' (CaseInsensitive)",
                this.createCharacterPredicateCaseInsensitive('A').toString());
    }

    @Override
    protected CaseSensitivityCharPredicate createCharacterPredicate() {
        return this.createCharacterPredicateCaseSensitive('A');
    }

    private void testTrueCaseSensitive(final char c) {
        this.testTrue(this.createCharacterPredicateCaseSensitive(), c);
    }

    private void testFalseCaseSensitive(final char c) {
        this.testFalse(this.createCharacterPredicateCaseSensitive(), c);
    }

    private CaseSensitivityCharPredicate createCharacterPredicateCaseSensitive() {
        return this.createCharacterPredicateCaseSensitive(CaseSensitivityCharPredicateTest.CHAR);
    }

    private CaseSensitivityCharPredicate createCharacterPredicateCaseSensitive(final char c) {
        return (CaseSensitivityCharPredicate) CaseSensitivity.SENSITIVE.charPredicate(c);
    }

    private void testTrueCaseInsensitive(final char c) {
        this.testTrue(this.createCharacterPredicateCaseInsensitive(), c);
    }

    private void testFalseCaseInsensitive(final char c) {
        this.testFalse(this.createCharacterPredicateCaseInsensitive(), c);
    }

    private CaseSensitivityCharPredicate createCharacterPredicateCaseInsensitive() {
        return this.createCharacterPredicateCaseInsensitive(CaseSensitivityCharPredicateTest.CHAR);
    }

    private CaseSensitivityCharPredicate createCharacterPredicateCaseInsensitive(final char c) {
        return (CaseSensitivityCharPredicate) CaseSensitivity.INSENSITIVE.charPredicate(c);
    }

    @Override
    protected Class<CaseSensitivityCharPredicate> type() {
        return CaseSensitivityCharPredicate.class;
    }
}
