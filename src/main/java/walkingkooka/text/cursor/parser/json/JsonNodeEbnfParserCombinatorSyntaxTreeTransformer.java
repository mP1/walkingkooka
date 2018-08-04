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
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.RepeatedParserToken;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.text.cursor.parser.StringParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfAlternativeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfConcatenationParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfExceptionParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGroupParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfOptionalParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRangeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRepeatedParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfTerminalParserToken;
import walkingkooka.text.cursor.parser.ebnf.combinator.EbnfParserCombinatorContext;
import walkingkooka.text.cursor.parser.ebnf.combinator.EbnfParserCombinatorSyntaxTreeTransformer;

import java.util.Iterator;
import java.util.List;

/**
 * A {@link EbnfParserCombinatorSyntaxTreeTransformer} that only transforms terminal and ranges into their corresponding {@link JsonNodeParserToken} equivalents.
 * Processing of other tokens will be done after this process completes.
 */
final class JsonNodeEbnfParserCombinatorSyntaxTreeTransformer implements EbnfParserCombinatorSyntaxTreeTransformer<JsonNodeParserContext> {
    @Override
    public Parser<ParserToken, JsonNodeParserContext> alternatives(final EbnfAlternativeParserToken token, final Parser<ParserToken, JsonNodeParserContext> parser, final EbnfParserCombinatorContext context) {
        return parser;
    }

    @Override
    public Parser<ParserToken, JsonNodeParserContext> concatenation(final EbnfConcatenationParserToken token, Parser<SequenceParserToken, JsonNodeParserContext> parser, EbnfParserCombinatorContext context) {
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
    private ParserToken concatenation(final SequenceParserToken sequence, final JsonNodeParserContext context) {
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
    public Parser<ParserToken, JsonNodeParserContext> exception(final EbnfExceptionParserToken token, final Parser<ParserToken, JsonNodeParserContext> parser, final EbnfParserCombinatorContext context) {
        return parser;
    }

    @Override
    public Parser<ParserToken, JsonNodeParserContext> group(final EbnfGroupParserToken token, final Parser<ParserToken, JsonNodeParserContext> parser, final EbnfParserCombinatorContext context) {
        return parser;
    }

    @Override
    public Parser<ParserToken, JsonNodeParserContext> identifier(final EbnfIdentifierParserToken token, final Parser<ParserToken, JsonNodeParserContext> parser, final EbnfParserCombinatorContext context) {
        return parser; // leave as is...
    }

    @Override
    public Parser<ParserToken, JsonNodeParserContext> optional(final EbnfOptionalParserToken token, final Parser<ParserToken, JsonNodeParserContext> parser, final EbnfParserCombinatorContext context) {
        return parser;
    }

    @Override
    public Parser<ParserToken, JsonNodeParserContext> range(final EbnfRangeParserToken token, final Parser<SequenceParserToken, JsonNodeParserContext> parser, final EbnfParserCombinatorContext contextd) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Parser<RepeatedParserToken, JsonNodeParserContext> repeated(final EbnfRepeatedParserToken token, Parser<RepeatedParserToken, JsonNodeParserContext> parser, EbnfParserCombinatorContext context) {
        return parser;
    }

    @Override
    public Parser<ParserToken, JsonNodeParserContext> terminal(final EbnfTerminalParserToken token, final Parser<StringParserToken, JsonNodeParserContext> parser, final EbnfParserCombinatorContext context) {
        throw new UnsupportedOperationException(token.toString());
    }
}
