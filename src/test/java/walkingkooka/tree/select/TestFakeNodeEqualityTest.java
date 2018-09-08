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

package walkingkooka.tree.select;

import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.NodeEqualityTestCase;

import java.util.Map;

public class TestFakeNodeEqualityTest extends NodeEqualityTestCase<TestFakeNode, StringName, StringName, Object> {

    @Override
    protected TestFakeNode createNode(int i) {
        return new TestFakeNode("test-" + i);
    }

    @Override
    protected Map<StringName, Object> createAttributes(int i) {
        final Map<StringName, Object> attributes = Maps.ordered();
        for(int j = 0; j < i; j++) {
            attributes.put(Names.string("attribute-" + i), i);
        }
        return attributes;
    }
}
