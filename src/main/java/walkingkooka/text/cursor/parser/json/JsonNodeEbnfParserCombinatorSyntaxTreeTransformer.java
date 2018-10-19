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
        return parser.transform(this::concatenation);
    }

    private ParserToken concatenation(final SequenceParserToken sequence, final ParserContext context) {
        return sequence;
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
     * If the identifier name ends in "REQUIRED" mark the parser so that it reports a failure.
     */
    @Override
    public Parser<ParserToken, ParserContext> identifier(final EbnfIdentifierParserToken token,
                                                         final Parser<ParserToken, ParserContext> parser) {
        final EbnfIdentifierName name = token.value();
        return name.equals(JsonNodeParsers.ARRAY_IDENTIFIER) ?
                parser.transform(this::array) :
                name.equals(JsonNodeParsers.OBJECT_IDENTIFIER) ?
                        parser.transform(this::object) :
                        this.requiredCheck(name, parser);
    }

    private ParserToken array(final ParserToken token, final ParserContext context) {
        return JsonNodeParserToken.array(this.clean(token.cast()),
                token.text());
    }

    private ParserToken object(final ParserToken token, final ParserContext context) {
        return JsonNodeParserToken.object(this.clean(token.cast()),
                token.text());
    }

    private List<ParserToken> clean(final SequenceParserToken token) {
        return token.flat().value();
    }

    private Parser<ParserToken, ParserContext> requiredCheck(final EbnfIdentifierName name, final Parser<ParserToken, ParserContext> parser) {
        return name.value().endsWith("REQUIRED") || JsonNodeParsers.REPORT_FAILURE_IDENTIFIER_NAMES.contains(name) ?
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
