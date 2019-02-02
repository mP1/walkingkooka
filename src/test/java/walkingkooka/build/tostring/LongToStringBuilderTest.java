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
 */

package walkingkooka.build.tostring;

import org.junit.jupiter.api.Test;

public final class LongToStringBuilderTest extends ScalarToStringBuilderTestCase<Long> {

    @Test
    public void testValueHexWholeNumber() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS);

        b.value(Long.valueOf(1L));

        this.buildAndCheck(b, "0000000000000001");
    }

    @Override
    void append(final ToStringBuilder builder, final Long value) {
        builder.append((long) value);
    }

    @Override
    void value(final ToStringBuilder builder, final Long value) {
        builder.value((long)value);
    }

    @Override
    Long defaultValue() {
        return 0L;
    }

    @Override
    Long value1() {
        return 123L;
    }

    @Override
    Long value2() {
        return 456L;
    }

    @Override
    String defaultValueToString() {
        return "0";
    }

    @Override
    String value1ToString() {
        return "123";
    }

    @Override
    String value2ToString() {
        return "456";
    }
}
