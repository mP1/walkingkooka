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
import walkingkooka.test.PublicStaticHelperTestCase;
import walkingkooka.text.CharSequences;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

    @Test
    public void testFailIfNullOrFalseTestFails() {
        this.failIfNullOrFalseAndFail("98", "TEXT contains invalid char '9' at position 0 expected letter =\"98\"");
    }

    @Test
    public void testFailIfNullOrFalseTestFails2() {
        this.failIfNullOrFalseAndFail("A1", "TEXT contains invalid char '1' at position 1 expected letter =\"A1\"");
    }

    @Test
    public void testFailIfNullOrFalseEmpty() {
        this.failIfNullOrFalseAndCheck("");
    }

    @Test
    public void testFailIfNullOrFalsePass() {
        this.failIfNullOrFalseAndCheck("A");
    }

    @Test
    public void testFailIfNullOrFalsePass2() {
        this.failIfNullOrFalseAndCheck("AB");
    }

    @Test
    public void testFailIfNullOrFalsePass3() {
        this.failIfNullOrFalseAndCheck("ABCDEFGH");
    }

    private void failIfNullOrFalseAndCheck(final String text) {
        CharPredicates.failIfNullOrFalse(text, "TEXT", CharPredicates.letter());
    }

    private void failIfNullOrFalseAndFail(final String text, final String message) {
        try {
            CharPredicates.failIfNullOrFalse(text, "TEXT", predicate());
            fail("Expected failure on " + CharSequences.quote(text));
        } catch (final IllegalArgumentException expected) {
            assertEquals("exception message different for " + CharSequences.quote(text), message, expected.getMessage());
        }
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

    @Test
    public void testFailIfNullOrEmptyOrFalseTestFails() {
        this.failIfNullOrEmptyOrFalseAndFail("98", "TEXT contains invalid char '9' at position 0 expected letter =\"98\"");
    }

    @Test
    public void testFailIfNullOrEmptyOrFalseTestFails2() {
        this.failIfNullOrEmptyOrFalseAndFail("A1", "TEXT contains invalid char '1' at position 1 expected letter =\"A1\"");
    }

    @Test
    public void testFailIfNullOrEmptyOrFalsePassInitialOnly() {
        this.failIfNullOrEmptyOrFalseAndCheck("A");
    }

    @Test
    public void testFailIfNullOrEmptyOrFalsePasses2() {
        this.failIfNullOrEmptyOrFalseAndCheck("AB");
    }

    @Test
    public void testFailIfNullOrEmptyOrFalsePasses3() {
        this.failIfNullOrEmptyOrFalseAndCheck("ABCDEFGH");
    }

    private void failIfNullOrEmptyOrFalseAndCheck(final String text) {
        CharPredicates.failIfNullOrEmptyOrFalse(text, "TEXT", CharPredicates.letter());
    }

    private void failIfNullOrEmptyOrFalseAndFail(final String text, final String message) {
        try {
            CharPredicates.failIfNullOrEmptyOrFalse(text, "TEXT", predicate());
            fail("Expected failure on " + CharSequences.quote(text));
        } catch (final IllegalArgumentException expected) {
            assertEquals("exception message different for " + CharSequences.quote(text), message, expected.getMessage());
        }
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

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalseInitialTestFails() {
        this.failIfNullOrEmptyOrInitialAndPartFalseAndFail("98",
                "TEXT contains invalid char '9' expected letter =\"98\"");
    }

    @Test
    public void testFailIfNullOrEmptyOrInitialAndPartFalsePartTestFails() {
        this.failIfNullOrEmptyOrInitialAndPartFalseAndFail("AB",
                "TEXT contains invalid char 'B' at position 1 expected digit =\"AB\"");
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

    private void failIfNullOrEmptyOrInitialAndPartFalseAndFail(final String text, final String message) {
        try {
            failIfNullOrEmptyOrInitialAndPartFalseAndCheck(text);
            fail("Expected failure on " + CharSequences.quote(text));
        } catch (final IllegalArgumentException expected) {
            assertEquals("exception message different for " + CharSequences.quote(text), message, expected.getMessage());
        }
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
