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

import walkingkooka.EndOfTextException;
import walkingkooka.InvalidCharacterException;
import walkingkooka.NeverError;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.HasMultiLineText;
import walkingkooka.text.HasTextWithSeparator;
import walkingkooka.text.LineEnding;
import walkingkooka.text.MultiLineText;
import walkingkooka.text.SupportsLineBreakDelimiter;
import walkingkooka.text.TextContext;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Base class for both {@link CsvStringList} and {@link TsvStringList}.
 */
abstract class CsvTsvStringList<T extends CsvTsvStringList<T>> extends AbstractList<String>
    implements CanRemoveTrailingNullOrEmptyStrings,
    DelimiterStringImmutableList,
    ImmutableListDefaults<T, String>,
    HasCsvStringListTsvStringList,
    HasMultiLineText,
    HasTextWithSeparator,
    SupportsLineBreakDelimiter<T> {

    /**
     * Parses the given {@link String}, adding each encountered element to the given {@link Consumer}.
     */
    static <T extends CsvTsvStringList<T>> T parse0(final String text,
                                                    final char separatorChar,
                                                    final Function<List<CsvTsvStringListElement>, T> factory) {
        Objects.requireNonNull(text, "text");

        final List<CsvTsvStringListElement> strings = Lists.array();

        final int MODE_FIRST_CHAR_AFTER_DELIMITER = 1;
        final int MODE_FIRST_CHAR_AFTER_NL = 2;
        final int MODE_NL_OR_FIRST_CHAR = 3;
        final int MODE_INSIDE_QUOTED = 4;
        final int MODE_TERMINATING_QUOTE = 5;
        final int MODE_RAW_TEXT = 6;

        int mode = MODE_FIRST_CHAR_AFTER_DELIMITER;

        StringBuilder element = null;

        int i = 0;
        final int length = text.length();

        while (i < length) {
            final char c = text.charAt(i);

            switch (mode) {
                case MODE_NL_OR_FIRST_CHAR:
                    // skip the NL after a CR
                    if (NL_CHAR == c) {
                        mode = MODE_FIRST_CHAR_AFTER_NL;
                        break;
                    }
                case MODE_FIRST_CHAR_AFTER_DELIMITER:
                case MODE_FIRST_CHAR_AFTER_NL:
                    switch (c) {
                        case CR_CHAR:
                            mode = MODE_FIRST_CHAR_AFTER_DELIMITER;

                            strings.add(
                                CsvTsvStringListElement.stringLineBreak("")
                            );

                            mode = MODE_NL_OR_FIRST_CHAR;
                            break;
                        case NL_CHAR:
                            //mode = MODE_FIRST_CHAR;
                            mode = MODE_FIRST_CHAR_AFTER_NL;

                            strings.add(
                                CsvTsvStringListElement.stringLineBreak("")
                            );
                            break;
                        case DOUBLE_QUOTE_CHAR:
                            element = new StringBuilder();
                            mode = MODE_INSIDE_QUOTED;
                            break;
                        default:
                            if (separatorChar == c) {
                                mode = MODE_FIRST_CHAR_AFTER_DELIMITER;

                                strings.add(
                                    CsvTsvStringListElement.string("")
                                );
                            } else {
                                element = new StringBuilder()
                                    .append(c);
                                mode = MODE_RAW_TEXT;
                            }
                            break;
                    }
                    break;
                case MODE_INSIDE_QUOTED:
                    switch (c) {
                        // double quote could be end of quoted string or escaped double quote.
                        case DOUBLE_QUOTE_CHAR:
                            mode = MODE_TERMINATING_QUOTE;
                            break;
                        default:
                            element.append(c);
                            break;
                    }
                    break;
                case MODE_TERMINATING_QUOTE:
                    switch (c) {
                        case DOUBLE_QUOTE_CHAR:
                            element.append(DOUBLE_QUOTE_CHAR);
                            mode = MODE_INSIDE_QUOTED;
                            break;
                        case CR_CHAR:
                            strings.add(
                                CsvTsvStringListElement.stringLineBreak(
                                    element.toString()
                                )
                            );

                            mode = MODE_NL_OR_FIRST_CHAR;
                            break;
                        case NL_CHAR:
                            strings.add(
                                CsvTsvStringListElement.stringLineBreak(
                                    element.toString()
                                )
                            );

                            mode = MODE_FIRST_CHAR_AFTER_NL;
                            break;
                        default:
                            if (separatorChar == c) {
                                // quote was terminating
                                strings.add(
                                    CsvTsvStringListElement.string(
                                        element.toString()
                                    )
                                );

                                mode = MODE_FIRST_CHAR_AFTER_DELIMITER;
                            } else {
                                // trailing quote must be followed by separator or EOF, trailing spaces etc are an ICE.
                                throw new InvalidCharacterException(
                                    text,
                                    i
                                );
                            }
                    }
                    break;
                case MODE_RAW_TEXT:
                    switch (c) {
                        case DOUBLE_QUOTE_CHAR:
                            throw new InvalidCharacterException(
                                text,
                                i
                            );
                        case CR_CHAR:
                            strings.add(
                                CsvTsvStringListElement.stringLineBreak(
                                    element.toString()
                                )
                            );

                            mode = MODE_NL_OR_FIRST_CHAR;
                            break;
                        case NL_CHAR:
                            strings.add(
                                CsvTsvStringListElement.stringLineBreak(
                                    element.toString()
                                )
                            );
                            mode = MODE_FIRST_CHAR_AFTER_DELIMITER;
                            break;
                        default:
                            if (separatorChar == c) {
                                strings.add(
                                    CsvTsvStringListElement.string(
                                        element.toString()
                                    )
                                );
                                mode = MODE_FIRST_CHAR_AFTER_DELIMITER;
                            } else {
                                element.append(c);
                            }
                            break;
                    }
                    break;
                default:
                    NeverError.unhandledCase(
                        mode,
                        MODE_NL_OR_FIRST_CHAR,
                        MODE_FIRST_CHAR_AFTER_DELIMITER,
                        MODE_INSIDE_QUOTED,
                        MODE_TERMINATING_QUOTE,
                        MODE_RAW_TEXT
                    );
            }

            i++;
        }

        // EOT
        switch (mode) {
            case MODE_NL_OR_FIRST_CHAR:
            case MODE_FIRST_CHAR_AFTER_NL:
                // dont insert an empty record after last LineEnding
                break;
            case MODE_FIRST_CHAR_AFTER_DELIMITER:
                // last character was COMMA or TSV insert empty String
                if (0 != i) {
                    strings.add(
                        CsvTsvStringListElement.string("")
                    );
                }
                break;
            case MODE_INSIDE_QUOTED:
                throw new EndOfTextException("Missing terminating '\"\'");
            case MODE_TERMINATING_QUOTE:
            case MODE_RAW_TEXT:
                strings.add(
                    CsvTsvStringListElement.string(element.toString())
                );
                break;
            default:
                NeverError.unhandledCase(
                    mode,
                    MODE_NL_OR_FIRST_CHAR,
                    MODE_FIRST_CHAR_AFTER_DELIMITER,
                    MODE_FIRST_CHAR_AFTER_NL,
                    MODE_INSIDE_QUOTED,
                    MODE_TERMINATING_QUOTE,
                    MODE_RAW_TEXT
                );
        }

        return factory.apply(strings);
    }

    private final static char CR_CHAR = '\r';

    private final static char NL_CHAR = '\n';

    private final static char DOUBLE_QUOTE_CHAR = '"';

    /**
     * Package private ctor use #parse or any would be mutator methods.
     */
    CsvTsvStringList(final List<CsvTsvStringListElement> strings) {
        super();
        this.strings = strings;
    }

    @Override
    public final String get(final int index) {
        final CsvTsvStringListElement string = this.strings.get(index);
        return null != string ?
            string.string :
            null;
    }

    @Override
    public final T deleteAtIndex(final int index) {
        final List<CsvTsvStringListElement> strings = Lists.array();
        strings.addAll(this.strings);
        strings.remove(index);

        return this.replaceStrings(strings);
    }

    @Override
    public final T deleteAll(final Collection<String> strings) {
        Objects.requireNonNull(strings, "strings");

        return this.deleteStringsIf(
            (final CsvTsvStringListElement element) -> strings.contains(element.string)
        );
    }

    @Override
    public final T deleteIf(final Predicate<? super String> predicate) {
        Objects.requireNonNull(predicate, "predicate");

        return this.deleteStringsIf(
            (final CsvTsvStringListElement element) -> predicate.test(element.string)
        );
    }

    private T deleteStringsIf(final Predicate<CsvTsvStringListElement> predicate) {
        final List<CsvTsvStringListElement> deleteAll = Lists.array();
        deleteAll.addAll(this.strings);
        deleteAll.removeIf(predicate);

        return this.replaceStrings(deleteAll);
    }

    @Override
    public T swap(final int left,
                  final int right) {
        T swapped = (T) this;

        if (left != right) {
            final List<CsvTsvStringListElement> before = this.strings;

            final CsvTsvStringListElement leftString = before.get(left);
            final CsvTsvStringListElement rightString = before.get(right);
            if (false == leftString.equals(rightString)) {

                // different need to return a new CsvTsvStringList
                final List<CsvTsvStringListElement> after = Lists.array();
                after.addAll(before);

                after.set(
                    right,
                    leftString
                );
                after.set(
                    left,
                    rightString
                );

                swapped = this.replaceStrings(after);
            }
        }

        return swapped;
    }

    @Override
    public final T setElements(final Collection<String> strings) {
        final T list;

        if (strings instanceof CsvStringList) {
            list = this.setCsvStringList((CsvStringList) strings);
        } else {
            if (strings instanceof TsvStringList) {
                list = this.setTsvStringList(
                    ((TsvStringList) strings)
                );
            } else {
                final List<CsvTsvStringListElement> previous = this.strings;
                final List<CsvTsvStringListElement> next = Lists.array();
                int i = 0;

                for (final String string : strings) {
                    Objects.requireNonNull(strings, "includes null string");

                    next.add(
                        i < previous.size() ?
                            previous.get(i)
                                .setString(string) :
                            CsvTsvStringListElement.string(string)
                    );

                    i++;
                }

                list = previous.equals(next) ?
                    (T) this :
                    this.setStrings(next);
            }
        }

        return list;
    }

    abstract T setCsvStringList(final CsvStringList csvStringList);

    abstract T setTsvStringList(final TsvStringList tsvStringList);

    private T setStrings(final List<CsvTsvStringListElement> strings) {
        return this.strings.equals(strings) ?
            (T) this :
            this.replaceStrings(strings);
    }

    abstract T replaceStrings(final List<CsvTsvStringListElement> strings);

    @Override
    public final int size() {
        return this.strings.size();
    }

    @Override
    public final void elementCheck(final String string) {
        Objects.requireNonNull(string, "string");
    }

    final List<CsvTsvStringListElement> strings;

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
                this,
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
        return this.strings.get(index) instanceof CsvTsvStringListElementStringLineBreak;
    }

    @Override
    public final T setLineBreak(final int index) {
        final T after;

        if (this.lineBreak(index)) {
            after = (T) this;
        } else {
            final List<CsvTsvStringListElement> copy = Lists.array();
            copy.addAll(this.strings);
            copy.set(
                index,
                copy.get(index)
                    .setLineBreak()
            );

            after = this.replaceStrings(copy);
        }

        return after;
    }

    @Override
    public final T removeLineBreak(final int index) {
        final T after;

        if (this.lineBreak(index)) {
            final List<CsvTsvStringListElement> copy = Lists.array();
            copy.addAll(this.strings);
            copy.set(
                index,
                copy.get(index)
                    .removeLineBreak()
            );

            after = this.replaceStrings(copy);
        } else {
            after = (T) this;
        }

        return after;
    }

    @Override
    public final T clearAllLineBreaks() {
        return this.setStrings(
            this.strings.stream()
                .map(CsvTsvStringListElement::removeLineBreak)
                .collect(Collectors.toList())
        );
    }

    @Override
    public final T setAllLineBreaks() {
        return this.setStrings(
            this.strings.stream()
                .map(CsvTsvStringListElement::setLineBreak)
                .collect(Collectors.toList())
        );
    }

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
        return this.strings.equals(other.strings);
    }
}
