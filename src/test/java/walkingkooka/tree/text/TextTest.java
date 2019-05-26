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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class TextTest extends TextLeafNodeTestCase<Text, String>{

    @Override
    public void testTypeNaming() {
    }

    @Test
    public void testWithEmpty() {
        this.createTextNodeAndCheck("");
    }

    @Test
    public void testWith2() {
        final String value = "abc123";
        this.checkText(Text.with(value), value);
    }

    @Test
    public void testSetTextSame() {
        final String value = "abc123";
        final Text text = Text.with(value);
        assertSame(text, text.setText(value));
    }

    @Test
    public void testSetTextDifferent() {
        final String before = "abc123";
        final Text text = Text.with(before);

        final String value = "different";
        final Text different = text.setText(value);
        assertNotSame(text, different);

        this.checkText(different, value);
        this.checkText(text, before);
    }

    @Test
    public void testSetTextDifferentWithParent() {
        final TextNode parent1 = Text.properties(Lists.of(Text.with("abc123")));
        final Text text1 = parent1.children().get(0).cast();

        final String value = "different456";
        final Text different = text1.setText(value);

        this.checkText(different, value);
        this.childCountCheck(different.parentOrFail(), different);
    }

    private void checkText(final Text text, final String value) {
        assertEquals(value, text.text(), "text");
        assertEquals(value, text.value(), "value");
    }

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(Text.with("ABC123"));
    }

    @Test
    public void testIsMethods() {
        final Text text = Text.with("abc");
        assertEquals(true, text.isText(), "isText");
        assertEquals(false, text.isProperties(), "isProperties");
        assertEquals(false, text.isStyled(), "isStyled");
    }

    @Test
    public void testToStringEmpty() {
        this.toStringAndCheck(Text.with(""), "\"\"");
    }

    // Visitor ........................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final TextNode node = this.createTextNode();

        new FakeTextNodeVisitor() {
            @Override
            protected Visiting startVisit(final TextNode n) {
                assertSame(node, n);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextNode n) {
                assertSame(node, n);
                b.append("2");
            }

            @Override
            protected void visit(final Text n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }

    // ToString ........................................................................................................

    @Test
    public void testToStringNotEmpty() {
        this.toStringAndCheck(Text.with("abc"), "\"abc\"");
    }

    @Test
    public void testToStringNotEmptyEscaping() {
        this.toStringAndCheck(Text.with("abc\tdef"), "\"abc\\tdef\"");
    }

    @Override
    Text createTextNode(final String value) {
        return Text.with(value);
    }

    @Override
    String value() {
        return "abc123";
    }

    @Override
    Class<Text> textNodeType() {
        return Text.class;
    }
}
