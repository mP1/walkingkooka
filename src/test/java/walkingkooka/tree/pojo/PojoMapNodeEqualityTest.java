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
import walkingkooka.collect.map.Maps;

import java.util.Map;

public final class PojoMapNodeEqualityTest extends PojoCollectionNodeEqualityTestCase<PojoMapNode> {

    private final static PojoName MAP = PojoName.property("map");

    @Test
    public void testDifferentValues() {
        this.createPojoNode(Maps.one("KEY", "different"));
    }

    @Override
    protected PojoNode createObject() {
        return this.createPojoNode(Maps.one("KEY", "VALUE"));
    }

    private PojoMapNode createPojoNode(final Map<Object, Object> map) {
        return Cast.to(PojoNode.wrap(MAP,
                map,
                new ReflectionPojoNodeContext()));

    }
}
