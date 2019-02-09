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
 *
 */
package walkingkooka.text.cursor.parser;

import walkingkooka.tree.search.SearchNode;

import java.time.LocalTime;
import java.util.Objects;

/**
 * The parser token for a time with the value contained in a {@link LocalTime}.
 */
public final class LocalTimeParserToken extends ParserToken2<LocalTime> implements LeafParserToken<LocalTime>{

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(LocalTimeParserToken.class);

    public static LocalTimeParserToken with(final LocalTime value, final String text) {
        Objects.requireNonNull(text, "text");

        return new LocalTimeParserToken(value, text);
    }

    private LocalTimeParserToken(final LocalTime value, final String text) {
        super(value, text);
    }

    @Override
    public LocalTimeParserToken setText(final String text){
        return this.setText0(text).cast();
    }

    @Override
    LocalTimeParserToken replaceText(final String text) {
        return with(this.value(), text);
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    @Override
    public void accept(final ParserTokenVisitor visitor){
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LocalTimeParserToken;
    }

    @Override
    boolean equals1(final ParserToken2<?> other) {
        return true; // no extra properties to compare
    }

    // HasSearchNode ...............................................................................................

    @Override
    public SearchNode toSearchNode()  {
        return SearchNode.localTime(this.text(), this.value());
    }
}
