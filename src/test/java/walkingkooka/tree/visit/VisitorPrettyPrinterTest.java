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
 *
 */

package walkingkooka.tree.visit;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.io.printer.IndentingPrinters;
import walkingkooka.io.printer.Printers;
import walkingkooka.test.PublicClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public final class VisitorPrettyPrinterTest extends PublicClassTestCase<VisitorPrettyPrinter<Object>> {

    @Test(expected = NullPointerException.class)
    public void testWithNullPrinterFails() {
        VisitorPrettyPrinter.with(null, this.indentation(), this.typeName());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullIdentationFails() {
        VisitorPrettyPrinter.with(this.printer(), null, this.typeName());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullTypeNameFails() {
        VisitorPrettyPrinter.with(this.printer(), this.indentation(), null);
    }

    @Test
    public void testTree() {
        final StringBuilder b = new StringBuilder();
        final VisitorPrettyPrinter<Object> printer = this.prettyPrinter(b);

        printer.enter("1");

        printer.enter("2");
        printer.leaf("3");
        printer.exit("2");

        printer.enter("4");
        printer.leaf("5");
        printer.exit("4");

        printer.exit("1");

        assertEquals("1\n  2\n    3=3\n  4\n    5=5\n", b.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testComputeFromClassSimpleNameObjectNullFails() {
        VisitorPrettyPrinter.computeFromClassSimpleName(null, "prefix", "suffix");
    }

    @Test(expected = NullPointerException.class)
    public void testComputeFromClassSimpleNamePrefixNullFails() {
        VisitorPrettyPrinter.computeFromClassSimpleName(new Object(), null, "suffix");
    }

    @Test(expected = NullPointerException.class)
    public void testComputeFromClassSimpleNameSuffixNullFails() {
        VisitorPrettyPrinter.computeFromClassSimpleName(new Object(), "prefix", null);
    }

    @Test
    public void testComputeFromClassSimpleNameWithoutPrefixWithoutSuffix() {
        this.computeFromClassSimpleNameAndCheck(new Object(),
                "<",
                ">",
                Object.class.getSimpleName());
    }

    @Test
    public void testComputeFromClassSimpleNameWithPrefix() {
        this.computeFromClassSimpleNameAndCheck(new Object(),
                "O",
                ">",
                "bject");
    }

    @Test
    public void testComputeFromClassSimpleNameWithSuffix() {
        this.computeFromClassSimpleNameAndCheck(new Object(),
                "<",
                "ct",
                "Obje");
    }

    @Test
    public void testComputeFromClassSimpleNameWithPrefixAndSuffix() {
        this.computeFromClassSimpleNameAndCheck(new Object(),
                "Ob",
                "ct",
                "je");
    }

    @Test
    public void testComputeFromClassSimpleNameWithPrefixAndSuffix2() {
        this.computeFromClassSimpleNameAndCheck("Hello String",
                "St",
                "g",
                "rin");
    }

    @Test
    public void testComputeFromClassSimpleNameWithPrefixAndSuffix3() {
        this.computeFromClassSimpleNameAndCheck(this,
                "V",
                "Test",
                "isitorPrettyPrinter");
    }

    private void computeFromClassSimpleNameAndCheck(final Object object,
                                                    final String prefix,
                                                    final String suffix,
                                                    final String expected) {
        assertEquals("object: " + object.getClass().getSimpleName() +
                " prefix=" + CharSequences.quoteAndEscape(prefix) +
                " suffix: " + CharSequences.quoteAndEscape(suffix),
                expected,
                VisitorPrettyPrinter.computeFromClassSimpleName(object, prefix, suffix));
    }

    @Test
    public void testToString() {
        final StringBuilder b = new StringBuilder();
        b.append("ABC");
        assertEquals(b.toString(), this.prettyPrinter(b).toString());
    }

    private VisitorPrettyPrinter<Object> prettyPrinter(final StringBuilder b) {
        return VisitorPrettyPrinter.with(
                IndentingPrinters.printer(Printers.stringBuilder(b, LineEnding.NL)),
                this.indentation(),
                this.typeName());
    }

    private IndentingPrinter printer() {
        return IndentingPrinters.fake();
    }

    private Indentation indentation() {
        return Indentation.with("  ");
    }

    private final Function<Object, String> typeName() {
        return (o -> o.toString());
    }

    @Override
    protected Class<VisitorPrettyPrinter<Object>> type() {
        return Cast.to(VisitorPrettyPrinter.class);
    }
}
