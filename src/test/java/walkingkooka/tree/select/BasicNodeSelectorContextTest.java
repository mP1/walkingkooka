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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.StringName;

import java.util.function.Consumer;

public final class BasicNodeSelectorContextTest extends NodeSelectorContextTestCase<BasicNodeSelectorContext<TestFakeNode, StringName, StringName, Object>,
        TestFakeNode,
        StringName,
        StringName,
        Object> {

    @Test(expected = NullPointerException.class)
    public void testWithNullPotentialFails() {
        BasicNodeSelectorContext.with(null, this.selected(), this.converter(), this.decimalNumberContext());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullSelectedFails() {
        BasicNodeSelectorContext.with(this.potential(), null, this.converter(), this.decimalNumberContext());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullConverterFails() {
        BasicNodeSelectorContext.with(this.potential(), this.selected(), null, this.decimalNumberContext());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullDecimalNumberContextFails() {
        BasicNodeSelectorContext.with(this.potential(), this.selected(), this.converter(), null);
    }

    @Override
    protected String requiredNameSuffix() {
        return NodeSelectorContext.class.getSimpleName();
    }

    @Override
    protected BasicNodeSelectorContext<TestFakeNode, StringName, StringName, Object> createContext() {
        return BasicNodeSelectorContext.with(this.potential(), this.selected(), this.converter(), this.decimalNumberContext());
    }

    private Consumer<TestFakeNode> potential() {
        return (n)->{};
    }

    private Consumer<TestFakeNode> selected() {
        return (n)->{};
    }

    private Converter converter() {
        return Converters.fake();
    }

    private DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.fake();
    }

    @Override
    protected Class<BasicNodeSelectorContext<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(BasicNodeSelectorContext.class);
    }
}
