/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.tree.search;

import java.time.LocalDateTime;

public abstract class SearchLocalDateTimeQueryValueSearchQueryTesterTestCase<T extends SearchLocalDateTimeQueryValueSearchQueryTester>
        extends SearchQueryTesterTestCase<T, LocalDateTime> {

    SearchLocalDateTimeQueryValueSearchQueryTesterTestCase() {
        super();
    }

    @Override
    final LocalDateTime value() {
        return LocalDateTime.of(2000, 1, 31, 1, 0, 0);
    }

    @Override
    final LocalDateTime differentValue() {
        return LocalDateTime.of(1999,12, 31, 23, 58, 59);
    }
}
