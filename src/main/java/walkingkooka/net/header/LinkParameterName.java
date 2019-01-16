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
final public class LinkParameterName<T> extends HeaderParameterName<T> implements Comparable<LinkParameterName<?>> {

    /**
     * A read only cache of already prepared {@link LinkParameterName names}. These constants are incomplete.
     */
    private final static HeaderParameterNameConstants<LinkParameterName<?>> CONSTANTS = HeaderParameterNameConstants.empty(
            LinkParameterName::new,
            HeaderValueConverter.quotedUnquotedString(
                    LinkHeaderParser.QUOTED_PARAMETER_VALUE,
                    true,
                    LinkHeaderParser.UNQUOTED_PARAMETER_VALUE)
    );

    /**
     * Holds the rel parameter name.
     */
    public final static LinkParameterName<List<LinkRelation<?>>> REL = CONSTANTS.register("rel", HeaderValueConverter.relation());

    /**
     * Holds the anchor parameter name.
     */
    public final static LinkParameterName<Url> ANCHOR = CONSTANTS.register("anchor", HeaderValueConverter.absoluteUrl());

    /**
     * Holds the rev parameter name.
     */
    public final static LinkParameterName<List<LinkRelation<?>>> REV = CONSTANTS.register("rev", HeaderValueConverter.relation());

    /**
     * Holds the hreflang parameter name.
     */
    public final static LinkParameterName<LanguageTagName> HREFLANG = CONSTANTS.register("hreflang", HeaderValueConverter.languageTagName());

    /**
     * The media type parameter.
     */
    public final static LinkParameterName<MediaType> MEDIA_TYPE = CONSTANTS.register("mediaType", HeaderValueConverter.mediaType());

    /**
     * The title parameter.
     * This should be used for ascii only title text.
     */
    public final static LinkParameterName<EncodedText> TITLE = CONSTANTS.register("title", HeaderValueConverter.quotedUnquotedString(
            CharPredicates.asciiPrintable(),
            false,
            CharPredicates.rfc2045Token()));

    /**
     * The title star parameter.
     * This should be used for all non ascii safe title text.
     */
    public final static LinkParameterName<EncodedText> TITLE_STAR = CONSTANTS.register("title*", HeaderValueConverter.encodedText());

    /**
     * The type parameter.
     */
    public final static LinkParameterName<HttpMethod> TYPE = CONSTANTS.register("type", HeaderValueConverter.method());

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
                              final HeaderValueConverter<T> converter) {
        super(value, converter);
    }

    // Comparable......................................................................................................

    @Override
    public int compareTo(final LinkParameterName<?> other) {
        return this.compareTo0(other);
    }

    // Object

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LinkParameterName;
    }
}
