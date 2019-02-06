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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.PathSeparator;
import walkingkooka.naming.StringName;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.TestNode;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract public class NodeSelectorTestCase<S extends NodeSelector<TestNode, StringName, StringName, Object>>
        extends ClassTestCase<S>
        implements HashCodeEqualsDefinedTesting<S>,
        ToStringTesting<S> {

    final static PathSeparator SEPARATOR = PathSeparator.requiredAtStart('/');

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
    }

    NodeSelectorTestCase() {
        super();
    }

    @Test
    public final void testNaming() {
        this.checkNaming(NodeSelector.class);
    }

    @Test
    public void testAppendNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createSelector().append(null);
        });
    }

    @Test
    public final void testSetToStringNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createSelector().setToString(null);
        });
    }

    @Test
    public final void testSetToString() {
        final S selector = this.createSelector();

        final String toString = "custom " + selector;
        final NodeSelector<TestNode, StringName, StringName, Object> custom = selector.setToString(toString);
        assertEquals(toString, custom.toString(), "toString");
    }

    abstract S createSelector();

    final void acceptAndCheck(final TestNode start) {
        this.acceptAndCheck0(this.createSelector(), start, new String[0]);
    }

    final void acceptAndCheck(final TestNode start, final String... nodes) {
        this.acceptAndCheck0(this.createSelector(), start, nodes);
    }

    final void acceptAndCheck(final TestNode start, final TestNode... nodes) {
        this.acceptAndCheck(this.createSelector(), start, nodes);
    }

    @SafeVarargs
    final void acceptAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                               final TestNode start,
                               final TestNode... nodes) {
        this.acceptAndCheck0(selector,
                start,
                Arrays.stream(nodes)
                        .map( n  -> n.name().value())
                        .toArray(size  -> new String[ size]));
    }

    abstract void acceptAndCheck0(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                               final TestNode start,
                               final String... nodes);

    final void acceptAndCheckUsingContext(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                          final TestNode start,
                                          final String... nodes) {
        final Set<TestNode> potential = Sets.ordered();
        final Set<TestNode> selected = Sets.ordered();
        selector.accept(start, context(
                (n)->potential.add(n),
                (n)->selected.add(n)));
        final List<String> selectedNames = selected.stream()
                .map(n -> n.name().value())
                .collect(Collectors.toList());
        assertEquals(Lists.of(nodes), selectedNames, () -> "Selector.accept\n" + start);
        assertNotEquals(Sets.empty(), potential, "potentials must not be empty");
        assertTrue(potential.contains(start), ()-> "potentials must include initial node=" + potential);
    }

    final NodeSelectorContext<TestNode, StringName, StringName, Object> context(final Consumer<TestNode> potential,
                                                                                final Consumer<TestNode> selected) {
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

    @Override
    public final S createObject() {
        return this.createSelector();
    }
}
