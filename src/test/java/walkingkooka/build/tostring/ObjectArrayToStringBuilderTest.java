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
import walkingkooka.collect.list.Lists;

import java.util.Optional;
import java.util.stream.Collectors;

public class ObjectArrayToStringBuilderTest extends VectorToStringBuilderTestCase<Object[]> {

    @Test
    public void testValueIncludesDefault(){
        final ToStringBuilder b = this.builder();
        b.label(LABEL);
        b.value(new Object[]{false, (byte)0, (short)0, 0, 0L, 0.0f, 0.0, ""});

        this.buildAndCheck(b, "LABEL=false, 0, 0, 0, 0, 0.0, 0.0, ");
    }

    @Test
    public void testQuotes() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.QUOTE);
        b.value(new Object[]{"ABC", 'z'});

        this.buildAndCheck(b, "\"ABC\", \'z\'");
    }

    @Test
    public void testSurroundOptionalQuoted() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.QUOTE);

        b.surroundValues("((", "))");
        b.value(new Object[]{Optional.of("1a"), 2});

        this.buildAndCheck(b, "((\"1a\", 2))");
    }

    @Test
    public void testSurroundOptionalUnquoted() {
        final ToStringBuilder b = this.builder();
        b.disable(ToStringBuilderOption.QUOTE);

        b.surroundValues("((", "))");
        b.value(new Object[]{Optional.of("1a"), 2});

        this.buildAndCheck(b, "((1a, 2))");
    }

    @Override
    Object[] defaultValue() {
        return new Object[0];
    }

    @Override
    Object[] value1() {
        return new Object[]{1234};
    }

    @Override
    Object[] value2() {
        return new Object[]{true,(byte)1, (short)2,3, 4L, 5.0f, 6.0, "ABC"};
    }

    @Override
    void append(final ToStringBuilder builder, final Object[] value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder, final Object[] value) {
        builder.value(value);
    }

    @Override
    String value1ToString() {
        return "1234";
    }

    @Override
    String value2ToString(final String separator) {
        return Lists.<Object>of(true, 1, 2, 3, 4, 5.0, 6.0, "ABC").stream().map(e -> String.valueOf(e)).collect(Collectors.joining(separator));
    }
}
