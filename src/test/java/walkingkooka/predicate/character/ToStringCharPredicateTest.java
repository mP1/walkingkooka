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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class ToStringCharPredicateTest extends CharPredicateTestCase<ToStringCharPredicate> {

    // constants

    private final static char MATCHED = 1;

    private final static char DIFFERENT = 2;

    private final static CharPredicate PREDICATE
            = CharPredicates.is(ToStringCharPredicateTest.MATCHED);

    private final static String TOSTRING = "*toString*";

    // tests

    @Test
    public void testWrapNullPredicateFails() {
        this.wrapFails(null, ToStringCharPredicateTest.TOSTRING);
    }

    @Test
    public void testWrapNullToStringFails() {
        this.wrapFails(ToStringCharPredicateTest.PREDICATE, null);
    }

    @Test
    public void testWrapEmptyPredicateFails() {
        this.wrapFails(ToStringCharPredicateTest.PREDICATE, "");
    }

    @Test
    public void testWrapWhitespacePredicateFails() {
        this.wrapFails(ToStringCharPredicateTest.PREDICATE, "   ");
    }

    private void wrapFails(final CharPredicate predicate, final String toString) {
        try {
            ToStringCharPredicate.wrap(predicate, toString);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testWrapPredicate() {
        final ToStringCharPredicate predicate
                = Cast.to(ToStringCharPredicate.wrap(ToStringCharPredicateTest.PREDICATE,
                ToStringCharPredicateTest.TOSTRING));
        assertSame("predicate", ToStringCharPredicateTest.PREDICATE, predicate.predicate);
        assertSame("toString", ToStringCharPredicateTest.TOSTRING, predicate.toString);
    }

    @Test
    public void testWrapAnotherToStringCharacterPredicate() {
        final ToStringCharPredicate predicate
                = Cast.to(ToStringCharPredicate.wrap(ToStringCharPredicate.wrap(ToStringCharPredicateTest.PREDICATE,
                "different"), ToStringCharPredicateTest.TOSTRING));
        assertSame("predicate", ToStringCharPredicateTest.PREDICATE, predicate.predicate);
        assertSame("toString", ToStringCharPredicateTest.TOSTRING, predicate.toString);
    }

    @Test
    public void testDoesntWrapSameToString() {
        final CharPredicate predicate = ToStringCharPredicate.wrap(PREDICATE, PREDICATE.toString());
        assertSame(predicate, PREDICATE);
    }

    @Test
    public void testMatched() {
        this.testTrue(ToStringCharPredicateTest.MATCHED);
    }

    @Test
    public void testNotMatched() {
        this.testFalse(ToStringCharPredicateTest.DIFFERENT);
    }

    @Test
    public void testToString() {
        assertEquals(ToStringCharPredicateTest.TOSTRING,
                this.createCharacterPredicate().toString());
    }

    @Override
    protected ToStringCharPredicate createCharacterPredicate() {
        return Cast.to(ToStringCharPredicate.wrap(ToStringCharPredicateTest.PREDICATE,
                ToStringCharPredicateTest.TOSTRING));
    }

    @Override
    protected Class<ToStringCharPredicate> type() {
        return Cast.to(ToStringCharPredicate.class);
    }
}
