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

import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * A {@link ConversionException} that also includes the source {@link Object value} and {@link Class target type} as properties.
 */
public class FailedConversionException extends ConversionException {

    private static final long serialVersionUID = 1;

    public FailedConversionException(final Object value,
                                     final Class<?> targetType) {
        super(buildMessage(value, targetType));

        this.value = value;
        this.targetType = targetType;
    }

    public FailedConversionException(final Object value,
                                     final Class<?> targetType,
                                     final Throwable cause) {
        super(buildMessage(value, targetType), cause);

        this.value = value;
        this.targetType = targetType;
    }

    private static String buildMessage(final Object value,
                                       final Class<?> targetType) {
        Objects.requireNonNull(targetType, "targetType");

        return "Failed to convert " + CharSequences.quoteIfChars(value) +
                (null == value ? "" : " (" + value.getClass().getName() + ")") +
                " to " + targetType.getName();
    }

    public Object value() {
        return this.value;
    }

    private final Object value;

    public Class<?> targetType() {
        return this.targetType;
    }

    private final Class<?> targetType;
}
