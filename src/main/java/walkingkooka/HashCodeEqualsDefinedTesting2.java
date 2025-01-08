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

/**
 * A mixin that contains additional tests for a created object.
 */
public interface HashCodeEqualsDefinedTesting2<T> extends HashCodeEqualsDefinedTesting {

    @Test
    default void testHashCode() {
        final Object object = this.createObject();
        this.checkEquals(
            object.hashCode(),
            object.hashCode(),
            () -> "repeated calls to hashCode should return same value: " + object
        );
    }

    @Test
    default void testEqualsNullIsFalse() {
        this.checkNotEquals(
            this.createObject(),
            null
        );
    }

    @Test
    default void testEqualsDifferentType() {
        this.checkNotEquals(
            this.createObject(),
            new Object()
        );
    }

    @Test
    default void testEqualsSelf() {
        final Object object = this.createObject();
        this.checkEqualsAndHashCode(object, object);
    }

    @Test
    default void testEquals() {
        final Object object1 = this.createObject();
        final Object object2 = this.createObject();
        this.checkEqualsAndHashCode(object1, object2);
    }

    T createObject();

    default void checkEquals(final Object actual) {
        this.checkEquals(this.createObject(), actual);
    }

    default void checkEqualsAndHashCode(final Object actual) {
        this.checkEqualsAndHashCode(this.createObject(), actual);
    }

    default void checkNotEquals(final Object actual) {
        this.checkNotEquals(
            this.createObject(),
            actual
        );
    }
}
