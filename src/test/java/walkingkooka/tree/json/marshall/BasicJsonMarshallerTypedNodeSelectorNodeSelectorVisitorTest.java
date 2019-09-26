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

package walkingkooka.tree.json.marshall;

import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.select.parser.NodeSelectorVisitorTesting;

public final class BasicJsonMarshallerTypedNodeSelectorNodeSelectorVisitorTest extends BasicTestCase<BasicJsonMarshallerTypedNodeSelectorNodeSelectorVisitor<TestNode, StringName, StringName, Object>>
        implements NodeSelectorVisitorTesting<BasicJsonMarshallerTypedNodeSelectorNodeSelectorVisitor<TestNode, StringName, StringName, Object>, TestNode, StringName, StringName, Object> {

    @Override
    public void testIfClassIsFinalIfAllConstructorsArePrivate() {
    }

    @Override
    public void testAllConstructorsVisibility() {
    }

    @Override
    public BasicJsonMarshallerTypedNodeSelectorNodeSelectorVisitor<TestNode, StringName, StringName, Object> createVisitor() {
        return new BasicJsonMarshallerTypedNodeSelectorNodeSelectorVisitor<>(null);
    }

    @Override
    public String typeNamePrefix() {
        return BasicJsonMarshallerTypedNodeSelector.class.getSimpleName();
    }

    @Override
    public Class<BasicJsonMarshallerTypedNodeSelectorNodeSelectorVisitor<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(BasicJsonMarshallerTypedNodeSelectorNodeSelectorVisitor.class);
    }
}
