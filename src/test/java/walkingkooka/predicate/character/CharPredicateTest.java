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

package walkingkooka.predicate.character;

import org.junit.jupiter.api.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CharPredicateTest implements ClassTesting<CharPredicate> {

    // failIfNullOrFalse ...............................................................................................

    private final CharPredicate PREDICATE = CharPredicates.letter();

    @Test
    public void testFailIfNullOrFalseWithNullLabelFails() {
        assertThrows(
                NullPointerException.class,
                () -> PREDICATE.failIfNullOrFalse(
                        null,
                        "ABC"
                )
        );
    }

    @Test
    public void testFailIfNullOrFalseWithEmptyLabelFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> PREDICATE.failIfNullOrFalse(
                        "",
                        "ABC"
                )
        );
    }

    @Test
    public void testFailIfNullOrFalseWithNullCharSequenceFails() {
        assertThrows(
                NullPointerException.class,
                () -> PREDICATE.failIfNullOrFalse(
                        "TEXT",
                        null
                )
        );
    }

    @Test
    public void testFailIfNullOrFalseWithEmptyChars() {
        PREDICATE.failIfNullOrFalse(
                "Label123",
                ""
        );
    }

    @Test
    public void testFailIfNullOrFalseTestFails() {
        assertThrows(
                InvalidCharacterException.class,
                () -> this.failIfNullOrFalse("98")
        );
    }

    @Test
    public void testFailIfNullOrFalseTestFails2() {
        assertThrows(
                InvalidCharacterException.class,
                () -> this.failIfNullOrFalse("A1")
        );
    }

    @Test
    public void testFailIfNullOrFalseEmptyFail() {
        this.failIfNullOrFalse("");
    }

    @Test
    public void testFailIfNullOrFalsePass() {
        this.failIfNullOrFalse("A");
    }

    @Test
    public void testFailIfNullOrFalsePass2() {
        this.failIfNullOrFalse("AB");
    }

    @Test
    public void testFailIfNullOrFalsePass3() {
        this.failIfNullOrFalse("ABCDEFGH");
    }

    private void failIfNullOrFalse(final String text) {
        PREDICATE.failIfNullOrFalse(
                "TEXT",
                text
        );
    }

    // class............................................................................................................

    @Override
    public Class<CharPredicate> type() {
        return CharPredicate.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
