
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

import walkingkooka.test.Testing;

public interface HasTsvStringListTesting extends Testing {

    default void tsvStringListAndCheck(final HasTsvStringList has,
                                       final String expected) {
        this.tsvStringListAndCheck(
            has,
            TsvStringList.parse(expected)
        );
    }

    default void tsvStringListAndCheck(final HasTsvStringList has,
                                       final TsvStringList expected) {
        this.checkEquals(
            expected,
            has.tsvStringList(),
            has::toString
        );
    }
}
