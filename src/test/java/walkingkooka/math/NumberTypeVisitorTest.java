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
import walkingkooka.type.JavaVisibility;
import walkingkooka.visit.Visiting;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class NumberTypeVisitorTest implements NumberTypeVisitorTesting<NumberTypeVisitor> {

    // BigDecimal.......................................................................................................

    @Test
    public void testAcceptBigDecimal() {
        final StringBuilder b = new StringBuilder();
        final Class<BigDecimal> type = BigDecimal.class;

        new FakeNumberTypeVisitor() {
            @Override
            protected Visiting startVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("2");
            }

            @Override
            protected void visitBigDecimal() {
                b.append("3");
            }
        }.accept(type);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptBigDecimal2() {
        new NumberTypeVisitor() {
        }.accept(BigDecimal.class);
    }

    // BigInteger.......................................................................................................

    @Test
    public void testAcceptBigInteger() {
        final StringBuilder b = new StringBuilder();
        final Class<BigInteger> type = BigInteger.class;

        new FakeNumberTypeVisitor() {
            @Override
            protected Visiting startVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("2");
            }

            @Override
            protected void visitBigInteger() {
                b.append("3");
            }
        }.accept(type);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptBigInteger2() {
        new NumberTypeVisitor() {
        }.accept(BigInteger.class);
    }

    // Byte............................................................................................................

    @Test
    public void testAcceptByte() {
        final StringBuilder b = new StringBuilder();
        final Class<Byte> type = Byte.class;

        new FakeNumberTypeVisitor() {
            @Override
            protected Visiting startVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("2");
            }

            @Override
            protected void visitByte() {
                b.append("3");
            }
        }.accept(type);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptByte2() {
        new NumberTypeVisitor() {
        }.accept(Byte.class);
    }

    // Double...........................................................................................................

    @Test
    public void testAcceptDouble() {
        final StringBuilder b = new StringBuilder();
        final Class<Double> type = Double.class;

        new FakeNumberTypeVisitor() {
            @Override
            protected Visiting startVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("2");
            }

            @Override
            protected void visitDouble() {
                b.append("3");
            }
        }.accept(type);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptDouble2() {
        new NumberTypeVisitor() {
        }.accept(Double.class);
    }

    // Float...........................................................................................................

    @Test
    public void testAcceptFloat() {
        final StringBuilder b = new StringBuilder();
        final Class<Float> type = Float.class;

        new FakeNumberTypeVisitor() {
            @Override
            protected Visiting startVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("2");
            }

            @Override
            protected void visitFloat() {
                b.append("3");
            }
        }.accept(type);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptFloat2() {
        new NumberTypeVisitor() {
        }.accept(Float.class);
    }

    // Integer...........................................................................................................

    @Test
    public void testAcceptInteger() {
        final StringBuilder b = new StringBuilder();
        final Class<Integer> type = Integer.class;

        new FakeNumberTypeVisitor() {
            @Override
            protected Visiting startVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("2");
            }

            @Override
            protected void visitInteger() {
                b.append("3");
            }
        }.accept(type);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptInteger2() {
        new NumberTypeVisitor() {
        }.accept(Integer.class);
    }

    // Long...........................................................................................................

    @Test
    public void testAcceptLong() {
        final StringBuilder b = new StringBuilder();
        final Class<Long> type = Long.class;

        new FakeNumberTypeVisitor() {
            @Override
            protected Visiting startVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("2");
            }

            @Override
            protected void visitLong() {
                b.append("3");
            }
        }.accept(type);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptLong2() {
        new NumberTypeVisitor() {
        }.accept(Long.class);
    }

    // Short...........................................................................................................

    @Test
    public void testAcceptShort() {
        final StringBuilder b = new StringBuilder();
        final Class<Short> type = Short.class;

        new FakeNumberTypeVisitor() {
            @Override
            protected Visiting startVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("2");
            }

            @Override
            protected void visitShort() {
                b.append("3");
            }
        }.accept(type);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptShort2() {
        new NumberTypeVisitor() {
        }.accept(Short.class);
    }

    // Unknown..........................................................................................................

    @Test
    public void testAcceptUnknown() {
        final StringBuilder b = new StringBuilder();
        final Class<TestNumber> type = TestNumber.class;

        new FakeNumberTypeVisitor() {
            @Override
            protected Visiting startVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Class<?> t) {
                assertSame(type, t);
                b.append("2");
            }

            @Override
            protected void visitUnknown(final Class<?> t) {
                assertSame(type, t);
                b.append("3");
            }
        }.accept(type);
        assertEquals("132", b.toString());
    }

    @Test
    public void testAcceptUnknown2() {
        new NumberTypeVisitor() {
        }.accept(TestNumber.class);
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
    public NumberTypeVisitor createVisitor() {
        return new FakeNumberTypeVisitor();
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNamePrefix() {
        return "";
    }

    // ClassTesting.....................................................................................................

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Class<NumberTypeVisitor> type() {
        return NumberTypeVisitor.class;
    }
}
