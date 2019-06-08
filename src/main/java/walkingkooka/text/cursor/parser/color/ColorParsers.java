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
package walkingkooka.text.cursor.parser.color;

import walkingkooka.collect.map.Maps;
import walkingkooka.color.BlueColorComponent;
import walkingkooka.color.Color;
import walkingkooka.color.ColorComponent;
import walkingkooka.color.GreenColorComponent;
import walkingkooka.color.Hsl;
import walkingkooka.color.HslComponent;
import walkingkooka.color.Hsv;
import walkingkooka.color.HsvComponent;
import walkingkooka.color.HueHslComponent;
import walkingkooka.color.HueHsvComponent;
import walkingkooka.color.LightnessHslComponent;
import walkingkooka.color.RedColorComponent;
import walkingkooka.color.SaturationHslComponent;
import walkingkooka.color.SaturationHsvComponent;
import walkingkooka.color.ValueHsvComponent;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarLoader;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.text.cursor.parser.select.NodeSelectorParserException;
import walkingkooka.type.PublicStaticHelper;

import java.util.List;
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

    static final EbnfIdentifierName PREDICATE_IDENTIFIER = EbnfIdentifierName.with("PREDICATE");

    /**
     * Loads the grammar and sets some {@link Parser} constants.
     */
    static {
        try {
            final Optional<EbnfGrammarParserToken> grammar = EbnfGrammarLoader.with("color-parsers.grammar", ColorParsers.class)
                    .grammar();

            final Map<EbnfIdentifierName, Parser<ParserContext>> predefined = Maps.sorted();
            predefined.put(EbnfIdentifierName.with("SPACE"), Parsers.character(CharPredicates.is(' ')));
            predefined.put(EbnfIdentifierName.with("SEPARATOR"), Parsers.string(",", CaseSensitivity.SENSITIVE));
            predefined.put(EbnfIdentifierName.with("NUMBER"), Parsers.doubleParser());

            final Map<EbnfIdentifierName, Parser<ParserContext>> result = grammar.get()
                    .combinator(predefined, ColorParsersEbnfParserCombinatorSyntaxTreeTransformer.INSTANCE);

            RGB_PARSER = result.get(EbnfIdentifierName.with("RGB"))
                    .transform(ColorParsers::transformColor);
            HSL_PARSER = result.get(EbnfIdentifierName.with("HSL"))
                    .transform(ColorParsers::transformHsl);
            HSV_PARSER = result.get(EbnfIdentifierName.with("HSV"))
                    .transform(ColorParsers::transformHsv);

        } catch (final RuntimeException rethrow) {
            throw rethrow;
        } catch (final Exception cause) {
            throw new NodeSelectorParserException("Failed to init parsers from grammar file, message: " + cause.getMessage(), cause);
        }
    }

    private static ColorParserToken transformColor(final ParserToken token, final ParserContext context) {
        final List<Float> values = ColorParsersComponentsParserTokenVisitor.transform(token);

        final RedColorComponent red = ColorComponent.red(values.get(0).byteValue());
        final GreenColorComponent green = ColorComponent.green(values.get(1).byteValue());
        final BlueColorComponent blue = ColorComponent.blue(values.get(2).byteValue());

        Color color = Color.with(red, green, blue);
        if (values.size() == 4) {
            color = color.set(ColorComponent.alpha(values.get(3).byteValue()));
        }

        return ColorParserToken.with(color, token.text());
    }

    private static HslParserToken transformHsl(final ParserToken token, final ParserContext context) {
        final List<Float> values = ColorParsersComponentsParserTokenVisitor.transform(token);

        final HueHslComponent hue = HslComponent.hue(values.get(0));
        final SaturationHslComponent saturation = HslComponent.saturation(values.get(1));
        final LightnessHslComponent lightness = HslComponent.lightness(values.get(2));

        Hsl hsl = Hsl.with(hue, saturation, lightness);
        if (values.size() == 4) {
            hsl = hsl.set(HslComponent.alpha(values.get(3)));
        }

        return HslParserToken.with(hsl, token.text());
    }

    private static HsvParserToken transformHsv(final ParserToken token, final ParserContext context) {
        final List<Float> values = ColorParsersComponentsParserTokenVisitor.transform(token);

        final HueHsvComponent hue = HsvComponent.hue(values.get(0));
        final SaturationHsvComponent saturation = HsvComponent.saturation(values.get(1));
        final ValueHsvComponent value = HsvComponent.value(values.get(2));

        Hsv hsv = Hsv.with(hue, saturation, value);
        if (values.size() == 4) {
            hsv = hsv.set(HsvComponent.alpha(values.get(3)));
        }

        return HsvParserToken.with(hsv, token.text());
    }

    // hsl..............................................................................................................

    /**
     * A parser that handles
     * <pre>
     * hsl(359, 100%, 99%)
     * </pre>
     * into a {@link HslParserToken}.
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
     * into a {@link HsvParserToken}.
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
     * into a {@link ColorParserToken}.
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
