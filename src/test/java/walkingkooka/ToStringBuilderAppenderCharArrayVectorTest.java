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

public final class ToStringBuilderAppenderCharArrayVectorTest extends ToStringBuilderAppenderVectorTestCase<ToStringBuilderAppenderCharArrayVector, char[]> {

    @Test
    public void testValueCharArrayNull() {
        final ToStringBuilder b = this.builder();
        b.label(LABEL1);
        b.value((char[]) null);
        b.label(LABEL2);
        b.value(2);

        this.buildAndCheck(b, LABEL2 + LABEL_SEPARATOR + 2);
    }

    @Test
    public void testValueIncludesDefault() {
        final ToStringBuilder b = this.builder();
        b.label(LABEL1);
        b.value("\0ab".toCharArray());

        this.buildAndCheck(b, LABEL1 + LABEL_SEPARATOR + "\0ab");
    }

    @Test
    public void testValueEscaped() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.ESCAPE);

        b.label(LABEL1);
        b.value("\na".toCharArray());

        this.buildAndCheck(b, LABEL1 + LABEL_SEPARATOR + "\\na");
    }

    @Test
    public void testValueInline() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.INLINE_ELEMENTS);

        b.label(LABEL1);
        b.value("abc".toCharArray());

        this.buildAndCheck(b, LABEL1 + LABEL_SEPARATOR + "abc");
    }

    @Test
    public void testValueQuoted() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.QUOTE);

        b.label(LABEL1);
        b.value("\0\na".toCharArray());

        this.buildAndCheck(b, LABEL1 + LABEL_SEPARATOR + "\"\0\na\"");
    }

    @Test
    public void testValueQuotedAndEscaped() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.QUOTE);
        b.enable(ToStringBuilderOption.ESCAPE);

        b.label(LABEL1);
        b.value("\0\na".toCharArray());

        this.buildAndCheck(b, LABEL1 + LABEL_SEPARATOR + "\"\\0\\na\"");
    }

    @Override
    char[] defaultValue() {
        return new char[0];
    }

    @Override final String defaultValueToString(final char[] value) {
        return "";
    }

    @Override
    char[] value1() {
        return "a".toCharArray();
    }

    @Override
    char[] value2() {
        return "xyz".toCharArray();
    }

    @Override
    void append(final ToStringBuilder builder, final char[] value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder, final char[] value) {
        builder.value(value);
    }

    @Override
    String value1ToString() {
        return "a";
    }

    @Override
    String value2ToString(final String separator) {
        return "xyz";
    }

    // ClassTesting......................................................................................................

    @Override
    public Class<ToStringBuilderAppenderCharArrayVector> type() {
        return ToStringBuilderAppenderCharArrayVector.class;
    }
}
