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
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.Node;
import walkingkooka.tree.NodeTestCase2;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class JsonNodeTestCase<N extends JsonNode> extends NodeTestCase2<JsonNode, JsonNodeName, Name, Object> {

    @Test
    public final void testPublicStaticFactoryMethod()  {
        this.publicStaticFactoryCheck(JsonNode.class, "Json", Node.class);
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
    public final void testIsMethods() throws Exception {
        final String prefix = "Json";
        final String suffix = Node.class.getSimpleName();

        final N node = this.createJsonNode();
        final String name = node.getClass().getSimpleName();
        assertEquals(true, name.startsWith(prefix),name + " starts with " + prefix);
        assertEquals(true, name.endsWith(suffix),name + " ends with " + suffix);

        final String isMethodName = "is" + CharSequences.capitalize(name.substring(prefix.length(), name.length() - suffix.length()));

        for(Method method : node.getClass().getMethods()) {
            if(MethodAttributes.STATIC.is(method)) {
                continue;
            }
            final String methodName = method.getName();
            if(methodName.equals("isRoot")){
                continue;
            }
            if(!methodName.startsWith("is")) {
                continue;
            }
            assertEquals(methodName.equals(isMethodName),
                    method.invoke(node),
                    method + " returned");
        }
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
    protected JsonNode createNode() {
        return this.createJsonNode();
    }

    abstract N createJsonNode();

    @Override
    protected Class<JsonNode> type() {
        return Cast.to(this.jsonNodeType());
    }

    abstract Class<N> jsonNodeType();

    @Override
    protected final String requiredNamePrefix() {
        return "Json";
    }

    final void toSearchNodeAndCheck(final N node, final SearchNode searchNode) {
        assertEquals(searchNode, node.toSearchNode(), () -> "toSearchNode failure from " + node);
    }
}
