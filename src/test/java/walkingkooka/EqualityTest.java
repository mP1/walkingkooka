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
import walkingkooka.test.PublicStaticHelperTesting;
import walkingkooka.type.JavaVisibility;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class EqualityTest implements PublicStaticHelperTesting<Equality> {

    // safeEquals.......................................................................................................

    @Test
    public void testSafeEqualsNullAndNull() {
        this.safeEqualsAndCheck(null, null, true);
    }

    @Test
    public void testSafeEqualsNullAndNonNull() {
        this.safeEqualsAndCheck(new Object(), null, false);
    }

    @Test
    public void testSafeEqualsNonNullAndNonNull() {
        this.safeEqualsAndCheck("abc123", "abc123", true);
    }

    private void safeEqualsAndCheck(final Object first, final Object second, final boolean expected) {
        assertEquals(expected, Equality.safeEquals(first, second), () -> first + " AND " + second);
        assertEquals(expected, Equality.safeEquals(second, first), () -> first + " AND " + second);
    }

    // safeEqualsCaseInsensitive.......................................................................................................

    @Test
    public void testSafeEqualsCaseInsensitiveNullAndNull() {
        this.safeEqualsCaseInsensitiveAndCheck(null, null, true);
    }

    @Test
    public void testSafeEqualsCaseInsensitiveNullAndNonNull() {
        this.safeEqualsCaseInsensitiveAndCheck("ABC", null, false);
    }

    @Test
    public void testSafeEqualsCaseInsensitiveSameCase() {
        this.safeEqualsCaseInsensitiveAndCheck("abc123", "abc123", true);
    }

    @Test
    public void testSafeEqualsCaseInsensitiveDifferentCase() {
        this.safeEqualsCaseInsensitiveAndCheck("ABC123", "abc123", true);
    }

    private void safeEqualsCaseInsensitiveAndCheck(final String first, final String second, final boolean expected) {
        assertEquals(expected, Equality.safeEqualsCaseInsensitive(first, second), () -> first + " AND " + second);
        assertEquals(expected, Equality.safeEqualsCaseInsensitive(second, first), () -> first + " AND " + second);
    }

    // isAlmostEqualsFloat...............................................................................................

    @Test
    public void testIsAlmostEqualsFloatFail() {
        this.isAlmostEqualsFloatAndCheck(4, 6, 1, false);
    }

    @Test
    public void testIsAlmostEqualsFloatFail2() {
        this.isAlmostEqualsFloatAndCheck(4, -5, 1, false);
    }

    @Test
    public void testIsAlmostEqualsFloatCloseEnough() {
        this.isAlmostEqualsFloatAndCheck(14, 15, 2, true);
    }

    private void isAlmostEqualsFloatAndCheck(final float first,
                                             final float second,
                                             final float epsilon,
                                             final boolean expected) {
        assertEquals(expected, Equality.isAlmostEquals(first, second, epsilon), () -> first + " AND " + second + " epsilon " + epsilon);
        assertEquals(expected, Equality.isAlmostEquals(second, first, epsilon), () -> first + " AND " + second + " epsilon " + epsilon);
    }

    // isAlmostEqualsDouble.............................................................................................

    @Test
    public void testIsAlmostEqualsDoubleFail() {
        this.isAlmostEqualsDoubleAndCheck(4, 6, 1, false);
    }

    @Test
    public void testIsAlmostEqualsDoubleFail2() {
        this.isAlmostEqualsDoubleAndCheck(4, -5, 1, false);
    }

    @Test
    public void testIsAlmostEqualsDoubleCloseEnough() {
        this.isAlmostEqualsDoubleAndCheck(4, 5, 2, true);
    }

    private void isAlmostEqualsDoubleAndCheck(final double first,
                                              final double second,
                                              final double epsilon,
                                              final boolean expected) {
        assertEquals(expected, Equality.isAlmostEquals(first, second, epsilon), () -> first + " AND " + second + " epsilon " + epsilon);
        assertEquals(expected, Equality.isAlmostEquals(second, first, epsilon), () -> first + " AND " + second + " epsilon " + epsilon);
    }

    // helpers..........................................................................................................
    @Override
    public Class<Equality> type() {
        return Equality.class;
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
