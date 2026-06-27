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
import walkingkooka.text.CharacterConstant;

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
public final class TsvStringSet extends AbstractSet<String>
    implements DelimiterStringImmutableSet,
    ImmutableSortedSetDefaults<TsvStringSet, String> {

    /**
     * An empty {@link TsvStringSet}
     */
    public final static TsvStringSet EMPTY = new TsvStringSet(SortedSets.empty());

    /**
     * Parses TSV text according to RFC 4180.
     * <br>
     * https://www.ietf.org/rfc/rfc4180.txt
     */
    public static TsvStringSet parse(final String text) {
        final SortedSet<String> strings = SortedSets.tree();

        SEPARATOR.parse(
            text,
            strings::add
        );

        return strings.isEmpty() ?
            EMPTY :
            new TsvStringSet(strings);
    }

    private TsvStringSet(final SortedSet<String> string) {
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
    public TsvStringSet subSet(final String from,
                               final String to) {
        return this.setElements(
            this.strings.subSet(
                from,
                to
            )
        );
    }

    @Override
    public TsvStringSet headSet(final String to) {
        return this.setElements(
            this.strings.headSet(to)
        );
    }

    @Override
    public TsvStringSet tailSet(final String from) {
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
    public TsvStringSet setElements(final Collection<String> strings) {
        final TsvStringSet tsvStringSet;

        if (strings instanceof TsvStringSet) {
            tsvStringSet = (TsvStringSet) strings;
        } else {
            final SortedSet<String> copy = SortedSets.tree();
            for (final String string : strings) {
                Objects.requireNonNull(string, "includes null string");
                copy.add(string);
            }

            tsvStringSet = this.strings.equals(copy) ?
                this :
                copy.isEmpty() ?
                    EMPTY :
                    new TsvStringSet(copy);
        }

        return tsvStringSet;
    }

    private final SortedSet<String> strings;

    // HasTextWithSeparator.............................................................................................

    public final static CharacterConstant SEPARATOR = CharacterConstant.TAB;

    @Override
    public char separator() {
        return SEPARATOR.character();
    }

    /**
     * Note strings with quotes, tabs, CR or NL will be quoted and double quotes escaped.
     */
    @Override
    public String textWithSeparator(final char separator) {
        return CharacterConstant.with(separator)
            .toDelimiteredString(this.strings);
    }
}
