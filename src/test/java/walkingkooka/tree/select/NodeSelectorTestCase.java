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
 */

package walkingkooka.tree.select;

import org.junit.Before;
import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.PathSeparator;
import walkingkooka.naming.StringName;
import walkingkooka.test.ClassTestCase;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.type.MemberVisibility;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

abstract public class NodeSelectorTestCase<S extends NodeSelector<TestFakeNode, StringName, StringName, Object>> extends ClassTestCase<S> {

    final static PathSeparator SEPARATOR = PathSeparator.requiredAtStart('/');

    @Before
    public void beforeEachTest() {
        TestFakeNode.names.clear();
    }

    NodeSelectorTestCase() {
        super();
    }

    @Test
    public final void testNaming() {
        this.checkNaming(NodeSelector.class);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendNullFails() {
        this.createSelector().append(null);
    }

    @Test(expected = NullPointerException.class)
    public final void testSetToStringNullFails() {
        this.createSelector().setToString(null);
    }

    @Test
    public final void testSetToString() {
        final S selector = this.createSelector();

        final String toString = "custom " + selector;
        final NodeSelector<TestFakeNode, StringName, StringName, Object> custom = selector.setToString(toString);
        assertEquals("toString", toString, custom.toString());
    }

    abstract S createSelector();

    final void acceptAndCheck(final TestFakeNode start) {
        this.acceptAndCheck0(this.createSelector(), start, new String[0]);
    }

    final void acceptAndCheck(final TestFakeNode start, final String... nodes) {
        this.acceptAndCheck0(this.createSelector(), start, nodes);
    }

    final void acceptAndCheck(final TestFakeNode start, final TestFakeNode... nodes) {
        this.acceptAndCheck(this.createSelector(), start, nodes);
    }

    @SafeVarargs
    final void acceptAndCheck(final NodeSelector<TestFakeNode, StringName, StringName, Object> selector,
                               final TestFakeNode start,
                               final TestFakeNode... nodes) {
        this.acceptAndCheck0(selector,
                start,
                Arrays.stream(nodes)
                        .map( n  -> n.name().value())
                        .toArray(size  -> new String[ size]));
    }

    abstract void acceptAndCheck0(final NodeSelector<TestFakeNode, StringName, StringName, Object> selector,
                               final TestFakeNode start,
                               final String... nodes);

    final void acceptAndCheckUsingContext(final NodeSelector<TestFakeNode, StringName, StringName, Object> selector,
                                          final TestFakeNode start,
                                          final String... nodes) {
        final Set<TestFakeNode> potential = Sets.ordered();
        final Set<TestFakeNode> selected = Sets.ordered();
        selector.accept(start, context(
                (n)->potential.add(n),
                (n)->selected.add(n)));
        final List<String> selectedNames = selected.stream()
                .map(n -> n.name().value())
                .collect(Collectors.toList());
        assertEquals("Selector.accept\n" + start, Lists.of(nodes), selectedNames);
        assertNotEquals("potentials must not be empty", Sets.empty(), potential);
        assertTrue("potentials must include initial node=" + potential, potential.contains(start));
    }

    final NodeSelectorContext<TestFakeNode, StringName, StringName, Object> context(final Consumer<TestFakeNode> potential,
                                                                                    final Consumer<TestFakeNode> selected) {
        return NodeSelectorContexts.basic(potential,
                selected,
                this.functions(),
                this.converter(),
                DecimalNumberContexts.fake());
    }

    final Function<ExpressionNodeName, Optional<ExpressionFunction<?>>> functions() {
        return NodeSelectorContexts.basicFunctions();
    }

    final Converter converter() {
        return new Converter() {
            @Override
            public boolean canConvert(final Object value, final Class<?> type, final ConverterContext context) {
                return false;
            }

            @Override
            public <T> T convert(final Object value, final Class<T> type, final ConverterContext context) {
                if(type.isInstance(value)) {
                    return type.cast(value);
                }
                if(value instanceof String && type == Integer.class) {
                    return type.cast(Integer.parseInt(String.valueOf(value)));
                }
                return failConversion(value, type);
            }
        };
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
