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

package walkingkooka.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Verifies that the test suite name matches the type being tested. The formula for the test suite is {@link #type()}
 * + <code>Test</code>
 */
public interface TestSuiteNameTesting<T> extends Testing {

    @Test
    default void testTestNaming() {
        final String type = this.type().getName();
        final String test = this.getClass().getName();
        if (!test.endsWith("Test")) {
            fail("Test name " + test + " incorrect for " + type);
        }
        assertEquals(test, type + "Test", () -> "Test name " + test + " incorrect for " + type);
    }

    /**
     * The type being tested.
     */
    abstract public Class<T> type();
}
