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

package walkingkooka.text.spreadsheetformat;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.math.Fraction;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatBigDecimalParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatColorParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatConditionParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatDateTimeParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatEqualsParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatExpressionParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatFractionParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatGeneralParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatGreaterThanEqualsParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatGreaterThanParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatLessThanEqualsParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatLessThanParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatNotEqualsParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatSeparatorSymbolParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatTextParserToken;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.function.Function;

/**
 * This visitor is used exclusively by {@link ExpressionSpreadsheetTextFormatter} to create formatters for all individual components
 * in an expressioned.
 */
final class ExpressionSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor extends TextFormatterSpreadsheetFormatParserTokenVisitor {

    /**
     * Visits all the individual tokens in the given token which was compiled from the given pattern.
     */
    static List<SpreadsheetTextFormatter<Object>> analyze(final SpreadsheetFormatExpressionParserToken token,
                                                     final MathContext mathContext,
                                                     final Function<BigDecimal, Fraction> fractioner) {
        final ExpressionSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor = new ExpressionSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor(mathContext, fractioner);
        visitor.accept(token);

        final List<SpreadsheetTextFormatter<?>> formatters = visitor.formatters;

        final SpreadsheetTextFormatter<?> formatter = visitor.formatter;
        if(null != formatter){
            formatters.add(visitor.formatter());
        }

        for(int i = formatters.size(); i < 4; i++) {
            formatters.add(noText());
        }

        return Cast.to(formatters);
    }

    private static SpreadsheetTextFormatter<Object> noText() {
        return SpreadsheetTextFormatters.fixed(Object.class, SpreadsheetTextFormatter.NO_TEXT);
    }

    /**
     * Private ctor use static method.
     */
    ExpressionSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor(final MathContext mathContext,
                                                                          final Function<BigDecimal, Fraction> fractioner) {
        super();
        this.mathContext = mathContext;
        this.fractioner = fractioner;
        this.formatter = noText();
    }

    // BigDecimal.....................................................................................

    @Override
    protected void endVisit(final SpreadsheetFormatBigDecimalParserToken token) {
        this.formatter = SpreadsheetTextFormatters.bigDecimal(token, this.mathContext);
    }

    @Override
    protected void endVisit(final SpreadsheetFormatFractionParserToken token) {
        this.formatter = SpreadsheetTextFormatters.bigDecimalFraction(token, this.mathContext, this.fractioner);
    }

    private final MathContext mathContext;
    private final Function<BigDecimal, Fraction> fractioner;

    // Color.....................................................................................

    @Override
    protected void endVisit(final SpreadsheetFormatColorParserToken token) {
        this.color = token;
    }

    // Condition.....................................................................................

    @Override
    protected void endVisit(final SpreadsheetFormatEqualsParserToken token) {
        this.condition = token;
    }

    @Override
    protected void endVisit(final SpreadsheetFormatGreaterThanEqualsParserToken token) {
        this.condition = token;
    }

    @Override
    protected void endVisit(final SpreadsheetFormatGreaterThanParserToken token) {
        this.condition = token;
    }

    @Override
    protected void endVisit(final SpreadsheetFormatLessThanEqualsParserToken token) {
        this.condition = token;
    }

    @Override
    protected void endVisit(final SpreadsheetFormatLessThanParserToken token) {
        this.condition = token;
    }

    @Override
    protected void endVisit(final SpreadsheetFormatNotEqualsParserToken token) {
        this.condition = token;
    }

    // Date .....................................................................................................

    @Override
    protected void endVisit(final SpreadsheetFormatDateTimeParserToken token) {
        this.formatter = SpreadsheetTextFormatters.localDateTime(token);
    }

    // General................................................................................................

    @Override
    protected void endVisit(final SpreadsheetFormatGeneralParserToken token) {
        this.formatter = SpreadsheetTextFormatters.general();
    }

    // Text..................................................................................................

    @Override
    protected void endVisit(final SpreadsheetFormatTextParserToken token) {
        this.formatter = SpreadsheetTextFormatters.text(token);
    }

    // Separator.................................................................................................

    @Override
    protected void visit(final SpreadsheetFormatSeparatorSymbolParserToken token) {
        this.formatters.add(this.formatter());
    }

    private SpreadsheetTextFormatter<?> formatter() {
        SpreadsheetTextFormatter<?> formatter = this.formatter;
        this.formatter = noText();

        final SpreadsheetFormatColorParserToken color = this.color;
        if(null!=color) {
            formatter = SpreadsheetTextFormatters.color(color, formatter);
            this.color = null;
        }

        final SpreadsheetFormatConditionParserToken condition = this.condition;
        if(null!=condition) {
            formatter = SpreadsheetTextFormatters.conditional(condition, formatter);
            this.condition = null;
        }

        return formatter;
    }

    private SpreadsheetFormatColorParserToken color;
    private SpreadsheetFormatConditionParserToken<?> condition;
    private SpreadsheetTextFormatter<?> formatter;

    private List<SpreadsheetTextFormatter<?>> formatters = Lists.array();
}
