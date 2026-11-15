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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.HasMultiLineTextAndCollectionStringTesting;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class CsvTsvStringListTestCase<T extends CsvTsvStringList<T>> implements DelimiterStringImmutableListTesting<T>,
    ParseStringTesting<T>,
    CanRemoveTrailingNullOrEmptyStringsTesting,
    HasCsvStringListTesting,
    HasMultiLineTextAndCollectionStringTesting,
    HasTsvStringListTesting {

    CsvTsvStringListTestCase() {
        super();
    }

    // setElements......................................................................................................

    @Test
    public final void testSetElementsIncludesNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.empty()
                .setElements(
                    Lists.of(null)
                )
        );
    }

    @Test
    public final void testSetElementsWithEmpty() {
        assertSame(
            this.empty(),
            this.empty()
                .setElements(
                    Lists.of(
                        "1A"
                    )
                ).setElements(Lists.empty())
        );
    }

    @Test
    public final void testSetElementsWithCsvStringList() {
        final T csvStringList = this.empty()
            .concat("abc");

        assertSame(
            csvStringList,
            this.empty()
                .setElements(
                    Lists.of(
                        "1A"
                    )
                ).setElements(csvStringList)
        );
    }

    @Override
    public T createList() {
        return this.empty();
    }

    abstract T empty();

    @Override
    public final Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public final RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
    }
}
