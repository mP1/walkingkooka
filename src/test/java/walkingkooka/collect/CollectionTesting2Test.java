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
import org.opentest4j.AssertionFailedError;
import walkingkooka.Cast;
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.list.Lists;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CollectionTesting2Test implements CollectionTesting2<List<String>, String> {

    private final static String ELEMENT1 = "a1";
    private final static String ELEMENT2 = "b2";

    @Test
    public void testIsEmptyAndSize() {
        this.sizeAndCheck(this.createCollection(), 2);
    }

    @Test
    public void testIsEmptyAndSizeFails() {
        assertThrows(AssertionFailedError.class, () -> {
            this.sizeAndCheck(this.createCollection(), 999);
        });
    }

    @Test
    public void testIteratorContainsAndCollectionFails() {
        assertThrows(AssertionFailedError.class, () -> {
            new CollectionTesting2<List<String>, String>() {

                @Override
                public List<String> createCollection() {
                    return new AbstractList<>() {
                        @Override
                        public String get(final int index) {
                            if (0 == index) {
                                return "a1";
                            }
                            if (1 == index) {
                                return "b1";
                            }
                            throw new ArrayIndexOutOfBoundsException("Invalid index: " + index);
                        }

                        @Override
                        public Iterator<String> iterator() {
                            return Iterators.one("a1");
                        }

                        @Override
                        public int size() {
                            return 2;
                        }
                    };
                }

                @Override
                public Class<List<String>> type() {
                    return Cast.to(List.class);
                }
            }.testIteratorContainsAndCollection();
        });
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
