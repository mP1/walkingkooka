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

package walkingkooka.text.cursor;

import walkingkooka.text.HasText;
import walkingkooka.tree.Traversable;

import java.util.Iterator;
import java.util.Objects;

/**
 * A {@link TextCursor} that consumes the text belonging to a {@link Traversable} and all its descendants.
 * This will be useful to walk over a tree of Traversable, where some or all or perhaps none contain text.
 * Note that any parent traversable, that is a traversable with one or more children will have its text if any ignored.
 * It is assumed that the text of a parent traversable, may be reconstructed from the text from child traversables.
 */
final class TraversableTextCursor<T extends Traversable<T> & HasText> implements TextCursor {

    /**
     * Factory that creates a {@link TextCursor} using the provided {@link Traversable}
     */
    static <T extends Traversable<T> & HasText> TraversableTextCursor<T> with(final T traversable) {
        Objects.requireNonNull(traversable, "traversable");

        return new TraversableTextCursor<>(traversable);
    }

    /**
     * Private ctor use factory
     */
    private TraversableTextCursor(final T traversable) {
        this.iterator = traversable.traversableIterator();
        this.cursor = TextCursors.charSequence(this.text);
    }

    @Override
    public boolean isEmpty() throws TextCursorException {
        this.fillIfCursorEmpty();
        return this.cursor.isEmpty();
    }

    @Override
    public char at() throws TextCursorException {
        this.fillIfCursorEmpty();
        return this.cursor.at();
    }

    @Override
    public TextCursor next() throws TextCursorException {
        this.fillIfCursorEmpty();
        this.cursor.next();
        return this;
    }

    private void fillIfCursorEmpty() {
        if (this.cursor.isEmpty()) {
            // grab next traversable, and fill StringBuilder $text.
            for (; ; ) {
                if (!this.iterator.hasNext()) {
                    break;
                }
                final T traversable = this.iterator.next();

                // only consume text from traversables without any children...
                if (traversable.children().isEmpty()) {
                    this.text.append(traversable.text());
                    break;
                }
            }
        }
    }

    @Override
    public TextCursorSavePoint save() {
        return cursor.save();
    }

    @Override
    public TextCursorLineInfo lineInfo() {
        return this.cursor.lineInfo();
    }

    /**
     * Provides the next {@link Traversable} to add more text to {@link #text}
     */
    private final Iterator<T> iterator;

    /**
     * Filled with text whenever the wrapped cursor becomes empty.
     */
    private final StringBuilder text = new StringBuilder();

    /**
     * The wrapped {@link TextCursor} that provides individual characters, save points, line info etc.
     */
    private final TextCursor cursor;

    @Override
    public String toString() {
        return this.cursor.toString();
    }
}
