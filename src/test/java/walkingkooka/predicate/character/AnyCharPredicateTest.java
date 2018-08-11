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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

final public class AnyCharPredicateTest extends CharPredicateTestCase<AnyCharPredicate> {

    // constants

    private final static String CHARS = "CCAB123";

    // tests

    @Test
    public void testWithNullCharsFails() {
        try {
            AnyCharPredicate.with(null);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testWithEmptyString() {
        final CharPredicate predicate = AnyCharPredicate.with("");
        assertNotEquals("predicate must not be a AnyCharPredicate",
                AnyCharPredicate.class,
                predicate.getClass());

        for (int i = 0; i < Character.MAX_VALUE; i++) {
            this.testFalse(predicate, (char) i);
        }
    }

    @Test
    public void testWithOneCharacterString() {
        final CharPredicate predicate = AnyCharPredicate.with("A");
        assertNotEquals("predicate must not be a AnyCharPredicate",
                AnyCharPredicate.class,
                predicate.getClass());

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
        final AnyCharPredicate predicate = this.createCharacterPredicate();
        assertArrayEquals("array", "123ABC".toCharArray(), predicate.array);
    }

    @Test
    public void testWithRemovesDuplicatesString() {
        final AnyCharPredicate predicate = Cast.to(AnyCharPredicate.with("ZAAAB"));
        assertArrayEquals("array", "ABZ".toCharArray(), predicate.array);
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
    public void testToString() {
        assertEquals("\"123ABC\"", this.createCharacterPredicate().toString());
    }

    @Override
    protected AnyCharPredicate createCharacterPredicate() {
        return this.createCharacterPredicate(AnyCharPredicateTest.CHARS);
    }

    private AnyCharPredicate createCharacterPredicate(final String chars) {
        return Cast.to(AnyCharPredicate.with(chars));
    }

    @Override
    protected Class<AnyCharPredicate> type() {
        return AnyCharPredicate.class;
    }
}
