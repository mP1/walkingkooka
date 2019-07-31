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

package walkingkooka.test;

import walkingkooka.SystemException;
import walkingkooka.test.StandardThrowableTestingTest.TestRuntimeException;
import walkingkooka.type.JavaVisibility;

public final class StandardThrowableTestingTest extends TestingTestCase implements StandardThrowableTesting<TestRuntimeException> {

    @Override
    public void testTestNaming() {
    }

    @Override
    public void testAllConstructorsVisibility() {
    }

    // StandardThrowableTesting.........................................................................................

    @Override
    public TestRuntimeException createThrowable(final String message) {
        return new TestRuntimeException(message);
    }

    @Override
    public TestRuntimeException createThrowable(final String message, final Throwable cause) {
        return new TestRuntimeException(message, cause);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Class<TestRuntimeException> type() {
        return TestRuntimeException.class;
    }

    static public class TestRuntimeException extends SystemException {
        TestRuntimeException(final String message) {
            super(message);
        }

        TestRuntimeException(final String message, final Throwable cause) {
            super(message, cause);
        }

        private static final long serialVersionUID = 1L;
    }
}
