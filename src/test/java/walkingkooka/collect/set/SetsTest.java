/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.collect.set;

import org.junit.jupiter.api.Test;
import walkingkooka.test.PublicStaticHelperTestCase;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class SetsTest extends PublicStaticHelperTestCase<Sets> {

    // tests

    @Test
    public void testOfNullFails() {
        assertThrows(NullPointerException.class, () -> {
            Sets.of((Object[]) null);
        });
    }

    @Test
    public void testAs() {
        assertEquals(Sets.empty(), Sets.of());
    }

    @Test
    public void testAsOneElement() {
        assertEquals(Sets.of("element"), Sets.of("element"));
    }

    @Test
    public void testHashSetWithManyElements() {
        final Object object1 = "first";
        final Object object2 = "second";
        final Object object3 = "third";

        final Set<Object> hashSet = Sets.hash();
        hashSet.add(object1);
        hashSet.add(object2);
        hashSet.add(object3);
        assertEquals(hashSet, Sets.of(object1, object2, object3));
    }

    // helpers

    @Override
    public Class<Sets> type() {
        return Sets.class;
    }

    @Override
    protected boolean canHavePublicTypes(final Method method) {
        return false;
    }
}
