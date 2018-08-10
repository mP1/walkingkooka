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
 */

package walkingkooka.text;

import walkingkooka.compare.Comparables;
import walkingkooka.compare.Comparators;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.util.systemproperty.SystemProperty;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * May be used of an option and includes numerous helpful methods.
 */
public enum CaseSensitivity {

    /**
     * Case is important when doing operations.
     */
    SENSITIVE {
        @Override
        char maybeLowercase(final char c) {
            return c;
        }

        @Override
        public int hash(final char c) {
            return Character.hashCode(c);
        }

        @Override
        public <C extends CharSequence> Comparator<C> comparator() {
            return CaseSensitivityComparator.sensitive();
        }

        @Override
        String toStringSuffix() {
            return "";
        }
    },

    /**
     * Case is not important when doing operations.
     */
    INSENSITIVE {
        @Override
        char maybeLowercase(final char c) {
            return Character.toLowerCase(c);
        }

        @Override
        public int hash(final char c) {
            return Character.hashCode(Character.toLowerCase(c));
        }

        @Override
        public <C extends CharSequence> Comparator<C> comparator() {
            return CaseSensitivityComparator.insensitive();
        }

        @Override
        String toStringSuffix() {
            return " (CaseInsensitive)";
        }
    };

    /**
     * Tests if the two {@link CharSequence} are equal respecting the current case sensitivity.
     */
    final public boolean equals(final CharSequence chars, final CharSequence otherChars) {
        return Comparators.EQUAL == this.comparator().compare(chars, otherChars);
    }

    /**
     * Tests if the first {@link CharSequence} starts with the second.
     */
    final public boolean startsWith(final CharSequence chars, final CharSequence startsWith) {
        Objects.requireNonNull(chars, "chars");
        Objects.requireNonNull(startsWith, "startsWith");

        boolean result = false;

        final int startsWithLength = startsWith.length();
        if (startsWithLength <= chars.length()) {
            result = true;

            for (int i = 0; i < startsWithLength; i++) {
                final char c = chars.charAt(i);
                final char d = startsWith.charAt(i);
                if (false == this.isEqual(c, d)) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Tests if the first {@link CharSequence} ends with the second {@link CharSequence} using the
     * current sensitivity.
     */
    final public boolean endsWith(final CharSequence chars, final CharSequence endsWith) {
        Objects.requireNonNull(chars, "chars");
        Objects.requireNonNull(endsWith, "endsWith");

        boolean result = false;

        final int stringLength = chars.length();
        final int endsWithLength = endsWith.length();
        if (endsWithLength <= stringLength) {
            result = true;

            for (int i = 0; i < endsWithLength; i++) {
                final char c = chars.charAt(stringLength - 1 - i);
                final char d = endsWith.charAt(endsWithLength - 1 - i);
                if (false == this.isEqual(c, d)) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Tests if the search for {@link CharSequence} are present in the given {@link CharSequence}
     */
    final public boolean contains(final CharSequence chars, final CharSequence searchFor) {
        return -1 != this.indexOf(chars, searchFor);
    }

    /**
     * Attempts to find the {@link CharSequence searchFor} within the {@link CharSequence}
     */
    final public int indexOf(final CharSequence chars, final CharSequence searchFor) {
        Objects.requireNonNull(chars, "chars");
        Objects.requireNonNull(searchFor, "searchFor");

        int index = -1;
        final int stringLength = chars.length();
        final int searchForLength = searchFor.length();
        if ((stringLength > 0) && (searchForLength > 0) && (searchForLength <= stringLength)) {
            final char firstCharOfTest = this.maybeLowercase(searchFor.charAt(0));
            final int lastCharSequenceCharacterToCheck = (stringLength - searchForLength) + 1;

            for (int i = 0; i < lastCharSequenceCharacterToCheck; i++) {
                if (firstCharOfTest == this.maybeLowercase(chars.charAt(i))) {
                    index = i;
                    for (int j = 1; j < searchForLength; j++) {
                        final char c = chars.charAt(i + j);
                        final char otherChar = searchFor.charAt(j);
                        if (false == this.isEqual(c, otherChar)) {
                            index = -1;
                            break;
                        }
                    }
                    if (-1 != index) {
                        break;
                    }
                }
            }
        }
        return index;
    }

    /**
     * Only makes the character lowercase if {@link #INSENSITIVE}. Only used by {@link
     * #indexOf(CharSequence, CharSequence)}
     */
    abstract char maybeLowercase(final char c);

    /**
     * Tests the two characters for equality.
     */
    final public boolean isEqual(final char c, final char other) {
        return Comparables.EQUAL == this.compare(c, other);
    }

    /**
     * Hashes a {@link CharSequence} which may be null, using the current sensitivity.
     */
    public int hash(final CharSequence chars) {
        int hash = 0;

        if (null != chars) {
            final int length = chars.length();
            for (int i = 0; i < length; i++) {
                hash = (31 * hash) + this.hash(chars.charAt(i));
            }
        }
        return hash;
    }

    /**
     * Hashes the given character.
     */
    abstract public int hash(char c);

    /**
     * Compares the two characters for equality.
     */
    final public int compare(final char c, final char other) {
        return this.maybeLowercase(c) - this.maybeLowercase(other);
    }

    /**
     * Returns a {@link Comparator} for comparing any two {@link CharSequence} ignoring type.
     */
    abstract public <C extends CharSequence> Comparator<C> comparator();

    /**
     * Gets a {@link CharPredicate} that may or may not be case sensitive.
     */
    final public CharPredicate charPredicate(final char c) {
        return CaseSensitivityCharPredicate.with(c, this);
    }

    /**
     * Handles the toString implementation of {@link CaseSensitivityCharPredicate#toString()}
     */
    final String toString(final char c) {
        return CharSequences.quoteAndEscape(c) + this.toStringSuffix();
    }

    /**
     * Gets a {@link Predicate} that may or may not be case sensitive.
     */
    final public <C extends CharSequence> Predicate<C> predicate(final C chars) {
        return CaseSensitivityCharSequencePredicate.with(chars, this);
    }

    /**
     * Gets a {@link Predicate} that may or may not be case sensitive.
     */
    final public <C extends CharSequence> Predicate<C> predicateContains(final C contains) {
        return CaseSensitivityContainsCharSequencePredicate.with(contains, this);
    }

    /**
     * Gets a {@link Predicate} that may or may not be case sensitive.
     */
    final public <C extends CharSequence> Predicate<C> predicateEndsWith(final C endsWith) {
        return CaseSensitivityEndsWithCharSequencePredicate.with(endsWith, this);
    }

    /**
     * Gets a {@link Predicate} that may or may not be case sensitive.
     */
    final public <C extends CharSequence> Predicate<C> predicateStartsWith(final C startsWith) {
        return CaseSensitivityStartsWithCharSequencePredicate.with(startsWith, this);
    }

    /**
     * Handles the toString implementation of {@link CaseSensitivityCharSequencePredicateTemplate}
     */
    final String toString(final String prefix, final CharSequence c) {
        return prefix + CharSequences.quote(c) + this.toStringSuffix();
    }

    abstract String toStringSuffix();

    /**
     * A boolean system property that allows the actual {@link CaseSensitivity} instance to be selected,
     * overriding a tests involving {@link java.io.File}.
     * A boolean value of true indicates the file system is case sensitive, any other value means its not.
     */
    public final static SystemProperty FILE_SYSTEM_PROPERTY = SystemProperty.FILE_SYSTEM_CASE_SENSITIVITY;

    // @VisibilityForTesting
    static volatile CaseSensitivity FILE_SYSTEM;

    /**
     * Returns a {@link CaseSensitivity} that matches the case sensitivity of file names.
     */
    public static CaseSensitivity fileSystem() {
        if(null==FILE_SYSTEM) {
            CaseSensitivity sensitivity = fromSystemProperty();
            if(null==sensitivity){
                sensitivity = fromFileEqualsTests();
            }
            FILE_SYSTEM = sensitivity;
        }
        return FILE_SYSTEM;
    }

    private static CaseSensitivity fromSystemProperty() {
        final String systemPropertyValue = FILE_SYSTEM_PROPERTY.propertyValue();
        return CharSequences.isNullOrEmpty(systemPropertyValue) ?
                null :
                Boolean.valueOf(systemPropertyValue) ?
                        SENSITIVE :
                        INSENSITIVE;
    }

    /**
     * Create two paths with the different case and then checks if they are the same file using {@link Files#isSameFile(Path, Path)}.
     */
    private static CaseSensitivity fromFileEqualsTests() {
        try {
            final String filename = "abc";
            return Files.isSameFile(Paths.get(filename.toLowerCase()), Paths.get(filename.toUpperCase())) ?
                    INSENSITIVE :
                    SENSITIVE;
        } catch (final Exception cause) {
            throw new Error(cause);
        }
    }
}
