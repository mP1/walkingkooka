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

import walkingkooka.text.CharSequences;

public interface CanRemoveTrailingNullOrEmptyStrings extends ImmutableList<String> {

    /**
     * Returns an instance with any trailing null or empty {@link String} removed.
     */
    default ImmutableList<String> removeTrailingNullOrEmptyStrings() {
        int newSize = this.size();

        while (newSize > 0) {
            if (false == CharSequences.isNullOrEmpty(this.get(newSize - 1))) {
                break;
            }
            newSize--;
        }

        return this.setElements(
            this.subList(
                0,
                newSize
            )
        );
    }
}
