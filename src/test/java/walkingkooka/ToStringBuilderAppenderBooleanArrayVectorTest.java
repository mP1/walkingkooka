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

public class ToStringBuilderAppenderBooleanArrayVectorTest extends ToStringBuilderAppenderVectorTestCase<ToStringBuilderAppenderBooleanArrayVector, boolean[]> {

    @Test
    public void testOneZero() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.ONE_AND_ZERO_BOOLEAN_ARRAYS);

        b.label(LABEL1);
        b.value(new boolean[]{true, true, false, false, true});

        this.buildAndCheck(b, LABEL1 + LABEL_SEPARATOR + "11001");
    }

    @Test
    public void testBooleanPrimitiveWithOneAndZero() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.ONE_AND_ZERO_BOOLEAN_ARRAYS);

        b.value(new boolean[]{true, true, false, false, true});

        this.buildAndCheck(b, "11001");
    }

    @Test
    public void testBooleanPrimitiveWithOneAndZero2() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.ONE_AND_ZERO_BOOLEAN_ARRAYS);

        b.value(new boolean[]{true, true, false, false, true});
        b.label(LABEL1);
        b.value(new boolean[]{false, true, true, false, false});

        this.buildAndCheck(b, "11001" + SEPARATOR + LABEL1 + LABEL_SEPARATOR + "01100");
    }

    @Test
    public void testBooleanWrapperArrayWithOneAndZeroIgnored() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.ONE_AND_ZERO_BOOLEAN_ARRAYS);

        b.value(new Boolean[]{true, true});

        this.buildAndCheck(b, true + VALUE_SEPARATOR + true);
    }

    @Override
    boolean[] defaultValue() {
        return new boolean[0];
    }

    @Override final String defaultValueToString(final boolean[] value) {
        return "";
    }

    @Override
    boolean[] value1() {
        return new boolean[]{true};
    }

    @Override
    boolean[] value2() {
        return new boolean[]{true, false, false, true};
    }

    @Override
    void append(final ToStringBuilder builder, final boolean[] value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder, final boolean[] value) {
        builder.value(value);
    }

    @Override
    String value1ToString() {
        return String.valueOf(true);
    }

    @Override
    String value2ToString(final String separator) {
        return true + separator + false + separator + false + separator + true;
    }

    // ClassTesting......................................................................................................

    @Override
    public Class<ToStringBuilderAppenderBooleanArrayVector> type() {
        return ToStringBuilderAppenderBooleanArrayVector.class;
    }
}
