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

import walkingkooka.collect.list.Lists;
import walkingkooka.color.AlphaColorComponent;
import walkingkooka.color.AlphaHslComponent;
import walkingkooka.color.AlphaHsvComponent;
import walkingkooka.color.BlueColorComponent;
import walkingkooka.color.ColorHslOrHsv;
import walkingkooka.color.GreenColorComponent;
import walkingkooka.color.HueHslComponent;
import walkingkooka.color.HueHsvComponent;
import walkingkooka.color.LightnessHslComponent;
import walkingkooka.color.RedColorComponent;
import walkingkooka.color.SaturationHslComponent;
import walkingkooka.color.SaturationHsvComponent;
import walkingkooka.color.ValueHsvComponent;
import walkingkooka.text.cursor.parser.ParentParserToken;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Holds a json array which may contain further json values.
 */
public final class ColorFunctionFunctionParserToken extends ColorFunctionParserToken implements ParentParserToken<ColorFunctionFunctionParserToken>  {

    static ColorFunctionFunctionParserToken with(final List<ParserToken> value,
                                                 final String text) {
        Objects.requireNonNull(value, "tokens");

        return new ColorFunctionFunctionParserToken(Lists.immutable(value),
                Objects.requireNonNull(text, "text"),
                WITHOUT_COMPUTE_REQUIRED);
    }

    private final static List<ParserToken> WITHOUT_COMPUTE_REQUIRED = null;

    private ColorFunctionFunctionParserToken(final List<ParserToken> value,
                                             final String text,
                                             final List<ParserToken> valueWithout) {
        super(text);
        this.value = value;
        this.without = value.equals(valueWithout) ?
                Optional.of(this) :
                computeWithout(value);
    }

    private Optional<ColorFunctionParserToken> computeWithout(final List<ParserToken> value) {
        final List<ParserToken> without = ParentParserToken.filterWithoutNoise(value);

        return Optional.of(value.size() == without.size() ?
                this :
                new ColorFunctionFunctionParserToken(without, this.text, without));
    }

    @Override
    public final List<ParserToken> value() {
        return this.value;
    }

    final List<ParserToken> value;

    @Override
    public final Optional<ColorFunctionParserToken> withoutSymbols() {
        return this.without;
    }

    final boolean isWithout() {
        return this.without.get() == this;
    }

    private final Optional<ColorFunctionParserToken> without;

    final List<ParserToken> valueIfWithoutSymbolsOrNull() {
        return this == this.without.get() ? this.value : null;
    }

    // toColorHslOrHsv..................................................................................................

    public ColorHslOrHsv toColorHslOrHsv() {
        return ColorParsersComponentsColorFunctionParserTokenVisitor.transform(this);
    }

    // isXXX............................................................................................................

    @Override
    public boolean isDegreesUnitSymbol() {
        return false;
    }

    @Override
    public boolean isFunction() {
        return true;
    }

    @Override
    public boolean isFunctionName() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isParenthesisCloseSymbol() {
        return false;
    }

    @Override
    public boolean isParenthesisOpenSymbol() {
        return false;
    }

    @Override
    public boolean isPercentage() {
        return false;
    }

    @Override
    public boolean isSeparatorSymbol() {
        return false;
    }

    @Override
    public boolean isSymbol() {
        return false;
    }

    @Override
    public boolean isWhitespace() {
        return false;
    }

    // visitor .........................................................................................................

    @Override
    public void accept(final ColorFunctionParserTokenVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    final void acceptValues(final ColorFunctionParserTokenVisitor visitor) {
        for (ParserToken token : this.value()) {
            visitor.accept(token);
        }
    }

    // Object .........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ColorFunctionFunctionParserToken;
    }

    // ColorFunctionTransformer.........................................................................................

    @Override
    RedColorComponent colorRed() {
        throw new UnsupportedOperationException();
    }

    @Override
    BlueColorComponent colorBlue() {
        throw new UnsupportedOperationException();
    }

    @Override
    GreenColorComponent colorGreen() {
        throw new UnsupportedOperationException();
    }

    @Override
    AlphaColorComponent colorAlpha() {
        throw new UnsupportedOperationException();
    }

    @Override
    HueHslComponent hslHue() {
        throw new UnsupportedOperationException();
    }

    @Override
    SaturationHslComponent hslSaturation() {
        throw new UnsupportedOperationException();
    }

    @Override
    LightnessHslComponent hslLightness() {
        throw new UnsupportedOperationException();
    }

    @Override
    AlphaHslComponent hslAlpha() {
        throw new UnsupportedOperationException();
    }

    @Override
    HueHsvComponent hsvHue() {
        throw new UnsupportedOperationException();
    }

    @Override
    SaturationHsvComponent hsvSaturation() {
        throw new UnsupportedOperationException();
    }

    @Override
    ValueHsvComponent hsvValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    AlphaHsvComponent hsvAlpha() {
        throw new UnsupportedOperationException();
    }
}
