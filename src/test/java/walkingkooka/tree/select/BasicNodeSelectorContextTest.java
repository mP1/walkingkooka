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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicNodeSelectorContextTest extends NodeSelectorContextTestCase<BasicNodeSelectorContext<TestNode, StringName, StringName, Object>,
        TestNode,
        StringName,
        StringName,
        Object> {

    @Test
    public void NullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            BasicNodeSelectorContext.with(null,
                    this.selected(),
                    this.functions(),
                    this.converter(),
                    this.decimalNumberContext());
        });
    }

    @Test
    public void testWithNullSelectedFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicNodeSelectorContext.with(this.potential(),
                    null,
                    this.functions(),
                    this.converter(),
                    this.decimalNumberContext());
        });
    }

    @Test
    public void testWithNullFunctionsFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicNodeSelectorContext.with(this.potential(),
                    this.selected(),
                    null,
                    this.converter(),
                    this.decimalNumberContext());
        });
    }

    @Test
    public void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicNodeSelectorContext.with(this.potential(),
                    this.selected(),
                    this.functions(),
                    null,
                    this.decimalNumberContext());
        });
    }

    @Test
    public void testWithNullDecimalNumberContextFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicNodeSelectorContext.with(this.potential(),
                    this.selected(),
                    this.functions(),
                    this.converter(),
                    null);
        });
    }

    @Override public String typeNameSuffix() {
        return NodeSelectorContext.class.getSimpleName();
    }

    @Override
    protected BasicNodeSelectorContext<TestNode, StringName, StringName, Object> createContext() {
        return BasicNodeSelectorContext.with(this.potential(),
                this.selected(),
                this.functions(),
                this.converter(),
                this.decimalNumberContext());
    }

    private Consumer<TestNode> potential() {
        return (n)->{};
    }

    private Consumer<TestNode> selected() {
        return (n)->{};
    }

    private Function<ExpressionNodeName, Optional<ExpressionFunction<?>>> functions() {
        return NodeSelectorContexts.basicFunctions();
    }

    private Converter converter() {
        return Converters.fake();
    }

    private DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.fake();
    }

    @Override
    public Class<BasicNodeSelectorContext<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(BasicNodeSelectorContext.class);
    }
}
