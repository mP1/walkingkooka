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

import walkingkooka.text.CharSequences;
import walkingkooka.text.HasMultiLineText;
import walkingkooka.text.LineEnding;
import walkingkooka.text.MultiLineText;
import walkingkooka.text.TextContext;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An immutable list of {@link String} that allows null elements.
 */
public final class StringList extends AbstractList<String>
    implements ImmutableListDefaults<StringList, String>,
    CanRemoveTrailingNullOrEmptyStrings,
    HasCsvStringList,
    HasMultiLineText,
    HasTsvStringList,
    StringLikeList {

    /**
     * An empty {@link StringList}.
     */
    public final static StringList EMPTY = new StringList(
        Lists.empty()
    );

    // @VisibleForTesting
    StringList(final List<String> strings) {
        this.strings = strings;
    }

    @Override
    public String get(int index) {
        return this.strings.get(index);
    }

    @Override
    public int size() {
        return this.strings.size();
    }

    private final List<String> strings;

    @Override
    public void elementCheck(final String string) {
        // nulls are allowed.
    }

    @Override
    public StringList setElements(final Collection<String> strings) {
        return (StringList)
            this.setElementsMaybeCreate(
                strings,
                (Collection<String> collection) -> collection instanceof StringList,
                this::elementCheck,
                this::replaceStrings
            );
    }

    @Override
    public StringList subList(final int from,
                              final int to) {
        return 0 == from && this.size() == to ?
            this :
            this.replaceStrings(
                this.strings.subList(
                    from,
                    to
                )
            );
    }

    private StringList replaceStrings(final List<String> strings) {
        return strings.isEmpty() ?
            EMPTY :
            new StringList(strings);
    }

    // CanRemoveTrailingNullOrEmptyStrings..............................................................................

    @Override
    public StringList removeTrailingNullOrEmptyStrings() {
        return (StringList) CanRemoveTrailingNullOrEmptyStrings.super.removeTrailingNullOrEmptyStrings();
    }

    // HasCsvStringList.................................................................................................

    /**
     * Converts this {@link StringList} into a {@link CsvStringList} with null elements converted into empty strings.
     */
    @Override
    public CsvStringList csvStringList() {
        return CsvStringList.EMPTY.setElements(
            this.strings.stream()
                .map((final String string) -> CharSequences.nullToEmpty(string).toString())
                .collect(Collectors.toList())
        );
    }

    // HasMultiLineText.................................................................................................

    /**
     * Note that elements are included verbatim, nothing is escaped.
     */
    @Override
    public MultiLineText multiLineText(final TextContext context) {
        Objects.requireNonNull(context, "context");

        final LineEnding lineEnding = context.lineEnding();

        return MultiLineText.EMPTY.setText(
            this.strings.stream()
            .collect(
                Collectors.joining(
                    lineEnding,
                    "",
                    lineEnding
                )
            )
        );
    }

    // HasTsvStringList.................................................................................................

    @Override
    public TsvStringList tsvStringList() {
        return TsvStringList.EMPTY.setElements(this);
    }
}
