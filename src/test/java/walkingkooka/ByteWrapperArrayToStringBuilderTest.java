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

public class ByteWrapperArrayToStringBuilderTest extends VectorToStringBuilderTestCase<Byte[]> {

    @Test
    public void testValueIncludesDefault() {
        final ToStringBuilder b = this.builder();
        b.label(LABEL);
        b.value(new Byte[]{0, 1, 2});

        this.buildAndCheck(b, "LABEL=0, 1, 2");
    }

    @Test
    public void testValueSeparatorList() {
        final ToStringBuilder b = this.builder();
        b.label(LABEL);
        b.valueSeparator("$");
        b.value(Lists.of(1, 2, 3));

        this.buildAndCheck(b, "LABEL=1$2$3");
    }

    @Override
    Byte[] defaultValue() {
        return new Byte[0];
    }

    @Override
    Byte[] value1() {
        return new Byte[]{123};
    }

    @Override
    Byte[] value2() {
        return new Byte[]{1, 2, 33};
    }

    @Override
    void append(final ToStringBuilder builder, final Byte[] value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder, final Byte[] value) {
        builder.value(value);
    }

    @Override
    String value1ToString() {
        return "123";
    }

    @Override
    String value2ToString(final String separator) {
        return "1" + separator + "2" + separator + "33";
    }
}