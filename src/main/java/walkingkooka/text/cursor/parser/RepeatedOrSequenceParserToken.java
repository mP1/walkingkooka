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
import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;

/**
 * A common base class for both {@link RepeatedParserToken} and {@link SequenceParserToken}.
 */
abstract public class RepeatedOrSequenceParserToken<T extends RepeatedOrSequenceParserToken> extends ParserTemplateToken<List<ParserToken>>
        implements ParentParserToken<T> {

    /**
     * Private ctor to limit subclassing.
     */
    RepeatedOrSequenceParserToken(final List<ParserToken> value, final String text) {
        super(value, text);

        if(value.isEmpty()) {
            throw new IllegalArgumentException("Tokens must not be empty");
        }
    }

    /**
     * Sub classes must create a public setValue and call this method and cast this.
     */
    final ParserTemplateToken<List<ParserToken>> setValue0(final List<ParserToken> value) {
        Objects.requireNonNull(value, "values");

        final List<ParserToken> copy = Lists.array();
        copy.addAll(value);
        return this.value().equals(copy) ?
                this :
                this.replaceValue(copy);
    }

    abstract ParserTemplateToken<List<ParserToken>> replaceValue(final List<ParserToken> value);

    final void acceptValues(final ParserTokenVisitor visitor){
        for(ParserToken token: this.value()){
            visitor.accept(token);
        }
    }

    /**
     * Recursively flattens all embedded {@link RepeatedOrSequenceParserToken} into a single {@link RepeatedOrSequenceParserToken}.
     */
    public abstract RepeatedOrSequenceParserToken<?> flat();

    /**
     * Takes the tokens of something that is a {@link RepeatedOrSequenceParserToken} and flattens them so no tokens that remain are also flattenable.
     */
    final List<ParserToken> flat0(){
        final List<ParserToken> flat = Lists.array();

        for(ParserToken token : this.value()) {
            if(token instanceof RepeatedOrSequenceParserToken) {
                final RepeatedOrSequenceParserToken<?> has = Cast.to(token);
                flat.addAll(has.flat0());
            } else {
                flat.add(token);
            }
        }

        return flat;
    }

    @Override
    final boolean equals1(final ParserTemplateToken<?> other){
        return true;
    }
}
