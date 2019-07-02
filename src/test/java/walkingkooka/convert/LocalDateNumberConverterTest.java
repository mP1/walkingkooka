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

package walkingkooka.convert;

public final class LocalDateNumberConverterTest extends LocalDateConverterTestCase<LocalDateNumberConverter, Number> {

    @Override
    final LocalDateNumberConverter createConverter(final long offset) {
        return LocalDateNumberConverter.with(offset);
    }

    @Override
    protected Class<Number> onlySupportedType() {
        return Number.class;
    }

    @Override final Long value(final long value) {
        return Long.valueOf(value);
    }

    @Override
    public Class<LocalDateNumberConverter> type() {
        return LocalDateNumberConverter.class;
    }
}
