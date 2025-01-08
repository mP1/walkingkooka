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

package walkingkooka;

import org.junit.jupiter.api.Test;

public final class ToStringBuilderAppenderLongArrayVectorTest extends ToStringBuilderAppenderVectorTestCase<ToStringBuilderAppenderLongArrayVector, long[]> {

    @Test
    public void testValueIncludesDefault() {
        final ToStringBuilder b = this.builder();
        b.label(LABEL1);
        b.value(new long[]{0, 1, 2});

        this.buildAndCheck(b, LABEL1 + LABEL_SEPARATOR + 0 + VALUE_SEPARATOR + 1 + VALUE_SEPARATOR + 2);
    }

    @Test
    public void testValueHexWholeNumber() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS);

        b.value(new long[]{0, 1, 2});

        this.buildAndCheck(b,
            "0000000000000000" + VALUE_SEPARATOR + "0000000000000001" + VALUE_SEPARATOR + "0000000000000002");
    }

    @Override
    long[] defaultValue() {
        return new long[0];
    }

    @Override final String defaultValueToString(final long[] value) {
        return "";
    }

    @Override
    long[] value1() {
        return new long[]{123};
    }

    @Override
    long[] value2() {
        return new long[]{1, 2, 33};
    }

    @Override
    void append(final ToStringBuilder builder, final long[] value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder, final long[] value) {
        builder.value(value);
    }

    @Override
    String value1ToString() {
        return "123";
    }

    @Override
    String value2ToString(final String separator) {
        return "1" + separator + "2" + separator + "33";
    }

    @Override
    public Class<ToStringBuilderAppenderLongArrayVector> type() {
        return ToStringBuilderAppenderLongArrayVector.class;
    }
}
