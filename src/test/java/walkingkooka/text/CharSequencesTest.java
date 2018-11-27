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

import org.junit.Test;
import walkingkooka.test.PublicStaticHelperTestCase;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

final public class CharSequencesTest extends PublicStaticHelperTestCase<CharSequences> {

    // capitalize....................................................................................

    @Test(expected = NullPointerException.class)
    public void testCapitalizeNullFails() {
        CharSequences.capitalize(null);
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
        assertEquals("Capitalize " + CharSequences.quote(chars), expected, CharSequences.capitalize(chars).toString());
    }

    // copyCase .....................................................................................

    private final static String CHARS = "abc";
    private final static String CASE_SOURCE = "xyz";

    @Test(expected = NullPointerException.class)
    public void testCopyCaseNullCharsFails() {
        CharSequences.copyCase(null, CASE_SOURCE);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyCaseNullCaseSourceFails() {
        CharSequences.copyCase(CHARS, null);
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

    private static void copyCaseAndCheck(final String chars, final String caseSource, final String expected) {
        assertEquals("copyCase " + CharSequences.quote(chars) + ", " + CharSequences.quote(caseSource),
                expected,
                CharSequences.copyCase(chars, caseSource).toString());
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
        assertEquals(CharSequences.quote(chars) + " ends with " + CharSequences.quote(endsWith),
                result,
                CharSequences.endsWith(chars, endsWith));
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
        assertEquals(CharSequences.quote(chars) + " equals " + CharSequences.quote(endsWith),
                result,
                CharSequences.equals(chars, endsWith));
    }

    // indexOf....................................................................

    @Test(expected = NullPointerException.class)
    public void testIndexOfNullCharsFails() {
        CharSequences.indexOf(null, "searchFor");
    }

    @Test(expected = NullPointerException.class)
    public void testIndexOfNullSearchForFails() {
        CharSequences.indexOf("chars", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexOfEmptySearchForFails() {
        CharSequences.indexOf("chars", "");
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
        assertEquals(CharSequences.quote(chars) + "index of " + CharSequences.quote(of), index, CharSequences.indexOf(chars, of));
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
        assertEquals(chars + " isNullOrEmpty", CharSequences.isNullOrEmpty(chars), result);
    }

    // quote and escape ............................................................

    @Test
    public void testQuoteAndEscapeNull() {
        assertEquals(null, CharSequences.quoteAndEscape(null));
    }

    @Test
    public void testQuoteAndEscapeEmpty() {
        final String string = "";
        quoteAndEscapeAndCheck(string, '"' + string + '"');
    }

    @Test
    public void testQuoteAndEscapeSingleChar() {
        quoteAndEscapeAndCheck("a", "\"a\"");
    }

    @Test
    public void testQuoteAndEscapeDoubleQuoteChar() {
        quoteAndEscapeAndCheck("\"", "\"\\\"\"");
    }

    @Test
    public void testQuoteAndEscapeSingleQuoteChar() {
        quoteAndEscapeAndCheck("'", "\"\\'\"");
    }

    @Test
    public void testQuoteAndEscapeUnneeded() {
        final String string = "apple";
        quoteAndEscapeAndCheck(string, '"' + string + '"');
    }

    @Test
    public void testQuoteAndEscapeNewLine() {
        final String string = "apple\nbanana";
        quoteAndEscapeAndCheck(string, "\"apple\\nbanana\"");
    }

    @Test
    public void testQuoteAndEscapeCarriageReturn() {
        final String string = "apple\rbanana";
        quoteAndEscapeAndCheck(string, "\"apple\\rbanana\"");
    }

    @Test
    public void testQuoteAndEscapeBackslash() {
        final String string = "apple\\banana";
        quoteAndEscapeAndCheck(string, "\"apple\\\\banana\"");
    }

    @Test
    public void testQuoteAndEscapeTab() {
        final String string = "apple\tbanana";
        quoteAndEscapeAndCheck(string, "\"apple\\tbanana\"");
    }

    @Test
    public void testQuoteAndEscapeEscapedDoubleQuote() {
        final String string = "apple\"";
        quoteAndEscapeAndCheck(string, "\"apple\\\"\"");
    }

    @Test
    public void testQuoteAndEscapeAddEscaped() {
        final String string = "apple\t\n\r\\";
        quoteAndEscapeAndCheck(string, "\"apple\\t\\n\\r\\\\\"");
    }

    private void quoteAndEscapeAndCheck(final CharSequence chars, final String expected) {
        assertEquals("Escape " + CharSequences.quote(chars), expected, CharSequences.quoteAndEscape(chars));
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
        quoteIfCharsAndCheck(Character.valueOf('\n'), "'\\n'");
    }

    private void quoteIfCharsAndCheck(final Object chars, final String expected) {
        assertEquals("Quote if chars " + chars, expected, CharSequences.quoteIfChars(chars));
    }

    // quote if necessary .................................................................................

    @Test
    public void testQuoteIfNecessaryNull() {
        final CharSequence chars = null;
        assertEquals(chars, CharSequences.quoteIfNecessary(chars));
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
        assertEquals("Quote if necessary \"" + sequence + "\" ",
                expected,
                CharSequences.quoteIfNecessary(sequence).toString());
    }

    // shrink............................................................................

    @Test(expected = NullPointerException.class)
    public void testShrinkNullCharSequenceFails() {
        CharSequences.shrink(null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShrinkInvalidDesiredLengthFails() {
        CharSequences.shrink("apple", 5);
    }

    @Test
    public void testShrinkTooShort() {
        final String string = "apple";
        this.shrinkAndCheck(string, 10, string);
    }

    @Test
    public void testShrinkAlreadyDesiredLengthed() {
        this.shrinkAndCheck("greenapple", 10, "greenapple");
    }

    @Test
    public void testShrinkNeedsShrinking() {
        this.shrinkAndCheck("apple banana", 10, "appl...ana");
    }

    @Test
    public void testShrinkNeedsShrinking2() {
        this.shrinkAndCheck("apple banana carrot", 11, "appl...rrot");
    }

    private void shrinkAndCheck(final CharSequence sequence, final int desiredLength,
                                final CharSequence expected) {
        final CharSequence actual = CharSequences.shrink(sequence, desiredLength);
        assertEquals("Shrinking \"" + sequence + "\" with a desired length=" + desiredLength,
                expected,
                actual);
    }

    // startsWith.....................................................................................

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
        assertEquals(CharSequences.quote(chars) + " starts with " + CharSequences.quote(startsWith),
                result,
                CharSequences.startsWith(chars, startsWith));
    }

    @Override
    protected Class<CharSequences> type() {
        return CharSequences.class;
    }

    @Override
    protected boolean canHavePublicTypes(final Method method) {
        return false;
    }
}
