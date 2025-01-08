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
import walkingkooka.InvalidCharacterException;
import walkingkooka.text.CharSequences;

import java.util.Objects;

public final class ParseStringTestingTest implements ParseStringTesting<String> {

    @Test
    public void testParseStringNullFails() {
        new TestParseStringNullFails().testParseStringNullFails();
    }

    static class TestParseStringNullFails extends TestParseStringTesting<String> {
        @Override
        public String parseString(final String text) {
            Objects.requireNonNull(text, "text");
            return text;
        }
    }

    @Test
    public void testParseStringNullFails2() {
        mustFail(() -> new TestParseStringNullFails2().testParseStringNullFails());
    }

    static class TestParseStringNullFails2 extends TestParseStringTesting<String> {
        @Override
        public String parseString(final String text) {
            return text;
        }
    }

    @Test
    public void testParseStringEmptyFails() {
        new TestParseStringEmptyFails().testParseStringEmptyFails();
    }

    static class TestParseStringEmptyFails extends TestParseStringTesting<String> {

        @Override
        public String parseString(final String text) {
            CharSequences.failIfNullOrEmpty(text, "text");
            return text;
        }
    }

    @Test
    public void testParseStringEmptyFails2() {
        mustFail(() -> new TestParseStringEmptyFails2().testParseStringEmptyFails());
    }

    static class TestParseStringEmptyFails2 extends TestParseStringTesting<String> {

        @Override
        public String parseString(final String text) {
            return text;
        }
    }

    @Test
    public void testParseStringAndCheck() {
        this.parseStringAndCheck("abc123", "abc123!!!");
    }

    @Test
    public void testParseStringAndCheckFails() {
        this.mustFail(() -> this.parseStringAndCheck("abc123", "WRONG!!!"));
    }

    @Test
    public void testParseStringInvalidCharacterFails() {
        new TestParseStringInvalidCharacterFails().parseStringInvalidCharacterFails("abc");
    }

    static class TestParseStringInvalidCharacterFails extends TestParseStringTesting<String> {
        @Override
        public String parseString(final String text) {
            throw new InvalidCharacterException(text, text.length() - 1);
        }
    }

    @Test
    public void testParseStringInvalidCharacterFails2() {
        mustFail(() -> new TestParseStringInvalidCharacterFails2().parseStringInvalidCharacterFails("abc"));
    }

    static class TestParseStringInvalidCharacterFails2 extends TestParseStringTesting<String> {
        @Override
        public String parseString(final String text) {
            return text;
        }
    }

    @Test
    public void testParseStringInvalidCharacterFailsChar() {
        new TestParseStringInvalidCharacterFailsChar().parseStringInvalidCharacterFails("abcd", 'b');
    }

    static class TestParseStringInvalidCharacterFailsChar extends TestParseStringTesting<String> {
        @Override
        public String parseString(final String text) {
            throw new InvalidCharacterException(text, 1);
        }
    }

    @Test
    public void testParseStringInvalidCharacterFailsChar2() {
        mustFail(() -> new TestParseStringInvalidCharacterFailsChar2().parseStringInvalidCharacterFails("abc", 'b'));
    }

    static class TestParseStringInvalidCharacterFailsChar2 extends TestParseStringTesting<String> {
        @Override
        public String parseString(final String text) {
            return text;
        }
    }

    @Test
    public void testParseStringInvalidCharacterFailsPos() {
        new TestParseStringInvalidCharacterFailsPos().parseStringInvalidCharacterFails("abcd", 1);
    }

    static class TestParseStringInvalidCharacterFailsPos extends TestParseStringTesting<String> {
        @Override
        public String parseString(final String text) {
            throw new InvalidCharacterException(text, 1);
        }
    }

    @Test
    public void testParseStringInvalidCharacterFailsPos2() {
        mustFail(() -> new TestParseStringInvalidCharacterFailsPos2().parseStringInvalidCharacterFails("abc", 1));
    }

    static class TestParseStringInvalidCharacterFailsPos2 extends TestParseStringTesting<String> {
        @Override
        public String parseString(final String text) {
            return text;
        }
    }


    @Test
    public void testParseStringFailsThrowable() {
        new TestParseStringFailsThrowable().parseStringFails("abcd", new IllegalArgumentException("message123"));
    }

    static class TestParseStringFailsThrowable extends TestParseStringTesting<String> {
        @Override
        public String parseString(final String text) {
            throw new IllegalArgumentException("message123");
        }
    }

    @Test
    public void testParseStringFailsThrowable2() {
        mustFail(() -> new TestParseStringFailsThrowable2().parseStringFails("abcd", new IllegalArgumentException("message123")));
    }

    static class TestParseStringFailsThrowable2 extends TestParseStringTesting<String> {
        @Override
        public String parseString(final String text) {
            throw new IllegalArgumentException("different");
        }
    }

    @Test
    public void testParseStringFailsThrowable3() {
        mustFail(() -> new TestParseStringFailsThrowable3().parseStringFails("abcd", new IllegalArgumentException("message123")));
    }

    static class TestParseStringFailsThrowable3 extends TestParseStringTesting<String> {
        @Override
        public String parseString(final String text) {
            throw new RuntimeException("different");
        }
    }

    @Test
    public void testParseStringFailsClass() {
        new TestParseStringFailsClass().parseStringFails("abcd", IllegalArgumentException.class);
    }

    static class TestParseStringFailsClass extends TestParseStringTesting<String> {
        @Override
        public String parseString(final String text) {
            throw new IllegalArgumentException();
        }
    }

    @Test
    public void testParseStringFailsClass2() {
        mustFail(() -> new TestParseStringFailsShaded2().parseStringFails("abcd", IllegalArgumentException.class));
    }

    static class TestParseStringFailsShaded2 extends TestParseStringTesting<String> {
        @Override
        public String parseString(final String text) {
            return text;
        }
    }

    // helper...........................................................................................................

    static abstract class TestParseStringTesting<T> implements ParseStringTesting<T> {

        @Override
        public void testParseStringNullFails() {
            ParseStringTesting.super.testParseStringNullFails();
        }

        @Override
        public void testParseStringEmptyFails() {
            ParseStringTesting.super.testParseStringEmptyFails();
        }

        @Override
        public final Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> expected) {
            return expected;
        }

        @Override
        public final RuntimeException parseStringFailedExpected(final RuntimeException expected) {
            return expected;
        }
    }

    private void mustFail(final Runnable run) {
        boolean fail = false;
        try {
            run.run();
        } catch (final AssertionFailedError expected) {
            fail = true;
        }
        this.checkEquals(true, fail);
    }

    @Override
    public String parseString(final String text) {
        return text + "!!!";
    }

    @Override
    public final Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    @Override
    public final RuntimeException parseStringFailedExpected(final RuntimeException expected) {
        return expected;
    }
}
