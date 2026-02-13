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

package walkingkooka.util;

import walkingkooka.test.Testing;

import java.util.Currency;
import java.util.Optional;

public interface HasOptionalCurrencyTesting extends Testing {

    default void currencyAndCheck(final HasOptionalCurrency has) {
        this.currencyAndCheck(
            has,
            Optional.empty()
        );
    }

    default void currencyAndCheck(final HasOptionalCurrency has,
                                  final Currency expected) {
        this.currencyAndCheck(
            has,
            Optional.of(expected)
        );
    }

    default void currencyAndCheck(final HasOptionalCurrency has,
                                  final Optional<Currency> expected) {
        this.checkEquals(
            expected,
            has.currency(),
            () -> has + " currency()"
        );
    }
}
