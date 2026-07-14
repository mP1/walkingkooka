
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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BinaryTextPrintingTest implements BinaryTextContextTesting,
    HashCodeEqualsDefinedTesting2<BinaryTextPrinting>,
    ToStringTesting<BinaryTextPrinting>,
    ClassTesting<BinaryTextPrinting> {

    private final static Charset DIFFERENT_CHARSET = StandardCharsets.ISO_8859_1;

    private final static Indentation DIFFERENT_INDENTATION = Indentation.SPACES4;

    private final static LineEnding DIFFERENT_LINE_ENDING = LineEnding.CR;

    // with.............................................................................................................

    @Test
    public void testWithNullCharsetFails() {
        assertThrows(
            NullPointerException.class,
            () -> BinaryTextPrinting.with(
                null,
                TEXT_PRINTING
            )
        );
    }

    @Test
    public void testWithNullTextPrintingFails() {
        assertThrows(
            NullPointerException.class,
            () -> BinaryTextPrinting.with(
                CHARSET,
                null
            )
        );
    }

    @Test
    public void testWith() {
        final BinaryTextPrinting binaryTextPrinting = BinaryTextPrinting.with(
            CHARSET,
            TEXT_PRINTING
        );

        this.charsetAndCheck(
            binaryTextPrinting,
            CHARSET
        );

        this.indentationAndCheck(
            binaryTextPrinting,
            INDENTATION
        );

        this.lineEndingAndCheck(
            binaryTextPrinting,
            LINE_ENDING
        );
    }

    // setCharset........................................................................................................

    @Test
    public void testSetCharsetWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject()
                .setCharset(null)
        );
    }

    @Test
    public void testSetCharsetSame() {
        final BinaryTextPrinting binaryTextPrinting = this.createObject();

        assertSame(
            binaryTextPrinting,
            binaryTextPrinting.setCharset(CHARSET)
        );
    }

    @Test
    public void testSetCharsetDifferent() {
        final BinaryTextPrinting binaryTextPrinting = this.createObject();

        final BinaryTextPrinting different = binaryTextPrinting.setCharset(DIFFERENT_CHARSET);

        assertNotSame(
            binaryTextPrinting,
            different
        );

        this.charsetAndCheck(
            different,
            DIFFERENT_CHARSET
        );

        this.indentationAndCheck(
            different,
            INDENTATION
        );

        this.lineEndingAndCheck(
            different,
            LINE_ENDING
        );

        this.charsetAndCheck(
            binaryTextPrinting,
            CHARSET
        );

        this.indentationAndCheck(
            binaryTextPrinting,
            INDENTATION
        );

        this.lineEndingAndCheck(
            binaryTextPrinting,
            LINE_ENDING
        );
    }

    // setIndentation...................................................................................................

    @Test
    public void testSetIndentationWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject()
                .setIndentation(null)
        );
    }

    @Test
    public void testSetIndentationSame() {
        final BinaryTextPrinting binaryTextPrinting = this.createObject();

        assertSame(
            binaryTextPrinting,
            binaryTextPrinting.setIndentation(INDENTATION)
        );
    }

    @Test
    public void testSetIndentationDifferent() {
        final BinaryTextPrinting binaryTextPrinting = this.createObject();

        final BinaryTextPrinting different = binaryTextPrinting.setIndentation(DIFFERENT_INDENTATION);

        assertNotSame(
            binaryTextPrinting,
            different
        );

        this.charsetAndCheck(
            binaryTextPrinting,
            CHARSET
        );

        this.indentationAndCheck(
            different,
            DIFFERENT_INDENTATION
        );

        this.lineEndingAndCheck(
            different,
            LINE_ENDING
        );

        this.indentationAndCheck(
            binaryTextPrinting,
            INDENTATION
        );

        this.lineEndingAndCheck(
            binaryTextPrinting,
            LINE_ENDING
        );
    }

    // setLineEnding....................................................................................................

    @Test
    public void testSetLineEndingWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject()
                .setLineEnding(null)
        );
    }

    @Test
    public void testSetLineEndingSame() {
        final BinaryTextPrinting binaryTextPrinting = this.createObject();

        assertSame(
            binaryTextPrinting,
            binaryTextPrinting.setLineEnding(LINE_ENDING)
        );
    }

    @Test
    public void testSetLineEndingDifferent() {
        final BinaryTextPrinting binaryTextPrinting = this.createObject();

        final BinaryTextPrinting different = binaryTextPrinting.setLineEnding(DIFFERENT_LINE_ENDING);

        assertNotSame(
            binaryTextPrinting,
            different
        );

        this.charsetAndCheck(
            binaryTextPrinting,
            CHARSET
        );

        this.indentationAndCheck(
            different,
            INDENTATION
        );

        this.lineEndingAndCheck(
            different,
            DIFFERENT_LINE_ENDING
        );

        this.indentationAndCheck(
            binaryTextPrinting,
            INDENTATION
        );

        this.lineEndingAndCheck(
            binaryTextPrinting,
            LINE_ENDING
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentCharset() {
        this.checkNotEquals(
            BinaryTextPrinting.with(
                DIFFERENT_CHARSET,
                TEXT_PRINTING
            )
        );
    }

    @Test
    public void testEqualsDifferentIndentation() {
        this.checkNotEquals(
            BinaryTextPrinting.with(
                CHARSET,
                TEXT_PRINTING.setIndentation(DIFFERENT_INDENTATION)
            )
        );
    }

    @Test
    public void testEqualsDifferentLineEnding() {
        this.checkNotEquals(
            BinaryTextPrinting.with(
                CHARSET,
                TEXT_PRINTING.setLineEnding(DIFFERENT_LINE_ENDING)
            )
        );
    }

    @Override
    public BinaryTextPrinting createObject() {
        return BinaryTextPrinting.with(
            CHARSET,
            TEXT_PRINTING
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createObject(),
            "charset=\"UTF-8\" indentation=\"  \" lineEnding=\"\\n\""
        );
    }

    // class............................................................................................................

    @Override
    public Class<BinaryTextPrinting> type() {
        return BinaryTextPrinting.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
