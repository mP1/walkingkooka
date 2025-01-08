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

public final class ToStringBuilderAppenderDefaultScalarTest extends ToStringBuilderAppenderScalarTestCase<ToStringBuilderAppenderDefaultScalar, Object> {

    @Test
    public void testLabelValueNullValue() {
        final ToStringBuilder builder = builder();

        builder.label(LABEL1)
            .value((Object) null)
            .value(1);

        this.buildAndCheck(builder, "1");
    }

    @Override
    void append(final ToStringBuilder builder, final Object value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder, final Object value) {
        builder.value(value);
    }

    @Override
    Object defaultValue() {
        return null;
    }

    @Override
    Object value1() {
        return this;
    }

    @Override
    Object value2() {
        return VALUE2;
    }

    @Override
    String defaultAppendToString(final Object value) {
        return this.defaultValueToString(value);
    }

    @Override
    String defaultValueToString(final Object value) {
        return "null";
    }

    @Override
    String value1ToString() {
        return this.toString();
    }

    @Override
    String value2ToString() {
        return VALUE2.toString();
    }

    static class TestDifferent {

    }

    private final static TestDifferent VALUE2 = new TestDifferent();

    // ClassTesting......................................................................................................

    @Override
    public Class<ToStringBuilderAppenderDefaultScalar> type() {
        return ToStringBuilderAppenderDefaultScalar.class;
    }
}
