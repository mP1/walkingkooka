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

package walkingkooka.collect;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CollectionTestingTest implements CollectionTesting<List<String>, String> {

    private final static String ELEMENT1 = "a1";
    private final static String ELEMENT2 = "b2";

    @Test
    public void testAddFails() {
        this.addFails(Collections.emptyList(), "add!");
    }

    @Test
    public void testContains() {
        this.containsAndCheck(Collections.singleton(ELEMENT1), ELEMENT1);
    }

    @Test
    public void testContainsPresentAbsent() {
        assertThrows(AssertionError.class, () -> {
            this.containsAndCheck(Collections.singleton(ELEMENT1), ELEMENT2);
        });
    }

    @Test
    public void testContainsAbsent() {
        this.containsAndCheckAbsent(Collections.singleton(ELEMENT1), ELEMENT2);
    }

    @Test
    public void testContainsAbsentPresent() {
        assertThrows(AssertionError.class, () -> {
            this.containsAndCheckAbsent(Collections.singleton(ELEMENT1), ELEMENT1);
        });
    }

    @Test
    public void testIsEmptyEmpty() {
        this.isEmptyAndCheck(Lists.empty(), true);
    }

    @Test
    public void testIsEmptyNotEmpty() {
        this.isEmptyAndCheck(this.createCollection(), false);
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createCollection(), 2);
    }

    @Test
    public void testRemoveFails() {
        this.removeFails(this.createCollection(), ELEMENT1);
    }

    @Override
    public void testCheckToStringOverridden() {
    }

    @Override
    public List<String> createCollection() {
        return Lists.of(ELEMENT1, ELEMENT2);
    }

    @Override
    public Class<List<String>> type() {
        return Cast.to(List.class);
    }
}
