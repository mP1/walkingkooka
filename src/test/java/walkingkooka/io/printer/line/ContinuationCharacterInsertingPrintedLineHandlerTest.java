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

package walkingkooka.io.printer.line;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.text.LineEnding;

final public class ContinuationCharacterInsertingPrintedLineHandlerTest
        extends PrintedLineHandlerTestCase<ContinuationCharacterInsertingPrintedLineHandler> {

    // constants

    private final static int WIDTH = 5;

    private final static char CONTINUATION = '*';

    // tests

    @Test
    public void testWithInvalidWidthFails() {
        this.withFails(0, ContinuationCharacterInsertingPrintedLineHandlerTest.CONTINUATION);
    }

    @Test
    public void testWithCarriageReturnContinuationCharacterFails() {
        this.withFails(ContinuationCharacterInsertingPrintedLineHandlerTest.WIDTH, '\r');
    }

    @Test
    public void testWithNewLineContinuationCharacterFails() {
        this.withFails(ContinuationCharacterInsertingPrintedLineHandlerTest.WIDTH, '\n');
    }

    private void withFails(final int width, final char c) {
        this.withFails(width, c, null);
    }

    private void withFails(final int width, final char c, final String message) {
        try {
            ContinuationCharacterInsertingPrintedLineHandler.with(width, c);
            Assert.fail();
        } catch (final RuntimeException expected) {
            if (null != message) {
                Assert.assertEquals("message",
                        ContinuationCharacterInsertingPrintedLineHandler.mustNotBeEndOfLineCharacter(
                                c),
                        expected.getMessage());
            }
        }
    }

    @Test
    public void testNotSplitBecauseNotLongEnough() {
        this.linePrintedAndCheck(10, "line", LineEnding.CR, "line@");
    }

    @Test
    public void testExactCr() {
        this.linePrintedAndCheck(3, "123", LineEnding.CR, "123@");
    }

    @Test
    public void testExactNl() {
        this.linePrintedAndCheck(3, "123", LineEnding.NL, "123@");
    }

    @Test
    public void testAlmost() {
        this.linePrintedAndCheck(3, "12", LineEnding.CR, "12@");
    }

    @Test
    public void testBrokenUpIntoTwoLinesCr() {
        this.linePrintedAndCheck(6, "12345678", LineEnding.CR, "12345*@678@");
    }

    @Test
    public void testBrokenUpIntoTwoLinesNl() {
        this.linePrintedAndCheck(6, "12345678", LineEnding.NL, "12345*@678@");
    }

    @Test
    public void testBrokenUpIntoTwoLinesCrNl() {
        this.linePrintedAndCheck(6, "12345678", LineEnding.CRNL, "12345*@678@");
    }

    @Test
    public void testBrokenUpIntoManyLinesCr() {
        this.linePrintedAndCheck(4, "123456789", LineEnding.CR, "123*@456*@789@");
    }

    @Test
    public void testBrokenUpIntoManyLinesNl() {
        this.linePrintedAndCheck(4, "123456789", LineEnding.NL, "123*@456*@789@");
    }

    @Test
    public void testBrokenUpIntoManyLinesCrNl() {
        this.linePrintedAndCheck(4, "123456789", LineEnding.CRNL, "123*@456*@789@");
    }

    @Test
    public void testBrokenUpIntoManyLinesCr2() {
        this.linePrintedAndCheck(4, "12345678", LineEnding.CR, "123*@456*@78@");
    }

    @Test
    public void testBrokenUpIntoManyLinesNl2() {
        this.linePrintedAndCheck(4, "12345678", LineEnding.NL, "123*@456*@78@");
    }

    @Test
    public void testBrokenUpIntoManyLinesCrNl2() {
        this.linePrintedAndCheck(4, "12345678", LineEnding.CRNL, "123*@456*@78@");
    }

    @Test
    public void testToString() {
        Assert.assertEquals("lines > " + ContinuationCharacterInsertingPrintedLineHandlerTest.WIDTH
                        + " continue w/ '"
                        + ContinuationCharacterInsertingPrintedLineHandlerTest.CONTINUATION + "'",
                this.createLineHandler().toString());
    }

    @Override
    protected ContinuationCharacterInsertingPrintedLineHandler createLineHandler() {
        return ContinuationCharacterInsertingPrintedLineHandler.with(
                ContinuationCharacterInsertingPrintedLineHandlerTest.WIDTH,
                ContinuationCharacterInsertingPrintedLineHandlerTest.CONTINUATION);
    }

    private void linePrintedAndCheck(final int width, final CharSequence line,
                                     final LineEnding lineEnding, final String expected) {
        this.linePrintedAndCheck(//
                ContinuationCharacterInsertingPrintedLineHandler.with(width,
                        ContinuationCharacterInsertingPrintedLineHandlerTest.CONTINUATION), //
                ContinuationCharacterInsertingPrintedLineHandlerTest.replacePlaceHolder(line,
                        lineEnding), //
                lineEnding, //
                ContinuationCharacterInsertingPrintedLineHandlerTest.replacePlaceHolder(expected,
                        lineEnding), //
                null);
    }

    private static String replacePlaceHolder(final CharSequence line, final LineEnding lineEnding) {
        return line.toString().replace("@", lineEnding);
    }

    @Override
    protected Class<ContinuationCharacterInsertingPrintedLineHandler> type() {
        return ContinuationCharacterInsertingPrintedLineHandler.class;
    }
}
