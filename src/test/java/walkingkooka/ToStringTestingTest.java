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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ToStringTestingTest implements ToStringTesting<StringBuilder> {

    @Test
    public void testCheckToStringOverriddenMissing() {
        assertThrows(AssertionError.class, () -> new TestToStringMissing().testCheckToStringOverridden());
    }

    private static class TestToStringMissing implements ToStringTesting<TestToStringMissing> {
        @Override
        public Class<TestToStringMissing> type() {
            return TestToStringMissing.class;
        }
    }

    @Test
    public void testCheckToStringOverriddenPresent() {
        new TestToStringPresent().testCheckToStringOverridden();
    }

    private static class TestToStringPresent implements ToStringTesting<TestToStringPresent> {

        @Override
        public Class<TestToStringPresent> type() {
            return TestToStringPresent.class;
        }

        @Override
        public String toString() {
            return "Present";
        }
    }

    @Test
    public void testToStringCheck() {
        this.toStringAndCheck(new StringBuilder().append("123"), "123");
    }

    @Test
    public void testToStringCheckFails() {
        assertThrows(AssertionError.class, () ->
            this.toStringAndCheck(new StringBuilder().append("123"), "different"));
    }

    @Test
    public void testToStringContainsCheck() {
        this.toStringContainsCheck(new StringBuilder().append("123456"), "1", "2", "3");
    }

    @Test
    public void testToStringContainsCheckFails() {
        assertThrows(AssertionError.class, () ->
            this.toStringContainsCheck(new StringBuilder().append("123456"), "1", "2", "missing")
        );
    }

    @Override
    public Class<StringBuilder> type() {
        return StringBuilder.class;
    }
}
