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

import walkingkooka.type.PackagePrivateStaticHelper;

import java.util.Objects;

final class CharSequenceSubSequencer implements PackagePrivateStaticHelper {

    /**
     * Adds support for negative to indexes without needing to fetch the length and subSequence. If
     * the to index is negative and before the from index a {@link ArrayIndexOutOfBoundsException}
     * is thrown.
     */
    static CharSequence make(final CharSequence chars, final int from, final int to) {
        Objects.requireNonNull(chars, "chars");

        CharSequence subSequence;
        do {
            if (0 == to) {
                subSequence = chars.subSequence(from, chars.length());
                break;
            }
            if (to > 0) {
                subSequence = chars.subSequence(from, to);
                break;
            }
            subSequence = CharSequenceSubSequencer.subSequenceWithNegativeToIndex(chars, from, to);
        } while (false);

        return subSequence;
    }

    /**
     * Performs a {@link CharSequence#subSequence(int, int)} with a negative to index.
     */
    private static CharSequence subSequenceWithNegativeToIndex(final CharSequence sequence,
                                                               final int from, final int to) {
        final int length = sequence.length();
        final int positiveToIndex = length + to;
        if (positiveToIndex < from) {
            throw new StringIndexOutOfBoundsException(CharSequenceSubSequencer.toIndexBeforeFromIndex(
                    from,
                    to,
                    length));
        }
        return sequence.subSequence(from, positiveToIndex);
    }

    /**
     * Returns a message that the to index points to a character before the from index.
     */
    static String toIndexBeforeFromIndex(final int from, final int to, final int length) {
        return "To index (which is negative) is before from: " + to + " < " + -(length - from);
    }

    /**
     * Stop creation
     */
    private CharSequenceSubSequencer() {
        throw new UnsupportedOperationException();
    }
}
