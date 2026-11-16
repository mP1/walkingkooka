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

import java.util.List;

/**
 * An immutable list of String elements. Note null elements are not allowed.
 */
public final class TsvStringList extends CsvTsvStringList<TsvStringList> {

    /**
     * An empty {@link TsvStringList}
     */
    public final static TsvStringList EMPTY = new TsvStringList(
        Lists.empty()
    );

    /**
     * Parses TSV text according to RFC 4180, replaces commas with tabs
     * <br>
     * https://www.ietf.org/rfc/rfc4180.txt
     */
    public static TsvStringList parse(final String text) {
        final TsvStringList tsvStringList = parse0(
            text,
            SEPARATOR.character(),
            TsvStringList::new
        );

        return tsvStringList.isEmpty() ?
            EMPTY :
            tsvStringList;
    }

    // @VisibleForTesting
    TsvStringList(final List<CsvTsvStringListElement> strings) {
        super(strings);
    }

    @Override
    TsvStringList setCsvStringList(final CsvStringList csvStringList) {
        return this.replaceStrings(csvStringList.strings);
    }

    @Override
    TsvStringList setTsvStringList(final TsvStringList tsvStringList) {
        return tsvStringList;
    }

    @Override
    TsvStringList replaceStrings(final List<CsvTsvStringListElement> strings) {
        return strings.isEmpty() ?
            EMPTY :
            new TsvStringList(strings);
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
}
