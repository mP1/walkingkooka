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

import walkingkooka.text.Csv;
import walkingkooka.text.HasText;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

/**
 * An immutable list of String elements. Note null elements are not allowed.
 */
public final class CsvStringList extends AbstractList<String> implements ImmutableListDefaults<CsvStringList, String>,
    HasText {

    /**
     * An empty {@link CsvStringList}
     */
    public final static CsvStringList EMPTY = new CsvStringList(Lists.empty());

    /**
     * Parses CSV text according to RFC 4180.
     * <br>
     * https://www.ietf.org/rfc/rfc4180.txt
     */
    public static CsvStringList parse(final String text) {
        final List<String> strings = Lists.array();
        Csv.parse(
            text,
            strings::add
        );
        return strings.isEmpty() ?
            EMPTY :
            new CsvStringList(strings);
    }

    /**
     * Private ctor use #parse or any would be mutator methods.
     */
    private CsvStringList(final List<String> strings) {
        super();
        this.strings = strings;
    }

    @Override
    public String get(final int index) {
        return this.strings.get(index);
    }

    @Override
    public int size() {
        return this.strings.size();
    }

    @Override
    public void elementCheck(final String string) {
        Objects.requireNonNull(string, "string");
    }

    @Override
    public CsvStringList setElements(final List<String> strings) {
        CsvStringList csvStringList;

        if (strings instanceof CsvStringList) {
            csvStringList = (CsvStringList) strings;
        } else {
            final List<String> copy = Lists.array();
            for(final String string : strings) {
                Objects.requireNonNull(strings, "includes null string");
                copy.add(string);
            }
            csvStringList = this.strings.equals(copy) ?
                this :
                copy.isEmpty() ?
                    EMPTY :
                    new CsvStringList(copy);
        }

        return csvStringList;
    }

    private final List<String> strings;

    // HasText..........................................................................................................

    /**
     * Note strings with quotes, commas, CR or NL will be quoted and double quotes escaped.
     */
    @Override
    public String text() {
        return Csv.toCsv(
            this.strings
        );
    }
}
