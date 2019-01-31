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

package walkingkooka.text;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.compare.ComparatorTestCase;

import static org.junit.jupiter.api.Assertions.assertEquals;

final public class CaseSensitivityComparatorTest
        extends ComparatorTestCase<CaseSensitivityComparator<CharSequence>, CharSequence> {

    // constants

    private final static CaseSensitivityComparator<CharSequence> SENSITIVE
            = CaseSensitivityComparator.sensitive();

    private final static CaseSensitivityComparator<CharSequence> INSENSITIVE
            = CaseSensitivityComparator.insensitive();

    // SENSITIVE

    @Test
    public void testSensitiveSame() {
        this.compareAndCheckEqual(SENSITIVE, "apple", "apple");
    }

    @Test
    public void testSensitiveSameButDifferentCase() {
        this.compareAndCheckMore(SENSITIVE, "apple", "APPLE");
    }

    @Test
    public void testSensitiveSameButDifferentCase2() {
        this.compareAndCheckLess(SENSITIVE, "APPLE", "apple");
    }

    @Test
    public void testSensitiveLess() {
        this.compareAndCheckLess(SENSITIVE, "apple", "banana");
    }

    @Test
    public void testSensitiveLessDifferentCase() {
        this.compareAndCheckMore(SENSITIVE, "apple", "BANANA");
    }

    @Test
    public void testSensitiveLessDifferentCase2() {
        this.compareAndCheckLess(SENSITIVE, "APPLE", "banana");
    }

    @Test
    public void testSensitiveMore() {
        this.compareAndCheckMore(SENSITIVE, "zebra", "apple");
    }

    @Test
    public void testSensitiveMoreDifferentCase() {
        this.compareAndCheckLess(SENSITIVE, "ZEBRA", "apple");
    }

    @Test
    public void testSensitiveStartsWith() {
        this.compareAndCheckLess(SENSITIVE, "apple", "apple-banana");
    }

    @Test
    public void testSensitiveStartsWithDifferentCase() {
        this.compareAndCheckMore(SENSITIVE, "apple", "APPLE-BANANA");
    }

    // INSENSITIVE

    @Test
    public void testInsensitiveSame() {
        this.compareAndCheckEqual(INSENSITIVE, "apple", "apple");
    }

    @Test
    public void testInsensitiveSameButDifferentCase() {
        this.compareAndCheckEqual(INSENSITIVE, "apple", "APPLE");
    }

    @Test
    public void testInsensitiveLess() {
        this.compareAndCheckLess(INSENSITIVE, "apple", "banana");
    }

    @Test
    public void testInsensitiveLessDifferentCase() {
        this.compareAndCheckLess(INSENSITIVE, "apple", "BANANA");
    }

    @Test
    public void testInsensitiveMore() {
        this.compareAndCheckMore(INSENSITIVE, "zebra", "apple");
    }

    @Test
    public void testInsensitiveMoreDifferentCase() {
        this.compareAndCheckMore(INSENSITIVE, "ZEBRA", "apple");
    }

    @Test
    public void testInsensitiveStartsWith() {
        this.compareAndCheckLess(INSENSITIVE, "apple", "apple-banana");
    }

    @Test
    public void testInsensitiveStartsWithDifferentCase() {
        this.compareAndCheckLess(INSENSITIVE, "apple", "APPLE-BANANA");
    }

    @Test
    public void testSameCharactersDifferentType() {
        this.compareAndCheckEqual(INSENSITIVE, "apple", new StringBuilder("apple"));
    }

    @Test
    public void testSameCharactersDifferentTypeDifferentCase() {
        this.compareAndCheckEqual(INSENSITIVE, "apple", new StringBuilder("APPLE"));
    }

    // toString

    @Test
    public void testToString() {
        assertEquals("SENSITIVE", SENSITIVE.toString());
    }

    @Test
    public void testToString2() {
        assertEquals("INSENSITIVE", INSENSITIVE.toString());
    }

    @Override
    protected CaseSensitivityComparator<CharSequence> createComparator() {
        return SENSITIVE;
    }

    @Override
    protected Class<CaseSensitivityComparator<CharSequence>> type() {
        return Cast.to(CaseSensitivityComparator.class);
    }
}
