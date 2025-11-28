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

import java.util.Optional;

/**
 * An interface that supports reading text or lines of text both with a timeout.
 */
public interface TextReader {

    /**
     * Tests if the {@link TextReader} is open
     */
    boolean isOpen();

    /**
     * Closes this, from further read operations.
     */
    void close();

    /**
     * Tries to read a maximum number of characters within the given timeout.
     */
    String readText(final int max,
                    final long timeout);

    /**
     * Tries to read a line of text within the given timeout.
     */
    Optional<String> readLine(final long timeout);
}
