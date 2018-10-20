/*
 *
 *  * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *
 */
package walkingkooka.text.cursor.parser;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.util.List;
import java.util.Optional;

/**
 * A {@link Parser} that requires all parsers are matched in order returning all tokens within a {@link SequenceParserToken}
 */
final class SequenceParser<C extends ParserContext> extends ParserTemplate<SequenceParserToken, C> implements HashCodeEqualsDefined {

    /**
     * Factory method only called by {@link SequenceParserBuilder#build()}
     */
    SequenceParser(final List<SequenceParserComponent<C>> components){
        this.components = components;
    }

    @Override
    Optional<SequenceParserToken> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint start) {
        Optional<SequenceParserToken> result = Optional.empty();

        final List<ParserToken> tokens = Lists.array();

        for(SequenceParserComponent component : this.components) {
            final Optional<ParserToken> token = component.parse(cursor, context);
            if(token.isPresent()) {
                tokens.add(token.get());
                continue;
            }
            if(component.abortIfMissing()) {
                tokens.clear();
                break;
            }
        }

        if(!tokens.isEmpty()){
            result = SequenceParserToken.with(tokens,
                    start.textBetween().toString())
                    .success();
        }
        return result;
    }

    private final List<SequenceParserComponent<C>> components;

    // Object .............................................................................................................

    @Override
    public int hashCode() {
        return this.components.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof SequenceParser && this.equals0(Cast.to(other));
    }

    private boolean equals0(final SequenceParser<?> other){
        return this.components.equals(other.components);
    }

    @Override
    public String toString() {
        return SequenceParserComponent.toString(this.components);
    }
}
