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

import walkingkooka.EndOfTextException;
import walkingkooka.InvalidCharacterException;
import walkingkooka.NeverError;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Holds both a character and its {@link String} equivalent. This is ideal for constants that should
 * not be inlined and is intended to replace dual declaration of constants of for the character and
 * another to hold the {@link String} equivalent.
 */
final public class CharacterConstant implements CharSequence {

    final static char LOWER_BOUNDS = '\n';

    final static char UPPER_BOUNDS = 'z';

    /**
     * The cache holds only printables between {@link #LOWER_BOUNDS} and {@link #UPPER_BOUNDS}.
     */
    private final static CharacterConstant[] CONSTANTS = CharacterConstant.fill();

    private static CharacterConstant[] fill() {
        final CharacterConstant[] constants = new CharacterConstant[
            (CharacterConstant.UPPER_BOUNDS + 1) - CharacterConstant.LOWER_BOUNDS];
        for (char c = CharacterConstant.LOWER_BOUNDS; c < (CharacterConstant.UPPER_BOUNDS + 1); c++) {
            constants[c - CharacterConstant.LOWER_BOUNDS] = new CharacterConstant(c);
        }
        return constants;
    }

    /**
     * Factory that returns a {@link CharacterConstant} which may be shared/cached.
     */
    public static CharacterConstant with(final char c) {
        return (c >= CharacterConstant.LOWER_BOUNDS) && (c <= CharacterConstant.UPPER_BOUNDS) ?
            CharacterConstant.CONSTANTS[c - CharacterConstant.LOWER_BOUNDS] :
            new CharacterConstant(c);
    }

    /**
     * COMMA
     */
    public final static CharacterConstant COMMA = with(',');

    /**
     * TAB
     */
    public final static CharacterConstant TAB = with('\t');

    private CharacterConstant(final char character) {
        super();
        this.character = character;
        this.string = String.valueOf(character);
    }

    public char character() {
        return this.character;
    }

    private final char character;

    public String string() {
        return this.string;
    }

    private final String string;

    // SubSequence

    @Override
    public int length() {
        return 1;
    }

    @Override
    public char charAt(final int index) {
        if (0 != index) {
            this.failIndexOfOutBounds("index must be 0");
        }
        return this.character;
    }

    @Override
    public CharSequence subSequence(final int start, final int end) {
        if (0 != start) {
            this.failIndexOfOutBounds("start must be 0 but was " + start);
        }
        return 0 == end ? "" : 1 == end ? this : this.failSubSequence(end);
    }

    private CharSequence failSubSequence(final int end) {
        return this.failIndexOfOutBounds("end must be 0 or 1 but was " + end);
    }

    private CharSequence failIndexOfOutBounds(final String message) {
        throw new StringIndexOutOfBoundsException(message);
    }

    // parse............................................................................................................

    /**
     * Parses the given {@link String}, adding each encountered element to the given {@link Consumer}.
     */
    public void parse(final String text,
                      final Consumer<String> elements) {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(elements, "elements");

        final char separatorChar = this.character;

        final int MODE_FIRST_CHAR = 1;
        final int MODE_INSIDE_QUOTED = 2;
        final int MODE_TERMINATING_QUOTE = 3;
        final int MODE_SEPARATOR = 4;
        final int MODE_RAW_TEXT = 5;

        int mode = MODE_FIRST_CHAR;

        StringBuilder element = null;

        int i = 0;
        final int length = text.length();

        while (i < length) {
            final char c = text.charAt(i);

            switch (mode) {
                case MODE_FIRST_CHAR:
                    if (DOUBLE_QUOTE_CHAR == c) {
                        element = new StringBuilder();
                        mode = MODE_INSIDE_QUOTED;
                    } else {
                        if (separatorChar == c) {
                            mode = MODE_FIRST_CHAR;
                            elements.accept("");
                        } else {
                            element = new StringBuilder()
                                .append(c);
                            mode = MODE_RAW_TEXT;
                        }
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
                    if (DOUBLE_QUOTE_CHAR == c) {
                        element.append(DOUBLE_QUOTE_CHAR);
                        mode = MODE_INSIDE_QUOTED;
                    } else {
                        if (separatorChar == c) {
                            // quote was terminating
                            elements.accept(element.toString());
                            mode = MODE_FIRST_CHAR;
                        } else {
                            // trailing quote must be followed by separator or EOF, trailing spaces etc are an ICE.
                            throw new InvalidCharacterException(
                                text,
                                i
                            );
                        }
                    }
                    break;
                case MODE_SEPARATOR:
                    if (separatorChar != c) {
                        throw new InvalidCharacterException(
                            text,
                            i
                        );
                    }
                    elements.accept(element.toString());
                    mode = MODE_FIRST_CHAR;
                    break;
                case MODE_RAW_TEXT:
                    if (DOUBLE_QUOTE_CHAR == c || CR_CHAR == c || NL_CHAR == c) {
                        throw new InvalidCharacterException(
                            text,
                            i
                        );
                    }
                    if (separatorChar == c) {
                        elements.accept(
                            element.toString()
                        );
                        mode = MODE_FIRST_CHAR;
                    } else {
                        element.append(c);
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
        switch (mode) {
            case MODE_FIRST_CHAR:
                if (0 != i) {
                    elements.accept("");
                }
                break;
            case MODE_INSIDE_QUOTED:
                throw new EndOfTextException("Missing terminating '\"\'");
            case MODE_TERMINATING_QUOTE:
            case MODE_SEPARATOR:
            case MODE_RAW_TEXT:
                elements.accept(element.toString());
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
    }

    public String toDelimiteredString(final Collection<String> elements) {
        Objects.requireNonNull(elements, "elements");

        final char separator = this.character;
        if('"' == separator) {
            throw new IllegalArgumentException("Invalid separator character is \"\"\"");
        }

        return CharacterConstant.with(separator)
            .toSeparatedString(
                elements,
                this::escapeIfNecessary
            );
    }

    private String escapeIfNecessary(final String text) {
        String result = text;

        final int length = text.length();
        for (int i = 0; i < length; i++) {

            final char c = text.charAt(i);
            if(DOUBLE_QUOTE_CHAR == c || CR_CHAR == c || NL_CHAR == c || this.character == c) {
                result = DOUBLE_QUOTE_STRING.concat(
                    text.replace(
                        DOUBLE_QUOTE_STRING,
                        DOUBLE_DOUBLE_QUOTE_STRING
                    )
                ).concat(DOUBLE_QUOTE_STRING);

                i = length;
            }
        }

        return result;
    }

    private final static String DOUBLE_QUOTE_STRING = "\"";
    private final static String DOUBLE_DOUBLE_QUOTE_STRING = "\"\"";

    private final static char CR_CHAR = '\r';
    private final static char NL_CHAR = '\n';

    private final static char DOUBLE_QUOTE_CHAR = '"';

    // toSeparatedString..................................................................................................

    /**
     * THe inverse of the {link #parse}, turns the given values back into a CSV String.
     */
    public <T> String toSeparatedString(final Collection<T> values,
                                        final Function<T, String> component) {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(component, "component");

        return values.stream()
            .map(component)
            .collect(Collectors.joining(this.string));
    }

    // Object..........................................................................................................

    @Override
    public int hashCode() {
        return this.character;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || //
            other instanceof CharacterConstant && this.equals0((CharacterConstant) other);
    }

    private boolean equals0(final CharacterConstant other) {
        return this.equals(other.character);
    }

    public boolean equals(final char c) {
        return this.character == c;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
