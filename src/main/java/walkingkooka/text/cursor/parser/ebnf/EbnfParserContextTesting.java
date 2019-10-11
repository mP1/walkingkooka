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

package walkingkooka.text.cursor.parser.ebnf;

import org.junit.jupiter.api.Test;
import walkingkooka.text.cursor.parser.ParserContextTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing testing interface for {@link EbnfParserContext}
 */
public interface EbnfParserContextTesting<C extends EbnfParserContext> extends ParserContextTesting<C> {

    @Test
    default void testLocaleFails() {
        assertThrows(UnsupportedOperationException.class, () -> this.createContext().locale());
    }

    @Override
    default void testMathContext() {
    }

    @Test
    default void testMathContextFails() {
        assertThrows(UnsupportedOperationException.class, () -> this.createContext().mathContext());
    }

    @Override
    default String typeNameSuffix() {
        return EbnfParserContext.class.getSimpleName();
    }
}
