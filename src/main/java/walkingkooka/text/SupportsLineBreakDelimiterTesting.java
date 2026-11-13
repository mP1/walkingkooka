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

import walkingkooka.Cast;
import walkingkooka.test.Testing;

import static org.junit.jupiter.api.Assertions.assertSame;

public interface SupportsLineBreakDelimiterTesting extends Testing {

    default void lastLineBreakAndCheck(final SupportsLineBreakDelimiter<?> collection) {
        this.setLineBreakAndCheck(
            Cast.to(collection),
            collection.size(),
            true,
            Cast.to(
                collection.setLineBreak(
                    collection.size(),
                    true
                )
            )
        );
    }

    default void lineBreakAndCheck(final SupportsLineBreakDelimiter<?> collection,
                                   final int index,
                                   final boolean expected) {
        this.checkEquals(
            expected,
            collection.lineBreak(index),
            () -> "index " + index
        );
    }

    default <T extends SupportsLineBreakDelimiter<T>> void setLineBreakAndCheck(final T collection,
                                                                                final int index,
                                                                                final boolean lineBreak,
                                                                                final T expected) {
        final T after = collection.setLineBreak(
            index,
            lineBreak
        );

        if (collection.equals(expected)) {
            assertSame(
                expected,
                after,
                () -> "setLineBreak " + index + " " + lineBreak
            );
        } else {
            this.checkEquals(
                expected,
                after,
                () -> "setLineBreak " + index + " " + lineBreak
            );
        }

        this.lineBreakAndCheck(
            after,
            index,
            lineBreak
        );
    }

    default <T extends SupportsLineBreakDelimiter<T>> void removeLineBreakAndCheck(final T collection,
                                                                                   final int index,
                                                                                   final T expected) {
        final T after = collection.removeLineBreak(
            index
        );

        if (collection.equals(expected)) {
            assertSame(
                expected,
                after,
                () -> "removeLineBreak " + index
            );
        } else {
            this.checkEquals(
                expected,
                after,
                () -> "removeLineBreak " + index
            );
        }

        this.lineBreakAndCheck(
            after,
            index,
            false
        );
    }
}
