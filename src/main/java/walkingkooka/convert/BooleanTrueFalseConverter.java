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

package walkingkooka.convert;

import walkingkooka.Either;

import java.util.Objects;

/**
 * A {@link Converter} that knows how to convert towards a boolean answer.
 */
final class BooleanTrueFalseConverter<S, D> extends Converter2 {

    static <S, T> BooleanTrueFalseConverter<S, T> with(final Class<S> sourceType,
                                                       final S falseValue,
                                                       final Class<T> targetType,
                                                       final T trueAnswer,
                                                       final T falseAnswer) {
        Objects.requireNonNull(sourceType, "sourceType");
        Objects.requireNonNull(falseValue, "falseValue");
        Objects.requireNonNull(targetType, "targetType");
        Objects.requireNonNull(trueAnswer, "trueAnswer");
        Objects.requireNonNull(falseAnswer, "falseAnswer");

        return new BooleanTrueFalseConverter<>(sourceType,
                falseValue,
                targetType,
                trueAnswer,
                falseAnswer);
    }

    private BooleanTrueFalseConverter(final Class<S> sourceType,
                                      final S falseValue,
                                      final Class<D> targetType,
                                      final D trueAnswer,
                                      final D falseAnswer) {
        super();
        this.sourceType = sourceType;
        this.falseValue = falseValue;
        this.targetType = targetType;
        this.trueAnswer = trueAnswer;
        this.falseAnswer = falseAnswer;
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final ConverterContext context) {
        return this.sourceType.isInstance(value) &&
                this.targetType == type;
    }

    private final Class<S> sourceType;

    @Override
    <T> Either<T, String> convert0(final Object value,
                                   final Class<T> type,
                                   final ConverterContext context) {
        return Either.left(type.cast(this.falseValue.equals(value) ?
                this.falseAnswer :
                this.trueAnswer));
    }

    private final Object falseValue;
    private final D trueAnswer;
    private final D falseAnswer;

    private final Class<D> targetType;

    @Override
    public String toString() {
        return this.sourceType.getSimpleName() + "->" + this.targetType.getSimpleName();
    }
}
