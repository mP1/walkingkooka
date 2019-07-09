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

import java.util.List;

public abstract class ListToStringBuilderTestCase<T> extends VectorToStringBuilderTestCase<T> {

    ListToStringBuilderTestCase() {
        super();
    }

    @Test
    public final void testLabelValueIncludesDefault() {
        final ToStringBuilder b = this.builder();
        b.label(LABEL);
        b.value(this.toValue(Lists.<Object>of(false, (byte) 0, (short) 0, 0, 0L, 0.0f, 0.0, "")));

        this.buildAndCheck(b, "LABEL=false, 0, 0, 0, 0, 0.0, 0.0, ");
    }

    @Test
    public final void testQuotesValueCharSequence() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.QUOTE);
        b.value(this.toValue(Lists.of("ABC", 'z')));

        this.buildAndCheck(b, "\"ABC\", \'z\'");
    }

    @Test
    public final void testLabelValuesLabelValuesLabelValues() {
        final ToStringBuilder b = this.builder();
        b.labelSeparator(": ");
        b.valueSeparator(", ");

        b.label(LABEL);
        b.value(this.toValue(Lists.of(1, 2, 3)));

        b.label("LABEL2");
        b.value(this.toValue(Lists.of(4, 5, 6)));

        b.label("LABEL3");
        b.value(this.toValue(Lists.of(7, 8, 9)));


        this.buildAndCheck(b, "LABEL: 1, 2, 3 LABEL2: 4, 5, 6 LABEL3: 7, 8, 9");
    }

    @Test
    public final void testLabelValuesLabelValuesLabelValues2() {
        final ToStringBuilder b = this.builder();
        b.labelSeparator(":: ");
        b.valueSeparator(",, ");
        b.separator("// ");

        b.label(LABEL);
        b.value(this.uses(this.toValue(Lists.of(1, 2, 3))));

        b.label("LABEL2");
        b.value(this.uses(this.toValue(Lists.of(4, 5, 6))));

        b.label("LABEL3");
        b.value(this.uses(this.toValue(Lists.of(7, 8, 9))));

        this.buildAndCheck(b, "LABEL:: 1,, 2,, 3// LABEL2:: 4,, 5,, 6// LABEL3:: 7,, 8,, 9");
    }

    @Test
    public final void testLabelValuesLabelValuesLabelValues3() {
        final ToStringBuilder b = this.builder();
        b.labelSeparator(":: ");
        b.valueSeparator(",, ");
        b.separator("// ");

        b.label(LABEL);
        b.value(this.toValue(Lists.of(this.uses(1))));

        b.label("LABEL2");
        b.value(this.toValue(Lists.of(this.uses(2))));

        b.label("LABEL3");
        b.value(this.toValue(Lists.of(this.uses(3))));

        this.buildAndCheck(b, "LABEL:: 1// LABEL2:: 2// LABEL3:: 3");
    }

    private UsesToStringBuilder uses(final Object values) {
        return new UsesToStringBuilder() {
            @Override
            public void buildToString(ToStringBuilder builder) {
                builder.value(values);
            }
        };
    }

    @Override
    final T defaultValue() {
        return this.toValue(Lists.empty());
    }

    @Override
    final T value1() {
        return this.toValue(Lists.of(1));
    }

    @Override
    final T value2() {
        return this.toValue(Lists.of(1, 22, "abc"));
    }

    abstract T toValue(final List<?> list);

    @Override
    final void append(final ToStringBuilder builder, final T value) {
        builder.append(value);
    }

    @Override
    final void value(final ToStringBuilder builder, final T value) {
        builder.value(value);
    }

    @Override
    final String value1ToString() {
        return "1";
    }

    @Override
    final String value2ToString(final String separator) {
        return "1" + separator + "22" + separator + "abc";
    }
}
