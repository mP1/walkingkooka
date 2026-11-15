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

package walkingkooka.collect.list;

import walkingkooka.text.CharacterConstant;
import walkingkooka.text.HasMultiLineText;
import walkingkooka.text.HasTextWithSeparator;
import walkingkooka.text.LineEnding;
import walkingkooka.text.MultiLineText;
import walkingkooka.text.SupportsLineBreakDelimiter;
import walkingkooka.text.TextContext;

import java.util.AbstractList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;

/**
 * Base class for both {@link CsvStringList} and {@link TsvStringList}.
 */
abstract class CsvTsvStringList<T extends CsvTsvStringList<T>> extends AbstractList<String>
    implements CanRemoveTrailingNullOrEmptyStrings,
    DelimiterStringImmutableList,
    ImmutableListDefaults<T, String>,
    HasCsvStringList,
    HasMultiLineText,
    HasTextWithSeparator,
    HasTsvStringList,
    SupportsLineBreakDelimiter<T> {

    /**
     * Package private ctor use #parse or any would be mutator methods.
     */
    CsvTsvStringList(final List<String> strings,
                     final BitSet lineBreaks) {
        super();
        this.strings = strings;
        this.lineBreaks = lineBreaks;
    }

    @Override
    public final String get(final int index) {
        return this.strings.get(index);
    }

    @Override
    public final int size() {
        return this.strings.size();
    }

    @Override
    public final void elementCheck(final String string) {
        Objects.requireNonNull(string, "string");
    }

    final List<String> strings;

    // HasCsvStringList.................................................................................................

    @Override
    public final CsvStringList csvStringList() {
        return CsvStringList.EMPTY.setElements(this);
    }

    // HasMultiLineText.................................................................................................

    /**
     * Returns this value as text with the given {@link HasMultiLineText}.
     */
    @Override
    public final MultiLineText multiLineText(final TextContext context) {
        Objects.requireNonNull(context, "context");

        final StringBuilder b = new StringBuilder();

        int i = -1;
        for (String string : this) {
            if (i >= 0) {
                b.append(
                    this.multiLineTextSeparator(
                        i,
                        context
                    )
                );
            }
            i++;

            // escape eols.
            b.append(
                string.replace("\r", "\\r")
                    .replace("\n", "\\n")
            );
        }

        if (i >= 0) {
            final CharSequence next = this.multiLineTextSeparator(
                i,
                context
            );
            if (next instanceof LineEnding) {
                b.append(next);
            }
        }

        return MultiLineText.EMPTY.setText(
            b.toString()
        );
    }

    private CharSequence multiLineTextSeparator(final int i,
                                                final TextContext context) {
        return this.lineBreak(i) ?
            context.lineEnding() :
            this.defaultTextSeparator();
    }

    // HasTextWithSeparator.............................................................................................

    @Override
    public final CharSequence defaultTextSeparator() {
        return this.characterConstant();
    }

    @Override
    public final CharSequence textSeparator(final int index) {
        return this.characterConstant();
    }

    abstract CharacterConstant characterConstant();

    /**
     * Note strings with quotes, commas, CR or NL will be quoted and double quotes escaped.
     */
    @Override
    public final String textWithSeparator(final CharSequence separator) {
        final String stringSeparator = String.valueOf(separator);

        return this.characterConstant()
            .toDelimiteredString(
                this.strings,
                (int position) -> stringSeparator
            );
    }

    // HasTsvStringList.................................................................................................

    @Override
    public final TsvStringList tsvStringList() {
        return TsvStringList.EMPTY.setElements(this);
    }

    // SupportsLineBreakDelimiter.......................................................................................

    @Override
    public final boolean lineBreak(final int index) {
        this.checkIndex(index);

        final BitSet lineBreaks = this.lineBreaks;
        return null != lineBreaks &&
            lineBreaks.get(index);
    }

    @Override
    public final T setLineBreak(final int index) {
        this.checkIndex(index);

        final T after;
        final BitSet lineBreaks = this.lineBreaks;

        // any change ?
        if (null == lineBreaks) {
            final BitSet afterLineBreaks = new BitSet();
            afterLineBreaks.set(index);

            after = this.replaceLineBreaks(afterLineBreaks);
        } else {
            if (false == lineBreaks.get(index)) {
                final BitSet afterLineBreaks = (BitSet) lineBreaks.clone();
                afterLineBreaks.set(index);

                after = this.replaceLineBreaks(
                    afterLineBreaks
                );
            } else {
                after = (T) this;
            }
        }

        return after;
    }

    @Override
    public final T removeLineBreak(final int index) {
        this.checkIndex(index);

        final T after;

        // any change ?
        final BitSet lineBreaks = this.lineBreaks;
        if (null != lineBreaks && this.lineBreaks.get(index)) {
            final BitSet afterLineBreaks = (BitSet) lineBreaks.clone();
            afterLineBreaks.set(
                index,
                false
            );

            after = this.replaceLineBreaks(
                afterLineBreaks.isEmpty() ?
                    null :
                    afterLineBreaks
            );
        } else {
            after = (T) this;
        }

        return after;
    }

    @Override
    public final T clearAllLineBreaks() {
        final BitSet lineBreaks = this.lineBreaks;
        return null == lineBreaks ?
            (T) this :
            this.replaceLineBreaks(
                null
            );
    }

    abstract T replaceLineBreaks(final BitSet lineBreaks);

    /**
     * Helper used by linebreak getter/setter/remover to validate the given index.
     */
    final void checkIndex(final int index) {
        final int size = this.size();

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index " + index + " < 0 or > " + size);
        }
    }

    /**
     * Tracks which values have a {@link walkingkooka.text.LineEnding} following them rather than CSVs.
     */
    final BitSet lineBreaks;

    // equals...........................................................................................................

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
            null != other &&
                this.getClass() == other.getClass() &&
                this.equals0(
                    (CsvTsvStringList<?>) other
                );
    }

    private boolean equals0(final CsvTsvStringList<?> other) {
        boolean equals = this.strings.equals(other.strings);

        if (equals) {
            final BitSet lineBreaks = this.lineBreaks;
            final BitSet otherLineBreaks = other.lineBreaks;

            equals = lineBreaks == otherLineBreaks;

            if (false == equals) {
                // lineBreaks could be null
                equals = Objects.equals(
                    lineBreaks,
                    otherLineBreaks
                );
            }
        }

        return equals;
    }
}
