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

package walkingkooka.net.header;

import walkingkooka.naming.Name;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.predicate.character.CharPredicates;

import java.util.List;


/**
 * The {@link Name} of an optional parameter accompanying a {@link Link}. Note the name may not contain the whitespace, equals sign, semi colons
 * or commas.
 * <a href="https://restfulapi.net/hateoas/"></a>
 */
final public class LinkParameterName<V> extends HeaderParameterName<V> implements Comparable<LinkParameterName<?>> {

    /**
     * A read only cache of already prepared {@link LinkParameterName names}. These constants are incomplete.
     */
    private final static HeaderParameterNameConstants<LinkParameterName<?>> CONSTANTS = HeaderParameterNameConstants.empty(
            LinkParameterName::new,
            HeaderValueHandler.quotedUnquotedString(
                    LinkHeaderValueParser.QUOTED_PARAMETER_VALUE,
                    true,
                    LinkHeaderValueParser.UNQUOTED_PARAMETER_VALUE)
    );

    /**
     * Holds the rel parameter name.
     */
    public final static LinkParameterName<List<LinkRelation<?>>> REL = CONSTANTS.register("rel", HeaderValueHandler.relation());

    /**
     * Holds the anchor parameter name.
     */
    public final static LinkParameterName<Url> ANCHOR = CONSTANTS.register("anchor", HeaderValueHandler.absoluteUrl());

    /**
     * Holds the rev parameter name.
     */
    public final static LinkParameterName<List<LinkRelation<?>>> REV = CONSTANTS.register("rev", HeaderValueHandler.relation());

    /**
     * Holds the hreflang parameter name.
     */
    public final static LinkParameterName<LanguageTagName> HREFLANG = CONSTANTS.register("hreflang", HeaderValueHandler.languageTagName());

    /**
     * The media type parameter.<br>
     * <a href="https://www.w3.org/TR/1999/REC-html401-19991224/types.html#h-6.13"></a>
     * <pre>
     * 6.13 Media descriptors
     * The following is a list of recognized media descriptors ( %MediaDesc in the DTD).
     *
     * screen
     * Intended for non-paged computer screens.
     * tty
     * Intended for media using a fixed-pitch character grid, such as teletypes, terminals, or portable devices with limited display capabilities.
     * tv
     * Intended for television-type devices (low resolution, color, limited scrollability).
     * projection
     * Intended for projectors.
     * handheld
     * Intended for handheld devices (small screen, monochrome, bitmapped graphics, limited bandwidth).
     * print
     * Intended for paged, opaque material and for documents viewed on screen in print preview mode.
     * braille
     * Intended for braille tactile feedback devices.
     * aural
     * Intended for speech synthesizers.
     * all
     * Suitable for all devices.
     * Future versions of HTML may introduce new values and may allow parameterized values. To facilitate the introduction of these extensions, conforming user agents must be able to parse the media attribute value as follows:
     *
     * The value is a comma-separated list of entries. For example,
     * media="screen, 3d-glasses, print and resolution > 90dpi"
     * is mapped to:
     *
     * "screen"
     * "3d-glasses"
     * "print and resolution > 90dpi"
     * Each entry is truncated just before the first character that isn't a US ASCII letter [a-zA-Z] (ISO 10646 hex 41-5a, 61-7a), digit [0-9] (hex 30-39), or hyphen (hex 2d). In the example, this gives:
     * "screen"
     * "3d-glasses"
     * "print"
     * A case-sensitive match is then made with the set of media types defined above. User agents may ignore entries that don't match. In the example we are left with screen and print.
     * Note. Style sheets may include media-dependent variations within them (e.g., the CSS @media construct). In such cases it may be appropriate to use "media=all".
     * </pre>
     */
    public final static LinkParameterName<String> MEDIA = CONSTANTS.register("media", HeaderValueHandler.quotedUnquotedString(LinkHeaderValueParser.QUOTED_PARAMETER_VALUE,
            true, // backslash escaping...
            LinkHeaderValueParser.UNQUOTED_PARAMETER_VALUE));

    /**
     * A non standard parameter to hold the {@link HttpMethod} for this resource
     */
    public final static LinkParameterName<HttpMethod> METHOD = CONSTANTS.register("method", HeaderValueHandler.method());

    /**
     * The title parameter.
     * This should be used for ascii only title text.
     */
    public final static LinkParameterName<EncodedText> TITLE = CONSTANTS.register("title", HeaderValueHandler.quotedUnquotedString(
            CharPredicates.asciiPrintable(),
            false,
            CharPredicates.rfc2045Token()));

    /**
     * The title star parameter.
     * This should be used for all non ascii safe title text.
     */
    public final static LinkParameterName<EncodedText> TITLE_STAR = CONSTANTS.register("title*", HeaderValueHandler.encodedText());

    /**
     * The type parameter, which holds the {@link MediaType content type} for this link.
     */
    public final static LinkParameterName<MediaType> TYPE = CONSTANTS.register("type", HeaderValueHandler.mediaType());

    /**
     * Factory that creates a {@link LinkParameterName}
     */
    public static LinkParameterName<?> with(final String value) {
        return CONSTANTS.lookup(value);
    }

    /**
     * Private ctor use factory.
     */
    private LinkParameterName(final String value,
                              final HeaderValueHandler<V> handler) {
        super(value, handler);
    }

    // Comparable......................................................................................................

    @Override
    public int compareTo(final LinkParameterName<?> other) {
        return this.compareTo0(other);
    }

    // HeaderName2......................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LinkParameterName;
    }
}
