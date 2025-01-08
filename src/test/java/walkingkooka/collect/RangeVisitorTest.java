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

package walkingkooka.collect;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.visit.Visiting;
import walkingkooka.visit.VisitorTesting;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class RangeVisitorTest implements VisitorTesting<RangeVisitor<Integer>, Range<Integer>> {

    private final static Integer LOWER = 44;
    private final static Integer UPPER = 55;

    @Test
    public void testStartVisitAllSkip() {
        final StringBuilder visited = new StringBuilder();
        final Range<Integer> range = Range.all();

        new FakeRangeVisitor<Integer>() {
            @Override
            protected Visiting startVisit(final Range<Integer> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final Range<Integer> range) {
                visited.append("2");
            }
        }.accept(range);

        this.checkEquals(visited.toString(), "12");
    }

    @Test
    public void testAll() {
        final StringBuilder visited = new StringBuilder();
        final Range<Integer> range = Range.all();

        new FakeRangeVisitor<Integer>() {

            @Override
            protected Visiting startVisit(final Range<Integer> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void all() {
                visited.append("2");
            }

            @Override
            protected void endVisit(final Range<Integer> range) {
                visited.append("3");
            }
        }.accept(range);

        this.checkEquals(visited.toString(), "123");
    }

    @Test
    public void testAll2() {
        new RangeVisitor<Integer>() {
        }.accept(Range.all());
    }

    @Test
    public void testStartVisitSingletonSkip() {
        final StringBuilder visited = new StringBuilder();
        final Range<Integer> range = Range.singleton(99);

        new FakeRangeVisitor<Integer>() {
            @Override
            protected Visiting startVisit(final Range<Integer> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final Range<Integer> range) {
                visited.append("2");
            }
        }.accept(range);

        this.checkEquals(visited.toString(), "12");
    }

    @Test
    public void testSingleton() {
        final StringBuilder visited = new StringBuilder();
        final Range<Integer> range = Range.singleton(99);

        new FakeRangeVisitor<Integer>() {

            @Override
            protected Visiting startVisit(final Range<Integer> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void singleton(final Integer value) {
                checkEquals(
                    Integer.valueOf(99),
                    value,
                    "value"
                );
                visited.append("2");
            }

            @Override
            protected void endVisit(final Range<Integer> range) {
                visited.append("3");
            }
        }.accept(range);

        this.checkEquals(visited.toString(), "123");
    }

    @Test
    public void testSingleton2() {
        new RangeVisitor<Integer>() {
        }.accept(Range.singleton(99));
    }

    @Test
    public void testStartVisitGreaterThanSkip() {
        final StringBuilder visited = new StringBuilder();
        final Range<Integer> range = Range.greaterThan(LOWER);

        new FakeRangeVisitor<Integer>() {
            @Override
            protected Visiting startVisit(final Range<Integer> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final Range<Integer> range) {
                visited.append("2");
            }
        }.accept(range);

        this.checkEquals(visited.toString(), "12");
    }

    @Test
    public void testStartBetweenSkip() {
        final StringBuilder visited = new StringBuilder();
        final Range<Integer> range = Range.greaterThanEquals(LOWER);

        new FakeRangeVisitor<Integer>() {

            @Override
            protected Visiting startVisit(final Range<Integer> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected Visiting startBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("2");
                return Visiting.SKIP;
            }

            @Override
            protected void endBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("3");
            }

            @Override
            protected void endVisit(final Range<Integer> range) {
                visited.append("4");
            }
        }.accept(range);

        this.checkEquals(visited.toString(), "1234");
    }

    @Test
    public void testGreaterThan() {
        final StringBuilder visited = new StringBuilder();
        final Range<Integer> range = Range.greaterThan(LOWER);

        new FakeRangeVisitor<Integer>() {

            @Override
            protected Visiting startVisit(final Range<Integer> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected Visiting startBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("2");
                return Visiting.CONTINUE;
            }

            @Override
            protected void lowerBoundExclusive(final Integer value) {
                assertSame(LOWER, value, "lowerBoundExclusive");
                visited.append("3");
            }

            @Override
            protected void upperBoundAll() {
                visited.append("4");
            }

            @Override
            protected void endBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("5");
            }

            @Override
            protected void endVisit(final Range<Integer> range) {
                visited.append("6");
                super.endVisit(range);
            }
        }.accept(range);

        this.checkEquals(visited.toString(), "123456");
    }

    @Test
    public void testGreaterThan2() {
        new RangeVisitor<Integer>() {
        }.accept(Range.greaterThan(LOWER));
    }

    @Test
    public void testGreaterThanEquals() {
        final StringBuilder visited = new StringBuilder();
        final Range<Integer> range = Range.greaterThanEquals(LOWER);

        new FakeRangeVisitor<Integer>() {

            @Override
            protected Visiting startVisit(final Range<Integer> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected Visiting startBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("2");
                return Visiting.CONTINUE;
            }

            @Override
            protected void lowerBoundInclusive(final Integer value) {
                assertSame(LOWER, value, "lowerBoundInclusive");
                visited.append("3");
            }

            @Override
            protected void upperBoundAll() {
                visited.append("4");
            }

            @Override
            protected void endBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("5");
            }

            @Override
            protected void endVisit(final Range<Integer> range) {
                visited.append("6");
            }
        }.accept(range);

        this.checkEquals(visited.toString(), "123456");
    }

    @Test
    public void testGreaterThanEquals2() {
        new RangeVisitor<Integer>() {
        }.accept(Range.greaterThanEquals(LOWER));
    }

    @Test
    public void testLessThan() {
        final StringBuilder visited = new StringBuilder();
        final Range<Integer> range = Range.lessThan(UPPER);

        new FakeRangeVisitor<Integer>() {

            @Override
            protected Visiting startVisit(final Range<Integer> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected Visiting startBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("2");
                return Visiting.CONTINUE;
            }

            @Override
            protected void lowerBoundAll() {
                visited.append("3");
            }

            @Override
            protected void upperBoundExclusive(final Integer value) {
                assertSame(UPPER, value, "upperBoundExclusive");
                visited.append("4");
            }

            @Override
            protected void endBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("5");
            }

            @Override
            protected void endVisit(final Range<Integer> range) {
                visited.append("6");
            }
        }.accept(range);

        this.checkEquals(visited.toString(), "123456");
    }

    @Test
    public void testLessThan2() {
        new RangeVisitor<Integer>() {
        }.accept(Range.lessThan(UPPER));
    }

    @Test
    public void testLessThanEqual() {
        final StringBuilder visited = new StringBuilder();
        final Range<Integer> range = Range.lessThanEquals(UPPER);

        new FakeRangeVisitor<Integer>() {

            @Override
            protected Visiting startVisit(final Range<Integer> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected Visiting startBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("2");
                return Visiting.CONTINUE;
            }

            @Override
            protected void lowerBoundAll() {
                visited.append("3");
            }

            @Override
            protected void upperBoundInclusive(final Integer value) {
                assertSame(UPPER, value, "upperBoundIn clusive");
                visited.append("4");
            }

            @Override
            protected void endBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("5");
            }

            @Override
            protected void endVisit(final Range<Integer> range) {
                visited.append("6");
            }
        }.accept(range);

        this.checkEquals(visited.toString(), "123456");
    }

    @Test
    public void testLessThanEquals2() {
        new RangeVisitor<Integer>() {
        }.accept(Range.lessThanEquals(UPPER));
    }

    @Test
    public void testBetweenExclusiveExclusive() {
        final StringBuilder visited = new StringBuilder();
        final Range<Integer> range = Range.greaterThanEquals(LOWER).and(Range.lessThanEquals(UPPER));

        new FakeRangeVisitor<Integer>() {

            @Override
            protected Visiting startVisit(final Range<Integer> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected Visiting startBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("2");
                return Visiting.CONTINUE;
            }

            @Override
            protected void lowerBoundInclusive(final Integer value) {
                assertSame(LOWER, value, "lowerBoundInclusive");
                visited.append("3");
            }

            @Override
            protected void upperBoundInclusive(final Integer value) {
                assertSame(UPPER, value, "upperBoundInclusive");
                visited.append("4");
            }

            @Override
            protected void endBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("5");
            }

            @Override
            protected void endVisit(final Range<Integer> range) {
                visited.append("6");
            }
        }.accept(range);

        this.checkEquals(visited.toString(), "123456");
    }

    @Test
    public void testBetweenExclusiveExclusive2() {
        new RangeVisitor<Integer>() {
        }.accept(Range.greaterThanEquals(LOWER).and(Range.lessThanEquals(UPPER)));
    }

    @Test
    public void testBetweenInclusiveInclusive() {
        final StringBuilder visited = new StringBuilder();
        final Range<Integer> range = Range.greaterThan(LOWER).and(Range.lessThan(UPPER));

        new FakeRangeVisitor<Integer>() {

            @Override
            protected Visiting startVisit(final Range<Integer> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected Visiting startBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("2");
                return Visiting.CONTINUE;
            }

            @Override
            protected void lowerBoundExclusive(final Integer value) {
                assertSame(LOWER, value, "lowerBoundExclusive");
                visited.append("3");
            }

            @Override
            protected void upperBoundExclusive(final Integer value) {
                assertSame(UPPER, value, "upperBoundExclusive");
                visited.append("4");
            }

            @Override
            protected void endBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("5");
            }

            @Override
            protected void endVisit(final Range<Integer> range) {
                visited.append("6");
            }
        }.accept(range);

        this.checkEquals(visited.toString(), "123456");
    }

    @Test
    public void testBetweenInclusiveInclusive2() {
        new RangeVisitor<Integer>() {
        }.accept(Range.greaterThan(LOWER).and(Range.lessThan(UPPER)));
    }

    private void check(final Range<Integer> range,
                       final RangeBound<Integer> lowerBound,
                       final RangeBound<Integer> upperBound) {
        assertSame(lowerBound, range.lower, "lowerBound");
        assertSame(upperBound, range.upper, "upperBound");
    }

    @Override
    public void testCheckToStringOverridden() {
        // ignore
    }

    @Override
    public RangeVisitor<Integer> createVisitor() {
        return new FakeRangeVisitor<>() {

            @Override
            public String toString() {
                return RangeVisitorTest.this.toString();
            }
        };
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public String typeNameSuffix() {
        return RangeVisitor.class.getSimpleName();
    }

    @Override
    public Class<RangeVisitor<Integer>> type() {
        return Cast.to(RangeVisitor.class);
    }
}
