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

package walkingkooka.collect.set;

import walkingkooka.Cast;
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.text.Csv;
import walkingkooka.text.HasText;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A {@link SortedSet} of {@link String}. Note strings are case-sensitive.
 */
public final class CsvStringSet extends AbstractSet<String>
    implements ImmutableSortedSetDefaults<CsvStringSet, String>,
    HasText {

    /**
     * An empty {@link CsvStringSet}
     */
    public final static CsvStringSet EMPTY = new CsvStringSet(SortedSets.empty());

    /**
     * Parses CSV text according to RFC 4180.
     * <br>
     * https://www.ietf.org/rfc/rfc4180.txt
     */
    public static CsvStringSet parse(final String text) {
        final SortedSet<String> strings = SortedSets.tree();
        Csv.parse(
            text,
            strings::add
        );
        return strings.isEmpty() ?
            EMPTY :
            new CsvStringSet(strings);
    }

    private CsvStringSet(final SortedSet<String> string) {
        super();
        this.strings = string;
    }

    @Override
    public Iterator<String> iterator() {
        return Iterators.readOnly(this.strings.iterator());
    }

    @Override
    public int size() {
        return this.strings.size();
    }

    @Override
    public SortedSet<String> toSet() {
        return new TreeSet<>(this.strings);
    }

    @Override
    public Comparator<String> comparator() {
        return Cast.to(
            this.strings.comparator()
        );
    }

    @Override
    public CsvStringSet subSet(final String from,
                                    final String to) {
        return this.setElements(
            this.strings.subSet(
                from,
                to
            )
        );
    }

    @Override
    public CsvStringSet headSet(final String to) {
        return this.setElements(
            this.strings.headSet(to)
        );
    }

    @Override
    public CsvStringSet tailSet(final String from) {
        return this.setElements(
            this.strings.tailSet(from)
        );
    }

    @Override
    public String first() {
        return this.strings.first();
    }

    @Override
    public String last() {
        return this.strings.last();
    }

    @Override
    public void elementCheck(final String string) {
        Objects.requireNonNull(string, "string");
    }

    @Override
    public CsvStringSet setElements(final Collection<String> strings) {
        final CsvStringSet csvStringSet;

        if (strings instanceof CsvStringSet) {
            csvStringSet = (CsvStringSet) strings;
        } else {
            final SortedSet<String> copy = SortedSets.tree();
            for (final String string : strings) {
                Objects.requireNonNull(string, "includes null string");
                copy.add(string);
            }

            csvStringSet = this.strings.equals(copy) ?
                this :
                copy.isEmpty() ?
                    EMPTY :
                    new CsvStringSet(copy);
        }

        return csvStringSet;
    }

    private final SortedSet<String> strings;

    // HasText..........................................................................................................

    @Override
    public String text() {
        return Csv.toCsv(this.strings);
    }
}
