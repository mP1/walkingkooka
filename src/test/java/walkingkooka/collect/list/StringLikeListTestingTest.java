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
import walkingkooka.reflect.ThrowableTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class StringLikeListTestingTest implements StringLikeListTesting,
    ThrowableTesting {

    @Test
    public void testGetCharacterAndCheckWhenNullString() {
        this.getCharacterAndCheck(
            StringList.EMPTY.concat(null),
            0,
            null
        );
    }

    @Test
    public void testGetCharacterAndCheckWhenOneCharacterString() {
        this.getCharacterAndCheck(
            StringList.EMPTY.concat("A"),
            0,
            'A'
        );
    }

    @Test
    public void testGetCharacterWhenEmptyStringFails() {
        this.getMessageAndCheck(
            assertThrows(
                IllegalArgumentException.class,
                () -> StringList.EMPTY.concat("")
                    .getCharacter(0)
            ),
            "Empty \"0\""
        );
    }

    @Test
    public void testGetCharacterWhenTwoCharacterStringFails() {
        this.getMessageAndCheck(
            assertThrows(
                IllegalArgumentException.class,
                () -> StringList.EMPTY.concat("AB")
                    .getCharacter(0)
            ),
            "Invalid character expected 1 character but got 2"
        );
    }
}
