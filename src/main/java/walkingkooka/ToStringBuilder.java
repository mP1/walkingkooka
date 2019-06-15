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

import walkingkooka.build.Builder;
import walkingkooka.text.CharSequences;

import java.util.EnumSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A builder that makes it easy to build nice consistent {@link Object#toString()}. Note that any
 * labels are only used for the next value that is added once. A second value will not have use the
 * first label but it must be set again. <br> The default {@link ToStringBuilderOption} are
 * <ul>
 * <li>{@link ToStringBuilderOption#QUOTE}</li>
 * <li>{@link ToStringBuilderOption#INLINE_ELEMENTS}</li>
 * <li>{@link ToStringBuilderOption#SKIP_IF_DEFAULT_VALUE}</li>
 * </ul>
 */
final public class ToStringBuilder implements Builder<String> {

    /**
     * Accepts a {@link UsesToStringBuilder} and creates a {@link ToStringBuilder} and then invokes
     * {@link UsesToStringBuilder#buildToString(ToStringBuilder)} and returns the final result.
     */
    public static String buildFrom(final UsesToStringBuilder uses) {
        Objects.requireNonNull(uses, "uses");

        final ToStringBuilder builder = new ToStringBuilder();
        uses.buildToString(builder);
        return builder.build();
    }

    /**
     * Creates an empty {@link ToStringBuilder}.
     */
    public static ToStringBuilder empty() {
        return new ToStringBuilder();
    }

    /**
     * The value that is added when a null value is encountered.
     */
    public final static String NULL = "null";

    /**
     * The default label value which also results in the {@link #labelSeparator(String)} being
     * skipped when a value is added.
     */
    public final static String DEFAULT_LABEL = null;

    /**
     * The default label separator is the equals sign. New or different values may be set using
     * {@link #labelSeparator(String)}.
     */
    public static final String LABEL_SEPARATOR = "=";

    /**
     * The default value separator which is a comma followed by a space. New or different values may
     * be set using {@link #valueSeparator(String)}.
     */
    public static final String DEFAULT_VALUE_SEPARATOR = ", ";

    /**
     * The default separator is a single space. New or different values may be set using {@link
     * #separator(String)}.
     */
    public static final String SEPARATOR = " ";

    /**
     * The default value which surrounds values charSequence an {@link Iterable iterable}, {@link Iterator},
     * {@link Map} and arrays except for char[] which is nothing. New or different values may be set
     * using {@link #surroundValues(String, String)}.
     */
    public static final String DEFAULT_BEFORE_VALUES = "";

    /**
     * The default value which surrounds values charSequence an {@link Iterable iterable}, {@link Iterator},
     * {@link Map} and arrays except for char[] which is nothing. New or different values may be set
     * using {@link #surroundValues(String, String)}.
     */
    public static final String DEFAULT_AFTER_VALUES = "";

    /**
     * The {@link String} added when processing true elements of a boolean[] when {@link
     * ToStringBuilderOption#ONE_AND_ZERO_BOOLEAN_ARRAYS} is false.
     */
    public static final String DEFAULT_TRUE_VALUE = Boolean.TRUE.toString();

    /**
     * The {@link String} added when processing false elements of a boolean[] when {@link
     * ToStringBuilderOption#ONE_AND_ZERO_BOOLEAN_ARRAYS} is false.
     */
    public static final String DEFAULT_FALSE_VALUE = Boolean.FALSE.toString();

    /**
     * The {@link char} added when processing true elements of a boolean[] when {@link
     * ToStringBuilderOption#ONE_AND_ZERO_BOOLEAN_ARRAYS} is true.
     */
    public static final char COMPACT_TRUE_VALUE = '1';

    /**
     * The {@link char} added when processing false elements of a boolean[] when {@link
     * ToStringBuilderOption#ONE_AND_ZERO_BOOLEAN_ARRAYS} is true.
     */
    public static final char COMPACT_FALSE_VALUE = '0';

    /**
     * The default global length.
     */
    public static final int GLOBAL_LENGTH = 1000;

    /**
     * The default value length.
     */
    public static final int VALUE_LENGTH = ToStringBuilder.GLOBAL_LENGTH - 100;

    /**
     * Private constructor use static factory
     */
    private ToStringBuilder() {
        super();

        this.before = ToStringBuilder.DEFAULT_BEFORE_VALUES;
        this.after = ToStringBuilder.DEFAULT_AFTER_VALUES;

        this.skipSeparatorIfNextValueIsWithoutLabel
                = ToStringBuilder.DEFAULT_SKIP_SEPARATOR_IF_NEXT_VALUE_IS_WITHOUT_LABEL;

        this.globalLength = ToStringBuilder.GLOBAL_LENGTH;
        this.maxGlobalLength = Integer.MAX_VALUE;
        this.valueLength = ToStringBuilder.VALUE_LENGTH;
        this.maxValueLength = Integer.MAX_VALUE;
        this.callerBufferLengthSnapshot = 0;

        this.options = EnumSet.noneOf(ToStringBuilderOption.class);
        this.buffer = new StringBuilder();
        this.parent = null; // NONE

        this.defaults();
    }

    /**
     * Restores this {@link ToStringBuilder} mutable state to defaults.
     */
    public ToStringBuilder defaults() {
        this.separator = ToStringBuilder.SEPARATOR;
        this.labelSeparator = ToStringBuilder.LABEL_SEPARATOR;
        this.valueSeparator = ToStringBuilder.DEFAULT_VALUE_SEPARATOR;

        final Set<ToStringBuilderOption> options = this.options;
        options.clear();
        options.add(ToStringBuilderOption.QUOTE);
        options.add(ToStringBuilderOption.INLINE_ELEMENTS);
        options.add(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);

        return this;
    }

    /**
     * Adds a {@link String label}. Note the label is not committed until a value is added. Setting
     * an empty label is ignored results in only the value separator followed by the value without
     * the label separator. Labels are only used for the next value even if it is skipped because it
     * is a default.
     */
    public ToStringBuilder label(final String label) {
        Objects.requireNonNull(label, "label");

        this.label = label;
        return this;
    }

    /**
     * When a value is added this will be added before the value.
     */
    private String label;

    /**
     * Flag that allows the ability to skip including the {@link #valueSeparator} if {@link #label}
     * is null but an append method was called.
     */
    private boolean skipSeparatorIfNextValueIsWithoutLabel;

    /**
     * This method should be called after adding a value and possibly its label to reset some flags
     * etc.
     */
    private void afterValue() {
        this.label = ToStringBuilder.DEFAULT_LABEL;
        this.skipSeparatorIfNextValueIsWithoutLabel
                = ToStringBuilder.DEFAULT_SKIP_SEPARATOR_IF_NEXT_VALUE_IS_WITHOUT_LABEL;
    }

    /**
     * The default {@link #skipSeparatorIfNextValueIsWithoutLabel}.
     */
    private final static boolean DEFAULT_SKIP_SEPARATOR_IF_NEXT_VALUE_IS_WITHOUT_LABEL = false;

    // value methods that include logic to maybe insert the label, separators and more.

    /**
     * Adds a label and separators of required and if necessary. Note if the buffer is full true is
     * returned and the label removed.
     */
    private boolean maybeLabel() {
        final StringBuilder buffer = this.buffer;
        final int length = buffer.length();
        final String label = this.label;

        if (this.valuesAdded > 0) {
            if ((null != label) || (false == this.skipSeparatorIfNextValueIsWithoutLabel)) {
                buffer.append(this.separator);
            }
        }

        if (null != label) {
            buffer.append(label);
            // only add $labelSeparator if label isnt empty
            if (!label.isEmpty()) {
                buffer.append(this.labelSeparator);
            }
            this.label = null;
        }

        // if the buffer has become full remove label, separators etc and return true.
        final boolean full = false;
        if (buffer.length() >= this.globalLength) {
            buffer.setLength(length);
        }

        return full;
    }

    /**
     * Adds a boolean value.
     */
    public ToStringBuilder value(final boolean value) {
        if (value || this.skipIfNotDefaultValue()) {
            if (false == this.maybeLabel()) {
                final int before = this.buffer.length();
                this.appendBoolean(value);
                this.trimBufferIfNecessary(before);
                this.valuesAdded++;
            }
        }
        return this;
    }

    /**
     * Appends the boolean using the {@link #DEFAULT_BEFORE_VALUES} and {@link #DEFAULT_FALSE_VALUE}
     * of appropriate.
     */
    private void appendBoolean(final boolean value) {
        this.buffer.append(value ?
                ToStringBuilder.DEFAULT_TRUE_VALUE :
                ToStringBuilder.DEFAULT_FALSE_VALUE);
    }

    /**
     * Adds a boolean array.
     */
    public ToStringBuilder value(final boolean[] array) {
        if (this.inlineElements()) {
            if (((null != array) && (array.length > 0)) || this.skipIfNotDefaultValue()) {
                if (false == this.maybeLabel()) {
                    final int before = this.buffer.length();
                    this.appendBooleanArray(array);
                    this.trimBufferIfNecessary(before);
                    this.valuesAdded++;
                }
            }
        }
        this.afterValue();
        return this;
    }

    private void appendBooleanArray(final boolean[] array) {
        final StringBuilder buffer = this.buffer;
        if (null == array) {
            buffer.append(ToStringBuilder.NULL);
        } else {
            if (this.options.contains(ToStringBuilderOption.ONE_AND_ZERO_BOOLEAN_ARRAYS)) {
                this.appendBooleanArrayCompact(array, buffer);
            } else {
                this.appendBooleanArrayUsingDefaultLiterals(array, buffer);
            }
            this.valuesAdded++;
        }
    }

    /**
     * Adds {@link #DEFAULT_TRUE_VALUE} for true and {@link #DEFAULT_FALSE_VALUE} for false elements
     * without a separator.
     */
    private void appendBooleanArrayCompact(final boolean[] array, final StringBuilder buffer) {
        for (final boolean value : array) {
            buffer.append(value ?
                    ToStringBuilder.COMPACT_TRUE_VALUE :
                    ToStringBuilder.COMPACT_FALSE_VALUE);
        }
    }

    /**
     * Adds {@link #DEFAULT_TRUE_VALUE} for true and {@link #DEFAULT_FALSE_VALUE} for false elements
     * separated by {@link #valueSeparator}.
     */
    private void appendBooleanArrayUsingDefaultLiterals(final boolean[] array,
                                                        final StringBuilder buffer) {
        buffer.append(this.before);

        final String valueSeparator = this.valueSeparator;
        String separator = "";

        for (final boolean value : array) {
            buffer.append(separator)
                    .append(value ?
                            ToStringBuilder.DEFAULT_TRUE_VALUE :
                            ToStringBuilder.DEFAULT_FALSE_VALUE);
            separator = valueSeparator;
        }
        buffer.append(this.after);
    }

    /**
     * Adds a byte note it will be in hex-decimal form if {@link ToStringBuilderOption#HEX_BYTES} is
     * enabled.
     */
    public ToStringBuilder value(final byte value) {
        if ((0 != value) || this.skipIfNotDefaultValue()) {
            if (false == this.maybeLabel()) {
                final int before = this.buffer.length();
                this.appendByte(value);
                this.trimBufferIfNecessary(before);
                this.valuesAdded++;
            }
        }
        this.afterValue();
        return this;
    }

    /**
     * Adds a byte array respecting any set {@link ToStringBuilderOption options}
     */
    public ToStringBuilder value(final byte[] array) {
        if (this.inlineElements()) {
            if (((null != array) && (array.length > 0)) || this.skipIfNotDefaultValue()) {
                if (false == this.maybeLabel()) {
                    final int before = this.buffer.length();
                    this.appendByteArray(array);
                    this.trimBufferIfNecessary(before);
                }
            }
        }
        this.afterValue();
        return this;
    }

    private void appendByteArray(final byte[] array) {
        final StringBuilder buffer = this.buffer;
        if (null == array) {
            buffer.append(ToStringBuilder.NULL);
        } else {
            buffer.append(this.before);

            final ToStringBuilderOption encoder = this.byteValueEncoder();
            final String valueSeparator = this.valueSeparator;

            String separator = "";
            for (final byte value : array) {
                buffer.append(separator);
                encoder.add(value, buffer);
                separator = valueSeparator;
            }

            buffer.append(this.after);
        }
        this.valuesAdded++;
    }

    private void appendByte(final byte value) {
        this.byteValueEncoder().add(value, this.buffer);
    }

    /**
     * Returns a {@link ToStringBuilderOption} which can be used to encode individual byte values.
     */
    private ToStringBuilderOption byteValueEncoder() {
        final Set<ToStringBuilderOption> options = this.options;
        return options.contains(ToStringBuilderOption.HEX_BYTES) ? ToStringBuilderOption.HEX_BYTES :
                //
                options.contains(ToStringBuilderOption.HEX_WHOLE_NUMBERS) ?
                        ToStringBuilderOption.HEX_WHOLE_NUMBERS :
                        //
                        ToStringBuilderOption.DEFAULT;
    }

    /**
     * Adds a character.
     */
    public ToStringBuilder value(final char value) {
        if (('\0' != value) || this.skipIfNotDefaultValue()) {
            if (false == this.maybeLabel()) {
                final int before = this.buffer.length();
                this.appendChar(value);
                this.trimBufferIfNecessary(before);
                this.valuesAdded++;
            }
        }
        this.afterValue();
        return this;
    }

    private void appendChar(final char value) {
        CharSequence chars = Character.toString(value);

        if (this.options.contains(ToStringBuilderOption.ESCAPE)) {
            chars = CharSequences.escape(chars);
        }
        if (this.options.contains(ToStringBuilderOption.QUOTE)) {
            chars = "\'" + chars + '\'';
        }

        this.buffer.append(chars);

        this.valuesAdded++;
    }

    /**
     * Adds a char array.
     */
    public ToStringBuilder value(final char[] array) {
        if (this.inlineElements()) {
            if (((null != array) && (array.length > 0)) || this.skipIfNotDefaultValue()) {
                final StringBuilder buffer = this.buffer;
                if (false == this.maybeLabel()) {
                    final int before = buffer.length();
                    if (null == array) {
                        // special case so NULL is not escaped or quoted ever by appendCharSequence
                        buffer.append(ToStringBuilder.NULL);
                    } else {
                        this.appendCharArray(array);
                    }
                    this.trimBufferIfNecessary(before);
                    this.valuesAdded++;
                }
            }
        }
        this.afterValue();
        return this;
    }

    private void appendCharArray(final char[] array) {
        this.buffer.append(this.before);
        this.appendCharSequence(null == array ? null : new String(array));
        this.buffer.append(this.after);
    }

    /**
     * Adds a double value
     */
    public ToStringBuilder value(final double value) {
        if ((0 != value) || this.skipIfNotDefaultValue()) {
            if (false == this.maybeLabel()) {
                final int before = this.buffer.length();
                this.appendDouble(value);
                this.trimBufferIfNecessary(before);
                this.valuesAdded++;
            }
        }
        this.afterValue();
        return this;
    }

    private void appendDouble(final double value) {
        this.buffer.append(value);
    }

    /**
     * Adds a double array.
     */
    public ToStringBuilder value(final double[] array) {
        if (this.inlineElements()) {
            if (((null != array) && (array.length > 0)) || this.skipIfNotDefaultValue()) {
                if (false == this.maybeLabel()) {
                    final int before = this.buffer.length();
                    this.appendDoubleArray(array);
                    this.trimBufferIfNecessary(before);
                    this.valuesAdded++;
                }
            }
        }
        this.afterValue();
        return this;
    }

    private void appendDoubleArray(final double[] array) {
        final StringBuilder buffer = this.buffer;
        if (null == array) {
            buffer.append(ToStringBuilder.NULL);
        } else {
            buffer.append(this.before);

            final String valueSeparator = this.valueSeparator;
            String separator = "";
            for (final double value : array) {
                buffer.append(separator).append(value);
                separator = valueSeparator;
            }

            buffer.append(this.after);
        }
        this.valuesAdded++;
    }

    /**
     * Adds a float value
     */
    public ToStringBuilder value(final float value) {
        if ((0 != value) || this.skipIfNotDefaultValue()) {
            if (false == this.maybeLabel()) {
                final int before = this.buffer.length();
                this.appendFloat(value);
                this.trimBufferIfNecessary(before);
                this.valuesAdded++;
            }
        }
        this.afterValue();
        return this;
    }

    private void appendFloat(final float value) {
        this.buffer.append(value);
    }

    /**
     * Adds a float array.
     */
    public ToStringBuilder value(final float[] array) {
        if (this.inlineElements()) {
            if (((null != array) && (array.length > 0)) || this.skipIfNotDefaultValue()) {
                if (false == this.maybeLabel()) {
                    final int before = this.buffer.length();
                    this.appendFloatArray(array);
                    this.trimBufferIfNecessary(before);
                    this.valuesAdded++;
                }
            }
        }
        this.afterValue();
        return this;
    }

    private void appendFloatArray(final float[] array) {
        final StringBuilder buffer = this.buffer;
        if (null == array) {
            buffer.append(ToStringBuilder.NULL);
        } else {
            buffer.append(this.before);

            final String valueSeparator = this.valueSeparator;
            String separator = "";
            for (final float value : array) {
                buffer.append(separator).append(value);
                separator = valueSeparator;
            }

            buffer.append(this.after);
            this.valuesAdded++;
        }
    }

    // int

    /**
     * Adds an int value.
     */
    public ToStringBuilder value(final int value) {
        if ((0 != value) || this.skipIfNotDefaultValue()) {
            final int before = this.buffer.length();
            if (false == this.maybeLabel()) {
                this.appendInt(value);
            }
            this.trimBufferIfNecessary(before);
            this.valuesAdded++;
        }
        this.afterValue();
        return this;
    }

    private void appendInt(final int value) {
        this.byteShortIntLongEncoder().add(value, this.buffer);
    }

    /**
     * Adds a int array.
     */
    public ToStringBuilder value(final int[] array) {
        if (this.inlineElements()) {
            if (((null != array) && (array.length > 0)) || this.skipIfNotDefaultValue()) {
                final int before = this.buffer.length();
                if (false == this.maybeLabel()) {
                    this.appendIntArray(array);
                    this.trimBufferIfNecessary(before);
                    this.valuesAdded++;
                }
            }
        }
        this.afterValue();
        return this;
    }

    private void appendIntArray(final int[] array) {
        final StringBuilder buffer = this.buffer;
        if (null == array) {
            buffer.append(ToStringBuilder.NULL);
        } else {
            buffer.append(this.before);

            final ToStringBuilderOption encoder = this.byteShortIntLongEncoder();
            final String valueSeparator = this.valueSeparator;

            String separator = "";
            for (final int value : array) {
                buffer.append(separator);
                encoder.add(value, buffer);
                separator = valueSeparator;
            }

            buffer.append(this.after);
            this.valuesAdded++;
        }
    }

    // long

    /**
     * Adds a long value
     */
    public ToStringBuilder value(final long value) {
        if ((0 != value) || this.skipIfNotDefaultValue()) {
            if (false == this.maybeLabel()) {
                final int before = this.buffer.length();
                this.appendLong(value);
                this.trimBufferIfNecessary(before);
                this.valuesAdded++;
            }
        }
        this.afterValue();
        return this;
    }

    private void appendLong(final long value) {
        this.byteShortIntLongEncoder().add(value, this.buffer);
    }

    /**
     * Adds a long array.
     */
    public ToStringBuilder value(final long[] array) {
        if (this.inlineElements()) {
            if (((null != array) && (array.length > 0)) || this.skipIfNotDefaultValue()) {
                if (false == this.maybeLabel()) {
                    final int before = this.buffer.length();
                    this.appendLongArray(array);
                    this.trimBufferIfNecessary(before);
                    this.valuesAdded++;
                }
            }
        }
        this.afterValue();
        return this;
    }

    private void appendLongArray(final long[] array) {
        final StringBuilder buffer = this.buffer;
        if (null == array) {
            buffer.append(ToStringBuilder.NULL);
        } else {
            buffer.append(this.before);

            final ToStringBuilderOption encoder = this.byteShortIntLongEncoder();
            final String valueSeparator = this.valueSeparator;

            String separator = "";
            for (final long value : array) {
                buffer.append(separator);
                encoder.add(value, buffer);
                separator = valueSeparator;
            }

            buffer.append(this.after);
            this.valuesAdded++;
        }
    }

    /**
     * Adds the given {@link CharSequence} which may involve escaping and quoting if those {@link
     * ToStringBuilderOption options} are enabled.
     */
    public ToStringBuilder value(final CharSequence chars) {
        if (((null != chars) && (chars.length() > 0)) || this.skipIfNotDefaultValue()) {
            if (false == this.maybeLabel()) {
                final int before = this.buffer.length();
                this.appendCharSequence(chars);
                this.trimBufferIfNecessary(before);
                this.valuesAdded++;
            }
        }
        this.afterValue();
        return this;
    }

    /**
     * Adds an optional, with logic to skip optionals if {@link ToStringBuilderOption#SKIP_IF_DEFAULT_VALUE} is
     * true.
     */
    public ToStringBuilder value(final Optional<?> value) {
        if ((value.isPresent())) {
            this.value(value.get());
        } else {
            if (this.skipIfNotDefaultValue()) {
                this.value(value.toString());
            }
        }
        return this;
    }

    private void appendOptional(final Optional<?> value) {
        value.ifPresent((v) -> this.appendValue(v));
    }

    /**
     * Adds a new value.
     */
    public ToStringBuilder value(final Object value) {
        for (; ; ) {
            if (this.bufferLeft() <= 0) {
                break;
            }

            final Set<ToStringBuilderOption> options = this.options;
            if ((null == value) && options.contains(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)) {
                this.afterValue();
                break;
            }
            if (value instanceof UsesToStringBuilder) {
                this.maybeUsesToStringBuilder((UsesToStringBuilder) value);
                break;
            }
            if (value instanceof CharSequence) {
                this.value((CharSequence) value);
                break;
            }
            if (value instanceof Boolean) {
                this.value(((Boolean) value).booleanValue());
                break;
            }
            if (value instanceof Character) {
                this.value(((Character) value).charValue());
                break;
            }
            if (value instanceof Number) {
                this.valueNumber((Number) value);
                break;
                // let other Number sub classes use the default. else skips boring array checks.
            }
            if (value instanceof Optional) {
                this.value((Optional<?>) value);
                break;
            }
            if (value instanceof Map) {
                this.valueMap((Map<?, ?>) value);
                break;
            }
            if (value instanceof Iterable) {
                if (this.inlineElements()) {
                    this.valueIterable((Iterable<?>) value);
                }
                break;
            }
            if (value instanceof Iterator) {
                this.valueIterator((Iterator<?>) value);
                break;
            }
            if (value instanceof Enumeration) {
                this.valueEnumeration((Enumeration<?>) value);
                break;
            }
            if (value instanceof boolean[]) {
                this.value((boolean[]) value);
                break;
            }
            if (value instanceof byte[]) {
                this.value((byte[]) value);
                break;
            }
            if (value instanceof char[]) {
                this.value((char[]) value);
                break;
            }
            if (value instanceof double[]) {
                this.value((double[]) value);
                break;
            }
            if (value instanceof float[]) {
                this.value((float[]) value);
                break;
            }
            if (value instanceof int[]) {
                this.value((int[]) value);
                break;
            }
            if (value instanceof long[]) {
                this.value((long[]) value);
                break;
            }
            if (value instanceof Object[]) {
                this.value((Object[]) value);
                break;
            }
            if (value instanceof short[]) {
                this.value((short[]) value);
                break;
            }
            this.valueObject(value);
            break;
        }

        return this;
    }

    private void valueNumber(final Number value) {
        for (; ; ) {
            if (value instanceof Byte) {
                this.value(value.byteValue());
                break;
            }
            if (value instanceof Double) {
                this.value(value.doubleValue());
                break;
            }
            if (value instanceof Float) {
                this.value(value.floatValue());
                break;
            }
            if (value instanceof Integer) {
                this.value(value.intValue());
                break;
            }
            if (value instanceof Long) {
                this.value(value.longValue());
                break;
            }
            if (value instanceof Short) {
                this.value(value.shortValue());
                break;
            }
            this.valueObject(value);
            break;
        }
    }

    /**
     * Adds any present label and then calls {@link #appendUsesToStringBuilder(UsesToStringBuilder)}
     */
    private void maybeUsesToStringBuilder(final UsesToStringBuilder value) {
        final StringBuilder buffer = this.buffer;
        final int beforeLabel = buffer.length();

        if (false == this.maybeLabel()) {
            final int before = buffer.length();
            this.appendUsesToStringBuilder(value);

            final int after = buffer.length();
            if (after > before) {
                this.trimBufferIfNecessary(before);
                this.valuesAdded++;
            } else {
                // remove label if was empty
                if (false == this.skipIfNotDefaultValue()) {
                    buffer.setLength(beforeLabel);
                }
            }
        }
        this.afterValue();
    }

    /**
     * Saves the state, calls the {@link UsesToStringBuilder#buildToString(ToStringBuilder)} and
     * then restores the backed up state.
     */
    private void appendUsesToStringBuilder(final UsesToStringBuilder value) {
        // save
        final String separator = this.separator;
        final String labelSeparator = this.labelSeparator;
        final String valueSeparator = this.valueSeparator;
        final String before = this.before;
        final String after = this.after;
        final EnumSet<ToStringBuilderOption> options = this.options;
        this.options = EnumSet.copyOf(options);

        // increase depth
        final int depth = this.depth;
        this.depth = 1 + depth;

        final int globalLength = this.globalLength;
        final int maxGlobalLength = this.maxGlobalLength;

        final int valueLength = this.valueLength;
        final int maxValueLength = this.maxValueLength;

        final int callerBufferLengthSnapshot = this.buffer.length();
        this.callerBufferLengthSnapshot = callerBufferLengthSnapshot;

        // share
        this.skipSeparatorIfNextValueIsWithoutLabel
                = true; // label cleared and do not want to insert separator again.
        this.maxValueLength = valueLength;

        final int max = callerBufferLengthSnapshot + valueLength;
        this.maxGlobalLength = max;
        this.globalLength = max;

        final int bufferLengthBefore = this.buffer.length();

        // this is what makes #parent() work
        final Object parent = this.parent;
        final Object self = this.self;
        this.parent = self;
        this.self = value;

        try {
            value.buildToString(this);
        } finally {
            // restore after finishing/failing
            this.separator = separator;
            this.labelSeparator = labelSeparator;
            this.valueSeparator = valueSeparator;
            this.before = before;
            this.after = after;

            this.globalLength = globalLength;
            this.maxGlobalLength = maxGlobalLength;

            this.valueLength = valueLength;
            this.maxValueLength = maxValueLength;

            this.callerBufferLengthSnapshot = callerBufferLengthSnapshot;

            this.options = options;
            this.depth = depth;

            // assume if the buffer increased then "values were added"...
            if (bufferLengthBefore != this.buffer.length()) {
                this.valuesAdded++;
            }

            // restore parent and self.
            this.parent = parent;
            this.self = self;
        }
    }

    /**
     * Performs a check if the given {@link Object} assuming that a implements {@link
     * UsesToStringBuilder} has already occurred.
     */
    private void valueObject(final Object value) {
        final String string = String.valueOf(value);
        if ((string.length() > 0) || this.skipIfNotDefaultValue()) {
            if (false == this.maybeLabel()) {
                final StringBuilder buffer = this.buffer;
                final int before = buffer.length();
                buffer.append(string);
                this.trimBufferIfNecessary(before);
                this.valuesAdded++;
            }
        }
        this.afterValue();
    }

    /**
     * Adds a {@link Object} array.
     */
    public ToStringBuilder value(final Object[] array) {
        if (this.inlineElements()) {
            if (((null != array) && (array.length > 0)) || this.skipIfNotDefaultValue()) {
                if (false == this.maybeLabel()) {
                    final int before = this.buffer.length();
                    this.appendArray(array);
                    this.trimBufferIfNecessary(before);
                    this.valuesAdded++;
                }
            }
        }
        this.afterValue();
        return this;
    }

    /**
     * Unconditionally appends the given array, adding {@link #NULL} if it is null.
     */
    private void appendArray(final Object[] array) {
        final StringBuilder buffer = this.buffer;
        if (null == array) {
            buffer.append(ToStringBuilder.NULL);
        } else {
            buffer.append(this.before);

            final String valueSeparator = this.valueSeparator;
            String separator = "";
            for (final Object value : array) {
                buffer.append(separator);
                this.appendValue(value);
                separator = valueSeparator;
            }

            buffer.append(this.after);
        }
    }

    /**
     * Unconditionally appends the given {@link Object}, including logic to dispatch to the
     * appropriate appendXXX depending on the type of the value.
     */
    private void appendValue(final Object value) {
        for (; ; ) {
            if (this.bufferLeft() <= 0) {
                break;
            }

            if (value instanceof CharSequence) {
                this.appendCharSequence((CharSequence) value);
                break;
            }
            if (value instanceof Character) {
                this.appendChar((Character) value);
                break;
            }
            if (value instanceof Number) {
                if (value instanceof Byte) {
                    this.appendByte((Byte) value);
                    break;
                }
                if (value instanceof Double) {
                    this.appendDouble((Double) value);
                    break;
                }
                if (value instanceof Float) {
                    this.appendFloat((Float) value);
                    break;
                }
                if (value instanceof Long) {
                    this.appendLong((Long) value);
                    break;
                }
                if (value instanceof Short) {
                    this.appendShort((Short) value);
                    break;
                }
            }
            if (value instanceof boolean[]) {
                this.appendBooleanArray((boolean[]) value);
                break;
            }
            if (value instanceof byte[]) {
                this.appendByteArray((byte[]) value);
                break;
            }
            if (value instanceof char[]) {
                this.appendCharArray((char[]) value);
                break;
            }
            if (value instanceof double[]) {
                this.appendDoubleArray((double[]) value);
                break;
            }
            if (value instanceof float[]) {
                this.appendFloatArray((float[]) value);
                break;
            }
            if (value instanceof long[]) {
                this.appendLongArray((long[]) value);
                break;
            }
            if (value instanceof Object[]) {
                this.appendArray((Object[]) value);
                break;
            }
            if (value instanceof Optional) {
                this.appendOptional(Cast.to(value));
                break;
            }
            if (value instanceof short[]) {
                this.appendShortArray((short[]) value);
                break;
            }
            if (value instanceof UsesToStringBuilder) {
                this.appendUsesToStringBuilder((UsesToStringBuilder) value);
                break;
            }
            if (value instanceof Iterable) {
                this.appendIterable((Iterable<?>) value);
                break;
            }
            if (value instanceof Iterator) {
                this.appendIterator((Iterator<?>) value);
                break;
            }
            if (value instanceof Map) {
                this.appendMap((Map<?, ?>) value);
                break;
            }
            this.buffer.append(value);
            this.valuesAdded++;
            break;
        }
    }

    /**
     * Unconditional adds a {@link String}. Note the {@link String} may be quoted or escaped if
     * those options are enabled.
     */
    private void appendCharSequence(CharSequence chars) {
        final EnumSet<ToStringBuilderOption> options = this.options;
        if (options.contains(ToStringBuilderOption.ESCAPE)) {
            chars = CharSequences.escape(chars);
        }
        if (options.contains(ToStringBuilderOption.QUOTE)) {
            chars = CharSequences.quote(chars);
        }

        this.buffer.append(chars);
        this.valuesAdded++;
    }

    /**
     * Simply calls {@link #valueIterator(Iterator)} and lets it handle empty cases.
     */
    private void valueIterable(final Iterable<?> iterable) {
        this.valueIterator(iterable.iterator());
    }

    /**
     * Only includes all elements of the {@link Iterator} and handles {@link
     * ToStringBuilderOption#SKIP_IF_DEFAULT_VALUE}.
     */
    private void valueIterator(final Iterator<?> iterator) {
        if (this.inlineElements()) {
            if (this.skipIfNotDefaultValue() || iterator.hasNext()) {
                if (false == this.maybeLabel()) {
                    final StringBuilder buffer = this.buffer;
                    final int before = buffer.length();
                    buffer.append(this.before);
                    this.appendIterator(iterator);
                    buffer.append(this.after);
                    this.trimBufferIfNecessary(before);
                }
            }
        }
        this.afterValue();
    }

    /**
     * Accepts an {@link Iterable} which is assumed not to be empty.
     */
    private void appendIterable(final Iterable<?> iterable) {
        this.appendIterator(iterable.iterator());
    }

    /**
     * Unconditionally adds all the elements of the given {@link Iterator} separating each value
     * using the {@link #valueSeparator}.
     */
    private void appendIterator(final Iterator<?> iterator) {
        final StringBuilder buffer = this.buffer;

        final String valueSeparator = this.valueSeparator;
        String separator = "";

        while (iterator.hasNext()) {
            buffer.append(separator);
            this.appendValue(iterator.next());
            separator = valueSeparator;
        }
    }

    // Enumeration

    /**
     * Only includes all elements of the {@link Enumeration} and handles {@link
     * ToStringBuilderOption#SKIP_IF_DEFAULT_VALUE}.
     */
    private void valueEnumeration(final Enumeration<?> enumeration) {
        if (this.inlineElements()) {
            if (this.skipIfNotDefaultValue() || enumeration.hasMoreElements()) {
                if (false == this.maybeLabel()) {
                    final StringBuilder buffer = this.buffer;
                    final int before = buffer.length();
                    buffer.append(this.before);
                    this.appendEnumeration(enumeration);
                    buffer.append(this.after);
                    this.trimBufferIfNecessary(before);
                }
            }
        }
        this.afterValue();
    }

    /**
     * Unconditionally adds all the elements of the given {@link Enumeration} separating each value
     * using the {@link #valueSeparator}.
     */
    private void appendEnumeration(final Enumeration<?> enumeration) {
        final StringBuilder buffer = this.buffer;

        final String valueSeparator = this.valueSeparator;
        String separator = "";

        while (enumeration.hasMoreElements()) {
            buffer.append(separator);
            this.appendValue(enumeration.nextElement());
            separator = valueSeparator;
        }
    }

    /**
     * Includes logic to handle empty maps if {@link ToStringBuilderOption#SKIP_IF_DEFAULT_VALUE} is
     * true.
     */
    private void valueMap(final Map<?, ?> map) {
        if (this.inlineElements()) {
            if ((false == map.isEmpty()) || this.skipIfNotDefaultValue()) {
                if (false == this.maybeLabel()) {
                    final int before = this.buffer.length();
                    this.appendMap(map);
                    this.trimBufferIfNecessary(before);
                }
            }
        }
        this.afterValue();
    }

    /**
     * Unconditionally adds all the entries charSequence the map using the {@link #labelSeparator} and
     * {@link #valueSeparator}.
     */
    private void appendMap(final Map<?, ?> map) {
        final StringBuilder buffer = this.buffer;
        buffer.append(this.before);

        final String labelSeparator = this.labelSeparator;
        final String valueSeparator = this.valueSeparator;
        String separator = "";

        for (final Entry<?, ?> element : map.entrySet()) {
            buffer.append(separator);

            buffer.append(element.getKey());
            buffer.append(labelSeparator);
            this.appendValue(element.getValue());
            separator = valueSeparator;
        }

        buffer.append(this.after);
    }

    /**
     * Adds a short value skipping 0 values if {@link ToStringBuilderOption#SKIP_IF_DEFAULT_VALUE}
     * is true.
     */
    public ToStringBuilder value(final short value) {
        if ((0 != value) || this.skipIfNotDefaultValue()) {
            final int before = this.buffer.length();
            if (false == this.maybeLabel()) {
                this.appendShort(value);
            }
            this.trimBufferIfNecessary(before);
            this.valuesAdded++;
        }
        this.afterValue();
        return this;
    }

    private void appendShort(final short value) {
        this.byteShortIntLongEncoder().add(value, this.buffer);
    }

    /**
     * Adds a short array with a special test
     */
    public ToStringBuilder value(final short[] array) {
        if (this.inlineElements()) {
            if (((null != array) && (array.length > 0)) || this.skipIfNotDefaultValue()) {
                if (false == this.maybeLabel()) {
                    final int before = this.buffer.length();
                    this.appendShortArray(array);
                    this.trimBufferIfNecessary(before);
                    this.valuesAdded++;
                }
            }
        }
        this.afterValue();
        return this;
    }

    private void appendShortArray(final short[] array) {
        final StringBuilder buffer = this.buffer;

        if (null == array) {
            buffer.append(ToStringBuilder.NULL);
        } else {
            buffer.append(this.before);

            final ToStringBuilderOption encoder = this.byteShortIntLongEncoder();
            final String valueSeparator = this.valueSeparator;

            String separator = "";
            for (final short value : array) {
                buffer.append(separator);
                encoder.add(value, buffer);
                separator = valueSeparator;
            }

            buffer.append(this.after);
        }
    }

    /**
     * Adds a {@link boolean}. Note any orphaned label is cleared.
     */
    public ToStringBuilder append(final boolean value) {
        return this.maybeAppend(value);
    }

    /**
     * Adds a {@link byte} of is ignoring {@link ToStringBuilderOption#HEX_BYTES}. Note any orphaned
     * label is cleared.
     */
    public ToStringBuilder append(final byte value) {
        return this.maybeAppend(value);
    }

    /**
     * Adds a {@link char}. Note any orphaned label is cleared.
     */
    public ToStringBuilder append(final char value) {
        return this.maybeAppend(value);
    }

    /**
     * Adds a {@link double}. Note any orphaned label is cleared.
     */
    public ToStringBuilder append(final double value) {
        return this.maybeAppend(value);
    }

    /**
     * Adds a {@link float}. Note any orphaned label is cleared.
     */
    public ToStringBuilder append(final float value) {
        return this.maybeAppend(value);
    }

    /**
     * Adds a {@link int}. Note any orphaned label is cleared.
     */
    public ToStringBuilder append(final int value) {
        return this.maybeAppend(value);
    }

    /**
     * Adds a {@link long}. Note any orphaned label is cleared.
     */
    public ToStringBuilder append(final long value) {
        return this.maybeAppend(value);
    }

    /**
     * Adds a {@link short}. Note any orphaned label is cleared.
     */
    public ToStringBuilder append(final short value) {
        return this.maybeAppend(value);
    }

    /**
     * Adds a {@link Object} calling {@link Object#toString()} and accepting null.
     */
    public ToStringBuilder append(final Object object) {
        return this.maybeAppend(object);
    }

    /**
     * Adds a {@link String} without escaping etc of is. Note any orphaned label is cleared.
     */
    public ToStringBuilder append(final CharSequence chars) {
        return this.maybeAppend(chars);
    }

    /**
     * Appends the {@link Object} and obeys the {@link #globalLength}.
     */
    private ToStringBuilder maybeAppend(final Object object) {
        final StringBuilder buffer = this.buffer;
        final int before = buffer.length();
        final int globalLength = this.globalLength;
        final int left = globalLength - before;
        if (left > 0) {
            final String string = String.valueOf(object);
            buffer.append(string, 0, Math.min(string.length(), left));
        }
        this.afterValue();
        this.skipSeparatorIfNextValueIsWithoutLabel = true;
        return this;
    }

    /**
     * Sets the separator before labels and after values. The default is a single space.
     */
    public ToStringBuilder separator(final String separator) {
        Objects.requireNonNull(separator, "separator");

        this.separator = separator;
        return this;
    }

    /**
     * The default separator is a single space.
     */
    private String separator;

    /**
     * Sets the separator between a label and value.
     */
    public ToStringBuilder labelSeparator(final String separator) {
        Objects.requireNonNull(separator, "separator");

        this.labelSeparator = separator;
        return this;
    }

    /**
     * The default label separator is the equals sign.
     */
    private String labelSeparator;

    /**
     * Sets the separator between consecutive values.
     */
    public ToStringBuilder valueSeparator(final String separator) {
        Objects.requireNonNull(separator, "separator");

        this.valueSeparator = separator;
        return this;
    }

    private String valueSeparator;

    /**
     * Sets two surround {@link String} values that surround zero or more values. This only affects
     * {@link Iterator iterators}, {@link Iterable iterable}, {@link Map maps} and arrays except for
     * char[].
     *
     * <pre>
     * ToStringBuilder.create()<br>
     * .surroundValues(&quot;[&quot;, &quot;]&quot;)<br>
     * .label(&quot;label&quot;)<br>
     * .value(new int[] { 1, 2, 3 })<br>
     * </pre>
     * <p>
     * prints
     *
     * <pre>
     * [1, 2, 3]
     * </pre>
     */
    public ToStringBuilder surroundValues(final String before, final String after) {
        Objects.requireNonNull(before, "before");
        Objects.requireNonNull(after, "after");

        this.before = before;
        this.after = after;
        return this;
    }

    private String before;

    private String after;

    /**
     * Sets the maximum length of the {@link String} form for any value that is added. Once this
     * limit is reached all future appends involving that value are short circuited such of
     * processing a multi value array etc. Note when processing a value recursively it is not
     * possible increase the global length.
     */
    public ToStringBuilder valueLength(final int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length " + length + " must be greater than 0");
        }

        this.valueLength = Math.min(length, this.maxValueLength);
        return this;
    }

    int valueLength;

    private int maxValueLength;

    /**
     * Sets the maximum length of the {@link String} that will be returned. Once this limit is
     * reached all future appends in any form are ignored. Note when processing a value recursively
     * it is not possible increase the global length.
     */
    public ToStringBuilder globalLength(final int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length " + length + " must be greater than 0");
        }

        final int max = this.maxGlobalLength;
        final int updatedLength = this.callerBufferLengthSnapshot + length;
        this.globalLength = Math.min(updatedLength, max);
        this.maxGlobalLength = Math.max(updatedLength, max);
        return this;
    }

    int globalLength;

    private int maxGlobalLength;

    private int callerBufferLengthSnapshot;

    /**
     * A counter that records the number of recursive calls, involving of value adding another
     * recursively.
     */
    private int depth;

    /**
     * Only returns true if inline elements is enabled.
     */
    private boolean inlineElements() {
        return this.options.contains(ToStringBuilderOption.INLINE_ELEMENTS);
    }

    /**
     * Trims or chops any excess chars added to the {@link StringBuilder buffer} if it has spilled
     * over the limits of {@link #globalLength} or {@link #valueLength}.
     */
    private void trimBufferIfNecessary(final int beforeLength) {
        final StringBuilder buffer = this.buffer;
        int length = buffer.length();

        // value
        final int valueLength = this.valueLength;
        if ((length - beforeLength) > valueLength) {
            length = beforeLength + valueLength;
            buffer.setLength(length);
        }

        // global
        final int globalLength = this.globalLength;
        if ((length - globalLength) > 0) {
            buffer.setLength(globalLength);
        }
    }

    /**
     * Returns the built {@link String}.
     */
    @Override
    public String build() {
        return this.buffer.toString();
    }

    /**
     * Tests if the {@link StringBuilder} is full returning the maximum number of characters left. A
     * value less than or equal to 0 indicates its full.
     */
    private int bufferLeft() {
        return this.globalLength - this.buffer.length();
    }

    /**
     * Accumulates the to string result of it is built.
     */
    final StringBuilder buffer;

    /**
     * The parent object of this. This allows children to make decisions depending on their parent
     * or container.
     */
    private Object parent;

    /**
     * Holds the current {@link UsesToStringBuilder} being processed.
     */
    private Object self;

    /**
     * Allows a nested {@link UsesToStringBuilder} to learn about its container /parent or null if
     * it this builder was just created.
     */
    public Object parent() {
        return this.parent;
    }

    /**
     * A counter that is incremented each time a value is added.
     */
    private int valuesAdded;

    // options

    /**
     * Adds a possible enabled {@link ToStringBuilderOption option}.
     */
    public ToStringBuilder enable(final ToStringBuilderOption option) {
        Objects.requireNonNull(option, "option");

        this.options.add(option);
        return this;
    }

    /**
     * Removes a possible enabled {@link ToStringBuilderOption option}.
     */
    public ToStringBuilder disable(final ToStringBuilderOption option) {
        Objects.requireNonNull(option, "option");

        this.options.remove(option);
        return this;
    }

    /**
     * Holds the currently enabled {@link ToStringBuilderOption options}.
     */
    EnumSet<ToStringBuilderOption> options;

    /**
     * Returns true if {@link ToStringBuilderOption#SKIP_IF_DEFAULT_VALUE} is true.
     */
    private boolean skipIfNotDefaultValue() {
        return false == this.options.contains(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);
    }

    /**
     * Returns a {@link ToStringBuilderOption} which can be used to encode individual short/int/long
     * values.
     */
    private ToStringBuilderOption byteShortIntLongEncoder() {
        return this.options.contains(ToStringBuilderOption.HEX_WHOLE_NUMBERS) ? //
                ToStringBuilderOption.HEX_WHOLE_NUMBERS : //
                ToStringBuilderOption.DEFAULT;
    }

    /**
     * Returns a {@link Runnable} which will when invoked restore the state captured here.
     * Currently the following is captured.
     * <ul>
     * <li>{@link #labelSeparator(String)}</li>
     * <li>{@link #options}</li>
     * <li>{@link #separator(String)}</li>
     * </ul>
     * Short term values such as {@link #label(String)} which are cleared after each value are ignored.
     */
    public Runnable saveState() {
        return ToStringBuilderSaveStateRunnable.with(this.labelSeparator,
                this.options,
                this.separator,
                this.valueSeparator,
                this);
    }

    /**
     * Only invoked by {@link ToStringBuilderSaveStateRunnable#run()}
     */
    void restoreState(final String labelSeparator,
                      final EnumSet<ToStringBuilderOption> options,
                      final String separator,
                      final String valueSeparator) {
        this.labelSeparator = labelSeparator;
        this.options.clear();
        this.options.addAll(options);
        this.separator = separator;
        this.valueSeparator = valueSeparator;
    }

    // Object...........................................................................................................

    /**
     * Dump the any set options followed by the {@link StringBuilder buffer}.
     */
    @Override
    public String toString() {
        final StringBuilder toString = new StringBuilder();
        toString.append(this.options);
        toString.setLength(toString.length() - 1); // remove the closing ]

        toString.append(", labelSeparator=");
        toString.append(CharSequences.quoteAndEscape(this.labelSeparator));

        toString.append(", valueSeparator=");
        toString.append(CharSequences.quoteAndEscape(this.valueSeparator));

        toString.append(", separator=");
        toString.append(CharSequences.quoteAndEscape(this.separator));

        final StringBuilder buffer = this.buffer;
        if (this.bufferLeft() >= 0) {
            toString.append(", valueLength=");
            toString.append(this.valueLength);

            toString.append(", globalLength=");
            toString.append(this.globalLength);
        } else {
            toString.append(", FULL");
        }
        toString.append("] ");
        toString.append(buffer.length());
        toString.append("=");
        toString.append(CharSequences.quoteAndEscape(buffer));
        return toString.toString();
    }
}
