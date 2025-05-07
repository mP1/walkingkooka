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
import walkingkooka.InvalidCharacterException;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.text.CharSequences;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CharPredicatesTest implements PublicStaticHelperTesting<CharPredicates> {

    // FailIfNullOrEmptyOrInitialAndPartFalse............ ..............................................................

    private final static CharPredicate PREDICATE = CharPredicates.fake();

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseNullCharSequenceFails() {
        assertThrows(NullPointerException.class, () -> CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(null, "TEXT", PREDICATE, PREDICATE));
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseEmptyCharSequenceFails() {
        assertThrows(IllegalArgumentException.class, () -> CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("", "TEXT", PREDICATE, PREDICATE));
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseNullLabelFails() {
        assertThrows(NullPointerException.class, () -> CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("ABC", null, PREDICATE, PREDICATE));
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseEmptyLabelFails() {
        assertThrows(IllegalArgumentException.class, () -> CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("ABC", "", PREDICATE, PREDICATE));
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseNullInitialFails() {
        assertThrows(NullPointerException.class, () -> CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("ABC", "TEXT", null, PREDICATE));
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseNullPartFails() {
        assertThrows(NullPointerException.class, () -> CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("ABC", "TEXT", PREDICATE, null));
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseInitialTestFails() {
        assertThrows(InvalidCharacterException.class, () -> this.failIfNullOrEmptyOrInitialAndPartFalseAndCheck("98"));
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalsePartTestFails() {
        assertThrows(InvalidCharacterException.class, () -> this.failIfNullOrEmptyOrInitialAndPartFalseAndCheck("AB"));
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalsePassInitialOnly() {
        this.failIfNullOrEmptyOrInitialAndPartFalseAndCheck("A");
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalsePasses2() {
        this.failIfNullOrEmptyOrInitialAndPartFalseAndCheck("A1");
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalsePasses3() {
        this.failIfNullOrEmptyOrInitialAndPartFalseAndCheck("A12345678");
    }

    private void failIfNullOrEmptyOrInitialAndPartFalseAndCheck(final String text) {
        assertSame(
            text,
            CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(
                text,
                "TEXT",
                CharPredicates.letter(),
                CharPredicates.digit()
            )
        );
    }

    // isInitialOrPart..................................................................................................

    @Test
    public void testIsInitialAndPartNull() {
        this.isInitialAndPartCheck(
            null,
            false
        );
    }

    @Test
    public void testIsInitialAndPartEmpty() {
        this.isInitialAndPartCheck(
            "",
            false
        );
    }

    @Test
    public void testIsInitialAndPartInitialFail() {
        this.isInitialAndPartCheck(
            "!",
            false
        );
    }

    @Test
    public void testIsInitialAndPartPartlFail() {
        this.isInitialAndPartCheck(
            "A!",
            false
        );
    }

    @Test
    public void testIsInitialAndPartOneChar() {
        this.isInitialAndPartCheck(
            "A",
            true
        );
    }

    @Test
    public void testIsInitialAndPartSeveralChars() {
        this.isInitialAndPartCheck(
            "A1",
            true
        );
    }

    @Test
    public void testIsInitialAndPartSeveralChars2() {
        this.isInitialAndPartCheck(
            "A123",
            true
        );
    }

    private void isInitialAndPartCheck(final String text,
                                       final boolean expected) {
        this.checkEquals(
            expected,
            CharPredicates.isInitialAndPart(
                text,
                Character::isLetter,
                Character::isDigit
            ),
            () -> "isInitialAndPart(" + CharSequences.quoteAndEscape(text) + ", ALPHA, DIGITS)"
        );
    }

    @Override
    public Class<CharPredicates> type() {
        return CharPredicates.class;
    }

    @Override
    public boolean canHavePublicTypes(Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
