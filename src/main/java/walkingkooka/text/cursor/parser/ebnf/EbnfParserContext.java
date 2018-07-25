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
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.SequenceParserBuilder;
import walkingkooka.text.cursor.parser.StringParserToken;

public final class EbnfParserContext implements ParserContext {

    public EbnfParserContext() {
    }

    static Parser<StringParserToken, EbnfParserContext> string(final char c) {
        return string(CharPredicates.is(c), 1, 1);
    }

    static Parser<StringParserToken, EbnfParserContext> string(final char start, final char end) {
        return string(CharPredicates.range(start, end), 1, 1);
    }

    static Parser<StringParserToken, EbnfParserContext> string(final CharPredicate predicate, final int minLength, final int maxLength) {
        return Parsers.stringCharPredicate(predicate, minLength, maxLength);
    }

    static Parser<StringParserToken, EbnfParserContext> string(final String string) {
        return Parsers.string(string);
    }

    static SequenceParserBuilder<EbnfParserContext> sequenceParserBuilder() {
        return Parsers.sequenceParserBuilder();
    }
}
