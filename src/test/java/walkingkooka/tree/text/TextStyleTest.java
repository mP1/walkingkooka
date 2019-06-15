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
import walkingkooka.collect.map.Maps;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.JavaVisibility;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStyleTest implements ClassTesting2<TextStyle>,
        HashCodeEqualsDefinedTesting<TextStyle>,
        HasJsonNodeTesting<TextStyle>,
        ToStringTesting<TextStyle> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            TextStyle.with(null);
        });
    }

    @Test
    public void testWithInvalidPropertyFails() {
        assertThrows(TextStylePropertyValueException.class, () -> {
            TextStyle.with(Maps.of(TextStylePropertyName.WORD_BREAK, null));
        });
    }

    @Test
    public void testWithTextStyleMap() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());
        final TextStyleMap textStyleMap = TextStyleMap.with(map);

        final TextStyle textStyle = TextStyle.with(textStyleMap);
        assertSame(textStyleMap, textStyle.value(), "value");
    }

    @Test
    public void testWithMapCopied() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        final Map<TextStylePropertyName<?>, Object> copy = Maps.sorted();
        copy.putAll(map);

        final TextStyle textStyle = TextStyle.with(map);

        map.clear();
        assertEquals(copy, textStyle.value(), "value");
    }

    @Test
    public void testEmpty() {
        assertSame(TextStyleMap.EMPTY, TextStyleMap.with(Maps.empty()));
    }

    @Test
    public void testValue() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        final TextStyle textStyle = TextStyle.with(map);
        assertEquals(TextStyleMap.class, textStyle.value().getClass(), () -> "" + textStyle);
    }

    // setChildren......................................................................................................

    @Test
    public void testSetChildrenNullFails() {
        assertThrows(NullPointerException.class, () -> {
            TextStyle.EMPTY.setChildren(null);
        });
    }

    @Test
    public void testSetChildrenEmptyAndNoProperties() {
        final List<TextNode> children = TextStyleNode.NO_CHILDREN;

        this.setChildrenAndCheck(TextStyle.EMPTY,
                children,
                TextStyleNode.with(children, TextStyleMap.EMPTY));
    }

    @Test
    public void testSetChildrenEmpty() {
        final TextStyle textStyle = this.textStyle();
        final List<TextNode> children = TextStyleNode.NO_CHILDREN;

        this.setChildrenAndCheck(textStyle,
                children,
                TextNode.style(children).setAttributes(textStyle.value()));
    }

    @Test
    public void testSetChildrenAndNoProperties() {
        final List<TextNode> children = this.children();

        this.setChildrenAndCheck(TextStyle.EMPTY,
                children,
                TextStyleNode.with(children, TextStyleMap.EMPTY));
    }

    @Test
    public void testSetChildrenAndProperties() {
        final TextStyle textStyle = this.textStyle();
        final List<TextNode> children = this.children();

        this.setChildrenAndCheck(textStyle,
                children,
                TextNode.style(children).setAttributes(textStyle.value()));
    }

    private List<TextNode> children() {
        return Lists.of(TextNode.text("text-1a"), TextNode.text("text-1b"));
    }

    private void setChildrenAndCheck(final TextStyle properties,
                                     final List<TextNode> children,
                                     final TextNode textStyleNode) {
        assertEquals(textStyleNode,
                properties.setChildren(children),
                () -> properties + " setChildren " + children);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        this.toStringAndCheck(TextStyle.with(map), map.toString());
    }

    @Test
    public void testFromEmptyJsonObject() {
        assertSame(TextStyle.EMPTY, TextStyle.fromJsonNode(JsonNode.object()));
    }

    @Override
    public TextStyle createObject() {
        return this.textStyle();
    }

    private TextStyle textStyle() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.ordered();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());
        return TextStyle.with(map);
    }

    private TextStylePropertyName<?> property1() {
        return TextStylePropertyName.WORD_WRAP;
    }

    private Object value1() {
        return WordWrap.BREAK_WORD;
    }

    private TextStylePropertyName<?> property2() {
        return TextStylePropertyName.FONT_FAMILY_NAME;
    }

    private Object value2() {
        return FontFamilyName.with("Times News Roman");
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TextStyle> type() {
        return TextStyle.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HasJsonNodeTesting................................................................................................

    @Override
    public TextStyle fromJsonNode(final JsonNode from) {
        return TextStyle.fromJsonNode(from);
    }

    @Override
    public TextStyle createHasJsonNode() {
        return this.createObject();
    }
}
