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

import walkingkooka.text.cursor.parser.ParentParserToken;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

/**
 * Base class for a token that contain another child token, with the class knowing the cardinality.
 */
abstract class EbnfParentParserToken<T extends EbnfParentParserToken<T>> extends EbnfParserToken implements ParentParserToken {

    EbnfParentParserToken(final List<ParserToken> value, final String text) {
        super(text);
        this.value = value;
    }

    final List<ParserToken> checkOnlyOneToken() {
        final List<ParserToken> without = ParentParserToken.filterWithoutNoise(this.value);
        final int count = without.size();
        if (count != 1) {
            throw new IllegalArgumentException("Expected 1 token(ignoring comments, symbols and whitespace) but was " + count + "=" + this.text());
        }
        return without;
    }

    final List<ParserToken> checkAtLeastTwoTokens() {
        final List<ParserToken> without = ParentParserToken.filterWithoutNoise(this.value);
        final int count = without.size();
        if (count < 2) {
            throw new IllegalArgumentException("Expected at least 2 tokens(ignoring comments, symbols and whitespace) but was " + count + "=" + this.text());
        }
        return without;
    }

    final List<ParserToken> checkOnlyTwoTokens() {
        final List<ParserToken> without = ParentParserToken.filterWithoutNoise(this.value);
        final int count = without.size();
        if (count != 2) {
            throw new IllegalArgumentException("Expected 2 tokens(ignoring comments, symbols and whitespace) but was " + count + "=" + this.text());
        }
        return without;
    }

    @Override
    public final List<ParserToken> value() {
        return this.value;
    }

    final List<ParserToken> value;

    final void acceptValues(final EbnfParserTokenVisitor visitor) {
        for (ParserToken token : this.value()) {
            visitor.accept(token);
        }
    }
}
