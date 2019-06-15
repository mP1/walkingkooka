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

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.color.ColorHslOrHsv;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The name of an expression node.
 */
public final class TextStylePropertyName<T> extends TextNodeNameName<TextStylePropertyName<?>> {

    /**
     * First because required by {@link #CONSTANTS} init.
     */
    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;

    // constants

    /**
     * A read only cache of already textStyle.
     */
    private final static Map<String, TextStylePropertyName<?>> CONSTANTS = Maps.sorted(TextStylePropertyName.CASE_SENSITIVITY.comparator());

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link Enum} values.
     */
    private static <E extends Enum<E>> TextStylePropertyName<E> registerEnumConstant(final String property,
                                                                                     final Function<String, E> factory,
                                                                                     final Class<E> type,
                                                                                     final BiConsumer<E, TextStyleVisitor> visitor) {
        return registerConstant(property,
                TextStylePropertyValueHandler.enumTextPropertyValueHandler(factory, type),
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link Enum} values.
     */
    private static <H extends HasJsonNode> TextStylePropertyName<H> registerHasJsonNodeConstant(final String property,
                                                                                                final Class<H> type,
                                                                                                final Function<JsonNode, H> fromJsonNode,
                                                                                                final BiConsumer<H, TextStyleVisitor> visitor) {
        return registerConstant(property,
                TextStylePropertyValueHandler.hasJsonNode(type, fromJsonNode),
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link NoneLength} or {@link PixelLength} values.
     */
    private static TextStylePropertyName<Length<?>> registerNoneLengthPixelLengthConstant(final String property,
                                                                                          final BiConsumer<Length<?>, TextStyleVisitor> visitor) {
        return registerConstant(property,
                TextStylePropertyValueHandler.noneLengthPixelLength(),
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link NormalLength} or {@link PixelLength} values.
     */
    private static TextStylePropertyName<Length<?>> registerNormalLengthPixelLengthConstant(final String property,
                                                                                            final BiConsumer<Length<?>, TextStyleVisitor> visitor) {
        return registerConstant(property,
                TextStylePropertyValueHandler.normalLengthPixelLength(),
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link PixelLength} values.
     */
    private static TextStylePropertyName<Length<?>> registerPixelLengthConstant(final String property,
                                                                                final BiConsumer<Length<?>, TextStyleVisitor> visitor) {
        final Class<Length<?>> length = Cast.to(Length.class);
        return registerHasJsonNodeConstant(property,
                length,
                PixelLength::fromJsonNodePixel,
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link NumberLength} values.
     */
    private static TextStylePropertyName<Length<?>> registerNumberLengthConstant(final String property,
                                                                                 final BiConsumer<Length<?>, TextStyleVisitor> visitor) {
        final Class<Length<?>> length = Cast.to(Length.class);
        return registerHasJsonNodeConstant(property,
                length,
                NumberLength::fromJsonNode,
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link String} values.
     */
    private static TextStylePropertyName<String> registerStringConstant(final String property,
                                                                        final BiConsumer<String, TextStyleVisitor> visitor) {
        return registerConstant(property,
                TextStylePropertyValueHandler.string(),
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} to the cache being built.
     */
    private static <T> TextStylePropertyName<T> registerConstant(final String property,
                                                                 final TextStylePropertyValueHandler<T> handler,
                                                                 final BiConsumer<T, TextStyleVisitor> visitor) {
        final TextStylePropertyName<T> textStylePropertyName = new TextStylePropertyName<>(property, handler, visitor);
        TextStylePropertyName.CONSTANTS.put(property, textStylePropertyName);
        return textStylePropertyName;
    }

    /**
     * Background color
     */
    public final static TextStylePropertyName<Color> BACKGROUND_COLOR = registerHasJsonNodeConstant("background-color",
            Color.class,
            Color::fromJsonNodeColor,
            (c, v) -> v.visitBackgroundColor(c));

    /**
     * border-bottom-color
     */
    public final static TextStylePropertyName<ColorHslOrHsv> BORDER_BOTTOM_COLOR = registerHasJsonNodeConstant("border-bottom-color",
            ColorHslOrHsv.class,
            ColorHslOrHsv::fromJsonNode,
            (c, v) -> v.visitBorderBottomColor(c));

    /**
     * border-bottom-textStyle
     */
    public final static TextStylePropertyName<BorderStyle> BORDER_BOTTOM_STYLE = registerEnumConstant("border-bottom-textStyle",
            BorderStyle::valueOf,
            BorderStyle.class,
            (s, v) -> v.visitBorderBottomStyle(s));

    /**
     * border-bottom-width
     */
    public final static TextStylePropertyName<Length<?>> BORDER_BOTTOM_WIDTH = registerPixelLengthConstant("border-bottom-width",
            (l, v) -> v.visitBorderBottomWidth(l));

    /**
     * border-collapse
     */
    public final static TextStylePropertyName<BorderCollapse> BORDER_COLLAPSE = registerEnumConstant("border-collapse",
            BorderCollapse::valueOf,
            BorderCollapse.class,
            (b, v) -> v.visitBorderCollapse(b));

    /**
     * border-left-color
     */
    public final static TextStylePropertyName<ColorHslOrHsv> BORDER_LEFT_COLOR = registerHasJsonNodeConstant("border-left-color",
            ColorHslOrHsv.class,
            ColorHslOrHsv::fromJsonNode,
            (c, v) -> v.visitBorderLeftColor(c));

    /**
     * border-left-textStyle
     */
    public final static TextStylePropertyName<BorderStyle> BORDER_LEFT_STYLE = registerEnumConstant("border-left-textStyle",
            BorderStyle::valueOf,
            BorderStyle.class,
            (s, v) -> v.visitBorderLeftStyle(s));

    /**
     * border-left-width
     */
    public final static TextStylePropertyName<Length<?>> BORDER_LEFT_WIDTH = registerPixelLengthConstant("border-left-width",
            (l, v) -> v.visitBorderLeftWidth(l));


    /**
     * border-space
     */
    public final static TextStylePropertyName<BorderSpacing> BORDER_SPACING = registerHasJsonNodeConstant("border-spacing",
            BorderSpacing.class,
            BorderSpacing::fromJsonNode,
            (l, v) -> v.visitBorderSpacing(l));

    /**
     * border-right-color
     */
    public final static TextStylePropertyName<ColorHslOrHsv> BORDER_RIGHT_COLOR = registerHasJsonNodeConstant("border-right-color",
            ColorHslOrHsv.class,
            ColorHslOrHsv::fromJsonNode,
            (c, v) -> v.visitBorderRightColor(c));

    /**
     * border-right-textStyle
     */
    public final static TextStylePropertyName<BorderStyle> BORDER_RIGHT_STYLE = registerEnumConstant("border-right-textStyle",
            BorderStyle::valueOf,
            BorderStyle.class,
            (s, v) -> v.visitBorderRightStyle(s));

    /**
     * border-right-width
     */
    public final static TextStylePropertyName<Length<?>> BORDER_RIGHT_WIDTH = registerPixelLengthConstant("border-right-width",
            (l, v) -> v.visitBorderRightWidth(l));

    /**
     * border-top-color
     */
    public final static TextStylePropertyName<ColorHslOrHsv> BORDER_TOP_COLOR = registerHasJsonNodeConstant("border-top-color",
            ColorHslOrHsv.class,
            ColorHslOrHsv::fromJsonNode,
            (c, v) -> v.visitBorderTopColor(c));

    /**
     * border-top-textStyle
     */
    public final static TextStylePropertyName<BorderStyle> BORDER_TOP_STYLE = registerEnumConstant("border-top-textStyle",
            BorderStyle::valueOf,
            BorderStyle.class,
            (s, v) -> v.visitBorderTopStyle(s));

    /**
     * border-top-width
     */
    public final static TextStylePropertyName<Length<?>> BORDER_TOP_WIDTH = registerPixelLengthConstant("border-top-width",
            (l, v) -> v.visitBorderTopWidth(l));

    /**
     * font-family-name
     */
    public final static TextStylePropertyName<FontFamilyName> FONT_FAMILY_NAME = registerHasJsonNodeConstant("font-family-name",
            FontFamilyName.class,
            FontFamilyName::fromJsonNode,
            (f, v) -> v.visitFontFamilyName(f));

    /**
     * font-kerning
     */
    public final static TextStylePropertyName<FontKerning> FONT_KERNING = registerEnumConstant("font-kerning",
            FontKerning::valueOf,
            FontKerning.class,
            (f, v) -> v.visitFontKerning(f));

    /**
     * font-size
     */
    public final static TextStylePropertyName<FontSize> FONT_SIZE = registerHasJsonNodeConstant("font-size",
            FontSize.class,
            FontSize::fromJsonNode,
            (f, v) -> v.visitFontSize(f));

    /**
     * font-stretch
     */
    public final static TextStylePropertyName<FontStretch> FONT_STRETCH = registerEnumConstant("font-stretch",
            FontStretch::valueOf,
            FontStretch.class,
            (f, v) -> v.visitFontStretch(f));

    /**
     * font-textStyle
     */
    public final static TextStylePropertyName<FontStyle> FONT_STYLE = registerEnumConstant("font-textStyle",
            FontStyle::valueOf,
            FontStyle.class,
            (f, v) -> v.visitFontStyle(f));

    /**
     * font-variant
     */
    public final static TextStylePropertyName<FontVariant> FONT_VARIANT = registerEnumConstant("font-variant",
            FontVariant::valueOf,
            FontVariant.class,
            (f, v) -> v.visitFontVariant(f));

    /**
     * font-weight
     */
    public final static TextStylePropertyName<FontWeight> FONT_WEIGHT = registerHasJsonNodeConstant("font-weight",
            FontWeight.class,
            FontWeight::fromJsonNode,
            (f, v) -> v.visitFontWeight(f));

    /**
     * hanging-punctuation
     */
    public final static TextStylePropertyName<HangingPunctuation> HANGING_PUNCTUATION = registerEnumConstant("hanging-punctuation",
            HangingPunctuation::valueOf,
            HangingPunctuation.class,
            (h, v) -> v.visitHangingPunctuation(h));

    /**
     * horizontal-alignment
     */
    public final static TextStylePropertyName<HorizontalAlignment> HORIZONTAL_ALIGNMENT = registerEnumConstant("horizontal-alignment",
            HorizontalAlignment::valueOf,
            HorizontalAlignment.class,
            (h, v) -> v.visitHorizontalAlignment(h));

    /**
     * height
     */
    public final static TextStylePropertyName<Length<?>> HEIGHT = registerPixelLengthConstant("height",
            (l, v) -> v.visitHeight(l));

    /**
     * hyphens
     */
    public final static TextStylePropertyName<Hyphens> HYPHENS = registerEnumConstant("hyphens",
            Hyphens::valueOf,
            Hyphens.class,
            (h, v) -> v.visitHyphens(h));

    /**
     * letter-space
     */
    public final static TextStylePropertyName<LetterSpacing> LETTER_SPACING = registerHasJsonNodeConstant("letter-spacing",
            LetterSpacing.class,
            LetterSpacing::fromJsonNode,
            (l, v) -> v.visitLetterSpacing(l));

    /**
     * line-height
     */
    public final static TextStylePropertyName<Length<?>> LINE_HEIGHT = registerNormalLengthPixelLengthConstant("line-height",
            (l, v) -> v.visitLineHeight(l));

    /**
     * list-textStyle-position
     */
    public final static TextStylePropertyName<ListStylePosition> LIST_STYLE_POSITION = registerEnumConstant("list-textStyle-position",
            ListStylePosition::valueOf,
            ListStylePosition.class,
            (p, v) -> v.visitListStylePosition(p));

    /**
     * list-textStyle-type
     */
    public final static TextStylePropertyName<ListStyleType> LIST_STYLE_TYPE = registerEnumConstant("list-textStyle-type",
            ListStyleType::valueOf,
            ListStyleType.class,
            (t, v) -> v.visitListStyleType(t));

    /**
     * margin-bottom
     */
    public final static TextStylePropertyName<Length<?>> MARGIN_BOTTOM = registerPixelLengthConstant("margin-bottom",
            (l, v) -> v.visitMarginBottom(l));

    /**
     * margin-left
     */
    public final static TextStylePropertyName<Length<?>> MARGIN_LEFT = registerPixelLengthConstant("margin-left",
            (l, v) -> v.visitMarginLeft(l));

    /**
     * margin-right
     */
    public final static TextStylePropertyName<Length<?>> MARGIN_RIGHT = registerPixelLengthConstant("margin-right",
            (l, v) -> v.visitMarginRight(l));

    /**
     * margin-top
     */
    public final static TextStylePropertyName<Length<?>> MARGIN_TOP = registerPixelLengthConstant("margin-top",
            (l, v) -> v.visitMarginTop(l));

    /**
     * max-height
     */
    public final static TextStylePropertyName<Length<?>> MAX_HEIGHT = registerNoneLengthPixelLengthConstant("max-height",
            (m, v) -> v.visitMaxHeight(m));

    /**
     * max-width
     */
    public final static TextStylePropertyName<Length<?>> MAX_WIDTH = registerNoneLengthPixelLengthConstant("max-width",
            (m, v) -> v.visitMaxWidth(m));

    /**
     * min-height
     */
    public final static TextStylePropertyName<Length<?>> MIN_HEIGHT = registerPixelLengthConstant("min-height",
            (m, v) -> v.visitMinHeight(m));

    /**
     * min-width
     */
    public final static TextStylePropertyName<Length<?>> MIN_WIDTH = registerPixelLengthConstant("min-width",
            (m, v) -> v.visitMinWidth(m));

    /**
     * opacity
     */
    public final static TextStylePropertyName<Opacity> OPACITY = registerHasJsonNodeConstant("opacity",
            Opacity.class,
            Opacity::fromJsonNode,
            (o, v) -> v.visitOpacity(o));

    /**
     * outline-color
     */
    public final static TextStylePropertyName<Color> OUTLINE_COLOR = registerHasJsonNodeConstant("outline-color",
            Color.class,
            Color::fromJsonNodeColor,
            (t, v) -> v.visitOutlineColor(t));

    /**
     * outline-offset
     */
    public final static TextStylePropertyName<Length<?>> OUTLINE_OFFSET = registerPixelLengthConstant("outline-offset",
            (l, v) -> v.visitOutlineOffset(l));

    /**
     * outline-textStyle
     */
    public final static TextStylePropertyName<OutlineStyle> OUTLINE_STYLE = registerEnumConstant("outline-textStyle",
            OutlineStyle::valueOf,
            OutlineStyle.class,
            (s, v) -> v.visitOutlineStyle(s));

    /**
     * outline-width
     */
    public final static TextStylePropertyName<Length<?>> OUTLINE_WIDTH = registerPixelLengthConstant("outline-width",
            (l, v) -> v.visitOutlineWidth(l));

    /**
     * overflow-x
     */
    public final static TextStylePropertyName<Overflow> OVERFLOW_X = registerEnumConstant("overflow-x",
            Overflow::valueOf,
            Overflow.class,
            (o, v) -> v.visitOverflowX(o));

    /**
     * overflow-y
     */
    public final static TextStylePropertyName<Overflow> OVERFLOW_Y = registerEnumConstant("overflow-y",
            Overflow::valueOf,
            Overflow.class,
            (o, v) -> v.visitOverflowY(o));

    /**
     * padding-bottom
     */
    public final static TextStylePropertyName<Length<?>> PADDING_BOTTOM = registerPixelLengthConstant("padding-bottom",
            (l, v) -> v.visitPaddingBottom(l));

    /**
     * padding-left
     */
    public final static TextStylePropertyName<Length<?>> PADDING_LEFT = registerPixelLengthConstant("padding-left",
            (l, v) -> v.visitPaddingLeft(l));

    /**
     * padding-right
     */
    public final static TextStylePropertyName<Length<?>> PADDING_RIGHT = registerPixelLengthConstant("padding-right",
            (l, v) -> v.visitPaddingRight(l));

    /**
     * padding-top
     */
    public final static TextStylePropertyName<Length<?>> PADDING_TOP = registerPixelLengthConstant("padding-top",
            (l, v) -> v.visitPaddingTop(l));

    /**
     * tab-size
     */
    public final static TextStylePropertyName<Length<?>> TAB_SIZE = registerNumberLengthConstant("tab-size",
            (l, v) -> v.visitTabSize(l));

    /**
     * text
     */
    public final static TextStylePropertyName<String> TEXT = registerStringConstant("text",
            (t, v) -> v.visitText(t));

    /**
     * text-alignment
     */
    public final static TextStylePropertyName<TextAlignment> TEXT_ALIGNMENT = registerEnumConstant("text-align",
            TextAlignment::valueOf,
            TextAlignment.class,
            (t, v) -> v.visitTextAlignment(t));

    /**
     * Text color
     */
    public final static TextStylePropertyName<Color> TEXT_COLOR = registerHasJsonNodeConstant("text-color",
            Color.class,
            Color::fromJsonNodeColor,
            (t, v) -> v.visitTextColor(t));

    /**
     * text-decoration: UNDERLINE, OVERLINE, LINE_THROUGH
     */
    public final static TextStylePropertyName<TextDecoration> TEXT_DECORATION = registerEnumConstant("text-decoration",
            TextDecoration::valueOf,
            TextDecoration.class,
            (t, v) -> v.visitTextDecoration(t));

    /**
     * text-decoration-color
     */
    public final static TextStylePropertyName<Color> TEXT_DECORATION_COLOR = registerHasJsonNodeConstant("text-decoration-color",
            Color.class,
            Color::fromJsonNodeColor,
            (t, v) -> v.visitTextDecorationColor(t));

    /**
     * text-decoration-textStyle
     */
    public final static TextStylePropertyName<TextDecorationStyle> TEXT_DECORATION_STYLE = registerEnumConstant("text-decoration-textStyle",
            TextDecorationStyle::valueOf,
            TextDecorationStyle.class,
            (t, v) -> v.visitTextDecorationStyle(t));

    /**
     * text-direction
     */
    public final static TextStylePropertyName<TextDirection> TEXT_DIRECTION = registerEnumConstant("text-direction",
            TextDirection::valueOf,
            TextDirection.class,
            (d, v) -> v.visitTextDirection(d));

    /**
     * text-indent
     */
    public final static TextStylePropertyName<Length<?>> TEXT_INDENT = registerPixelLengthConstant("text-indent",
            (l, v) -> v.visitTextIndent(l));

    /**
     * text-justify
     */
    public final static TextStylePropertyName<TextJustify> TEXT_JUSTIFY = registerEnumConstant("text-justify",
            TextJustify::valueOf,
            TextJustify.class,
            (t, v) -> v.visitTextJustify(t));

    /**
     * text-overflow
     */
    public final static TextStylePropertyName<TextOverflow> TEXT_OVERFLOW = registerHasJsonNodeConstant("text-overflow",
            TextOverflow.class,
            TextOverflow::fromJsonNode,
            (t, v) -> v.visitTextOverflow(t));

    /**
     * text-transform
     */
    public final static TextStylePropertyName<TextTransform> TEXT_TRANSFORM = registerEnumConstant("text-transform",
            TextTransform::valueOf,
            TextTransform.class,
            (t, v) -> v.visitTextTransform(t));

    /**
     * text-wrapping
     */
    public final static TextStylePropertyName<TextWrapping> TEXT_WRAPPING = registerEnumConstant("text-wrapping",
            TextWrapping::valueOf,
            TextWrapping.class,
            (t, v) -> v.visitTextWrapping(t));

    /**
     * vertical-alignment
     */
    public final static TextStylePropertyName<VerticalAlignment> VERTICAL_ALIGNMENT = registerEnumConstant("vertical-alignment",
            VerticalAlignment::valueOf,
            VerticalAlignment.class,
            (va, v) -> v.visitVerticalAlignment(va));

    /**
     * visibility
     */
    public final static TextStylePropertyName<Visibility> VISIBILITY = registerEnumConstant("visibility",
            Visibility::valueOf,
            Visibility.class,
            (o, v) -> v.visitVisibility(o));

    /**
     * white-space
     */
    public final static TextStylePropertyName<TextWhitespace> WHITE_SPACE = registerEnumConstant("white-space",
            TextWhitespace::valueOf,
            TextWhitespace.class,
            (w, v) -> v.visitWhitespace(w));

    /**
     * width
     */
    public final static TextStylePropertyName<Length<?>> WIDTH = registerPixelLengthConstant("width",
            (w, v) -> v.visitWidth(w));

    /**
     * word-break
     */
    public final static TextStylePropertyName<WordBreak> WORD_BREAK = registerEnumConstant("word-break",
            WordBreak::valueOf,
            WordBreak.class,
            (w, v) -> v.visitWordBreak(w));

    /**
     * word-spacing
     */
    public final static TextStylePropertyName<WordSpacing> WORD_SPACING = registerHasJsonNodeConstant("word-spacing",
            WordSpacing.class,
            WordSpacing::fromJsonNode,
            (w, v) -> v.visitWordSpacing(w));

    /**
     * word-wrap
     */
    public final static TextStylePropertyName<WordWrap> WORD_WRAP = registerEnumConstant("word-wrap",
            WordWrap::valueOf,
            WordWrap.class,
            (w, v) -> v.visitWordWrap(w));

    /**
     * writing-mode
     */
    public final static TextStylePropertyName<WritingMode> WRITING_MODE = registerEnumConstant("writing-mode",
            WritingMode::valueOf,
            WritingMode.class,
            (w, v) -> v.visitWritingMode(w));

    /**
     * Factory that retrieves an existing property or if unknown a property that assumes non empty string value.
     */
    public static TextStylePropertyName<?> with(final String name) {
        Objects.requireNonNull(name, "name");

        final TextStylePropertyName<?> textStylePropertyName = CONSTANTS.get(name);
        return null != textStylePropertyName ?
                textStylePropertyName :
                new TextStylePropertyName<>(checkName(name),
                        TextStylePropertyValueHandler.hasJsonNodeWithType(),
                        TextStylePropertyName::acceptUnknown);
    }

    private static void acceptUnknown(final Object value, final TextStyleVisitor visitor) {
        visitor.visitUnknown(value);
    }

    private TextStylePropertyName(final String name,
                                  final TextStylePropertyValueHandler<T> handler,
                                  final BiConsumer<T, TextStyleVisitor> visitor) {
        super(name);
        this.handler= handler;
        this.visitor = visitor;

        this.jsonNodeName = JsonNodeName.with(this.name);
    }

    /**
     * Verifies that a value is legal for this {@link TextStylePropertyName}
     */
    public void check(final Object value) {
        this.handler.check(value, this);
    }

    /**
     * Used to handle/validate property values.
     */
    final TextStylePropertyValueHandler<T> handler;

    /**
     * Returns the name in quotes, useful for error messages.
     */
    CharSequence inQuotes() {
        return CharSequences.quoteAndEscape(this.name);
    }

    // HasJsonNode.....................................................................................................

    static TextStylePropertyName<?> fromJsonNodeName(final JsonNode node) {
        return TextStylePropertyName.with(node.name().value());
    }

    JsonNodeName toJsonNodeName() {
        return this.jsonNodeName;
    }

    private final JsonNodeName jsonNodeName;

    /**
     * Dispatches to the appropriate {@link TextStyleVisitor} visit method.
     */
    void accept(final Object value, final TextStyleVisitor visitor) {
        this.visitor.accept(Cast.to(value), visitor);
    }

    /**
     * Calls the appropriate {@link TextStyleVisitor} visit method
     */
    private final BiConsumer<T, TextStyleVisitor> visitor;

    // Object..........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof TextStylePropertyName;
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }
}
