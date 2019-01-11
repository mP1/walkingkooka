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

import org.junit.Test;
import walkingkooka.test.PublicThrowableTestCase;

import java.io.UnsupportedEncodingException;

public final class NeverErrorTest extends PublicThrowableTestCase<NeverError> {

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

    enum TestEnum {
        A,
        B,
        C;
    }

    @Override
    protected Class<NeverError> type() {
        return NeverError.class;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
