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

import java.math.BigInteger;

public final class SearchBigIntegerQueryValueEqualityTest extends SearchQueryValueEqualityTestCase<SearchBigIntegerQueryValue, BigInteger> {
    @Override
    SearchBigIntegerQueryValue createSearchQueryValue(final BigInteger value) {
        return SearchBigIntegerQueryValue.with(value);
    }

    @Override
    BigInteger value() {
        return BigInteger.valueOf(123);
    }

    @Override
    BigInteger differentValue() {
        return BigInteger.valueOf(999);
    }
}
