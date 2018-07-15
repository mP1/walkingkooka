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
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.SequenceParserToken;

import java.util.function.BiFunction;

/**
 * Wraps any {@link ClassCastException} and {@link IndexOutOfBoundsException} inside a {@link ParserException}
 */
abstract class ParserBiFunctionTemplate<C extends ParserContext, T extends ParserToken> implements BiFunction<SequenceParserToken, C, T> {

    ParserBiFunctionTemplate(){
        super();
    }

    @Override
    public final T apply(final SequenceParserToken token, final C c) {
        try {
            return this.apply0(token, c);
        } catch (final ClassCastException | IllegalStateException | IndexOutOfBoundsException cause) {
            throw new ParserException("Failure while applying to token="+ token + ", message: " + cause, cause);
        }
    }

    abstract T apply0(final SequenceParserToken token, final C c);
}
