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

package walkingkooka.net;

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.text.CharacterConstant;

import java.util.Arrays;
import java.util.Objects;

/**
 * Holds an IP4 {@link IpAddress}.
 */
public final class Ip4Address extends IpAddress implements Comparable<Ip4Address> {

    /**
     * The separator used in between components.
     */
    public final static CharacterConstant SEPARATOR = CharacterConstant.with('.');

    /**
     * The number of bits in an IP4
     */
    public final static int BIT_COUNT = 32;

    /**
     * The number of octets in an IP6
     */
    public final static int OCTET_COUNT = Ip4Address.BIT_COUNT / 8;

    /**
     * Creates a new {@link Ip4Address} expecting just 4 elements.
     */
    public static Ip4Address with(final byte[] components) {
        Objects.requireNonNull(components, "components");

        final byte[] copy = components.clone();
        if(copy.length != OCTET_COUNT){
            throw new IllegalArgumentException("Expected " + OCTET_COUNT + " components but got " + copy.length + "=" + Arrays.toString(components));
        }

        return new Ip4Address(copy);
    }

    /**
     * Private constructor use factory
     */
    private Ip4Address(final byte[] components) {
        super(components);
    }

    /**
     * Returns an {@link Ip4Address} subnet with the given significant bits.
     */
    public Ip4Address subnet(final int significantBits) throws IllegalArgumentException {
        if(significantBits < 0 || significantBits > BIT_COUNT){
            throw new IllegalArgumentException("Invalid significiant bits " + significantBits + " must be between 0 and " + BIT_COUNT);
        }

        final byte[] original = this.components;
        final int value = (mask(original[0]) << 24) | //
                (mask(original[1]) << 16) | //
                (mask(original[2]) << 8) | //
                mask(original[3]);
        final int masked = (int) (0xFFFFFFFF00000000L >> significantBits) & value;

        Ip4Address result = this;
        if (value != masked) {
            result = new Ip4Address(toBytes(masked));
        }

        return result;
    }

    private static int mask(final int value) {
        return 0xFF & value;
    }

    // @VisibleForTesting
    static byte[] toBytes(final int value) {
        return new byte[]{toByte(value >> 24),
                toByte(value >> 16),
                toByte(value >> 8),
                toByte(value)};
    }

    private static byte toByte(final int value) {
        return (byte) value;
    }

    @Override
    public boolean isIp4() {
        return true;
    }

    @Override
    public boolean isIp6() {
        return false;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof Ip4Address;
    }

    @Override
    public final void buildToString(final ToStringBuilder builder) {
        builder.separator(Ip4Address.SEPARATOR.string());
        builder.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);

        for (final byte octet : this.components) {
            builder.value(0xFF & octet);
        }
    }

    // Comparable

    @Override
    public int compareTo(final Ip4Address other) {
        return this.compare0(other);
    }

    // Serializable
    private static final long serialVersionUID = 1;
}
