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

package walkingkooka.collect.iterator;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.Testing;

import java.util.List;

public final class IteratorTestingTest implements Testing {

    // iterateAndCheck..................................................................................................

    @Test
    public void testIterateAndCheck() {
        final List<String> list = Lists.of(
            "A1",
            "B2",
            "C3"
        );

        new TestIteratorTesting()
            .iterateAndCheck(
                list.iterator(),
                list.toArray(new String[0])
            );
    }

    @Test
    public void testIterateAndCheckElementFail() {
        final List<String> list = Lists.of(
            "A1",
            "B2",
            "C3"
        );

        boolean failed = false;

        try {
            new TestIteratorTesting()
                .iterateAndCheck(
                    list.iterator(),
                    "*"
                );
        } catch (final AssertionFailedError expected) {
            failed = true;
        }
        this.checkEquals(
            true,
            failed
        );
    }

    @Test
    public void testIterateAndCheckFail() {
        final List<String> list = Lists.of(
            "A1",
            "B2",
            "C3"
        );

        boolean failed = false;

        try {
            new TestIteratorTesting()
                .iterateAndCheck(
                    list.iterator(),
                    "A1",
                    "B2"
                );
        } catch (final AssertionFailedError expected) {
            failed = true;
        }
        this.checkEquals(
            true,
            failed
        );
    }

    // iterateUsingHasNextAndCheck......................................................................................

    @Test
    public void testIterateUsingHasNextAndCheck() {
        final List<String> list = Lists.of(
            "A1",
            "B2",
            "C3"
        );

        new TestIteratorTesting()
            .iterateUsingHasNextAndCheck(
                list.iterator(),
                list.toArray(new String[0])
            );
    }

    @Test
    public void testIterateUsingHasNextAndCheckElementFail() {
        final List<String> list = Lists.of(
            "A1",
            "B2",
            "C3"
        );

        boolean failed = false;

        try {
            new TestIteratorTesting()
                .iterateUsingHasNextAndCheck(
                    list.iterator(),
                    "*"
                );
        } catch (final AssertionFailedError expected) {
            failed = true;
        }
        this.checkEquals(
            true,
            failed
        );
    }

    @Test
    public void testIterateUsingHasNextAndCheckFail() {
        final List<String> list = Lists.of(
            "A1",
            "B2",
            "C3"
        );

        boolean failed = false;

        try {
            new TestIteratorTesting()
                .iterateUsingHasNextAndCheck(
                    list.iterator(),
                    "A1",
                    "B2"
                );
        } catch (final AssertionFailedError expected) {
            failed = true;
        }
        this.checkEquals(
            true,
            failed
        );
    }

    static class TestIteratorTesting implements IteratorTesting {

    }
}
