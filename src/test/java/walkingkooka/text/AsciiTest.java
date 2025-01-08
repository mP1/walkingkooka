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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class AsciiTest implements PublicStaticHelperTesting<Ascii> {

    // isLetter

    @Test
    public void testIsLetterBigA() {
        this.checkLetter('A');
    }

    @Test
    public void testIsLetterBigZ() {
        this.checkLetter('Z');
    }

    @Test
    public void testIsLetterLittleA() {
        this.checkLetter('a');
    }

    @Test
    public void testIsLetterLittleZ() {
        this.checkLetter('z');
    }

    @Test
    public void testIsLetterWithSpace() {
        this.checkNotLetter(' ');
    }

    @Test
    public void testIsLetterWithSymbol() {
        this.checkNotLetter('#');
    }

    @Test
    public void testIsLetterWithDigit() {
        this.checkNotLetter('0');
    }

    @Test
    public void testIsLetterWithControl() {
        this.checkNotLetter(Ascii.NUL);
    }

    @Test
    public void testIsLetterWithDelete() {
        this.checkNotLetter(Ascii.DELETE);
    }

    private void checkLetter(final char c) {
        assertTrue(Ascii.isLetter(c), "'" + c + "' is a letter");
    }

    private void checkNotLetter(final char c) {
        assertFalse(Ascii.isLetter(c), "'" + c + "' is a NOT letter");
    }

    // isDigit

    @Test
    public void testIsDigitBigA() {
        this.checkNotDigit('A');
    }

    @Test
    public void testIsDigitBigZ() {
        this.checkNotDigit('Z');
    }

    @Test
    public void testIsDigitLittleA() {
        this.checkNotDigit('a');
    }

    @Test
    public void testIsDigitLittleZ() {
        this.checkNotDigit('z');
    }

    @Test
    public void testIsDigitWithZero() {
        this.checkDigit('0');
    }

    @Test
    public void testIsDigitWithNine() {
        this.checkDigit('9');
    }

    @Test
    public void testIsDigitWithControl() {
        this.checkNotDigit(Ascii.NUL);
    }

    @Test
    public void testIsDigitWithSpace() {
        this.checkNotDigit(' ');
    }

    @Test
    public void testIsDigitWithSymbol() {
        this.checkNotDigit('#');
    }

    @Test
    public void testIsDigitWithDelete() {
        this.checkNotDigit(Ascii.DELETE);
    }

    private void checkDigit(final char c) {
        assertTrue(Ascii.isDigit(c), "'" + c + "' is a digit");
    }

    private void checkNotDigit(final char c) {
        assertFalse(Ascii.isDigit(c), "'" + c + "' is a NOT digit");
    }

    // isPrintable

    @Test
    public void testIsPrintableWithSpace() {
        this.checkPrintable(' ');
    }

    @Test
    public void testIsPrintableWithDigit() {
        this.checkPrintable('0');
    }

    @Test
    public void testIsPrintableWithLetter() {
        this.checkPrintable('0');
    }

    @Test
    public void testIsPrintableWithSymbol() {
        this.checkPrintable('#');
    }

    @Test
    public void testIsPrintableWithControl() {
        this.checkNotPrintable(Ascii.NUL);
    }

    @Test
    public void testIsPrintableWithDelete() {
        this.checkNotPrintable(Ascii.DELETE);
    }

    private void checkPrintable(final char c) {
        assertTrue(Ascii.isPrintable(c), "'" + c + "' is a printable");
    }

    private void checkNotPrintable(final char c) {
        assertFalse(Ascii.isPrintable(c), "'" + c + "' is a NOT printable");
    }

    // isControl

    @Test
    public void testIsControlWithSpace() {
        this.checkNotControl(' ');
    }

    @Test
    public void testIsControlWithDigit() {
        this.checkNotControl('0');
    }

    @Test
    public void testIsControlWithLetter() {
        this.checkNotControl('0');
    }

    @Test
    public void testIsControlWithSymbol() {
        this.checkNotControl('#');
    }

    @Test
    public void testIsControlWithControl() {
        this.checkControl(Ascii.NUL);
    }

    @Test
    public void testIsControlWithDelete() {
        this.checkControl(Ascii.DELETE);
    }

    private void checkControl(final char c) {
        assertTrue(Ascii.isControl(c), "'" + c + "' is a unprintable");
    }

    private void checkNotControl(final char c) {
        assertFalse(Ascii.isControl(c), "'" + c + "' is a NOT printable");
    }

    // Ascii.is ........................................................................................

    @Test
    public void testIsControlCharacter() {
        this.isAndCheck('\n', true);
    }

    @Test
    public void testIsAsciiLetter() {
        this.isAndCheck('A', true);
    }

    @Test
    public void testIsAsciiDigit() {
        this.isAndCheck('0', true);
    }

    @Test
    public void testIsNonAscii() {
        this.isAndCheck((char) 255, false);
    }

    private void isAndCheck(final char c, final boolean expected) {
        this.checkEquals(expected, Ascii.is(c), "Ascii.is " + CharSequences.quoteAndEscape(c));
    }

    // Ascii.isDigit ........................................................................................

    @Test
    public void testIsDigitLetter() {
        this.isDigitAndCheck('a', false);
    }

    @Test
    public void testIsDigitCharacterBeforeZero() {
        this.isDigitAndCheck('0' - 1, false);
    }

    @Test
    public void testIsDigitZero() {
        this.isDigitAndCheck('0', true);
    }

    @Test
    public void testIsDigitTwo() {
        this.isDigitAndCheck('2', true);
    }

    @Test
    public void testIsDigitNine() {
        this.isDigitAndCheck('9', true);
    }

    @Test
    public void testIsDigitAfterNine() {
        this.isDigitAndCheck('9' + 1, false);
    }

    @Test
    public void testIsDigitNonAsciiNumber() {
        char c = 0;
        for (int i = 255; i < 65536; i++) {
            if (Character.isDigit(c)) {
                c = (char) i;
                break;
            }
        }
        assertFalse(c != 0, "didnt find non ascii digit");
        this.isDigitAndCheck(c, false);
    }

    private void isDigitAndCheck(final int c, final boolean expected) {
        this.checkEquals(expected,
            Ascii.isDigit((char) c),
            "isLetter " + CharSequences.quoteAndEscape((char) c));
    }

    // test

    @Override
    public Class<Ascii> type() {
        return Ascii.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }
}
