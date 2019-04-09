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

package walkingkooka.tree.patch;

import walkingkooka.Cast;
import walkingkooka.NeverError;
import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.tree.Node;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.pointer.NodePointer;
import walkingkooka.tree.pointer.NodePointerException;

import java.util.Objects;
import java.util.function.Function;

/**
 * A {@link NodePatch} supports operations for a {@link Node} that match the functionality of json-patch with json.<br>
 * <A href="http://jsonpatch.com">jsonpatch.com</A>
 */
public abstract class NodePatch<N extends Node<N, NAME, ?, ?>, NAME extends Name> implements HasJsonNode,
        HashCodeEqualsDefined,
        Function<N, N> {

    /**
     * Returns an empty patch.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> EmptyNodePatch<N, NAME> empty(final Class<N> type) {
        return EmptyNodePatch.get(type);
    }

    /**
     * Package private to limit sub classing.
     */
    NodePatch() {
        super();
    }

    // public factory methods supporting the builder pattern to build up a patch.........................

    /**
     * Adds an add operation to this patch.
     */
    public NodePatch<N, NAME> add(final NodePointer<N, NAME> path,
                                  final N value) {
        return this.append(AddNodePatch.with(path, value));
    }

    /**
     * Adds a copy operation to this patch.
     */
    public NodePatch<N, NAME> copy(final NodePointer<N, NAME> from,
                                   final NodePointer<N, NAME> path) {
        return this.append(CopyNodePatch.with(from, path));
    }

    /**
     * Adds a copy operation to this patch.
     */
    public NodePatch<N, NAME> move(final NodePointer<N, NAME> from,
                                   final NodePointer<N, NAME> path) {
        return this.append(MoveNodePatch.with(from, path));
    }

    /**
     * Adds an remove operation to this patch.
     */
    public NodePatch<N, NAME> remove(final NodePointer<N, NAME> path) {
        return this.append(RemoveNodePatch.with(path));
    }

    /**
     * Adds an add operation to this patch.
     */
    public NodePatch<N, NAME> replace(final NodePointer<N, NAME> path,
                                final N value) {
        return this.append(ReplaceNodePatch.with(path, value));
    }

    /**
     * Adds a test operation to this patch.
     */
    public NodePatch<N, NAME> test(final NodePointer<N, NAME> path,
                                   final N value) {
        return this.append(TestNodePatch.with(path, value));
    }

    final NonEmptyNodePatch<N, NAME> append(final NonEmptyNodePatch<N, NAME> patch) {
        final NonEmptyNodePatch<N, NAME> next = this.nextOrNull();
        return this.append0(null != next ? next.append(patch) : patch);
    }

    /**
     * Used to concatenate multiple patch components.
     */
    abstract NonEmptyNodePatch<N, NAME> append0(final NonEmptyNodePatch<N, NAME> patch);

    // Function................................................................................................

    /**
     * Executes this patch accepting the {@link Node} as the base of all pointer paths and operations.
     */
    @Override
    public final N apply(final N node) {
        Objects.requireNonNull(node, "node");

        final NodePointer<N, NAME> start = node.pointer();
        NodePatch<N, NAME> currentPatch = this;
        N currentNode = node;

        do {
            currentNode = currentPatch.apply0(currentNode, start);
            currentPatch = currentPatch.nextOrNull();
        } while (null != currentPatch);

        return currentNode;
    }

    /**
     * Performs the actual operation. Base is necessary for two step operations such as replace which need to relocate prior to executing the second half.
     */
    abstract N apply0(final N node, final NodePointer<N, NAME> start);

    /**
     * Helper which locates the original start {@link Node} or fails.
     */
    final N traverseStartOrFail(final N node, final NodePointer<N, NAME> start) {
        return start.traverse(node.root())
                .orElseThrow(() -> new NodePointerException("Unable to navigate to starting node: " + node));
    }

    /**
     * Returns the next component in the patch or null if the end has been reached.
     */
    abstract NonEmptyNodePatch<N, NAME> nextOrNull();

    // Object................................................................................................

    @Override
    public abstract String toString();

    /**
     * Sub classes must add their individual toString representation.
     */
    abstract void toString0(final StringBuilder b);

    // HasJsonNode...............................................................................

    /**
     * Accepts a json string holding a patch collection.
     * <pre>
     * {
     *      "type"="node-patch",
     *      "node-pointer-name-type"=“String”,
     *      "node-patch-value-type"=“String”,
     * 	    "value"=[
     *            {
     * 	            "op": "remove",
     * 	            "patch": "/1"
     *            },
     *            {
     * 	            "op": "remove",
     * 	            "patch": "/2"
     *            }
     * 	    ]
     * }
     * </pre>
     */
    public static NodePatch<?, ?> fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        try {
            return fromJsonNode0(node.arrayOrFail());
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    private static NodePatch<?, ?> fromJsonNode0(final JsonArrayNode array) {
        NodePatch<?, ?> patch = EmptyNodePatch.getWildcard();

        for (JsonNode child : array.children()) {
            patch = patch.append(Cast.to(fromJsonNode1(child.objectOrFail())));
        }

        return patch;
    }

    final static String ADD = "add";
    final static String COPY = "copy";
    final static String MOVE = "move";
    final static String REMOVE = "remove";
    final static String REPLACE = "replace";
    final static String TEST = "test";

    /**
     * Factory that switches on the op and then creates a {@link NodePatch}.
     */
    private static NonEmptyNodePatch<?, ?> fromJsonNode1(final JsonObjectNode node) {
        NonEmptyNodePatch<?, ?> patch = null;

        final String op = node.getOrFail(OP_PROPERTY).stringValueOrFail();
        switch (op) {
            case ADD:
                patch = AddReplaceOrTestNodePatchNodePatchJsonObjectNodePropertyVisitor.add(node);
                break;
            case COPY:
                patch = CopyOrMoveNodePatchNodePatchJsonObjectNodePropertyVisitor.copy(node);
                break;
            case MOVE:
                patch = CopyOrMoveNodePatchNodePatchJsonObjectNodePropertyVisitor.move(node);
                break;
            case REMOVE:
                patch = RemoveNodePatchNodePatchJsonObjectNodePropertyVisitor.remove(node);
                break;
            case REPLACE:
                patch = AddReplaceOrTestNodePatchNodePatchJsonObjectNodePropertyVisitor.replace(node);
                break;
            case TEST:
                patch = AddReplaceOrTestNodePatchNodePatchJsonObjectNodePropertyVisitor.test(node);
                break;
            default:
                NeverError.unhandledCase(op, ADD, COPY, MOVE, REMOVE, REPLACE, TEST);
        }

        return patch;
    }

    final static String OP = "op";
    final static String PATH_NAME_TYPE = "path-name-type";
    final static String FROM = "from";
    final static String PATH = "path";
    final static String VALUE_TYPE = "value-type";
    final static String VALUE = "value";

    final static JsonNodeName OP_PROPERTY = JsonNodeName.with(OP);

    final static JsonNodeName PATH_NAME_TYPE_PROPERTY = JsonNodeName.with(PATH_NAME_TYPE);

    final static JsonNodeName FROM_PROPERTY = JsonNodeName.with(FROM);
    final static JsonNodeName PATH_PROPERTY = JsonNodeName.with(PATH);

    final static JsonNodeName VALUE_TYPE_PROPERTY = JsonNodeName.with(VALUE_TYPE);
    final static JsonNodeName VALUE_PROPERTY = JsonNodeName.with(VALUE);

    static {
        HasJsonNode.register("patch",
                NodePatch::fromJsonNode,
                AddNodePatch.class,
                CopyNodePatch.class,
                EmptyNodePatch.class,
                MoveNodePatch.class,
                RemoveNodePatch.class,
                ReplaceNodePatch.class,
                TestNodePatch.class);
    }
}
