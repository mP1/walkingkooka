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

package walkingkooka.tree.pointer;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class RelativeNodePointerTest extends NodePointerTestCase2<RelativeNodePointer<TestNode, StringName>> {

    private final static boolean NO_HASH = false;
    private final static boolean HASH = !NO_HASH;

    // with.............................................................................................................

    @Test
    public void testWithNegativeIndexFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            RelativeNodePointer.with(-1, NO_HASH);
        });
    }

    @Test
    public void testWithZero() {
        this.withAndCheck(0);
    }

    @Test
    public void testWith() {
        this.withAndCheck(1);
    }

    @Test
    public void testWith2() {
        this.withAndCheck(10);
    }

    private void withAndCheck(final int ancestorCount) {
        final RelativeNodePointer<TestNode, StringName> pointer = RelativeNodePointer.with(ancestorCount, NO_HASH);
        assertEquals(ancestorCount, pointer.ancestorCount, "ancestorCount");
    }

    // add..............................................................................................................

    @Test
    public void testAdd() {
        final TestNode value = TestNode.with("added");

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("first"))
                .appendChild(TestNode.with("replaced"));

        this.addAndCheck(NodePointer.relative(0, TestNode.class)
                        .appendToLast(IndexedChildNodePointer.with(1)),
                root,
                value,
                root.setChild(1, value));
    }

    // remove...........................................................................................................

    @Test
    public void testRemove() {
        final TestNode start = TestNode.with("object")
                .appendChild(TestNode.with("first"))
                .appendChild(TestNode.with("removed"));

        this.removeAndCheck(NodePointer.relative(0, TestNode.class)
                        .appendToLast(IndexedChildNodePointer.with(1)),
                start,
                start.removeChild(1));
    }

    // equals...........................................................................................................

    @Test
    public final void testEqualsAncestor() {
        this.checkNotEquals(RelativeNodePointer.with(99, NO_HASH));
    }

    @Test
    public final void testEqualsHash() {
        this.checkNotEquals(RelativeNodePointer.with(1, !NO_HASH));
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(RelativeNodePointer.with(1, NO_HASH), "1");
    }

    @Test
    public void testToStringHash() {
        this.toStringAndCheck(RelativeNodePointer.with(1, HASH), "1#");
    }

    @Test
    public void testToStringAndIndex() {
        this.toStringAndCheck(RelativeNodePointer.with(1, NO_HASH), "1");
    }

    // visitor..........................................................................................................

    @Test
    public void testVisitor() {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<TestNode, StringName>() {
            @Override
            protected Visiting startVisit(final NodePointer<TestNode, StringName> node) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodePointer<TestNode, StringName> node) {
                b.append("2");
            }

            @Override
            protected Visiting startVisit(final RelativeNodePointer<TestNode, StringName> node) {
                b.append("3");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final RelativeNodePointer<TestNode, StringName> node) {
                b.append("4");
            }

        }.accept(this.createNodePointer());

        assertEquals("1342", b.toString());
    }

    @Test
    public void testVisitorWithNext() {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<TestNode, StringName>() {
            @Override
            protected Visiting startVisit(final NodePointer<TestNode, StringName> node) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodePointer<TestNode, StringName> node) {
                b.append("2");
            }

            @Override
            protected void visit(final AppendNodePointer<TestNode, StringName> node) {
                b.append("3");
            }

            @Override
            protected Visiting startVisit(final RelativeNodePointer<TestNode, StringName> node) {
                b.append("4");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final RelativeNodePointer<TestNode, StringName> node) {
                b.append("6");
            }

        }.accept(this.createNodePointer().append());

        assertEquals("1413262", b.toString());
    }

    @Test
    public void testVisitorWithNextSkipped() {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<TestNode, StringName>() {
            @Override
            protected Visiting startVisit(final NodePointer<TestNode, StringName> node) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodePointer<TestNode, StringName> node) {
                b.append("2");
            }

            @Override
            protected Visiting startVisit(final RelativeNodePointer<TestNode, StringName> node) {
                b.append("4");
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final RelativeNodePointer<TestNode, StringName> node) {
                b.append("5");
            }

        }.accept(this.createNodePointer().append());

        assertEquals("1452", b.toString());
    }

    // helpers..........................................................................................................

    @Override
    RelativeNodePointer<TestNode, StringName> createNodePointer() {
        return RelativeNodePointer.with(1, false);
    }

    @Override
    public Class<RelativeNodePointer<TestNode, StringName>> type() {
        return Cast.to(RelativeNodePointer.class);
    }
}
