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

package walkingkooka.text.cursor.parser.spreadsheet;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
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
import java.util.stream.Collectors;

/**
 * A {@link EbnfParserCombinatorSyntaxTreeTransformer} that only transforms terminal and ranges into their corresponding {@link SpreadsheetParserToken} equivalents.
 * Processing of other tokens will be done after this process completes.
 */
final class SpreedsheetEbnfParserCombinatorSyntaxTreeTransformer implements EbnfParserCombinatorSyntaxTreeTransformer<SpreadsheetParserContext> {
    @Override
    public Parser<ParserToken, SpreadsheetParserContext> alternatives(final EbnfAlternativeParserToken token, final Parser<ParserToken, SpreadsheetParserContext> parser, final EbnfParserCombinatorContext context) {
        return parser;
    }

    @Override
    public Parser<ParserToken, SpreadsheetParserContext> concatenation(final EbnfConcatenationParserToken token, Parser<SequenceParserToken, SpreadsheetParserContext> parser, EbnfParserCombinatorContext context) {
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
    private ParserToken concatenation(final SequenceParserToken sequence, final SpreadsheetParserContext context) {
        ParserToken result;

        for(;;){
            final String text = sequence.text();
            final List<ParserToken> tokens = sequence.flat().removeMissing().value();

            SpreadsheetParserToken first = null;

            for(final Iterator<ParserToken> all = tokens.iterator(); all.hasNext();){
                first = all.next().cast();
                if(!first.isWhitespace()){
                    break;
                }
            }

            if(first.isFunctionName()){
                result = SpreadsheetParserToken.function(tokens, text);
                break;
            }
            if(first.isOpenParenthesisSymbol()) {
                result = SpreadsheetParserToken.group(tokens, text);
                break;
            }
            if(first.isMinusSymbol()) {
                result = SpreadsheetParserToken.negative(tokens, text);
                break;
            }

            final SpreadsheetParserToken last = tokens.get(tokens.size() -1).cast();
            if(last.isPercentSymbol()) {
                result = SpreadsheetParserToken.percentage(tokens, text);
                break;
            }

            if(first.isSymbol()) {
                result = sequence;
                break;
            }

            result = this.binaryOperandPrioritize(tokens, sequence);
            break;
        }

        return result;
    }

    private ParserToken binaryOperandPrioritize(final List<ParserToken> tokens, final SequenceParserToken parent) {
        List<ParserToken> prioritized = this.maybeExpandNegatives(tokens);

        for(int priority = SpreadsheetParserToken.HIGHEST_PRIORITY; priority > SpreadsheetParserToken.LOWEST_PRIORITY; priority--) {
            boolean changed;

            do {
                changed = false;
                int i = 0;
                for(ParserToken t : prioritized) {
                    final SpreadsheetParserToken s = t.cast();
                    if(s.operatorPriority() == priority) {
                        changed = true;

                        final int firstIndex = this.findNonWhitespaceSiblingToken(prioritized, i - 1, -1);
                        final int lastIndex = this.findNonWhitespaceSiblingToken(prioritized, i + 1, +1);

                        final List<ParserToken> binaryOperandTokens = Lists.array();
                        binaryOperandTokens.addAll(prioritized.subList(firstIndex, lastIndex + 1));

                        final String text = binaryOperandTokens.stream()
                                .map(b -> b.text())
                                .collect(Collectors.joining());

                        final List<ParserToken> replaced = Lists.array();
                        replaced.addAll(prioritized.subList(0, firstIndex));
                        replaced.add(s.binaryOperand(binaryOperandTokens, text));
                        replaced.addAll(prioritized.subList(lastIndex + 1, prioritized.size()));

                        prioritized = replaced;
                        break;
                    }
                    i++;
                }
            } while(changed && prioritized.size() > 1);
        }

        return prioritized.size() == 1 ?
               prioritized.get(0) :
               parent.setValue(prioritized);
    }

    /**
     * Expands any {@link SpreadsheetNegativeParserToken} into its core components, only if it doesnt follow another symbol.
     * This fixes the parsing "mistake" that converts any minus followed by a token into a {@link SpreadsheetNegativeParserToken}.
     */
    private List<ParserToken> maybeExpandNegatives(final List<ParserToken> tokens) {
        final List<ParserToken> expanded = Lists.array();
        boolean expand = false;

        for(ParserToken t : tokens) {
            final SpreadsheetParserToken s = t.cast();
            if(s.isWhitespace()) {
                expanded.add(t);
                continue;
            }

            if(s.isNegative() && expand) {
                final SpreadsheetNegativeParserToken negativeParserToken = s.cast();
                expanded.addAll(negativeParserToken.value());
                expand = true;
                continue;
            }
            expand = !s.isSymbol();
            expanded.add(s);
        }

        return expanded;
    }

    private int findNonWhitespaceSiblingToken(final List<ParserToken> tokens, final int startIndex, final int step) {
        int i = startIndex;
        for(;;) {
            final SpreadsheetParserToken token = tokens.get(i).cast();
            if(!token.isWhitespace()){
                break;
            }
            i = i + step;
        }
        return i;
    }

    @Override
    public Parser<ParserToken, SpreadsheetParserContext> exception(final EbnfExceptionParserToken token, final Parser<ParserToken, SpreadsheetParserContext> parser, final EbnfParserCombinatorContext context) {
        return parser;
    }

    @Override
    public Parser<ParserToken, SpreadsheetParserContext> group(final EbnfGroupParserToken token, final Parser<ParserToken, SpreadsheetParserContext> parser, final EbnfParserCombinatorContext context) {
        return parser;
    }

    @Override
    public Parser<ParserToken, SpreadsheetParserContext> identifier(final EbnfIdentifierParserToken token, final Parser<ParserToken, SpreadsheetParserContext> parser, final EbnfParserCombinatorContext context) {
        return parser; // leave as is...
    }

    @Override
    public Parser<ParserToken, SpreadsheetParserContext> optional(final EbnfOptionalParserToken token, final Parser<ParserToken, SpreadsheetParserContext> parser, final EbnfParserCombinatorContext context) {
        return parser;
    }

    /**
     * Accepts the bounds tokens and creates a {@link SpreadsheetRangeParserToken}
     */
    @Override
    public Parser<ParserToken, SpreadsheetParserContext> range(final EbnfRangeParserToken token, final Parser<SequenceParserToken, SpreadsheetParserContext> parser, final EbnfParserCombinatorContext contextd) {
        return parser.transform((sequenceParserToken, spreadsheetParserContext) -> SpreadsheetParserToken.range(Cast.to(sequenceParserToken.value()), sequenceParserToken.text()));
    }

    @Override
    public Parser<RepeatedParserToken, SpreadsheetParserContext> repeated(final EbnfRepeatedParserToken token, Parser<RepeatedParserToken, SpreadsheetParserContext> parser, EbnfParserCombinatorContext context) {
        return parser;
    }

    @Override
    public Parser<ParserToken, SpreadsheetParserContext> terminal(final EbnfTerminalParserToken token, final Parser<StringParserToken, SpreadsheetParserContext> parser, final EbnfParserCombinatorContext context) {
        throw new UnsupportedOperationException(token.toString());
    }
}
