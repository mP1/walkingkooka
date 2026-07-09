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

final class ToStringBuilderAppenderVectorArrayShared2Float extends ToStringBuilderAppenderVectorArrayShared2<float[]> {

    static ToStringBuilderAppenderVectorArrayShared2Float with(final float[] value) {
        return new ToStringBuilderAppenderVectorArrayShared2Float(value);
    }

    private ToStringBuilderAppenderVectorArrayShared2Float(final float[] value) {
        super(value);
    }

    @Override
    int length() {
        return this.value.length;
    }

    @Override
    void prepareValue(final ToStringBuilder builder) {
    }

    @Override
    void append(int index, ToStringBuilder builder) {
        builder.buffer.append(this.value[index]);
        builder.mode = builder.mode.value();
    }
}
