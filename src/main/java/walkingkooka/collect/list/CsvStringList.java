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
 * An immutable list of String elements.
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
        final List<String> elements = Lists.array();
        Csv.parse(
            text,
            elements::add
        );
        return elements.isEmpty() ?
            EMPTY :
            new CsvStringList(elements);
    }

    /**
     * Private ctor use #parse or any would be mutator methods.
     */
    private CsvStringList(final List<String> elements) {
        super();
        this.elements = elements;
    }

    @Override
    public String get(final int index) {
        return this.elements.get(index);
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public void elementCheck(final String element) {
        Objects.requireNonNull(element, "element");
    }

    @Override
    public CsvStringList setElements(final List<String> elements) {
        CsvStringList csvStringList;

        if (elements instanceof CsvStringList) {
            csvStringList = (CsvStringList) elements;
        } else {
            final ImmutableList<String> copy = Lists.immutable(
                Objects.requireNonNull(elements, "elements")
            );
            csvStringList = this.elements.equals(copy) ?
                this :
                copy.isEmpty() ?
                    EMPTY :
                    new CsvStringList(copy);
        }

        return csvStringList;
    }

    private final List<String> elements;

    // HasText..........................................................................................................

    /**
     * Note elements with quotes, commas, CR or NL will be quoted and double quotes escaped.
     */
    @Override
    public String text() {
        return Csv.toCsv(
            this.elements
        );
    }
}
