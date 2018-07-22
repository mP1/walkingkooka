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

import walkingkooka.text.cursor.parser.DecimalParserToken;
import walkingkooka.text.cursor.parser.HasSign;
import walkingkooka.text.cursor.parser.NumberParserToken;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.type.PublicStaticHelper;

import java.util.function.BiFunction;

/**
 * A collection of factory methods to create functions for use by {@link Parsers#
 */
public final class ParserBiFunctions implements PublicStaticHelper {

    /**
     * {@see DecimalParserTokenBiFunction}
     */
    public static <C extends ParserContext> BiFunction<SequenceParserToken, C, DecimalParserToken> decimal() {
        return DecimalParserTokenBiFunction.get();
    }

    /**
     * {@see PrefixedNumberParserTokenBiFunction}
     */
    public static <C extends ParserContext> BiFunction<SequenceParserToken, C, NumberParserToken> prefixedNumber() {
        return PrefixedNumberParserTokenBiFunction.get();
    }

    /**
     * {@see SignedNumberParserTokenBiFunction}
     */
    public static <T extends ParserToken & HasSign, C extends ParserContext> BiFunction<SequenceParserToken, C, T> signed(final Class<T> token) {
        return SignedNumberParserTokenBiFunction.with(token);
    }

    /**
     * Stop creation.
     */
    private ParserBiFunctions() {
        throw new UnsupportedOperationException();
    }
}
