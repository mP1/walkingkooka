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

package walkingkooka.collect.enumeration;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.Enumeration;
import java.util.Vector;

public final class EnumerationTestingTest implements EnumerationTesting {

    @Test
    public void testHasMoreElementsAndCheckFalse() {
        this.hasMoreElementsAndCheck(
            enumeration(),
            false
        );
    }

    @Test
    public void testHasMoreElementsAndCheckTrue() {
        this.hasMoreElementsAndCheck(
            this.enumeration("A"),
            true
        );
    }

    @Test
    public void testNextElementAndCheck() {
        this.nextElementAndCheck(
            enumeration("A"),
            "A"
        );
    }

    @Test
    public void testNextElementAndCheckTrue() {
        this.nextElementFails(
            this.enumeration()
        );
    }

    @Test
    public void testEnumerateUsingHasMoreElementsAndCheck() {
        final String[] array = new String[]{
            "1A",
            "2B",
            "3C"
        };
        this.enumerateUsingHasMoreElementsAndCheck(
            enumeration(array),
            array
        );
    }

    @Test
    public void testEnumerateUsingHasMoreElementsAndCheckFails() {
        boolean thrown = false;

        try {
            this.enumerateUsingHasMoreElementsAndCheck(
                enumeration("1A", "2B"),
                "1A"
            );
        } catch (final AssertionFailedError expected) {
            thrown = true;
        }

        this.checkEquals(
            true,
            thrown
        );
    }

    @Test
    public void testEnumerateAndCheck() {
        final String[] array = new String[]{
            "1A",
            "2B",
            "3C"
        };
        this.enumerateAndCheck(
            enumeration(array),
            array
        );
    }

    @Test
    public void testEnumerateAndCheckFails() {
        boolean thrown = false;

        try {
            this.enumerateAndCheck(
                enumeration("1A", "2B"),
                "1A"
            );
        } catch (final AssertionFailedError expected) {
            thrown = true;
        }

        this.checkEquals(
            true,
            thrown
        );
    }

    private Enumeration<String> enumeration(final String... values) {
        final Vector<String> vector = new Vector<>();
        for (final String value : values) {
            vector.add(value);
        }

        return vector.elements();
    }
}
