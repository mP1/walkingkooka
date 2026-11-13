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

import walkingkooka.test.Testing;

import java.util.Optional;

public interface TextReaderTesting extends Testing {

    // readLine.........................................................................................................

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
}
