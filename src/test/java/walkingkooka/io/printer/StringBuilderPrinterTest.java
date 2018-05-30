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

package walkingkooka.io.printer;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.text.LineEnding;

final public class StringBuilderPrinterTest extends PrinterTestCase2<StringBuilderPrinter> {

    // constants

    private final static StringBuilder STRING_BUILDER = new StringBuilder();

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testWithNullStringBuilderFails() {
        this.withFails(null, StringBuilderPrinterTest.LINE_ENDING);
    }

    @Test
    public void testWithNullLineEndingFails() {
        this.withFails(StringBuilderPrinterTest.STRING_BUILDER, null);
    }

    private void withFails(final StringBuilder builder, final LineEnding lineEnding) {
        try {
            StringBuilderPrinter.with(builder, lineEnding);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Override
    @Test
    public void testPrintNullFails() {
        // nop
    }

    @Test
    public void testPrint() {
        this.printAndCheck(new CharSequence[]{"1", "23", "456"}, "123456");
    }

    @Override
    protected StringBuilderPrinter createPrinter(final StringBuilder target) {
        return StringBuilderPrinter.with(target, StringBuilderPrinterTest.LINE_ENDING);
    }

    @Override
    protected Class<StringBuilderPrinter> type() {
        return StringBuilderPrinter.class;
    }
}
