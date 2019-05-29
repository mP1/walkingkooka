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

package walkingkooka.text.cursor.parser.color;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.tree.visit.Visiting;

/**
 * Base class for a color, hsl or hsv {@link ParserToken}.
 */
abstract public class ColorHslOrHsvParserToken<V> implements ParserToken, Value<V> {

    /**
     * Private ctor to limit subclassing.
     */
    ColorHslOrHsvParserToken(final V value, final String text) {
        super();
        this.value = value;
        this.text = text;
    }

    @Override
    public final V value() {
        return this.value;
    }

    private final V value;

    /**
     * The text matched by the {@link Parser}.
     */
    public final String text() {
        return this.text;
    }

    private final String text;

    /**
     * Sub classes have a value, so cant be symbols.
     */
    @Override
    public final boolean isSymbol() {
        return false;
    }

    // ColorParserTokenVisitor..........................................................................................

    @Override
    public final void accept(final ParserTokenVisitor visitor) {
        final ColorParserTokenVisitor visitor2 = Cast.to(visitor);

        if (Visiting.CONTINUE == visitor2.startVisit(this)) {
            this.accept(ColorParserTokenVisitor.class.cast(visitor));
        }
        visitor2.endVisit(this);
    }

    abstract public void accept(final ColorParserTokenVisitor visitor);

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return this.text.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other || this.canBeEqual(other) && this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final ColorHslOrHsvParserToken<?> other) {
        return this.value.equals(other.value) && this.text.equals(other.text);
    }

    @Override
    public final String toString() {
        return this.text();
    }
}
