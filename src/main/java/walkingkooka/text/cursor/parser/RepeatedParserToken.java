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

import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Objects;

/**
 * This {@link ParserToken} holds one or more of the tokens of the same type but not equal.
 */
public final class RepeatedParserToken extends RepeatedOrSequenceParserToken<RepeatedParserToken> {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(RepeatedParserToken.class);

    static RepeatedParserToken with(final List<ParserToken> tokens, final String text) {
        Objects.requireNonNull(tokens, "tokens");
        Objects.requireNonNull(text, "text");

        return new RepeatedParserToken(tokens, text);
    }

    private RepeatedParserToken(final List<ParserToken> tokens, final String text) {
        super(tokens, text);
    }

    @Override
    public RepeatedParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    RepeatedParserToken replaceText(final String text) {
        return with(this.value(), text);
    }

    public RepeatedParserToken setValue(final List<ParserToken> value) {
        return this.setValue0(value).cast();
    }

    @Override
    final RepeatedParserToken replaceValue(final List<ParserToken> value) {
        return new RepeatedParserToken(value, this.text());
    }

    @Override
    public RepeatedParserToken flat() {
        return this.setValue(this.flat0());
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    @Override
    public void accept(final ParserTokenVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof RepeatedParserToken;
    }
}
