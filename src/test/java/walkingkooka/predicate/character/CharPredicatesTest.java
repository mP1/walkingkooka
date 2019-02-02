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
 *
 */

package walkingkooka.predicate.character;

import org.junit.jupiter.api.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.test.PublicStaticHelperTestCase;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CharPredicatesTest extends PublicStaticHelperTestCase<CharPredicates> {

    // failIfNullOrFalse .............................................................

    @Test
    public void testFailIfNullOrFalseNullCharSequenceFails() {
        assertThrows(NullPointerException.class, () -> {
            CharPredicates.failIfNullOrFalse(null, "TEXT", predicate());
        });
    }

    @Test
    public void testFailIfNullOrFalseEmptyCharSequence() {
        CharPredicates.failIfNullOrFalse("", "TEXT", predicate());
    }

    @Test
    public void testFailIfNullOrFalseNullLabelFails() {
        assertThrows(NullPointerException.class, () -> {
            CharPredicates.failIfNullOrFalse("ABC", null, predicate());
        });
    }

    @Test
    public void testFailIfNullOrFalseEmptyLabelFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            CharPredicates.failIfNullOrFalse("ABC", "", predicate());
        });
    }

    @Test
    public void testFailIfNullOrFalseTestFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            this.failIfNullOrFalse("98");
        });
    }

    @Test
    public void testFailIfNullOrFalseTestFails2() {
        assertThrows(InvalidCharacterException.class, () -> {
            this.failIfNullOrFalse("A1");
        });
    }

    @Test
    public void testFailIfNullOrFalseEmptyFail() {
        this.failIfNullOrFalse("");
    }

    @Test
    public void testFailIfNullOrFalsePass() {
        this.failIfNullOrFalse("A");
    }

    @Test
    public void testFailIfNullOrFalsePass2() {
        this.failIfNullOrFalse("AB");
    }

    @Test
    public void testFailIfNullOrFalsePass3() {
        this.failIfNullOrFalse("ABCDEFGH");
    }

    private void failIfNullOrFalse(final String text) {
        CharPredicates.failIfNullOrFalse(text, "TEXT", CharPredicates.letter());
    }

    // failIfNullOrEmptyOrFalse ............................................................

    @Test
    public void testFailIfNullOrEmptyOrFalseNullCharSequenceFails() {
        assertThrows(NullPointerException.class, () -> {
            CharPredicates.failIfNullOrEmptyOrFalse(null, "TEXT", predicate());
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrFalseEmptyCharSequenceFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            CharPredicates.failIfNullOrEmptyOrFalse("", "TEXT", predicate());
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrFalseNullLabelFails() {
        assertThrows(NullPointerException.class, () -> {
            CharPredicates.failIfNullOrEmptyOrFalse("ABC", null, predicate());
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrFalseEmptyLabelFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            CharPredicates.failIfNullOrEmptyOrFalse("ABC", "", predicate());
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrFalseNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            CharPredicates.failIfNullOrEmptyOrFalse("ABC", "TEXT", null);
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrFalseTestFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            this.failIfNullOrEmptyOrFalse("98");
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrFalseTestFails2() {
        assertThrows(InvalidCharacterException.class, () -> {
            this.failIfNullOrEmptyOrFalse("A1");
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrFalsePassInitialOnly() {
        this.failIfNullOrEmptyOrFalse("A");
    }

    @Test
    public void testFailIfNullOrEmptyOrFalsePasses2() {
        this.failIfNullOrEmptyOrFalse("AB");
    }

    @Test
    public void testFailIfNullOrEmptyOrFalsePasses3() {
        this.failIfNullOrEmptyOrFalse("ABCDEFGH");
    }

    private void failIfNullOrEmptyOrFalse(final String text) {
        CharPredicates.failIfNullOrEmptyOrFalse(text, "TEXT", CharPredicates.letter());
    }

    private CharPredicate predicate() {
        return CharPredicates.letter();
    }


    // fail ..............................................................

    private final static CharPredicate PREDICATE = CharPredicates.fake();

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseNullCharSequenceFails() {
        assertThrows(NullPointerException.class, () -> {
            CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(null, "TEXT", PREDICATE, PREDICATE);
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseEmptyCharSequenceFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("", "TEXT", PREDICATE, PREDICATE);
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseNullLabelFails() {
        assertThrows(NullPointerException.class, () -> {
            CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("ABC", null, PREDICATE, PREDICATE);
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseEmptyLabelFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("ABC", "", PREDICATE, PREDICATE);
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseNullInitialFails() {
        assertThrows(NullPointerException.class, () -> {
            CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("ABC", "TEXT", null, PREDICATE);
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseNullPartFails() {
        assertThrows(NullPointerException.class, () -> {
            CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("ABC", "TEXT", PREDICATE, null);
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseInitialTestFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            this.failIfNullOrEmptyOrInitialAndPartFalseAndCheck("98");
        });
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalsePartTestFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            this.failIfNullOrEmptyOrInitialAndPartFalseAndCheck("AB");
        });
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
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(text,
                "TEXT",
                CharPredicates.letter(),
                CharPredicates.digit());
    }

    @Override
    protected Class<CharPredicates> type() {
        return CharPredicates.class;
    }

    @Override
    protected boolean canHavePublicTypes(Method method) {
        return false;
    }
}
