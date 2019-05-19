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

public final class BooleanWrapperArrayToStringBuilderTest extends WrapperVectorToStringBuilderTestCase<Boolean[]> {

    // Doesnt honour the enabled setting.
    @Test
    public void testOneZero() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.ONE_AND_ZERO_BOOLEAN_ARRAYS);

        b.label(LABEL);
        b.value(new Boolean[]{true, true, false, false, true});

        this.buildAndCheck(b, "LABEL=true, true, false, false, true");
    }

    @Override
    Boolean[] defaultValue() {
        return new Boolean[0];
    }

    @Override
    Boolean[] value1() {
        return new Boolean[]{true};
    }

    @Override
    Boolean[] value2() {
        return new Boolean[]{true, false, false, true};
    }

    @Override
    void append(final ToStringBuilder builder, final Boolean[] value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder, final Boolean[] value) {
        builder.value(value);
    }

    @Override
    String value1ToString() {
        return String.valueOf(true);
    }

    @Override
    String value2ToString(final String separator) {
        return true + separator + false + separator + false + separator + true;
    }
}
