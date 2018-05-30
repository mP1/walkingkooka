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
import walkingkooka.text.Indentation;

abstract public class IndentingPrinterTestCase<P extends IndentingPrinter>
        extends PrinterTestCase2<P> {

    protected IndentingPrinterTestCase() {
        super();
    }

    // constants

    private final static Indentation INDENTATION = Indentation.with("  ");

    // tests

    @Override
    @Test
    public void testNaming() {
        this.checkNaming(IndentingPrinter.class);
    }

    @Test(expected = NullPointerException.class)
    public void testIndentWithNullFails() {
        final P printer = this.createPrinter();
        printer.indent(null);
    }

    @Test
    public void testUnmatchedOutdentFails() {
        this.outdentFails(this.createPrinter());
    }

    @Test
    public void testTooManyOutdentsFails() {
        this.outdentFails(this.createPrinterIdentAndOutdent(1));
    }

    @Test
    public void testTooManyOutdentsFails2() {
        this.outdentFails(this.createPrinterIdentAndOutdent(2));
    }

    private P createPrinterIdentAndOutdent(final int count) {
        final P printer = this.createPrinter();
        for (int i = 0; i < count; i++) {
            printer.indent(IndentingPrinterTestCase.INDENTATION);
            printer.outdent();
        }
        return printer;
    }

    private void outdentFails(final P printer) {
        try {
            printer.outdent();
            Assert.fail();
        } catch (final IllegalStateException expected) {
        }
    }
}
