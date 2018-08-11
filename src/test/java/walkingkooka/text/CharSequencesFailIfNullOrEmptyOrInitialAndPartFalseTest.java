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

package walkingkooka.text;

import org.junit.Test;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.test.StaticMethodTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

final public class CharSequencesFailIfNullOrEmptyOrInitialAndPartFalseTest extends StaticMethodTestCase {

    private final static CharPredicate PREDICATE = CharPredicates.fake();

    @Test(expected = NullPointerException.class)
    public void testNullCharSequenceFails() {
        CharSequences.failIfNullOrEmptyOrInitialAndPartFalse(null, "TEXT", PREDICATE, PREDICATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharSequenceFails() {
        CharSequences.failIfNullOrEmptyOrInitialAndPartFalse("", "TEXT", PREDICATE, PREDICATE);
    }

    @Test(expected = NullPointerException.class)
    public void testNullLabelFails() {
        CharSequences.failIfNullOrEmptyOrInitialAndPartFalse("ABC", null, PREDICATE, PREDICATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLabelFails() {
        CharSequences.failIfNullOrEmptyOrInitialAndPartFalse("ABC", "", PREDICATE, PREDICATE);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInitialFails() {
        CharSequences.failIfNullOrEmptyOrInitialAndPartFalse("ABC", "TEXT", null, PREDICATE);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPartFails() {
        CharSequences.failIfNullOrEmptyOrInitialAndPartFalse("ABC", "TEXT", PREDICATE, null);
    }

    @Test
    public void testInitialTestFails() {
        this.checkAndFail("98", "TEXT contains invalid initial char '9' expected letter =\"98\"");
    }

    @Test
    public void testPartTestFails() {
        this.checkAndFail("AB", "TEXT contains invalid char 'B' at position 1 expected digit =\"AB\"");
    }

    @Test
    public void testPassInitialOnly() {
        this.check("A");
    }

    @Test
    public void testPasses2() {
        this.check("A1");
    }

    @Test
    public void testPasses3() {
        this.check("A12345678");
    }

    private void check(final String text) {
        CharSequences.failIfNullOrEmptyOrInitialAndPartFalse(text, "TEXT", CharPredicates.letter(), CharPredicates.digit());
    }

    private void checkAndFail(final String text, final String message) {
        try {
            check(text);
            fail("Expected failure on " + CharSequences.quote(text));
        } catch (final IllegalArgumentException expected) {
            assertEquals("exception message different for " + CharSequences.quote(text), message, expected.getMessage());
        }
    }
}
