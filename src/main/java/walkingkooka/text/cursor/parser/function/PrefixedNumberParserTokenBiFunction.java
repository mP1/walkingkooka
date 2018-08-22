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
package walkingkooka.text.cursor.parser.function;

import walkingkooka.Cast;
import walkingkooka.text.cursor.parser.BigIntegerParserToken;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.text.cursor.parser.StringParserToken;

import java.util.function.BiFunction;

/**
 * Assumes two tokens wrapped in a {@link SequenceParserToken}, and combines the text keeping the 2nd tokens value.
 */
final class PrefixedNumberParserTokenBiFunction<C extends ParserContext> implements BiFunction<SequenceParserToken, C, BigIntegerParserToken> {

    /**
     * Type safe singleton getter.
     */
    static <C extends ParserContext> PrefixedNumberParserTokenBiFunction<C> get() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static PrefixedNumberParserTokenBiFunction INSTANCE = new PrefixedNumberParserTokenBiFunction();

    private PrefixedNumberParserTokenBiFunction(){
        super();
    }

    @Override
    public final BigIntegerParserToken apply(final SequenceParserToken token, final C c) {
        try {
            return this.apply0(token);
        } catch (final ClassCastException | IllegalStateException | IndexOutOfBoundsException cause) {
            throw new ParserException("Failure while applying to token="+ token + ", message: " + cause, cause);
        }
    }

    private BigIntegerParserToken apply0(final SequenceParserToken token) {
        token.checkTokenCount(2);

        final StringParserToken prefix = token.required(0, StringParserToken.class);
        final BigIntegerParserToken number = token.required(1, BigIntegerParserToken.class);

        return number.setText(prefix.text().concat(number.text()));
    }

    @Override
    public String toString() {
        return PrefixedNumberParserTokenBiFunction.class.getName();
    }
}
