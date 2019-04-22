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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;

import java.time.LocalTime;

public final class LocalTimeLongConverterTest extends LocalTimeConverterTestCase<LocalTimeLongConverter, Long> {

    private final static int VALUE = 123;

    @Test
    public void testLocalTime() {
        this.convertAndCheck(LocalTime.ofSecondOfDay(VALUE), Long.valueOf(VALUE));
    }

    @Test
    public void testConverterRoundTripWithNanos() {
        final LocalTime localTime = LocalTime.ofSecondOfDay(VALUE);
        final Long longValue = Cast.to(this.convertAndCheck(localTime, Long.valueOf(VALUE)));
        this.convertAndCheck(Converters.numberLocalTime(), longValue, LocalTime.class, localTime);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), "LocalTime->Long");
    }

    @Override public LocalTimeLongConverter createConverter() {
        return LocalTimeLongConverter.INSTANCE;
    }

    @Override
    protected Class<Long> onlySupportedType() {
        return Long.class;
    }

    @Override
    public Class<LocalTimeLongConverter> type() {
        return LocalTimeLongConverter.class;
    }
}
