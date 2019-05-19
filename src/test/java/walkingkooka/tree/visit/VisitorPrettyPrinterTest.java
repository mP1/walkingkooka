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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.io.printer.IndentingPrinters;
import walkingkooka.io.printer.Printers;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.type.MemberVisibility;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class VisitorPrettyPrinterTest implements ClassTesting2<VisitorPrettyPrinter<Object>>,
        ToStringTesting<VisitorPrettyPrinter<Object>> {

    @Test
    public void testWithNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> {
            VisitorPrettyPrinter.with(null, this.indentation(), this.typeName());
        });
    }

    @Test
    public void testWithNullIdentationFails() {
        assertThrows(NullPointerException.class, () -> {
            VisitorPrettyPrinter.with(this.printer(), null, this.typeName());
        });
    }

    @Test
    public void testWithNullTypeNameFails() {
        assertThrows(NullPointerException.class, () -> {
            VisitorPrettyPrinter.with(this.printer(), this.indentation(), null);
        });
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

    @Test
    public void testComputeFromClassSimpleNameObjectNullFails() {
        assertThrows(NullPointerException.class, () -> {
            VisitorPrettyPrinter.computeFromClassSimpleName(null, "prefix", "suffix");
        });
    }

    @Test
    public void testComputeFromClassSimpleNamePrefixNullFails() {
        assertThrows(NullPointerException.class, () -> {
            VisitorPrettyPrinter.computeFromClassSimpleName(new Object(), null, "suffix");
        });
    }

    @Test
    public void testComputeFromClassSimpleNameSuffixNullFails() {
        assertThrows(NullPointerException.class, () -> {
            VisitorPrettyPrinter.computeFromClassSimpleName(new Object(), "prefix", null);
        });
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
        assertEquals(expected,
                VisitorPrettyPrinter.computeFromClassSimpleName(object, prefix, suffix),
                () -> "object: " + object.getClass().getSimpleName() +
                        " prefix=" + CharSequences.quoteAndEscape(prefix) +
                        " suffix: " + CharSequences.quoteAndEscape(suffix));
    }

    @Test
    public void testToString() {
        final StringBuilder b = new StringBuilder();
        b.append("ABC");
        this.toStringAndCheck(this.prettyPrinter(b), b.toString());
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

    private Function<Object, String> typeName() {
        return (o -> o.toString());
    }

    @Override
    public Class<VisitorPrettyPrinter<Object>> type() {
        return Cast.to(VisitorPrettyPrinter.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
