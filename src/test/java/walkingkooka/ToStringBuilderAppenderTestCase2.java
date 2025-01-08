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

public abstract class ToStringBuilderAppenderTestCase2<A extends ToStringBuilderAppender<V>, V> extends ToStringBuilderAppenderTestCase<A, V>
    implements ToStringTesting<A> {

    final static String SEPARATOR = "< SEPARATOR >";
    final static String LABEL_SEPARATOR = "< LABEL1-SEPARATOR >";
    final static String VALUE_SEPARATOR = "< VALUE-SEPARATOR >";

    static final String LABEL1 = "Label1";
    static final String LABEL2 = "label2";
    static final String LABEL3 = "label3";
    static final String LABEL4 = "label4";

    ToStringBuilderAppenderTestCase2() {
        super();
    }

    @Test
    public final void testAppendDefault() {
        final V value = this.defaultValue();
        final String valueString = this.defaultAppendToString(value);

        final ToStringBuilder b = this.builder();
        this.append(b, value);

        this.buildAndCheck(b, valueString);
    }

    @Test
    public final void testAppend() {
        final V value = this.value1();
        final String valueString = value.toString();

        final ToStringBuilder b = this.builder();
        this.append(b, value);

        this.buildAndCheck(b, valueString);
    }

    @Test
    public final void testSkipDefaultValueEnabledValueDefault() {
        final ToStringBuilder b = this.builder();

        final V value = this.defaultValue();
        this.value(b, value);

        this.buildAndCheck(b, "");
    }

    @Test
    public final void testValue() {
        final ToStringBuilder b = this.builder();

        this.value(b, this.value1());

        this.buildAndCheck(b, this.value1ToString());
    }

    @Test
    public final void testDisableSkipDefaultValueValue() {
        final ToStringBuilder b = this.builder();
        b.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);

        final V value = this.defaultValue();
        this.value(b, value);

        this.buildAndCheck(b, this.defaultValueToString(value));
    }

    @Test
    public final void testValueValueSeparatedBySeparator() {
        final ToStringBuilder b = this.builder();

        this.value(b, this.value1());
        this.value(b, this.value2());

        this.buildAndCheck(b, this.value1ToString() + SEPARATOR + this.value2ToString());
    }

    @Test
    public final void testLabelSeparatorLabelAppend() {
        final V value1 = this.value1();
        final String value1ToString = value1.toString();

        final ToStringBuilder b = this.builder();
        b.label(LABEL1);
        this.append(b, value1);

        this.buildAndCheck(b, value1ToString);
    }

    @Test
    public final void testLabelSeparatorLabelValue() {
        final ToStringBuilder b = this.builder();

        b.label(LABEL1);
        this.value(b, this.value1());

        this.buildAndCheck(b, LABEL1 + LABEL_SEPARATOR + this.value1ToString());
    }

    @Test
    public final void testSeparatorLabelAppendValueIgnoresLabelAndSeparator() {
        final ToStringBuilder b = this.builder();
        final V value1 = this.value1();

        b.label(LABEL1);
        this.append(b, value1);
        this.value(b, this.value2());

        this.buildAndCheck(b, value1 + this.value2ToString());
    }

    @Test
    public final void testLabelValueValue() {
        final ToStringBuilder b = this.builder();

        b.label(LABEL1);
        this.value(b, this.value1());
        this.value(b, this.value2());

        this.buildAndCheck(b, LABEL1 + LABEL_SEPARATOR + this.value1ToString() + SEPARATOR + this.value2ToString());
    }

    abstract ToStringBuilder builder();

    abstract V defaultValue();

    abstract V value1();

    abstract V value2();

    abstract void append(final ToStringBuilder builder, final V value);

    abstract void value(final ToStringBuilder builder, final V value);

    abstract String defaultAppendToString(final V value);

    abstract String defaultValueToString(final V value);

    abstract String value1ToString();

    abstract String value2ToString();

    final void buildAndCheck(final ToStringBuilder builder) {
        this.buildAndCheck(builder, "");
    }

    final void buildAndCheck(final ToStringBuilder builder, final String expected) {
        final String built = builder.build();
        if (false == expected.equals(built)) {
            this.checkEquals(format(expected),
                format(built),
                () -> "options=" + builder.options.toString());
        }
    }

    static String format(final String string) {
        return string.length() + "=" + CharSequences.quoteAndEscape(string);
    }
}
