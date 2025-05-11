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
import walkingkooka.text.HasText;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

/**
 * An immutable list of String elements.
 */
public final class CsvStringList extends AbstractList<String> implements ImmutableListDefaults<CsvStringList, String>,
    HasText {

    /**
     * An empty {@link CsvStringList}
     */
    public final static CsvStringList EMPTY = new CsvStringList(Lists.empty());

    /**
     * Parses CSV text according to RFC 4180.
     * <br>
     * https://www.ietf.org/rfc/rfc4180.txt
     */
    public static CsvStringList parse(final String text) {
        Objects.requireNonNull(text, "text");

        final int MODE_FIRST_CHAR = 1;
        final int MODE_INSIDE_QUOTED = 2;
        final int MODE_TERMINATING_QUOTE = 3;
        final int MODE_SEPARATOR = 4;
        final int MODE_RAW_TEXT = 5;

        int mode = MODE_FIRST_CHAR;

        final List<String> elements = Lists.array();
        StringBuilder element = null;

        int i = 0;
        final int length = text.length();

        while (i < length) {
            final char c = text.charAt(i);

            switch (mode) {
                case MODE_FIRST_CHAR:
                    switch (c) {
                        case DOUBLE_QUOTE_CHAR:
                            element = new StringBuilder();
                            mode = MODE_INSIDE_QUOTED;
                            break;
                        case SEPARATOR_CHAR:
                            mode = MODE_FIRST_CHAR;
                            elements.add("");
                            break;
                        default:
                            element = new StringBuilder()
                                .append(c);
                            mode = MODE_RAW_TEXT;
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
                        case SEPARATOR_CHAR:
                            // quote was terminating
                            elements.add(element.toString());
                            mode = MODE_FIRST_CHAR;
                            break;
                        default:
                            // trailing quote must be followed by separator or EOF, trailing spaces etc are an ICE.
                            throw new InvalidCharacterException(
                                text,
                                i
                            );
                    }
                    break;
                case MODE_SEPARATOR:
                    switch (c) {
                        case SEPARATOR_CHAR:
                            elements.add(element.toString());
                            mode = MODE_FIRST_CHAR;
                            break;
                        default:
                            // trailing quote must be followed by separator or EOF, trailing spaces etc are an ICE.
                            throw new InvalidCharacterException(
                                text,
                                i
                            );
                    }
                case MODE_RAW_TEXT:
                    switch (c) {
                        // Fields containing line breaks (CRLF), double quotes, and commas
                        // should be enclosed in double-quotes.  For example:
                        case DOUBLE_QUOTE_CHAR:
                        case CR_CHAR:
                        case NL_CHAR:
                            throw new InvalidCharacterException(
                                text,
                                i
                            );
                        case SEPARATOR_CHAR:
                            elements.add(
                                element.toString()
                            );
                            mode = MODE_FIRST_CHAR;
                            break;
                        default:
                            element.append(c);
                            break;
                    }
                    break;
                default:
                    NeverError.unhandledCase(
                        mode,
                        MODE_FIRST_CHAR,
                        MODE_INSIDE_QUOTED,
                        MODE_TERMINATING_QUOTE,
                        MODE_SEPARATOR,
                        MODE_RAW_TEXT
                    );
            }

            i++;
        }

        // EOT
        switch(mode) {
            case MODE_FIRST_CHAR:
                if(0 != i) {
                    elements.add("");
                }
                break;
            case MODE_INSIDE_QUOTED:
                throw new EndOfTextException("Missing terminating '\"\'");
            case MODE_TERMINATING_QUOTE:
            case MODE_SEPARATOR:
            case MODE_RAW_TEXT:
                elements.add(element.toString());
                break;
            default:
                NeverError.unhandledCase(
                    mode,
                    MODE_FIRST_CHAR,
                    MODE_INSIDE_QUOTED,
                    MODE_TERMINATING_QUOTE,
                    MODE_SEPARATOR,
                    MODE_RAW_TEXT
                );
        }

        return elements.isEmpty() ?
            EMPTY :
            new CsvStringList(elements);
    }

    private final static char CR_CHAR = '\r';
    private final static char NL_CHAR = '\n';

    private final static char DOUBLE_QUOTE_CHAR = '"';
    private final static char SEPARATOR_CHAR = ',';
    private final static CharacterConstant SEPARATOR = CharacterConstant.COMMA;

    /**
     * Private ctor use #parse or any would be mutator methods.
     */
    private CsvStringList(final List<String> elements) {
        super();
        this.elements = elements;
    }

    @Override
    public String get(final int index) {
        return this.elements.get(index);
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public void elementCheck(final String element) {
        Objects.requireNonNull(element, "element");
    }

    @Override
    public CsvStringList setElements(final List<String> elements) {
        CsvStringList csvStringList;

        if (elements instanceof CsvStringList) {
            csvStringList = (CsvStringList) elements;
        } else {
            final ImmutableList<String> copy = Lists.immutable(
                Objects.requireNonNull(elements, "elements")
            );
            csvStringList = this.elements.equals(copy) ?
                this :
                copy.isEmpty() ?
                    EMPTY :
                    new CsvStringList(copy);
        }

        return csvStringList;
    }

    private final List<String> elements;

    // HasText..........................................................................................................

    /**
     * Note elements with quotes, commas, CR or NL will be quoted and double quotes escaped.
     */
    @Override
    public String text() {
        return SEPARATOR.toSeparatedString(
            this,
            CsvStringList::escapeIfNecessary
        );
    }

    private static String escapeIfNecessary(final String text) {
        String result = text;

        final int length = text.length();
        for (int i = 0; i < length; i++) {
            switch (text.charAt(i)) {
                case DOUBLE_QUOTE_CHAR:
                case CR_CHAR:
                case NL_CHAR:
                case SEPARATOR_CHAR:
                    result = DOUBLE_QUOTE_STRING.concat(
                        text.replace(
                            DOUBLE_QUOTE_STRING,
                            DOUBLE_DOUBLE_QUOTE_STRING
                        )
                    ).concat(DOUBLE_QUOTE_STRING);

                    i = length;
                    break;
                default:
                    break;
            }
        }

        return result;
    }

    private final static String DOUBLE_QUOTE_STRING = "\"";
    private final static String DOUBLE_DOUBLE_QUOTE_STRING = "\"\"";
}
