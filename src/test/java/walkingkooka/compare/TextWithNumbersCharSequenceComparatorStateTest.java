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
import walkingkooka.test.PackagePrivateClassTestCase;
import walkingkooka.text.CaseSensitivity;

import static org.junit.Assert.assertEquals;

final public class TextWithNumbersCharSequenceComparatorStateTest
        extends PackagePrivateClassTestCase<TextWithNumbersCharSequenceComparatorState> {

    @Test
    public void testToString() {
        assertEquals("\"[a]bc\" \"[d]ef\"",
                new TextWithNumbersCharSequenceComparatorState("abc",
                        "def",
                        TextWithNumbersCharSequenceComparator.with(CaseSensitivity.SENSITIVE, CharPredicates.is('.'))).toString());
    }

    @Override
    protected Class<TextWithNumbersCharSequenceComparatorState> type() {
        return TextWithNumbersCharSequenceComparatorState.class;
    }
}
