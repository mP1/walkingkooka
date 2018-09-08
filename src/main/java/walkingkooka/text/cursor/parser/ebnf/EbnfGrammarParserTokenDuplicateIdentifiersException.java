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

package walkingkooka.text.cursor.parser.ebnf;

import walkingkooka.collect.set.Sets;

import java.util.Objects;
import java.util.Set;

/**
 * Used to report at least one unknown duplicates within a grammar.
 */
public final class EbnfGrammarParserTokenDuplicateIdentifiersException extends EbnfParserException {

    EbnfGrammarParserTokenDuplicateIdentifiersException(final String message, final Set<EbnfRuleParserToken> duplicates) {
        super(message);

        Objects.requireNonNull(duplicates, "duplicates");
        if(duplicates.isEmpty()) {
            throw new IllegalArgumentException("Duplicates is empty");
        }
        this.duplicates = duplicates;
    }

    public Set<EbnfRuleParserToken> duplicates() {
        return Sets.readOnly(this.duplicates);
    }

    private final Set<EbnfRuleParserToken> duplicates;

    @Override
    public String toString() {
        return "Unknown duplicates=" + this.duplicates;
    }

    private final static long serialVersionUID = 1L;
}
