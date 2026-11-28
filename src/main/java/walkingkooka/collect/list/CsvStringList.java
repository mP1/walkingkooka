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
 * Special characters such as line endings, quotes or even commas will be encoded
 * within a double quoted token. It is also possible to set individual tokens to be followed by a {@link walkingkooka.text.LineEnding}
 * rather than a COMMA when converted by to text using {@link #multiLineText(TextContext)}.
 * <br>
 * The {@link #parse(String)} is provided and honours the RFC transforming the provided text into a {@link CsvStringList}
 * instance.
 */
public final class CsvStringList extends CsvTsvStringList<CsvStringList> {

    /**
     * An empty {@link CsvStringList}
     */
    public final static CsvStringList EMPTY = new CsvStringList(
        Lists.empty()
    );

    /**
     * Helper that combines mapping elements from a source list into a {@link CsvStringList}.
     * Not the mapper cannot return null
     */
    public static <T> CsvStringList makeCsvStringList(final List<T> list,
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
     * Parses CSV text according to RFC 4180.
     * <br>
     * https://www.ietf.org/rfc/rfc4180.txt
     */
    public static CsvStringList parse(final String text) {
        return DelimiterSeparatedValuesKind.COMMA.parse(text);
    }

    // @VisibleForTesting
    CsvStringList(final List<CsvTsvStringListElement> strings) {
        super(strings);
    }

    @Override
    CsvStringList setCsvStringList(final CsvStringList csvStringList) {
        return csvStringList;
    }

    @Override
    CsvStringList setTsvStringList(final TsvStringList tsvStringList) {
        return this.replaceStrings(tsvStringList.strings);
    }

    @Override
    CsvStringList replaceStrings(final List<CsvTsvStringListElement> strings) {
        return strings.isEmpty() ?
            EMPTY :
            new CsvStringList(strings);
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

    // delimiterSeparatedValuesKind.....................................................................................

    @Override
    public DelimiterSeparatedValuesKind<CsvStringList> delimiterSeparatedValuesKind() {
        return DelimiterSeparatedValuesKind.COMMA;
    }
}
