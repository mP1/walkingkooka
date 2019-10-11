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

package walkingkooka.text.printer;

import org.junit.jupiter.api.Test;
import walkingkooka.text.LineEnding;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class StringBuilderPrinterTest extends PrinterTestCase2<StringBuilderPrinter> {

    // constants

    private final static StringBuilder STRING_BUILDER = new StringBuilder();

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testWithNullStringBuilderFails() {
        assertThrows(NullPointerException.class, () -> StringBuilderPrinter.with(null, LINE_ENDING));
    }

    @Test
    public void testWithNullLineEndingFails() {
        assertThrows(NullPointerException.class, () -> StringBuilderPrinter.with(STRING_BUILDER, null));
    }

    @Override
    public void testPrintNullFails() {
        // nop
    }

    @Test
    public void testPrint() {
        this.printAndCheck(new CharSequence[]{"1", "23", "456"}, "123456");
    }

    @Override
    public StringBuilderPrinter createPrinter(final StringBuilder target) {
        return StringBuilderPrinter.with(target, LINE_ENDING);
    }

    @Override
    public Class<StringBuilderPrinter> type() {
        return StringBuilderPrinter.class;
    }
}
