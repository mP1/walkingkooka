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
import walkingkooka.predicate.PredicateTestCase;

import static org.junit.Assert.assertEquals;

final public class CharPredicateCharSequencePredicateTest
        extends PredicateTestCase<CharPredicateCharSequencePredicate<String>, String> {

    // constants

    private final static CharPredicate PREDICATE = CharPredicates.letter();

    // tests

    @Test
    public void testAdaptNullCharacterPredicateFails() {
        try {
            CharPredicateCharSequencePredicate.adapt(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testAllValid() {
        this.testTrue("AB");
    }

    @Test
    public void testWrongCharacter() {
        this.testFalse("A1");
    }

    @Test
    public void testToString() {
        assertEquals(CharPredicateCharSequencePredicateTest.PREDICATE.toString(),
                this.createPredicate().toString());
    }

    @Override
    protected CharPredicateCharSequencePredicate<String> createPredicate() {
        return CharPredicateCharSequencePredicate.adapt(CharPredicateCharSequencePredicateTest.PREDICATE);
    }

    @Override
    protected Class<CharPredicateCharSequencePredicate<String>> type() {
        return Cast.to(CharPredicateCharSequencePredicate.class);
    }
}
