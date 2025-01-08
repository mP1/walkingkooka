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

package walkingkooka.text;

import org.junit.jupiter.api.Test;
import walkingkooka.EmptyTextException;
import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.reflect.ThrowableTesting;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class CharSequencesTest implements PublicStaticHelperTesting<CharSequences>,
    ThrowableTesting {

    // nullToEmpty.....................................................................................................

    @Test
    public void testNullToEmptyNull() {
        this.nullToEmptyAndCheck(null, "");
    }

    @Test
    public void testNullToEmptyEmpty() {
        this.nullToEmptyAndCheck("");
    }

    @Test
    public void testNullToEmptyWhitespace() {
        this.nullToEmptyAndCheck(" \t\r");
    }

    @Test
    public void testNullToEmptyNotEmpty() {
        this.nullToEmptyAndCheck("abc123");
    }

    private void nullToEmptyAndCheck(final CharSequence chars) {
        this.nullToEmptyAndCheck(
            chars,
            chars
        );
    }

    private void nullToEmptyAndCheck(final CharSequence chars,
                                     final CharSequence expected) {
        this.checkEquals(
            expected,
            CharSequences.nullToEmpty(chars),
            () -> "nullToEmpty " + CharSequences.quoteAndEscape(chars)
        );
    }

    // failIfNullOrEmpty................................................................................................

    @Test
    public void testFailIfNullOrEmptyNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> CharSequences.failIfNullOrEmpty(null, "null-CharSequence")
        );
    }

    @Test
    public void testFailIfNullOrEmptyEmptyStringFails() {
        assertThrows(
            EmptyTextException.class,
            () -> CharSequences.failIfNullOrEmpty("", "empty-String")
        );
    }

    @Test
    public void testFailIfNullOrEmptyCharSequence() {
        final CharSequence charSequence = "123";
        assertSame(
            charSequence,
            CharSequences.failIfNullOrEmpty(charSequence, "1")
        );
    }

    @Test
    public void testFailIfNullOrEmptyString() {
        final String string = "123";
        assertSame(
            string,
            CharSequences.failIfNullOrEmpty(string, "1")
        );
    }

    @Test
    public void testFailIfNullOrEmptyStringBuilder() {
        final StringBuilder stringBuilder = new StringBuilder("123");
        assertSame(
            stringBuilder,
            CharSequences.failIfNullOrEmpty(stringBuilder, "1")
        );
    }

    // bestParse........................................................................................................

    @Test
    public void testBestParseWithNullTextFails() {
        assertThrows(
            NullPointerException.class,
            () -> CharSequences.bestParse(
                null,
                Integer::parseInt
            )
        );
    }

    @Test
    public void testBestParseWithNullParserFunctionFails() {
        assertThrows(
            NullPointerException.class,
            () -> CharSequences.bestParse(
                "",
                null
            )
        );
    }

    @Test
    public void testBestParseEmpty() {
        this.bestParseAndCheck(
            "",
            Integer::parseInt,
            0 // expected must be empty
        );
    }

    @Test
    public void testBestParseAll() {
        this.bestParseAndCheck(
            "1",
            Integer::parseInt,
            1 // expected must be empty
        );
    }

    @Test
    public void testBestParseAll2() {
        this.bestParseAndCheck(
            "12",
            Integer::parseInt,
            2 // expected must be empty
        );
    }

    @Test
    public void testBestParseAll3() {
        this.bestParseAndCheck(
            "123",
            Integer::parseInt,
            3 // expected must be empty
        );
    }

    @Test
    public void testBestParseThrowsInvalidCharacterException() {
        final String text = "123.";

        this.bestParseAndCheck(
            text,
            (t) -> {
                throw new InvalidCharacterException(
                    t,
                    t.indexOf('.')
                );
            },
            3 // expected must be empty
        );
    }

    @Test
    public void testBestParseRetries() {
        this.bestParseAndCheck(
            "123.",
            Integer::parseInt,
            '.' // expected must be empty
        );
    }

    @Test
    public void testBestParseRetries2() {
        this.bestParseAndCheck(
            "123.B",
            Integer::parseInt,
            '.' // expected must be empty
        );
    }

    @Test
    public void testBestParseRetries3() {
        this.bestParseAndCheck(
            "123.BC",
            Integer::parseInt,
            '.' // expected must be empty
        );
    }

    @Test
    public void testBestParseRetries4() {
        this.bestParseAndCheck(
            "123.BCD",
            Integer::parseInt,
            '.' // expected must be empty
        );
    }

    @Test
    public void testBestParseRetries5() {
        this.bestParseAndCheck(
            "1.",
            Integer::parseInt,
            '.' // expected must be empty
        );
    }

    @Test
    public void testBestParseRetriesMany() {
        final int length = 21;
        for (int i = 0; i < length; i++) {
            this.bestParseAndCheck(
                CharSequences.padRight(
                    "1.",
                    length,
                    'X'
                ).toString(),
                Integer::parseInt,
                '.' // expected must be empty
            );
        }
    }

    private void bestParseAndCheck(final String text,
                                   final Function<String, Object> parser,
                                   final char expected) {
        this.bestParseAndCheck(
            text,
            parser,
            text.indexOf(expected)
        );
    }

    private void bestParseAndCheck(final String text,
                                   final Function<String, Object> parser,
                                   final int expected) {
        this.checkEquals(
            expected,
            CharSequences.bestParse(
                text,
                parser
            ),
            () -> "bestParse " + CharSequences.quoteAndEscape(text)
        );
    }

    // bigEndianHexDigits....................................................................

    @Test
    public void testBigEndianHexDigitsNullFails() {
        assertThrows(NullPointerException.class, () -> CharSequences.bigEndianHexDigits(null));
    }

    @Test
    public void testBigEndianHexDigitsOddNumberOfDigitsFails() {
        assertThrows(IllegalArgumentException.class, () -> CharSequences.bigEndianHexDigits("1"));
    }

    @Test
    public void testBigEndianHexDigitsOddNumberOfDigits2Fails() {
        assertThrows(IllegalArgumentException.class, () -> CharSequences.bigEndianHexDigits("12345"));
    }

    @Test
    public void testBigEndianHexDigitsIllegalCharacterFails() {
        assertThrows(InvalidCharacterException.class, () -> CharSequences.bigEndianHexDigits("123X56"));
    }

    @Test
    public void testBigEndianHexDigitsEmpty() {
        this.bigEndianHexDigitsAndCheck("");
    }

    @Test
    public void testBigEndianHexDigitsTwoDigits() {
        this.bigEndianHexDigitsAndCheck("1F",
            new byte[]{0x1f});
    }

    @Test
    public void testBigEndianHexDigitsTwoDigits2() {
        this.bigEndianHexDigitsAndCheck("1f",
            new byte[]{0x1f});
    }

    @Test
    public void testBigEndianHexDigitsTwoDigitsLeadingZero() {
        this.bigEndianHexDigitsAndCheck("01",
            new byte[]{0x01});
    }

    @Test
    public void testBigEndianHexDigitsManyDigits() {
        this.bigEndianHexDigitsAndCheck("010203",
            new byte[]{1, 2, 3});
    }

    @Test
    public void testBigEndianHexDigitsManyDigits2() {
        this.bigEndianHexDigitsAndCheck("1234567890abcdef",
            (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xab, (byte) 0xcd, (byte) 0xef);
    }

    private void bigEndianHexDigitsAndCheck(final String hexDigits, final byte... expected) {
        final byte[] bytes = CharSequences.bigEndianHexDigits(hexDigits);
        if (false == Arrays.equals(expected, bytes)) {
            this.checkEquals("from " + CharSequences.quote(hexDigits),
                Arrays.toString(expected),
                Arrays.toString(bytes));
        }
    }

    // capitalize....................................................................................

    @Test
    public void testCapitalizeNullFails() {
        assertThrows(NullPointerException.class, () -> CharSequences.capitalize(null));
    }

    @Test
    public void testCapitalizeEmpty() {
        capitalizeAndCheck("", "");
    }

    @Test
    public void testCapitalizeSingleLetter() {
        capitalizeAndCheck("a", "A");
    }

    @Test
    public void testCapitalizeOneChar() {
        capitalizeAndCheck("1", "1");
    }

    @Test
    public void testCapitalizeCapitalize() {
        capitalizeAndCheck("apple", "Apple");
    }

    private void capitalizeAndCheck(final CharSequence chars, final String expected) {
        this.checkEquals(expected,
            CharSequences.capitalize(chars).toString(),
            "Capitalize " + CharSequences.quote(chars));
    }

    // copyCase .....................................................................................

    private final static String CHARS = "abc";
    private final static String CASE_SOURCE = "xyz";

    @Test
    public void testCopyCaseNullCharsFails() {
        assertThrows(NullPointerException.class, () -> CharSequences.copyCase(null, CASE_SOURCE));
    }

    @Test
    public void testCopyCaseNullCaseSourceFails() {
        assertThrows(NullPointerException.class, () -> CharSequences.copyCase(CHARS, null));
    }

    @Test
    public void testCopyCaseBothLowerCase() {
        copyCaseAndCheck("abc", "def", "abc");
    }

    @Test
    public void testCopyCaseEmptyCaseSource() {
        copyCaseAndCheck("abc", "", "abc");
    }

    @Test
    public void testCopyCaseEmptyChars() {
        copyCaseAndCheck("", CASE_SOURCE, "");
    }

    @Test
    public void testCopyCaseDifferentCase() {
        copyCaseAndCheck("abc", "XYZ", "ABC");
    }

    @Test
    public void testCopyCaseDifferentCase2() {
        copyCaseAndCheck("ABC", "xyz", "abc");
    }

    @Test
    public void testCopyCaseDifferentCase3() {
        copyCaseAndCheck("ABcd", "mnOP", "abCD");
    }

    @Test
    public void testCopyCaseCaseSourceNotLetters() {
        copyCaseAndCheck("ABcd", "123", "ABcd");
    }

    @Test
    public void testCopyCaseCharsIncludesNonLetters() {
        copyCaseAndCheck("AB12cd", "aBcDeF", "aB12cD");
    }

    @Test
    public void testCopyCaseCaseSourceShorter() {
        copyCaseAndCheck("ABcd", "a", "aBcd");
    }

    @Test
    public void testCopyCaseCaseSourceShorter2() {
        copyCaseAndCheck("aBcd", "A", "ABcd");
    }

    private void copyCaseAndCheck(final String chars, final String caseSource, final String expected) {
        this.checkEquals(
            expected,
            CharSequences.copyCase(chars, caseSource).toString(),
            "copyCase " + CharSequences.quote(chars) + ", " + CharSequences.quote(caseSource)
        );
    }

    // ends with .................................................................

    @Test
    public void testEndsWithDifferentEnding() {
        final CharSequence test = "apple";
        final String endsWith = "X";
        endsWithAndCheck(test, endsWith, false);
    }

    @Test
    public void testEndsWithSameCase() {
        final CharSequence test = "apple";
        final String endsWith = "le";
        endsWithAndCheck(test, endsWith, true);
    }

    @Test
    public void testEndsWithDifferentCase() {
        final CharSequence test = "Apple";
        final String endsWith = "LE";
        endsWithAndCheck(test, endsWith, false);
    }

    private void endsWithAndCheck(final CharSequence chars, final String endsWith, final boolean result) {
        this.checkEquals(result,
            CharSequences.endsWith(chars, endsWith),
            CharSequences.quote(chars) + " ends with " + CharSequences.quote(endsWith));
    }

    // equals .......................................................................

    @Test
    public void testEqualsDifferentLength() {
        final CharSequence test = "apple";
        final String other = "different";
        equalsAndCheck(test, other, false);
    }

    @Test
    public void testEqualsSame() {
        final CharSequence test = "apple";
        final String other = "apple";
        equalsAndCheck(test, other, true);
    }

    @Test
    public void testEqualsDifferentCase() {
        final CharSequence test = "apple";
        final String other = "APPLE";
        equalsAndCheck(test, other, false);
    }

    private void equalsAndCheck(final CharSequence chars, final String endsWith, final boolean result) {
        this.checkEquals(result,
            CharSequences.equals(chars, endsWith),
            CharSequences.quote(chars) + " equals " + CharSequences.quote(endsWith));
    }

    // escape/unescape .......................................................................

    @Test
    public void testEscapeUnescapeNoNeeded() {
        this.escapeUnescapeAndCheck("apple", "apple");
    }

    @Test
    public void testEscapeUnescapeNewLine() {
        this.escapeUnescapeAndCheck("apple\nbanana", "apple\\nbanana");
    }

    @Test
    public void testEscapeUnescapeCarriageReturn() {
        this.escapeUnescapeAndCheck("apple\rbanana", "apple\\rbanana");
    }

    @Test
    public void testEscapeUnescapeBackslash() {
        this.escapeUnescapeAndCheck("apple\\banana", "apple\\\\banana");
    }

    @Test
    public void testEscapeUnescapeTab() {
        this.escapeUnescapeAndCheck("apple\tbanana", "apple\\tbanana");
    }

    @Test
    public void testEscapeUnescapeDoubleQuote() {
        this.escapeUnescapeAndCheck("apple\"", "apple\\\"");
    }

    @Test
    public void testEscapeUnescapeSingleQuote() {
        this.escapeUnescapeAndCheck("apple\'", "apple\\\'");
    }

    @Test
    public void testEscapeUnescapeNul() {
        this.escapeUnescapeAndCheck("apple\0", "apple\\0");
    }

    @Test
    public void testEscapeUnescapeTabNewLineCarriageReturn() {
        this.escapeUnescapeAndCheck("apple\t\n\r\\", "apple\\t\\n\\r\\\\");
    }

    @Test
    public void testEscapeUnescapeControlCharacter() {
        this.escapeUnescapeAndCheck("apple\u000F;banana", "apple\\u000F;banana");
    }

    @Test
    public void testEscapeUnescapeControlCharacter2() {
        this.escapeUnescapeAndCheck("apple\u001F;banana", "apple\\u001F;banana");
    }

    private void escapeUnescapeAndCheck(final CharSequence chars, final String expected) {
        this.checkEquals(expected,
            CharSequences.escape(chars).toString(),
            () -> "escape " + CharSequences.quote(chars));
        this.checkEquals(chars.toString(),
            CharSequences.unescape(expected),
            () -> "unescape " + CharSequences.quote(chars));
    }

    // hash.............................................................................................................

    @Test
    public void testHashNull() {
        this.checkEquals(0, CharSequences.hash(null));
    }

    @Test
    public void testHashNonNull() {
        final String text = "abc123";
        this.checkEquals(text.hashCode(), CharSequences.hash(text));
    }

    // indexOf..........................................................................................................

    @Test
    public void testIndexOfNullCharsFails() {
        assertThrows(NullPointerException.class, () -> CharSequences.indexOf(null, "searchFor"));
    }

    @Test
    public void testIndexOfNullSearchForFails() {
        assertThrows(NullPointerException.class, () -> CharSequences.indexOf("chars", null));
    }

    @Test
    public void testIndexOfEmptySearchForFails() {
        assertThrows(IllegalArgumentException.class, () -> CharSequences.indexOf("chars", ""));
    }

    @Test
    public void testIndexOfNotContained() {
        final StringBuilder chars = new StringBuilder("apple");
        final String search = "banana";

        indexOfAndCheck(chars, search, -1);
    }

    @Test
    public void testIndexOfContainsWithSameCase() {
        final StringBuilder chars = new StringBuilder("apple banana carrot");
        final String search = "banana";

        indexOfAndCheck(chars, search, 6);
    }

    @Test
    public void testIndexOfContainsDifferentCase() {
        final StringBuilder chars = new StringBuilder("apple banana carrot");
        final String search = "BANAna";

        indexOfAndCheck(chars, search, -1);
    }

    @Test
    public void testIndexOfPartialButIncompleteMatch() {
        final StringBuilder chars = new StringBuilder("apple banana carrot");
        final String search = "BANANARAMAMA";

        indexOfAndCheck(chars, search, -1);
    }

    @Test
    public void testIndexOfMatchesStart() {
        final StringBuilder chars = new StringBuilder("apple banana carrot");
        final String search = "apple";

        indexOfAndCheck(chars, search, 0);
    }

    @Test
    public void testIndexOfMatchesEnd() {
        final StringBuilder chars = new StringBuilder("apple banana carrot");
        final String search = "carrot";

        indexOfAndCheck(chars, search, "apple banana ".length());
    }

    @Test
    public void testIndexOfOneChar() {
        final StringBuilder chars = new StringBuilder("a");
        indexOfAndCheck(chars, chars.toString(), 0);
    }

    @Test
    public void testIndexOfOneCharIfIgnoringCase() {
        indexOfAndCheck(new StringBuilder("a"), "A", -1);
    }

    @Test
    public void testIndexOfOneCharFails() {
        indexOfAndCheck(new StringBuilder("a"), "b", -1);
    }

    @Test
    public void testIndexOfEmptyCharSequence() {
        indexOfAndCheck(new StringBuilder(), "never matched", -1);
    }

    @Test
    public void testIndexOfSearchForContainCharSequenceAndMore() {
        final StringBuilder chars = new StringBuilder("chars");
        indexOfAndCheck(chars, chars + " + extra", -1);
    }

    @Test
    public void testIndexOfEqual() {
        final StringBuilder chars = new StringBuilder("chars");
        indexOfAndCheck(chars, "" + chars, 0);
    }

    @Test
    public void testIndexOfEqualButDifferentCase() {
        final String chars = "chars";
        indexOfAndCheck(chars.toUpperCase(), chars, -1);
    }

    private void indexOfAndCheck(final CharSequence chars, final String of, final int index) {
        this.checkEquals(index,
            CharSequences.indexOf(chars, of),
            () -> CharSequences.quote(chars) + "index of " + CharSequences.quote(of));
    }

    // isNullOrEmpty................................................

    @Test
    public void testIsNullOrEmptyNotEmptyString() {
        isNullOrEmptyAndCheck("a", false);
    }

    @Test
    public void testIsNullOrEmptyEmptyString() {
        isNullOrEmptyAndCheck("", true);
    }

    @Test
    public void testIsNullOrEmptyNull() {
        isNullOrEmptyAndCheck(null, true);
    }

    private void isNullOrEmptyAndCheck(final CharSequence chars, final boolean result) {
        this.checkEquals(CharSequences.isNullOrEmpty(chars), result, () -> chars + " isNullOrEmpty");
    }

    // padLeft................................................

    private final static int LENGTH = CHARS.length() + 3; // 3 + 3
    private final static char PADDING = '.';

    @Test
    public void testPadLeftNullCharSequenceFails() {
        assertThrows(NullPointerException.class, () -> CharSequences.padLeft(null, LENGTH, PADDING));
    }

    @Test
    public void testPadLeftInvalidLengthFails() {
        assertThrows(IllegalArgumentException.class, () -> CharSequences.padLeft(CHARS, CHARS.length() - 1, PADDING));
    }

    @Test
    public void testPadLeftPaddingNotRequired() {
        assertSame(CHARS,
            CharSequences.padLeft(CHARS,
                CHARS.length(),
                PADDING));
    }

    @Test
    public void testPadLeftLength() {
        this.checkEquals(LENGTH, this.padLeft().length());
    }

    @Test
    public void testPadLeftSubSequencePaddingOnly() {
        this.padLeftSubSequenceAndCheck(0, 1, "" + PADDING);
    }

    @Test
    public void testPadLeftSubSequencePaddingOnly2() {
        this.padLeftSubSequenceAndCheck(0, 2, "" + PADDING + PADDING);
    }

    @Test
    public void testPadLeftSubSequenceExactlyWrapped() {
        final CharSequence sequence = this.padLeft();
        assertSame(CHARS,
            sequence.subSequence(3, LENGTH));
    }

    @Test
    public void testPadLeftSubSequenceWithinWrapped() {
        this.padLeftSubSequenceAndCheck(4, LENGTH, "bc");
    }

    @Test
    public void testPadLeftSubSequenceWithinWrapped2() {
        this.padLeftSubSequenceAndCheck(5, LENGTH, "c");
    }

    @Test
    public void testPadLeftSubSequenceSomePadding() {
        this.padLeftSubSequenceAndCheck(1, LENGTH, "" + PADDING + PADDING + "abc");
    }

    @Test
    public void testPadLeftSubSequenceSomePadding2() {
        this.padLeftSubSequenceAndCheck(2, LENGTH - 1, PADDING + "ab");
    }

    private void padLeftSubSequenceAndCheck(final int start,
                                            final int end,
                                            final String expected) {
        final CharSequence sequence = this.padLeft();
        final CharSequence sub = sequence.subSequence(start, end);

        this.checkEquals(expected,
            sub.toString(),
            () -> sequence + " sub sequence " + start + "," + end);
    }

    private CharSequence padLeft() {
        return CharSequences.padLeft(CHARS,
            LENGTH,
            PADDING);
    }

    // padRight................................................

    @Test
    public void testPadRightNullCharSequenceFails() {
        assertThrows(NullPointerException.class, () -> CharSequences.padRight(null, LENGTH, PADDING));
    }

    @Test
    public void testPadRightInvalidLengthFails() {
        assertThrows(IllegalArgumentException.class, () -> CharSequences.padRight(CHARS, CHARS.length() - 1, PADDING));
    }

    @Test
    public void testPadRightPaddingNotRequired() {
        assertSame(CHARS,
            CharSequences.padRight(CHARS,
                CHARS.length(),
                PADDING));
    }

    @Test
    public void testPadRightLength() {
        this.checkEquals(LENGTH,
            CharSequences.padRight(CHARS,
                LENGTH,
                PADDING).length());
    }

    @Test
    public void testPadRightSubSequencePaddingOnly() {
        this.padRightSubSequenceAndCheck(3, 4, "" + PADDING);
    }

    @Test
    public void testPadRightSubSequencePaddingOnly2() {
        this.padRightSubSequenceAndCheck(3, 5, "" + PADDING + PADDING);
    }

    @Test
    public void testPadRightSubSequenceExactlyWrapped() {
        final CharSequence sequence = this.padRight();
        assertSame(CHARS,
            sequence.subSequence(0, CHARS.length()));
    }

    @Test
    public void testPadRightSubSequenceWithinWrapped() {
        this.padRightSubSequenceAndCheck(1, 3, "bc");
    }

    @Test
    public void testPadRightSubSequenceWithinWrapped2() {
        this.padRightSubSequenceAndCheck(2, 3, "c");
    }

    @Test
    public void testPadRightSubSequenceSomePadding() {
        this.padRightSubSequenceAndCheck(1, LENGTH, "bc" + PADDING + PADDING + PADDING);
    }

    @Test
    public void testPadRightSubSequenceSomePadding2() {
        this.padRightSubSequenceAndCheck(2, LENGTH - 1, "c" + PADDING + PADDING);
    }

    private void padRightSubSequenceAndCheck(final int start,
                                             final int end,
                                             final String expected) {
        final CharSequence sequence = this.padRight();
        final CharSequence sub = sequence.subSequence(start, end);

        this.checkEquals(expected,
            sub.toString(),
            () -> sequence + " sub sequence " + start + "," + end);
    }

    private CharSequence padRight() {
        return CharSequences.padRight(CHARS,
            LENGTH,
            PADDING);
    }

    // quote and escape char ............................................................

    @Test
    public void testQuoteAndEscapeChar() {
        this.quoteAndEscapeCharAndCheck('a', "\'a\'");
    }

    @Test
    public void testQuoteAndEscapeCharTab() {
        this.quoteAndEscapeCharAndCheck('\t', "\'\\t\'");
    }

    @Test
    public void testQuoteAndEscapeCharNul() {
        this.quoteAndEscapeCharAndCheck('\0', "\'\\0\'");
    }

    private void quoteAndEscapeCharAndCheck(final char c, final String expected) {
        this.checkEquals(expected,
            CharSequences.quoteAndEscape(c).toString(),
            "Escape " + CharSequences.quoteIfChars(c));
    }

    // quote and escape CharSequence ............................................................

    @Test
    public void testQuoteAndEscapeCharSequence() {
        quoteCharSequenceAndCheck("a", "\"a\"");
    }

    @Test
    public void testQuoteAndEscapeCharSequence2() {
        quoteCharSequenceAndCheck("a\t", "\"a\t\"");
    }

    @Test
    public void testQuoteAndEscapeCharSequence3() {
        quoteCharSequenceAndCheck("a\"", "\"a\"\"");
    }

    private void quoteCharSequenceAndCheck(final CharSequence chars, final String expected) {
        this.checkEquals(expected,
            CharSequences.quote(chars).toString(),
            "Escape " + CharSequences.quote(chars));
    }

    // quote and escape ............................................................

    @Test
    public void testQuoteAndEscapeCharSequenceNull() {
        this.checkEquals(null, CharSequences.quoteAndEscape(null));
    }

    @Test
    public void testQuoteAndEscapeCharSequenceEmpty() {
        final String string = "";
        quoteAndEscapeCharSequenceAndCheck(string, '"' + string + '"');
    }

    @Test
    public void testQuoteAndEscapeCharSequenceSingleChar() {
        quoteAndEscapeCharSequenceAndCheck("a", "\"a\"");
    }

    @Test
    public void testQuoteAndEscapeCharSequenceDoubleQuoteChar() {
        quoteAndEscapeCharSequenceAndCheck("\"", "\"\\\"\"");
    }

    @Test
    public void testQuoteAndEscapeCharSequenceSingleQuoteChar() {
        quoteAndEscapeCharSequenceAndCheck("'", "\"\\'\"");
    }

    @Test
    public void testQuoteAndEscapeCharSequenceUnneeded() {
        final String string = "apple";
        quoteAndEscapeCharSequenceAndCheck(string, '"' + string + '"');
    }

    @Test
    public void testQuoteAndEscapeCharSequenceNewLine() {
        final String string = "apple\nbanana";
        quoteAndEscapeCharSequenceAndCheck(string, "\"apple\\nbanana\"");
    }

    @Test
    public void testQuoteAndEscapeCharSequenceCarriageReturn() {
        final String string = "apple\rbanana";
        quoteAndEscapeCharSequenceAndCheck(string, "\"apple\\rbanana\"");
    }

    @Test
    public void testQuoteAndEscapeCharSequenceBackslash() {
        final String string = "apple\\banana";
        quoteAndEscapeCharSequenceAndCheck(string, "\"apple\\\\banana\"");
    }

    @Test
    public void testQuoteAndEscapeCharSequenceTab() {
        final String string = "apple\tbanana";
        quoteAndEscapeCharSequenceAndCheck(string, "\"apple\\tbanana\"");
    }

    @Test
    public void testQuoteAndEscapeCharSequenceEscapedDoubleQuote() {
        final String string = "apple\"";
        quoteAndEscapeCharSequenceAndCheck(string, "\"apple\\\"\"");
    }

    @Test
    public void testQuoteAndEscapeCharSequenceAddEscaped() {
        final String string = "apple\t\n\r\\";
        quoteAndEscapeCharSequenceAndCheck(string, "\"apple\\t\\n\\r\\\\\"");
    }

    private void quoteAndEscapeCharSequenceAndCheck(final CharSequence chars, final String expected) {
        this.checkEquals(expected,
            CharSequences.quoteAndEscape(chars),
            "Escape " + CharSequences.quote(chars));
    }

    // quote if chars ................................................................................

    @Test
    public void testQuoteIfCharsNull() {
        quoteIfCharsAndCheck(null, "" + null);
    }

    @Test
    public void testQuoteIfCharsEmptyCharSequence() {
        quoteIfCharsAndCheck("", "\"\"");
    }

    @Test
    public void testQuoteIfCharsNonCharSequence() {
        quoteIfCharsAndCheck(1L, "1");
    }

    @Test
    public void testQuoteIfCharsCharSequenceRequiresQuotes() {
        quoteIfCharsAndCheck("abc\n", "\"abc\\n\"");
    }

    @Test
    public void testQuoteIfCharsCharSequenceAlreadyQuoted() {
        quoteIfCharsAndCheck("\"abc\n\"", "\"abc\\n\"");
    }

    @Test
    public void testQuoteIfCharsCharArrayRequiredQuotes() {
        quoteIfCharsAndCheck("\"abc\n\"".toCharArray(), "\"abc\\n\"");
    }

    @Test
    public void testQuoteIfCharsCharArrayAlreadyQuoted() {
        quoteIfCharsAndCheck("\"abc\n\"".toCharArray(), "\"abc\\n\"");
    }

    @Test
    public void testQuoteIfCharsChar() {
        quoteIfCharsAndCheck('\n', "'\\n'");
    }

    @Test
    public void testQuoteIfCharsList() {
        this.quoteIfCharsAndCheck(
            Lists.of(
                11,
                "Hello",
                null
            ),
            Lists.of(
                11,
                "\"Hello\"",
                null
            ).toString()
        );
    }

    @Test
    public void testQuoteIfCharsMap() {
        this.quoteIfCharsAndCheck(
            Maps.of(
                11,
                22,
                "Key33",
                "Value44",
                "Key55",
                Optional.of(
                    "Value66"
                )
            ),
            Maps.of(
                "11",
                22,
                "\"Key33\"",
                "\"Value44\"",
                "\"Key55\"",
                "\"Value66\""
            ).toString()
        );
    }

    @Test
    public void testQuoteIfCharsEmptyOptional() {
        this.quoteIfCharsAndCheck(
            Optional.empty(),
            "null"
        );
    }

    @Test
    public void testQuoteIfCharsOptionalString() {
        this.quoteIfCharsAndCheck(
            Optional.of("Hello"),
            "\"Hello\""
        );
    }

    @Test
    public void testQuoteIfCharsOptionalNonString() {
        this.quoteIfCharsAndCheck(
            Optional.of(123),
            "123"
        );
    }

    @Test
    public void testQuoteIfCharsOptionalList() {
        this.quoteIfCharsAndCheck(
            Optional.of(
                Lists.of(
                    11,
                    "Hello",
                    null
                )
            ),
            Lists.of(
                "11",
                "\"Hello\"",
                null
            ).toString()
        );
    }

    private void quoteIfCharsAndCheck(final Object chars, final String expected) {
        this.checkEquals(expected,
            CharSequences.quoteIfChars(chars),
            "Quote if chars " + chars);
    }

    // quote if necessary .................................................................................

    @Test
    public void testQuoteIfNecessaryNull() {
        final CharSequence chars = null;
        this.checkEquals(chars, CharSequences.quoteIfNecessary(chars));
    }

    @Test
    public void testQuoteIfNecessaryUnnecessary() {
        final String chars = "\"apple\"";
        quoteIfNecessaryAndCheck(chars, chars);
    }

    @Test
    public void testQuoteIfNecessaryQuotesRequired() {
        final CharSequence chars = "apple banana";
        quoteIfNecessaryAndCheck(chars, '"' + chars.toString() + '"');
    }

    @Test
    public void testQuoteIfNecessaryAlreadyQuoted() {
        final String already = "\"abc\"";
        quoteIfNecessaryAndCheck(already, already);
    }

    private void quoteIfNecessaryAndCheck(final CharSequence sequence, final String expected) {
        this.checkEquals(expected,
            CharSequences.quoteIfNecessary(sequence).toString(),
            "Quote if necessary \"" + sequence + "\" ");
    }

    // startsWith.......................................................................................................

    @Test
    public void testStartsWithDifferent() {
        final CharSequence test = "apple";
        final String startsWith = "X";
        startsWithAndCheck(test, startsWith, false);
    }

    @Test
    public void testStartsWithMixedCase() {
        final CharSequence test = "apple";
        final String startsWith = "aP";
        startsWithAndCheck(test, startsWith, false);
    }

    @Test
    public void testStartsWithStartsWithDifferentCase() {
        final CharSequence test = "Apple";
        final String startsWith = "AP";
        startsWithAndCheck(test, startsWith, false);
    }

    @Test
    public void testStartsWithSameCase() {
        final CharSequence test = "Apple";
        final String startsWith = test + "Banana";
        startsWithAndCheck(test, startsWith, false);
    }

    @Test
    public void testStartsWithStartsWith() {
        final CharSequence first = "green.apple";
        final String second = "green";

        startsWithAndCheck(first, second, true);
    }

    @Test
    public void testStartsWithFailsBecauseSecondIsLongerThanFirst() {
        final CharSequence first = "green.apple";
        final String second = "green.apple.big";

        startsWithAndCheck(first, second, false);
    }

    private void startsWithAndCheck(final CharSequence chars, final String startsWith, final boolean result) {
        this.checkEquals(result,
            CharSequences.startsWith(chars, startsWith),
            CharSequences.quote(chars) + " starts with " + CharSequences.quote(startsWith));
    }

    // subSequence............................................................................................

    @Test
    public void testSubSequenceNullFails() {
        assertThrows(NullPointerException.class, () -> CharSequences.subSequence(null, 1, 1));
    }

    @Test
    public void testSubSequenceToBeforeFromFails() {
        final int from = 4;
        final int to = -5;
        final String string = "string";

        final IndexOutOfBoundsException expected = assertThrows(IndexOutOfBoundsException.class, () -> CharSequences.subSequence(string, from, to));
        checkMessage(expected,
            CharSequences.toIndexBeforeFromIndex(from, to, string.length()));
    }

    @Test
    public void testSubSequencePositiveIndices() {
        this.subSequenceAndCheck(" sub ", 1, 4, "sub");
    }

    @Test
    public void testSubSequencePositiveIndices2() {
        this.subSequenceAndCheck(" ssuubbb ", 2, 6, "suub");
    }

    @Test
    public void testSubSequenceNegativeTo() {
        this.subSequenceAndCheck("abcdef", 1, -2, "bcd");
    }

    @Test
    public void testSubSequenceZeroTo() {
        this.subSequenceAndCheck("abcdef", 1, 0, "bcdef");
    }

    private void subSequenceAndCheck(final String chars, final int from, final int to, final String expected) {
        this.checkEquals(expected,
            CharSequences.subSequence(chars, from, to),
            "subSequence of " + CharSequences.quote(chars) + " " + from + ".." + to);
    }

    // trim............................................................................................

    @Test
    public void testTrimNullFails() {
        assertThrows(NullPointerException.class, () -> CharSequences.trim(null));
    }

    @Test
    public void testTrimEmpty() {
        this.trimAndCheck("", "");
    }

    @Test
    public void testTrimWhitespaceBefore() {
        this.trimAndCheck(" apple", "apple");
    }

    @Test
    public void testTrimWhitespaceAfter() {
        this.trimAndCheck("apple ", "apple");
    }

    @Test
    public void testTrimWhitespaceBeforeAndAfter() {
        this.trimAndCheck(" apple ", "apple");
    }

    @Test
    public void testTrimWhitespace() {
        this.trimAndCheck(" ", "");
    }

    @Test
    public void testTrimWhitespaceOnly() {
        this.trimAndCheck("   ", "");
    }

    private void trimAndCheck(final CharSequence sequence, final CharSequence expected) {
        this.checkEquals(expected,
            CharSequences.trim(sequence),
            () -> "trimming of " + CharSequences.quote(sequence.toString()));
    }

    // trimLeft ............................................................................................

    @Test
    public void testTrimLeftNullFails() {
        assertThrows(NullPointerException.class, () -> CharSequences.trimLeft(null));
    }

    @Test
    public void testTrimLeftLeadingAndTrailingWhitespace() {
        this.trimLeftAndCheck(" apple ", "apple ");
    }

    @Test
    public void testTrimLeftTrailingWhitespace() {
        this.trimLeftAndCheck("apple ", "apple ");
    }

    @Test
    public void testTrimLeftLeadingWhitespace() {
        this.trimLeftAndCheck("   apple ", "apple ");
    }

    @Test
    public void testTrimLeftWhitespaceOnly() {
        this.trimLeftAndCheck("   ", "");
    }

    private void trimLeftAndCheck(final CharSequence input, final CharSequence expected) {
        final CharSequence actual = CharSequences.trimLeft(input);
        this.checkEquals(expected, actual, () -> "trimming of " + CharSequences.quote(input.toString()));
    }

    // trimRight ............................................................................................

    @Test
    public void testTrimRightNullFails() {
        assertThrows(NullPointerException.class, () -> CharSequences.trimRight(null));
    }

    @Test
    public void testTrimRightLeadingAndTrailingWhitespace() {
        this.trimRightAndCheck(" apple ", " apple");
    }

    @Test
    public void testTrimRightNoTrailingWhitespace() {
        this.trimRightAndCheck(" apple", " apple");
    }

    @Test
    public void testTrimRightTrailingWhitespace() {
        this.trimRightAndCheck(" apple   ", " apple");
    }

    @Test
    public void testTrimRightWhitespaceOnly() {
        this.trimRightAndCheck("   ", "");
    }

    private void trimRightAndCheck(final CharSequence input, final CharSequence expected) {
        final CharSequence actual = CharSequences.trimRight(input);
        this.checkEquals(expected, actual, () -> "trimming of " + CharSequences.quote(input.toString()));
    }

    @Override
    public Class<CharSequences> type() {
        return CharSequences.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
