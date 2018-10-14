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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.cursor.TextCursors;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class ReportingParserTest extends ParserTestCase2<ReportingParser<ParserToken, ParserContext>, ParserToken> {

    private final static ParserReporterCondition CONDITION = ParserReporterCondition.ALWAYS;

    @Test(expected = NullPointerException.class)
    public void testWithNullConditionFails() {
        ReportingParser.with(null, this.reporter(), this.parser2());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullReporterFails() {
        ReportingParser.with(CONDITION, null, this.parser2());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullParserFails() {
        ReportingParser.with(CONDITION, this.reporter(), null);
    }

    @Test
    public void testReportFails() {
        this.parseThrows("!", "Unrecognized");
    }

    @Test
    public void testNotEmptyConditionCursorEmptyNotFails() {
        this.parseThrows(this.createParser(ParserReporterCondition.NOT_EMPTY), "!", "Unrecognized");
    }

    @Test
    public void testNotEmptyConditionCursorNotEmptyParserFail() {
        this.parseThrows(this.createParser(ParserReporterCondition.NOT_EMPTY), "!", "Unrecognized");
    }

    @Test
    public void testNotEmptyConditionCursorNotEmptyParserSuccessful() {
        this.parseAndCheck(this.createParser(ParserReporterCondition.NOT_EMPTY),
                "A",
                ParserTokens.character('A',"A"),
                "A");
    }

    @Test
    public void testNotEmptyConditionCursorEmpty() {
        assertEquals(Optional.empty(), this.createParser(ParserReporterCondition.NOT_EMPTY).parse(TextCursors.charSequence(""), this.createContext()));
    }

    @Test
    @Override
    public void testEmptyCursorFail() {
        this.parseThrows("!", "Unrecognized");
    }

    @Test
    public void testToString() {
        assertEquals(this.reporter().toString(), this.createParser().toString());
    }

    @Override
    protected ReportingParser<ParserToken, ParserContext> createParser() {
        return this.createParser(ParserReporterCondition.ALWAYS);
    }

    private ReportingParser<ParserToken, ParserContext> createParser(final ParserReporterCondition condition) {
        return ReportingParser.with(condition, this.reporter(), this.parser2());
    }

    private ParserReporter<ParserToken, ParserContext> reporter() {
        return ParserReporters.basic();
    }

    private Parser<ParserToken, ParserContext> parser2() {
        return Parsers.character(CharPredicates.letter()).cast();
    }

    @Override
    protected Class<ReportingParser<ParserToken, ParserContext>> type() {
        return Cast.to(ReportingParser.class);
    }
}
