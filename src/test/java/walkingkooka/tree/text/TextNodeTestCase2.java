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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.text.HasTextLengthTesting;
import walkingkooka.text.HasTextTesting;
import walkingkooka.tree.HasTextOffsetTesting;
import walkingkooka.tree.Node;
import walkingkooka.tree.NodeTesting;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.type.JavaVisibility;

import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class TextNodeTestCase2<N extends TextNode> extends TextNodeTestCase<TextNode>
        implements NodeTesting<TextNode, TextNodeName, TextStylePropertyName<?>, Object>,
        HasJsonNodeTesting<TextNode>,
        HasTextLengthTesting,
        HasTextOffsetTesting,
        HasTextTesting,
        IsMethodTesting<N> {

    TextNodeTestCase2() {
        super();
    }

    // SetAttributes....................................................................................................

    @Test
    public final void testSetAttributesEmpty() {
        final N textNode = this.createTextNode();
        assertSame(textNode, textNode.setAttributes(TextNode.NO_ATTRIBUTES));
    }

    final void setAttributeNotEmptyAndCheck() {
        final N before = this.createTextNode();
        final Map<TextStylePropertyName<?>, Object> attributes = Maps.of(TextStylePropertyName.FONT_STYLE, FontStyle.ITALIC);
        final TextNode after = before.setAttributes(attributes);
        assertNotSame(after, before);

        final TextStyleNode parent = after.parentOrFail().cast();
        this.childCountCheck(parent, before);
        assertEquals(attributes, parent.attributes(), "attributes");
    }

    // textStyle........................................................................................................

    @Test
    public final void testTextStyle() {
        final N textNode = this.createTextNode();
        assertEquals(textNode.attributes(), textNode.textStyle().textStyleMap());
        assertEquals(TextStyle.with(textNode.attributes()), textNode.textStyle());
    }

    // HasTextOffset .....................................................................................................

    @Test
    public final void testTextOffset() {
        this.textOffsetAndCheck(this.createTextNode(), 0);
    }

    // TextNodeVisitor..................................................................................................

    @Test
    public final void testVisitor() {
        new TextNodeVisitor(){}.accept(this.createNode());
    }

    // helpers .........................................................................................................

    @Override
    public final TextNode createNode() {
        return this.createTextNode();
    }

    abstract N createTextNode();

    // ClassTesting.....................................................................................................

    @Override
    public final Class<TextNode> type() {
        return Cast.to(this.textNodeType());
    }

    abstract Class<N> textNodeType();

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // TypeNameTesting...................................................................................................

    @Override
    public final String typeNamePrefix() {
        return "Text";
    }

    // IsMethodTesting...................................................................................................

    @Override
    public final N createIsMethodObject() {
        return this.createTextNode();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "Text";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return Node.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (n) -> n.equals("isText") || n.equals("isRoot");
    }

    // JsonNodeTesting...................................................................................................

    @Override
    public final TextNode createHasJsonNode() {
        return this.createTextNode();
    }
}