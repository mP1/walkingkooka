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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import walkingkooka.ContextTesting;
import walkingkooka.test.ToStringTesting;

public abstract class BasicJsonNodeContextTestCase<C extends JsonNodeContext> extends BasicTestCase<C>
        implements ContextTesting<C>,
        ToStringTesting<C> {

    BasicJsonNodeContextTestCase() {
        super();
    }

    @BeforeAll
    public static void beforeAll() {
        TestJsonNodeValue.register();
    }

    @AfterAll
    public static void afterAll() {
        TestJsonNodeValue.unregister();
    }

    @Test
    public final void testToString() {
        this.toStringContainsCheck(this.createContext(),
                "big-decimal",
                "big-integer",
                "boolean",
                "byte",
                "character",
                "double",
                "float",
                "int",
                "java.lang.Boolean",
                "java.lang.Byte",
                "java.lang.Character",
                "java.lang.Double",
                "java.lang.Float",
                "java.lang.Integer",
                "java.lang.Long",
                "java.lang.Number",
                "java.lang.Short",
                "java.lang.String",
                "java.math.BigDecimal",
                "java.math.BigInteger",
                "java.math.MathContext",
                "java.math.RoundingMode",
                "java.time.LocalDate",
                "java.time.LocalDateTime",
                "java.time.LocalTime",
                "java.util.List",
                "java.util.Locale",
                "java.util.Map",
                "java.util.Optional",
                "java.util.Set",
                "json",
                "json-property-name",
                "list",
                "local-date",
                "local-datetime",
                "local-time",
                "locale",
                "long",
                "marshall",
                "math-context",
                "number",
                "optional",
                "rounding-mode",
                "set",
                "short",
                "string",
                "walkingkooka.tree.json.JsonArrayNode",
                "walkingkooka.tree.json.JsonBooleanNode",
                "walkingkooka.tree.json.JsonNode",
                "walkingkooka.tree.json.JsonNodeName",
                "walkingkooka.tree.json.JsonNullNode",
                "walkingkooka.tree.json.JsonNumberNode",
                "walkingkooka.tree.json.JsonObjectNode",
                "walkingkooka.tree.json.JsonStringNode");
    }
}
