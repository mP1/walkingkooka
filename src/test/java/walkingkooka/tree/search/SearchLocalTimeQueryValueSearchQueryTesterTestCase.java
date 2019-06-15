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

package walkingkooka.tree.search;

import java.time.LocalTime;

public abstract class SearchLocalTimeQueryValueSearchQueryTesterTestCase<T extends SearchLocalTimeQueryValueSearchQueryTester>
        extends SearchQueryTesterTestCase<T, LocalTime> {

    SearchLocalTimeQueryValueSearchQueryTesterTestCase() {
        super();
    }

    @Override
    final LocalTime value() {
        return LocalTime.of(1, 0, 0);
    }

    @Override
    final LocalTime differentValue() {
        return LocalTime.of(23, 58, 59);
    }
}
