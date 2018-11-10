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

package walkingkooka.tree.pojo;

import org.junit.Test;
import walkingkooka.Cast;

public final class PojoDoubleArrayNodeEqualityTest extends PojoArrayNodeEqualityTestCase<PojoDoubleArrayNode> {

    @Test
    public void testDifferentValues() {
        this.createPojoNode(new double[]{1});
    }

    @Test
    public void testDifferentValues2() {
        this.createPojoNode(new double[]{1, 2});
    }

    @Override
    protected PojoNode createObject() {
        return this.createPojoNode(new double[]{1});
    }

    private PojoDoubleArrayNode createPojoNode(final double[] values) {
        return Cast.to(PojoNode.wrap(ARRAY,
                values,
                new ReflectionPojoNodeContext()));
    }
}
