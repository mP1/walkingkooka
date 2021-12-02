
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

public final class TestingTest {

    private final static String MESSAGE = "Message123";

    // checkEquals.....................................................................................................

    @Test
    public void testCheckEquals() {
        new Testing() {

        }.checkEquals("123", "123");
    }

    @Test
    public void testCheckEqualsDifferent() {
        boolean failed = false;

        try {
            new Testing() {

            }.checkEquals("123", "different");
        } catch (final AssertionFailedError caught) {
            failed = true;
        }
        assertEquals(true, failed);
    }

    @Test
    public void testCheckEqualsString() {
        new Testing() {

        }.checkEquals("123", "123", MESSAGE);
    }

    @Test
    public void testCheckEqualsStringDifferent() {
        boolean failed = false;

        try {
            new Testing() {

            }.checkEquals("123", "different", MESSAGE);
        } catch (final AssertionFailedError caught) {
            failed = true;
        }
        assertEquals(true, failed);
    }

    @Test
    public void testCheckEqualsSupplier() {
        new Testing() {

        }.checkEquals("123", "123", () -> MESSAGE);
    }

    @Test
    public void testCheckEqualsSupplierDifferent() {
        boolean failed = false;

        try {
            new Testing() {

            }.checkEquals("123", "different", () -> MESSAGE);
        } catch (final AssertionFailedError caught) {
            failed = true;
        }
        assertEquals(true, failed);
    }

    // checkNotEquals...................................................................................................

    @Test
    public void testCheckNotEquals() {
        new Testing() {

        }.checkNotEquals("123", "different");
    }

    @Test
    public void testCheckNotEqualsDifferent() {
        boolean failed = false;

        try {
            new Testing() {

            }.checkNotEquals("123", "123");
        } catch (final AssertionFailedError caught) {
            failed = true;
        }
        assertEquals(true, failed);
    }

    @Test
    public void testCheckNotEqualsString() {
        new Testing() {

        }.checkNotEquals("123", "different", MESSAGE);
    }

    @Test
    public void testCheckNotEqualsStringDifferent() {
        boolean failed = false;

        try {
            new Testing() {

            }.checkNotEquals("123", "123", MESSAGE);
        } catch (final AssertionFailedError caught) {
            failed = true;
        }
        assertEquals(true, failed);
    }

    @Test
    public void testCheckNotEqualsSupplier() {
        new Testing() {

        }.checkNotEquals("123", "different", () -> MESSAGE);
    }

    @Test
    public void testCheckNotEqualsSupplierDifferent() {
        boolean failed = false;

        try {
            new Testing() {

            }.checkNotEquals("123", "123", () -> MESSAGE);
        } catch (final AssertionFailedError caught) {
            failed = true;
        }
        assertEquals(true, failed);
    }
}
