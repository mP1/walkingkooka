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

public final class ToStringBuilderAppenderByteScalarTest extends ToStringBuilderAppenderScalarTestCase<ToStringBuilderAppenderByteScalar, Byte> {

    @Test
    public void testBytePrimitiveWithHexEncoding() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.enable(ToStringBuilderOption.HEX_BYTES);
        b.value((byte) 12);
        b.label(LABEL1);
        b.value((byte) 34);

        this.buildAndCheck(b, "0c " + LABEL1 + "=22");
    }

    @Test
    public void testBytePrimitiveWithHexWholeNumbers() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS);
        b.value((byte) 12);
        b.label(LABEL1);
        b.value((byte) 34);

        this.buildAndCheck(b, "0c " + LABEL1 + "=22");
    }

    @Test
    public void testByteWrapperWithHexEncoding() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.enable(ToStringBuilderOption.HEX_BYTES);
        b.value(Byte.valueOf((byte) 12));
        b.label(LABEL1);
        b.value(Byte.valueOf((byte) 34));

        this.buildAndCheck(b, "0c " + LABEL1 + "=22");
    }

    @Override
    void append(final ToStringBuilder builder, final Byte value) {
        builder.append((byte) value);
    }

    @Override
    void value(final ToStringBuilder builder, final Byte value) {
        builder.value((byte) value);
    }

    @Override
    Byte defaultValue() {
        return 0;
    }

    @Override
    Byte value1() {
        return 11;
    }

    @Override
    Byte value2() {
        return 22;
    }

    @Override
    String defaultAppendToString(final Byte value) {
        return this.defaultValueToString(value);
    }

    @Override
    String defaultValueToString(final Byte value) {
        return "0";
    }

    @Override
    String value1ToString() {
        return "11";
    }

    @Override
    String value2ToString() {
        return "22";
    }

    // ClassTesting......................................................................................................

    @Override
    public Class<ToStringBuilderAppenderByteScalar> type() {
        return ToStringBuilderAppenderByteScalar.class;
    }
}
