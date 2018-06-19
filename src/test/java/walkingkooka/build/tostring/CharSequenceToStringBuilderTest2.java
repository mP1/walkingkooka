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

import org.junit.Test;

public final class CharSequenceToStringBuilderTest2 extends ScalarToStringBuilderTestCase<StringBuilder> {

    @Test
    public void testValueDefaultEmpty() {
        final ToStringBuilder b = ToStringBuilder.create();
        b.value(new StringBuilder(""));

        this.buildAndCheck(b, "");
    }

    @Test
    public void testLabelValueDefaultEmpty() {
        final ToStringBuilder b = ToStringBuilder.create();
        b.label(LABEL);
        b.value("");

        this.buildAndCheck(b, "");
    }

    @Override
    void append(final ToStringBuilder builder, final StringBuilder value) {
        builder.append((CharSequence) value);
    }

    @Override
    void value(final ToStringBuilder builder, final StringBuilder value) {
        builder.value((CharSequence)value);
    }

    @Override
    StringBuilder defaultValue() {
        return null;
    }

    @Override
    StringBuilder value1() {
        return new StringBuilder("ABC");
    }

    @Override
    StringBuilder value2() {
        return new StringBuilder("XYZ");
    }

    @Override
    String defaultValueToString() {
        return "null";
    }

    @Override
    String value1ToString() {
        return "ABC";
    }

    @Override
    String value2ToString() {
        return "XYZ";
    }
}
