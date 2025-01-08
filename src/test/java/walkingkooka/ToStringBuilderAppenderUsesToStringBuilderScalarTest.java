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
import walkingkooka.collect.list.Lists;

public final class ToStringBuilderAppenderUsesToStringBuilderScalarTest extends ToStringBuilderAppenderScalarTestCase<ToStringBuilderAppenderUsesToStringBuilderScalar, UsesToStringBuilder> {

    private static final String VALUE1 = "value1";

    @Test
    public void testLabelEmptyUsesToStringBuilderLabelRemoved() {
        this.buildAndCheck(this.builder()//
                .label(LABEL1)//
                .value(this.usesToStringBuilder("")), //
            "");
    }

    @Test
    public void testEmptyUsesToStringBuilderSkipped() {
        this.buildAndCheck(this.builder()//
                .value(this.usesToStringBuilder("")) //
                .value("next"),//
            "next");
    }

    @Test
    public void testLabelEmptyUsesToStringBuilderLabelValueRemovesEmptyLabel() {
        this.buildAndCheck(this.builder()//
                .disable(ToStringBuilderOption.QUOTE)//
                .label(LABEL1).value(this.usesToStringBuilder(""))//
                .label(LABEL2).value(2), //
            LABEL2 + LABEL_SEPARATOR + "2");
    }

    @Test
    public void testLabelUsesToStringBuilderLabelValue() {
        final String value = "a1";

        this.buildAndCheck(this.builder()//
                .disable(ToStringBuilderOption.QUOTE)//
                .label(LABEL1).value(this.usesToStringBuilder(value))//
                .label(LABEL2).value(2), //
            LABEL1 + LABEL_SEPARATOR + value + SEPARATOR +
                LABEL2 + LABEL_SEPARATOR + 2);
    }

    @Test
    public void testLabelUsesToStringBuilder() {
        this.buildAndCheck(this.builder()//
                .disable(ToStringBuilderOption.QUOTE)//
                .label(LABEL1).value(1) //
                .label(LABEL2).value(this.usesToStringBuilder(2))//
                .label(LABEL3).value(3), //
            LABEL1 + LABEL_SEPARATOR + 1 + SEPARATOR +
                LABEL2 + LABEL_SEPARATOR + 2 + SEPARATOR +
                LABEL3 + LABEL_SEPARATOR + 3);
    }

    @Test
    public void testLabelUsesToStringBuilderChangesBuilderState() {
        final String labelSeparator2 = "<LABEL1-SEPARATOR-2>";
        final String separator2 = "<SEPARATOR-2>";
        final String valueSeparator2 = "<VALUE-SEPARATOR-2>";
        final String value3a = "value3a";
        final String value3b = "value3b";

        this.buildAndCheck(this.builder()//
                .disable(ToStringBuilderOption.QUOTE)//
                .enable(ToStringBuilderOption.INLINE_ELEMENTS)
                .label(LABEL1)
                .value(builder -> {
                    builder.separator("/")
                        .labelSeparator(labelSeparator2)
                        .separator(separator2)
                        .valueSeparator(valueSeparator2)
                        .enable(ToStringBuilderOption.QUOTE);
                    builder.value(VALUE1);
                    builder.label(LABEL2)
                        .value(Lists.of(20, 21));
                })//
                .label(LABEL3)
                .value(Lists.of(value3a, value3b)), //
            LABEL1 + LABEL_SEPARATOR +
                '"' + VALUE1 + '"' + separator2 +
                LABEL2 + labelSeparator2 + 20 + valueSeparator2 + 21 + SEPARATOR +
                LABEL3 + LABEL_SEPARATOR + value3a + VALUE_SEPARATOR + value3b);
    }

    @Test
    public void testLabelUsesToStringBuilderChangesBuilderState2() {
        final String labelSeparator2 = "<LABEL1-SEPARATOR-2>";
        final String separator2 = "<SEPARATOR-2>";
        final String valueSeparator2 = "<VALUE-SEPARATOR-2>";

        final String value2 = "value2";
        final String value4a = "value4a";
        final String value4b = "value4b";

        this.buildAndCheck(this.builder()//
                .disable(ToStringBuilderOption.QUOTE)//
                .enable(ToStringBuilderOption.INLINE_ELEMENTS)//
                .label(LABEL1)
                .value(VALUE1) //
                .label(LABEL2)
                .value(builder -> {
                    builder.separator("/")
                        .labelSeparator(labelSeparator2)
                        .separator(separator2)
                        .valueSeparator(valueSeparator2)
                        .enable(ToStringBuilderOption.QUOTE);
                    builder.value(value2);
                    builder.label(LABEL3).value(Lists.of(30, 31));
                })//
                .label(LABEL4)
                .value(Lists.of(value4a, value4b)), //
            LABEL1 + LABEL_SEPARATOR + VALUE1 + SEPARATOR +
                LABEL2 + LABEL_SEPARATOR + '"' + value2 + '"' + separator2 +
                LABEL3 + labelSeparator2 + 30 + valueSeparator2 + 31 + SEPARATOR +
                LABEL4 + LABEL_SEPARATOR + value4a + VALUE_SEPARATOR + value4b);
    }

    @Test
    public void testLabelValueLabelValueUsesToStringBuilderAndSeparator() {
        this.buildAndCheck(this.builder()//
                .label(LABEL1).value(1) //
                .label(LABEL2).value(this.usesToStringBuilder(2))//
                .label(LABEL3).value(3), //
            LABEL1 + LABEL_SEPARATOR + 1 + SEPARATOR +
                LABEL2 + LABEL_SEPARATOR + 2 + SEPARATOR +
                LABEL3 + LABEL_SEPARATOR + 3);
    }

    @Test// #1908
    public void testLabelValueUsesToStringBuilderLabelValueAndSeparatorDoubleLabelShouldntInsertSeparatorTwice() {
        this.buildAndCheck(this.builder()//
                .label(LABEL1) //
                .value(this.usesToStringBuilder(LABEL2, 2))//
                .label(LABEL3)
                .value(3), //
            LABEL1 + LABEL_SEPARATOR +
                LABEL2 + LABEL_SEPARATOR + 2 + SEPARATOR +
                LABEL3 + LABEL_SEPARATOR + 3);
    }

    @Test// #1908
    public void testLabelValueValueUsesToStringBuilderLabelValueAndSeparatorDoubleLabelShouldntInsertSeparatorTwice() {
        this.buildAndCheck(this.builder()//
                .label(LABEL1) //
                .value(1)//
                .value(this.usesToStringBuilder(LABEL2, this.usesToStringBuilder(LABEL3, 3))),//
            LABEL1 + LABEL_SEPARATOR + 1 + SEPARATOR +
                LABEL2 + LABEL_SEPARATOR +
                LABEL3 + LABEL_SEPARATOR + 3);
    }

    @Override
    void append(final ToStringBuilder builder, final UsesToStringBuilder value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder, final UsesToStringBuilder value) {
        builder.value(value);
    }

    @Override
    UsesToStringBuilder defaultValue() {
        return null;
    }

    @Override
    UsesToStringBuilder value1() {
        return this.usesToStringBuilder("abc123");
    }

    @Override
    UsesToStringBuilder value2() {
        return this.usesToStringBuilder(456);
    }

    @Override
    String defaultAppendToString(final UsesToStringBuilder value) {
        return String.valueOf(value);
    }

    @Override
    String defaultValueToString(final UsesToStringBuilder value) {
        return "";
    }

    @Override
    String value1ToString() {
        return "abc123";
    }

    @Override
    String value2ToString() {
        return "456";
    }

    private UsesToStringBuilder usesToStringBuilder(final Object value) {
        return this.usesToStringBuilder(null, value);
    }

    private UsesToStringBuilder usesToStringBuilder(final String label, final Object value) {
        return new UsesToStringBuilder() {

            @Override
            public void buildToString(final ToStringBuilder builder) {
                if (null != label) {
                    builder.label(label);
                }
                builder.value(value);
            }

            @Override
            public String toString() {
                return (null != label ? label + "=" : "") + value;
            }
        };
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ToStringBuilderAppenderUsesToStringBuilderScalar> type() {
        return ToStringBuilderAppenderUsesToStringBuilderScalar.class;
    }
}
