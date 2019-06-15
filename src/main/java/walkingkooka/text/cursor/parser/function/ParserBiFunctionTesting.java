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
package walkingkooka.text.cursor.parser.function;

import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokens;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.util.BiFunctionTesting;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Mixing interface that provides methods to test a {@link BiFunction}
 */
public interface ParserBiFunctionTesting<F extends BiFunction<SequenceParserToken, C, TOUT>,
        C extends ParserContext,
        TOUT extends ParserToken>
        extends BiFunctionTesting<F, SequenceParserToken, C, TOUT> {

    default TOUT apply(final ParserToken... tokens) {
        return this.apply(this.sequence(tokens));
    }

    default TOUT apply(final SequenceParserToken token) {
        return this.createBiFunction().apply(token, this.createContext());
    }

    default void applyAndCheck(final SequenceParserToken token,
                               final TOUT result) {
        this.applyAndCheck(token, this.createContext(), result);
    }

    default void applyAndCheck(final BiFunction<SequenceParserToken, C, TOUT> function,
                               final SequenceParserToken token,
                               final TOUT result) {
        this.applyAndCheck(function, token, this.createContext(), result);
    }

    C createContext();

    default SequenceParserToken sequence(final ParserToken... tokens) {
        return ParserTokens.sequence(
                Lists.of(tokens),
                Arrays.stream(tokens)
                        .map(t -> t.text())
                        .collect(Collectors.joining()));
    }
}
