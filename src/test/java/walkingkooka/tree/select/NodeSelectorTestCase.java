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
import walkingkooka.naming.PathSeparator;
import walkingkooka.naming.StringName;
import walkingkooka.test.PackagePrivateClassTestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

abstract public class NodeSelectorTestCase<S extends NodeSelector<TestFakeNode, StringName, StringName, Object>> extends PackagePrivateClassTestCase<S> {

    final static PathSeparator SEPARATOR = PathSeparator.requiredAtStart('/');

    @Before
    public void beforeEachTest() {
        TestFakeNode.names.clear();
    }

    NodeSelectorTestCase() {
        super();
    }

    @Test(expected = NullPointerException.class)
    public void testAppendNullFails() {
        this.createSelector().append(null);
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
        final Set<TestFakeNode> matched = selector.accept(start);
        final List<String> matchedNodes = matched.stream()
                .map(n -> n.name().value())
                .collect(Collectors.toList());
        assertEquals("Selector.accept\n" + start, Lists.of(nodes), matchedNodes);
    }
}
