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

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.StandardThrowableTesting;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NeverErrorTest implements StandardThrowableTesting<NeverError> {

    @Test
    public void testUnsupportedEncodingException() {
        final UnsupportedEncodingException unsupported = new UnsupportedEncodingException("something");
        this.check(() -> NeverError.unsupportedEncodingException(unsupported),
            "something",
            unsupported);
    }

    @Test
    public void testUnexpectedMethodCall() {
        this.check(() -> NeverError.unexpectedMethodCall(this, "method1", 1, 2, 3),
            "walkingkooka.NeverErrorTest.method1(1,2,3)",
            null);
    }

    @Test
    public void testUnhandledCase() {
        this.check(() -> NeverError.unhandledCase(this, "case-1", "case-2"),
            "Unhandled value: NeverErrorTest only expected: \"case-1\",\"case-2\"",
            null);
    }

    @Test
    public void testUnhandledEnum() {
        this.check(() -> NeverError.unhandledEnum(TestEnum.A, TestEnum.B, TestEnum.C),
            "Unhandled enum value: A only expected: B,C",
            null);
    }

    private void check(final Runnable thrower,
                       final String message,
                       final Throwable cause) {
        final NeverError expected = assertThrows(NeverError.class, thrower::run);

        this.checkThrowable(expected, message, cause);
    }

    enum TestEnum {
        A,
        B,
        C
    }

    @Override
    public NeverError createThrowable(final String message) {
        return new NeverError(message);
    }

    @Override
    public NeverError createThrowable(final String message, final Throwable cause) {
        return new NeverError(message, cause);
    }

    @Override
    public Class<NeverError> type() {
        return NeverError.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
