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

package walkingkooka.tree;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchSequenceNode;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class TraversableHasTextOffsetTest implements HasTextOffsetTesting {

    @Test
    public void testRootLeaf() {
        this.textOffsetAndCheck(text("abc"),
                0);
    }

    @Test
    public void testRootParentWithChildren() {
        this.textOffsetAndCheck(text("123")
                        .selected(),
                0);
    }

    @Test
    public void testFirstChild() {
        this.textOffsetAndCheck(sequence("123")
                .selected()
                .child(), 0);
    }

    @Test
    public void testFirstChild2() {
        this.textOffsetAndCheck(sequence("1a", "2b")
                        .children().get(0),
                0);
    }

    @Test
    public void testSecondChild() {
        this.textOffsetAndCheck(sequence("1a", "2b")
                        .children()
                        .get(1),
                "1a");
    }

    @Test
    public void testThirdChild() {
        this.textOffsetAndCheck(sequence(
                text("1a"),
                sequence("2b", "3c"),
                text("4d"))
                        .children().get(2),
                "1a2b3c");
    }

    @Test
    public void testThirdChildDeep() {
        this.textOffsetAndCheck(sequence(
                text("1a"),
                sequence("2b", "3c", "4d"))
                        .children().get(1)
                        .children().get(2),
                "1a2b3c");
    }

    private static SearchNode text(final String text) {
        return SearchNode.text(text, text);
    }

    private static SearchSequenceNode sequence(final String... text) {
        return SearchNode.sequence(Arrays.stream(text)
                .map(TraversableHasTextOffsetTest::text)
                .collect(Collectors.toList()));
    }

    private static SearchSequenceNode sequence(final SearchNode... nodes) {
        return SearchNode.sequence(Lists.of(nodes));
    }
}
