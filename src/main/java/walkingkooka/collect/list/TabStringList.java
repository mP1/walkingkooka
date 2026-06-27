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

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * An immutable list of String elements. Note null elements are not allowed.
 */
public final class TabStringList extends AbstractList<String> implements DelimiterStringImmutableList,
    ImmutableListDefaults<TabStringList, String> {

    /**
     * An empty {@link TabStringList}
     */
    public final static TabStringList EMPTY = new TabStringList(Lists.empty());

    /**
     * Parses TSV text according to RFC 4180, replaces commas with tabs
     * <br>
     * https://www.ietf.org/rfc/rfc4180.txt
     */
    public static TabStringList parse(final String text) {
        final List<String> strings = Lists.array();

        SEPARATOR.parse(
            text,
            strings::add
        );

        return strings.isEmpty() ?
            EMPTY :
            new TabStringList(strings);
    }

    /**
     * Private ctor use #parse or any would be mutator methods.
     */
    private TabStringList(final List<String> strings) {
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
    public TabStringList setElements(final Collection<String> strings) {
        TabStringList tsvStringList;

        if (strings instanceof TabStringList) {
            tsvStringList = (TabStringList) strings;
        } else {
            final List<String> copy = Lists.array();
            for(final String string : strings) {
                Objects.requireNonNull(strings, "includes null string");
                copy.add(string);
            }
            tsvStringList = this.strings.equals(copy) ?
                this :
                copy.isEmpty() ?
                    EMPTY :
                    new TabStringList(copy);
        }

        return tsvStringList;
    }

    private final List<String> strings;

    // HasTextWithSeparator.............................................................................................

    public final static CharacterConstant SEPARATOR = CharacterConstant.TAB;

    @Override
    public char separator() {
        return SEPARATOR.character();
    }

    /**
     * Note strings with quotes, tab, CR or NL will be quoted and double quotes escaped.
     */
    @Override
    public String textWithSeparator(final char separator) {
        return CharacterConstant.with(separator)
            .toDelimiteredString(this.strings);
    }
}
