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
 * Used by a value to support returning different {@link CharSequence} separators.
 * An example might be a CSV list that supports both COMMAs and EOLs as the separator between
 * {@link String values} on a value by value basis.
 */
public interface HasTextSeparator {

    /**
     * Returns the separator for the given value identified by index.
     * All index values should be positive numbers.
     */
    CharSequence textSeparator(final int index);
}
