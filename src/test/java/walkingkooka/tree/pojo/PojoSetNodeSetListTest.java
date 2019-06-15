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
import walkingkooka.collect.set.Sets;

import java.util.List;
import java.util.Set;

public final class PojoSetNodeSetListTest extends PojoNodeListTestCase<PojoSetNodeSetList, Object> {

    @Override
    List<Object> components() {
        return Lists.of("a1", "b2", "c3");
    }

    @Override
    List<Object> differentComponents() {
        return Lists.of("different1", "different2", "different3");
    }

    @Override
    List<Object> createList(final List<Object> components) {
        final Set<Object> set = Sets.ordered();
        set.addAll(components);
        return PojoSetNodeSetList.with(set);
    }

    @Override
    public Class<PojoSetNodeSetList> type() {
        return PojoSetNodeSetList.class;
    }
}
