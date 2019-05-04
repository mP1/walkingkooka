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
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract public class NodeSelectorTestCase3<S extends NodeSelector<TestNode, StringName, StringName, Object>> extends NodeSelectorTestCase2<S>
        implements HashCodeEqualsDefinedTesting<S>,
        ToStringTesting<S> {

    NodeSelectorTestCase3() {
        super();
    }

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
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
                        .map(n -> n.name().value())
                        .toArray(size -> new String[size]));
    }

    abstract void acceptAndCheck0(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                  final TestNode start,
                                  final String... nodes);

    final void acceptAndCheckUsingContext(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                          final TestNode start,
                                          final String... nodes) {
        final Set<TestNode> potential = Sets.ordered();
        final Set<TestNode> selected = Sets.ordered();
        assertSame(start, selector.accept(start, this.context(
                (n) -> potential.add(n),
                (n) -> selected.add(n))));
        final List<String> selectedNames = selected.stream()
                .map(n -> n.name().value())
                .collect(Collectors.toList());
        assertEquals(Lists.of(nodes), selectedNames, () -> "Selector.accept\n" + start);
        assertNotEquals(Sets.empty(), potential, "potentials must not be empty");
        assertTrue(potential.contains(start), () -> "potentials must include initial node=" + potential);
    }

    final void acceptMapAndCheck(final TestNode start) {
        this.acceptMapAndCheck(start, start);
    }

    final void acceptMapAndCheck(final TestNode start,
                                 final TestNode result) {
        this.acceptMapAndCheck(this.createSelector(), start, result);
    }

    final void acceptMapAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                 final TestNode start) {
        this.acceptMapAndCheck(selector, start, start);
    }

    final void acceptMapAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                 final TestNode start,
                                 final TestNode result) {
        TestNode.clear();

        final String startToString = start.toString();

        assertEquals(result,
                selector.accept(start, this.context0((n) -> {
                        },
                        new Function<TestNode, TestNode>() {
                            @Override
                            public TestNode apply(final TestNode testNode) {
                                return TestNode.with(testNode.name().value() + "*" + this.i++)
                                        .setChildren(testNode.children());
                            }

                            private int i;

                            @Override
                            public String toString() {
                                return "mapper: next: " + i;
                            }
                        })));

        assertEquals(startToString,
                start.toString(),
                "toString has changed for starting node, indicating it probably was mutated");
    }

    final NodeSelectorContext<TestNode, StringName, StringName, Object> context(final Consumer<TestNode> potential,
                                                                                final Consumer<TestNode> selected) {
        return this.context0(potential,
                (n) -> {
                    this.checkSelectCaller();
                    selected.accept(n);
                    return n;
                });
    }

    /**
     * Scan the call stack, expecting the callingNodeSelector ignoring contexts to be {@link TerminalNodeSelector}.
     */
    private void checkSelectCaller() {
        final Class<?> caller = this.callingNodeSelector();

        assertEquals(Optional.of(caller.getSimpleName()),
                Arrays.stream(Thread.currentThread().getStackTrace())
                        .map(this::simpleClassName)
                        .peek(c -> System.out.println(c))
                        .filter(c -> c.endsWith(NodeSelector.class.getSimpleName()))
                        .findFirst(),
                () -> "Expected callingNodeSelector to be " + caller.getName());
    }

    /**
     * Should return {@link TerminalNodeSelector} except for {@link AndNodeSelector} tests.
     */
    abstract Class<? extends NodeSelector> callingNodeSelector();

    private String simpleClassName(final StackTraceElement element) {
        final String className = element.getClassName();
        final int dot = className.lastIndexOf('.');
        return -1 == dot ?
                className :
                className.substring(dot + 1);
    }

    final NodeSelectorContext<TestNode, StringName, StringName, Object> context0(final Consumer<TestNode> potential,
                                                                                 final Function<TestNode, TestNode> mapper) {
        return NodeSelectorContexts.basic(potential,
                mapper,
                this.functions(),
                this.converter(),
                DecimalNumberContexts.fake());
    }

    final TestNode nodeWithAttributes(final String name, final String attribute, final String value) {
        return TestNode.with(name).setAttributes(this.attributes(attribute, value));
    }

    final Map<StringName, Object> attributes(final String name, final Object value) {
        return Maps.of(Names.string(name), value);
    }

    final Function<ExpressionNodeName, Optional<ExpressionFunction<?>>> functions() {
        return NodeSelectorContexts.basicFunctions();
    }

    final Converter converter() {
        return Converters.collection(Lists.of(
                Converters.numberInteger(),
                Converters.function(String.class, Integer.class, Integer::parseInt),
                Converters.simple()
        ));
    }

    @Override
    public final S createObject() {
        return this.createSelector();
    }
}
