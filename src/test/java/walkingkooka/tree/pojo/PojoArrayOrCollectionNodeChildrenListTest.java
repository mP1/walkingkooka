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

package walkingkooka.tree.pojo;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.util.List;

public final class PojoArrayOrCollectionNodeChildrenListTest extends PojoNodeListTestCase<PojoArrayOrCollectionNodeChildrenList<?>, PojoNode> {

    @Override
    List<PojoNode> components() {
        return list0("abc123", "def234");
    }

    @Override
    List<PojoNode> differentComponents() {
        return list0("different123", "different234");
    }

    @Override
    List<PojoNode> createList(final List<PojoNode> components) {
        return list0((String) components.get(0).value(),
                (String) components.get(1).value());
    }

    private List<PojoNode> list0(final String component0, final String component1) {
        return PojoNode.wrap(PojoName.property("root"),
                Lists.of(component0, component1),
                new ReflectionPojoNodeContext())
                .children();
    }

    @Override
    public Class<PojoArrayOrCollectionNodeChildrenList<?>> type() {
        return Cast.to(PojoArrayOrCollectionNodeChildrenList.class);
    }
}
