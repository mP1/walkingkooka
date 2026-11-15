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

import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * An immutable list of String elements. Note null elements are not allowed.
 */
public final class CsvStringList extends CsvTsvStringList<CsvStringList> {

    /**
     * An empty {@link CsvStringList}
     */
    public final static CsvStringList EMPTY = new CsvStringList(
        Lists.empty(),
        null
    );

    /**
     * Parses CSV text according to RFC 4180.
     * <br>
     * https://www.ietf.org/rfc/rfc4180.txt
     */
    public static CsvStringList parse(final String text) {
        final CsvStringList csvStringList = parse0(
            text,
            SEPARATOR.character(),
            CsvStringList::new
        );

        return csvStringList.isEmpty() ?
            EMPTY :
            csvStringList;
    }

    // @VisibleForTesting
    CsvStringList(final List<String> strings,
                  final BitSet lineBreaks) {
        super(
            strings,
            lineBreaks
        );
    }

    @Override
    public CsvStringList setElements(final Collection<String> strings) {
        final CsvStringList csvStringList;

        if (strings instanceof CsvStringList) {
            csvStringList = (CsvStringList) strings;
        } else {
            final List<String> copy;

            if (strings instanceof TsvStringList) {
                csvStringList = this.replaceStrings(
                    ((TsvStringList) strings)
                        .strings
                );
            } else {
                copy = Lists.array();
                for (final String string : strings) {
                    Objects.requireNonNull(strings, "includes null string");
                    copy.add(string);
                }

                csvStringList = this.strings.equals(copy) ?
                    this :
                    this.replaceStrings(copy);
            }
        }

        return csvStringList;
    }

    private CsvStringList replaceStrings(final List<String> strings) {
        return strings.isEmpty() ?
            EMPTY :
            new CsvStringList(
                strings,
                this.lineBreaks
            );
    }

    // CanRemoveTrailingNullOrEmptyStrings..............................................................................

    @Override
    public CsvStringList removeTrailingNullOrEmptyStrings() {
        return (CsvStringList) super.removeTrailingNullOrEmptyStrings();
    }

    // HasTextWithSeparator.............................................................................................

    public final static CharacterConstant SEPARATOR = CharacterConstant.COMMA;

    @Override
    CharacterConstant characterConstant() {
        return SEPARATOR;
    }

    @Override
    CsvStringList replaceLineBreaks(final BitSet lineBreaks) {
        return new CsvStringList(
            this.strings,
            lineBreaks
        );
    }
}
