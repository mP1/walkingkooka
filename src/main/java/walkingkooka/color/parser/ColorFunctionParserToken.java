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
import walkingkooka.color.AlphaColorComponent;
import walkingkooka.color.AlphaHslComponent;
import walkingkooka.color.AlphaHsvComponent;
import walkingkooka.color.BlueColorComponent;
import walkingkooka.color.GreenColorComponent;
import walkingkooka.color.HueHslComponent;
import walkingkooka.color.HueHsvComponent;
import walkingkooka.color.LightnessHslComponent;
import walkingkooka.color.RedColorComponent;
import walkingkooka.color.SaturationHslComponent;
import walkingkooka.color.SaturationHsvComponent;
import walkingkooka.color.ValueHsvComponent;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.Whitespace;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Optional;

/**
 * Base class for a color function {@link ParserToken}.
 */
public abstract class ColorFunctionParserToken implements ParserToken, HashCodeEqualsDefined {

    /**
     * {@see ColorFunctionDegreesUnitSymbolParserToken}
     */
    public static ColorFunctionDegreesUnitSymbolParserToken degreesUnitSymbol(final String value, final String text) {
        return ColorFunctionDegreesUnitSymbolParserToken.with(value, text);
    }

    /**
     * {@see ColorFunctionFunctionParserToken}
     */
    public static ColorFunctionFunctionParserToken function(final List<ParserToken> value, final String text) {
        return ColorFunctionFunctionParserToken.with(value, text);
    }

    /**
     * {@see ColorFunctionFunctionNameParserToken}
     */
    public static ColorFunctionFunctionNameParserToken functionName(final String value, final String text) {
        return ColorFunctionFunctionNameParserToken.with(value, text);
    }

    /**
     * {@see ColorFunctionNumberParserToken}
     */
    public static ColorFunctionNumberParserToken number(final Double value, final String text) {
        return ColorFunctionNumberParserToken.with(value, text);
    }

    /**
     * {@see ColorFunctionParenthesisCloseSymbolParserToken}
     */
    public static ColorFunctionParenthesisCloseSymbolParserToken parenthesisCloseSymbol(final String value, final String text) {
        return ColorFunctionParenthesisCloseSymbolParserToken.with(value, text);
    }
    
    /**
     * {@see ColorFunctionParenthesisOpenSymbolParserToken}
     */
    public static ColorFunctionParenthesisOpenSymbolParserToken parenthesisOpenSymbol(final String value, final String text) {
        return ColorFunctionParenthesisOpenSymbolParserToken.with(value, text);
    }

    /**
     * {@see ColorFunctionPercentageParserToken}
     */
    public static ColorFunctionPercentageParserToken percentage(final Double value, final String text) {
        return ColorFunctionPercentageParserToken.with(value, text);
    }

    /**
     * {@see ColorFunctionSeparatorSymbolParserToken}
     */
    public static ColorFunctionSeparatorSymbolParserToken separatorSymbol(final String value, final String text) {
        return ColorFunctionSeparatorSymbolParserToken.with(value, text);
    }

    /**
     * {@see ColorFunctionWhitespaceParserToken}
     */
    public static ColorFunctionWhitespaceParserToken whitespace(final String value, final String text) {
        return ColorFunctionWhitespaceParserToken.with(value, text);
    }

    static String checkText(final String text) {
        Whitespace.failIfNullOrEmptyOrWhitespace(text, "text");
        return text;
    }

    /**
     * Package private ctor to limit subclassing.
     */
    ColorFunctionParserToken(final String text) {
        super();
        this.text = text;
    }

    /**
     * The text matched by the {@link Parser}.
     */
    public final String text() {
        return this.text;
    }

    final String text;

    abstract Object value();

    /**
     * Returns a copy without any symbols or whitespace tokens. The original text form will still contain
     * those tokens as text, but the tokens themselves will be removed.
     */
    abstract public Optional<ColorFunctionParserToken> withoutSymbols();

    // isXXX............................................................................................................

    public abstract boolean isDegreesUnitSymbol();

    public abstract boolean isFunction();

    public abstract boolean isFunctionName();

    public abstract boolean isNumber();

    public abstract boolean isParenthesisCloseSymbol();

    public abstract boolean isParenthesisOpenSymbol();

    public abstract boolean isPercentage();

    public abstract boolean isSeparatorSymbol();

    public abstract boolean isWhitespace();

    // ColorFunctionParserTokenVisitor..................................................................................

    @Override
    public final void accept(final ParserTokenVisitor visitor) {
        final ColorFunctionParserTokenVisitor visitor2 = Cast.to(visitor);
        final ColorFunctionParserToken token = this;

        if (Visiting.CONTINUE == visitor2.startVisit(token)) {
            this.accept(ColorFunctionParserTokenVisitor.class.cast(visitor));
        }
        visitor2.endVisit(token);
    }

    public abstract void accept(final ColorFunctionParserTokenVisitor visitor);

    // ColorFunctionTransformer.........................................................................................

    abstract RedColorComponent colorRed();

    abstract BlueColorComponent colorBlue();

    abstract GreenColorComponent colorGreen();

    abstract AlphaColorComponent colorAlpha();

    abstract HueHslComponent hslHue();

    abstract SaturationHslComponent hslSaturation();

    abstract LightnessHslComponent hslLightness();

    abstract AlphaHslComponent hslAlpha();

    abstract HueHsvComponent hsvHue();

    abstract SaturationHsvComponent hsvSaturation();

    abstract ValueHsvComponent hsvValue();

    abstract AlphaHsvComponent hsvAlpha();
    
    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return this.text().hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other || this.canBeEqual(other) && this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final ColorFunctionParserToken other) {
        return this.text.equals(other.text) && this.value().equals(other.value());
    }

    @Override
    public final String toString() {
        return this.text();
    }
}
