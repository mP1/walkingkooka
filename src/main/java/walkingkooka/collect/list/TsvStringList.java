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
import walkingkooka.text.TextContext;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An immutable list of String non NULL elements, that may be encoded to a CSV {@link String} as documented in
 * https://www.ietf.org/rfc/rfc4180.txt.
 * <br>
 * Special characters such as line endings, quotes or even tabs will be encoded within a double quoted token.
 * It is also possible to set individual tokens to be followed by a {@link walkingkooka.text.LineEnding} rather than a
 * TAB when converted by to text using {@link #multiLineText(TextContext)}.
 * <br>
 * The {@link #parse(String)} is provided and honours the RFC transforming the provided text into a {@link CsvStringList}
 * instance.
 */
public final class TsvStringList extends CsvTsvStringList<TsvStringList> {

    /**
     * An empty {@link TsvStringList}
     */
    public final static TsvStringList EMPTY = new TsvStringList(
        Lists.empty()
    );

    /**
     * Helper that combines mapping elements from a source list into a {@link TsvStringList}.
     */
    public static <T> TsvStringList makeTsvStringList(final List<T> list,
                                                      final Function<T, String> mapper) {
        Objects.requireNonNull(list, "list");
        Objects.requireNonNull(mapper, "mapper");

        return EMPTY.replaceStrings(
            list.stream()
                .map(
                    (T value) -> CsvTsvStringListElement.string(
                        Objects.requireNonNull(
                            mapper.apply(value),
                            "Mapper returned null"
                        )
                    )
                ).collect(Collectors.toList())
        );
    }

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
