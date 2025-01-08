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

import java.util.Arrays;

public abstract class ToStringBuilderAppenderScalarTestCase<A extends ToStringBuilderAppenderScalar<V>, V> extends ToStringBuilderAppenderTestCase2<A, V> {

    ToStringBuilderAppenderScalarTestCase() {
        super();
    }

    private final static int GLOBAL = 100;

    @Test
    public final void testFullThenAppend() {
        final ToStringBuilder b = this.builderFull();
        this.append(b, this.value1());
        this.buildAndCheck(b, this.full());
    }

    @Test
    public final void testFullThenValue() {
        final ToStringBuilder b = this.builderFull();
        this.value(b, this.value1());
        this.buildAndCheck(b, this.full());
    }

    private ToStringBuilder builderFull() {
        final ToStringBuilder b = this.builder();
        b.globalLength(GLOBAL);
        b.append(this.full());
        return b;
    }

    private String full() {
        final char[] c = new char[GLOBAL];
        Arrays.fill(c, '*');
        return new String(c);
    }

    final ToStringBuilder builder() {
        return ToStringBuilder.empty().disable(ToStringBuilderOption.ESCAPE)
            .disable(ToStringBuilderOption.HEX_BYTES)
            .disable(ToStringBuilderOption.HEX_WHOLE_NUMBERS)
            .disable(ToStringBuilderOption.INLINE_ELEMENTS)
            .disable(ToStringBuilderOption.QUOTE)
            .disable(ToStringBuilderOption.ONE_AND_ZERO_BOOLEAN_ARRAYS)
            .labelSeparator(LABEL_SEPARATOR)
            .valueSeparator(VALUE_SEPARATOR)
            .separator(SEPARATOR);
    }
}
