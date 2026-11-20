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

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface StringLikeListTesting2<T extends StringLikeList> extends StringLikeListTesting,
    ListTesting2<T, String> {

    @Test
    default void testGetCharacterWithNegativeIndexFails() {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> this.createList()
                .getCharacter(-1)
        );
    }

    // getCharacterOrNull...............................................................................................

    @Test
    default void testGetCharacterOrNullWithNegativeIndexFails() {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> this.createList()
                .getCharacterOrNull(-1)
        );
    }

    @Test
    default void testGetCharacterOrNullWithSizePlus1() {
        final T list = this.createList();

        this.getCharacterOrNullAndCheck(
            list,
            list.size() + 1,
            null
        );
    }

    // getStringOrNull..................................................................................................

    @Test
    default void testGetStringOrNullWithNegativeIndexFails() {
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> this.createList()
                .getStringOrNull(-1)
        );
    }

    @Test
    default void testGetStringOrNullWithSizePlus1() {
        final T list = this.createList();

        this.getStringOrNullAndCheck(
            list,
            list.size() + 1,
            null
        );
    }
}
