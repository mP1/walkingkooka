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
public final class TsvStringList extends CsvTsvStringList<TsvStringList> {

    /**
     * An empty {@link TsvStringList}
     */
    public final static TsvStringList EMPTY = new TsvStringList(Lists.empty());

    /**
     * Parses TSV text according to RFC 4180, replaces commas with tabs
     * <br>
     * https://www.ietf.org/rfc/rfc4180.txt
     */
    public static TsvStringList parse(final String text) {
        final List<String> strings = Lists.array();

        SEPARATOR.parse(
            text,
            strings::add
        );

        return strings.isEmpty() ?
            EMPTY :
            new TsvStringList(strings);
    }

    private TsvStringList(final List<String> strings) {
        this(
            strings,
            null
        );
    }

    // @VisibleForTesting
    TsvStringList(final List<String> strings,
                  final BitSet lineBreaks) {
        super(
            strings,
            lineBreaks
        );
    }

    @Override
    public TsvStringList setElements(final Collection<String> strings) {
        final TsvStringList tsvStringList;

        if (strings instanceof TsvStringList) {
            tsvStringList = (TsvStringList) strings;
        } else {
            final List<String> copy;

            if (strings instanceof CsvStringList) {
                tsvStringList = this.replaceStrings(
                    ((CsvStringList) strings)
                        .strings
                );
            } else {
                copy = Lists.array();
                for (final String string : strings) {
                    Objects.requireNonNull(strings, "includes null string");
                    copy.add(string);
                }

                tsvStringList = this.strings.equals(copy) ?
                    this :
                    this.replaceStrings(copy);
            }
        }

        return tsvStringList;
    }

    private TsvStringList replaceStrings(final List<String> strings) {
        return strings.isEmpty() ?
            EMPTY :
            new TsvStringList(
                strings,
                this.lineBreaks
            );
    }

    // CanRemoveTrailingNullOrEmptyStrings..............................................................................

    @Override
    public TsvStringList removeTrailingNullOrEmptyStrings() {
        return (TsvStringList) super.removeTrailingNullOrEmptyStrings();
    }

    // HasTextWithSeparator.............................................................................................

    public final static CharacterConstant SEPARATOR = CharacterConstant.TAB;

    @Override
    CharacterConstant characterConstant() {
        return SEPARATOR;
    }

    @Override
    TsvStringList replaceLineBreaks(final BitSet lineBreaks) {
        return new TsvStringList(
            this.strings,
            lineBreaks
        );
    }
}
