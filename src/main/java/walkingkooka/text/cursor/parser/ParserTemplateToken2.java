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

/**
 * Represents a result of a parser attempt to consume a {@link walkingkooka.text.cursor.TextCursor}
 */
abstract class ParserTemplateToken2 extends ParserTemplateToken<List<ParserToken>> {

    /**
     * Private ctor to limit subclassing.
     */
    ParserTemplateToken2(final List<ParserToken> value, final String text) {
        super(value, text);
    }

    final void acceptValues(final ParserTokenVisitor visitor){
        for(ParserToken token: this.value()){
            visitor.accept(token);
        }
    }

    /**
     * Takes the tokens of something that implements {@link SupportsFlat} and flattens them so no tokens that remain are also flattenable.
     */
    final List<ParserToken> flat(final List<ParserToken> tokens){
        final List<ParserToken> flat = Lists.array();

        for(ParserToken token : tokens) {
            if(token instanceof SupportsFlat) {
                final SupportsFlat<?, ParserToken> has = Cast.to(token);
                flat.addAll(has.flat().value());
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
