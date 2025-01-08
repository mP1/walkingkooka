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

public final class ToStringBuilderAppenderDoubleArrayVectorTest extends ToStringBuilderAppenderVectorTestCase<ToStringBuilderAppenderDoubleArrayVector, double[]> {

    @Test
    public void testValueIncludesDefault() {
        final ToStringBuilder b = this.builder();
        b.label(LABEL1);
        b.value(new double[]{0.0, 1.0, 2.0});

        this.buildAndCheck(b,
            LABEL1 + LABEL_SEPARATOR + 0.0 + VALUE_SEPARATOR + 1.0 + VALUE_SEPARATOR + 2.0);
    }

    @Override
    double[] defaultValue() {
        return new double[0];
    }

    @Override final String defaultValueToString(final double[] value) {
        return "";
    }

    @Override
    double[] value1() {
        return new double[]{123.5};
    }

    @Override
    double[] value2() {
        return new double[]{1.0, 2.0, 33.5};
    }

    @Override
    void append(final ToStringBuilder builder, final double[] value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder, final double[] value) {
        builder.value(value);
    }

    @Override
    String value1ToString() {
        return "" + 123.5;
    }

    @Override
    String value2ToString(final String separator) {
        return 1.0 + separator + 2.0 + separator + 33.5;
    }

    // ClassTesting......................................................................................................

    @Override
    public Class<ToStringBuilderAppenderDoubleArrayVector> type() {
        return ToStringBuilderAppenderDoubleArrayVector.class;
    }
}
