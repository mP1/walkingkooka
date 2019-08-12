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
package walkingkooka.text.cursor.parser.ebnf;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringBuilder;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;

final class BasicEbnfParserContext implements EbnfParserContext {

    static BasicEbnfParserContext instance() {
        return INSTANCE;
    }

    private final static BasicEbnfParserContext INSTANCE = new BasicEbnfParserContext();

    private BasicEbnfParserContext() {
        super();
    }

    @Override
    public List<String> ampms() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String currencySymbol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char decimalSeparator() {
        return '.';
    }

    @Override
    public char exponentSymbol() {
        return 'E';
    }

    @Override
    public char groupingSeparator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale locale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MathContext mathContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char negativeSign() {
        return '-';
    }

    @Override
    public List<String> monthNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> monthNameAbbreviations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char percentageSymbol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char positiveSign() {
        return '+';
    }

    @Override
    public int twoDigitYear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> weekDayNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> weekDayNameAbbreviations() {
        throw new UnsupportedOperationException();
    }

    @Test
    public String toString() {
        return ToStringBuilder.empty()
                .label("decimalSeparator").value(this.decimalSeparator())
                .label("exponentSymbol").value(this.exponentSymbol())
                .label("negativeSign").value(this.negativeSign())
                .label("positiveSign").value(this.positiveSign())
                .build();
    }
}
