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
 */
package walkingkooka.text.cursor.parser;

import walkingkooka.Cast;

import java.math.BigInteger;
import java.util.Objects;

/**
 * The parser token for a number with the value contained in a {@link BigInteger}.
 */
public final class NumberParserToken extends ParserTemplateToken<BigInteger> implements HasSign<NumberParserToken> {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(NumberParserToken.class);

    public static NumberParserToken with(final BigInteger value, final String text) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(text, "text");

        return new NumberParserToken(value, text);
    }

    private NumberParserToken(final BigInteger value, final String text) {
        super(value, text);
    }

    @Override
    public NumberParserToken setText(final String text){
        return Cast.to(this.setText0(text));
    }

    @Override
    NumberParserToken replaceText(final String text) {
        return with(this.value(), text);
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    @Override
    public boolean isNegative() {
        return this.value().signum() < 0;
    }

    @Override
    public NumberParserToken setNegative(final boolean negative) {
        final BigInteger value = this.value();
        return value.signum() < 0 && negative ?
                this :
                new NumberParserToken(value.negate(), this.text());
    }

    @Override
    public void accept(final ParserTokenVisitor visitor){
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NumberParserToken;
    }

    @Override
    boolean equals1(final ParserTemplateToken<?> other) {
        return true; // no extra properties to compare
    }

    @Override
    public String toString() {
        return this.text();
    }
}
