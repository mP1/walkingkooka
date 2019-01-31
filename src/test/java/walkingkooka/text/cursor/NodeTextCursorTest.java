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

package walkingkooka.text.cursor;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.naming.StringName;

public final class NodeTextCursorTest extends TextCursorTestCase<NodeTextCursor<NodeTextCursorTestNode, StringName, Name, Object>> {

    // in all tests below text with numbers should be skipped because nodes with children text is ignored.

    @Test
    public void testParentAndChild() {
        final TextCursor cursor = NodeTextCursor.with(
                new NodeTextCursorTestNode("parent", "123",
                        new NodeTextCursorTestNode("child", "ABC")));
        this.checkNotEmpty(cursor);

        this.atAndCheck(cursor, 'A');
        cursor.next();
        this.atAndCheck(cursor, 'B');
        cursor.next();
        this.atAndCheck(cursor, 'C');
        cursor.next();

        this.checkEmpty(cursor);
    }

    @Test
    public void testParentAndChild2() {
        final TextCursor cursor = NodeTextCursor.with(
                new NodeTextCursorTestNode("parent", "123",
                        new NodeTextCursorTestNode("child", "A"),
                        new NodeTextCursorTestNode("child", "B")));
        this.checkNotEmpty(cursor);

        this.atAndCheck(cursor, 'A');
        cursor.next();
        this.atAndCheck(cursor, 'B');
        cursor.next();

        this.checkEmpty(cursor);
    }

    @Test
    public void testParentAndChildAndGrandchildren() {
        final TextCursor cursor = NodeTextCursor.with(
                new NodeTextCursorTestNode("parent", "123",
                        new NodeTextCursorTestNode("child1", "A"),
                        new NodeTextCursorTestNode("child2", "456",
                                new NodeTextCursorTestNode("grandChild-1", "B"),
                                new NodeTextCursorTestNode("grandChild-2", "C")),
                        new NodeTextCursorTestNode("child3", "D")));
        this.checkNotEmpty(cursor);

        this.atAndCheck(cursor, 'A');
        cursor.next();
        this.atAndCheck(cursor, 'B');
        cursor.next();
        this.atAndCheck(cursor, 'C');
        cursor.next();
        this.atAndCheck(cursor, 'D');
        cursor.next();

        this.checkEmpty(cursor);
    }

    @Test
    public void testGraphSaveAndRestores() {
        final TextCursor cursor = NodeTextCursor.with(
                new NodeTextCursorTestNode("parent", "123",
                        new NodeTextCursorTestNode("child1", "A"),
                        new NodeTextCursorTestNode("child2", "456",
                                new NodeTextCursorTestNode("grandChild-1", "B"),
                                new NodeTextCursorTestNode("grandChild-2", "C")),
                        new NodeTextCursorTestNode("child3", "D")));
        this.checkNotEmpty(cursor);
        this.atAndCheck(cursor, 'A');
        cursor.next();

        final TextCursorSavePoint save = cursor.save();

        this.atAndCheck(cursor, 'B');
        cursor.next();
        this.atAndCheck(cursor, 'C');
        cursor.next();
        this.atAndCheck(cursor, 'D');
        cursor.next();

        this.checkEmpty(cursor);

        save.restore(); // BCD

        this.atAndCheck(cursor, 'B');
        cursor.next();

        save.save();

        this.atAndCheck(cursor, 'C');
        cursor.next();
        this.atAndCheck(cursor, 'D');
        cursor.next();

        this.checkEmpty(cursor);

        save.restore(); // CD

        this.atAndCheck(cursor, 'C');
        cursor.next();
        this.atAndCheck(cursor, 'D');
        cursor.next();

        this.checkEmpty(cursor);
    }

    @Override
    protected NodeTextCursor<NodeTextCursorTestNode, StringName, Name, Object> createTextCursor(final String text) {
        return NodeTextCursor.with(new NodeTextCursorTestNode("root-without-children", text));
    }

    @Override
    protected Class<NodeTextCursor<NodeTextCursorTestNode, StringName, Name, Object>> type() {
        return Cast.to(NodeTextCursor.class);
    }
}
