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

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An enumeration with type safe methods to parse, fetch an empty instance and get the actual separator character.
 * Possible new entries include SEMI-COLON and PIPE.
 */
public final class DelimiterSeparatedValuesKind<T extends CsvTsvStringList<T>> {

    public final static DelimiterSeparatedValuesKind<CsvStringList> COMMA = new DelimiterSeparatedValuesKind<CsvStringList>(
        ',',
        CsvStringList.EMPTY::setElements,
        (final List<CsvTsvStringListElement> elements) -> elements.isEmpty() ?
            CsvStringList.EMPTY :
            new CsvStringList(elements),
        CsvStringList.EMPTY
    );

    public final static DelimiterSeparatedValuesKind<TsvStringList> TAB = new DelimiterSeparatedValuesKind<TsvStringList>(
        '\t',
        TsvStringList.EMPTY::setElements,
        (final List<CsvTsvStringListElement> elements) -> elements.isEmpty() ?
            TsvStringList.EMPTY :
            new TsvStringList(elements),
        TsvStringList.EMPTY
    );

    private DelimiterSeparatedValuesKind(final char character,
                                         final Function<List<String>, T> setElements,
                                         final Function<List<CsvTsvStringListElement>, T> newInstance,
                                         final T empty) {
        super();
        this.character = character;
        this.setElements = setElements;
        this.newInstance = newInstance;

        this.empty = empty;
    }

    public char character() {
        return this.character;
    }

    private final char character;

    /**
     * SetElements that returns an instance with the given {@link String} values.
     */
    public T setElements(final List<String> strings) {
        return this.setElements.apply(strings);
    }

    private final Function<List<String>, T> setElements;

    /**
     * Parses the given {@link String}, adding each encountered element to the given {@link Consumer}.
     */
    public T parse(final String text) {
        Objects.requireNonNull(text, "text");

        final char separatorChar = this.character;
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
                            strings.add(
                                CsvTsvStringListElement.stringLineBreak("")
                            );

                            mode = MODE_NL_OR_FIRST_CHAR;
                            break;
                        case NL_CHAR:
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

        return this.newInstance.apply(strings);
    }

    private final static char CR_CHAR = '\r';

    private final static char NL_CHAR = '\n';

    private final static char DOUBLE_QUOTE_CHAR = '"';

    private final Function<List<CsvTsvStringListElement>, T> newInstance;

    public T empty() {
        return this.empty;
    }

    private final T empty;
}
