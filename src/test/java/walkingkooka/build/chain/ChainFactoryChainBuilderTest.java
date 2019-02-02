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

package walkingkooka.build.chain;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.build.BuilderException;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.Fake;
import walkingkooka.type.MemberVisibility;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ChainFactoryChainBuilderTest
        extends ChainBuilderTestCase<ChainFactoryChainBuilder<Fake>, Fake> {

    // constants
    private final static ChainType TYPE = ChainType.ALL;

    private final static ChainFactory<Fake> FACTORY = new ChainFactory<Fake>() {

        @Override
        public Fake create(final Object[] chained) {
            final Fake[] array = new Fake[chained.length];
            System.arraycopy(chained, 0, array, 0, chained.length);
            return new FakeChain(array);
        }
    };

    private final static Fake A = createFake(1);

    private final static Fake B = createFake(2);

    private final static Fake C = createFake(3);

    // tests
    @Test
    public void testWithNullChainTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            ChainFactoryChainBuilder.with(null, FACTORY);
        });
    }

    @Test
    public void testWithNullChainFactoryFails() {
        assertThrows(NullPointerException.class, () -> {
            ChainFactoryChainBuilder.with(TYPE, null);
        });
    }

    @Test
    public void testWith() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertEquals(FACTORY, builder.factory, "factory");
        assertEquals(Sets.empty(), builder.all, "all");
    }

    @Test
    public void testAddOneAndBuild() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(A));
        assertSame(A, builder.build());
    }

    @Test
    public void testAddOneThenDuplicateIgnoredAndBuild() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(A));
        assertSame(builder, builder.add(A));
        assertSame(A, builder.build());
    }

    @Test
    public void testAdd() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(A));
        assertSame(builder, builder.add(B));
        assertSame(builder, builder.add(C));
        assertEquals(Sets.of(A,
                        B,
                        C),
                builder.all,
                "all");
    }

    @Test
    public void testAddDuplicateIgnored() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(A));
        assertSame(builder, builder.add(B));
        assertSame(builder, builder.add(C));
        assertSame(builder, builder.add(A));
        assertEquals(Sets.of(A,
                        B,
                        C),
                builder.all,
                "all");
    }

    @Test
    public void testAddDifferentChained() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(ChainType.with("different"),
                A,
                B);
        assertSame(builder, builder.add(chain));
        assertEquals(Sets.of(chain), builder.all, "all should only contain of not its items");
    }

    @Test
    public void testAddChained() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(A,
                B);
        assertSame(builder, builder.add(chain));

        assertEquals(chain, builder.build(), "builder product");
    }

    @Test
    public void testAddChainedDuplicateIgnored() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(A,
                B);
        assertSame(builder, builder.add(chain));
        assertSame(builder, builder.add(A));

        assertSame(chain, builder.build(), "builder product");
    }

    @Test
    public void testAddChainedDuplicateIgnored2() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(A,
                B);
        assertSame(builder, builder.add(chain));
        assertSame(builder, builder.add(B));

        assertSame(chain, builder.build(), "builder product");
    }

    @Test
    public void testAddChainedDuplicateIgnored3() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(A,
                B);
        assertSame(builder, builder.add(chain));
        assertSame(builder, builder.add(chain));

        assertSame(chain, builder.build(), "builder product");
    }

    @Test
    public void testAddChainedDuplicateIgnored4() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(A,
                B);
        assertSame(builder, builder.add(chain));
        assertSame(builder,
                builder.add(new FakeChain(B,
                        A)));

        assertSame(chain, builder.build(), "builder product");
    }

    @Test
    public void testAddChained2() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(A));
        assertSame(builder,
                builder.add(new FakeChain(B,
                        C)));

        assertEquals(Sets.of(A,
                        B,
                        C),
                builder.all,
                "all");
    }

    @Test
    public void testAddChainedDuplicatesIgnored2() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(A));
        assertSame(builder,
                builder.add(new FakeChain(A,
                        A,
                        B,
                        B,
                        C)));

        assertEquals(Sets.of(A,
                        B,
                        C),
                builder.all,
                "all");
    }

    @Override
    @Test
    public void testBuildWithoutAdds() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertThrows(BuilderException.class, () -> {
            builder.build();
        });
    }

    @Test
    public void testBuild() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(A));
        assertSame(builder, builder.add(B));
        final FakeChain chain = Cast.to(builder.build());
        assertArrayEquals(new Fake[]{A, B},
                chain.chained(),
                "chain");
    }

    @Test
    public void testBuildAfterAddingDuplicate() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(A));
        assertSame(builder, builder.add(A));

        assertSame(A, builder.build());
    }

    @Test
    public void testBuildAfterAddingDuplicate2() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(A,
                B);
        assertSame(builder, builder.add(chain));
        assertSame(builder, builder.add(A));

        assertSame(chain, builder.build());
    }

    @Test
    public void testBuildAfterAddingChainedIncludedPrevious() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(A));
        final FakeChain chain = new FakeChain(A,
                B);
        assertSame(builder, builder.add(chain));

        assertSame(chain, builder.build());
    }

    @Test
    public void testBuildAfterAddingChainedIncludedPrevious2() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(A));
        assertSame(builder, builder.add(B));
        final FakeChain chain = new FakeChain(A,
                B);
        assertSame(builder, builder.add(chain));

        assertSame(chain, builder.build());
    }

    @Test
    public void testBuildAfterAddingChainedIncludedPrevious3() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(A));
        assertSame(builder, builder.add(B));
        final FakeChain chain = new FakeChain(A,
                B,
                C);
        assertSame(builder, builder.add(chain));

        assertSame(chain, builder.build());
    }

    @Test
    public void testBuildAfterAddingChainedIncludedPrevious4() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(A));
        final FakeChain chain = new FakeChain(A,
                B,
                C);
        assertSame(builder, builder.add(B));
        assertSame(builder, builder.add(chain));

        assertSame(chain, builder.build());
    }

    @Test
    public void testToStringEmpty() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertEquals("[]", builder.toString());
    }

    @Override
    protected Fake createAdded() {
        return createFake(1);
    }

    @Override
    protected ChainFactoryChainBuilder<Fake> createBuilder() {
        return ChainFactoryChainBuilder.with(TYPE,
                FACTORY);
    }

    @Override
    protected Class<ChainFactoryChainBuilder> type() {
        return ChainFactoryChainBuilder.class;
    }

    @Override
    protected Class<Fake> builderProductType() {
        return Fake.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    static private Fake createFake(final int value) {
        return new Fake() {

            @Override
            public String toString() {
                return String.valueOf(value);
            }
        };
    }

    static private class FakeChain implements Chained<Fake>, Fake {

        private FakeChain(final Fake... chain) {
            this(TYPE, chain);
        }

        private FakeChain(final ChainType type, final Fake... chain) {
            super();
            this.chain = chain;
            this.type = type;
        }

        @Override
        public Fake[] chained() {
            return this.chain.clone();
        }

        private final Fake[] chain;

        @Override
        public ChainType chainType() {
            return this.type;
        }

        private final ChainType type;

        @Override
        public String toString() {
            return this.type + "=" + Arrays.toString(this.chain) + '@'
                    + Integer.toHexString(System.identityHashCode(this));
        }
    }
}
