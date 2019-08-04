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

package walkingkooka.datetime;

import java.util.List;

final class DateTimeContextGetter {

    static String get(final int index,
                      final List<String> texts,
                      final String label) {
        final int max = texts.size();
        if (index < 0 || index >= max) {
            throw new IllegalArgumentException("Invalid " + label + "=" + index + " not between 0 and " + max);
        }

        return texts.get(index);
    }

    /**
     * Stop creation
     */
    private DateTimeContextGetter() {
        throw new UnsupportedOperationException();
    }
}
