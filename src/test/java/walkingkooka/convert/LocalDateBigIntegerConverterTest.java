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

import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public final class LocalDateBigIntegerConverterTest extends LocalDateConverterTestCase<LocalDateBigIntegerConverter, BigInteger> {

    @Test
    public void testLocalDate() {
        final int value = 123;
        this.convertAndCheck(LocalDate.ofEpochDay(value), BigInteger.valueOf(value));
    }

    @Test
    public void testToString() {
        assertEquals("LocalDate->BigInteger", this.createConverter().toString());
    }

    @Override
    protected LocalDateBigIntegerConverter createConverter() {
        return LocalDateBigIntegerConverter.INSTANCE;
    }

    @Override
    protected Class<BigInteger> onlySupportedType() {
        return BigInteger.class;
    }

    @Override
    protected Class<LocalDateBigIntegerConverter> type() {
        return LocalDateBigIntegerConverter.class;
    }
}
