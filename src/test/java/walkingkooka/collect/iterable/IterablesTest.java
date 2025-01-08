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

package walkingkooka.collect.iterable;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;

import java.lang.reflect.Method;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class IterablesTest implements PublicStaticHelperTesting<Iterables> {

    private final static BiPredicate<String, String> EQUIVALENCY = String::equalsIgnoreCase;
    private final static String A = "a";
    private final static String B = "b";
    private final static String C = "c";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testEqualsNullIterableFails() {
        assertThrows(NullPointerException.class, () -> Iterables.equals(null, Iterables.fake(), EQUIVALENCY));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testEqualsNullOtherIterableFails() {
        assertThrows(NullPointerException.class, () -> Iterables.equals(Iterables.fake(), null, EQUIVALENCY));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testEqualsNullEquivalenceFunctionFails() {
        assertThrows(NullPointerException.class, () -> Iterables.equals(Iterables.fake(), Iterables.fake(), null));
    }

    @Test
    public void testEqualsEmpty() {
        this.notEqualsAndCheck();
    }

    @Test
    public void testEqualsDifferent() {
        this.notEqualsAndCheck("different", B, C);
    }

    @Test
    public void testEqualsDifferent2() {
        this.notEqualsAndCheck("different");
    }

    @Test
    public void testEqualsDifferent3() {
        this.notEqualsAndCheck(A, B, "different");
    }

    @Test
    public void testEqualsShorter() {
        this.notEqualsAndCheck(A, B);
    }

    @Test
    public void testEqualsLonger() {
        this.notEqualsAndCheck(A, B, C, "Z");
    }

    @Test
    public void testEquals() {
        this.equalsAndCheck(A, B, "C");
    }

    @Test
    public void testEqualsBothEmpty() {
        this.equalsAndCheck0(Lists.empty(), Lists.empty(), true);
    }

    private void equalsAndCheck(final String... other) {
        this.equalsAndCheck0(Lists.of(other), true);
    }

    private void notEqualsAndCheck(final String... other) {
        this.equalsAndCheck0(Lists.of(other), false);
    }

    private void equalsAndCheck0(final Iterable<String> other, final boolean equals) {
        equalsAndCheck0(Lists.of(A, B, C), other, equals);
    }

    private void equalsAndCheck0(final Iterable<String> iterable, final Iterable<String> other, final boolean equals) {
        this.checkEquals(equals, Iterables.equals(iterable, other, EQUIVALENCY), iterable + " AND " + other);
    }

    @Override
    public Class<Iterables> type() {
        return Iterables.class;
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
