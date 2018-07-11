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

import java.util.Objects;

/**
 * A matched double quoted string.
 */
public final class DoubleQuotedParserToken extends QuotedParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(DoubleQuotedParserToken.class);

    static DoubleQuotedParserToken with(final String value, final String text) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(value, "value");
        if(!text.startsWith("\"") || !text.endsWith("\"") ){
            throw new IllegalArgumentException("text must start and end with '\"' but was " + text);
        }

        return new DoubleQuotedParserToken(value, text);
    }

    private DoubleQuotedParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    char quotedCharacter() {
        return '"';
    }

    @Override
    public DoubleQuotedParserToken setText(final String text){
        return Cast.to(this.setText0(text));
    }

    @Override
    DoubleQuotedParserToken replaceText(final String text) {
        return with(this.value(), text);
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof DoubleQuotedParserToken;
    }
}
