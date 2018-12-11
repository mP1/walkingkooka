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

import org.junit.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.test.PublicStaticHelperTestCase;

import java.lang.reflect.Method;

public final class CharPredicatesTest extends PublicStaticHelperTestCase<CharPredicates> {

    // failIfNullOrFalse .............................................................

    @Test(expected = NullPointerException.class)
    public void testFailIfNullOrFalseNullCharSequenceFails() {
        CharPredicates.failIfNullOrFalse(null, "TEXT", predicate());
    }

    @Test
    public void testFailIfNullOrFalseEmptyCharSequence() {
        CharPredicates.failIfNullOrFalse("", "TEXT", predicate());
    }

    @Test(expected = NullPointerException.class)
    public void testFailIfNullOrFalseNullLabelFails() {
        CharPredicates.failIfNullOrFalse("ABC", null, predicate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailIfNullOrFalseEmptyLabelFails() {
        CharPredicates.failIfNullOrFalse("ABC", "", predicate());
    }

    @Test(expected = InvalidCharacterException.class)
    public void testFailIfNullOrFalseTestFails() {
        this.failIfNullOrFalse("98");
    }

    @Test(expected = InvalidCharacterException.class)
    public void testFailIfNullOrFalseTestFails2() {
        this.failIfNullOrFalse("A1");
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

    @Test(expected = NullPointerException.class)
    public void testFailIfNullOrEmptyOrFalseNullCharSequenceFails() {
        CharPredicates.failIfNullOrEmptyOrFalse(null, "TEXT", predicate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailIfNullOrEmptyOrFalseEmptyCharSequenceFails() {
        CharPredicates.failIfNullOrEmptyOrFalse("", "TEXT", predicate());
    }

    @Test(expected = NullPointerException.class)
    public void testFailIfNullOrEmptyOrFalseNullLabelFails() {
        CharPredicates.failIfNullOrEmptyOrFalse("ABC", null, predicate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailIfNullOrEmptyOrFalseEmptyLabelFails() {
        CharPredicates.failIfNullOrEmptyOrFalse("ABC", "", predicate());
    }

    @Test(expected = NullPointerException.class)
    public void testFailIfNullOrEmptyOrFalseNullPredicateFails() {
        CharPredicates.failIfNullOrEmptyOrFalse("ABC", "TEXT", null);
    }

    @Test(expected = InvalidCharacterException.class)
    public void testFailIfNullOrEmptyOrFalseTestFails() {
        this.failIfNullOrEmptyOrFalse("98");
    }

    @Test(expected = InvalidCharacterException.class)
    public void testFailIfNullOrEmptyOrFalseTestFails2() {
        this.failIfNullOrEmptyOrFalse("A1");
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

    // 

    // fail ..............................................................

    private final static CharPredicate PREDICATE = CharPredicates.fake();

    @Test(expected = NullPointerException.class)
    public void testFailIfNullOrEmptyOrInitialAndPartFalseNullCharSequenceFails() {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(null, "TEXT", PREDICATE, PREDICATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailIfNullOrEmptyOrInitialAndPartFalseEmptyCharSequenceFails() {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("", "TEXT", PREDICATE, PREDICATE);
    }

    @Test(expected = NullPointerException.class)
    public void testFailIfNullOrEmptyOrInitialAndPartFalseNullLabelFails() {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("ABC", null, PREDICATE, PREDICATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailIfNullOrEmptyOrInitialAndPartFalseEmptyLabelFails() {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("ABC", "", PREDICATE, PREDICATE);
    }

    @Test(expected = NullPointerException.class)
    public void testFailIfNullOrEmptyOrInitialAndPartFalseNullInitialFails() {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("ABC", "TEXT", null, PREDICATE);
    }

    @Test(expected = NullPointerException.class)
    public void testFailIfNullOrEmptyOrInitialAndPartFalseNullPartFails() {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse("ABC", "TEXT", PREDICATE, null);
    }

    @Test(expected = InvalidCharacterException.class)
    public void testFailIfNullOrEmptyOrInitialAndPartFalseInitialTestFails() {
        this.failIfNullOrEmptyOrInitialAndPartFalseAndCheck("98");
    }

    @Test(expected = InvalidCharacterException.class)
    public void testFailIfNullOrEmptyOrInitialAndPartFalsePartTestFails() {
        this.failIfNullOrEmptyOrInitialAndPartFalseAndCheck("AB");
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
