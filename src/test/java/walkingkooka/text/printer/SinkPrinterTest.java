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

final public class SinkPrinterTest extends PrinterTestCase<SinkPrinter> {

    @Override
    @Test
    public void testPrintNullFails() {
        // nop
    }

    @Test
    public void testPrintWorks() {
        SinkPrinter.INSTANCE.print("string");
    }

    @Override
    @Test
    public void testPrintAfterCloseFails() {
        // nop
    }

    @Override
    @Test
    public void testLineEndingAfterCloseFails() {
        // nop
    }

    @Override
    @Test
    public void testFlushAfterCloseFails() {
        // nop
    }

    @Test
    public void testToString() {
        checkEquals("sink", SinkPrinter.INSTANCE.toString());
    }

    @Override
    public SinkPrinter createPrinter() {
        return SinkPrinter.INSTANCE;
    }

    @Override
    public Class<SinkPrinter> type() {
        return SinkPrinter.class;
    }
}
