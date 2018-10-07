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

/**
 * Base class for both integer and exponent digits. These share several similarities including inserting the sign
 * and adding digits.
 */
abstract class BigDecimalSpreadsheetTextFormatterDigits2 extends BigDecimalSpreadsheetTextFormatterDigits {

    BigDecimalSpreadsheetTextFormatterDigits2(final SpreadsheetTextFormatContextSign sign,
                                              final String text) {
        super(text);
        this.valueSign = sign;
    }

    final void addDigits(final int start,
                         final int end,
                         final String textDigits,
                         final BigDecimalSpreadsheetTextFormatterComponentContext context) {
        int numberDigitPosition = textDigits.length() - start - 1;
        for (int i = start; i <= end; i++) {
            context.appendDigit(textDigits.charAt(i), numberDigitPosition--);
        }
    }

    @Override final void sign(final BigDecimalSpreadsheetTextFormatterComponentContext context) {
        final SpreadsheetTextFormatContextSign sign = this.valueSign;
        if (this.formatSign.shouldAppendSymbol(sign)) {
            context.appendSign(sign);
        }
        this.formatSign = BigDecimalSpreadsheetTextFormatterSign.NOT_REQUIRED;
    }

    final SpreadsheetTextFormatContextSign valueSign;

    /**
     * This will after the initial sign is inserted on demand.
     */
    BigDecimalSpreadsheetTextFormatterSign formatSign = BigDecimalSpreadsheetTextFormatterSign.REQUIRED;
}
