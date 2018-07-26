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

package walkingkooka.text.cursor.parser.ebnf.combinator;

import walkingkooka.Context;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRuleParserToken;

import java.util.Map;
import java.util.Objects;

/**
 * A context that accepts a grammar and uses a {@link EbnfParserCombinatorSyntaxTreeTransformer} to optional combine and
 * replace parsers.<br>
 * Note currently it not used by any parsers, but does control part of the grammar to parsers processing.
 */
final public class EbnfParserCombinatorContext<C extends ParserContext> implements Context {

    /**
     * Ctor
     */
    EbnfParserCombinatorContext(final EbnfGrammarParserToken grammar,
                                final Map<EbnfIdentifierParserToken, Parser<ParserToken, C>> identifierToParser,
                                final EbnfParserCombinatorSyntaxTreeTransformer<C> syntaxTreeTransformer) {
        this.grammar = grammar;

        this.identifierToParser = Maps.sorted();
        this.identifierToParser.putAll(identifierToParser);
        this.syntaxTreeTransformer = syntaxTreeTransformer;
    }

    private final EbnfGrammarParserToken grammar;
    final EbnfParserCombinatorSyntaxTreeTransformer<C> syntaxTreeTransformer;

    /**
     * Controls the entire transformation process.
     */
    Map<EbnfIdentifierParserToken, Parser<ParserToken, C>> process() {
       this.grammar.checkIdentifierReferencesExist(this.identifierToParser.keySet());
       this.preloadProxies();
       new EbnfParserCombinatorParserCompilingEbnfParserTokenVisitor(this).accept(this.grammar);
       return Maps.readOnly(this.identifierToParser);
    }

    /**
     * Fill the {@link #identifierToParser} with proxies, allowing forward references in the grammar.
     */
    private void preloadProxies() {
        this.grammar.value()
                .stream()
                .forEach(t -> {
                    final EbnfRuleParserToken rule = t.cast();
                    this.identifierToParser.put(rule.identifier(), new EbnfParserCombinatorProxyParser(rule.identifier()));
                });
    }

    /**
     * Returns the {@link Parser} for the given {@link EbnfIdentifierParserToken}.
     */
    public Parser<ParserToken, C> parser(final EbnfIdentifierParserToken identifier) {
        Objects.requireNonNull(identifier, "identifier");
        return this.identifierToParser.get(identifier);
    }

    /**
     * Identifier to token.
     */
    final Map<EbnfIdentifierParserToken, Parser<ParserToken, C>> identifierToParser;

    @Override
    public String toString() {
        return this.grammar.toString();
    }
}
