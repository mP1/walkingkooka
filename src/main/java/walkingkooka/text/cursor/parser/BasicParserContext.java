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

package walkingkooka.text.cursor.parser;

import walkingkooka.DecimalNumberContext;

import java.util.Objects;

/**
 * An adaptor for {@link DecimalNumberContext} to {@link ParserContext}.
 */
final class BasicParserContext implements ParserContext {

    /**
     * Creates a new {@link BasicParserContext}.
     */
    static BasicParserContext with(final DecimalNumberContext context) {
        Objects.requireNonNull(context, "context");
        return new BasicParserContext(context);
    }

    /**
     * Private ctor use factory
     */
    private BasicParserContext(final DecimalNumberContext context) {
        super();
        this.context = context;
    }

    @Override
    public String currencySymbol() {
        return this.context.currencySymbol();
    }

    @Override
    public char decimalPoint() {
        return this.context.decimalPoint();
    }

    @Override
    public char exponentSymbol() {
        return this.context.exponentSymbol();
    }

    @Override
    public char groupingSeparator() {
        return this.context.groupingSeparator();
    }

    @Override
    public char minusSign() {
        return this.context.minusSign();
    }

    @Override
    public char percentageSymbol() {
        return this.context.percentageSymbol();
    }

    @Override
    public char plusSign() {
        return this.context.plusSign();
    }

    private final DecimalNumberContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}