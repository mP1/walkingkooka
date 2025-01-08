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

public final class ToStringBuilderAppenderFloatArrayVectorTest extends ToStringBuilderAppenderVectorTestCase<ToStringBuilderAppenderFloatArrayVector, float[]> {

    @Test
    public void testValueIncludesDefault() {
        final ToStringBuilder b = this.builder();
        b.label(LABEL1);
        b.value(new float[]{0, 1, 2});

        this.buildAndCheck(b,
            LABEL1 + LABEL_SEPARATOR + 0.0f + VALUE_SEPARATOR + 1.0f + VALUE_SEPARATOR + 2.0f);
    }

    @Override
    float[] defaultValue() {
        return new float[0];
    }

    @Override final String defaultValueToString(final float[] value) {
        return "";
    }

    @Override
    float[] value1() {
        return new float[]{123.5f};
    }

    @Override
    float[] value2() {
        return new float[]{1, 2, 33.5f};
    }

    @Override
    void append(final ToStringBuilder builder, final float[] value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder, final float[] value) {
        builder.value(value);
    }

    @Override
    String value1ToString() {
        return "" + 123.5f;
    }

    @Override
    String value2ToString(final String separator) {
        return 1f + separator + 2f + separator + 33.5f;
    }

    // ClassTesting......................................................................................................

    @Override
    public Class<ToStringBuilderAppenderFloatArrayVector> type() {
        return ToStringBuilderAppenderFloatArrayVector.class;
    }
}
