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

import walkingkooka.Cast;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.test.SerializationTestCase;
import walkingkooka.text.CaseSensitivity;

final public class TextWithNumbersCharSequenceComparatorSerializationTest
        extends SerializationTestCase<TextWithNumbersCharSequenceComparator<String>> {

    @Override
    protected Class<TextWithNumbersCharSequenceComparator<String>> type() {
        return Cast.to(TextWithNumbersCharSequenceComparator.class);
    }

    @Override
    protected TextWithNumbersCharSequenceComparator<String> create() {
        return TextWithNumbersCharSequenceComparator.with(CaseSensitivity.SENSITIVE, CharPredicates.is('.'));
    }

    @Override
    protected boolean isSingleton() {
        return false;
    }
}
