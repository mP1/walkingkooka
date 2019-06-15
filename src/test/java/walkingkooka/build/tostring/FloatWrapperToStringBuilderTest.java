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

package walkingkooka.build.tostring;

import org.junit.jupiter.api.Test;

public final class FloatWrapperToStringBuilderTest extends WrapperToStringBuilderTestCase<Float> {

    @Test
    public void testValueHexWholeNumber() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS);

        b.value(Float.valueOf(123.5f));

        this.buildAndCheck(b, "123.5");
    }

    @Override
    Float defaultValue() {
        return 0.0f;
    }

    @Override
    Float value1() {
        return 123.0f;
    }

    @Override
    Float value2() {
        return 456.0f;
    }

    @Override
    String defaultValueToString() {
        return "0.0";
    }

    @Override
    String value1ToString() {
        return "123.0";
    }

    @Override
    String value2ToString() {
        return "456.0";
    }
}
