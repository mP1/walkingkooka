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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.build.BuilderException;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.Fake;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

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

    private final static Fake A = ChainFactoryChainBuilderTest.createFake(1);

    private final static Fake B = ChainFactoryChainBuilderTest.createFake(2);

    private final static Fake C = ChainFactoryChainBuilderTest.createFake(3);

    // tests
    @Test
    public void testWithNullChainTypeFails() {
        this.withFails(null, ChainFactoryChainBuilderTest.FACTORY);
    }

    @Test
    public void testWithNullChainFactoryFails() {
        this.withFails(ChainFactoryChainBuilderTest.TYPE, null);
    }

    private void withFails(final ChainType type, final ChainFactory<Fake> factory) {
        try {
            ChainFactoryChainBuilder.with(type, factory);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testWith() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertEquals("factory", ChainFactoryChainBuilderTest.FACTORY, builder.factory);
        assertEquals("all", Sets.empty(), builder.all);
    }

    @Test
    public void testAddOneAndBuild() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        assertSame(ChainFactoryChainBuilderTest.A, builder.build());
    }

    @Test
    public void testAddOneThenDuplicateIgnoredAndBuild() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        assertSame(ChainFactoryChainBuilderTest.A, builder.build());
    }

    @Test
    public void testAdd() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.B));
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.C));
        assertEquals("all",
                Sets.of(ChainFactoryChainBuilderTest.A,
                        ChainFactoryChainBuilderTest.B,
                        ChainFactoryChainBuilderTest.C),
                builder.all);
    }

    @Test
    public void testAddDuplicateIgnored() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.B));
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.C));
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        assertEquals("all",
                Sets.of(ChainFactoryChainBuilderTest.A,
                        ChainFactoryChainBuilderTest.B,
                        ChainFactoryChainBuilderTest.C),
                builder.all);
    }

    @Test
    public void testAddDifferentChained() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(ChainType.with("different"),
                ChainFactoryChainBuilderTest.A,
                ChainFactoryChainBuilderTest.B);
        assertSame(builder, builder.add(chain));
        assertEquals("all should only contain of not its items", Sets.of(chain), builder.all);
    }

    @Test
    public void testAddChained() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(ChainFactoryChainBuilderTest.A,
                ChainFactoryChainBuilderTest.B);
        assertSame(builder, builder.add(chain));

        assertEquals("chain not returned", chain, builder.build());
    }

    @Test
    public void testAddChainedDuplicateIgnored() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(ChainFactoryChainBuilderTest.A,
                ChainFactoryChainBuilderTest.B);
        assertSame(builder, builder.add(chain));
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));

        assertSame("chain not returned", chain, builder.build());
    }

    @Test
    public void testAddChainedDuplicateIgnored2() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(ChainFactoryChainBuilderTest.A,
                ChainFactoryChainBuilderTest.B);
        assertSame(builder, builder.add(chain));
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.B));

        assertSame("chain not returned", chain, builder.build());
    }

    @Test
    public void testAddChainedDuplicateIgnored3() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(ChainFactoryChainBuilderTest.A,
                ChainFactoryChainBuilderTest.B);
        assertSame(builder, builder.add(chain));
        assertSame(builder, builder.add(chain));

        assertSame("chain not returned", chain, builder.build());
    }

    @Test
    public void testAddChainedDuplicateIgnored4() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(ChainFactoryChainBuilderTest.A,
                ChainFactoryChainBuilderTest.B);
        assertSame(builder, builder.add(chain));
        assertSame(builder,
                builder.add(new FakeChain(ChainFactoryChainBuilderTest.B,
                        ChainFactoryChainBuilderTest.A)));

        assertSame("chain not returned", chain, builder.build());
    }

    @Test
    public void testAddChained2() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        assertSame(builder,
                builder.add(new FakeChain(ChainFactoryChainBuilderTest.B,
                        ChainFactoryChainBuilderTest.C)));

        assertEquals("all",
                Sets.of(ChainFactoryChainBuilderTest.A,
                        ChainFactoryChainBuilderTest.B,
                        ChainFactoryChainBuilderTest.C),
                builder.all);
    }

    @Test
    public void testAddChainedDuplicatesIgnored2() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        assertSame(builder,
                builder.add(new FakeChain(ChainFactoryChainBuilderTest.A,
                        ChainFactoryChainBuilderTest.A,
                        ChainFactoryChainBuilderTest.B,
                        ChainFactoryChainBuilderTest.B,
                        ChainFactoryChainBuilderTest.C)));

        assertEquals("all",
                Sets.of(ChainFactoryChainBuilderTest.A,
                        ChainFactoryChainBuilderTest.B,
                        ChainFactoryChainBuilderTest.C),
                builder.all);
    }

    @Override
    @Test
    public void testBuildWithoutAdds() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        try {
            builder.build();
            Assert.fail();
        } catch (final BuilderException expected) {
        }
    }

    @Test
    public void testBuild() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.B));
        final FakeChain chain = Cast.to(builder.build());
        assertArrayEquals("chain",
                new Fake[]{ChainFactoryChainBuilderTest.A, ChainFactoryChainBuilderTest.B},
                chain.chained());
    }

    @Test
    public void testBuildAfterAddingDuplicate() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));

        assertSame(ChainFactoryChainBuilderTest.A, builder.build());
    }

    @Test
    public void testBuildAfterAddingDuplicate2() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        final FakeChain chain = new FakeChain(ChainFactoryChainBuilderTest.A,
                ChainFactoryChainBuilderTest.B);
        assertSame(builder, builder.add(chain));
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));

        assertSame(chain, builder.build());
    }

    @Test
    public void testBuildAfterAddingChainedIncludedPrevious() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        final FakeChain chain = new FakeChain(ChainFactoryChainBuilderTest.A,
                ChainFactoryChainBuilderTest.B);
        assertSame(builder, builder.add(chain));

        assertSame(chain, builder.build());
    }

    @Test
    public void testBuildAfterAddingChainedIncludedPrevious2() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.B));
        final FakeChain chain = new FakeChain(ChainFactoryChainBuilderTest.A,
                ChainFactoryChainBuilderTest.B);
        assertSame(builder, builder.add(chain));

        assertSame(chain, builder.build());
    }

    @Test
    public void testBuildAfterAddingChainedIncludedPrevious3() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.B));
        final FakeChain chain = new FakeChain(ChainFactoryChainBuilderTest.A,
                ChainFactoryChainBuilderTest.B,
                ChainFactoryChainBuilderTest.C);
        assertSame(builder, builder.add(chain));

        assertSame(chain, builder.build());
    }

    @Test
    public void testBuildAfterAddingChainedIncludedPrevious4() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.A));
        final FakeChain chain = new FakeChain(ChainFactoryChainBuilderTest.A,
                ChainFactoryChainBuilderTest.B,
                ChainFactoryChainBuilderTest.C);
        assertSame(builder, builder.add(ChainFactoryChainBuilderTest.B));
        assertSame(builder, builder.add(chain));

        assertSame(chain, builder.build());
    }

    @Test
    public void testToStringEmpty() {
        final ChainFactoryChainBuilder<Fake> builder = this.createBuilder();
        Assert.assertEquals("[]", builder.toString());
    }

    @Override
    protected Fake createAdded() {
        return ChainFactoryChainBuilderTest.createFake(1);
    }

    @Override
    protected ChainFactoryChainBuilder<Fake> createBuilder() {
        return ChainFactoryChainBuilder.with(ChainFactoryChainBuilderTest.TYPE,
                ChainFactoryChainBuilderTest.FACTORY);
    }

    @Override
    protected Class<ChainFactoryChainBuilder> type() {
        return ChainFactoryChainBuilder.class;
    }

    @Override
    protected Class<Fake> builderProductType() {
        return Fake.class;
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
            this(ChainFactoryChainBuilderTest.TYPE, chain);
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
