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

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.type.PublicStaticHelper;

import java.io.Reader;
import java.util.Objects;

final public class CharSequences implements PublicStaticHelper {
    /**
     * {@see BigEndianHexByteReader#read(CharSequence)}
     */
    public static byte[] bigEndianHexDigits(final CharSequence hexDigits) {
        return BigEndianHexByteReader.read(hexDigits);
    }

    /**
     * Capitalises the first character of the given {@link CharSequence}
     */
    public static CharSequence capitalize(final CharSequence chars) {
        Objects.requireNonNull(chars, "chars");

        CharSequence result = chars;
        final int length = chars.length();
        if (length > 0) {
            result = new StringBuilder().append(Character.toUpperCase(chars.charAt(0)))
                    .append(chars, 1, length)
                    .toString();
        }
        return result;
    }

    /**
     * {@see EmptyCharSequence}
     */
    public static CharSequence empty() {
        return EmptyCharSequence.INSTANCE;
    }

    /**
     * Tests if the first string ends with the second ignoring case. This is equivalent to
     * CharSequence.endsWith() but ignores case.
     */
    public static boolean endsWith(final CharSequence chars, final String endsWith) {
        Objects.requireNonNull(chars, "chars");
        failIfNullOrEmpty(endsWith, "endsWith");

        boolean result = false;

        final int stringLength = chars.length();
        final int endsWithLength = endsWith.length();
        if (endsWithLength <= stringLength) {
            result = true;

            for (int i = 0; i < endsWithLength; i++) {
                if (chars.charAt(stringLength - 1 - i) != endsWith.charAt(endsWithLength - 1 - i)) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Accepts a character and returns a {@link CharSequence} that has been escaped and surrounded
     * by single quotes.
     */
    public static CharSequence escape(final char c) {
        return CharSequences.escape(String.valueOf(c));
    }

    /**
     * {@see CharSequenceEscaping#escape(String)}
     */
    public static CharSequence escape(final CharSequence chars) {
        return CharSequenceEscaping.escape(chars);
    }

    /**
     * Tests if two {@link CharSequences} are equal
     */
    public static boolean equals(final CharSequence chars, final CharSequence otherChars) {
        Objects.requireNonNull(chars, "chars");
        Objects.requireNonNull(otherChars, "otherChars");

        boolean equals = false;
        final int length = chars.length();
        final int otherLength = otherChars.length();

        // must first be the same length
        if (length == otherLength) {
            equals = true;

            // give up when a mis-match is encountered
            for (int i = 0; i < length; i++) {
                if (chars.charAt(i) != otherChars.charAt(i)) {
                    equals = false;
                    break;
                }
            }
        }

        return equals;
    }

    /**
     * Fails if the chars are null or empty.
     */
    public static void failIfNullOrEmpty(final CharSequence chars, final String label) {
        failIfNullOrEmpty0(label, "label");
        failIfNullOrEmpty0(chars, label);
    }

    private static void failIfNullOrEmpty0(final CharSequence chars, final String label) {
        Objects.requireNonNull(label, label);
        if (chars.length() == 0) {
            throw new IllegalArgumentException(label + " is empty");
        }
    }

    /**
     * Fails if the chars are null or empty or any characters fail the initial or part test.
     * It is assumed the {@link CharPredicate} have a meaningful toString as it is included in any exception messages.
     */
    public static void failIfNullOrEmptyOrInitialAndPartFalse(final CharSequence chars, final String label, final CharPredicate initial, final CharPredicate part) {
        failIfNullOrEmpty(chars, label);
        Objects.requireNonNull(initial, "initial");
        Objects.requireNonNull(part, "part");

        final char first = chars.charAt(0);
        if(!initial.test(first)) {
            throw new IllegalArgumentException(label + " contains invalid initial char " + quoteIfChars(first) + " expected " + initial + " =" + CharSequences.quoteAndEscape(chars));
        }

        final int length = chars.length();
        for(int i = 1; i < length; i++) {
            final char c = chars.charAt(i);
            if(!part.test(c)) {
                throw new IllegalArgumentException(label + " contains invalid char " + quoteIfChars(c) + " at position " + i + " expected " + part + " =" + CharSequences.quoteAndEscape(chars));
            }
        }
    }

    /**
     * Attempts to find the {@link CharSequence searchFor} within the {@link CharSequence}
     */
    public static int indexOf(final CharSequence chars, final String indexOf) {
        Objects.requireNonNull(chars, "chars");
        failIfNullOrEmpty(indexOf, "indexOf");

        int index = -1;
        final int charLength = chars.length();
        final int searchForLength = indexOf.length();
        if ((charLength > 0) && (searchForLength > 0) && (searchForLength <= charLength)) {
            final char firstCharOfTest = indexOf.charAt(0);
            final int lastCharSequenceCharacterToCheck = (charLength - searchForLength) + 1;

            for (int i = 0; i < lastCharSequenceCharacterToCheck; i++) {
                if (firstCharOfTest == chars.charAt(i)) {
                    index = i;
                    for (int j = 1; j < searchForLength; j++) {
                        final char c = chars.charAt(i + j);
                        final char otherChar = indexOf.charAt(j);
                        if (c != otherChar) {
                            index = -1;
                            break;
                        }
                    }
                    if (-1 != index) {
                        break;
                    }
                }
            }
        }
        return index;
    }

    /**
     * Helper that returns true if the given {@link CharSequence} is null or empty.
     */
    public static boolean isNullOrEmpty(final CharSequence chars) {
        return (chars == null) || (chars.length() == 0);
    }

    /**
     * {@see LeftPaddedCharSequence}
     */
    public static CharSequence padLeft(final CharSequence sequence, final int length,
                                       final char pad) {
        return LeftPaddedCharSequence.wrap(sequence, length, pad);
    }

    /**
     * {@see RightPaddedCharSequence}
     */
    public static CharSequence padRight(final CharSequence sequence, final int length,
                                        final char pad) {
        return RightPaddedCharSequence.wrap(sequence, length, pad);
    }

    /**
     * {@see QuotedCharSequence}
     */
    public static CharSequence quote(final CharSequence sequence) {
        return QuotesAroundCharSequence.with(sequence);
    }

    /**
     * Convenience method which quotes non null {@link CharSequence chars} of well of escaping
     * newlines, carriage returns and double quotes.
     */
    public static CharSequence quoteAndEscape(final CharSequence chars) {
        return null == chars ? null : CharSequences.quoteAndEscape2(chars);
    }

    /**
     * Helper that combines {@link #escape} and adds double quotes if required and escapes.
     */
    private static CharSequence quoteAndEscape2(final CharSequence chars) {
        final boolean quoted = startsWith(chars, "\"") && endsWith(chars, "\"");

        return new StringBuilder()
                .append('"')
                .append(CharSequences.escape(quoted ? chars.subSequence(1, chars.length() -1) : chars))
                .append('"')
                .toString();
    }

    /**
     * Accepts a character and returns a {@link CharSequence} that has been escaped and surrounded
     * by single quotes.
     */
    public static CharSequence quoteAndEscape(final char c) {
        return "\'" + CharSequences.escape(String.valueOf(c)) + '\'';
    }

    /**
     * Helper which adds quotes to the given {@link CharSequence} if it contains any whitespace. For
     * null or {@link CharSequence sequences} without whitespace the original will be returned.
     */
    public static CharSequence quoteIfNecessary(final CharSequence chars) {
        CharSequence result = chars;

        if (null != chars) {
            if (Whitespace.has(chars)) {
                result = quoteIfChars(chars);
            }
        }

        return result;
    }

    /**
     * Quotes and escapes {@link CharSequence}, {@link char[]} or {@link Character}. The later is
     * surrounded in single quotes and the others in double quotes. It is ok to pass in null.
     */
    public static CharSequence quoteIfChars(final Object object) {
        CharSequence result;

        for (; ; ) {
            if (object instanceof CharSequence) {
                result = CharSequences.quoteAndEscape(String.valueOf(object));
                break;
            }
            if (object instanceof char[]) {
                result = CharSequences.quoteAndEscape(new String((char[]) object));
                break;
            }
            if (object instanceof Character) {
                result = "'" + CharSequences.escape(String.valueOf(object)) + '\'';
                break;
            }
            result = String.valueOf(object);
            break;
        }

        return result;
    }

    /**
     * {@see ReaderConsumingCharSequence}
     */
    public static CharSequence readerConsuming(final Reader reader, final int bufferSize) {
        return ReaderConsumingCharSequence.with(reader, bufferSize);
    }

    /**
     * {@see RepeatingCharSequence}
     */
    public static CharSequence repeating(final char c, final int length) {
        return RepeatingCharSequence.with(c, length);
    }

    /**
     * Creates a new {@link CharSequence} which contains the beginning and end of the given {@link
     * CharSequence} if its too long.
     *
     * <pre>
     * CharSequence shrunk = CharSequence.shrink(&quot;apple banana&quot;, 8);
     * System.out.println(shrunk); // &quot;appl...nana&quot;
     *
     * CharSequence shrunk2 = CharSequence.shrink(&quot;short&quot;, 8);
     * System.out.println(shrunk2); // &quot;short&quot;
     * </pre>
     */
    public static CharSequence shrink(final CharSequence chars, final int desiredLength) {
        Objects.requireNonNull(chars, "chars");

        if (desiredLength < 0 || desiredLength < 6) {
            throw new IllegalArgumentException(
                    "DesiredLength " + desiredLength + " must be between 0 and 6");
        }

        CharSequence shorter = chars;

        final int length = chars.length();
        if (length > desiredLength) {
            final int keep = (desiredLength - 3) / 2;

            shorter = chars.subSequence(0, keep + (((desiredLength & 1) == 1) ? 0 : 1)) + "..."
                    + chars.subSequence(length - keep, chars.length());
        }

        return shorter;
    }

    /**
     * {@see CharSequenceSubSequencer#make(String, int, int)}.
     */
    public static CharSequence subSequence(final CharSequence string, final int from, final int to) {
        return CharSequenceSubSequencer.make(string, from, to);
    }

    /**
     * Tests if the first string starts with the second ignoring case. This is equivalent to
     * CharSequence.startsWith() but ignores case.
     */
    public static boolean startsWith(final CharSequence chars, final String startsWith) {
        Objects.requireNonNull(chars, "chars");
        failIfNullOrEmpty(startsWith, "startsWith");

        boolean result = false;

        final int startsWithLength = startsWith.length();
        if (startsWithLength <= chars.length()) {
            result = true;

            for (int i = 0; i < startsWithLength; i++) {
                if (chars.charAt(i) != startsWith.charAt(i)) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * {@see Trimmer#left(CharSequence)}.
     */
    public static CharSequence trim(final CharSequence sequence) {
        return CharSequenceTrimmer.leftAndRight(sequence);
    }

    /**
     * {@see Trimmer#left(CharSequence)}.
     */
    public static CharSequence trimLeft(final CharSequence sequence) {
        return CharSequenceTrimmer.left(sequence);
    }

    /**
     * {@see Trimmer#right(CharSequence)}.
     */
    public static CharSequence trimRight(final CharSequence sequence) {
        return CharSequenceTrimmer.right(sequence);
    }

    /**
     * {@see CharSequenceEscaping#unescape(CharSequence)}
     */
    public static CharSequence unescape(final CharSequence chars) {
        return CharSequenceEscaping.unescape(chars);
    }

    /**
     * Stop creation
     */
    private CharSequences() {
        throw new UnsupportedOperationException();
    }
}
