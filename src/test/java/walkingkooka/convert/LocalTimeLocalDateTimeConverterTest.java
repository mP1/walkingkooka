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

import java.time.LocalDateTime;
import java.time.LocalTime;

public final class LocalTimeLocalDateTimeConverterTest extends LocalDateTimeConverterTestCase<LocalTimeLocalDateTimeConverter, LocalTime> {

    @Test
    public void testLocalTime() {
        this.convertAndCheck(LocalTime.of(12, 58, 59, 789), LocalDateTime.of(1970, 1, 1, 12, 58, 59, 789));
    }

    @Override
    public LocalTimeLocalDateTimeConverter createConverter() {
        return LocalTimeLocalDateTimeConverter.INSTANCE;
    }

    @Override
    public Class<LocalTimeLocalDateTimeConverter> type() {
        return LocalTimeLocalDateTimeConverter.class;
    }
}
