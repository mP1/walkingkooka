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

package walkingkooka.tree.json.parser;

import walkingkooka.ToStringBuilder;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;

/**
 * A {@link JsonNodeParserContext} without any functionality.
 */
final class BasicJsonNodeParserContext implements JsonNodeParserContext {

    /**
     * Type safe getter
     */
    static BasicJsonNodeParserContext instance() {
        return INSTANCE;
    }

    /**
     * Singleton
     */
    private final static BasicJsonNodeParserContext INSTANCE = new BasicJsonNodeParserContext();

    private BasicJsonNodeParserContext() {
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
    public char decimalPoint() {
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
    public char minusSign() {
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
        return '%';
    }

    @Override
    public char plusSign() {
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

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .label("decimalPoint").value(this.decimalPoint())
                .label("exponentSymbol").value(this.exponentSymbol())
                .label("minusSign").value(this.minusSign())
                .label("percentageSymbol").value(this.percentageSymbol())
                .label("plusSign").value(this.plusSign())
                .build();
    }
}
