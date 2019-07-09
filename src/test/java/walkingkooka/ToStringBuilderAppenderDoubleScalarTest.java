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

public final class ToStringBuilderAppenderDoubleScalarTest extends ToStringBuilderAppenderScalarTestCase<ToStringBuilderAppenderDoubleScalar, Double> {

    @Test
    public void testValueHexWholeNumber() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS);

        b.value(123.5);

        this.buildAndCheck(b, "123.5");
    }

    @Test
    public void testAppendFloat() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS);
        b.append(123.5f);

        this.buildAndCheck(b, "123.5");
    }

    @Test
    public void testAppendFloatDefault() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS);
        b.append(0f);
        b.append(1);

        this.buildAndCheck(b, "" + 0f + 1);
    }

    @Test
    public void testValueFloat() {
        final ToStringBuilder b = this.builder();
        b.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);

        b.value(123.5f);

        this.buildAndCheck(b, "123.5");
    }

    @Test
    public void testValueFloatHexWholeNumbers() {
        final ToStringBuilder b = this.builder();
        b.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);
        b.enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS);

        b.value(123.5f);

        this.buildAndCheck(b, "123.5");
    }

    @Test
    public void testValueFloatDefault() {
        final ToStringBuilder b = this.builder();
        b.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);
        b.enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS);

        b.value(0f);
        b.value(1);

        this.buildAndCheck(b, 0f + SEPARATOR + "00000001");
    }

    @Override
    void append(final ToStringBuilder builder, final Double value) {
        builder.append((double) value);
    }

    @Override
    void value(final ToStringBuilder builder, final Double value) {
        builder.value((double) value);
    }

    @Override
    Double defaultValue() {
        return 0.0;
    }

    @Override
    Double value1() {
        return 123.0;
    }

    @Override
    Double value2() {
        return 456.0;
    }

    @Override
    String defaultAppendToString(final Double value) {
        return this.defaultValueToString(value);
    }

    @Override
    String defaultValueToString(final Double value) {
        return "0.0";
    }

    @Override
    String value1ToString() {
        return "123.0";
    }

    @Override
    String value2ToString() {
        return "456.0";
    }

    // ClassTesting......................................................................................................

    @Override
    public Class<ToStringBuilderAppenderDoubleScalar> type() {
        return ToStringBuilderAppenderDoubleScalar.class;
    }
}
