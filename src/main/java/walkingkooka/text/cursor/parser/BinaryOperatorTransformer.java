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

package walkingkooka.text.cursor.parser;

import java.util.List;

/**
 * A transformer that transforms the {@link ParserToken} within a {@link SequenceParserToken} into others that
 * include unary negative tokens, binary operator tokens etc.
 */
public interface BinaryOperatorTransformer {

    /**
     * The highest operator priority
     */
    int highestPriority();

    /**
     * The lowest operator priority inclusive of tokens that are a binary operator.
     * Other tokens that are not a binary operator should have a value less than this.
     */
    int lowestPriority();

    /**
     * Returns the operator priority inclusive of tokens that are a binary operator
     */
    int priority(final ParserToken token);

    /**
     * Factory that creates a {@link ParserToken} to hold two arguments separated by a symbol.
     */
    ParserToken binaryOperand(final List<ParserToken> tokens, final String text, final ParserToken parent);
}
