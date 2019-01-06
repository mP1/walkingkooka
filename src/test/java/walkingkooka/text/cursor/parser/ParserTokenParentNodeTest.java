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
package walkingkooka.text.cursor.parser;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.tree.select.FakeNodeSelectorContext;
import walkingkooka.tree.select.NodeSelector;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public final class ParserTokenParentNodeTest extends ParserTokenNodeTestCase<ParserTokenParentNode> {

    private final static String TEXT = "a1b2";

    private final static StringParserToken STRING1 = string("a1");
    private final static StringParserToken STRING2 = string("b2");
    private final static StringParserToken STRING3 = string("c3");
    private final static StringParserToken STRING4 = string("d4");
    private final static StringParserToken STRING5 = string("e5");
    private final static StringParserToken STRING6 = string("f6");

    @Test
    public void testParent() {
        assertEquals(Optional.empty(), this.createParserTokenNode().parent());
    }

    @Test
    public void testChildren() {
        final ParserTokenParentNode node = this.createParserTokenNode();
        this.childrenCheck2(node, STRING1, STRING2);
    }

    @Test
    public void testSetChildrenSame() {
        final ParserTokenParentNode node = this.createParserTokenNode();
        final ParserTokenNode node2 = node.setChildren(node.children());
        assertSame(node, node2);
    }

    @Test
    public void testSetChildrenSame2() {
        final ParserTokenParentNode node = this.createParserTokenNode();
        final ParserTokenNode node2 = node.setChildren(node.children().stream().collect(Collectors.toList()));
        assertSame(node, node2);
    }

    @Test
    public void testSetChildrenDifferent() {
        final ParserTokenParentNode node = this.createParserTokenNode();
        final ParserTokenNode node2 = node.setChildren(children(STRING3, STRING4));
        assertNotSame(node, node2);

        this.childrenCheck2(node2, STRING3, STRING4);
        this.childrenCheck2(node, STRING1, STRING2);
    }

    @Test
    public void testSetChildrenWithParent() {
        final ParserTokenParentNode root = sequence("a1b2c3d4",
                STRING1,
                ParserTokens.sequence(Lists.of(STRING2, STRING3), "b2c3"),
                STRING4);
        final ParserTokenNode child = root.children().get(1);
        final ParserTokenNode child2 = child.setChildren(children(STRING5, STRING6));
        assertNotSame("a different node should have been returned after setting new children", child, child2);

        final ParserTokenNode parent = child2.parent().get();
        this.childrenCheck2(parent,
                STRING1,
                ParserTokens.sequence(Lists.of(STRING5, STRING6), "b2c3"),
                STRING4);
    }

    @Test
    public void testSetChildrenValuesSame() {
        final ParserTokenParentNode node = this.createParserTokenNode();
        final ParserTokenNode node2 = node.setChildrenValues(node.childrenValues());
        assertSame(node, node2);
    }

    @Test
    public void testSetChildrenValuesSame2() {
        final ParserTokenParentNode node = this.createParserTokenNode();
        final ParserTokenNode node2 = node.setChildrenValues(node.childrenValues().stream().collect(Collectors.toList()));
        assertSame(node, node2);
    }

    @Test
    public void testSetChildrenValuesDifferent() {
        final ParserTokenParentNode node = this.createParserTokenNode();
        final ParserTokenNode node2 = node.setChildrenValues(Lists.of(STRING3, STRING4));
        assertNotSame(node, node2);

        this.childrenCheck2(node2, STRING3, STRING4);
        this.childrenCheck2(node, STRING1, STRING2);
    }

    @Test
    public void testAttributes() {
        assertEquals(Maps.one(ParserTokenNodeAttributeName.TEXT, TEXT), this.createParserTokenNode().attributes());
    }

    @Test
    public void testSetAttributesDifferent() {
        final ParserTokenParentNode node = this.createParserTokenNode();

        final String differentText = "different";
        final Map<ParserTokenNodeAttributeName, String> attributes = Maps.one(ParserTokenNodeAttributeName.TEXT, differentText);
        final ParserTokenNode different = node.setAttributes(attributes);
        assertNotSame(node, different);

        assertEquals("attributes", attributes, different.attributes());
        assertEquals(node.value().setText(differentText).asNode(), different);
    }

    @Test
    public void testSetAttributesChildDifferent() {
        final ParserTokenParentNode parent = this.createParserTokenNode();
        final ParserTokenNode child = parent.children().get(0);

        final String differentText = "different";
        final Map<ParserTokenNodeAttributeName, String> attributes = Maps.one(ParserTokenNodeAttributeName.TEXT, differentText);
        final ParserTokenNode differentChild = child.setAttributes(attributes);
        assertNotSame(child, differentChild);

        assertEquals("attributes", attributes, differentChild.attributes());
        assertEquals(child.value().setText(differentText).asNode(), differentChild);

        final Optional<ParserTokenNode> maybeDifferentParent = differentChild.parent();
        final ParserTokenNode differentParent = maybeDifferentParent.get();
        assertEquals("different parent", sequence(TEXT, STRING1.setText(differentText), STRING2), differentParent);
    }

    @Test
    public void testSelectorByName() {
        final NodeSelector<ParserTokenNode, ParserTokenNodeName, ParserTokenNodeAttributeName, String> selector = ParserTokenNode.PATH_SEPARATOR.absoluteNodeSelectorBuilder(ParserTokenNode.class)
                .descendant()
                .named(StringParserToken.NAME)
                .build();

        final ParserTokenParentNode root = sequence("a1b2c3d4",
                STRING1,
                ParserTokens.sequence(Lists.of(STRING2, STRING3, ParserTokens.bigInteger(BigInteger.ZERO, "**")), "b2c3**"),
                STRING4);

        final Set<ParserTokenNode> selected = Sets.ordered();
        selector.accept(root, new FakeNodeSelectorContext<ParserTokenNode, ParserTokenNodeName, ParserTokenNodeAttributeName, String>(){
            @Override
            public void potential(final ParserTokenNode node) {

            }

            @Override
            public void selected(final ParserTokenNode node) {
                selected.add(node);
            }
        });

        assertEquals(Lists.of("a1", "b2", "c3", "d4"),
                selected.stream()
                        .map(n -> n.value().text())
                        .collect(Collectors.toList()));
    }

    @Override
    ParserTokenParentNode createParserTokenNode() {
        return sequence(TEXT, STRING1, STRING2);
    }

    private ParserTokenParentNode sequence(final String text, final ParserToken...tokens) {
        return Cast.to(ParserTokens.sequence(Lists.of(tokens), text).asNode());
    }

    private List<ParserTokenNode> children(final ParserToken...tokens) {
        return Arrays.stream(tokens)
                .map(t -> t.asNode())
                .collect(Collectors.toList());
    }

    private void childrenCheck2(final ParserTokenNode node, final ParserToken...tokens) {
        this.childrenCheck(node);

        int i = 0;
        for(ParserTokenNode child: node.children()) {
            assertEquals("token for child: " + i, tokens[i], child.token);
            i++;
        }
    }

    @Override
    Class<ParserTokenParentNode> parserTokenNodeType() {
        return ParserTokenParentNode.class;
    }
}
