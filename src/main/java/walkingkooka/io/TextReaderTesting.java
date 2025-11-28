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

package walkingkooka.io;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface TextReaderTesting<T extends TextReader> extends ClassTesting<T> {

    // readLine.........................................................................................................

    @Test
    default void testReadLineWithNegativeTimeoutFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createTextReader()
                .readLine(
                    -1
                )
        );
    }

    default void readLineAndCheck(final TextReader textReader,
                                  final long timeout) {
        this.readLineAndCheck(
            textReader,
            timeout,
            Optional.empty()
        );
    }

    default void readLineAndCheck(final TextReader textReader,
                                  final long timeout,
                                  final String expected) {
        this.readLineAndCheck(
            textReader,
            timeout,
            Optional.of(expected)
        );
    }

    default void readLineAndCheck(final TextReader textReader,
                                  final long timeout,
                                  final Optional<String> expected) {
        this.checkEquals(
            expected,
            textReader.readLine(timeout)
        );
    }

    // readText.........................................................................................................

    @Test
    default void testReadTextWithNegativeMaxFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createTextReader()
                .readText(
                    -1,
                    1
                )
        );
    }

    @Test
    default void testReadTextWithNegativeTimeoutFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createTextReader()
                .readText(
                    0,
                    -1
                )
        );
    }

    default void readTextAndCheck(final TextReader textReader,
                                  final int max,
                                  final long timeout,
                                  final String expected) {
        this.checkEquals(
            expected,
            textReader.readText(
                max,
                timeout
            )
        );
    }

    T createTextReader();
}
