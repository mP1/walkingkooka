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
import walkingkooka.build.BuilderTestCase;
import walkingkooka.collect.list.Lists;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Base class for any {@link ChainBuilder} that includes mostly parameter check tests
 */
abstract public class ChainBuilderTestCase<B extends ChainBuilder<T>, T>
        extends BuilderTestCase<B, T> {

    protected ChainBuilderTestCase() {
        super();
    }

    @Test(expected = NullPointerException.class) final public void testAddNullFails() {
        final B builder = this.createBuilder();
        builder.add(null);
    }

    abstract public void testBuildWithoutAdds();

    @Test final public void testAddAndBuild() {
        final B builder = this.createBuilder();
        final T added1 = this.createAdded();
        assertSame(builder, builder.add(added1));

        final T added2 = this.createAdded();
        assertSame(builder, builder.add(added2));

        final T built = builder.build();
        if ((added1 == built) || (added2 == built)) {
            Assert.fail("builder should not have returned the 1st/2nd item added=" + built);
        }
    }

    @Test final public void testToStringAfterAdd() {
        final B builder = this.createBuilder();
        final T added = this.createAdded();
        assertEquals(Lists.of(added).toString(), builder.add(added).toString());
    }

    abstract protected T createAdded();
}
