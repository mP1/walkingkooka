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

package walkingkooka.reflect;

import org.junit.jupiter.api.Test;
import walkingkooka.test.Testing;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TypeNameTestingTest implements Testing {

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
        assertThrows(AssertionError.class, () -> new TestEmptyPrefixSuffix().testTypeNaming());
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
        assertThrows(AssertionError.class, () -> new TestIncorrectPrefix().testTypeNaming());
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
        assertThrows(AssertionError.class, () -> new TestIncorrectSuffix().testTypeNaming());
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
        assertThrows(AssertionError.class, () -> new TestSubtractPrefixFails().subtractTypeNamePrefix());
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
        this.checkEquals("actPrefix", new TestSubtractPrefix().subtractTypeNamePrefix());
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
        assertThrows(AssertionError.class, () -> new TestSubtractSuffixFails().subtractTypeNameSuffix());
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
        this.checkEquals("TestSubtra", new TestSubtractSuffix().subtractTypeNameSuffix());
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
}
