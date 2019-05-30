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
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * The name of an expression node.
 */
public final class TextPropertyName<T> implements Name,
        Comparable<TextPropertyName<?>> {

    /**
     * First because required by {@link #CONSTANTS} init.
     */
    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;

    // constants

    /**
     * A read only cache of already properties.
     */
    final static Map<String, TextPropertyName<?>> CONSTANTS = Maps.sorted(TextPropertyName.CASE_SENSITIVITY.comparator());

    /**
     * Creates and adds a new {@link TextPropertyName} to the cache being built that handles {@link Color} values.
     */
    private static TextPropertyName<Color> registerColorConstant(final String property) {
        return registerConstant(property, TextPropertyValueConverter.color());
    }

    /**
     * Creates and adds a new {@link TextPropertyName} that handles {@link Enum} values.
     */
    private static <E extends Enum<E>> TextPropertyName<E> registerEnumConstant(final String property,
                                                                                final Function<String, E> factory,
                                                                                final Class<E> type) {
        return registerConstant(property, TextPropertyValueConverter.enumTextPropertyValueConverter(factory, type));
    }

    /**
     * Creates and adds a new {@link TextPropertyName} to the cache being built that handles {@link FontFamilyName} values.
     */
    private static TextPropertyName<FontFamilyName> registerFontFamilyNameConstant(final String property) {
        return registerConstant(property, TextPropertyValueConverter.fontFamilyName());
    }

    /**
     * Creates and adds a new {@link TextPropertyName} to the cache being built that handles {@link FontSize} values.
     */
    private static TextPropertyName<FontSize> registerFontSizeConstant(final String property) {
        return registerConstant(property, TextPropertyValueConverter.fontSize());
    }

    /**
     * Creates and adds a new {@link TextPropertyName} to the cache being built that handles {@link FontWeight} values.
     */
    private static TextPropertyName<FontWeight> registerFontWeightConstant(final String property) {
        return registerConstant(property, TextPropertyValueConverter.fontWeight());
    }

    /**
     * Creates and adds a new {@link TextPropertyName} to the cache being built.
     */
    private static <T> TextPropertyName<T> registerConstant(final String property,
                                                            final TextPropertyValueConverter<T> converter) {
        final TextPropertyName<T> textPropertyName = new TextPropertyName<>(property, converter);
        TextPropertyName.CONSTANTS.put(property, textPropertyName);
        return textPropertyName;
    }

    /**
     * Background color
     */
    public final static TextPropertyName<Color> BACKGROUND_COLOR = registerColorConstant("background-color");

    /**
     * font-family-name
     */
    public final static TextPropertyName<FontFamilyName> FONT_FAMILY_NAME = registerFontFamilyNameConstant("font-family-name");

    /**
     * font-kerning
     */
    public final static TextPropertyName<FontKerning> FONT_KERNING = registerEnumConstant("font-kerning",
            FontKerning::valueOf,
            FontKerning.class);

    /**
     * font-size
     */
    public final static TextPropertyName<FontSize> FONT_SIZE = registerFontSizeConstant("font-size");

    /**
     * font-style
     */
    public final static TextPropertyName<FontStyle> FONT_STYLE = registerEnumConstant("font-style",
            FontStyle::valueOf,
            FontStyle.class);

    /**
     * font-variant
     */
    public final static TextPropertyName<FontVariant> FONT_VARIANT = registerEnumConstant("font-variant",
            FontVariant::valueOf,
            FontVariant.class);

    /**
     * font-weight
     */
    public final static TextPropertyName<FontWeight> FONT_WEIGHT = registerFontWeightConstant("font-weight");

    /**
     * horizontal-alignment
     */
    public final static TextPropertyName<HorizontalAlignment> HORIZONTAL_ALIGNMENT = registerEnumConstant("horizontal-alignment",
            HorizontalAlignment::valueOf,
            HorizontalAlignment.class);

    /**
     * hyphens
     */
    public final static TextPropertyName<Hyphens> HYPHENS = registerEnumConstant("hyphens",
            Hyphens::valueOf,
            Hyphens.class);

    /**
     * text-alignment
     */
    public final static TextPropertyName<TextAlignment> TEXT_ALIGNMENT = registerEnumConstant("text-align",
            TextAlignment::valueOf,
            TextAlignment.class);

    /**
     * Text color
     */
    public final static TextPropertyName<Color> TEXT_COLOR = registerColorConstant("text-color");

    /**
     * text-decoration: UNDERLINE, OVERLINE, LINE_THROUGH
     */
    public final static TextPropertyName<TextDecoration> TEXT_DECORATION = registerEnumConstant("text-decoration",
            TextDecoration::valueOf,
            TextDecoration.class);

    /**
     * text-decoration-color
     */
    public final static TextPropertyName<Color> TEXT_DECORATION_COLOR = registerColorConstant("text-decoration-color");

    /**
     * text-decoration-style
     */
    public final static TextPropertyName<TextDecorationStyle> TEXT_DECORATION_STYLE = registerEnumConstant("text-decoration-style",
            TextDecorationStyle::valueOf,
            TextDecorationStyle.class);

    /**
     * text-justify
     */
    public final static TextPropertyName<TextJustify> TEXT_JUSTIFY = registerEnumConstant("text-justify",
            TextJustify::valueOf,
            TextJustify.class);

    /**
     * text-wrapping
     */
    public final static TextPropertyName<TextWrapping> TEXT_WRAPPING = registerEnumConstant("text-wrapping",
            TextWrapping::valueOf,
            TextWrapping.class);

    /**
     * vertical-alignment
     */
    public final static TextPropertyName<VerticalAlignment> VERTICAL_ALIGNMENT = registerEnumConstant("vertical-alignment",
            VerticalAlignment::valueOf,
            VerticalAlignment.class);

    /**
     * white-space
     */
    public final static TextPropertyName<TextWhitespace> WHITE_SPACE = registerEnumConstant("white-space",
            TextWhitespace::valueOf,
            TextWhitespace.class);

    /**
     * Factory that retrieves an existing property or if unknown a property that assumes non empty string value.
     */
    public static TextPropertyName<?> with(final String name) {
        Objects.requireNonNull(name, "name");

        final TextPropertyName<?> textPropertyName = CONSTANTS.get(name);
        return null != textPropertyName ?
                textPropertyName :
                new TextPropertyName<>(checkName(name), TextPropertyValueConverter.string());
    }

    private static String checkName(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(name,
                "name",
                INITIAL,
                PART);
        return name;
    }

    private final static CharPredicate INITIAL = CharPredicates.letter();
    private final static CharPredicate PART = CharPredicates.letterOrDigit().or(CharPredicates.any("-"));

    // @VisibleForTesting
    private TextPropertyName(final String name,
                             final TextPropertyValueConverter<T> converter) {
        super();
        this.name = name;
        this.converter= converter;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    /**
     * Used to convert/validate property values.
     */
    final TextPropertyValueConverter<T> converter;

    /**
     * Returns the name in quotes, useful for error messages.
     */
    CharSequence inQuotes() {
        return CharSequences.quoteAndEscape(this.name);
    }

    // HasJsonNode.....................................................................................................

    static TextPropertyName<?> fromJsonNodeName(final JsonNode node) {
        return TextPropertyName.with(node.name().value());
    }

    JsonNodeName toJsonNodeName() {
        return JsonNodeName.with(this.name);
    }

    // Object..........................................................................................................

    @Override
    public final int hashCode() {
        return CASE_SENSITIVITY.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof TextPropertyName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final TextPropertyName other) {
        return CASE_SENSITIVITY.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Comparable ......................................................................................................

    @Override
    public int compareTo(final TextPropertyName other) {
        return CASE_SENSITIVITY.comparator().compare(this.name, other.name);
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }
}
