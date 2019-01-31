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

import org.junit.jupiter.api.Test;
import walkingkooka.test.TestCase;
import walkingkooka.text.CharSequences;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ToStringBuilderTestCase<T> extends TestCase {

    static final String LABEL = "LABEL";

    @Test
    public void testAppendDefault() {
        final T value = this.defaultValue();
        final String valueString = String.valueOf(value);

        final ToStringBuilder b = this.builder();
        this.append(b, value);

        this.buildAndCheck(b, valueString);
    }

    @Test
    public void testAppend() {
        final T value = this.value1();
        final String valueString = value.toString();

        final ToStringBuilder b = this.builder();
        this.append(b, value);

        this.buildAndCheck(b, valueString);
    }

    @Test
    public void testValueDefault() {
        final ToStringBuilder b = this.builder();

        this.value(b, this.defaultValue());

        this.buildAndCheck(b, "");
    }

    @Test
    public void testValue() {
        final ToStringBuilder b = this.builder();

        this.value(b, this.value1());

        this.buildAndCheck(b, this.value1ToString());
    }

    @Test
    public void testValueDisableSkipDefaultValue() {
        final ToStringBuilder b = this.builder();
        b.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);

        this.value(b, this.defaultValue());

        this.buildAndCheck(b, this.defaultValueToString());
    }

    @Test
    public void testValueWithValueSeparator() {
        this.testValueWithValueSeparatorAndCheck(" ");
    }

    @Test
    public void testValueWithValueSeparator2() {
        this.testValueWithValueSeparatorAndCheck("#");
    }

    private void testValueWithValueSeparatorAndCheck(final String valueSeparator) {
        final ToStringBuilder b = this.builder();
        b.separator(valueSeparator);

        this.value(b, this.value1());
        this.value(b, this.value2());

        this.buildAndCheck(b, this.value1ToString() + valueSeparator + this.value2ToString());
    }

    @Test
    public void testLabelAppend() {
        final T value1 = this.value1();
        final String value1ToString = value1.toString();

        final ToStringBuilder b = this.builder();
        b.labelSeparator(":");
        b.label(LABEL);
        this.append(b, value1);

        this.buildAndCheck(b, value1ToString);
    }

    @Test
    public void testLabelValue() {
        this.testLabelValueAndCheck(":");
    }

    @Test
    public void testLabelValueLabel2() {
        this.testLabelValueAndCheck("=");
    }

    private void testLabelValueAndCheck(final String labelSeparator) {
        final ToStringBuilder b = this.builder();

        b.labelSeparator(labelSeparator);
        b.label(LABEL);
        this.value(b, this.value1());

        this.buildAndCheck(b, LABEL + labelSeparator + this.value1ToString());
    }

    @Test
    public void testLabelAppendValue() {
        final ToStringBuilder b = this.builder();
        b.separator("!!!");

        final T value1 = this.value1();

        b.label(LABEL);
        this.append(b, value1);
        this.value(b, this.value2());

        this.buildAndCheck(b, value1 + this.value2ToString());
    }

    @Test
    public void testLabelValueValue() {
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
            assertEquals("options=" + builder.options.toString(),
                    format(expected),
                    format(built));
        }
    }

    static String format(final String string) {
        return string.length() + "=" + CharSequences.quoteAndEscape(string);
    }
}
