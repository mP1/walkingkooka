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

package walkingkooka.net;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.compare.ComparatorTesting;
import walkingkooka.net.header.MediaType;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

public final class QFactorWeightComparatorTest implements ClassTesting2<QFactorWeightComparator<MediaType>>,
        ComparatorTesting<QFactorWeightComparator<MediaType>, MediaType> {

    @Test
    public void testLeftHigherQFactor() {
        this.compareAndCheckLess(MediaType.parse("a/b;q=1.0"), MediaType.parse("c/d;q=0.5"));
    }

    @Test
    public void testLeftDefaultedHigherQFactor() {
        this.compareAndCheckLess(MediaType.parse("a/b"), MediaType.parse("c/d;q=0.5"));
    }

    @Test
    public void testSecondDefaultedAndHigherQFactor() {
        this.compareAndCheckMore(MediaType.parse("a/b;q=0.5"), MediaType.parse("c/d"));
    }

    @Test
    public void testArraySortedDescending() {
        final MediaType one = MediaType.parse("a/b;q=1.0");
        final MediaType half = MediaType.parse("c/d;q=0.5");
        final MediaType quarter = MediaType.parse("e/f;q=0.25");

        this.comparatorArraySortAndCheck(half, one, quarter,
                one, half, quarter);
    }

    @Test
    public void testListSortedDescending2() {
        final MediaType one = MediaType.parse("a/b");
        final MediaType half = MediaType.parse("c/d;q=0.5");
        final MediaType quarter = MediaType.parse("e/f;q=0.25");
        final MediaType eighth = MediaType.parse("g/h;q=0.125");

        this.comparatorArraySortAndCheck(half, eighth, one, quarter,
                one, half, quarter, eighth);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createComparator(), "QFactor");
    }

    @Override
    public QFactorWeightComparator<MediaType> createComparator() {
        return QFactorWeightComparator.instance();
    }

    @Override
    public Class<QFactorWeightComparator<MediaType>> type() {
        return Cast.to(QFactorWeightComparator.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
