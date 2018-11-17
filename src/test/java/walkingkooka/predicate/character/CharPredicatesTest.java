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
        this.failIfNullOrEmptyOrInitialAndPartFalseAndCheck("A");
    }

    @Test
    public void testFailIfNullOrEmptyOrFalsePasses2() {
        this.failIfNullOrEmptyOrInitialAndPartFalseAndCheck("AB");
    }

    @Test
    public void testFailIfNullOrEmptyOrFalsePasses3() {
        this.failIfNullOrEmptyOrInitialAndPartFalseAndCheck("ABCDEFGH");
    }

    private void failIfNullOrEmptyOrInitialAndPartFalseAndCheck(final String text) {
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

    @Override
    protected Class<CharPredicates> type() {
        return CharPredicates.class;
    }

    @Override
    protected boolean canHavePublicTypes(Method method) {
        return false;
    }
}
