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
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ListsTest implements PublicStaticHelperTesting<Lists>,
    IteratorTesting {

    @Test
    public void testArray() {
        final List<String> list = Lists.array();
        this.isImmutableAndCheck(list, false);
        this.checkEquals(ArrayList.class, list.getClass(), "list class");

        list.add("1a");
    }

    @Test
    public void testImmutableNullListFails() {
        assertThrows(NullPointerException.class, () -> ImmutableListImpl.with(null));
    }

    @Test
    public void testImmutable() {
        final List<String> list = Lists.array();
        list.add("1a");
        list.add("2b");

        final List<String> immutable = Lists.immutable(list);
        this.isImmutableAndCheck(immutable, true);
    }

    @Test
    public void testImmutableDefensiveCopy() {
        final List<String> list = Lists.array();
        list.add("1a");
        list.add("2b");

        final List<String> immutable = Lists.immutable(list);
        this.isImmutableAndCheck(immutable, true);

        list.clear();

        this.checkEquals(Lists.of("1a", "2b"), immutable, "defensive copy not taken");
    }

    @Test
    public void testImmutableEmpty() {
        final List<?> empty = Lists.empty();
        final List<?> immutable = Lists.immutable(empty);
        this.isImmutableAndCheck(immutable, true);
        assertSame(empty, immutable);
    }

    @Test
    public void testLinked() {
        final List<String> list = Lists.linkedList();
        this.isImmutableAndCheck(list, false);
        this.checkEquals(LinkedList.class, list.getClass(), "list class");

        list.add("1a");
    }

    @Test
    public void testOfVarArgsNullArrayFails() {
        assertThrows(NullPointerException.class, () -> Lists.of((Object[]) null));
    }

    @Test
    public void testOfVarArgs() {
        final List<String> immutable = Lists.of("A1", "B2");
        this.isImmutableAndCheck(immutable, true);

        this.iterateAndCheck(immutable.iterator(), "A1", "B2");
    }

    @Test
    public void testOfVarArgs2() {
        final String a = "a1";
        final String b = "b2";
        final String[] array = new String[]{a, b};

        final List<String> immutable = Lists.of(array);
        this.isImmutableAndCheck(immutable, true);
        this.checkEquals(Lists.of(a, b), immutable);
    }

    @Test
    public void testOfVarArgsNullElements() {
        final String a = null;
        final String b = "b2";
        final String[] array = new String[]{a, b};

        final List<String> immutable = Lists.of(array);
        this.isImmutableAndCheck(immutable, true);
        this.checkEquals(Lists.of(a, b), immutable);
    }

    @Test
    public void testOfVarArgsDefensiveCopy() {
        final String a = "a1";
        final String b = "b2";
        final String[] array = new String[]{a, b};

        final List<String> immutable = Lists.of(array);
        this.isImmutableAndCheck(immutable, true);

        array[0] = "*";
        array[1] = "!";

        this.checkEquals(Lists.of(a, b), immutable, "defensive copy not taken");
    }

    @Test
    public void testReadOnly() {
        final List<String> list = Lists.array();
        list.add("A1");
        list.add("B2");
        this.isImmutableAndCheck(list, false);

        final List<?> readOnly = Lists.readOnly(list);
        assertNotSame(list, readOnly);

        this.isImmutableAndCheck(readOnly, false); // readonly isnt the same as immutable(defensive copy not taken).
    }

    @Test
    public void testVector() {
        final Vector<?> vector = Lists.vector();
        this.checkEquals(Vector.class, vector.getClass(), "vector class");
    }

    private void isImmutableAndCheck(final List<?> list,
                                     final boolean expected) {
        this.checkEquals(
            expected,
            list instanceof ImmutableList,
            () -> "isImmutable " + list.getClass().getName() + "=" + list);
    }

    @Override
    public Class<Lists> type() {
        return Lists.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
