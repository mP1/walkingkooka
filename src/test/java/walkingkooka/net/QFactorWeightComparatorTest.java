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
 *
 */

package walkingkooka.net;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.compare.ComparatorTestCase;
import walkingkooka.net.media.MediaType;

import java.util.List;

import static org.junit.Assert.assertEquals;

public final class QFactorWeightComparatorTest extends ComparatorTestCase<QFactorWeightComparator<MediaType>, MediaType> {

    @Test
    public void testLeftHigherQFactor() {
        this.compareAndCheckLess(MediaType.parseOne("a/b;q=1.0"), MediaType.parseOne("c/d;q=0.5"));
    }

    @Test
    public void testLeftDefaultedHigherQFactor() {
        this.compareAndCheckLess(MediaType.parseOne("a/b"), MediaType.parseOne("c/d;q=0.5"));
    }

    @Test
    public void testSecondDefaultedAndHigherQFactor() {
        this.compareAndCheckMore(MediaType.parseOne("a/b;q=0.5"), MediaType.parseOne("c/d"));
    }

    @Test
    public void testListSortedDescending() {
        final MediaType one = MediaType.parseOne("a/b;q=1.0");
        final MediaType half = MediaType.parseOne("c/d;q=0.5");
        final MediaType quarter = MediaType.parseOne("e/f;q=0.25");

        final List<MediaType> list = Lists.array();
        list.add(quarter);
        list.add(one);
        list.add(half);

        list.sort(QFactorWeightComparator.instance());

        assertEquals(Lists.of(one, half, quarter), list);
    }

    @Test
    public void testListSortedDescending2() {
        final MediaType one = MediaType.parseOne("a/b");
        final MediaType half = MediaType.parseOne("c/d;q=0.5");
        final MediaType quarter = MediaType.parseOne("e/f;q=0.25");

        final List<MediaType> list = Lists.array();
        list.add(quarter);
        list.add(one);
        list.add(half);

        list.sort(QFactorWeightComparator.instance());

        assertEquals(Lists.of(one, half, quarter), list);
    }

    @Override
    public void testToString() {
        assertEquals("QFactor", this.createComparator().toString());
    }

    @Override
    protected QFactorWeightComparator createComparator() {
        return QFactorWeightComparator.instance();
    }

    @Override
    protected Class<QFactorWeightComparator<MediaType>> type() {
        return Cast.to(QFactorWeightComparator.class);
    }
}
