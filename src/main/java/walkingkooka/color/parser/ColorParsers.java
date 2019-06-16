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
package walkingkooka.color.parser;

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.cursor.parser.DoubleParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarLoader;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.type.PublicStaticHelper;

import java.util.Map;
import java.util.Optional;

/**
 * A collection of factory methods to create parsers.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/color"></a>
 * <pre>
 * <color>
 * where
 * &lt;color&gt; = &lt;rgb()&gt; | &lt;rgba()&gt; | &lt;hsl()&gt; | &lt;hsla()&gt; | &lt;hex-color&gt; | &lt;named-color&gt; | currentcolor | &lt;deprecated-system-color&gt;
 *
 * where
 * &lt;rgb()&gt; = rgb( &lt;percentage&gt;{3} [ / &lt;alpha-value&gt; ]? ) | rgb( &lt;number&gt;{3} [ / &lt;alpha-value&gt; ]? ) | rgb( &lt;percentage&gt;#{3} , &lt;alpha-value&gt;? ) | rgb( &lt;number&gt;#{3} , &lt;alpha-value&gt;? )
 * &lt;rgba()&gt; = rgba( &lt;percentage&gt;{3} [ / &lt;alpha-value&gt; ]? ) | rgba( &lt;number&gt;{3} [ / &lt;alpha-value&gt; ]? ) | rgba( &lt;percentage&gt;#{3} , &lt;alpha-value&gt;? ) | rgba( &lt;number&gt;#{3} , &lt;alpha-value&gt;? )
 * &lt;hsl()&gt; = hsl( &lt;hue&gt; &lt;percentage&gt; &lt;percentage&gt; [ / &lt;alpha-value&gt; ]? ) | hsl( &lt;hue&gt;, &lt;percentage&gt;, &lt;percentage&gt;, &lt;alpha-value&gt;? )
 * &lt;hsla()&gt; = hsla( &lt;hue&gt; &lt;percentage&gt; &lt;percentage&gt; [ / &lt;alpha-value&gt; ]? ) | hsla( &lt;hue&gt;, &lt;percentage&gt;, &lt;percentage&gt;, &lt;alpha-value&gt;? )
 *
 * where
 * &lt;alpha-value&gt; = &lt;number&gt; | &lt;percentage&gt;
 * &lt;hue&gt; = &lt;number&gt; | &lt;angle&gt;
 * </pre>
 */
public final class ColorParsers implements PublicStaticHelper {

    /**
     * Loads the grammar extracts parsers sets some {@link Parser} constants.
     */
    static {
        try {
            final Optional<EbnfGrammarParserToken> grammar = EbnfGrammarLoader.with("color-parsers.grammar", ColorParsers.class)
                    .grammar();

            final Map<EbnfIdentifierName, Parser<ParserContext>> predefined = Maps.sorted();
            predefined.put(EbnfIdentifierName.with("NUMBER"), Parsers.doubleParser().transform(ColorParsers::transformNumber));

            final Map<EbnfIdentifierName, Parser<ParserContext>> result = grammar.get()
                    .combinator(predefined, ColorParsersEbnfParserCombinatorSyntaxTreeTransformer.INSTANCE);

            RGB_PARSER = result.get(EbnfIdentifierName.with("RGB_RGBA_FUNCTION"));
            HSL_PARSER = result.get(EbnfIdentifierName.with("HSL_HSLA_FUNCTION"));
            HSV_PARSER = result.get(EbnfIdentifierName.with("HSV_HSVA_FUNCTION"));

        } catch (final RuntimeException rethrow) {
            throw rethrow;
        } catch (final Exception cause) {
            throw new ColorParserException("Failed to init parsers from grammar file, message: " + cause.getMessage(), cause);
        }
    }

    private static ParserToken transformNumber(final ParserToken token, ParserContext context) {
        final DoubleParserToken doubleParserToken = Cast.to(token);
        return ColorFunctionParserToken.number(doubleParserToken.value(), doubleParserToken.text());
    }

    // hsl..............................................................................................................

    /**
     * A parser that handles
     * <pre>
     * hsl(359, 100%, 99%)
     * </pre>
     * into a {@link ColorFunctionFunctionParserToken}.
     */
    public static <C extends ParserContext> Parser<C> hsl() {
        return HSL_PARSER.cast();
    }

    private final static Parser<ParserContext> HSL_PARSER;

    // hsv..............................................................................................................

    /**
     * A parser that handles
     * <pre>
     * hsv(359, 100%, 99%)
     * </pre>
     * into a {@link ColorFunctionFunctionParserToken}.
     */
    public static <C extends ParserContext> Parser<C> hsv() {
        return HSV_PARSER.cast();
    }

    private final static Parser<ParserContext> HSV_PARSER;

    // ................................................................................................................

    /**
     * A parser that handles
     * <pre>
     * rgb(RR,GG,BB)
     * </pre>
     * into a {@link ColorFunctionFunctionParserToken}.
     */
    public static <C extends ParserContext> Parser<C> rgb() {
        return RGB_PARSER.cast();
    }

    private final static Parser<ParserContext> RGB_PARSER;

    // ................................................................................................................

    /**
     * Stop creation.
     */
    private ColorParsers() {
        throw new UnsupportedOperationException();
    }
}
