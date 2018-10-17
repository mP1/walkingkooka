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

package walkingkooka.convert;

import org.junit.Test;

import java.time.format.DateTimeFormatter;

public abstract class DateTimeFormatterConverterTestCase<C extends FixedSourceTypeTargetTypeConverter<S, T>, S, T> extends FixedTypeConverterTestCase<C, T> {

    @Test(expected = NullPointerException.class)
    public final void testWithNullFormatterFails() {
        this.createConverter(null);
    }

    @Override
    protected C createConverter() {
        return this.createConverter(this.formatter());
    }

    abstract C createConverter(final DateTimeFormatter formatter);

    @Override
    protected final ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    abstract DateTimeFormatter formatter();
}
