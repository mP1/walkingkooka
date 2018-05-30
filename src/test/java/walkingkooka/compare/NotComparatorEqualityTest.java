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
import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

final public class NotComparatorEqualityTest
        extends HashCodeEqualsDefinedEqualityTestCase<NotComparator<String>> {

    @Test
    public void testDifferentPredicate() {
        this.checkNotEquals(NotComparator.wrap(String.CASE_INSENSITIVE_ORDER));
    }

    @Override
    protected NotComparator<String> createObject() {
        return Cast.to(NotComparator.wrap(Comparators.<String>naturalOrdering()));
    }
}
