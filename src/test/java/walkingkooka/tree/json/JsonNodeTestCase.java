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

package walkingkooka.tree.json;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.io.printer.IndentingPrinters;
import walkingkooka.io.printer.Printers;
import walkingkooka.naming.Name;
import walkingkooka.test.BeanPropertiesTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.PublicStaticFactoryTesting;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.Node;
import walkingkooka.tree.NodeTesting;
import walkingkooka.tree.search.HasSearchNodeTesting;
import walkingkooka.type.MemberVisibility;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class JsonNodeTestCase<N extends JsonNode> implements ClassTesting2<JsonNode>,
        HasJsonNodeTesting<JsonNode>,
        HasSearchNodeTesting<N>,
        IsMethodTesting<N>,
        NodeTesting<JsonNode, JsonNodeName, Name, Object> {

    JsonNodeTestCase() {
        super();
    }

    @Test
    public final void testPublicStaticFactoryMethod() {
        PublicStaticFactoryTesting.check(JsonNode.class,
                "Json",
                Node.class,
                this.type());
    }

    @Test
    public final void testSetNameNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createJsonNode().setName(null);
        });
    }

    @Test
    public final void testSetNameSame() {
        final N node = this.createJsonNode();
        assertSame(node, node.setName(node.name()));
    }

    @Test
    public abstract void testSetNameDifferent();

    @Test
    public final void testSetAttributesFails() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createJsonNode().setAttributes(Maps.empty());
        });
    }

    // Functional.................................................................................................

    @Test
    public final void testOptional() {
        final N node = this.createJsonNode();

        if (node.isNull()) {
            final Optional<JsonNode> optionalJsonNode = node.optional();
            assertEquals(
                    Optional.empty(),
                    optionalJsonNode,
                    "JsonNullNode should have returned Optional.empty");
            final Optional<N> optionalJsonNode2 = node.optional();
            assertEquals(
                    Optional.empty(),
                    optionalJsonNode2,
                    "JsonNullNode should have returned Optional.empty");
        } else {
            final Optional<JsonNode> optionalJsonNode = node.optional();
            assertEquals(
                    Optional.of(node),
                    optionalJsonNode,
                    "JsonNullNode should have returned Optional.of(JsonNode)");
            final Optional<N> optionalJsonNode2 = node.optional();
            assertEquals(
                    Optional.of(node),
                    optionalJsonNode2,
                    "JsonNullNode should have returned Optional.of(JsonNode)");
        }
    }

    // ToXXXValueOrFail.................................................................................................
    @Test
    public void testBooleanValueOrFail() {
        assertThrows(JsonNodeException.class, () -> {
            this.createNode().booleanValueOrFail();
        });
    }

    @Test
    public void testNumberValueOrFail() {
        assertThrows(JsonNodeException.class, () -> {
            this.createNode().numberValueOrFail();
        });
    }

    @Test
    public void testStringValueOrFail() {
        assertThrows(JsonNodeException.class, () -> {
            this.createNode().stringValueOrFail();
        });
    }

    @Test
    public void testObjectOrFail() {
        final N node = this.createJsonNode();
        if (node instanceof JsonObjectNode) {
            assertSame(node, node.objectOrFail());
        } else {
            assertThrows(JsonNodeException.class, () -> {
                node.objectOrFail();
            });
        }
    }

    @Test
    public final void testPropertiesNeverReturnNull() throws Exception {
        BeanPropertiesTesting.allPropertiesNeverReturnNullCheck(this.createJsonNode(),
                (m) -> this.propertiesNeverReturnNullSkipProperties().contains(m.getName()));
    }

    abstract List<String> propertiesNeverReturnNullSkipProperties();

    final static String ARRAY_OR_FAIL = "arrayOrFail";
    final static String BOOLEAN_VALUE_OR_FAIL = "booleanValueOrFail";
    final static String FROM_WITH_TYPE_LIST = "fromJsonNodeWithTypeList";
    final static String FROM_WITH_TYPE_SET = "fromJsonNodeWithTypeSet";
    final static String FROM_WITH_TYPE_MAP = "fromJsonNodeWithTypeMap";
    final static String FROM_WITH_TYPE = "fromJsonNodeWithType";
    final static String NUMBER_VALUE_OR_FAIL = "numberValueOrFail";
    final static String OBJECT_OR_FAIL = "objectOrFail";
    final static String PARENT_OR_FAIL = "parentOrFail";
    final static String STRING_VALUE_OR_FAIL = "stringValueOrFail";
    final static String VALUE = "value";

    @Test
    public final void testEqualsDifferentParent() {
        this.checkNotEquals(JsonNode.array().appendChild(this.createObject()));
    }

    // HasJsonNode............................................................................................

    private final Class<?> NULL_CLASS = null;

    @Test
    public final void testFromJsonNodeNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createJsonNode().fromJsonNode(NULL_CLASS);
        });
    }

    @Test
    public final void testFromJsonNodeListNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createJsonNode().fromJsonNodeList(NULL_CLASS);
        });
    }

    @Test
    public final void testFromJsonNodeSetNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createJsonNode().fromJsonNodeSet(NULL_CLASS);
        });
    }

    @Test
    public final void testFromJsonNodeMapNullKeyTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createJsonNode().fromJsonNodeMap(NULL_CLASS, Object.class);
        });
    }

    @Test
    public final void testFromJsonNodeMapNullValueTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createJsonNode().fromJsonNodeMap(Object.class, NULL_CLASS);
        });
    }

    // ToJsonNodeTesting...............................................................................................

    @Test
    public final void testToJsonNode() {
        final N node = this.createJsonNode();
        assertSame(node, node.toJsonNode());
    }

    @Test
    public final void testToJsonNodeWithType() {
        final N node = this.createJsonNode();
        assertEquals(JsonNode.object()
                        .set(JsonObjectNode.TYPE, JsonNode.string(this.nodeTypeName()))
                        .set(JsonObjectNode.VALUE, node),
                node.toJsonNodeWithType());
    }

    abstract String nodeTypeName();

    @Test
    public void testToJsonNodeRemovesParent() {
        final N node = this.createJsonNode();
        final JsonNode parent = JsonNode.object()
                .appendChild(node);
        this.toJsonNodeAndCheck(parent.children().get(0), node);
    }

    @Test
    public void testPrintJsonNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createJsonNode().printJson(null);
        });
    }

    @Test
    public void testPrintJson() {
        final N node = this.createJsonNode();
        final StringBuilder b = new StringBuilder();
        final IndentingPrinter printer = IndentingPrinters.printer(Printers.stringBuilder(b, LineEnding.SYSTEM));
        node.printJson(printer);

        assertEquals(node.toString(), b.toString());
    }

    @Override
    public JsonNode createNode() {
        return this.createJsonNode();
    }

    abstract N createJsonNode();

    @Override
    public Class<JsonNode> type() {
        return Cast.to(this.jsonNodeType());
    }

    abstract Class<N> jsonNodeType();

    @Override
    public final String typeNamePrefix() {
        return "Json";
    }

    // IsMethodTesting.................................................................................................

    @Override
    public final N createIsMethodObject() {
        return Cast.to(this.createNode());
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "Json";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return Node.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> m.equals("isRoot");
    }

    // ClassTestCase.........................................................................................

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // HasJsonNode.........................................................................................

    @Override
    public final N createHasJsonNode() {
        return this.createJsonNode();
    }

    final <T> void fromJsonNodeWithTypeAndCheck(final JsonNode node,
                                                final T value) {
        assertEquals(value,
                node.fromJsonNodeWithType(),
                () -> "JsonNode.fromNodeWithType " + node);
    }

    final void fromJsonNodeWithTypeAndFail(final Class<? extends Throwable> thrown) {
        this.fromJsonNodeWithTypeAndFail(this.createJsonNode(), thrown);
    }

    final void fromJsonNodeWithTypeAndFail(final JsonNode node,
                                           final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            node.fromJsonNodeWithType();
        });
    }

    final <T> void fromJsonNodeWithTypeListAndCheck(final JsonNode node,
                                                    final T value) {
        assertEquals(value,
                node.fromJsonNodeWithTypeList(),
                () -> "JsonNode.fromNodeWithTypeList " + node);
    }

    final void fromJsonNodeWithTypeListAndFail(final Class<? extends Throwable> thrown) {
        this.fromJsonNodeWithTypeListAndFail(this.createNode(), thrown);
    }

    final void fromJsonNodeWithTypeListAndFail(final JsonNode node,
                                               final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            node.fromJsonNodeWithTypeList();
        });
    }

    final <T> void fromJsonNodeWithTypeSetAndCheck(final JsonNode node,
                                                   final T value) {
        assertEquals(value,
                node.fromJsonNodeWithTypeSet(),
                () -> "JsonNode.fromNodeWithTypeSet " + node);
    }

    final void fromJsonNodeWithTypeSetAndFail(final Class<? extends Throwable> thrown) {
        this.fromJsonNodeWithTypeSetAndFail(this.createNode(), thrown);
    }

    final void fromJsonNodeWithTypeSetAndFail(final JsonNode node,
                                              final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            node.fromJsonNodeWithTypeSet();
        });
    }

    final void fromJsonNodeWithTypeMapAndCheck(final JsonNode node,
                                               final Map<?, ?> value) {
        assertEquals(value,
                node.fromJsonNodeWithTypeMap(),
                () -> "JsonNode.fromNodeWithTypeMap " + node);
    }

    final void fromJsonNodeWithTypeMapAndFail(final Class<? extends Throwable> thrown) {
        this.fromJsonNodeWithTypeMapAndFail(this.createNode(), thrown);
    }

    final void fromJsonNodeWithTypeMapAndFail(final JsonNode node,
                                              final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            node.fromJsonNodeWithTypeMap();
        });
    }

    final <T> void fromJsonNodeAndCheck(final JsonNode node,
                                        final Class<T> type,
                                        final T value) {
        assertEquals(value,
                node.fromJsonNode(type),
                () -> "JsonNode.fromJsonNode " + node + " " + type.getName());
    }

    final void fromJsonNodeAndFail(final Class<?> type,
                                   final Class<? extends Throwable> thrown) {
        this.fromJsonNodeAndFail(this.createJsonNode(),
                type,
                thrown);
    }

    final void fromJsonNodeAndFail(final JsonNode node,
                                   final Class<?> type,
                                   final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            node.fromJsonNode(type);
        });
    }

    final <T> void fromJsonNodeListAndCheck(final JsonNode node,
                                            final Class<T> elementType,
                                            final List<T> value) {
        assertEquals(value,
                node.fromJsonNodeList(elementType),
                () -> "JsonNode.fromNodeList " + node + " elementType: " + elementType.getName());
    }

    final void fromJsonNodeListAndFail(final Class<?> elementType,
                                       final Class<? extends Throwable> thrown) {
        this.fromJsonNodeListAndFail(this.createNode(), elementType, thrown);
    }

    final void fromJsonNodeListAndFail(final JsonNode node,
                                       final Class<?> elementType,
                                       final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            node.fromJsonNodeList(elementType);
        });
    }

    final <T> void fromJsonNodeSetAndCheck(final JsonNode node,
                                           final Class<T> elementType,
                                           final Set<T> value) {
        assertEquals(value,
                node.fromJsonNodeSet(elementType),
                () -> "JsonNode.fromNodeSet " + node + " elementType: " + elementType.getName());
    }

    final void fromJsonNodeSetAndFail(final Class<?> elementType,
                                      final Class<? extends Throwable> thrown) {
        this.fromJsonNodeSetAndFail(this.createNode(), elementType, thrown);
    }

    final void fromJsonNodeSetAndFail(final JsonNode node,
                                      final Class<?> elementType,
                                      final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            node.fromJsonNodeSet(elementType);
        });
    }

    final <K, V> void fromJsonNodeMapAndCheck(final JsonNode node,
                                              final Class<K> keyType,
                                              final Class<V> valueType,
                                              final Map<K, V> value) {
        assertEquals(value,
                node.fromJsonNodeMap(keyType, valueType),
                () -> "JsonNode.fromNodeMap " + node + ", keyType: " + keyType.getName() + ", valueType: " + valueType.getName());
    }

    final void fromJsonNodeMapAndFail(final Class<?> keyType,
                                      final Class<?> valueType,
                                      final Class<? extends Throwable> thrown) {
        this.fromJsonNodeMapAndFail(this.createNode(), keyType, valueType, thrown);
    }

    final void fromJsonNodeMapAndFail(final JsonNode node,
                                      final Class<?> keyType,
                                      final Class<?> valueType,
                                      final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            node.fromJsonNodeMap(keyType, valueType);
        });
    }
}
