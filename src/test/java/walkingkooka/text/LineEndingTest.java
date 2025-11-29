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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class LineEndingTest implements ClassTesting2<LineEnding>,
    CharSequenceTesting<LineEnding>,
    ParseStringTesting<LineEnding> {

    // parse............................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseUnknownFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> LineEnding.parse("UNKNOWN LINE ENDING")
        );
    }

    @Test
    public void testParseEmpty() {
        assertSame(
            LineEnding.NONE,
            LineEnding.parse("")
        );
    }

    @Test
    public void testParseCrLowercase() {
        this.parseStringAndCheck(
            "cr",
            LineEnding.CR
        );
    }

    @Test
    public void testParseCrUpperCase() {
        this.parseStringAndCheck(
            "CR",
            LineEnding.CR
        );
    }

    @Test
    public void testParseBackslashCr() {
        this.parseStringAndCheck(
            "\r",
            LineEnding.CR
        );
    }

    @Test
    public void testParseCrlfLowerCase() {
        this.parseStringAndCheck(
            "crlf",
            LineEnding.CRNL
        );
    }

    @Test
    public void testParseCrNlUpperCase() {
        this.parseStringAndCheck(
            "CRLF",
            LineEnding.CRNL
        );
    }

    @Test
    public void testParseBackslashCrBackslashNl() {
        this.parseStringAndCheck(
            "\r\n",
            LineEnding.CRNL
        );
    }

    @Test
    public void testParseLfLowerCase() {
        this.parseStringAndCheck(
            "lf",
            LineEnding.NL
        );
    }

    @Test
    public void testParseLfUpperCase() {
        this.parseStringAndCheck(
            "LF",
            LineEnding.NL
        );
    }

    @Test
    public void testParseNlLowerCase() {
        this.parseStringAndCheck(
            "nl",
            LineEnding.NL
        );
    }

    @Test
    public void testParseNlUpperCase() {
        this.parseStringAndCheck(
            "NL",
            LineEnding.NL
        );
    }

    @Test
    public void testParseBackslashN() {
        this.parseStringAndCheck(
            "\n",
            LineEnding.NL
        );
    }

    @Override
    public LineEnding parseString(final String text) {
        return LineEnding.parse(text);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException expected) {
        return expected;
    }

    // LineEnding.......................................................................................................

    @Test
    public void testCr() {
        this.checkEquals("\r", LineEnding.CR.toString());
    }

    @Test
    public void testCrNl() {
        this.checkEquals("\r\n", LineEnding.CRNL.toString());
    }

    @Test
    public void testNl() {
        this.checkEquals("\n", LineEnding.NL.toString());
    }

    @Test
    public void testNone() {
        this.checkEquals("", LineEnding.NONE.toString());
    }

    @Test
    public void testSystem() {
        assertNotSame(LineEnding.SYSTEM, LineEnding.NONE);
    }

    @Override
    public LineEnding createCharSequence() {
        return LineEnding.CR;
    }

    // Object...........................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(LineEnding.NL);
    }

    @Override
    public LineEnding createObject() {
        return this.createCharSequence();
    }

    // class............................................................................................................

    @Override
    public Class<LineEnding> type() {
        return LineEnding.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
