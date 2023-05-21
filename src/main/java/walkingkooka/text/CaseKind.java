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

import java.util.Objects;

/**
 * Utility to change text in one case format to a new format.
 */
public enum CaseKind {
    /**
     * A naming standard where text words are joined with no separator, with all chunks except the first having the
     * first character capitalized and other text lower-cased.
     * <code>abcDefGhi</code>
     */
    CAMEL {
        @Override
        boolean isEndOfChunk(final int i,
                             final char c) {
            return 0 == i || Character.isUpperCase(c);
        }

        @Override
        void insertSeparator(final StringBuilder b) {
            // nop
        }

        @Override
        char sourceBegin(final int i,
                         final char c) {
            return Character.toLowerCase(c);
        }

        @Override
        char destBegin(final int i,
                       final char c) {
            return i == 0 ?
                    Character.toLowerCase(c) :
                    Character.toUpperCase(c);
        }
    },

    /**
     * Text is separated by a dash or minute, note text is not lower-cased.<code>abc-def-ghi</code>
     */
    KEBAB {
        @Override
        boolean isEndOfChunk(final int i,
                             final char c) {
            return '-' == c;
        }

        @Override
        void insertSeparator(final StringBuilder b) {
            b.append('-');
        }

        @Override
        char sourceBegin(final int i,
                         final char c) {
            return c;
        }

        @Override
        char destBegin(final int i,
                       final char c) {
            return c;
        }
    }, // lowerToUpper, // dash

    /**
     * Text that follows the Pascal way of naming variables, words have no separator, and the first letter of each chunk is
     * capitalized, and other characters lower cased.
     * <code>AbcDef</code>
     */
    PASCAL {
        @Override
        boolean isEndOfChunk(final int i,
                             final char c) {
            return Character.isUpperCase(c);
        }

        @Override
        void insertSeparator(final StringBuilder b) {
            // nop
        }

        @Override
        char sourceBegin(final int i,
                         final char c) {
            return Character.toLowerCase(c);
        }

        @Override
        char destBegin(final int i,
                       final char c) {
            return Character.toUpperCase(c);
        }
    }, // lowerToUpper

    /**
     * A kind of text where words are separated by underscores. Note text characters are not converted to upper case.<br>
     * <code>abc_def</code>
     */
    SNAKE {
        @Override
        boolean isEndOfChunk(final int i,
                             final char c) {
            return '_' == c;
        }

        @Override
        void insertSeparator(final StringBuilder b) {
            b.append('_');
        }

        @Override
        char sourceBegin(final int i,
                         final char c) {
            return c;
        }

        @Override
        char destBegin(final int i,
                       final char c) {
            return c;
        }
    }; // lowerToUpper

    public final String change(final String text,
                               final CaseKind to) {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(to, "to");

        return text.isEmpty() || this == to ?
                text :
                change0(
                        text,
                        to
                );
    }

    private String change0(final String text,
                           final CaseKind to) {
        final StringBuilder b = new StringBuilder();

        final int length = text.length();
        int i = 0;

        while (i < length) {
            final char c = text.charAt(i);

            if (this.isEndOfChunk(i, c)) {
                if(this.shouldIgnoreSeparator()) {
                    i++;
                }

                if (i < length) {
                    if (i > 0) {
                        to.insertSeparator(b);
                    }

                    b.append(
                            to.destBegin(
                                    i,
                                    this.sourceBegin(
                                            i,
                                            text.charAt(i)
                                    )
                            )
                    );

                    i++;
                }
            } else {
                i++;
                b.append(c);
            }
        }

        return b.toString();
    }

    /**
     * eg: snake will return true when an underscore is encountered.
     */
    abstract boolean isEndOfChunk(final int i,
                                  final char c);

    /**
     * Snake and Kebab will return true.
     */
    final boolean shouldIgnoreSeparator() {
        return KEBAB == this || SNAKE == this;
    }

    /**
     * eg: snake will append an underscore.
     */
    abstract void insertSeparator(final StringBuilder b);

    /**
     * eg: camel case will lower case the letter.
     */
    abstract char sourceBegin(final int i,
                              final char c);

    /**
     * eg: camel case will upper case the letter
     */
    abstract char destBegin(final int i,
                            final char c);

    /**
     * Turns the {@link Enum#name()} into its kebab equivalent.
     */
    public static String kebabEnumName(final Enum<?> value) {
        Objects.requireNonNull(value, "value");

        return value.name()
                .toLowerCase()
                .replace('_', '-');
    }
}
