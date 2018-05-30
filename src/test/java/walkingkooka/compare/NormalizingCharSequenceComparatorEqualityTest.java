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

package walkingkooka.compare;

import org.junit.Test;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

final public class NormalizingCharSequenceComparatorEqualityTest
        extends HashCodeEqualsDefinedEqualityTestCase<NormalizingCharSequenceComparator<String>> {

    @Test
    public void testDifferentCharacterPredicate() {
        this.checkNotEquals(NormalizingCharSequenceComparator.with(CharPredicates.never()));
    }

    @Override
    protected NormalizingCharSequenceComparator<String> createObject() {
        return NormalizingCharSequenceComparator.with(CharPredicates.always());
    }
}
