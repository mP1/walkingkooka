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

import walkingkooka.collect.list.Lists;

import java.util.List;

public final class PojoArrayNodeChildrenValueListTest extends PojoNodeListTestCase<PojoArrayNodeChildrenValueList, Object> {

    @Override
    public Class<PojoArrayNodeChildrenValueList> type() {
        return PojoArrayNodeChildrenValueList.class;
    }

    @Override
    List<Object> components() {
        return Lists.of(true, true, false);
    }

    @Override
    List<Object> differentComponents() {
        return Lists.of(false, false, true);
    }

    @Override
    List<Object> createList(final List<Object> components) {
        return PojoNode.wrap(PojoName.property("root"),
                new boolean[]{(Boolean) components.get(0), (Boolean) components.get(1), (Boolean) components.get(2)},
                new ReflectionPojoNodeContext())
                .childrenValues();
    }
}
