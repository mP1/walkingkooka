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

package walkingkooka.math;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.visit.Visiting;
import walkingkooka.type.JavaVisibility;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class NumberVisitorTest implements NumberVisitorTesting<NumberVisitor> {

    // BigDecimal.......................................................................................................

    @Test
    public void testAcceptBigDecimal() {
        final StringBuilder b = new StringBuilder();
        final BigDecimal value = BigDecimal.ONE;

        new FakeNumberVisitor() {
            @Override
            protected Visiting startVisit(final Number t) {
                assertSame(value, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Number t) {
                assertSame(value, t);
                b.append("2");
            }

            @Override
            protected void visit(final BigDecimal t) {
                assertSame(value, t);
                b.append("3");
            }
        }.accept(value);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptBigDecimal2() {
        new NumberVisitor() {
        }.accept(BigDecimal.ONE);
    }

    // BigInteger.......................................................................................................

    @Test
    public void testAcceptBigInteger() {
        final StringBuilder b = new StringBuilder();
        final BigInteger value = BigInteger.ONE;

        new FakeNumberVisitor() {
            @Override
            protected Visiting startVisit(final Number t) {
                assertSame(value, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Number t) {
                assertSame(value, t);
                b.append("2");
            }

            @Override
            protected void visit(final BigInteger t) {
                assertSame(value, t);
                b.append("3");
            }
        }.accept(value);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptBigInteger2() {
        new NumberVisitor() {
        }.accept(BigInteger.ONE);
    }

    // Byte.............................................................................................................

    @Test
    public void testAcceptByte() {
        final StringBuilder b = new StringBuilder();
        final Byte value = Byte.MAX_VALUE;

        new FakeNumberVisitor() {
            @Override
            protected Visiting startVisit(final Number t) {
                assertSame(value, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Number t) {
                assertSame(value, t);
                b.append("2");
            }

            @Override
            protected void visit(final Byte t) {
                assertSame(value, t);
                b.append("3");
            }
        }.accept(value);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptByte2() {
        new NumberVisitor() {
        }.accept(Byte.MAX_VALUE);
    }

    // Double...........................................................................................................

    @Test
    public void testAcceptDouble() {
        final StringBuilder b = new StringBuilder();
        final Double value = Double.MAX_VALUE;

        new FakeNumberVisitor() {
            @Override
            protected Visiting startVisit(final Number t) {
                assertSame(value, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Number t) {
                assertSame(value, t);
                b.append("2");
            }

            @Override
            protected void visit(final Double t) {
                assertSame(value, t);
                b.append("3");
            }
        }.accept(value);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptDouble2() {
        new NumberVisitor() {
        }.accept(Double.MAX_VALUE);
    }

    // Float............................................................................................................

    @Test
    public void testAcceptFloat() {
        final StringBuilder b = new StringBuilder();
        final Float value = Float.MAX_VALUE;

        new FakeNumberVisitor() {
            @Override
            protected Visiting startVisit(final Number t) {
                assertSame(value, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Number t) {
                assertSame(value, t);
                b.append("2");
            }

            @Override
            protected void visit(final Float t) {
                assertSame(value, t);
                b.append("3");
            }
        }.accept(value);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptFloat2() {
        new NumberVisitor() {
        }.accept(Float.MAX_VALUE);
    }

    // Integer.........................................................................................................

    @Test
    public void testAcceptInteger() {
        final StringBuilder b = new StringBuilder();
        final Integer value = Integer.MAX_VALUE;

        new FakeNumberVisitor() {
            @Override
            protected Visiting startVisit(final Number t) {
                assertSame(value, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Number t) {
                assertSame(value, t);
                b.append("2");
            }

            @Override
            protected void visit(final Integer t) {
                assertSame(value, t);
                b.append("3");
            }
        }.accept(value);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptInteger2() {
        new NumberVisitor() {
        }.accept(Integer.MAX_VALUE);
    }

    // Long.......................................................................................................

    @Test
    public void testAcceptLong() {
        final StringBuilder b = new StringBuilder();
        final Long value = Long.MAX_VALUE;

        new FakeNumberVisitor() {
            @Override
            protected Visiting startVisit(final Number t) {
                assertSame(value, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Number t) {
                assertSame(value, t);
                b.append("2");
            }

            @Override
            protected void visit(final Long t) {
                assertSame(value, t);
                b.append("3");
            }
        }.accept(value);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptLong2() {
        new NumberVisitor() {
        }.accept(Long.MAX_VALUE);
    }

    // Short............................................................................................................

    @Test
    public void testAcceptShort() {
        final StringBuilder b = new StringBuilder();
        final Short value = Short.MAX_VALUE;

        new FakeNumberVisitor() {
            @Override
            protected Visiting startVisit(final Number t) {
                assertSame(value, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Number t) {
                assertSame(value, t);
                b.append("2");
            }

            @Override
            protected void visit(final Short t) {
                assertSame(value, t);
                b.append("3");
            }
        }.accept(value);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptShort2() {
        new NumberVisitor() {
        }.accept(Short.MAX_VALUE);
    }

    // Unknown..........................................................................................................

    @Test
    public void testAcceptUnknown() {
        final StringBuilder b = new StringBuilder();
        final TestNumber value = new TestNumber();

        new FakeNumberVisitor() {
            @Override
            protected Visiting startVisit(final Number t) {
                assertSame(value, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Number t) {
                assertSame(value, t);
                b.append("2");
            }

            @Override
            protected void visitUnknown(final Number t) {
                assertSame(value, t);
                b.append("3");
            }
        }.accept(value);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptUnknown2() {
        new NumberVisitor() {
        }.accept(new TestNumber());
    }

    static class TestNumber extends Number {

        @Override
        public int intValue() {
            return 1;
        }

        @Override
        public long longValue() {
            return 1;
        }

        @Override
        public float floatValue() {
            return 1;
        }

        @Override
        public double doubleValue() {
            return 1;
        }

        private final static long serialVersionUID = 1L;
    }

    @Override
    public void testCheckToStringOverridden() {
    }

    @Override
    public NumberVisitor createVisitor() {
        return new FakeNumberVisitor();
    }

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Class<NumberVisitor> type() {
        return NumberVisitor.class;
    }
}
