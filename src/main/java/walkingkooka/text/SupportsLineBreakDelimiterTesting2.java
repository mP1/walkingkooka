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

package walkingkooka.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface SupportsLineBreakDelimiterTesting2<T extends SupportsLineBreakDelimiter<T>> extends SupportsLineBreakDelimiterTesting {

    // lastLineBreaks...................................................................................................

    @Test
    default void testLastLineBreakWhenEmptyFails() {
        final T supportsLineBreakDelimiter = this.createSupportsLineBreakDelimiter();

        this.checkEquals(
            0,
            supportsLineBreakDelimiter.size()
        );

        assertThrows(
            IllegalStateException.class,
            () -> supportsLineBreakDelimiter.lastLineBreak()
        );
    }

    // lineBreaks.......................................................................................................

    @Test
    default void testLineBreakWithNegativeIndexFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createSupportsLineBreakDelimiter()
                .lineBreak(
                    -1
                )
        );
    }

    @Test
    default void testLineBreakWithInvalidIndexFails() {
        final T supportsLineBreakDelimiter = this.createSupportsLineBreakDelimiter();

        assertThrows(
            IllegalArgumentException.class,
            () -> supportsLineBreakDelimiter.lineBreak(
                supportsLineBreakDelimiter.size()
            )
        );
    }

    // setLineBreaks....................................................................................................

    @Test
    default void testSetLineBreakWithNegativeIndexFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createSupportsLineBreakDelimiter()
                .setLineBreak(
                    -1,
                    false
                )
        );
    }

    @Test
    default void testSetLineBreakWithInvalidIndexFails() {
        final T supportsLineBreakDelimiter = this.createSupportsLineBreakDelimiter();

        assertThrows(
            IllegalArgumentException.class,
            () -> supportsLineBreakDelimiter.setLineBreak(
                supportsLineBreakDelimiter.size(),
                false
            )
        );
    }

    // removeLineBreaks.................................................................................................

    @Test
    default void testRemoveLineBreakWithNegativeIndexFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createSupportsLineBreakDelimiter()
                .removeLineBreak(
                    -1
                )
        );
    }

    @Test
    default void testRemoveLineBreakWithInvalidIndexFails() {
        final T supportsLineBreakDelimiter = this.createSupportsLineBreakDelimiter();

        assertThrows(
            IllegalArgumentException.class,
            () -> supportsLineBreakDelimiter.removeLineBreak(
                supportsLineBreakDelimiter.size()
            )
        );
    }

    T createSupportsLineBreakDelimiter();
}
