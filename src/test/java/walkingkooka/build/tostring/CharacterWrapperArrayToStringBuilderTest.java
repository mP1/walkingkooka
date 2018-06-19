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
import walkingkooka.collect.list.Lists;

public class CharacterWrapperArrayToStringBuilderTest extends VectorToStringBuilderTestCase<Character[]> {

    @Test
    public void testValueIncludesDefault(){
        final ToStringBuilder b = this.builder();
        b.label(LABEL);
        b.value(array("\0ab"));

        this.buildAndCheck(b, "LABEL=\0, a, b");
    }

    @Test
    public void testValueEscaped(){
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.ESCAPE);

        b.label(LABEL);
        b.value(array("\nab"));

        this.buildAndCheck(b, "LABEL=\\n, a, b");
    }

    @Test
    public void testValueInline(){
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.INLINE_ELEMENTS);

        b.label(LABEL);
        b.value(array("abc"));

        this.buildAndCheck(b, "LABEL=a, b, c");
    }

    @Test
    public void testValueQuoted(){
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.QUOTE);

        b.label(LABEL);
        b.value(array("\0\na".toCharArray()));

        this.buildAndCheck(b, "LABEL='\0', '\n', 'a'");
    }

    @Test
    public void testValueQuotedAndEscaped(){
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.QUOTE);
        b.enable(ToStringBuilderOption.ESCAPE);

        b.label(LABEL);
        b.value(array("\0\na"));

        this.buildAndCheck(b, "LABEL='\\0', '\\n', 'a'");
    }

    @Test
    public void testValueSeparatorList() {
        final ToStringBuilder b = this.builder();
        b.label(LABEL);
        b.valueSeparator("$");
        b.value(Lists.of(array("abc")));

        this.buildAndCheck(b, "LABEL=a$b$c");
    }

    private Character[] array(final String content) {
        return this.array(content.toCharArray());
    }

    private Character[] array(final char...elements){
        final Character[] array = new Character[elements.length];
        int i = 0;
        for(char c : elements){
            array[i++] = c;
        }
        return array;
    }

    @Override
    Character[] defaultValue() {
        return new Character[0];
    }

    @Override
    Character[] value1() {
        return new Character[]{'a'};
    }

    @Override
    Character[] value2() {
        return new Character[]{'x', 'y', 'z'};
    }

    @Override
    void append(final ToStringBuilder builder, final Character[] value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder, final Character[] value) {
        builder.value(value);
    }

    @Override
    String value1ToString() {
        return "a";
    }

    @Override
    String value2ToString(final String separator) {
        return "x" + separator + "y" + separator + "z";
    }
}
