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

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TypeNameTestingTest {

    @Test
    public void testNonEmptyPrefixSuffix() {
        new TestNonEmptyPrefixSuffix().testTypeNaming();
    }

    private static class TestNonEmptyPrefixSuffix implements TypeNameTesting<TestNonEmptyPrefixSuffix> {

        @Override
        public Class<TestNonEmptyPrefixSuffix> type() {
            return TestNonEmptyPrefixSuffix.class;
        }

        @Override
        public String typeNamePrefix() {
            return "TestNonE";
        }

        @Override
        public String typeNameSuffix() {
            return "mptyPrefixSuffix";
        }
    }
    
    @Test
    public void testEmptyPrefixSuffixFails() {
        this.mustFail(() -> new TestEmptyPrefixSuffix().testTypeNaming());
    }

    private static class TestEmptyPrefixSuffix implements TypeNameTesting<TestEmptyPrefixSuffix> {

        @Override
        public Class<TestEmptyPrefixSuffix> type() {
            return TestEmptyPrefixSuffix.class;
        }

        @Override
        public String typeNamePrefix() {
            return "";
        }

        @Override
        public String typeNameSuffix() {
            return "";
        }
    }

    @Test
    public void testIncorrectPrefixFails() {
        this.mustFail(() -> new TestIncorrectPrefix().testTypeNaming());
    }

    private static class TestIncorrectPrefix implements TypeNameTesting<TestIncorrectPrefix> {

        @Override
        public Class<TestIncorrectPrefix> type() {
            return TestIncorrectPrefix.class;
        }

        @Override
        public String typeNamePrefix() {
            return "123";
        }

        @Override
        public String typeNameSuffix() {
            return "";
        }
    }

    @Test
    public void testIncorrectSuffixFails() {
        this.mustFail(() -> new TestIncorrectSuffix().testTypeNaming());
    }

    private static class TestIncorrectSuffix implements TypeNameTesting<TestIncorrectSuffix> {

        @Override
        public Class<TestIncorrectSuffix> type() {
            return TestIncorrectSuffix.class;
        }

        @Override
        public String typeNamePrefix() {
            return "";
        }

        @Override
        public String typeNameSuffix() {
            return "123";
        }
    }

    // subtractTypeNamePrefix...........................................................................................

    @Test
    public void testSubtractPrefixFails() {
        this.mustFail(() -> new TestSubtractPrefixFails().subtractTypeNamePrefix());
    }

    private static class TestSubtractPrefixFails implements TypeNameTesting<TestIncorrectSuffix> {

        @Override
        public Class<TestIncorrectSuffix> type() {
            return TestIncorrectSuffix.class;
        }

        @Override
        public String typeNamePrefix() {
            return "123";
        }

        @Override
        public String typeNameSuffix() {
            return "";
        }
    }

    @Test
    public void testSubtractPrefix() {
        assertEquals("actPrefix", new TestSubtractPrefix().subtractTypeNamePrefix());
    }

    private static class TestSubtractPrefix implements TypeNameTesting<TestSubtractPrefix> {

        @Override
        public Class<TestSubtractPrefix> type() {
            return TestSubtractPrefix.class;
        }

        @Override
        public String typeNamePrefix() {
            return "TestSubtr";
        }

        @Override
        public String typeNameSuffix() {
            return "";
        }
    }

    // subtractTypeNameSuffix...........................................................................................

    @Test
    public void testSubtractSuffixFails() {
        this.mustFail(() -> new TestSubtractSuffixFails().subtractTypeNameSuffix());
    }

    private static class TestSubtractSuffixFails implements TypeNameTesting<TestSubtractSuffixFails> {

        @Override
        public Class<TestSubtractSuffixFails> type() {
            return TestSubtractSuffixFails.class;
        }

        @Override
        public String typeNamePrefix() {
            return "";
        }

        @Override
        public String typeNameSuffix() {
            return "123";
        }
    }

    @Test
    public void testSubtractSuffix() {
        assertEquals("TestSubtra", new TestSubtractSuffix().subtractTypeNameSuffix());
    }

    private static class TestSubtractSuffix implements TypeNameTesting<TestSubtractSuffix> {

        @Override
        public Class<TestSubtractSuffix> type() {
            return TestSubtractSuffix.class;
        }

        @Override
        public String typeNamePrefix() {
            return "";
        }

        @Override
        public String typeNameSuffix() {
            return "ctSuffix";
        }
    }

    // testTypeNaming...................................................................................................

    @Test
    public void testTypeNaming() {
        new TestPrefixSuffix().testTypeNaming();
    }

    private static class TestPrefixSuffix implements TypeNameTesting<TestPrefixSuffix> {

        @Override
        public Class<TestPrefixSuffix> type() {
            return TestPrefixSuffix.class;
        }

        @Override
        public String typeNamePrefix() {
            return "TestPre";
        }

        @Override
        public String typeNameSuffix() {
            return "Suffix";
        }
    }

    private void mustFail(final Runnable runnable) {
        boolean failed = false;
        try {
            runnable.run();
        } catch (final AssertionFailedError expected) {
            failed = true;
        }
        assertEquals(true, failed);
    }
}
