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

public abstract class ToStringBuilderAppenderVectorTestCase<A extends ToStringBuilderAppenderVector<V>, V> extends ToStringBuilderAppenderTestCase2<A, V> {

    ToStringBuilderAppenderVectorTestCase() {
        super();
    }

    @Test
    public final void testValueNull() {
        final ToStringBuilder b = this.builder();

        b.label(LABEL1);
        this.value(b, null);

        this.buildAndCheck(b, "");
    }

    @Test
    public final void testValueNullObject() {
        final ToStringBuilder b = this.builder();

        b.label(LABEL1);
        b.value((Object) null);

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

        this.buildAndCheck(b, this.value1ToString() + SEPARATOR + this.value2ToString(valueSeparator));
    }

    @Test
    public final void testLabelValueSeparator() {
        this.labelValueSeparatorValueAndCheck(ToStringBuilder.DEFAULT_VALUE_SEPARATOR);
    }

    @Test
    public final void testLabelValueSeparator2() {
        this.labelValueSeparatorValueAndCheck("$");
    }

    private void labelValueSeparatorValueAndCheck(final String valueSeparator) {
        final ToStringBuilder b = this.builder();

        b.label(LABEL1);
        b.valueSeparator(valueSeparator);
        this.value(b, this.value2());

        this.buildAndCheck(b, LABEL1 + LABEL_SEPARATOR + this.value2ToString(valueSeparator));
    }

    @Test
    public final void testLabelValueLabelValue() {
        this.labelSeparatorValueValueAndCheck(ToStringBuilder.SEPARATOR);
    }

    @Test
    public final void testLabelValueLabelValue2() {
        this.labelSeparatorValueValueAndCheck("$");
    }

    private void labelSeparatorValueValueAndCheck(final String separator) {
        final V value1 = this.value1();
        final V value2 = this.value2();

        final ToStringBuilder b = this.builder();
        b.separator(separator);
        b.valueSeparator(separator);

        this.value(b, value1);
        this.value(b, value2);

        this.buildAndCheck(b, this.value1ToString() + separator + this.value2ToString(separator));
    }

    @Test
    public void testValueObjectOverload() {
        final ToStringBuilder b = this.builder();

        b.value(this.value1());
        b.label(LABEL2);
        b.value(this.value2());

        this.buildAndCheck(b, this.value1ToString() + SEPARATOR + LABEL2 + LABEL_SEPARATOR + this.value2ToString(VALUE_SEPARATOR));
    }

    @Test
    public final void testSurroundValueDefault() {
        this.surroundValueAndCheck("(", ")", this.defaultValue(), "");
    }

    @Test
    public final void testSurroundValueDefault2() {
        this.surroundValueAndCheck("(", ")", this.defaultValue2(), "");
    }

    @Test
    public final void testSurroundValue() {
        this.surroundValueAndCheck("(", ")", this.value1(), "(" + this.value1ToString() + ")");
    }

    @Test
    public final void testSurroundValue2() {
        this.surroundValueAndCheck("(((", ")))", this.value1(), "(((" + this.value1ToString() + ")))");
    }

    private void surroundValueAndCheck(final String before, final String after, final V value, final String expected) {
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

        b.label(LABEL1);
        this.value(b, this.value2());
        this.buildAndCheck(b, "");
    }

    final ToStringBuilder builder() {
        return ToStringBuilder.empty().disable(ToStringBuilderOption.ESCAPE)
            .disable(ToStringBuilderOption.HEX_BYTES)
            .disable(ToStringBuilderOption.HEX_WHOLE_NUMBERS)
            .enable(ToStringBuilderOption.INLINE_ELEMENTS)
            .disable(ToStringBuilderOption.QUOTE)
            .disable(ToStringBuilderOption.ONE_AND_ZERO_BOOLEAN_ARRAYS)
            .labelSeparator(LABEL_SEPARATOR)
            .valueSeparator(VALUE_SEPARATOR)
            .separator(SEPARATOR);
    }

    final V defaultValue2() {
        return null;
    }

    @Override final String defaultAppendToString(final V value) {
        return String.valueOf(value);
    }

    @Override final String value2ToString() {
        return this.value2ToString(VALUE_SEPARATOR);
    }

    abstract String value2ToString(final String separator);
}
