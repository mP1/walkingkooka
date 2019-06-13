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

package walkingkooka.tree.select;

import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.text.cursor.parser.select.NodeSelectorVisitorTesting;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.type.JavaVisibility;

public final class NodeSelectorHasJsonNodeNodeSelectorVisitorTest implements NodeSelectorVisitorTesting<NodeSelectorHasJsonNodeNodeSelectorVisitor<TestNode, StringName, StringName, Object>,
        TestNode, StringName, StringName, Object> {

    @Override
    public NodeSelectorHasJsonNodeNodeSelectorVisitor<TestNode, StringName, StringName, Object> createVisitor() {
        return new NodeSelectorHasJsonNodeNodeSelectorVisitor<>();
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return NodeSelector.class.getSimpleName() + HasJsonNode.class.getSimpleName();
    }

    @Override
    public Class<NodeSelectorHasJsonNodeNodeSelectorVisitor<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NodeSelectorHasJsonNodeNodeSelectorVisitor.class);
    }
}
