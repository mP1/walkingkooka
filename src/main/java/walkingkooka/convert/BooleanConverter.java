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
 *
 */

package walkingkooka.convert;

import java.util.Objects;

/**
 * A {@link Converter} that knows how to convert towards a boolean answer.
 */
final class BooleanConverter<S, T> extends FixedTargetTypeConverter<T> {

    static <S, T> BooleanConverter<S, T> with(final Class<S> sourceType, final S falseValue, final Class<T> targetType, final T trueAnswer, final T falseAnswer) {
        Objects.requireNonNull(sourceType, "sourceType");
        Objects.requireNonNull(falseValue, "falseValue");
        Objects.requireNonNull(targetType, "targetType");
        Objects.requireNonNull(trueAnswer, "trueAnswer");
        Objects.requireNonNull(falseAnswer, "falseAnswer");

        return new BooleanConverter(sourceType, falseValue, targetType, trueAnswer, falseAnswer);
    }

    private BooleanConverter(final Class<S> sourceType, final S falseValue, final Class<T> targetType, final T trueAnswer, final T falseAnswer) {
        super();
        this.sourceType = sourceType;
        this.falseValue = falseValue;
        this.targetType = targetType;
        this.trueAnswer = trueAnswer;
        this.falseAnswer = falseAnswer;
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type) {
        return this.sourceType.isInstance(value) &&
               this.targetType == type;
    }

    private final Class<S> sourceType;

    @Override
    T convert1(final Object value, final Class<T> type) {
        return this.falseValue.equals(value) ?
                this.falseAnswer :
                this.trueAnswer;
    }

    private final Object falseValue;
    private final T trueAnswer;
    private final T falseAnswer;

    @Override
    Class<T> targetType() {
        return this.targetType;
    }

    private final Class<T> targetType;

    @Override
    public String toString() {
        return this.sourceType.getSimpleName() + "->" + this.targetType.getSimpleName();
    }
}
