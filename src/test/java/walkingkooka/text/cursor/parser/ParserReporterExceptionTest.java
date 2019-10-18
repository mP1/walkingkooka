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

package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.cursor.TextCursorLineInfo;
import walkingkooka.text.cursor.TextCursorLineInfos;
import walkingkooka.text.cursor.TextCursors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ParserReporterExceptionTest implements ClassTesting2<ParserReporterException> {

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithNullMessageFails() {
        assertThrows(NullPointerException.class, () -> new ParserReporterException(null, TextCursorLineInfos.fake()));
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithEmptyMessageFails() {
        assertThrows(IllegalArgumentException.class, () -> new ParserReporterException("", TextCursorLineInfos.fake()));
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithBlankMessageFails() {
        assertThrows(IllegalArgumentException.class, () -> new ParserReporterException("   ", TextCursorLineInfos.fake()));
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    public void testWithNullLineInfoFails() {
        assertThrows(NullPointerException.class, () -> new ParserReporterException("message!", null));
    }

    @Test
    public void testWith() {
        final TextCursorLineInfo info = TextCursors.charSequence("").lineInfo();
        final ParserReporterException exception = new ParserReporterException("message", info);
        assertEquals(info, exception.lineInfo(), "lineInfo");
    }

    @Override
    public void testAllConstructorsVisibility() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<ParserReporterException> type() {
        return ParserReporterException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
