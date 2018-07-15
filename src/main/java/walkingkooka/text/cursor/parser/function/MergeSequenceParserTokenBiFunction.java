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

import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.SequenceParserToken;

import java.util.Objects;

/**
 * Assumes three tokens wrapped in a {@link SequenceParserToken}, and combines the text into the 2nd tokens value.
 */
final class MergeSequenceParserTokenBiFunction<T extends ParserToken, C extends ParserContext> extends ParserBiFunctionTemplate<C, T> {

    /**
     * Factory that creates a MergeSequenceParserTokenBiFunction
     */
    static <T extends ParserToken, C extends ParserContext> MergeSequenceParserTokenBiFunction<T, C> with(final Class<T> token) {
        Objects.requireNonNull(token, "token");

        return new MergeSequenceParserTokenBiFunction(token);
    }

    private MergeSequenceParserTokenBiFunction(final Class<T> token){
        super();
        this.token = token;
    }

    @Override
    T apply0(final SequenceParserToken token, final C c) {
        token.checkTokenCount(3);

        final T second = token.required(1, this.token);

        return this.token.cast(second.setText(token.text()));
    }

    private final Class<T> token;

    @Override
    public String toString() {
        return MergeSequenceParserTokenBiFunction.class.getName();
    }
}
