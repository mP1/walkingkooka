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

import walkingkooka.text.CharSequences;

import java.util.Collection;
import java.util.Map;

/**
 * The options that may be used to control how various values are encoded or handled by {@link
 * ToStringBuilder}.
 */
public enum ToStringBuilderOption {
    /**
     * Any added {@link CharSequence} or characters will be escaped
     */
    ESCAPE,
    //

    /**
     * Any added {@link CharSequence} or characters will be double quoted.
     */
    QUOTE,
    //

    /**
     * If an array, {@link Collection} or {@link Map} or similar container the elements will be
     * added of a comma separated values.
     */
    INLINE_ELEMENTS,
    //

    /**
     * Null, false or 0 values are ignored.
     */
    SKIP_IF_DEFAULT_VALUE,
    //

    /**
     * any byte values added will be in a hex-decimal value
     */
    HEX_BYTES {
        @Override
        void add(final byte value, final StringBuilder buffer) {
            ToStringBuilderOption.addHex(0xFF & value, buffer, 2);
        }
    },
    //

    /**
     * any <code>byte</code>, <code>short</code>, <code>int</code> or <code>long<code> values added
     * will be in a hex-decimal value
     */
    HEX_WHOLE_NUMBERS {
        @Override
        void add(final byte value, final StringBuilder buffer) {
            ToStringBuilderOption.addHex(0xFF & (long) value, buffer, 2);
        }

        @Override
        void add(final short value, final StringBuilder buffer) {
            ToStringBuilderOption.addHex(0xFFFF & (long) value, buffer, 4);
        }

        @Override
        void add(final int value, final StringBuilder buffer) {
            ToStringBuilderOption.addHex(0xFFFFFFFFL & (value), buffer, 8);
        }

        @Override
        void add(final long value, final StringBuilder buffer) {
            ToStringBuilderOption.addHex(value, buffer, 16);
        }
    },
    //

    /**
     * Any <code>boolean array<code> will have its true values encoded of ones and false values
     * encoded of zeroes.
     */
    ONE_AND_ZERO_BOOLEAN_ARRAYS;

    /**
     * Adds the byte in string form using the default base 10 system.
     */
    void add(final byte value, final StringBuilder buffer) {
        buffer.append(value);
    }

    /**
     * Encodes the given value into its hex form and pads with leading zeroes of required.
     */
    static private void addHex(final long value, final StringBuilder buffer, final int pad) {
        buffer.append(CharSequences.padLeft(Long.toHexString(value).toUpperCase(), pad, '0'));
    }

    void add(final short value, final StringBuilder buffer) {
        buffer.append(value);
    }

    void add(final int value, final StringBuilder buffer) {
        buffer.append(value);
    }

    void add(final long value, final StringBuilder buffer) {
        buffer.append(value);
    }

    final static ToStringBuilderOption DEFAULT = ESCAPE;
}
