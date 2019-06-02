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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class NonEmptyTextPropertiesTest extends TextPropertiesTestCase<NonEmptyTextProperties> {

    @Test
    public void testWithTextPropertiesMap() {
        final Map<TextPropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());
        final TextPropertiesMap textPropertiesMap = TextPropertiesMap.with(map);

        final NonEmptyTextProperties textProperties = this.createTextProperties(textPropertiesMap);
        assertSame(textPropertiesMap, textProperties.value(), "value");
    }

    @Test
    public void testWithMapCopied() {
        final Map<TextPropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        final Map<TextPropertyName<?>, Object> copy = Maps.sorted();
        copy.putAll(map);

        final NonEmptyTextProperties textProperties = this.createTextProperties(map);

        map.clear();
        assertEquals(copy, textProperties.value(), "value");
    }

    @Test
    public void testEmpty() {
        assertSame(TextPropertiesMap.EMPTY, TextPropertiesMap.with(Maps.empty()));
    }

    @Test
    public void testValue() {
        final Map<TextPropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        final NonEmptyTextProperties textProperties = this.createTextProperties(map);
        assertEquals(TextPropertiesMap.class, textProperties.value().getClass(), () -> "" + textProperties.value);
    }

    // replace...........................................................................................................

    @Test
    public void testReplaceTextPropertiesWithoutAttributes() {
        final List<TextNode> children = this.children();
        final TextProperties textProperties = this.createTextProperties();

        this.replaceAndCheck(textProperties,
                TextNode.properties(children),
                TextPropertiesNode.with(children, TextPropertiesMap.with(textProperties.value())));
    }

    @Test
    public void testReplaceTextPropertiesWithAttributes() {
        final List<TextNode> children = this.children();
        final TextPropertiesNode textProperties = TextPropertiesNode.with(children, this.createTextProperties().textPropertiesMap());

        this.replaceAndCheck(TextProperties.EMPTY,
                textProperties,
                TextNode.properties(children));
    }

    @Test
    public void testReplaceTextPropertiesWithParent() {
        final TextPropertiesNode textPropertiesNode = TextNode.properties(this.children());

        final TextProperties textProperties = this.createTextProperties(this.property2(), this.value2());

        this.replaceAndCheck(textProperties,
                this.setStyledParent(textPropertiesNode),
                this.setStyledParent(textPropertiesNode.setAttributes(textProperties.value())));
    }

    @Test
    public void testReplaceTextPropertiesWithParent2() {
        final TextPropertiesNode textPropertiesNode = TextNode.properties(this.children());

        final TextProperties textProperties = this.createTextProperties(this.property2(), this.value2());

        this.replaceAndCheck(textProperties,
                this.setStyledParent(textPropertiesNode.setAttributes(Maps.of(this.property1(), this.value1()))),
                this.setStyledParent(textPropertiesNode.setAttributes(textProperties.value())));
    }

    @Test
    public void testReplaceTextStyled() {
        this.replaceAndCheck2(this.styled("style123"));
    }

    @Test
    public void testReplaceTextStyledWithParent() {
        final TextStyledNode styledNode = this.styled("child-style123");
        final TextProperties  textProperties = this.createTextProperties();

        this.replaceAndCheck(textProperties,
                this.setStyledParent(styledNode),
                this.setStyledParent(TextNode.properties(Lists.of(styledNode)).setAttributes(textProperties.value())).children().get(0));
    }

    @Test
    public void testReplaceText() {
        this.replaceAndCheck2(TextNode.text("abc123"));
    }

    @Test
    public void testReplaceTextWithParent() {
        this.replaceAndCheck3(TextNode.text("child-abc123"));
    }

    @Test
    public void testReplaceTextPlaceholder() {
        this.replaceAndCheck2(this.placeholder("placeholder123"));
    }

    @Test
    public void testReplaceTextPlaceholderWithParent() {
        this.replaceAndCheck3(this.placeholder("child-placeholder123"));
    }

    private void replaceAndCheck3(final TextNode textNode) {
        final TextProperties textProperties = this.createTextProperties();

        this.replaceAndCheck(textProperties,
                this.setStyledParent(textNode),
                this.setStyledParent(TextNode.properties(Lists.of(textNode)).setAttributes(textProperties.value())).children().get(0));
    }

    private void replaceAndCheck2(final TextNode textNode) {
        final TextProperties textProperties = this.createTextProperties();

        this.replaceAndCheck(textProperties,
                textNode,
                TextNode.properties(Lists.of(textNode)).setAttributes(textProperties.value()).children().get(0));
    }

    // get..............................................................................................................

    @Test
    public void testGet() {
        this.getAndCheck(this.createTextProperties(),
                this.property1(),
                this.value1());
    }

    @Test
    public void testGet2() {
        this.getAndCheck(this.createTextProperties(),
                this.property2(),
                this.value2());
    }

    // set..............................................................................................................

    @Test
    public void testSetExistingPropertyAndValue() {
        this.setAndCheck(this.createTextProperties(),
                this.property1(),
                this.value1());
    }

    @Test
    public void testSetExistingPropertyAndValue2() {
        this.setAndCheck(this.createTextProperties(),
                this.property2(),
                this.value2());
    }

    @Test
    public void testSetReplacePropertyAndValue() {
        final TextPropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();

        final TextPropertyName<FontFamilyName> property2 = this.property2();
        final FontFamilyName value2 = this.value2();

        final WordWrap different = WordWrap.NORMAL;
        assertNotSame(different, value1);

        this.setAndCheck(this.createTextProperties(property1, value1, property2, value2),
                property1,
                different,
                this.createTextProperties(property1, different, property2, value2));
    }

    @Test
    public void testSetReplacePropertyAndValue2() {
        final TextPropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();

        final TextPropertyName<FontFamilyName> property2 = this.property2();
        final FontFamilyName value2 = this.value2();

        final FontFamilyName different = FontFamilyName.with("different");
        assertNotSame(different, value2);

        this.setAndCheck(this.createTextProperties(property1, value1, property2, value2),
                property2,
                different,
                this.createTextProperties(property1, value1, property2, different));
    }

    @Test
    public void testSetNewPropertyAndValue() {
        final TextPropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();

        final TextPropertyName<FontFamilyName> property2 = this.property2();
        final FontFamilyName value2 = this.value2();

        final TextPropertyName<WritingMode> property3 = TextPropertyName.WRITING_MODE;
        final WritingMode value3 = WritingMode.VERTICAL_LR;

        this.setAndCheck(this.createTextProperties(property1, value1, property2, value2),
                property3,
                value3,
                this.createTextProperties(property1, value1, property2, value2, property3, value3));
    }

    @Test
    public void testSetNewPropertyAndValue2() {
        final TextPropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();

        final TextPropertyName<FontFamilyName> property2 = this.property2();
        final FontFamilyName value2 = this.value2();

        final TextPropertyName<Color> property3 = TextPropertyName.BACKGROUND_COLOR;
        final Color value3 = Color.fromRgb(0x123456);

        this.setAndCheck(this.createTextProperties(property1, value1, property2, value2),
                property3,
                value3,
                this.createTextProperties(property3, value3, property1, value1, property2, value2));
    }

    private <T> void setAndCheck(final TextProperties properties,
                                 final TextPropertyName<T> propertyName,
                                 final T value) {
        assertSame(properties,
                properties.set(propertyName, value),
                () -> properties + " set " + propertyName + " and " + CharSequences.quoteIfChars(value));
    }

    // remove...........................................................................................................

    @Test
    public void testRemove() {
        final TextPropertyName<WordWrap> property1 = this.property1();

        final TextPropertyName<FontFamilyName> property2 = this.property2();
        final FontFamilyName value2 = this.value2();

        this.removeAndCheck(this.createTextProperties(property1, this.value1(), property2, value2),
                property1,
                this.createTextProperties(property2, value2));
    }

    @Test
    public void testRemove2() {
        final TextPropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();

        final TextPropertyName<FontFamilyName> property2 = this.property2();

        this.removeAndCheck(this.createTextProperties(property1, value1, property2, this.value2()),
                property2,
                this.createTextProperties(property1, value1));
    }

    @Test
    public void testRemoveBecomesEmpty() {
        final TextPropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();

        this.removeAndCheck(this.createTextProperties(property1, value1),
                property1,
                TextProperties.EMPTY);
    }

    // set & remove ...................................................................................................

    @Test
    public void testSetSetRemoveRemove() {
        //set
        final TextPropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();
        final TextProperties textProperties1 = this.setAndCheck(TextProperties.EMPTY,
                property1,
                value1,
                this.createTextProperties(property1, value1));

        //set
        final TextPropertyName<FontFamilyName> property2 = this.property2();
        final FontFamilyName value2 = this.value2();
        final TextProperties textProperties2 = this.setAndCheck(textProperties1,
                property2,
                value2,
                this.createTextProperties(property1, value1, property2, value2));

        // remove1
        final TextProperties textProperties3 = this.removeAndCheck(textProperties2,
                property1,
                this.createTextProperties(property2, value2));

        this.removeAndCheck(textProperties3,
                property2,
                TextProperties.EMPTY);
    }

    @Test
    public void testSetSetRemoveSet() {
        //set
        final TextPropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();
        final TextProperties textProperties1 = this.setAndCheck(TextProperties.EMPTY,
                property1,
                value1,
                this.createTextProperties(property1, value1));

        //set
        final TextPropertyName<FontFamilyName> property2 = this.property2();
        final FontFamilyName value2 = this.value2();
        final TextProperties textProperties2 = this.setAndCheck(textProperties1,
                property2,
                value2,
                this.createTextProperties(property1, value1, property2, value2));

        // remove1
        final TextProperties textProperties3 = this.removeAndCheck(textProperties2,
                property1,
                this.createTextProperties(property2, value2));


        //set property1 again
        this.setAndCheck(textProperties3,
                property1,
                value1,
                this.createTextProperties(property1, value1, property2, value2));
    }

    // ToString.........................................................................................................

    @Test
    public void testToString() {
        final Map<TextPropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        this.toStringAndCheck(NonEmptyTextProperties.with(map), map.toString());
    }

    @Test
    public void testFromEmptyJsonObject() {
        assertSame(TextProperties.EMPTY, TextProperties.fromJsonNode(JsonNode.object()));
    }

    @Override
    public NonEmptyTextProperties createObject() {
        return this.createTextProperties();
    }

    private NonEmptyTextProperties createTextProperties() {
        return this.createTextProperties(this.property1(), this.value1(), this.property2(), this.value2());
    }

    private <X> NonEmptyTextProperties createTextProperties(final TextPropertyName<X> property1,
                                                            final X value1) {
        return this.createTextProperties(Maps.of(property1, value1));
    }

    private <X, Y> NonEmptyTextProperties createTextProperties(final TextPropertyName<X> property1,
                                                               final X value1,
                                                               final TextPropertyName<Y> property2,
                                                               final Y value2) {
        final Map<TextPropertyName<?>, Object> map = Maps.sorted();
        map.put(property1, value1);
        map.put(property2, value2);
        return this.createTextProperties(map);
    }

    private <X, Y, Z> NonEmptyTextProperties createTextProperties(final TextPropertyName<X> property1,
                                                                  final X value1,
                                                                  final TextPropertyName<Y> property2,
                                                                  final Y value2,
                                                                  final TextPropertyName<Z> property3,
                                                                  final Z value3) {
        final Map<TextPropertyName<?>, Object> map = Maps.sorted();
        map.put(property1, value1);
        map.put(property2, value2);
        map.put(property3, value3);
        return this.createTextProperties(map);
    }

    private NonEmptyTextProperties createTextProperties(final Map<TextPropertyName<?>, Object> map) {
        return Cast.to(TextProperties.with(map));
    }

    private TextPropertyName<WordWrap> property1() {
        return TextPropertyName.WORD_WRAP;
    }

    private WordWrap value1() {
        return WordWrap.BREAK_WORD;
    }

    private TextPropertyName<FontFamilyName> property2() {
        return TextPropertyName.FONT_FAMILY_NAME;
    }

    private FontFamilyName value2() {
        return FontFamilyName.with("Times News Roman");
    }

    @Override
    Class<NonEmptyTextProperties> textPropertiesType() {
        return NonEmptyTextProperties.class;
    }

}
