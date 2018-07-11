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

import java.util.List;
import java.util.Objects;

/**
 * This {@link ParserToken} holds a sequence in order of tokens.
 */
public final class SequenceParserToken<T extends ParserToken> extends ParserTemplateToken<List<T>> {

    /**
     * Factory that wraps many tokens in a {@link SequenceParserToken}.
     */
    static <T extends ParserToken> SequenceParserToken<T> with(final List<T> tokens, final String text) {
        Objects.requireNonNull(tokens, "tokens");
        Objects.requireNonNull(text, "text");

        final int count = tokens.size();
        if(count <= 1) {
            throw new IllegalArgumentException("Expected more than 1 token but got " + count + "=" + tokens);
        }

        return new SequenceParserToken(tokens, text);
    }

    private SequenceParserToken(final List<T> tokens, final String text) {
        super(tokens, text);
    }

    @Override
    public SequenceParserToken<T> setText(final String text){
        return Cast.to(this.setText0(text));
    }

    @Override
    SequenceParserToken<T> replaceText(final String text) {
        return with(this.value(), text);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SequenceParserToken;
    }

    @Override
    boolean equals1(final ParserTemplateToken<?> other) {
        return true; // no extra properties to compare
    }

    @Override
    public String toString() {
        return this.value().toString();
    }
}
