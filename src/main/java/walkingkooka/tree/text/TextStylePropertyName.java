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

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
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
     * A read only cache of already style.
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
            Color::fromJsonNode,
            (c, v) -> v.visitBackgroundColor(c));

    /**
     * border-collapse
     */
    public final static TextStylePropertyName<BorderCollapse> BORDER_COLLAPSE = registerEnumConstant("border-collapse",
            BorderCollapse::valueOf,
            BorderCollapse.class,
            (b, v) -> v.visitBorderCollapse(b));

    /**
     * border-space
     */
    public final static TextStylePropertyName<BorderSpacing> BORDER_SPACING = registerHasJsonNodeConstant("border-spacing",
            BorderSpacing.class,
            BorderSpacing::fromJsonNode,
            (l, v) -> v.visitBorderSpacing(l));

    /**
     * direction
     */
    public final static TextStylePropertyName<Direction> DIRECTION = registerEnumConstant("direction",
            Direction::valueOf,
            Direction.class,
            (d, v) -> v.visitDirection(d));

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
     * font-style
     */
    public final static TextStylePropertyName<FontStyle> FONT_STYLE = registerEnumConstant("font-style",
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
    public final static TextStylePropertyName<Height> HEIGHT = registerHasJsonNodeConstant("height",
            Height.class,
            Height::fromJsonNode,
            (h, v) -> v.visitHeight(h));

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
    public final static TextStylePropertyName<LineHeight> LINE_HEIGHT = registerHasJsonNodeConstant("line-height",
            LineHeight.class,
            LineHeight::fromJsonNode,
            (lh, v) -> v.visitLineHeight(lh));

    /**
     * margin-bottom
     */
    public final static TextStylePropertyName<MarginBottom> MARGIN_BOTTOM = registerHasJsonNodeConstant("margin-bottom",
            MarginBottom.class,
            MarginBottom::fromJsonNode,
            (p, v) -> v.visitMarginBottom(p));

    /**
     * margin-left
     */
    public final static TextStylePropertyName<MarginLeft> MARGIN_LEFT = registerHasJsonNodeConstant("margin-left",
            MarginLeft.class,
            MarginLeft::fromJsonNode,
            (p, v) -> v.visitMarginLeft(p));

    /**
     * margin-right
     */
    public final static TextStylePropertyName<MarginRight> MARGIN_RIGHT = registerHasJsonNodeConstant("margin-right",
            MarginRight.class,
            MarginRight::fromJsonNode,
            (p, v) -> v.visitMarginRight(p));

    /**
     * margin-top
     */
    public final static TextStylePropertyName<MarginTop> MARGIN_TOP = registerHasJsonNodeConstant("margin-top",
            MarginTop.class,
            MarginTop::fromJsonNode,
            (p, v) -> v.visitMarginTop(p));
    
    /**
     * opacity
     */
    public final static TextStylePropertyName<Opacity> OPACITY = registerHasJsonNodeConstant("opacity",
            Opacity.class,
            Opacity::fromJsonNode,
            (o, v) -> v.visitOpacity(o));

    /**
     * padding-bottom
     */
    public final static TextStylePropertyName<PaddingBottom> PADDING_BOTTOM = registerHasJsonNodeConstant("padding-bottom",
            PaddingBottom.class,
            PaddingBottom::fromJsonNode,
            (p, v) -> v.visitPaddingBottom(p));

    /**
     * padding-left
     */
    public final static TextStylePropertyName<PaddingLeft> PADDING_LEFT = registerHasJsonNodeConstant("padding-left",
            PaddingLeft.class,
            PaddingLeft::fromJsonNode,
            (p, v) -> v.visitPaddingLeft(p));

    /**
     * padding-right
     */
    public final static TextStylePropertyName<PaddingRight> PADDING_RIGHT = registerHasJsonNodeConstant("padding-right",
            PaddingRight.class,
            PaddingRight::fromJsonNode,
            (p, v) -> v.visitPaddingRight(p));

    /**
     * padding-top
     */
    public final static TextStylePropertyName<PaddingTop> PADDING_TOP = registerHasJsonNodeConstant("padding-top",
            PaddingTop.class,
            PaddingTop::fromJsonNode,
            (p, v) -> v.visitPaddingTop(p));

    /**
     * tab-size
     */
    public final static TextStylePropertyName<TabSize> TAB_SIZE = registerHasJsonNodeConstant("tab-size",
            TabSize.class,
            TabSize::fromJsonNode,
            (t, v) -> v.visitTabSize(t));

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
            Color::fromJsonNode,
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
            Color::fromJsonNode,
            (t, v) -> v.visitTextDecorationColor(t));

    /**
     * text-decoration-style
     */
    public final static TextStylePropertyName<TextDecorationStyle> TEXT_DECORATION_STYLE = registerEnumConstant("text-decoration-style",
            TextDecorationStyle::valueOf,
            TextDecorationStyle.class,
            (t, v) -> v.visitTextDecorationStyle(t));

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
     * white-space
     */
    public final static TextStylePropertyName<TextWhitespace> WHITE_SPACE = registerEnumConstant("white-space",
            TextWhitespace::valueOf,
            TextWhitespace.class,
            (w, v) -> v.visitWhitespace(w));

    /**
     * width
     */
    public final static TextStylePropertyName<Width> WIDTH = registerHasJsonNodeConstant("width",
            Width.class,
            Width::fromJsonNode,
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
