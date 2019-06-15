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
package walkingkooka.tree.pojo;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.ListTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public abstract class PojoNodeListTestCase<L extends List<E>, E> implements ClassTesting2<L>,
        ListTesting<L, E> {

    PojoNodeListTestCase() {
        super();
    }

    @Test
    public final void testEqualListOfComponents() {
        final List<E> components = this.components();
        assertEquals(this.createList(components), components);
    }

    @Test
    public final void testEqualListOfComponents2() {
        final List<E> components = this.components();
        assertEquals(this.createList(components), new ArrayList<>(components));
    }

    @Test
    public final void testEqualsDifferentListOfComponents() {
        assertNotEquals(this.createList(this.components()), this.differentComponents());
    }

    @Override
    public final L createList() {
        return Cast.to(this.createList(this.components()));
    }

    abstract List<E> createList(final List<E> components);

    abstract List<E> components();

    abstract List<E> differentComponents();

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
