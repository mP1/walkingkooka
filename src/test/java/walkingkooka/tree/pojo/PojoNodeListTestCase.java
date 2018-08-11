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
package walkingkooka.tree.pojo;

import org.junit.Test;
import walkingkooka.test.PackagePrivateClassTestCase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotEquals;

public abstract class PojoNodeListTestCase<L extends List<E>, E> extends PackagePrivateClassTestCase<L> {

    @Test
    public final void testEqualListOfComponents() {
        final List<E> components = this.listOfComponents();
        assertEquals(this.list(components), components);
    }

    @Test
    public final void testEqualListOfComponents2() {
        final List<E> components = this.listOfComponents();
        assertEquals(this.list(components), new ArrayList<>(components));
    }

    @Test
    public final void testEqualsDifferentListOfComponents() {
        assertNotEquals(this.list(this.listOfComponents()), this.listOfDifferentComponents());
    }

    abstract List<E> listOfComponents();

    abstract List<E> listOfDifferentComponents();

    abstract List<E> list(final List<E> components);
}
