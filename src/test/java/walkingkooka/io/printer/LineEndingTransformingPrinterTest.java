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

package walkingkooka.io.printer;

import org.junit.jupiter.api.Test;
import walkingkooka.NeverError;
import walkingkooka.text.LineEnding;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class LineEndingTransformingPrinterTest extends PrinterTestCase2<LineEndingTransformingPrinter> {

    // constants

    private final static Printer PRINTER = Printers.fake();

    private final static Function<LineEnding, LineEnding> TRANFORMER
            = new Function<LineEnding, LineEnding>() {

        @Override
        public LineEnding apply(final LineEnding ending) {
            LineEnding result;

            for (; ; ) {
                if (LineEnding.CR == ending) {
                    result = LineEnding.CRNL;
                    break;
                }
                if (LineEnding.CRNL == ending) {
                    result = LineEnding.NL;
                    break;
                }
                if (LineEnding.NL == ending) {
                    result = LineEnding.CR;
                    break;
                }
                throw new NeverError("Unknown LineEnding=" + ending);
            }
            return result;
        }
    };

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testWithNullLineEndingTransformerFails() {
        assertThrows(NullPointerException.class, () -> {
            LineEndingTransformingPrinter.wrap(null, PRINTER);
        });
    }

    @Test
    public void testWithNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> {
            LineEndingTransformingPrinter.wrap(TRANFORMER, null);
        });
    }

    @Test
    public void testWithoutAnyLineEndings() {
        this.printAndCheck("123");
    }

    @Test
    public void testIncludesCr() {
        this.printAndCheck("B\rA", "B\r\nA");
    }

    @Test
    public void testIncludesCrCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("B\rA"), "B\r\nA");
    }

    @Test
    public void testIncludesNl() {
        this.printAndCheck("B\nA", "B\rA");
    }

    @Test
    public void testIncludesNlCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("B\nA"), "B\rA");
    }

    @Test
    public void testIncludesCrNl() {
        this.printAndCheck("B\r\nA", "B\nA");
    }

    @Test
    public void testIncludesCrNlCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("B\r\nA"), "B\nA");
    }

    @Test
    public void testIncludesManyCr() {
        this.printAndCheck("B\r*\rA", "B\r\n*\r\nA");
    }

    @Test
    public void testIncludesManyCrCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("B\r*\rA"), "B\r\n*\r\nA");
    }

    @Test
    public void testIncludesManyNl() {
        this.printAndCheck("B\n*\nA", "B\r*\rA");
    }

    @Test
    public void testIncludesManyNlCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("B\n*\nA"), "B\r*\rA");
    }

    @Test
    public void testIncludesManyCrNl() {
        this.printAndCheck("B\r\n*\r\nA", "B\n*\nA");
    }

    @Test
    public void testIncludesManyCrNlCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("B\r\n*\r\nA"), "B\n*\nA");
    }

    @Test
    public void testWithoutFlushPendingCrIsLost() {
        final LineEndingTransformingPrinter printer = LineEndingTransformingPrinter.wrap(
                TRANFORMER,
                Printers.fake());
        printer.print("\r");
    }

    @Test
    public void testEmptyLineCrCr() {
        this.printAndCheck("B\r\rA", "B\r\n\r\nA");
    }

    @Test
    public void testEmptyLineCrCrCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("B\r\rA"), "B\r\n\r\nA");
    }

    @Test
    public void testEmptyLineNlNl() {
        this.printAndCheck("B\n\nA", "B\r\rA");
    }

    @Test
    public void testEmptyLineNlNlCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("B\n\nA"), "B\r\rA");
    }

    @Test
    public void testEmptyLineCrNlCrNl() {
        this.printAndCheck("B\r\n\r\nA", "B\n\nA");
    }

    @Test
    public void testEmptyLineCrNlCrNlCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("B\r\n\r\nA"), "B\n\nA");
    }

    @Test
    public void testLineEndingsFilteredByTransformerReturnsNone() {
        final StringBuilder printed = new StringBuilder();
        this.printAndCheck(//
                LineEndingTransformingPrinter.wrap((lineEnding -> LineEnding.NONE),
                        Printers.stringBuilder(printed,
                                LINE_ENDING)), //
                "before\nAFTER", //
                printed, //
                "beforeAFTER");
    }

    @Test
    public void testToString() {
        final Printer printer = Printers.fake();
        checkEquals(TRANFORMER + " " + printer,
                LineEndingTransformingPrinter.wrap(TRANFORMER,
                        printer).toString());
    }

    @Override
    public LineEndingTransformingPrinter createPrinter(final StringBuilder target) {
        return LineEndingTransformingPrinter.wrap(TRANFORMER,
                Printers.stringBuilder(target, LINE_ENDING));
    }

    @Override
    public Class<LineEndingTransformingPrinter> type() {
        return LineEndingTransformingPrinter.class;
    }
}
