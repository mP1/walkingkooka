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

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * An immutable list of {@link String} that allows null elements.
 */
public final class StringList extends AbstractList<String>
    implements ImmutableListDefaults<StringList, String> {

    /**
     * An empty {@link StringList}.
     */
    public final static StringList EMPTY = new StringList(
        Lists.empty()
    );

    /**
     * Factory that creates a {@link StringList} from the list of {@link String strings}.
     */
    public static StringList with(final Collection<String> strings) {
        Objects.requireNonNull(strings, "strings");

        StringList DateList;

        if (strings instanceof StringList) {
            DateList = (StringList) strings;
        } else {
            final List<String> copy = Lists.array();
            for (final String string : strings) {
                copy.add(string);
            }

            switch (strings.size()) {
                case 0:
                    DateList = EMPTY;
                    break;
                default:
                    DateList = new StringList(copy);
                    break;
            }
        }

        return DateList;
    }

    private StringList(final List<String> strings) {
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
        final StringList copy = with(strings);
        return this.equals(copy) ?
            this :
            copy;
    }
}
