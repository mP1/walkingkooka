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
import walkingkooka.collect.map.Maps;

import java.util.Map;
import java.util.Map.Entry;

public final class ToStringBuilderAppenderEntryScalarTest extends ToStringBuilderAppenderScalarTestCase<ToStringBuilderAppenderEntryScalar, Entry<?, ?>> {

    @Test
    public void testValueHexWholeNumber() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.QUOTE);

        final String value = "value123";
        b.value(Map.entry("key1", value));

        this.buildAndCheck(b, "key1" + LABEL_SEPARATOR + '"' + value + '"');
    }

    @Override
    void append(final ToStringBuilder builder, final Entry<?, ?> value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder, final Entry<?, ?> value) {
        builder.value(value);
    }

    @Override
    Entry<?, ?> defaultValue() {
        return null;
    }

    @Override
    Entry<?, ?> value1() {
        return Maps.entry("key1", 11);
    }

    @Override
    Entry<?, ?> value2() {
        return Maps.entry("key2", 22);
    }

    @Override
    String defaultAppendToString(final Entry<?, ?> value) {
        return this.defaultValueToString(value);
    }

    @Override
    String defaultValueToString(final Entry<?, ?> value) {
        return "null";
    }

    @Override
    String value1ToString() {
        return "key1" + LABEL_SEPARATOR + 11;
    }

    @Override
    String value2ToString() {
        return "key2" + LABEL_SEPARATOR + 22;
    }

    // ClassTesting......................................................................................................

    @Override
    public Class<ToStringBuilderAppenderEntryScalar> type() {
        return ToStringBuilderAppenderEntryScalar.class;
    }
}
