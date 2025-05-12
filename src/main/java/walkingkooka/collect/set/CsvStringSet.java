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
        final SortedSet<String> elements = SortedSets.tree();
        Csv.parse(
            text,
            elements::add
        );
        return elements.isEmpty() ?
            EMPTY :
            new CsvStringSet(elements);
    }

    private CsvStringSet(final SortedSet<String> set) {
        super();
        this.set = set;
    }

    @Override
    public Iterator<String> iterator() {
        return Iterators.readOnly(this.set.iterator());
    }

    @Override
    public int size() {
        return this.set.size();
    }

    @Override
    public SortedSet<String> toSet() {
        return new TreeSet<>(this.set);
    }

    @Override
    public Comparator<String> comparator() {
        return Cast.to(
            this.set.comparator()
        );
    }

    @Override
    public SortedSet<String> subSet(final String from,
                                    final String to) {
        return this.setElements(
            this.set.subSet(
                from,
                to
            )
        );
    }

    @Override
    public SortedSet<String> headSet(final String to) {
        return this.setElements(
            this.set.headSet(to)
        );
    }

    @Override
    public SortedSet<String> tailSet(final String from) {
        return this.setElements(
            this.set.tailSet(from)
        );
    }

    @Override
    public String first() {
        return this.set.first();
    }

    @Override
    public String last() {
        return this.set.last();
    }

    @Override
    public void elementCheck(final String element) {
        Objects.requireNonNull(element, "element");
    }

    @Override
    public ImmutableSortedSet<String> setElements(final SortedSet<String> elements) {
        final CsvStringSet csvStringSet;

        if (elements instanceof CsvStringSet) {
            csvStringSet = (CsvStringSet) elements;
        } else {
            final SortedSet<String> copy = SortedSets.tree();
            for (final String string : elements) {
                Objects.requireNonNull(string, "includes null");
                copy.add(string);
            }

            csvStringSet = this.set.equals(copy) ?
                this :
                copy.isEmpty() ?
                    EMPTY :
                    new CsvStringSet(copy);
        }

        return csvStringSet;
    }

    private final SortedSet<String> set;

    // HasText..........................................................................................................

    @Override
    public String text() {
        return Csv.toCsv(this.set);
    }
}
