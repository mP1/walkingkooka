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

public final class ToStringBuilderAppenderCharSequenceScalarTest extends ToStringBuilderAppenderScalarTestCase<ToStringBuilderAppenderCharSequenceScalar, CharSequence> {

    @Test
    public void testValueDefaultEmpty() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.value("");

        this.buildAndCheck(b, "");
    }

    @Test
    public void testLabelValueDefaultEmpty() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.label(LABEL1);
        b.value("");

        this.buildAndCheck(b, "");
    }

    @Test
    public void testQuoted() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.enable(ToStringBuilderOption.QUOTE);

        b.label(LABEL1);
        b.value("abc\n");

        this.buildAndCheck(b, LABEL1 + "=\"abc\n\"");
    }

    @Test
    public void testEscaped() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.disable(ToStringBuilderOption.QUOTE);
        b.enable(ToStringBuilderOption.ESCAPE);

        b.label(LABEL1);
        b.value("abc\n");

        this.buildAndCheck(b, LABEL1 + "=abc\\n");
    }

    @Override
    void append(final ToStringBuilder builder, final CharSequence value) {
        builder.append((CharSequence) value);
    }

    @Override
    void value(final ToStringBuilder builder, final CharSequence value) {
        builder.value((CharSequence) value);
    }

    @Override
    CharSequence defaultValue() {
        return "";
    }

    @Override
    CharSequence value1() {
        return "ABC";
    }

    @Override
    CharSequence value2() {
        return "XYZ";
    }

    @Override
    String defaultAppendToString(final CharSequence value) {
        return this.defaultValueToString(value);
    }

    @Override
    String defaultValueToString(final CharSequence value) {
        return "";
    }

    @Override
    String value1ToString() {
        return "ABC";
    }

    @Override
    String value2ToString() {
        return "XYZ";
    }

    // ClassTesting......................................................................................................

    @Override
    public final Class<ToStringBuilderAppenderCharSequenceScalar> type() {
        return ToStringBuilderAppenderCharSequenceScalar.class;
    }
}
