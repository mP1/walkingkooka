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

public final class BooleanToStringBuilderTest extends ScalarToStringBuilderTestCase<Boolean> {

    @Override
    void append(final ToStringBuilder builder, final Boolean value) {
        builder.append((boolean) value);
    }

    @Override
    void value(final ToStringBuilder builder, final Boolean value) {
        builder.value((boolean) value);
    }

    @Override
    Boolean defaultValue() {
        return false;
    }

    @Override
    Boolean value1() {
        return true;
    }

    @Override
    Boolean value2() {
        return true;
    }

    @Override
    String defaultValueToString() {
        return "false";
    }

    @Override
    String value1ToString() {
        return "true";
    }

    @Override
    String value2ToString() {
        return "true";
    }
}
