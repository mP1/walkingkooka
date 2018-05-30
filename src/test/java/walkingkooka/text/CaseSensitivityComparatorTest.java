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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.compare.ComparatorTestCase;

final public class CaseSensitivityComparatorTest
        extends ComparatorTestCase<CaseSensitivityComparator<CharSequence>, CharSequence> {

    // constants

    private final CaseSensitivityComparator<CharSequence> SENSITIVE
            = CaseSensitivityComparator.sensitive();

    private final CaseSensitivityComparator<CharSequence> INSENSITIVE
            = CaseSensitivityComparator.insensitive();

    // SENSITIVE

    @Test
    public void testSensitiveSame() {
        this.compareAndCheckEqual(this.SENSITIVE, "apple", "apple");
    }

    @Test
    public void testSensitiveSameButDifferentCase() {
        this.compareAndCheckMore(this.SENSITIVE, "apple", "APPLE");
    }

    @Test
    public void testSensitiveSameButDifferentCase2() {
        this.compareAndCheckLess(this.SENSITIVE, "APPLE", "apple");
    }

    @Test
    public void testSensitiveLess() {
        this.compareAndCheckLess(this.SENSITIVE, "apple", "banana");
    }

    @Test
    public void testSensitiveLessDifferentCase() {
        this.compareAndCheckMore(this.SENSITIVE, "apple", "BANANA");
    }

    @Test
    public void testSensitiveLessDifferentCase2() {
        this.compareAndCheckLess(this.SENSITIVE, "APPLE", "banana");
    }

    @Test
    public void testSensitiveMore() {
        this.compareAndCheckMore(this.SENSITIVE, "zebra", "apple");
    }

    @Test
    public void testSensitiveMoreDifferentCase() {
        this.compareAndCheckLess(this.SENSITIVE, "ZEBRA", "apple");
    }

    @Test
    public void testSensitiveStartsWith() {
        this.compareAndCheckLess(this.SENSITIVE, "apple", "apple-banana");
    }

    @Test
    public void testSensitiveStartsWithDifferentCase() {
        this.compareAndCheckMore(this.SENSITIVE, "apple", "APPLE-BANANA");
    }

    // INSENSITIVE

    @Test
    public void testInsensitiveSame() {
        this.compareAndCheckEqual(this.INSENSITIVE, "apple", "apple");
    }

    @Test
    public void testInsensitiveSameButDifferentCase() {
        this.compareAndCheckEqual(this.INSENSITIVE, "apple", "APPLE");
    }

    @Test
    public void testInsensitiveLess() {
        this.compareAndCheckLess(this.INSENSITIVE, "apple", "banana");
    }

    @Test
    public void testInsensitiveLessDifferentCase() {
        this.compareAndCheckLess(this.INSENSITIVE, "apple", "BANANA");
    }

    @Test
    public void testInsensitiveMore() {
        this.compareAndCheckMore(this.INSENSITIVE, "zebra", "apple");
    }

    @Test
    public void testInsensitiveMoreDifferentCase() {
        this.compareAndCheckMore(this.INSENSITIVE, "ZEBRA", "apple");
    }

    @Test
    public void testInsensitiveStartsWith() {
        this.compareAndCheckLess(this.INSENSITIVE, "apple", "apple-banana");
    }

    @Test
    public void testInsensitiveStartsWithDifferentCase() {
        this.compareAndCheckLess(this.INSENSITIVE, "apple", "APPLE-BANANA");
    }

    @Test
    public void testSameCharactersDifferentType() {
        this.compareAndCheckEqual(this.INSENSITIVE, "apple", new StringBuilder("apple"));
    }

    @Test
    public void testSameCharactersDifferentTypeDifferentCase() {
        this.compareAndCheckEqual(this.INSENSITIVE, "apple", new StringBuilder("APPLE"));
    }

    @Override
    protected CaseSensitivityComparator<CharSequence> createComparator() {
        return this.SENSITIVE;
    }

    @Override
    protected Class<CaseSensitivityComparator<CharSequence>> type() {
        return Cast.to(CaseSensitivityComparator.class);
    }
}
