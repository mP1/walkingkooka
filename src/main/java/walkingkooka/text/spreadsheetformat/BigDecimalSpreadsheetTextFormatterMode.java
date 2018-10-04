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

import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatExponentParserToken;

/**
 * Keeps track of the three modes or phases of a number, the integer, fractionDigitSymbolCount and exponentDigitSymbolCount digits.
 */
enum BigDecimalSpreadsheetTextFormatterMode {

    /**
     * Any digit symbols belong to the INTEGER portion of a number.
     */
    INTEGER {
        @Override
        void decimalPoint(final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor) {
            visitor.mode = FRACTION;
            visitor.add(BigDecimalSpreadsheetTextFormatterComponent.decimalPointSymbol());
        }

        @Override
        int digitCounterAndIncrement(final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor) {
            if (visitor.comma > 0) {
                visitor.thousandsGrouping = true;
                visitor.comma = 0;
            }
            return visitor.integerDigitSymbolCount++;
        }

        @Override
        void exponent(final SpreadsheetFormatExponentParserToken token,
                      final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor) {
            visitor.exponent(token);
        }

        @Override
        void thousands(final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor) {
            visitor.comma++;
        }
    },

    /**
     * Any digit symbols belong to the FRACTION portion of a number.
     */
    FRACTION {
        @Override
        void decimalPoint(final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor) {
            visitor.mode = EXPONENT;
        }

        @Override
        int digitCounterAndIncrement(final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor) {
            return visitor.fractionDigitSymbolCount++;
        }

        @Override
        void exponent(final SpreadsheetFormatExponentParserToken token,
                      final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor) {
            visitor.exponent(token);
        }

        @Override
        void thousands(final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor) {
            visitor.thousandsGrouping = true;
        }
    },

    /**
     * Any digit symbols belong to the FRACTION portion of a number.
     */
    EXPONENT {
        @Override
        void decimalPoint(final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor) {
            //nop
        }

        @Override
        int digitCounterAndIncrement(final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor) {
            return visitor.exponentDigitSymbolCount++;
        }

        @Override
        void exponent(final SpreadsheetFormatExponentParserToken token,
                      final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor) {
            // nop
        }

        @Override
        void thousands(final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor) {
            visitor.thousandsGrouping = true;
        }
    };

    abstract void decimalPoint(final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor);

    abstract int digitCounterAndIncrement(final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor);

    abstract void exponent(final SpreadsheetFormatExponentParserToken token,
                           final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor);

    abstract void thousands(final BigDecimalSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor visitor);
}
