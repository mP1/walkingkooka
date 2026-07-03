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

import walkingkooka.text.CaseSensitivity;

import java.util.Comparator;
import java.util.List;

/**
 * A {@link Comparator} that may be used to compare {@link CharSequence text}, accepting a custom list which can be
 * used to fix the ordering for those entries. Text not within the custom list will be sorted and appear after.
 * <pre>
 * customList = "Hi", "Med", "Lo"
 * text = "Lo", "Med", "AAA', "bbb"
 *
 * "Med", "Lo", "AAA", "bbb"
 *
 * </pre>
 * Case is important.
 */
final class CustomListComparatorCaseSensitive extends CustomListComparator {

    /**
     * Factory
     */
    static CustomListComparatorCaseSensitive with(final List<CharSequence> customList){
        return new CustomListComparatorCaseSensitive(customList);
    }

    private CustomListComparatorCaseSensitive(final List<CharSequence> customList) {
        super(customList);
    }

    @Override
    CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }
}
