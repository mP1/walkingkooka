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
package walkingkooka.text.cursor.parser.ebnf;

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.cursor.parser.CharacterParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.SequenceParserBuilder;

public final class EbnfParserContext implements ParserContext {

    EbnfParserContext() {
    }

    static Parser<CharacterParserToken, EbnfParserContext> character(final char c) {
        return character(CharPredicates.is(c));
    }

    static Parser<CharacterParserToken, EbnfParserContext> character(final char start, final char end) {
        return character(CharPredicates.range(start, end));
    }

    static Parser<CharacterParserToken, EbnfParserContext> character(final CharPredicate predicate) {
        return Parsers.character(predicate);
    }

    static SequenceParserBuilder<EbnfParserContext> sequenceParserBuilder() {
        return Parsers.sequenceParserBuilder();
    }
}
