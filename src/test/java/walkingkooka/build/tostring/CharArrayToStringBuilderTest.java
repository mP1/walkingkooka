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

public final class CharArrayToStringBuilderTest extends VectorToStringBuilderTestCase<char[]> {

    @Test
    public void testValueIncludesDefault() {
        final ToStringBuilder b = this.builder();
        b.label(LABEL);
        b.value("\0ab".toCharArray());

        this.buildAndCheck(b, "LABEL=\0ab");
    }

    @Test
    public void testValueEscaped() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.ESCAPE);

        b.label(LABEL);
        b.value("\na".toCharArray());

        this.buildAndCheck(b, "LABEL=\\na");
    }

    @Test
    public void testValueInline() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.INLINE_ELEMENTS);

        b.label(LABEL);
        b.value("abc".toCharArray());

        this.buildAndCheck(b, "LABEL=abc");
    }

    @Test
    public void testValueQuoted() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.QUOTE);

        b.label(LABEL);
        b.value("\0\na".toCharArray());

        this.buildAndCheck(b, "LABEL=\"\0\na\"");
    }

    @Test
    public void testValueQuotedAndEscaped() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.QUOTE);
        b.enable(ToStringBuilderOption.ESCAPE);

        b.label(LABEL);
        b.value("\0\na".toCharArray());

        this.buildAndCheck(b, "LABEL=\"\\0\\na\"");
    }

    @Override
    char[] defaultValue() {
        return new char[0];
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
}
