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
import walkingkooka.text.CharSequences;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ToStringBuilderTestCase<T> {

    static final String LABEL = "LABEL";

    ToStringBuilderTestCase() {
        super();
    }

    @Test
    public final void testAppendDefault() {
        final T value = this.defaultValue();
        final String valueString = String.valueOf(value);

        final ToStringBuilder b = this.builder();
        this.append(b, value);

        this.buildAndCheck(b, valueString);
    }

    @Test
    public final void testAppend() {
        final T value = this.value1();
        final String valueString = value.toString();

        final ToStringBuilder b = this.builder();
        this.append(b, value);

        this.buildAndCheck(b, valueString);
    }

    @Test
    public final void testValueDefault() {
        final ToStringBuilder b = this.builder();

        this.value(b, this.defaultValue());

        this.buildAndCheck(b, "");
    }

    @Test
    public final void testValue() {
        final ToStringBuilder b = this.builder();

        this.value(b, this.value1());

        this.buildAndCheck(b, this.value1ToString());
    }

    @Test
    public final void testValueDisableSkipDefaultValue() {
        final ToStringBuilder b = this.builder();
        b.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);

        this.value(b, this.defaultValue());

        this.buildAndCheck(b, this.defaultValueToString());
    }

    @Test
    public final void testValueWithValueSeparator() {
        this.valueWithValueSeparatorAndCheck(" ");
    }

    @Test
    public final void testValueWithValueSeparator2() {
        this.valueWithValueSeparatorAndCheck("#");
    }

    private void valueWithValueSeparatorAndCheck(final String valueSeparator) {
        final ToStringBuilder b = this.builder();
        b.separator(valueSeparator);

        this.value(b, this.value1());
        this.value(b, this.value2());

        this.buildAndCheck(b, this.value1ToString() + valueSeparator + this.value2ToString());
    }

    @Test
    public final void testLabelSeparatorLabelAppend() {
        final T value1 = this.value1();
        final String value1ToString = value1.toString();

        final ToStringBuilder b = this.builder();
        b.labelSeparator(":");
        b.label(LABEL);
        this.append(b, value1);

        this.buildAndCheck(b, value1ToString);
    }

    @Test // overridden by MapToStringBuilderTest
    public void testLabelSeparatorLabelValue() {
        this.labelSeparatorLabelValueAndCheck(":");
    }

    @Test // overridden by MapToStringBuilderTest
    public void testLabelSeparatorLabelValue2() {
        this.labelSeparatorLabelValueAndCheck("=");
    }

    private void labelSeparatorLabelValueAndCheck(final String labelSeparator) {
        final ToStringBuilder b = this.builder();

        b.labelSeparator(labelSeparator);
        b.label(LABEL);
        this.value(b, this.value1());

        this.buildAndCheck(b, LABEL + labelSeparator + this.value1ToString());
    }

    @Test
    public final void testSeparatorLabelAppendValueIgnoresSeparator() {
        final ToStringBuilder b = this.builder();
        b.separator("!!!");

        final T value1 = this.value1();

        b.label(LABEL);
        this.append(b, value1);
        this.value(b, this.value2());

        this.buildAndCheck(b, value1 + this.value2ToString());
    }

    @Test
    public final void testSeparatorLabelValueValue() {
        final String separator = "!";

        final ToStringBuilder b = this.builder();
        b.separator(separator);

        b.label(LABEL);
        this.value(b, this.value1());
        this.value(b, this.value2());

        this.buildAndCheck(b, LABEL + "=" + this.value1ToString() + separator + this.value2ToString());
    }

    abstract ToStringBuilder builder();

    abstract T defaultValue();

    abstract T value1();

    abstract T value2();

    abstract void append(final ToStringBuilder builder, final T value);

    abstract void value(final ToStringBuilder builder, final T value);

    abstract String defaultValueToString();

    abstract String value1ToString();

    abstract String value2ToString();

    final void buildAndCheck(final ToStringBuilder builder) {
        this.buildAndCheck(builder, "");
    }

    final void buildAndCheck(final ToStringBuilder builder, final String expected) {
        final String built = builder.build();
        if (false == expected.equals(built)) {
            assertEquals(format(expected),
                    format(built),
                    () -> "options=" + builder.options.toString());
        }
    }

    static String format(final String string) {
        return string.length() + "=" + CharSequences.quoteAndEscape(string);
    }
}
