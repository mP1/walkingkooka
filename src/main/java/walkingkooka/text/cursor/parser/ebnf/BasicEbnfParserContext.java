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
package walkingkooka.text.cursor.parser.ebnf;

import org.junit.jupiter.api.Test;
import walkingkooka.build.tostring.ToStringBuilder;

final class BasicEbnfParserContext implements EbnfParserContext {

    static BasicEbnfParserContext instance() {
        return INSTANCE;
    }

    private final static BasicEbnfParserContext INSTANCE = new BasicEbnfParserContext();

    private BasicEbnfParserContext() {
        super();
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
    public char minusSign() {
        return '-';
    }

    @Override
    public char percentageSymbol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char plusSign() {
        return '+';
    }

    @Test
    public String toString() {
        return ToStringBuilder.empty()
                .label("decimalPoint").value(this.decimalPoint())
                .label("exponentSymbol").value(this.exponentSymbol())
                .label("minusSign").value(this.minusSign())
                .label("plusSign").value(this.plusSign())
                .build();
    }
}
