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

import walkingkooka.collect.list.Lists;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CaseSensitivity;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
 */
abstract class CustomListComparator implements Comparator<CharSequence> {

    CustomListComparator(final List<CharSequence> customList) {
        super();

        this.customList = Lists.immutable(
            Objects.requireNonNull(customList, "customList")
        );
    }

    @Override
    public final int compare(final CharSequence left,
                             final CharSequence right) {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");

        int result;

        final int leftCustomListIndex = this.customListItem(left);
        final int rightCustomListIndex = this.customListItem(right);

        // one or maybe both are in custom list
        if (leftCustomListIndex >= 0 || rightCustomListIndex >= 0) {

            // both are in custom list compare indicies
            if (leftCustomListIndex >= 0 && rightCustomListIndex >= 0) {
                result = leftCustomListIndex - rightCustomListIndex;
            } else {
                result = rightCustomListIndex - leftCustomListIndex;
            }
        } else {
            // neither appears in custom list, compare both CharSequence
            result = this.caseSensitivity()
                .comparator()
                .compare(
                    left,
                    right
                );
        }

        return result;
    }

    private int customListItem(final CharSequence text) {
        int index = -1;

        final CaseSensitivity caseSensitivity = this.caseSensitivity();

        int i = 0;
        for (final CharSequence element : this.customList) {
            if (caseSensitivity.equals(element, text)) {
                index = i;
                break;
            }

            i++;
        }

        return index;
    }

    private final List<CharSequence> customList;

    abstract CaseSensitivity caseSensitivity();

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(
            this.caseSensitivity(),
            this.customList.hashCode()
        );
    }

    @Override
    public final boolean equals(final Object other) {
        return other == this || other instanceof CustomListComparator && this.equals0((CustomListComparator) other);
    }

    private boolean equals0(final CustomListComparator other) {
        return this.caseSensitivity() == other.caseSensitivity() &&
            this.customList.equals(other.customList);
    }

    @Override
    public final String toString() {
        return "CustomList " +
            this.customList +
            "(Case" +
            CaseKind.TITLE.change(
                this.caseSensitivity()
                    .name(),
                CaseKind.PASCAL
            )
            + ")";
    }
}
