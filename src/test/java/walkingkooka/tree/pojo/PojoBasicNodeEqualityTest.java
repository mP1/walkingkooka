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

public class PojoBasicNodeEqualityTest extends PojoNodeEqualityTestCase<PojoBasicNode> {

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(this.createPojoNode(456));
    }

    @Test
    public void testDifferentValueType() {
        this.checkNotEquals(this.createPojoNode(6.7));
    }

    @Test
    public void testSameValueDifferentType() {
        this.checkNotEquals(this.createPojoNode((byte) 123));
    }

    @Override
    protected PojoNode createObject() {
        return this.createPojoNode(123);
    }

    private PojoBasicNode createPojoNode(final Object value) {
        return Cast.to(PojoNode.wrap(PojoName.property("root"),
                value,
                new ReflectionPojoNodeContext()));
    }
}
