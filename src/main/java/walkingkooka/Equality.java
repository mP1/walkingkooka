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

import walkingkooka.type.PublicStaticHelper;

/**
 * Helpers that assist with testing values and objects for equality.
 */
final public class Equality implements PublicStaticHelper {

    /**
     * Null safe equals test between two {@link Object objects}.
     */
    public static boolean safeEquals(final Object first, final Object second) {
        return null == first ? null == second : first.equals(second);
    }

    /**
     * Null safe equals ignoring case test between two {@link Object objects}.
     */
    public static boolean safeEqualsCaseInsensitive(final String first, final String second) {
        return null == first ? null == second : first.equalsIgnoreCase(second);
    }

    /**
     * Tests if two floats are almost equal using the epsilon.
     */
    public static boolean isAlmostEquals(final float value, final float otherValue,
                                         final float epsilon) {
        return ((value - epsilon) <= otherValue) && ((value + epsilon) >= otherValue);
    }

    /**
     * Tests if two doubles are almost equal using the epsilon.
     */
    public static boolean isAlmostEquals(final double value, final double otherValue,
                                         final double epsilon) {
        return ((value - epsilon) <= otherValue) && ((value + epsilon) >= otherValue);
    }

    /**
     * Stop creation
     */
    private Equality() {
        throw new UnsupportedOperationException();
    }
}
