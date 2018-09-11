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

package walkingkooka.text.cursor.parser.json;

import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.RepeatedParserToken;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.text.cursor.parser.StringParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfAlternativeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfConcatenationParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfExceptionParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGroupParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfOptionalParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRangeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRepeatedParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfTerminalParserToken;
import walkingkooka.text.cursor.parser.ebnf.combinator.EbnfParserCombinatorSyntaxTreeTransformer;

import java.util.Iterator;
import java.util.List;

/**
 * A {@link EbnfParserCombinatorSyntaxTreeTransformer} that only transforms terminal and ranges into their corresponding {@link JsonNodeParserToken} equivalents.
 * Processing of other tokens will be done after this process completes.
 */
final class JsonNodeEbnfParserCombinatorSyntaxTreeTransformer implements EbnfParserCombinatorSyntaxTreeTransformer {
    @Override
    public Parser<ParserToken, ParserContext> alternatives(final EbnfAlternativeParserToken token, final Parser<ParserToken, ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserToken, ParserContext> concatenation(final EbnfConcatenationParserToken token, final Parser<SequenceParserToken, ParserContext> parser) {
        // needs to examine tokens and wrap.. might be a group, or other type of tokens.
        return parser.transform(this::concatenation);
    }

    /**
     * (* addition, subtraction, multiplication, division, power, range *)
     * BINARY_OPERATOR         = "+" | "-" | "*" | "/" | "^" | ":";
     * BINARY_EXPRESSION       = EXPRESSION2, [ WHITESPACE ], BINARY_OPERATOR, [ WHITESPACE ], EXPRESSION2;
     *
     * (* cell column/row OR label *)
     * CELL			        = COLUMN_ROW | LABEL_NAME;
     */
    private ParserToken concatenation(final SequenceParserToken sequence, final ParserContext context) {
        ParserToken result;

        for(;;){
            final String text = sequence.text();
            final SequenceParserToken flat = sequence.flat();
            final List<ParserToken> tokens = flat.removeMissing().value();

            JsonNodeParserToken first = null;

            for(final Iterator<ParserToken> all = tokens.iterator(); all.hasNext();){
                first = all.next().cast();
                if(!first.isWhitespace()){
                    break;
                }
            }

            if(null == first) {
                result = sequence;
                break;
            }

            if(first.isArrayBeginSymbol()) {
                result = JsonNodeParserToken.array(tokens, text);
                break;
            }
            if(first.isObjectBeginSymbol()) {
                result = JsonNodeParserToken.object(tokens, text);
                break;
            }

            result = sequence;
            break;
        }

        return result;
    }

    @Override
    public Parser<ParserToken, ParserContext> exception(final EbnfExceptionParserToken token, final Parser<ParserToken, ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserToken, ParserContext> group(final EbnfGroupParserToken token, final Parser<ParserToken, ParserContext> parser) {
        return parser;
    }

    /**
     * Any symbol with a name ending in "REQUIRED" or in {@link JsonNodeParsers#REPORT_FAILURE_IDENTIFIER_NAMES} will be marked as orreport, that is report a parse failure if the parser fails.
     */
    @Override
    public Parser<ParserToken, ParserContext> identifier(final EbnfIdentifierParserToken token, final Parser<ParserToken, ParserContext> parser) {
        final EbnfIdentifierName identifier =token.value();
        return identifier.value().endsWith("REQUIRED") || JsonNodeParsers.REPORT_FAILURE_IDENTIFIER_NAMES.contains(token.value()) ?
                parser.orReport(ParserReporters.basic()) :
                parser; // leave as is...
    }

    @Override
    public Parser<ParserToken, ParserContext> optional(final EbnfOptionalParserToken token, final Parser<ParserToken, ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserToken, ParserContext> range(final EbnfRangeParserToken token, final Parser<SequenceParserToken, ParserContext> parserd) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Parser<RepeatedParserToken, ParserContext> repeated(final EbnfRepeatedParserToken token, final Parser<RepeatedParserToken, ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserToken, ParserContext> terminal(final EbnfTerminalParserToken token, final Parser<StringParserToken, ParserContext> parser) {
        throw new UnsupportedOperationException(token.toString());
    }
}
