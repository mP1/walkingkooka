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

package walkingkooka.color;

import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A web color name.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/color_value"></a>
 */
public final class WebColorName implements Name, Comparable<WebColorName> {

    /**
     * First because required by {@link #NAME_CONSTANTS} init.
     * Probably should be case sensitive but to be a bit more practical make insensitive.
     */
    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    private final static CharPredicate INITIAL = CharPredicates.letter();
    private final static CharPredicate PART = CharPredicates.letterOrDigit();

    // constants

    /**
     * A read only cache of {@link WebColorName} constants.
     * <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/color_value"></a>
     * <pre>
     * var rows = document.evaluate( "//*[@id='colors_table']/tbody/tr", document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null );
     * var s = "";
     *
     * for ( var i=0 ; i < rows.snapshotLength; i++ )
     * {
     *     var row = rows.snapshotItem(i);
     *     var columns = row.getElementsByTagName('td');
     *     var name = columns.item(1).textContent;
     *     var value = columns.item(2).textContent;
     *
     *     s = s + "public final static WebColorName " + name.toUpperCase() + " = registerConstant(\"" + name + "\", \"" + value + "\");\n";
     * }
     * console.log(s);
     * </pre>
     */
    private final static Map<String, WebColorName> NAME_CONSTANTS = Maps.sorted(WebColorName.CASE_SENSITIVITY.comparator());

    /**
     * Allows lookup by RRGGBB value, used by {@link Color#webColorName}.
     */
    final static Map<Integer, WebColorName> RRGGBB_CONSTANTS = Maps.sorted();

    // generated constants begin........................................................................................

    public final static WebColorName BLACK = registerConstant("black", "#000000");
    public final static WebColorName SILVER = registerConstant("silver", "#c0c0c0");
    public final static WebColorName GRAY = registerConstant("gray", "#808080");
    public final static WebColorName WHITE = registerConstant("white", "#ffffff");
    public final static WebColorName MAROON = registerConstant("maroon", "#800000");
    public final static WebColorName RED = registerConstant("red", "#ff0000");
    public final static WebColorName PURPLE = registerConstant("purple", "#800080");
    public final static WebColorName FUCHSIA = registerConstant("fuchsia", "#ff00ff");
    public final static WebColorName GREEN = registerConstant("green", "#008000");
    public final static WebColorName LIME = registerConstant("lime", "#00ff00");
    public final static WebColorName OLIVE = registerConstant("olive", "#808000");
    public final static WebColorName YELLOW = registerConstant("yellow", "#ffff00");
    public final static WebColorName NAVY = registerConstant("navy", "#000080");
    public final static WebColorName BLUE = registerConstant("blue", "#0000ff");
    public final static WebColorName TEAL = registerConstant("teal", "#008080");
    public final static WebColorName AQUA = registerConstant("aqua", "#00ffff");
    public final static WebColorName ORANGE = registerConstant("orange", "#ffa500");
    public final static WebColorName ALICEBLUE = registerConstant("aliceblue", "#f0f8ff");
    public final static WebColorName ANTIQUEWHITE = registerConstant("antiquewhite", "#faebd7");
    public final static WebColorName AQUAMARINE = registerConstant("aquamarine", "#7fffd4");
    public final static WebColorName AZURE = registerConstant("azure", "#f0ffff");
    public final static WebColorName BEIGE = registerConstant("beige", "#f5f5dc");
    public final static WebColorName BISQUE = registerConstant("bisque", "#ffe4c4");
    public final static WebColorName BLANCHEDALMOND = registerConstant("blanchedalmond", "#ffebcd");
    public final static WebColorName BLUEVIOLET = registerConstant("blueviolet", "#8a2be2");
    public final static WebColorName BROWN = registerConstant("brown", "#a52a2a");
    public final static WebColorName BURLYWOOD = registerConstant("burlywood", "#deb887");
    public final static WebColorName CADETBLUE = registerConstant("cadetblue", "#5f9ea0");
    public final static WebColorName CHARTREUSE = registerConstant("chartreuse", "#7fff00");
    public final static WebColorName CHOCOLATE = registerConstant("chocolate", "#d2691e");
    public final static WebColorName CORAL = registerConstant("coral", "#ff7f50");
    public final static WebColorName CORNFLOWERBLUE = registerConstant("cornflowerblue", "#6495ed");
    public final static WebColorName CORNSILK = registerConstant("cornsilk", "#fff8dc");
    public final static WebColorName CRIMSON = registerConstant("crimson", "#dc143c");
    public final static WebColorName CYAN = registerConstant("cyan", "#00ffff"); // aqua
    public final static WebColorName DARKBLUE = registerConstant("darkblue", "#00008b");
    public final static WebColorName DARKCYAN = registerConstant("darkcyan", "#008b8b");
    public final static WebColorName DARKGOLDENROD = registerConstant("darkgoldenrod", "#b8860b");
    public final static WebColorName DARKGRAY = registerConstant("darkgray", "#a9a9a9");
    public final static WebColorName DARKGREEN = registerConstant("darkgreen", "#006400");
    public final static WebColorName DARKGREY = registerConstant("darkgrey", "#a9a9a9");
    public final static WebColorName DARKKHAKI = registerConstant("darkkhaki", "#bdb76b");
    public final static WebColorName DARKMAGENTA = registerConstant("darkmagenta", "#8b008b");
    public final static WebColorName DARKOLIVEGREEN = registerConstant("darkolivegreen", "#556b2f");
    public final static WebColorName DARKORANGE = registerConstant("darkorange", "#ff8c00");
    public final static WebColorName DARKORCHID = registerConstant("darkorchid", "#9932cc");
    public final static WebColorName DARKRED = registerConstant("darkred", "#8b0000");
    public final static WebColorName DARKSALMON = registerConstant("darksalmon", "#e9967a");
    public final static WebColorName DARKSEAGREEN = registerConstant("darkseagreen", "#8fbc8f");
    public final static WebColorName DARKSLATEBLUE = registerConstant("darkslateblue", "#483d8b");
    public final static WebColorName DARKSLATEGRAY = registerConstant("darkslategray", "#2f4f4f");
    public final static WebColorName DARKSLATEGREY = registerConstant("darkslategrey", "#2f4f4f");
    public final static WebColorName DARKTURQUOISE = registerConstant("darkturquoise", "#00ced1");
    public final static WebColorName DARKVIOLET = registerConstant("darkviolet", "#9400d3");
    public final static WebColorName DEEPPINK = registerConstant("deeppink", "#ff1493");
    public final static WebColorName DEEPSKYBLUE = registerConstant("deepskyblue", "#00bfff");
    public final static WebColorName DIMGRAY = registerConstant("dimgray", "#696969");
    public final static WebColorName DIMGREY = registerConstant("dimgrey", "#696969");
    public final static WebColorName DODGERBLUE = registerConstant("dodgerblue", "#1e90ff");
    public final static WebColorName FIREBRICK = registerConstant("firebrick", "#b22222");
    public final static WebColorName FLORALWHITE = registerConstant("floralwhite", "#fffaf0");
    public final static WebColorName FORESTGREEN = registerConstant("forestgreen", "#228b22");
    public final static WebColorName GAINSBORO = registerConstant("gainsboro", "#dcdcdc");
    public final static WebColorName GHOSTWHITE = registerConstant("ghostwhite", "#f8f8ff");
    public final static WebColorName GOLD = registerConstant("gold", "#ffd700");
    public final static WebColorName GOLDENROD = registerConstant("goldenrod", "#daa520");
    public final static WebColorName GREENYELLOW = registerConstant("greenyellow", "#adff2f");
    public final static WebColorName GREY = registerConstant("grey", "#808080");
    public final static WebColorName HONEYDEW = registerConstant("honeydew", "#f0fff0");
    public final static WebColorName HOTPINK = registerConstant("hotpink", "#ff69b4");
    public final static WebColorName INDIANRED = registerConstant("indianred", "#cd5c5c");
    public final static WebColorName INDIGO = registerConstant("indigo", "#4b0082");
    public final static WebColorName IVORY = registerConstant("ivory", "#fffff0");
    public final static WebColorName KHAKI = registerConstant("khaki", "#f0e68c");
    public final static WebColorName LAVENDER = registerConstant("lavender", "#e6e6fa");
    public final static WebColorName LAVENDERBLUSH = registerConstant("lavenderblush", "#fff0f5");
    public final static WebColorName LAWNGREEN = registerConstant("lawngreen", "#7cfc00");
    public final static WebColorName LEMONCHIFFON = registerConstant("lemonchiffon", "#fffacd");
    public final static WebColorName LIGHTBLUE = registerConstant("lightblue", "#add8e6");
    public final static WebColorName LIGHTCORAL = registerConstant("lightcoral", "#f08080");
    public final static WebColorName LIGHTCYAN = registerConstant("lightcyan", "#e0ffff");
    public final static WebColorName LIGHTGOLDENRODYELLOW = registerConstant("lightgoldenrodyellow", "#fafad2");
    public final static WebColorName LIGHTGRAY = registerConstant("lightgray", "#d3d3d3");
    public final static WebColorName LIGHTGREEN = registerConstant("lightgreen", "#90ee90");
    public final static WebColorName LIGHTGREY = registerConstant("lightgrey", "#d3d3d3");
    public final static WebColorName LIGHTPINK = registerConstant("lightpink", "#ffb6c1");
    public final static WebColorName LIGHTSALMON = registerConstant("lightsalmon", "#ffa07a");
    public final static WebColorName LIGHTSEAGREEN = registerConstant("lightseagreen", "#20b2aa");
    public final static WebColorName LIGHTSKYBLUE = registerConstant("lightskyblue", "#87cefa");
    public final static WebColorName LIGHTSLATEGRAY = registerConstant("lightslategray", "#778899");
    public final static WebColorName LIGHTSLATEGREY = registerConstant("lightslategrey", "#778899");
    public final static WebColorName LIGHTSTEELBLUE = registerConstant("lightsteelblue", "#b0c4de");
    public final static WebColorName LIGHTYELLOW = registerConstant("lightyellow", "#ffffe0");
    public final static WebColorName LIMEGREEN = registerConstant("limegreen", "#32cd32");
    public final static WebColorName LINEN = registerConstant("linen", "#faf0e6");
    public final static WebColorName MAGENTA = registerConstant("magenta", "#ff00ff"); //SYNONYM OF FUCHSIA
    public final static WebColorName MEDIUMAQUAMARINE = registerConstant("mediumaquamarine", "#66cdaa");
    public final static WebColorName MEDIUMBLUE = registerConstant("mediumblue", "#0000cd");
    public final static WebColorName MEDIUMORCHID = registerConstant("mediumorchid", "#ba55d3");
    public final static WebColorName MEDIUMPURPLE = registerConstant("mediumpurple", "#9370db");
    public final static WebColorName MEDIUMSEAGREEN = registerConstant("mediumseagreen", "#3cb371");
    public final static WebColorName MEDIUMSLATEBLUE = registerConstant("mediumslateblue", "#7b68ee");
    public final static WebColorName MEDIUMSPRINGGREEN = registerConstant("mediumspringgreen", "#00fa9a");
    public final static WebColorName MEDIUMTURQUOISE = registerConstant("mediumturquoise", "#48d1cc");
    public final static WebColorName MEDIUMVIOLETRED = registerConstant("mediumvioletred", "#c71585");
    public final static WebColorName MIDNIGHTBLUE = registerConstant("midnightblue", "#191970");
    public final static WebColorName MINTCREAM = registerConstant("mintcream", "#f5fffa");
    public final static WebColorName MISTYROSE = registerConstant("mistyrose", "#ffe4e1");
    public final static WebColorName MOCCASIN = registerConstant("moccasin", "#ffe4b5");
    public final static WebColorName NAVAJOWHITE = registerConstant("navajowhite", "#ffdead");
    public final static WebColorName OLDLACE = registerConstant("oldlace", "#fdf5e6");
    public final static WebColorName OLIVEDRAB = registerConstant("olivedrab", "#6b8e23");
    public final static WebColorName ORANGERED = registerConstant("orangered", "#ff4500");
    public final static WebColorName ORCHID = registerConstant("orchid", "#da70d6");
    public final static WebColorName PALEGOLDENROD = registerConstant("palegoldenrod", "#eee8aa");
    public final static WebColorName PALEGREEN = registerConstant("palegreen", "#98fb98");
    public final static WebColorName PALETURQUOISE = registerConstant("paleturquoise", "#afeeee");
    public final static WebColorName PALEVIOLETRED = registerConstant("palevioletred", "#db7093");
    public final static WebColorName PAPAYAWHIP = registerConstant("papayawhip", "#ffefd5");
    public final static WebColorName PEACHPUFF = registerConstant("peachpuff", "#ffdab9");
    public final static WebColorName PERU = registerConstant("peru", "#cd853f");
    public final static WebColorName PINK = registerConstant("pink", "#ffc0cb");
    public final static WebColorName PLUM = registerConstant("plum", "#dda0dd");
    public final static WebColorName POWDERBLUE = registerConstant("powderblue", "#b0e0e6");
    public final static WebColorName ROSYBROWN = registerConstant("rosybrown", "#bc8f8f");
    public final static WebColorName ROYALBLUE = registerConstant("royalblue", "#4169e1");
    public final static WebColorName SADDLEBROWN = registerConstant("saddlebrown", "#8b4513");
    public final static WebColorName SALMON = registerConstant("salmon", "#fa8072");
    public final static WebColorName SANDYBROWN = registerConstant("sandybrown", "#f4a460");
    public final static WebColorName SEAGREEN = registerConstant("seagreen", "#2e8b57");
    public final static WebColorName SEASHELL = registerConstant("seashell", "#fff5ee");
    public final static WebColorName SIENNA = registerConstant("sienna", "#a0522d");
    public final static WebColorName SKYBLUE = registerConstant("skyblue", "#87ceeb");
    public final static WebColorName SLATEBLUE = registerConstant("slateblue", "#6a5acd");
    public final static WebColorName SLATEGRAY = registerConstant("slategray", "#708090");
    public final static WebColorName SLATEGREY = registerConstant("slategrey", "#708090");
    public final static WebColorName SNOW = registerConstant("snow", "#fffafa");
    public final static WebColorName SPRINGGREEN = registerConstant("springgreen", "#00ff7f");
    public final static WebColorName STEELBLUE = registerConstant("steelblue", "#4682b4");
    public final static WebColorName TAN = registerConstant("tan", "#d2b48c");
    public final static WebColorName THISTLE = registerConstant("thistle", "#d8bfd8");
    public final static WebColorName TOMATO = registerConstant("tomato", "#ff6347");
    public final static WebColorName TURQUOISE = registerConstant("turquoise", "#40e0d0");
    public final static WebColorName VIOLET = registerConstant("violet", "#ee82ee");
    public final static WebColorName WHEAT = registerConstant("wheat", "#f5deb3");
    public final static WebColorName WHITESMOKE = registerConstant("whitesmoke", "#f5f5f5");
    public final static WebColorName YELLOWGREEN = registerConstant("yellowgreen", "#9acd32");
    public final static WebColorName REBECCAPURPLE = registerConstant("rebeccapurple", "#663399");


    /**
     * <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/color_value"></a>
     * <pre>
     * The transparent keyword represents a fully transparent color.
     * This makes the background behind the colored item completely visible. Technically, transparent is a shortcut for rgba(0,0,0,0).
     * </pre>
     */
    public final static WebColorName TRANSPARENT = registerConstant("transparent", "#00000000");

    // generated constants end..........................................................................................

    /**
     * Creates and adds a new {@link WebColorName} to the cache being built.
     */
    private static <T> WebColorName registerConstant(final String name,
                                                     final String text) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(name,
                "name",
                INITIAL,
                PART);
        if (text.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty text for " + CharSequences.quoteAndEscape(name));
        }

        final WebColorName webColorName = new WebColorName(name, Color.parseColor(text));
        WebColorName.NAME_CONSTANTS.put(name, webColorName);
        return webColorName;
    }

    /**
     * Attempts to lookup a {@link WebColorName} by name.
     */
    public static Optional<WebColorName> with(final String name) {
        Objects.requireNonNull(name, "name");

        return Optional.ofNullable(NAME_CONSTANTS.get(name));
    }

    // @VisibleForTesting
    WebColorName(final String name, final Color color) {
        super();
        this.name = name;
        this.color = color;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    /**
     * Returns the {@link Color} with this name.
     */
    public Color color() {
        return this.color;
    }

    private final Color color;

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.name;
    }

    // Comparable ......................................................................................................

    @Override
    public int compareTo(final WebColorName other) {
        return CASE_SENSITIVITY.comparator().compare(this.name, other.name);
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }
}
