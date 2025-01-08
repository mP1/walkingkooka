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

package walkingkooka.text;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.predicate.character.CharPredicateTesting;

final public class CaseSensitivityCharPredicateTest
    implements CharPredicateTesting<CaseSensitivityCharPredicate>,
    HashCodeEqualsDefinedTesting2<CaseSensitivityCharPredicate> {

    // constants

    private final static char CHAR = 'a';
    private final static CaseSensitivity SENSITIVITY = CaseSensitivity.SENSITIVE;

    // tests

    @Test
    public void testExactCaseSensitive() {
        this.testTrue(this.createCharPredicateCaseSensitive(), CHAR);
    }

    @Test
    public void testWrongCaseCaseSensitive() {
        this.testFalseCaseSensitive('A');
    }

    @Test
    public void testEqualsDifferentCaseSensitive() {
        this.testFalseCaseSensitive('B');
    }

    @Test
    public void testExactCaseInsensitive() {
        this.testTrueCaseInsensitive('A');
    }

    @Test
    public void testEqualsDifferentCaseCaseInsensitive() {
        this.testTrueCaseInsensitive('a');
    }

    @Test
    public void testEqualsDifferentCaseInsensitive() {
        this.testFalseCaseInsensitive('z');
    }

    @Test
    public void testEqualsDifferentCaseInsensitive2() {
        this.testFalseCaseInsensitive('y');
    }

    @Test
    public void testEqualsDifferentCharacter() {
        this.checkNotEquals(CaseSensitivityCharPredicate.with('b', SENSITIVITY));
    }

    @Test
    public void testEqualsDifferentCase() {
        this.checkNotEquals(CaseSensitivityCharPredicate.with('A', SENSITIVITY));
    }

    @Test
    public void testEqualsDifferentCaseSensitivity() {
        this.checkNotEquals(CaseSensitivityCharPredicate.with(
            CHAR,
            SENSITIVITY.invert()));
    }

    @Test
    public void testToStringCaseSensitive() {
        this.toStringAndCheck(this.createCharPredicate(),
            "'A'");
    }

    @Test
    public void testToStringCaseInsensitive() {
        this.toStringAndCheck(this.createCharPredicateCaseInsensitive('A'),
            "'A' (CaseInsensitive)");
    }

    @Override
    public CaseSensitivityCharPredicate createCharPredicate() {
        return this.createCharPredicateCaseSensitive('A');
    }

    private void testFalseCaseSensitive(final char c) {
        this.testFalse(this.createCharPredicateCaseSensitive(), c);
    }

    private CaseSensitivityCharPredicate createCharPredicateCaseSensitive() {
        return this.createCharPredicateCaseSensitive(CHAR);
    }

    private CaseSensitivityCharPredicate createCharPredicateCaseSensitive(final char c) {
        return (CaseSensitivityCharPredicate) SENSITIVITY.charPredicate(c);
    }

    private void testTrueCaseInsensitive(final char c) {
        this.testTrue(this.createCharPredicateCaseInsensitive(), c);
    }

    private void testFalseCaseInsensitive(final char c) {
        this.testFalse(this.createCharPredicateCaseInsensitive(), c);
    }

    private CaseSensitivityCharPredicate createCharPredicateCaseInsensitive() {
        return this.createCharPredicateCaseInsensitive(CHAR);
    }

    private CaseSensitivityCharPredicate createCharPredicateCaseInsensitive(final char c) {
        return (CaseSensitivityCharPredicate) CaseSensitivity.INSENSITIVE.charPredicate(c);
    }

    @Override
    public Class<CaseSensitivityCharPredicate> type() {
        return CaseSensitivityCharPredicate.class;
    }

    @Override
    public CaseSensitivityCharPredicate createObject() {
        return CaseSensitivityCharPredicate.with(CHAR, SENSITIVITY);
    }
}
