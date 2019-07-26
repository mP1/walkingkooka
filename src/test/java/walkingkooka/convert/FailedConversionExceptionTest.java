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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ThrowableTesting2;
import walkingkooka.type.JavaVisibility;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FailedConversionExceptionTest implements ThrowableTesting2<FailedConversionException> {

    @Test
    public void testWithNullTargetTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            new FailedConversionException("value", null);
        });
    }

    @Test
    public void testWithNullTargetTypeAndThrowableFails() {
        assertThrows(NullPointerException.class, () -> {
            new FailedConversionException("value", null, new Exception("cause"));
        });
    }

    @Test
    public void testWithNullThrowableFails() {
        assertThrows(NullPointerException.class, () -> {
            new FailedConversionException("value", Void.class, null);
        });
    }

    @Test
    public void testMessage() {
        this.checkMessage(new FailedConversionException("abc123", BigDecimal.class),
                "Failed to convert \"abc123\" (java.lang.String) to java.math.BigDecimal");
    }

    @Test
    public void testMessage2() {
        this.checkMessage(new FailedConversionException(1234.5, Byte.class),
                "Failed to convert 1234.5 (java.lang.Double) to java.lang.Byte");
    }

    @Test
    public void testMessageWithCause() {
        this.checkMessage(new FailedConversionException("abc123", BigDecimal.class, new Exception("cause")),
                "Failed to convert \"abc123\" (java.lang.String) to java.math.BigDecimal");
    }

    @Override
    public Class<FailedConversionException> type() {
        return FailedConversionException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
