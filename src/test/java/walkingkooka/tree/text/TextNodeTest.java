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
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converters;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.select.NodeSelectorContexts;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TextNodeTest extends TextNodeTestCase<TextNode> implements HasJsonNodeTesting<TextNode>,
        ToStringTesting<TextNode> {

    @Test
    public void testFromJsonNode() {
        final TextNode text = this.createHasJsonNode();
        final JsonNode jsonNode = text.toJsonNodeWithType();
        assertEquals(text,
                TextNode.fromJsonNode(jsonNode),
                () -> "" + text);
    }

    @Override
    public void testToJsonNodeRoundtripTwice() {
        // nop
    }

    @Test
    public void testBuildAndCheckToString() {
        this.toStringAndCheck(TextNode.style(Lists.of(
                TextNode.text("text-1a"),
                TextNode.styleName(TextStyleName.with("style123"))
                        .setChildren(Lists.of(TextNode.text("text-2b"), TextNode.placeholder(TextPlaceholderName.with("place-1")))))),
                "[\"text-1a\", style123[\"text-2b\", place-1]]");
    }

    @Test
    public void testSelectorUsage() {
        final TextNode node = TextNode.style(Lists.of(
                TextNode.styleName(TextStyleName.with("style123"))
                        .setChildren(Lists.of(TextNode.text("text-1a"), TextNode.text("text-2b"), TextNode.placeholder(TextPlaceholderName.with("place-1"))))));
        assertEquals(2, TextNode.absoluteNodeSelector()
                .descendant()
                .named(Text.NAME)
                .stream(node, NodeSelectorContexts.basicFunctions(), Converters.fake(), DecimalNumberContexts.fake(), TextNode.class)
                .count());
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TextNode> type() {
        return TextNode.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HasJsonNode.....................................................................................................

    @Override
    public TextNode fromJsonNode(final JsonNode from) {
        return TextNode.fromJsonNode(from);
    }

    @Override
    public TextNode createHasJsonNode() {
        return TextNode.styleName(TextStyleName.with("style123"))
                .setChildren(Lists.of(TextNode.style(Lists.of(TextNode.text("text1"), TextNode.placeholder(TextPlaceholderName.with("place2"))))));
    }

    // TypeNameTesting...................................................................................................

    @Override
    public String typeNamePrefix() {
        return "TextNode";
    }

    @Override
    public String typeNameSuffix() {
        return "";
    }
}
