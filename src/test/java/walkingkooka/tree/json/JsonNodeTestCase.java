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
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.PublicStaticFactoryTesting;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.Node;
import walkingkooka.tree.NodeTesting2;
import walkingkooka.tree.search.HasSearchNodeTesting;
import walkingkooka.type.MemberVisibility;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class JsonNodeTestCase<N extends JsonNode> extends ClassTestCase<JsonNode>
        implements HasJsonNodeTesting<N>,
        HasSearchNodeTesting<N>,
        IsMethodTesting<N>,
        NodeTesting2<JsonNode, JsonNodeName, Name, Object> {

    JsonNodeTestCase() {
        super();
    }

    @Test
    public final void testPublicStaticFactoryMethod()  {
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

    @Test
    public final void testEqualsDifferentParent() {
        this.checkNotEquals(JsonNode.array().appendChild(this.createObject()));
    }

    @Test
    public final void testToJsonNode() {
        final N node = this.createJsonNode();
        assertSame(node, node.toJsonNode());
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
}
