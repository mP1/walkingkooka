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

package walkingkooka.collect.list;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ListTestingTest implements ListTesting {

    @Test
    public void testGetAndCheck() {
        this.getAndCheck(Lists.of("a1", "b2", "c3"), 2, "c3");
    }

    @Test
    public void testGetAndCheckFails() {
        assertThrows(AssertionFailedError.class, () -> this.getAndCheck(Lists.of("a1", "b2", "c3"), 2, "999"));
    }

    @Test
    public void testGetFails() {
        this.getFails(Lists.of("a1", "b2", "c3"), 99);
    }

    @Test
    public void testGetFailsDoesntFail() {
        assertThrows(AssertionFailedError.class, () -> this.getFails(Lists.of("a1", "b2", "c3"), 2));
    }

    @Test
    public void testSetFails() {
        this.setFails(
            Lists.of("1a"),
            0,
            "*NEW*"
        );
    }

    @Test
    public void testRemoveIndexFails() {
        this.removeIndexFails(
            Lists.of("1a"),
            0
        );
    }

    @Test
    public void testRemoveIndexAndCheck() {
        final List<String> list = Lists.array();
        list.add("A1");
        list.add("B2");
        list.add("C3");

        this.removeIndexAndCheck(
            list,
            1,
            "B2"
        );

        this.checkEquals(
            Lists.of(
                "A1",
                "C3"
            ),
            list
        );
    }

    @Test
    public void testRemoveElementAndCheck() {
        final List<String> list = Lists.array();
        list.add("A1");
        list.add("B2");
        list.add("C3");

        this.removeElementAndCheck(
            list,
            "B2",
            true
        );

        this.checkEquals(
            Lists.of(
                "A1",
                "C3"
            ),
            list
        );
    }

    @Test
    public void testRemoveElementAndCheckUnknownElement() {
        final List<String> list = Lists.array();
        list.add("A1");
        list.add("B2");
        list.add("C3");

        this.removeElementAndCheck(
            list,
            "?Unknown???",
            false
        );

        this.checkEquals(
            Lists.of(
                "A1",
                "B2",
                "C3"
            ),
            list
        );
    }
}
