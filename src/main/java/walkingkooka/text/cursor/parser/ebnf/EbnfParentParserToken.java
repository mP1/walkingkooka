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

import walkingkooka.Cast;
import walkingkooka.Value;

import java.util.List;

/**
 * Base class for a token that contain another child token, with the class knowing the cardinality.
 */
abstract class EbnfParentParserToken extends EbnfParserToken implements Value<List<EbnfParserToken>> {

    EbnfParentParserToken(final List<EbnfParserToken> value, final String text) {
        super(text);
        this.value = value;
    }

    @Override
    public final boolean isComment() {
        return false;
    }

    @Override
    public final boolean isGrammar() {
        return false;
    }

    @Override
    public final boolean isIdentifier() {
        return false;
    }

    @Override
    public final boolean isSymbol() {
        return false;
    }

    @Override
    public final boolean isTerminal() {
        return false;
    }

    @Override
    public final boolean isWhitespace() {
        return false;
    }

    @Override
    public final List<EbnfParserToken> value() {
        return this.value;
    }

    final List<EbnfParserToken> value;

    @Override
    final boolean equals1(final EbnfParserToken other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final EbnfParentParserToken other) {
        return this.value.equals(other.value);
    }
}
