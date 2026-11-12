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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class CharPredicateAnyTest extends CharPredicateTestCase<CharPredicateAny>
    implements HashCodeEqualsDefinedTesting2<CharPredicateAny> {

    // constants

    private final static String CHARS = "CCAB123";

    // tests

    @Test
    public void testWithNullCharsFails() {
        assertThrows(NullPointerException.class, () -> CharPredicateAny.with(null));
    }

    @Test
    public void testWithEmptyString() {
        final CharPredicate predicate = CharPredicateAny.with("");
        this.checkNotEquals(CharPredicateAny.class,
            predicate.getClass(),
            "predicate must not be a CharPredicateAny");

        for (int i = 0; i < Character.MAX_VALUE; i++) {
            this.testFalse(predicate, (char) i);
        }
    }

    @Test
    public void testWithOneCharacterString() {
        final CharPredicate predicate = CharPredicateAny.with("A");
        this.checkNotEquals(CharPredicateAny.class,
            predicate.getClass(),
            "predicate must not be a CharPredicateAny");

        for (int i = 0; i < Character.MAX_VALUE; i++) {
            if (i != 'A') {
                this.testFalse(predicate, (char) i);
            } else {
                this.testTrue(predicate, (char) i);
            }
        }
    }

    @Test
    public void testWithLongString() {
        final CharPredicateAny predicate = this.createCharPredicate();
        assertArrayEquals("123ABC".toCharArray(), predicate.array, "array");
    }

    @Test
    public void testWithRemovesDuplicatesString() {
        final CharPredicateAny predicate = Cast.to(CharPredicateAny.with("ZAAAB"));
        assertArrayEquals("ABZ".toCharArray(), predicate.array, "array");
    }

    // Predicate

    @Test
    public void testKnown() {
        this.testTrue('1');
    }

    @Test
    public void testBefore() {
        this.testFalse(' ');
    }

    @Test
    public void testAfter() {
        this.testFalse('Z');
    }

    @Test
    public void testAfter2() {
        this.testFalse('9');
    }

    @Test
    public void testEqualsDifferentAny() {
        this.checkNotEquals(CharPredicateAny.with("different"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCharPredicate(), "\"123ABC\"");
    }

    @Override
    public CharPredicateAny createCharPredicate() {
        return Cast.to(CharPredicateAny.with(CHARS));
    }

    @Override
    public Class<CharPredicateAny> type() {
        return CharPredicateAny.class;
    }

    @Override
    public CharPredicateAny createObject() {
        return this.createCharPredicate();
    }
}
