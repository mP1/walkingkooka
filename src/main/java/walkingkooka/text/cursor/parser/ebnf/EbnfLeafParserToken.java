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

import walkingkooka.Value;

import java.util.Objects;

/**
 * Base class for a leaf token. A leaf has no further breakdown into more detailed tokens.
 */
abstract class EbnfLeafParserToken<T> extends EbnfParserToken implements Value<T> {

    static void checkValue(final String value) {
        Objects.requireNonNull(value, "value");
    }

    EbnfLeafParserToken(final T value, final String text){
        super(text);
        this.value = value;
    }

    public final T value() {
        return this.value;
    }

    final T value;

    @Override
    public final boolean isAlternative() {
        return false;
    }

    @Override
    public final boolean isConcatenation() {
        return false;
    }

    @Override
    public final boolean isException() {
        return false;
    }

    @Override
    public final boolean isGrammar() {
        return false;
    }

    @Override
    public final boolean isGroup() {
        return false;
    }

    @Override
    public final boolean isOptional() {
        return false;
    }

    @Override
    public final boolean isRange() {
        return false;
    }

    @Override
    public final boolean isRepeated() {
        return false;
    }

    @Override
    public final boolean isRule() {
        return false;
    }

    @Override
    final boolean equals1(final EbnfParserToken other) {
        return this.equals2(other.cast());
    }

    private boolean equals2(final EbnfLeafParserToken other) {
        return this.value.equals(other.value);
    }
}
