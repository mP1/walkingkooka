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

import java.util.List;

public abstract class ListToStringBuilderTestCase<T> extends VectorToStringBuilderTestCase<T> {

    @Test
    public final void testValueIncludesDefault(){
        final ToStringBuilder b = this.builder();
        b.label(LABEL);
        b.value(this.toValue(Lists.of(false, (byte)0, (short)0, 0, 0L, 0.0f, 0.0, "")));

        this.buildAndCheck(b, "LABEL=false, 0, 0, 0, 0, 0.0, 0.0, ");
    }

    @Test
    public final void testQuotes() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.QUOTE);
        b.value(this.toValue(Lists.of("ABC", 'z')));

        this.buildAndCheck(b, "\"ABC\", \'z\'");
    }

    @Override
    final T defaultValue() {
        return this.toValue(Lists.empty());
    }

    @Override
    final T value1() {
        return this.toValue(Lists.of(1));
    }

    @Override
    final T value2() {
        return this.toValue(Lists.of(1, 22, "abc"));
    }

    abstract T toValue(final List<?> list);

    @Override
    final void append(final ToStringBuilder builder, final T value) {
        builder.append(value);
    }

    @Override
    final void value(final ToStringBuilder builder, final T value) {
        builder.value(value);
    }

    @Override
    final String value1ToString() {
        return "1";
    }

    @Override
    final String value2ToString(final String separator) {
        return "1" + separator + "22" + separator + "abc";
    }
}
