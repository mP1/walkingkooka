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

package walkingkooka.compare;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface LowerOrUpperTesting<C extends LowerOrUpper<C>> {

    @Test
    default void testLowerNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createLowerOrUpper().lower(null);
        });
    }

    @Test
    default void testLowerSame() {
        final C object = this.createLowerOrUpper();
        assertSame(object, object.lower(object));
    }

    @Test
    default void testUpperNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createLowerOrUpper().upper(null);
        });
    }

    @Test
    default void testUpperSame() {
        final C object = this.createLowerOrUpper();
        assertSame(object, object.lower(object));
    }

    C createLowerOrUpper();
}
