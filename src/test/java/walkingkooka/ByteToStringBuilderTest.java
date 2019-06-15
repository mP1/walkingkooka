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

public final class ByteToStringBuilderTest extends ScalarToStringBuilderTestCase<Byte> {

    @Test
    public void testValueWithHexEncoding() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.enable(ToStringBuilderOption.HEX_BYTES);
        b.value(12);
        b.label(LABEL);
        b.value(34);

        this.buildAndCheck(b, "12 " + LABEL + "=34");
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
    String defaultValueToString() {
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
}
