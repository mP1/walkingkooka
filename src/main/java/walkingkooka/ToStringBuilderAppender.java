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

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * A template class that handles "appending" a value of any type honouring the options and encodings set on the parent {@link ToStringBuilderOption}.
 */
abstract class ToStringBuilderAppender<V> {

    /**
     * {@see ToStringBuilderAppenderVectorArraySharedBoolean}
     */
    static ToStringBuilderAppender<?> booleanArray(final boolean[] value) {
        return ToStringBuilderAppenderVectorArraySharedBoolean.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderScalarSharedBoolean}
     */
    static ToStringBuilderAppender<?> booleanValue(final Boolean value) {
        return ToStringBuilderAppenderScalarSharedBoolean.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderVectorArrayShared2Byte}
     */
    static ToStringBuilderAppender<?> byteArray(final byte[] value) {
        return ToStringBuilderAppenderVectorArrayShared2Byte.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderScalarSharedByte}
     */
    static ToStringBuilderAppender<?> byteValue(final Byte value) {
        return ToStringBuilderAppenderScalarSharedByte.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderVectorArraySharedChar}
     */
    static ToStringBuilderAppender<?> charArray(final char[] value) {
        return ToStringBuilderAppenderVectorArraySharedChar.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderScalarSharedChar}
     */
    static ToStringBuilderAppender<?> charValue(final Character value) {
        return ToStringBuilderAppenderScalarSharedChar.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderScalarSharedCharSequence}
     */
    static ToStringBuilderAppender<?> charSequence(final CharSequence value) {
        return ToStringBuilderAppenderScalarSharedCharSequence.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderScalarSharedDefault}
     */
    private static ToStringBuilderAppenderScalarSharedDefault defaultScalar(final Object value) {
        return ToStringBuilderAppenderScalarSharedDefault.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderVectorArrayShared2Double}
     */
    static ToStringBuilderAppender<?> doubleArray(final double[] value) {
        return ToStringBuilderAppenderVectorArrayShared2Double.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderScalarSharedDouble}
     */
    static ToStringBuilderAppender<?> doubleValue(final Double value) {
        return ToStringBuilderAppenderScalarSharedDouble.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderScalarSharedEntry}
     */
    private static ToStringBuilderAppender<?> entry(final Entry<?, ?> value) {
        return ToStringBuilderAppenderScalarSharedEntry.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderVectorArrayShared2Float}
     */
    static ToStringBuilderAppender<?> floatArray(final float[] value) {
        return ToStringBuilderAppenderVectorArrayShared2Float.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderScalarSharedDouble}
     */
    static ToStringBuilderAppender<?> floatValue(final Float value) {
        return ToStringBuilderAppenderScalarSharedDouble.with(null == value ? null : value.doubleValue());
    }

    /**
     * {@see ToStringBuilderAppenderVectorArrayShared2Int}
     */
    static ToStringBuilderAppender<?> intArray(final int[] value) {
        return ToStringBuilderAppenderVectorArrayShared2Int.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderScalarSharedInt}
     */
    static ToStringBuilderAppender<?> intValue(final Integer value) {
        return ToStringBuilderAppenderScalarSharedInt.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderVectorIterable}
     */
    private static ToStringBuilderAppender<?> iterable(final Iterable<?> value) {
        return ToStringBuilderAppenderVectorIterable.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderVectorArrayShared2Long}
     */
    static ToStringBuilderAppender<?> longArray(final long[] value) {
        return ToStringBuilderAppenderVectorArrayShared2Long.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderScalarSharedLong}
     */
    static ToStringBuilderAppender<?> longValue(final Long value) {
        return ToStringBuilderAppenderScalarSharedLong.with(value);
    }

    /**
     * Performs a series of instanceof checks to determine the matching {@link ToStringBuilderAppender}.
     */
    static ToStringBuilderAppender<?> object(final Object value) {
        ToStringBuilderAppender<?> appender;

        do {
            if (value instanceof UsesToStringBuilder) {
                appender = usesToStringBuilder((UsesToStringBuilder) value);
                break;
            }
            if (value instanceof Boolean) {
                appender = booleanValue((Boolean) value);
                break;
            }
            if (value instanceof Character) {
                appender = charValue((Character) value);
                break;
            }
            if (value instanceof CharSequence) {
                appender = charSequence((CharSequence) value);
                break;
            }
            if (value instanceof Number) {
                if (value instanceof Byte) {
                    appender = byteValue((Byte) value);
                    break;
                }
                if (value instanceof Double) {
                    appender = doubleValue((Double) value);
                    break;
                }
                if (value instanceof Float) {
                    appender = floatValue((Float) value);
                    break;
                }
                if (value instanceof Integer) {
                    appender = intValue((Integer) value);
                    break;
                }
                if (value instanceof Long) {
                    appender = longValue((Long) value);
                    break;
                }
                if (value instanceof Short) {
                    appender = shortValue((Short) value);
                    break;
                }
                appender = defaultScalar(value);
                break;
            }

            if (value instanceof Optional) {
                appender = optional(
                    Cast.to(value)
                );
                break;
            }

            if (value instanceof Map) {
                final Map<?, ?> map = Cast.to(value);
                appender = iterable(map.entrySet());
                break;
            }
            if (value instanceof Map.Entry) {
                appender = entry(Cast.to(value));
                break;
            }
            if (value instanceof Iterable) {
                appender = iterable(Cast.to(value));
                break;
            }
            if (value instanceof boolean[]) {
                appender = booleanArray((boolean[]) value);
                break;
            }
            if (value instanceof byte[]) {
                appender = byteArray((byte[]) value);
                break;
            }
            if (value instanceof char[]) {
                appender = charArray((char[]) value);
                break;
            }
            if (value instanceof double[]) {
                appender = doubleArray((double[]) value);
                break;
            }
            if (value instanceof float[]) {
                appender = floatArray((float[]) value);
                break;
            }
            if (value instanceof int[]) {
                appender = intArray((int[]) value);
                break;
            }
            if (value instanceof long[]) {
                appender = longArray((long[]) value);
                break;
            }
            if (value instanceof Object[]) {
                appender = objectArray((Object[]) value);
                break;
            }
            if (value instanceof short[]) {
                appender = shortArray((short[]) value);
                break;
            }

            if (value instanceof OptionalDouble) {
                appender = optionalDouble(
                    Cast.to(value)
                );
                break;
            }
            if (value instanceof OptionalInt) {
                appender = optionalInt(
                    Cast.to(value)
                );
                break;
            }
            if (value instanceof OptionalLong) {
                appender = optionalLong(
                    Cast.to(value)
                );
                break;
            }

            appender = defaultScalar(value);
        } while (false);

        return appender;
    }

    /**
     * Wraps the object array which must be non null, and creates a {@link #iterable(Iterable)}.
     */
    private static ToStringBuilderAppender<?> objectArray(final Object[] value) {
        return iterable(Arrays.asList(value));
    }

    /**
     * {@see ToStringBuilderAppenderVectorOptional}
     */
    static ToStringBuilderAppender<?> optional(final Optional<?> value) {
        return ToStringBuilderAppenderVectorOptional.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderVectorOptionalDouble}
     */
    static ToStringBuilderAppender<?> optionalDouble(final OptionalDouble value) {
        return ToStringBuilderAppenderVectorOptionalDouble.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderVectorOptionalInt}
     */
    static ToStringBuilderAppender<?> optionalInt(final OptionalInt value) {
        return ToStringBuilderAppenderVectorOptionalInt.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderVectorOptionalLong}
     */
    static ToStringBuilderAppender<?> optionalLong(final OptionalLong value) {
        return ToStringBuilderAppenderVectorOptionalLong.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderVectorArrayShared2Short}
     */
    static ToStringBuilderAppender<?> shortArray(final short[] value) {
        return ToStringBuilderAppenderVectorArrayShared2Short.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderScalarSharedShort}
     */
    static ToStringBuilderAppender<?> shortValue(final Short value) {
        return ToStringBuilderAppenderScalarSharedShort.with(value);
    }

    /**
     * {@see ToStringBuilderAppenderScalarUsesToStringBuilder}
     */
    static ToStringBuilderAppender<?> usesToStringBuilder(final UsesToStringBuilder value) {
        return ToStringBuilderAppenderScalarUsesToStringBuilder.with(value);
    }

    /**
     * Package private to limit sub classing.
     */
    ToStringBuilderAppender(final V value) {
        super();
        this.value = value;
    }

    final void append(final ToStringBuilder builder) {
        if (builder.bufferLeft() > 0) {
            this.append0(builder);
        }
        builder.valueFinished(); // clear labels, before and after
    }

    abstract void append0(final ToStringBuilder builder);

    final void appendLabelBeforeValueAfter(final ToStringBuilder builder) {
        final int beforeLength = builder.buffer.length();

        if (!this.shouldSkip(builder)) {
            if (false == builder.appendLabel(builder.label)) {

                final String after = this.before(builder);
                this.value(builder);
                this.after(after, builder);

                builder.trimBufferIfNecessary(beforeLength);
            }
        }
    }

    final void appendLabelBeforeValueAfterPostEmptyCheck(final ToStringBuilder builder) {
        final StringBuilder buffer = builder.buffer;
        final int beforeLength = buffer.length();

        if (!this.shouldSkip(builder)) {
            final ToStringBuilderMode mode = builder.mode;
            if (false == builder.appendLabel(builder.label)) {

                final String after = this.before(builder);

                final int lengthBeforeValue = buffer.length();
                this.value(builder);

                if (buffer.length() == lengthBeforeValue && builder.option(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)) {
                    buffer.setLength(beforeLength);
                    builder.mode = mode; // restore mode trying to reverse any changes because value is empty
                } else {
                    this.after(after, builder);

                    builder.trimBufferIfNecessary(beforeLength);
                }
            }
        }
    }

    /**
     * Returns true if the value should be skipped because skipping is enabled and the value is empty/default.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    abstract boolean shouldSkip(final ToStringBuilder builder);

    /**
     * Vectors or contains may surround values with a before/after values.
     */
    abstract String before(final ToStringBuilder builder);

    /**
     * Inserts the value which may involve escaping or other forms of encoding such as HEX if enabled.
     */
    abstract void value(final ToStringBuilder builder);

    abstract void after(final String after, ToStringBuilder builder);

    /**
     * The value.
     */
    final V value;

    @Override
    public final String toString() {
        return String.valueOf(this.value);
    }
}
