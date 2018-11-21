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

package walkingkooka.net.http;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.compare.Range;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

public final class HttpHeaderRangeEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<HttpHeaderRange> {

    private final static String UNIT = "bytes";

    @Test
    public void testDifferentUnit() {
        this.checkNotEquals(this.range("different", this.range()));
    }

    @Test
    public void testDifferentRanges() {
        this.checkNotEquals(this.range(UNIT, Range.greaterThan(456L)));
    }

    @Override
    protected HttpHeaderRange createObject() {
        return this.range(UNIT, this.range());
    }

    private Range<Long> range() {
        return Range.greaterThan(123L);
    }

    @SafeVarargs
    private final HttpHeaderRange range(final String unit, final Range<Long>... ranges) {
        return HttpHeaderRange.with(unit, Lists.of(ranges));
    }
}
