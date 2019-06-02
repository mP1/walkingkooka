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
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.JsonNode;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class EmptyTextPropertiesTest extends TextPropertiesTestCase<EmptyTextProperties> {

    @Test
    public void testEmpty() {
        assertSame(TextProperties.EMPTY, TextProperties.with(Maps.empty()));
    }

    @Test
    public void testValue() {
        assertSame(TextProperties.EMPTY.value(), TextProperties.EMPTY.value());
    }

    // replace...........................................................................................................

    @Test
    public void testReplaceTextPlaceholderNode() {
        this.replaceAndCheck2(this.placeholder("place1"));
    }

    @Test
    public void testReplaceTextPlaceholderNodeWithParent() {
        this.replaceAndCheck2(setStyledParent(this.placeholder("child-place1")));
    }

    @Test
    public void testReplaceTextProperties() {
        this.replaceAndCheck2(TextNode.properties(this.children()));
    }

    @Test
    public void testReplaceTextProperties2() {
        final TextPropertiesNode node = TextNode.properties(children());

        this.replaceAndCheck(TextProperties.EMPTY,
                node.setAttributes(Maps.of(TextPropertyName.FONT_STYLE, FontStyle.ITALIC)),
                node);
    }

    @Test
    public void testReplaceTextPropertiesNodeWithParent() {
        final TextPropertiesNode textPropertiesNode = TextNode.properties(this.children());

        this.replaceAndCheck(TextProperties.EMPTY,
                setStyledParent(textPropertiesNode.setAttributes(Maps.of(TextPropertyName.FONT_STYLE, FontStyle.ITALIC))),
                setStyledParent(textPropertiesNode));
    }

    @Test
    public void testReplaceTextStyled() {
        this.replaceAndCheck2(TextNode.styled(TextStyleName.with("style123")));
    }

    @Test
    public void testReplaceTextStyleWithParent() {
        final TextStyledNode styled = setStyledParent(this.styled("child-style-456"));

        this.replaceAndCheck2(styled);
    }

    @Test
    public void testReplaceText() {
        this.replaceAndCheck2(TextNode.text("abc123"));
    }

    @Test
    public void testReplaceTextWithParent() {
        this.replaceAndCheck2(setStyledParent(TextNode.text("child-abc123")));
    }

    private void replaceAndCheck2(final TextNode textNode) {
        this.replaceAndCheck(TextProperties.EMPTY, textNode);
    }

    // set..............................................................................................................

    @Test
    public void testSet() {
        final TextPropertyName<FontFamilyName> propertyName = TextPropertyName.FONT_FAMILY_NAME;
        final FontFamilyName familyName = FontFamilyName.with("Antiqua");

        this.setAndCheck(TextProperties.EMPTY,
                propertyName,
                familyName,
                TextProperties.with(Maps.of(propertyName, familyName)));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(TextProperties.EMPTY, "{}");
    }

    @Test
    public void testFromEmptyJsonObject() {
        assertSame(TextProperties.EMPTY, TextProperties.fromJsonNode(JsonNode.object()));
    }

    @Override
    public EmptyTextProperties createObject() {
        return Cast.to(TextProperties.EMPTY);
    }

    @Override
    Class<EmptyTextProperties> textPropertiesType() {
        return EmptyTextProperties.class;
    }
}
