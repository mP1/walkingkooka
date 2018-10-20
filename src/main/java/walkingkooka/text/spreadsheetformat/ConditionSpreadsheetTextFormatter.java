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

import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatConditionParserToken;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Tries to convert a value to a {@Link BigDecimal} and then tests a condition and if it is true, executes the given {@link SpreadsheetTextFormatter}.
 */
final class ConditionSpreadsheetTextFormatter<T> extends SpreadsheetTextFormatterTemplate2<T, SpreadsheetFormatConditionParserToken> {

    /**
     * Creates a {@link ConditionSpreadsheetTextFormatter}
     */
    static <T> ConditionSpreadsheetTextFormatter<T> with(final SpreadsheetFormatConditionParserToken token,
                                                         final Converter bigDecimalConverter,
                                                         final SpreadsheetTextFormatter<T> formatter) {
        check(token);
        Objects.requireNonNull(bigDecimalConverter, "bigDecimalConverter");
        Objects.requireNonNull(formatter, "formatter");

        return new ConditionSpreadsheetTextFormatter(token, bigDecimalConverter, formatter);
    }

    /**
     * Private use factory
     */
    private ConditionSpreadsheetTextFormatter(final SpreadsheetFormatConditionParserToken token,
                                              final Converter bigDecimalConverter,
                                              final SpreadsheetTextFormatter<T> formatter) {
        super(token);

        this.bigDecimalConverter = bigDecimalConverter;
        this.predicate = ConditionSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor.predicateOrFail(token);
        this.formatter = formatter;
    }

    @Override
    public Class<T> type() {
        return this.formatter.type();
    }

    @Override
    Optional<SpreadsheetFormattedText> format0(final T value, final SpreadsheetTextFormatContext context) {
        final ConverterContext converterContext = ConverterContexts.basic(context);
        
        // predicate test result inverted because $value is on the wrong side of compare
        return this.bigDecimalConverter.canConvert(value, BigDecimal.class, converterContext) &&
                this.predicate.test(this.bigDecimalConverter.convert(value, BigDecimal.class, converterContext)) ?
                this.formatter.format(value, context) :
                Optional.empty();
    }

    /**
     * Converts the incoming value to {@link BigDecimal}.
     */
    private final Converter bigDecimalConverter;

    /**
     * The formatter that will be executed if the guard test passes.
     */
    private final SpreadsheetTextFormatter<T> formatter;

    /**
     * A guard which only executes the formatter if the condition is true.
     */
    final Predicate<BigDecimal> predicate;

    @Override final String toStringSuffix() {
        return " " + this.formatter;
    }
}
