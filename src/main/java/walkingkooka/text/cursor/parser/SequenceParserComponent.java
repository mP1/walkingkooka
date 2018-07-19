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
package walkingkooka.text.cursor.parser;

import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.cursor.TextCursor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A component within one of several parsers in a sequence.
 */
abstract class SequenceParserComponent<C extends ParserContext> implements HashCodeEqualsDefined {

    SequenceParserComponent(final Parser<ParserToken, C> parser, final ParserTokenNodeName name) {
        Objects.requireNonNull(parser, "parser");
        Objects.requireNonNull(name, "name");

        this.parser = parser;
        this.name = name;
    }

    abstract Optional<ParserToken> parse(final TextCursor cursor, final C context);

    final void checkName(final List<SequenceParserComponent<C>> components) {
        final int index = this.name.index();
        if(index != -1) {
            final int requiredIndex = components.size();
            if(index != requiredIndex) {
                throw new IllegalArgumentException("Name contains invalid index " + index + " should have been " + requiredIndex);
            }
        }
    }

    final Parser<ParserToken, C> parser;
    final ParserTokenNodeName name;

    // Object .............................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.parser, this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) && this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final SequenceParserComponent<?> other){
        return this.parser.equals(other.parser) && this.name.equals(other.name);
    }

    @Override
    abstract public String toString();

    static <C extends ParserContext> String toString(final List<SequenceParserComponent<C>> components) {
        return components.stream()
                .map(p -> p.toString())
                .collect(Collectors.joining(", ", "(", ")"));
    }
}
