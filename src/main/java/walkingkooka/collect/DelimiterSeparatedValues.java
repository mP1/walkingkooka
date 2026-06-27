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

package walkingkooka.collect;

import walkingkooka.collect.list.CsvStringList;
import walkingkooka.collect.list.DelimiterStringImmutableList;
import walkingkooka.collect.list.TsvStringList;
import walkingkooka.collect.set.CsvStringSet;
import walkingkooka.collect.set.DelimiterStringImmutableSet;
import walkingkooka.collect.set.TsvStringSet;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;

import java.util.Arrays;
import java.util.Collection;

/**
 * Provides support for a {@link Collection} of {@link String} values with different delimiters.
 */
public enum DelimiterSeparatedValues {

    CSV(CharacterConstant.COMMA) {
        @Override
        public CsvStringList emptyList() {
            return CsvStringList.EMPTY;
        }

        @Override
        public CsvStringSet emptySet() {
            return CsvStringSet.EMPTY;
        }
        
        @Override
        public CsvStringList parseList(final String string) {
            return CsvStringList.parse(string);
        }
    },

    TAB(CharacterConstant.TAB) {
        @Override
        public TsvStringList emptyList() {
            return TsvStringList.EMPTY;
        }

        @Override
        public TsvStringSet emptySet() {
            return TsvStringSet.EMPTY;
        }

        @Override
        public DelimiterStringImmutableList parseList(final String string) {
            return TsvStringList.parse(string);
        }
    };

    DelimiterSeparatedValues(final CharacterConstant character) {
        this.character = character;
    }

    public abstract DelimiterStringImmutableList emptyList();

    public abstract DelimiterStringImmutableSet emptySet();

    /**
     * Parses the {@link String} form using the delimiter into a {@link DelimiterStringImmutableList}.
     */
    public abstract DelimiterStringImmutableList parseList(final String string);

    /**
     * The delimiter character between values.
     */
    public final char character() {
        return this.character.character();
    }

    /**
     * The delimiter character.
     */
    final CharacterConstant character;

    /**
     * Find the {@link DelimiterSeparatedValues} for the character if it is supported.
     */
    public static DelimiterSeparatedValues forCharacter(final char character) {
        return Arrays.stream(values())
            .filter((DelimiterSeparatedValues dsv) -> dsv.character() == character)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported " + CharSequences.quoteIfChars(character)));
    }
}
