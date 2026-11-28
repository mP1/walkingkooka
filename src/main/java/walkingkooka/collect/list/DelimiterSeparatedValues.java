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

import java.util.List;
import java.util.function.Function;

/**
 * An enumeration with type safe methods to parse, fetch an empty instance and get the actual separator character.
 * Possible new entries include SEMI-COLON and PIPE.
 */
public final class DelimiterSeparatedValues<T extends CsvTsvStringList<T>> {

    public final static DelimiterSeparatedValues<CsvStringList> COMMA = new DelimiterSeparatedValues<CsvStringList>(
        ',',
        CsvStringList.EMPTY::setElements
    );

    public final static DelimiterSeparatedValues<TsvStringList> TAB = new DelimiterSeparatedValues<TsvStringList>(
        '\t',
        TsvStringList.EMPTY::setElements
    );

    private DelimiterSeparatedValues(final char character,
                                     final Function<List<String>, T> setElements) {
        super();
        this.character = character;
        this.setElements = setElements;
    }

    public char character() {
        return this.character;
    }

    private final char character;

    /**
     * SetElements that returns an instance with the given {@link String} values.
     */
    public T setElements(final List<String> strings) {
        return this.setElements.apply(strings);
    }

    private final Function<List<String>, T> setElements;
}
