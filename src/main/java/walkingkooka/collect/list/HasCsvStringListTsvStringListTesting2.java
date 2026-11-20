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

import org.junit.jupiter.api.Test;

public interface HasCsvStringListTsvStringListTesting2<T extends HasCsvStringListTsvStringList> extends HasCsvStringListTsvStringListTesting {

    @Test
    default void testCsvStringListAndTsvStringListRoundtrip() {
        final T has = this.createHasCsvStringListTsvStringList();

        this.csvStringListAndCheck(
            has,
            has.tsvStringList()
                .csvStringList()
        );
    }

    @Test
    default void testTsvStringListAndCsvStringListRoundtrip() {
        final T has = this.createHasCsvStringListTsvStringList();

        this.tsvStringListAndCheck(
            has,
            has.csvStringList()
                .tsvStringList()
        );
    }

    T createHasCsvStringListTsvStringList();
}
