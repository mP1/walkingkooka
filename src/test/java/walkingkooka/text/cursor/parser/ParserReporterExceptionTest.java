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

package walkingkooka.text.cursor.parser;

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.test.PublicClassTestCase;
import walkingkooka.text.cursor.TextCursorLineInfo;
import walkingkooka.text.cursor.TextCursorLineInfos;
import walkingkooka.text.cursor.TextCursors;

import static org.junit.Assert.assertEquals;

public final class ParserReporterExceptionTest extends PublicClassTestCase<ParserReporterException> {

    @Test(expected = NullPointerException.class)
    public void testWithNullMessageFails() {
        new ParserReporterException(null, TextCursorLineInfos.fake());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyMessageFails() {
        new ParserReporterException("", TextCursorLineInfos.fake());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithBlankMessageFails() {
        new ParserReporterException("   ", TextCursorLineInfos.fake());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullLineInfoFails() {
        new ParserReporterException("message!", null);
    }

    @Test
    public void testWith() {
        final TextCursorLineInfo info = TextCursors.charSequence("").lineInfo();
        final ParserReporterException exception = new ParserReporterException("message", info);
        assertEquals("lineInfo", info, exception.lineInfo());
    }

    @Test
    @Ignore
    public void testAllConstructorsVisibility() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Class<ParserReporterException> type() {
        return ParserReporterException.class;
    }
}
