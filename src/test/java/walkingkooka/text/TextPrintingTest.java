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
import walkingkooka.HasCharsetTesting;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextPrintingTest implements TextContextTesting,
    HashCodeEqualsDefinedTesting2<TextPrinting>,
    HasCharsetTesting,
    ToStringTesting<TextPrinting>,
    ClassTesting<TextPrinting> {

    private final static Indentation INDENTATION = Indentation.SPACES2;

    private final static Indentation DIFFERENT_INDENTATION = Indentation.SPACES4;

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    private final static LineEnding DIFFERENT_LINE_ENDING = LineEnding.CR;

    // with.............................................................................................................

    @Test
    public void testWithNullIndentationFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextPrinting.with(
                null,
                LINE_ENDING
            )
        );
    }

    @Test
    public void testWithNullLineEndingFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextPrinting.with(
                INDENTATION,
                null
            )
        );
    }

    @Test
    public void testWith() {
        final TextPrinting textPrinting = TextPrinting.with(
            INDENTATION,
            LINE_ENDING
        );

        this.indentationAndCheck(
            textPrinting,
            INDENTATION
        );

        this.lineEndingAndCheck(
            textPrinting,
            LINE_ENDING
        );
    }

    // setCharset.......................................................................................................

    @Test
    public void testSetCharsetWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject()
                .setCharset(null)
        );
    }

    @Test
    public void testSetCharset() {
        final Charset charset = StandardCharsets.UTF_8;

        final BinaryTextPrinting binaryTextPrinting = this.createObject()
            .setCharset(charset);

        this.charsetAndCheck(
            binaryTextPrinting,
            charset
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
        final TextPrinting textPrinting = this.createObject();

        assertSame(
            textPrinting,
            textPrinting.setIndentation(INDENTATION)
        );
    }

    @Test
    public void testSetIndentationDifferent() {
        final TextPrinting textPrinting = this.createObject();

        final TextPrinting different = textPrinting.setIndentation(DIFFERENT_INDENTATION);

        assertNotSame(
            textPrinting,
            different
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
            textPrinting,
            INDENTATION
        );

        this.lineEndingAndCheck(
            textPrinting,
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
        final TextPrinting textPrinting = this.createObject();

        assertSame(
            textPrinting,
            textPrinting.setLineEnding(LINE_ENDING)
        );
    }

    @Test
    public void testSetLineEndingDifferent() {
        final TextPrinting textPrinting = this.createObject();

        final TextPrinting different = textPrinting.setLineEnding(DIFFERENT_LINE_ENDING);

        assertNotSame(
            textPrinting,
            different
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
            textPrinting,
            INDENTATION
        );

        this.lineEndingAndCheck(
            textPrinting,
            LINE_ENDING
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentIndentation() {
        this.checkNotEquals(
            TextPrinting.with(
                DIFFERENT_INDENTATION,
                LINE_ENDING
            )
        );
    }

    @Test
    public void testEqualsDifferentLineEnding() {
        this.checkNotEquals(
            TextPrinting.with(
                INDENTATION,
                DIFFERENT_LINE_ENDING
            )
        );
    }

    @Override
    public TextPrinting createObject() {
        return TextPrinting.with(
            INDENTATION,
            LINE_ENDING
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createObject(),
            "indentation=\"  \" lineEnding=\"\\n\""
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextPrinting> type() {
        return TextPrinting.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
