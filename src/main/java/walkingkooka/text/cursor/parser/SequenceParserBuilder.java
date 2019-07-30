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
package walkingkooka.text.cursor.parser;

import walkingkooka.build.Builder;
import walkingkooka.build.BuilderException;
import walkingkooka.collect.list.Lists;

import java.util.List;

/**
 * A {@link Builder} that may be used to build a sequence of tokens, some required or optional.
 */
public final class SequenceParserBuilder<C extends ParserContext> implements Builder<Parser<C>> {

    static <C extends ParserContext> SequenceParserBuilder<C> empty() {
        return new SequenceParserBuilder<>();
    }

    private SequenceParserBuilder() {
        super();
    }

    public SequenceParserBuilder<C> optional(final Parser<C> parser) {
        return this.add(SequenceParserOptionalComponent.with(parser));
    }

    public SequenceParserBuilder<C> required(final Parser<C> parser) {
        return this.add(SequenceParserRequiredComponent.with(parser));
    }

    @Override
    public Parser<C> build() throws BuilderException {
        if (this.components.size() < 2) {
            throw new BuilderException("Sequence requires at least 2 parsers=" + this.components);
        }
        return SequenceParser.with(this.components);
    }

    private SequenceParserBuilder<C> add(final SequenceParserComponent<C> component) {
        this.components.add(component);
        return this;
    }

    private final List<SequenceParserComponent<C>> components = Lists.array();

    @Override
    public String toString() {
        return SequenceParserComponent.toString(this.components);
    }
}
