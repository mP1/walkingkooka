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

package walkingkooka.predicate;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

import static org.junit.Assert.assertEquals;

final public class CharPredicateCharSequencePredicateTest
        extends PredicateTestCase<CharPredicateCharSequencePredicate, CharSequence> {

    // constants

    private final static String CHARS = "abcdef";

    // tests

    @Test(expected = NullPointerException.class)
    public void testWithNullCharPredicateFails() {
        CharPredicateCharSequencePredicate.with(null);
    }

    @Test
    public void testEmpty() {
        this.testFalse("");
    }

    @Test
    public void testMatchesAll() {
        this.testTrue("fed");
    }

    @Test
    public void testMatchesNone() {
        this.testFalse("xyz");
    }

    @Test
    public void testMatchesSome() {
        this.testFalse("abcxyz");
    }

    @Test
    public void testToString() {
        assertEquals(CharSequences.quote(CHARS).toString(),
                this.createPredicate().toString());
    }

    @Override
    protected CharPredicateCharSequencePredicate createPredicate() {
        return Cast.to(CharPredicateCharSequencePredicate.with(CharPredicates.any(CHARS)));
    }

    @Override
    protected Class<CharPredicateCharSequencePredicate> type() {
        return CharPredicateCharSequencePredicate.class;
    }
}
