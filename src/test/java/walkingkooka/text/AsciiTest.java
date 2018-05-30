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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.test.PublicStaticHelperTestCase;

import java.lang.reflect.Method;

final public class AsciiTest extends PublicStaticHelperTestCase<Ascii> {

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
        Assert.assertTrue("'" + c + "' is a letter", Ascii.isLetter(c));
    }

    private void checkNotLetter(final char c) {
        Assert.assertFalse("'" + c + "' is a NOT letter", Ascii.isLetter(c));
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
        this.checkDigit('0');
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
        Assert.assertTrue("'" + c + "' is a digit", Ascii.isDigit(c));
    }

    private void checkNotDigit(final char c) {
        Assert.assertFalse("'" + c + "' is a NOT digit", Ascii.isDigit(c));
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
        Assert.assertTrue("'" + c + "' is a printable", Ascii.isPrintable(c));
    }

    private void checkNotPrintable(final char c) {
        Assert.assertFalse("'" + c + "' is a NOT printable", Ascii.isPrintable(c));
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
        Assert.assertTrue("'" + c + "' is a unprintable", Ascii.isControl(c));
    }

    private void checkNotControl(final char c) {
        Assert.assertFalse("'" + c + "' is a NOT printable", Ascii.isControl(c));
    }

    // test

    @Override
    protected Class<Ascii> type() {
        return Ascii.class;
    }

    @Override
    protected boolean canHavePublicTypes(final Method method) {
        return false;
    }
}
