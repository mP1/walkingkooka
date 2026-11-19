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

package walkingkooka.collect.list;

import walkingkooka.EmptyTextException;
import walkingkooka.text.HasTextWithSeparator;

/**
 * An {@link ImmutableList} of {@link String string values} with a delimiter such as CSV
 */
public interface DelimiterStringImmutableList extends ImmutableList<String>,
    CanRemoveTrailingNullOrEmptyStrings,
    HasTextWithSeparator {

    /**
     * Helper that gets the {@link String} and verifies it is a single character and returns {@link Character}.
     * Any {@link String strings} that are empty or have more than one character will throw a {@link IllegalArgumentException}.
     */
    default Character getCharacter(final int index) {
        final String string = this.get(index);

        final Character character;

        if (null != string) {
            final int length = string.length();
            switch (length) {
                case 0:
                    throw new EmptyTextException(
                        String.valueOf(index)
                    );
                case 1:
                    character = string.charAt(0);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid character expected 1 character but got " + length);
            }
        } else {
            character = null;
        }
        return character;
    }
}
