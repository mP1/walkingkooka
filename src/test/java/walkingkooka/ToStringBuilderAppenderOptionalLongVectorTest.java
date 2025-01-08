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

import java.util.Optional;
import java.util.OptionalLong;

public final class ToStringBuilderAppenderOptionalLongVectorTest extends ToStringBuilderAppenderVectorTestCase<ToStringBuilderAppenderOptionalLongVector, OptionalLong> {

    @Test
    public void testEmptyDisabledSkipIfDefaultValue() {
        final ToStringBuilder b = this.builder();
        b.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);
        b.label("Label123")
            .value(OptionalLong.empty());

        this.buildAndCheck(
            b,
            "Label123" + LABEL_SEPARATOR
        );
    }

    @Test
    public void testEmptyEnabledSkipIfDefaultValue() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);
        b.label("Label123")
            .value(OptionalLong.empty());

        this.buildAndCheck(
            b,
            ""
        );
    }

    @Test
    public void testNotEmptyEnabledSkipIfDefaultValue() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);
        b.label("Label123")
            .value(Optional.of(123));

        this.buildAndCheck(
            b,
            "Label123" + LABEL_SEPARATOR + "123"
        );
    }

    @Override
    OptionalLong defaultValue() {
        return OptionalLong.empty();
    }

    @Override
    String defaultValueToString(final OptionalLong value) {
        return "";
    }

    @Override
    OptionalLong value1() {
        return OptionalLong.of(111);
    }

    @Override
    OptionalLong value2() {
        return OptionalLong.of(222);
    }

    @Override
    void append(final ToStringBuilder builder,
                final OptionalLong value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder,
               final OptionalLong value) {
        builder.value(value);
    }

    @Override
    String value1ToString() {
        return "111";
    }

    @Override
    String value2ToString(final String separator) {
        return "222";
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ToStringBuilderAppenderOptionalLongVector> type() {
        return ToStringBuilderAppenderOptionalLongVector.class;
    }
}
