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
import walkingkooka.collect.set.Sets;
import walkingkooka.compare.ComparableTestCase;

import java.util.Iterator;
import java.util.Set;

public final class IpPortComparableTest extends ComparableTestCase<IpPort> {

    @Test
    public void testLess() {
        this.compareToAndCheckLess(IpPort.HTTPS);
    }

    @Test
    public void testInSortedSet() {
        final IpPort one = IpPort.with(1);
        final IpPort two = IpPort.with(2);
        final IpPort three = IpPort.with(3);

        final Set<IpPort> set = Sets.sorted();
        set.add(one);
        set.add(two);
        set.add(three);

        final Iterator<IpPort> values = set.iterator();
        assertEquals(one, values.next());
        assertEquals(two, values.next());
        assertEquals(three, values.next());
    }

    @Override
    protected IpPort createComparable() {
        return IpPort.HTTP;
    }

    @Override
    protected Class<IpPort> type() {
        return IpPort.class;
    }
}
