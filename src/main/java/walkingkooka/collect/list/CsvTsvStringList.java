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
import walkingkooka.text.HasMultiLineTextAndCollectionString;
import walkingkooka.text.HasTextWithSeparator;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

/**
 * Base class for both {@link CsvStringList} and {@link TsvStringList}.
 */
abstract class CsvTsvStringList<T extends ImmutableListDefaults<T, String>> extends AbstractList<String>
    implements CanRemoveTrailingNullOrEmptyStrings,
    DelimiterStringImmutableList,
    ImmutableListDefaults<T, String>,
    HasCsvStringList,
    HasMultiLineTextAndCollectionString,
    HasTextWithSeparator,
    HasTsvStringList {

    /**
     * Package private ctor use #parse or any would be mutator methods.
     */
    CsvTsvStringList(final List<String> strings) {
        super();
        this.strings = strings;
    }

    @Override
    public final String get(final int index) {
        return this.strings.get(index);
    }

    @Override
    public final int size() {
        return this.strings.size();
    }

    @Override
    public final void elementCheck(final String string) {
        Objects.requireNonNull(string, "string");
    }

    final List<String> strings;

    // HasCsvStringList.................................................................................................

    @Override
    public final CsvStringList csvStringList() {
        return CsvStringList.EMPTY.setElements(this);
    }

    // HasTextWithSeparator.............................................................................................

    @Override
    public final CharSequence defaultTextSeparator() {
        return this.characterConstant();
    }

    @Override
    public final CharSequence textSeparator(final int index) {
        return this.characterConstant();
    }

    abstract CharacterConstant characterConstant();

    /**
     * Note strings with quotes, commas, CR or NL will be quoted and double quotes escaped.
     */
    @Override
    public final String textWithSeparator(final CharSequence separator) {
        final String stringSeparator = String.valueOf(separator);

        return this.characterConstant()
            .toDelimiteredString(
                this.strings,
                (int position) -> stringSeparator
            );
    }

    // HasTsvStringList.................................................................................................

    @Override
    public final TsvStringList tsvStringList() {
        return TsvStringList.EMPTY.setElements(this);
    }
}
