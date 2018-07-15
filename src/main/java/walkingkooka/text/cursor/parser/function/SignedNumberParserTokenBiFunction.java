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
import walkingkooka.text.cursor.parser.HasSign;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.text.cursor.parser.SignParserToken;

import java.util.Objects;
import java.util.Optional;

/**
 * Assumes two tokens wrapped in a {@link SequenceParserToken}, and applies the sign to the number.
 */
final class SignedNumberParserTokenBiFunction<T extends ParserToken & HasSign, C extends ParserContext> extends ParserBiFunctionTemplate<C, T> {

    /**
     * Type safe singleton getter.
     */
    static <T extends ParserToken & HasSign, C extends ParserContext> SignedNumberParserTokenBiFunction<T, C> with(final Class<T> token) {
        Objects.requireNonNull(token, "token");

        return new SignedNumberParserTokenBiFunction<>(token);
    }

    private SignedNumberParserTokenBiFunction(final Class<T> token){
        super();
        this.token = token;
    }

    @Override
    T apply0(final SequenceParserToken token, final C c) {
        token.checkTokenCount(2);

        final Optional<SignParserToken> sign = token.optional(0, SignParserToken.class);
        final T number = token.required(1, this.token);

        return sign.isPresent() ?
                this.applySignPresent(sign.get(), number) :
                number;
    }

    private T applySignPresent(final SignParserToken sign, final T token) {
        return Cast.to(
                token.setNegative(sign.value())
                .setText(sign.text().concat(token.text()))
        );
    }

    private final Class<T> token;

    @Override
    public String toString() {
        return SignedNumberParserTokenBiFunction.class.getName();
    }
}
