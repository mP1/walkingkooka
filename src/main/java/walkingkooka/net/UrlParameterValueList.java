/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.net;

import walkingkooka.collect.list.Lists;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

/**
 * A custom immutable {@link List} that appears read only outside this package, but is mutable during the parsing process.
 */
final class UrlParameterValueList extends AbstractList<String> {

    static UrlParameterValueList empty() {
        return new UrlParameterValueList();
    }

    private UrlParameterValueList() {
    }

    void addParameterValue(final String value) {
        this.values.add(value);
    }

    void removeParameterValues(final String value) {
        // List.remove only removes the FIRST and not all values...
        for (Iterator<String> i = this.values.iterator(); i.hasNext(); ) {
            if (i.next().equals(value)) {
                i.remove();
            }
        }
    }

    @Override
    public String get(final int index) {
        return this.values.get(index);
    }

    @Override
    public int size() {
        return this.values.size();
    }

    private final List<String> values = Lists.array();

    @Override
    public String toString() {
        return this.values.toString();
    }
}
