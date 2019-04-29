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

package walkingkooka;

import org.junit.jupiter.api.Test;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BinaryTest implements HashCodeEqualsDefinedTesting<Binary>,
        ToStringTesting<Binary> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            Binary.with(null);
        });
    }

    @Test
    public void testWith() {
        final byte[] value = this.value();
        final Binary binary = Binary.with(value);
        assertArrayEquals(value, binary.value());
    }

    @Test
    public void testWithCopied() {
        final byte[] value = this.value();
        final Binary binary = Binary.with(value);
        value[0] = (byte) 0xff;

        assertArrayEquals(this.value(), binary.value());
    }

    @Test
    public void testValueCopied() {
        final Binary binary = this.createObject();
        final byte[] value = binary.value();
        value[0] = (byte) 0xff;

        assertArrayEquals(this.value(), binary.value());
    }

    @Test
    public void testDifferent() {
        this.checkNotEquals(Binary.with(new byte[]{4, 5, 6}));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createObject(), Arrays.toString(this.value()));
    }

    @Override
    public Binary createObject() {
        return Binary.with(this.value());
    }

    private byte[] value() {
        return new byte[]{1, 22, 33};
    }

    @Override
    public Class<Binary> type() {
        return Binary.class;
    }
}
