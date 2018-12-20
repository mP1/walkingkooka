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

public abstract class VectorToStringBuilderTestCase<T> extends ToStringBuilderTestCase<T> {

    @Test
    public final void testValueNull(){
        final ToStringBuilder b = this.builder();

        b.label(LABEL);
        this.value(b, null);

        this.buildAndCheck(b, "");
    }

    @Test
    public final void testValueSeparator() {
        this.testValueSeparatorAndCheck(ToStringBuilder.DEFAULT_VALUE_SEPARATOR);
    }

    @Test
    public final void testValueSeparator2() {
        this.testValueSeparatorAndCheck("$");
    }

    private void testValueSeparatorAndCheck(final String valueSeparator) {
        final ToStringBuilder b = this.builder();

        b.valueSeparator(valueSeparator);
        this.value(b, this.value1());
        this.value(b, this.value2());

        this.buildAndCheck(b, this.value1ToString() + ToStringBuilder.SEPARATOR + this.value2ToString(valueSeparator));
    }

    @Test
    public final void testLabelValueSeparator() {
        this.testLabelValueSeparatorAndCheck(ToStringBuilder.DEFAULT_VALUE_SEPARATOR);
    }

    @Test
    public final void testLabelValueSeparator2() {
        this.testLabelValueSeparatorAndCheck("$");
    }

    private void testLabelValueSeparatorAndCheck(final String valueSeparator) {
        final ToStringBuilder b = this.builder();

        b.label(LABEL);
        b.valueSeparator(valueSeparator);
        this.value(b, this.value2());

        this.buildAndCheck(b, "LABEL=" + this.value2ToString(valueSeparator));
    }

    @Test
    public void testLabelValueLabelValue() {
        this.testLabelValueLabelValueAndCheck(ToStringBuilder.SEPARATOR);
    }

    @Test
    public void testLabelValueLabelValue2() {
        this.testLabelValueLabelValueAndCheck("$");
    }

    private void testLabelValueLabelValueAndCheck(final String separator) {
        final T value1 = this.value1();
        final T value2 = this.value2();

        final ToStringBuilder b = this.builder();
        b.separator(separator);

        this.value(b, value1);
        this.value(b, value2);

        this.buildAndCheck(b, this.value1ToString() + separator + this.value2ToString());
    }

    @Test
    public void testSurroundValueDefault() {
        this.testSurroundValueAndCheck("(", ")", this.defaultValue(), "");
    }

    @Test
    public void testSurroundValueDefault2() {
        this.testSurroundValueAndCheck("(", ")", this.defaultValue2(), "");
    }

    @Test
    public void testSurroundValue() {
        this.testSurroundValueAndCheck("(", ")", this.value1(), "(" + this.value1ToString() + ")");
    }

    @Test
    public void testSurroundValue2() {
        this.testSurroundValueAndCheck("(((", ")))", this.value1(), "(((" + this.value1ToString() + ")))");
    }

    private void testSurroundValueAndCheck(final String before, final String after, final T value, final String expected) {
        final ToStringBuilder b = this.builder();
        b.surroundValues(before, after);
        this.value(b, value);
        this.buildAndCheck(b, expected);
    }

    @Test
    public final void testDisableInlineElements() {
        final ToStringBuilder b = this.builder();
        b.disable(ToStringBuilderOption.INLINE_ELEMENTS);

        this.value(b, this.value2());
        this.buildAndCheck(b, "");
    }

    @Test
    public final void testDisableInlineElementsLabel() {
        final ToStringBuilder b = this.builder();
        b.disable(ToStringBuilderOption.INLINE_ELEMENTS);

        b.label(LABEL);
        this.value(b, this.value2());
        this.buildAndCheck(b, "");
    }

    final ToStringBuilder builder() {
        return ToStringBuilder.empty().disable(ToStringBuilderOption.ESCAPE)
                .disable(ToStringBuilderOption.HEX_BYTES)
                .disable(ToStringBuilderOption.HEX_WHOLE_NUMBERS)
                .enable(ToStringBuilderOption.INLINE_ELEMENTS)
                .disable(ToStringBuilderOption.QUOTE)
                .disable(ToStringBuilderOption.ONE_AND_ZERO_BOOLEAN_ARRAYS);
    }

    final T defaultValue2() {
        return null;
    }

    @Override
    final String defaultValueToString() {
        return "";
    }

    @Override
    final String value2ToString() {
        return this.value2ToString(ToStringBuilder.DEFAULT_VALUE_SEPARATOR);
    }

    abstract String value2ToString(final String separator);
}
