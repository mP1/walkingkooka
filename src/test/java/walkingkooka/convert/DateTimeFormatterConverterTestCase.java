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

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class DateTimeFormatterConverterTestCase<C extends FixedSourceTypeTargetTypeConverter<S, T>, S, T> extends FixedTypeConverterTestCase<C, T> {

    @Test
    public final void testWithNullFormatterFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createConverter(null);
        });
    }

    @Override
    public final C createConverter() {
        return this.createConverter(this.formatter());
    }

    abstract C createConverter(final DateTimeFormatter formatter);

    @Override
    public final ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    abstract DateTimeFormatter formatter();

    @Override
    public final String typeNamePrefix() {
        return DateTimeFormatter.class.getSimpleName() + Converter.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return this.onlySupportedType().getSimpleName();
    }
}
