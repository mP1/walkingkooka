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

package walkingkooka.net;

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.text.CharacterConstant;

import java.util.Arrays;
import java.util.Objects;

/**
 * Holds an IP6 {@link IpAddress}.
 */
public final class Ip6Address extends IpAddress implements Comparable<Ip6Address> {

    /**
     * The separator used in between components.
     */
    final static CharacterConstant SEPARATOR = CharacterConstant.with(':');

    /**
     * The number of bits in an IP6
     */
    public final static int BIT_COUNT = 128;

    /**
     * The number of octets in an IP6
     */
    public final static int OCTET_COUNT = Ip6Address.BIT_COUNT / 8;

    /**
     * Creates a new {@link Ip6Address} expecting just 16 elements.
     */
    public static Ip6Address with(final byte[] components) {
        Objects.requireNonNull(components, "components");

        final byte[] copy = components.clone();
        if (copy.length != OCTET_COUNT) {
            throw new IllegalArgumentException("Expected " + OCTET_COUNT + " components but got " + copy.length + "=" + Arrays.toString(components));
        }

        return new Ip6Address(components);
    }

    /**
     * Private constructor use factory
     */
    private Ip6Address(final byte[] components) {
        super(components);
    }

    @Override
    public boolean isIp4() {
        return false;
    }

    @Override
    public boolean isIp6() {
        return true;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof Ip6Address;
    }

    // UsesToStringBuilder

    @Override
    public final void buildToString(final ToStringBuilder builder) {
        final byte[] components = this.components;
        int significant = Ip6Address.OCTET_COUNT;
        do {
            if (0 != components[significant - 1]) {
                break;
            }
            significant--;
        } while (significant > 0);
        significant = Math.max(1, significant);

        builder.separator(Ip6Address.SEPARATOR.string());
        builder.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);
        builder.disable(ToStringBuilderOption.QUOTE);

        for (int i = 0; i < significant; i++) {
            final int value = 0xFF & components[i];
            builder.value(Integer.toHexString(value).toUpperCase());
        }

        if (significant < Ip6Address.OCTET_COUNT) {
            builder.append(Ip6Address.SEPARATOR.character()).append(Ip6Address.SEPARATOR.character());
        }
    }

    @Override
    public int compareTo(final Ip6Address other) {
        return this.compare0(other);
    }

    // Serializable
    private static final long serialVersionUID = 1;
}
