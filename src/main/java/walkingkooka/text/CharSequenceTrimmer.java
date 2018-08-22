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

final class CharSequenceTrimmer implements PackagePrivateStaticHelper {

    /**
     * Trims whitespace from the left or beginning of the given {@link CharSequence}.
     */
    static CharSequence left(final CharSequence chars) {
        Objects.requireNonNull(chars, "chars");

        return chars.subSequence(CharSequenceTrimmer.findStart(chars), chars.length());
    }

    /**
     * Trims whitespace from the right or end of the given {@link CharSequence}.
     */
    static CharSequence right(final CharSequence chars) {
        Objects.requireNonNull(chars, "chars");

        return chars.subSequence(0, CharSequenceTrimmer.findEnd(chars, 0));
    }

    /**
     * Trims whitespace from the left and end of the given {@link CharSequence}.
     */
    static CharSequence leftAndRight(final CharSequence chars) {
        Objects.requireNonNull(chars, "chars");

        final int start = CharSequenceTrimmer.findStart(chars);
        return chars.subSequence(start, CharSequenceTrimmer.findEnd(chars, start));
    }

    /**
     * Finds the first non whitespace character in the {@link CharSequence}
     */
    static private int findStart(final CharSequence chars) {
        final int length = chars.length();
        int i = 0;

        while (i < length) {
            final char c = chars.charAt(i);
            if (CharSequenceTrimmer.isSpace(c)) {
                i++;
                continue;
            }

            break;
        }

        return i;
    }

    /**
     * Finds the last non whitespace character in the {@link CharSequence}
     */
    static private int findEnd(final CharSequence chars, final int stop) {
        int i = chars.length();

        while (i > stop) {
            i--;

            final char c = chars.charAt(i);
            if (CharSequenceTrimmer.isSpace(c)) {
                continue;
            }
            i++;
            break;
        }

        return i;
    }

    @SuppressWarnings("deprecation")
    private static boolean isSpace(final char c) {
        return Character.isSpace(c);
    }

    /**
     * Stop creation
     */
    private CharSequenceTrimmer() {
        throw new UnsupportedOperationException();
    }
}
