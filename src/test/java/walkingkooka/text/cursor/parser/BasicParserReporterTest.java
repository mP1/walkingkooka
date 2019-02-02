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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;

public final class BasicParserReporterTest extends ParserReporterTestCase<BasicParserReporter<StringParserToken, FakeParserContext>, StringParserToken, FakeParserContext>{

    @Test
    public void testReport() {
        // has a dependency on the results of TextCursorLineInfo methods...
        this.reportAndCheck("abc def ghi", Parsers.fake().setToString("ABC").cast(), "Unrecognized character 'a' at (1,1) \"abc def ghi\" expected ABC");
    }

    @Test
    public void testReport2() {
       // has a dependency on the results of TextCursorLineInfo methods...
        final TextCursor cursor = TextCursors.charSequence("abc def ghi");
        cursor.next();
        cursor.next();

        this.reportAndCheck(cursor, Parsers.fake().setToString("ABC").cast(), "Unrecognized character 'c' at (3,1) \"abc def ghi\" expected ABC");
    }

    @Test
    public void testReportEmptyTextCursor() {
        final TextCursor cursor = TextCursors.charSequence("abc");
        cursor.end();
        this.reportAndCheck(cursor, Parsers.fake().setToString("XYZ").cast(), "End of text at (4,1) \"abc\" expected XYZ");
    }

    @Override
    protected BasicParserReporter<StringParserToken, FakeParserContext> createParserReporter() {
        return BasicParserReporter.get();
    }

    @Override
    protected FakeParserContext createContext() {
        return new FakeParserContext();
    }

    @Override
    protected Class<BasicParserReporter<StringParserToken, FakeParserContext>> type() {
        return Cast.to(BasicParserReporter.class);
    }
}
