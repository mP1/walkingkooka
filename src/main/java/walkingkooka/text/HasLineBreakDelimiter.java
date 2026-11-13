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

/**
 * Supports a collection of values to also support {@link LineEnding} as the separator rather than the normal separator.
 */
public interface HasLineBreakDelimiter {

    /**
     * Returns the current number of values.
     */
    int size();

    /**
     * Returns a boolean if the given position will use a {@link LineEnding} rather than the separator.
     */
    boolean lineBreak(final int index);
}
