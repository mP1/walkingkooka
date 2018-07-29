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
import walkingkooka.text.Whitespace;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.tree.visit.Visiting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a token within the grammar.
 */
public abstract class SpreadsheetParserToken implements ParserToken {

    /**
     * {@see SpreadsheetAdditionParserToken}
     */
    public static SpreadsheetAdditionParserToken addition(final List<SpreadsheetParserToken> value, final String text){
        return SpreadsheetAdditionParserToken.with(value, text);
    }

    /**
     * {@see SpreadsheetCellParserToken}
     */
    public static SpreadsheetCellParserToken cell(final List<SpreadsheetParserToken> value, final String text){
        return SpreadsheetCellParserToken.with(value, text);
    }
    
    /**
     * {@see SpreadsheetColumnParserToken}
     */
    public static SpreadsheetColumnParserToken column(final SpreadsheetColumn value, final String text){
        return SpreadsheetColumnParserToken.with(value, text);
    }
    
    /**
     * {@see SpreadsheetDecimalParserToken}
     */
    public static SpreadsheetDecimalParserToken decimal(final BigDecimal value, final String text){
        return SpreadsheetDecimalParserToken.with(value, text);
    }

    /**
     * {@see SpreadsheetDivisionParserToken}
     */
    public static SpreadsheetDivisionParserToken division(final List<SpreadsheetParserToken> value, final String text){
        return SpreadsheetDivisionParserToken.with(value, text);
    }

    /**
     * {@see SpreadsheetDoubleParserToken}
     */
    public static SpreadsheetDoubleParserToken doubleParserToken(final double value, final String text){
        return SpreadsheetDoubleParserToken.with(value, text);
    }
    
    /**
     * {@see SpreadsheetFunctionParserToken}
     */
    public static SpreadsheetFunctionParserToken function(final List<SpreadsheetParserToken> value, final String text){
        return SpreadsheetFunctionParserToken.with(value, text);
    }

    /**
     * {@see SpreadsheetFunctionNameParserToken}
     */
    public static SpreadsheetFunctionNameParserToken functionName(final SpreadsheetFunctionName value, final String text){
        return SpreadsheetFunctionNameParserToken.with(value, text);
    }

    /**
     * {@see SpreadsheetGroupParserToken}
     */
    public static SpreadsheetGroupParserToken group(final List<SpreadsheetParserToken> value, final String text){
        return SpreadsheetGroupParserToken.with(value, text);
    }

    /**
     * {@see SpreadsheetLabelNameParserToken}
     */
    public static SpreadsheetLabelNameParserToken labelName(final SpreadsheetLabelName value, final String text){
        return SpreadsheetLabelNameParserToken.with(value, text);
    }

    /**
     * {@see SpreadsheetMultiplicationParserToken}
     */
    public static SpreadsheetMultiplicationParserToken multiplication(final List<SpreadsheetParserToken> value, final String text){
        return SpreadsheetMultiplicationParserToken.with(value, text);
    }

    /**
     * {@see SpreadsheetNegativeParserToken}
     */
    public static SpreadsheetNegativeParserToken negative(final List<SpreadsheetParserToken> value, final String text){
        return SpreadsheetNegativeParserToken.with(value, text);
    }
    
    /**
     * {@see SpreadsheetNumberParserToken}
     */
    public static SpreadsheetNumberParserToken number(final BigInteger value, final String text){
        return SpreadsheetNumberParserToken.with(value, text);
    }

    /**
     * {@see SpreadsheetPercentageParserToken}
     */
    public static SpreadsheetPercentageParserToken percentage(final List<SpreadsheetParserToken> value, final String text){
        return SpreadsheetPercentageParserToken.with(value, text);
    }

    /**
     * {@see SpreadsheetPowerParserToken}
     */
    public static SpreadsheetPowerParserToken power(final List<SpreadsheetParserToken> value, final String text){
        return SpreadsheetPowerParserToken.with(value, text);
    }

    /**
     * {@see SpreadsheetRangeParserToken}
     */
    public static SpreadsheetRangeParserToken range(final List<SpreadsheetParserToken> value, final String text){
        return SpreadsheetRangeParserToken.with(value, text);
    }
    
    /**
     * {@see SpreadsheetRowParserToken}
     */
    public static SpreadsheetRowParserToken row(final SpreadsheetRow value, final String text){
        return SpreadsheetRowParserToken.with(value, text);
    }
    
    /**
     * {@see SpreadsheetSubtractionParserToken}
     */
    public static SpreadsheetSubtractionParserToken subtraction(final List<SpreadsheetParserToken> value, final String text){
        return SpreadsheetSubtractionParserToken.with(value, text);
    }

    /**
     * {@see SpreadsheetSymbolParserToken}
     */
    public static SpreadsheetSymbolParserToken symbol(final String value, final String text){
        return SpreadsheetSymbolParserToken.with(value, text);
    }

    /**
     * {@see SpreadsheetTextParserToken}
     */
    public static SpreadsheetTextParserToken text(final String value, final String text){
        return SpreadsheetTextParserToken.with(value, text);
    }
    
    /**
     * {@see SpreadsheetWhitespaceParserToken}
     */
    public static SpreadsheetWhitespaceParserToken whitespace(final String value, final String text){
        return SpreadsheetWhitespaceParserToken.with(value, text);
    }

    static List<SpreadsheetParserToken> copyAndCheckTokens(final List<SpreadsheetParserToken> tokens) {
        Objects.requireNonNull(tokens, "tokens");

        final List<SpreadsheetParserToken> copy = Lists.array();
        copy.addAll(tokens);

        if(copy.isEmpty()) {
            throw new IllegalArgumentException("Tokens is empty");
        }
        return copy;
    }

    static String checkText(final String text) {
        Whitespace.failIfNullOrWhitespace(text, "text");
        return text;
    }

    /**
     * Package private ctor to limit sub classing.
     */
    SpreadsheetParserToken(final String text) {
        this.text = text;
    }

    @Override
    public final String text() {
        return this.text;
    }

    private final String text;

    final SpreadsheetParserToken setText0(final String text) {
        Objects.requireNonNull(text, "text");
        return this.text.equals(text) ?
                this :
                replaceText(text);
    }

    abstract SpreadsheetParserToken replaceText(final String text);

    /**
     * Returns a copy without any symbols or whitespace tokens. The original text form will still contain
     * those tokens as text, but the tokens themselves will be removed.
     */
    abstract public Optional<SpreadsheetParserToken> withoutSymbolsOrWhitespace();

    /**
     * Only {@link SpreadsheetAdditionParserToken} return true
     */
    public abstract boolean isAddition();

    /**
     * Only {@link SpreadsheetCellParserToken} return true
     */
    public abstract boolean isCell();

    /**
     * Only {@link SpreadsheetColumnParserToken} return true
     */
    public abstract boolean isColumn();

    /**
     * Only {@link SpreadsheetDecimalParserToken} return true
     */
    public abstract boolean isDecimal();

    /**
     * Only {@link SpreadsheetDoubleParserToken} return true
     */
    public abstract boolean isDouble();

    /**
     * Only {@link SpreadsheetDivisionParserToken} return true
     */
    public abstract boolean isDivision();

    /**
     * Only {@link SpreadsheetFunctionParserToken} return true
     */
    public abstract boolean isFunction();

    /**
     * Only {@link SpreadsheetFunctionNameParserToken} return true
     */
    public abstract boolean isFunctionName();

    /**
     * Only {@link SpreadsheetGroupParserToken} return true
     */
    public abstract boolean isGroup();

    /**
     * Only {@link SpreadsheetLabelNameParserToken} return true
     */
    public abstract boolean isLabelName();

    /**
     * Only {@link SpreadsheetLongParserToken} return true
     */
    public abstract boolean isLong();

    /**
     * Only {@link SpreadsheetMultiplicationParserToken} return true
     */
    public abstract boolean isMultiplication();

    /**
     * Only {@link SpreadsheetNegativeParserToken} return true
     */
    public abstract boolean isNegative();

    /**
     * Only {@link SpreadsheetNumberParserToken} return true
     */
    public abstract boolean isNumber();

    /**
     * Only {@link SpreadsheetPercentageParserToken} return true
     */
    public abstract boolean isPercentage();

    /**
     * Only {@link SpreadsheetPowerParserToken} return true
     */
    public abstract boolean isPower();

    /**
     * Only {@link SpreadsheetRangeParserToken} return true
     */
    public abstract boolean isRange();

    /**
     * Only {@link SpreadsheetRowParserToken} return true
     */
    public abstract boolean isRow();

    /**
     * Only {@link SpreadsheetSubtractionParserToken} return true
     */
    public abstract boolean isSubtraction();

    /**
     * Only {@link SpreadsheetSymbolParserToken} return true
     */
    public abstract boolean isSymbol();

    /**
     * Only {@link SpreadsheetTextParserToken} return true
     */
    public abstract boolean isText();

    /**
     * Only {@link SpreadsheetWhitespaceParserToken} return true
     */
    public abstract boolean isWhitespace();

    /**
     * Useful to get help reduce casting noise.
     */
    public final <T extends SpreadsheetParserToken> T cast() {
        return Cast.to(this);
    }

    public final void accept(final ParserTokenVisitor visitor) {
        final SpreadsheetParserTokenVisitor ebnfParserTokenVisitor = Cast.to(visitor);
        final SpreadsheetParserToken token = this;

        if(Visiting.CONTINUE == ebnfParserTokenVisitor.startVisit(token)){
            this.accept(SpreadsheetParserTokenVisitor.class.cast(visitor));
        }
        ebnfParserTokenVisitor.endVisit(token);
    }

    abstract public void accept(final SpreadsheetParserTokenVisitor visitor);

    // Object ...........................................................................................................

    @Override
    public final int hashCode() {
        return this.text().hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
               this.canBeEqual(other) &&
               this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final SpreadsheetParserToken other) {
        return this.text().equals(other.text()) &&
               this.equals1(other);
    }

    abstract boolean equals1(SpreadsheetParserToken other);

    @Override
    public final String toString() {
        return this.text();
    }
}
