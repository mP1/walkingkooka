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
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class CharPredicateToStringTest extends CharPredicateTestCase<CharPredicateToString>
    implements HashCodeEqualsDefinedTesting2<CharPredicateToString> {

    // constants

    private final static char MATCHED = 1;

    private final static char DIFFERENT = 2;

    private final static CharPredicate PREDICATE
        = CharPredicates.is(MATCHED);

    private final static String TOSTRING = "*toString*";

    // tests

    @Test
    public void testWrapNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> CharPredicateToString.wrap(null, TOSTRING));
    }

    @Test
    public void testWrapNullToStringFails() {
        assertThrows(NullPointerException.class, () -> CharPredicateToString.wrap(PREDICATE, null));
    }

    @Test
    public void testWrapEmptyPredicateFails() {
        assertThrows(IllegalArgumentException.class, () -> CharPredicateToString.wrap(PREDICATE, ""));
    }

    @Test
    public void testWrapWhitespacePredicateFails() {
        assertThrows(IllegalArgumentException.class, () -> CharPredicateToString.wrap(PREDICATE, " \t"));
    }

    @Test
    public void testWrapPredicate() {
        final CharPredicateToString predicate
            = Cast.to(CharPredicateToString.wrap(PREDICATE,
            TOSTRING));
        assertSame(PREDICATE, predicate.predicate, "predicate");
        assertSame(TOSTRING, predicate.toString, "toString");
    }

    @Test
    public void testWrapAnotherToStringCharPredicate() {
        final CharPredicateToString predicate
            = Cast.to(CharPredicateToString.wrap(CharPredicateToString.wrap(PREDICATE,
            "different"), TOSTRING));
        assertSame(PREDICATE, predicate.predicate, "predicate");
        assertSame(TOSTRING, predicate.toString, "toString");
    }

    @Test
    public void testDoesntWrapSameToString() {
        final CharPredicate predicate = CharPredicateToString.wrap(PREDICATE, PREDICATE.toString());
        assertSame(predicate, PREDICATE);
    }

    @Test
    public void testMatched() {
        this.testTrue(MATCHED);
    }

    @Test
    public void testNotMatched() {
        this.testFalse(DIFFERENT);
    }

    @Test
    public void testEqualsDifferentPredicate() {
        this.checkNotEquals(CharPredicateToString.wrap(CharPredicates.fake(), TOSTRING));
    }

    @Test
    public void testEqualsDifferentToString() {
        this.checkNotEquals(CharPredicateToString.wrap(PREDICATE, "different"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCharPredicate(), TOSTRING);
    }

    @Override
    public CharPredicateToString createCharPredicate() {
        return Cast.to(CharPredicateToString.wrap(PREDICATE,
            TOSTRING));
    }

    @Override
    public CharPredicateToString createObject() {
        return this.createCharPredicate();
    }

    @Override
    public Class<CharPredicateToString> type() {
        return Cast.to(CharPredicateToString.class);
    }
}
