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

import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.type.PublicStaticHelper;

/**
 * A collection of factory methods to create parsers.
 */
public final class ColorParsers implements PublicStaticHelper {

    // hsl..............................................................................................................

    /**
     * A parser that handles
     * <pre>
     * hsl(359, 100%, 99%)
     * </pre>
     * into a {@link HslParserToken}.
     */
    public static <C extends ParserContext> Parser<C> hslFunction() {
        return HSL_FUNCTION_PARSER.cast();
    }

    private static ParserToken transformHslFunction(final ParserToken token, final ParserContext context) {
        return HslFunctionParserTokenVisitor.acceptParserToken(token);
    }

    private final static Parser<ParserContext> HSL_FUNCTION_PARSER = hslFunctionParser();

    private static Parser<ParserContext> hslFunctionParser() {
        final Parser<ParserContext> whitespace = Parsers.repeated(Parsers.character(CharPredicates.whitespace()));
        final Parser<ParserContext> component = Parsers.doubleParser();
        final Parser<ParserContext> percentage = Parsers.character(CharPredicates.is('%'));
        final Parser<ParserContext> comma = Parsers.character(CharPredicates.is(','));

        return Parsers.sequenceParserBuilder()
                .required(Parsers.string("hsl(", CaseSensitivity.SENSITIVE))
                .optional(whitespace) // hue
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // sat
                .required(component)
                .required(percentage)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // value
                .required(component)
                .required(percentage)
                .optional(whitespace)
                .required(Parsers.character(CharPredicates.is(')')))
                .build()
                .transform(ColorParsers::transformHslFunction)
                .setToString("hsl()");
    }

    // hsla..............................................................................................................

    /**
     * A parser that handles
     * <pre>
     * hsla(359, 100%, 99%, 50%)
     * </pre>
     * into a {@link HslParserToken}.
     */
    public static <C extends ParserContext> Parser<C> hslaFunction() {
        return HSLA_FUNCTION_PARSER.cast();
    }

    private static ParserToken transformHslaFunction(final ParserToken token, final ParserContext context) {
        return HslaFunctionParserTokenVisitor.acceptParserToken(token);
    }

    private final static Parser<ParserContext> HSLA_FUNCTION_PARSER = hslaFunctionParser();

    private static Parser<ParserContext> hslaFunctionParser() {
        final Parser<ParserContext> whitespace = Parsers.repeated(Parsers.character(CharPredicates.whitespace()));
        final Parser<ParserContext> component = Parsers.doubleParser();
        final Parser<ParserContext> percentage = Parsers.character(CharPredicates.is('%'));
        final Parser<ParserContext> comma = Parsers.character(CharPredicates.is(','));

        return Parsers.sequenceParserBuilder()
                .required(Parsers.string("hsla(", CaseSensitivity.SENSITIVE))
                .optional(whitespace) // hue
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // sat
                .required(component)
                .required(percentage)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // value
                .required(component)
                .required(percentage)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace)
                .required(component) // alpha
                .required(percentage)
                .optional(whitespace)
                .required(Parsers.character(CharPredicates.is(')')))
                .build()
                .transform(ColorParsers::transformHslaFunction)
                .setToString("hsla()");
    }

    // hsv..............................................................................................................

    /**
     * A parser that handles
     * <pre>
     * hsv(359, 100%, 99%)
     * </pre>
     * into a {@link HsvParserToken}.
     */
    public static <C extends ParserContext> Parser<C> hsvFunction() {
        return HSV_FUNCTION_PARSER.cast();
    }

    private static ParserToken transformHsvFunction(final ParserToken token, final ParserContext context) {
        return HsvFunctionParserTokenVisitor.acceptParserToken(token);
    }

    private final static Parser<ParserContext> HSV_FUNCTION_PARSER = hsvFunctionParser();

    private static Parser<ParserContext> hsvFunctionParser() {
        final Parser<ParserContext> whitespace = Parsers.repeated(Parsers.character(CharPredicates.whitespace()));
        final Parser<ParserContext> component = Parsers.doubleParser();
        final Parser<ParserContext> percentage = Parsers.character(CharPredicates.is('%'));
        final Parser<ParserContext> comma = Parsers.character(CharPredicates.is(','));

        return Parsers.sequenceParserBuilder()
                .required(Parsers.string("hsv(", CaseSensitivity.SENSITIVE))
                .optional(whitespace) // hue
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // sat
                .required(component)
                .required(percentage)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // value
                .required(component)
                .required(percentage)
                .optional(whitespace)
                .required(Parsers.character(CharPredicates.is(')')))
                .build()
                .transform(ColorParsers::transformHsvFunction)
                .setToString("hsv()");
    }

    // hsva..............................................................................................................

    /**
     * A parser that handles
     * <pre>
     * hsva(359, 100%, 99%, 50%)
     * </pre>
     * into a {@link HsvParserToken}.
     */
    public static <C extends ParserContext> Parser<C> hsvaFunction() {
        return HSVA_FUNCTION_PARSER.cast();
    }

    private static ParserToken transformHsvaFunction(final ParserToken token, final ParserContext context) {
        return HsvaFunctionParserTokenVisitor.acceptParserToken(token);
    }

    private final static Parser<ParserContext> HSVA_FUNCTION_PARSER = hsvaFunctionParser();

    private static Parser<ParserContext> hsvaFunctionParser() {
        final Parser<ParserContext> whitespace = Parsers.repeated(Parsers.character(CharPredicates.whitespace()));
        final Parser<ParserContext> component = Parsers.doubleParser();
        final Parser<ParserContext> percentage = Parsers.character(CharPredicates.is('%'));
        final Parser<ParserContext> comma = Parsers.character(CharPredicates.is(','));

        return Parsers.sequenceParserBuilder()
                .required(Parsers.string("hsva(", CaseSensitivity.SENSITIVE))
                .optional(whitespace) // hue
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // sat
                .required(component)
                .required(percentage)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // value
                .required(component)
                .required(percentage)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace)
                .required(component) // alpha
                .required(percentage)
                .optional(whitespace)
                .required(Parsers.character(CharPredicates.is(')')))
                .build()
                .transform(ColorParsers::transformHsvaFunction)
                .setToString("hsva()");
    }
    
    // ................................................................................................................

    /**
     * A parser that handles
     * <pre>
     * rgb(RR,GG,BB)
     * </pre>
     * into a {@link ColorParserToken}.
     */
    public static <C extends ParserContext> Parser<C> rgbFunction() {
        return RGB_FUNCTION_PARSER.cast();
    }

    /**
     * This method should only be called to init {@link #RGB_FUNCTION_PARSER}
     */
    private static Parser<ParserContext> rgbFunctionParser() {
        final Parser<ParserContext> whitespace = Parsers.repeated(Parsers.character(CharPredicates.whitespace()));
        final Parser<ParserContext> component = Parsers.longParser(10);
        final Parser<ParserContext> comma = Parsers.character(CharPredicates.is(','));

        return Parsers.sequenceParserBuilder()
                .required(Parsers.string("rgb(", CaseSensitivity.SENSITIVE))
                .optional(whitespace) // red
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // green
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // blue
                .required(component)
                .optional(whitespace)
                .required(Parsers.character(CharPredicates.is(')')))
                .build()
                .transform(ColorParsers::transformRgbFunction)
                .setToString("rgb()");
    }

    private static ParserToken transformRgbFunction(final ParserToken token, final ParserContext context) {
        return transformRgbFunction0(token.cast(), context);
    }

    private static ColorParserToken transformRgbFunction0(final SequenceParserToken token, final ParserContext context) {
        return RgbFunctionParserTokenVisitor.parseSequenceParserToken(token);
    }

    private final static Parser<ParserContext> RGB_FUNCTION_PARSER = rgbFunctionParser();

    // ................................................................................................................

    /**
     * A parser that handles
     * <pre>
     * rgba(RR,GG,BB,1.0)
     * </pre>
     * into a {@link ColorParserToken}.
     */
    public static <C extends ParserContext> Parser<C> rgbaFunction() {
        return RGBA_FUNCTION_PARSER.cast();
    }

    /**
     * This method should only be called to init {@link #RGBA_FUNCTION_PARSER}
     */
    private static Parser<ParserContext> rgbaFunctionParser() {
        final Parser<ParserContext> whitespace = Parsers.repeated(Parsers.character(CharPredicates.whitespace()));
        final Parser<ParserContext> component = Parsers.longParser(10);
        final Parser<ParserContext> comma = Parsers.character(CharPredicates.is(','));

        return Parsers.sequenceParserBuilder()
                .required(Parsers.string("rgba(", CaseSensitivity.SENSITIVE))
                .optional(whitespace) // red
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // green
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // blue
                .required(component)
                .optional(whitespace)
                .required(comma)
                .optional(whitespace) // alpha
                .required(Parsers.doubleParser())
                .optional(whitespace)
                .required(Parsers.character(CharPredicates.is(')')))
                .build()
                .transform(ColorParsers::transformRgbAFunction)
                .setToString("rgba()");
    }

    private static ParserToken transformRgbAFunction(final ParserToken token, final ParserContext context) {
        return transformArgbFunction0(token.cast(), context);
    }

    private static ColorParserToken transformArgbFunction0(final SequenceParserToken token, final ParserContext context) {
        return RgbaFunctionParserTokenVisitor.parseSequenceParserToken(token);
    }

    private final static Parser<ParserContext> RGBA_FUNCTION_PARSER = rgbaFunctionParser();

    // ................................................................................................................

    /**
     * Stop creation.
     */
    private ColorParsers() {
        throw new UnsupportedOperationException();
    }
}
