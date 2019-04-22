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

package walkingkooka.text.cursor.parser.ebnf.combinator;

import walkingkooka.text.cursor.parser.ebnf.EbnfRuleParserToken;

import java.util.Objects;

/**
 * This exception is thrown when a grammar attempts to replace/override another rule of the same name.
 */
public class EbnfParserCombinatorDuplicateRuleException extends EbnfParserCombinatorException {

    public EbnfParserCombinatorDuplicateRuleException(final String message, final EbnfRuleParserToken duplicate) {
        super(message);

        Objects.requireNonNull(duplicate, "duplicate");
        this.duplicate = duplicate;
    }

    /**
     * The duplicate {@link EbnfRuleParserToken}
     */
    public EbnfRuleParserToken duplicate() {
        return this.duplicate;
    }

    private final EbnfRuleParserToken duplicate;


    private final static long serialVersionUID = 1L;
}
