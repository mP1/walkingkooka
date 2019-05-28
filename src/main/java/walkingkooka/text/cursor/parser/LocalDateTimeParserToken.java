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

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The parser token for a date+time with the value contained in a {@link LocalDateTime}.
 */
public final class LocalDateTimeParserToken extends ParserToken2<LocalDateTime> implements LeafParserToken<LocalDateTime> {

    public static LocalDateTimeParserToken with(final LocalDateTime value, final String text) {
        Objects.requireNonNull(text, "text");

        return new LocalDateTimeParserToken(value, text);
    }

    private LocalDateTimeParserToken(final LocalDateTime value, final String text) {
        super(value, text);
    }

    @Override
    public LocalDateTimeParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    LocalDateTimeParserToken replaceText(final String text) {
        return with(this.value(), text);
    }

    @Override
    public void accept(final ParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LocalDateTimeParserToken;
    }

    @Override
    boolean equals1(final ParserToken2<?> other) {
        return true; // no extra properties to compare
    }

    // HasSearchNode ...............................................................................................

    @Override
    public SearchNode toSearchNode() {
        return SearchNode.localDateTime(this.text(), this.value());
    }
}
