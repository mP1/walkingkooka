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

final class ToStringBuilderAppenderCharArrayVector extends ToStringBuilderAppenderArrayOrCharArrayVector<char[]> {

    static ToStringBuilderAppenderCharArrayVector with(final char[] value) {
        return new ToStringBuilderAppenderCharArrayVector(value);
    }

    private ToStringBuilderAppenderCharArrayVector(final char[] value) {
        super(value);
    }

    @Override
    boolean isEmpty() {
        final char[] value = this.value;
        return null == value || value.length == 0;
    }

    @Override
    void value(final ToStringBuilder builder) {
        final char[] value = this.value;

        builder.appendCharSequence(null != value ?
                new String(value) :
                null,
            '"');
    }
}
