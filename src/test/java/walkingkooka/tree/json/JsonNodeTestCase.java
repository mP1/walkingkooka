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

package walkingkooka.tree.json;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.test.BeanPropertiesTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.PublicStaticFactoryTesting;
import walkingkooka.text.HasTextTesting;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.IndentingPrinters;
import walkingkooka.text.printer.Printers;
import walkingkooka.tree.HasTextOffsetTesting;
import walkingkooka.tree.Node;
import walkingkooka.tree.NodeTesting;
import walkingkooka.tree.search.HasSearchNodeTesting;
import walkingkooka.type.JavaVisibility;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class JsonNodeTestCase<N extends JsonNode> implements BeanPropertiesTesting,
        ClassTesting2<JsonNode>,
        HasSearchNodeTesting<N>,
        HasTextOffsetTesting,
        HasTextTesting,
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
        this.allPropertiesNeverReturnNullCheck(this.createJsonNode(),
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

    // JsonNodeVisitor..................................................................................................

    @Test
    public final void testAccept2() {
        new JsonNodeVisitor() {
        }.accept(this.createNode());
    }

    // Object...........................................................................................................

    @Test
    public final void testEqualsDifferentParent() {
        this.checkNotEquals(JsonNode.array().appendChild(this.createObject()));
    }

    // HasTextOffset....................................................................................................

    @Test
    public final void testTextOffset() {
        this.textOffsetAndCheck(this.createNode(), 0);
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
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
