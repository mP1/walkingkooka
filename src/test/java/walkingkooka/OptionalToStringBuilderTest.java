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

public final class OptionalToStringBuilderTest extends ScalarToStringBuilderTestCase<Optional<Integer>> {

    @Test
    public void testOptionalDefaultValueSkipped() {
        final ToStringBuilder b = this.builder();
        b.disable(ToStringBuilderOption.QUOTE);

        b.value(Optional.of(0));
        b.value("abc");
        this.buildAndCheck(b, "abc");
    }

    @Test
    public void testOptionalQuoted() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.QUOTE);
        b.value(Optional.of("1a"));

        this.buildAndCheck(b, "\"1a\"");
    }

    @Test
    public void testOptionalUnquoted() {
        final ToStringBuilder b = this.builder();
        b.disable(ToStringBuilderOption.QUOTE);
        b.value(Optional.of("1a"));

        this.buildAndCheck(b, "1a");
    }

    @Override
    void append(final ToStringBuilder builder, final Optional<Integer> value) {
        builder.append((Optional<Integer>) value);
    }

    @Override
    void value(final ToStringBuilder builder, final Optional<Integer> value) {
        builder.value((Optional<Integer>) value);
    }

    @Override
    Optional<Integer> defaultValue() {
        return Optional.empty();
    }

    @Override
    Optional<Integer> value1() {
        return Optional.of(11);
    }

    @Override
    Optional<Integer> value2() {
        return Optional.of(22);
    }

    @Override
    String defaultValueToString() {
        return Optional.empty().toString();
    }

    @Override
    String value1ToString() {
        return "11";
    }

    @Override
    String value2ToString() {
        return "22";
    }
}
