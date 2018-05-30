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

package walkingkooka.text;

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

abstract public class CaseSensitivityCharSequencePredicateEqualityTestCase<M extends CaseSensitivityCharSequencePredicateTemplate<String>>
        extends HashCodeEqualsDefinedEqualityTestCase<M> {

    // constants

    private final static String STRING = "strING";

    // tests

    @Test
    public void testDifferentCharSequence() {
        this.checkNotEquals(CaseSensitivityCharSequencePredicate.with("different",
                CaseSensitivity.SENSITIVE));
    }

    @Test
    public void testDifferentCharSensitivity() {
        this.checkNotEquals(CaseSensitivityCharSequencePredicate.with(
                CaseSensitivityCharSequencePredicateEqualityTestCase.STRING,
                CaseSensitivity.INSENSITIVE));
    }

    @Override final protected M createObject() {
        return this.createObject(CaseSensitivityCharSequencePredicateEqualityTestCase.STRING,
                CaseSensitivity.SENSITIVE);
    }

    abstract M createObject(final String chars, final CaseSensitivity sensitivity);
}
