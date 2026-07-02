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

package walkingkooka.compare;

import org.junit.jupiter.api.Test;

public final class TextWithNumbersComparatorCaseSensitiveTest extends TextWithNumbersComparatorTestCase<TextWithNumbersComparatorCaseSensitive> {

    @Test
    public void testCompareSameCase() {
        this.compareAndCheckEquals(
            "ABC",
            "ABC"
        );
    }

    @Test
    public void testCompareDifferentCase() {
        this.compareAndCheckLess(
            "ABC",
            "abc"
        );
    }

    @Override
    public TextWithNumbersComparatorCaseSensitive createComparator() {
        return TextWithNumbersComparatorCaseSensitive.INSTANCE;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            TextWithNumbersComparatorCaseSensitive.INSTANCE,
            "TextWithNumbersComparator(CaseSensitive)"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextWithNumbersComparatorCaseSensitive> type() {
        return TextWithNumbersComparatorCaseSensitive.class;
    }
}
