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
import walkingkooka.collect.map.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class PojoMapNodeMapListTest extends PojoNodeListTestCase<PojoMapNodeMapList, Object> {

    @Override
    List<Object> components() {
        return list("k1", "v1", "k2", "v2");
    }

    @Override
    List<Object> differentComponents() {
        return list("different1-k", "different1-v", "different2-k", "different2-v");
    }

    private List<Object> list(final String key1,
                              final String value1,
                              final String key2,
                              final String value2) {
        return new ArrayList<>(Maps.of(key1, value1, key2, value2).entrySet());
    }

    @Override
    List<Object> createList(final List<Object> components) {
        final Map<Object, Object> map = Maps.ordered();
        for (Object value : components) {
            final Entry<Object, Object> entry = Cast.to(value);
            map.put(entry.getKey(), entry.getValue());
        }
        return new PojoMapNodeMapList(map);
    }

    @Override
    public Class<PojoMapNodeMapList> type() {
        return PojoMapNodeMapList.class;
    }
}
