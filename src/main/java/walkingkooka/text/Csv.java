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

package walkingkooka.text;

import walkingkooka.reflect.PublicStaticHelper;

import java.util.Collection;
import java.util.Objects;

/**
 * Utilities to assist parsing CSV text or producing a CSV text from elements.
 * <br>
 * https://www.ietf.org/rfc/rfc4180.txt
 */
public final class Csv implements PublicStaticHelper {

//    /**
//     * Parses the given {@link String}, adding each encountered element to the given {@link Consumer}.
//     */
//    public static void parse(final String text,
//                             final Consumer<String> elements) {
//        Objects.requireNonNull(text, "text");
//        Objects.requireNonNull(elements, "elements");
//
//        final int MODE_FIRST_CHAR = 1;
//        final int MODE_INSIDE_QUOTED = 2;
//        final int MODE_TERMINATING_QUOTE = 3;
//        final int MODE_SEPARATOR = 4;
//        final int MODE_RAW_TEXT = 5;
//
//        int mode = MODE_FIRST_CHAR;
//
//        StringBuilder element = null;
//
//        int i = 0;
//        final int length = text.length();
//
//        while (i < length) {
//            final char c = text.charAt(i);
//
//            switch (mode) {
//                case MODE_FIRST_CHAR:
//                    if (DOUBLE_QUOTE_CHAR == c) {
//                        element = new StringBuilder();
//                        mode = MODE_INSIDE_QUOTED;
//                    } else {
//                        if (SEPARATOR_CHAR == c) {
//                            mode = MODE_FIRST_CHAR;
//                            elements.accept("");
//                        } else {
//                            element = new StringBuilder()
//                                .append(c);
//                            mode = MODE_RAW_TEXT;
//                        }
//                    }
//                    break;
//                case MODE_INSIDE_QUOTED:
//                    switch (c) {
//                        // double quote could be end of quoted string or escaped double quote.
//                        case DOUBLE_QUOTE_CHAR:
//                            mode = MODE_TERMINATING_QUOTE;
//                            break;
//                        default:
//                            element.append(c);
//                            break;
//                    }
//                    break;
//                case MODE_TERMINATING_QUOTE:
//                    if (DOUBLE_QUOTE_CHAR == c) {
//                        element.append(DOUBLE_QUOTE_CHAR);
//                        mode = MODE_INSIDE_QUOTED;
//                    } else {
//                        if (SEPARATOR_CHAR == c) {
//                            // quote was terminating
//                            elements.accept(element.toString());
//                            mode = MODE_FIRST_CHAR;
//                        } else {
//                            // trailing quote must be followed by separator or EOF, trailing spaces etc are an ICE.
//                            throw new InvalidCharacterException(
//                                text,
//                                i
//                            );
//                        }
//                    }
//                    break;
//                case MODE_SEPARATOR:
//                    if (SEPARATOR_CHAR != c) {
//                        throw new InvalidCharacterException(
//                            text,
//                            i
//                        );
//                    }
//                    elements.accept(element.toString());
//                    mode = MODE_FIRST_CHAR;
//                    break;
//                case MODE_RAW_TEXT:
//                    if (DOUBLE_QUOTE_CHAR == c || CR_CHAR == c || NL_CHAR == c) {
//                        throw new InvalidCharacterException(
//                            text,
//                            i
//                        );
//                    }
//                    if (SEPARATOR_CHAR == c) {
//                        elements.accept(
//                            element.toString()
//                        );
//                        mode = MODE_FIRST_CHAR;
//                    } else {
//                        element.append(c);
//                    }
//                    break;
//                default:
//                    NeverError.unhandledCase(
//                        mode,
//                        MODE_FIRST_CHAR,
//                        MODE_INSIDE_QUOTED,
//                        MODE_TERMINATING_QUOTE,
//                        MODE_SEPARATOR,
//                        MODE_RAW_TEXT
//                    );
//            }
//
//            i++;
//        }
//
//        // EOT
//        switch (mode) {
//            case MODE_FIRST_CHAR:
//                if (0 != i) {
//                    elements.accept("");
//                }
//                break;
//            case MODE_INSIDE_QUOTED:
//                throw new EndOfTextException("Missing terminating '\"\'");
//            case MODE_TERMINATING_QUOTE:
//            case MODE_SEPARATOR:
//            case MODE_RAW_TEXT:
//                elements.accept(element.toString());
//                break;
//            default:
//                NeverError.unhandledCase(
//                    mode,
//                    MODE_FIRST_CHAR,
//                    MODE_INSIDE_QUOTED,
//                    MODE_TERMINATING_QUOTE,
//                    MODE_SEPARATOR,
//                    MODE_RAW_TEXT
//                );
//        }
//    }

    public static String toCsv(final Collection<String> elements,
                               final char separator) {
        Objects.requireNonNull(elements, "elements");
        if('"' == separator) {
            throw new IllegalArgumentException("Invalid separator character is \"\"\"");
        }

        return CharacterConstant.with(separator)
            .toSeparatedString(
                elements,
                Csv::escapeIfNecessary
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

    private final static char CR_CHAR = '\r';
    private final static char NL_CHAR = '\n';

    private final static char DOUBLE_QUOTE_CHAR = '"';
    private final static char SEPARATOR_CHAR = ',';
    public final static CharacterConstant SEPARATOR = CharacterConstant.COMMA;

    /**
     * Private ctor use public methods
     */
    private Csv() {
        throw new UnsupportedOperationException();
    }
}
