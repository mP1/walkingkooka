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

import walkingkooka.collect.map.Maps;
import walkingkooka.net.HasQFactorWeight;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Holds a LanguageTag which is the value of a accept-language and similar headers.
 */
public final class LanguageTag extends HeaderValueWithParameters2<LanguageTag,
        LanguageTagParameterName<?>,
        LanguageTagName> implements HasQFactorWeight {

    /**
     * No parameters constant.
     */
    public final static Map<LanguageTagParameterName<?>, Object> NO_PARAMETERS = Maps.empty();

    /**
     * A {@link LanguageTag} wildcard without any parameters.
     */
    public final static LanguageTag WILDCARD = new LanguageTag(LanguageTagName.WILDCARD, NO_PARAMETERS);

    /**
     * Factory that creates a new {@link LanguageTag}
     */
    public static LanguageTag with(final LanguageTagName value) {
        checkValue(value);

        return value.isWildcard() ?
                WILDCARD :
                new LanguageTag(value, NO_PARAMETERS);
    }

    /**
     * Parsers a header value holding a single tag.
     */
    public static LanguageTag parse(final String text) {
        return LanguageTagOneHeaderParser.parseOne(text);
    }

    /**
     * Parsers a header value which may hold one or more tags.
     */
    public static List<LanguageTag> parseList(final String text) {
        return LanguageTagListHeaderParser.parseList(text);
    }

    /**
     * Package private to limit sub classing.
     */
    LanguageTag(final LanguageTagName value, final Map<LanguageTagParameterName<?>, Object> parameters) {
        super(value, parameters);
    }

    /**
     * Only returns true if languageTag is matched by the given languageTag. If this is a wildcard it matches any other languageTag.
     * If the argument is a wildcard a false is always returned even if this is a wildcard.
     */
    public final boolean isMatch(final LanguageTag languageTag) {
        Objects.requireNonNull(languageTag, "languageTag");
        return this.value.isMatch(languageTag);
    }

    // value.....................................................................................................

    /**
     * Would be setter that returns a {@link LanguageTag} with the given value creating a new instance as necessary.
     */
    public final LanguageTag setValue(final LanguageTagName value) {
        checkValue(value);

        return this.value.equals(value) ?
                this :
                this.replace(value);
    }

    static void checkValue(final LanguageTagName value) {
        Objects.requireNonNull(value, "value");
    }

    // replace ........................................................................................................

    private LanguageTag replace(final LanguageTagName value) {
        return this.replace0(value, this.parameters);
    }

    @Override
    LanguageTag replace(final Map<LanguageTagParameterName<?>, Object> parameters) {
        return this.replace0(this.value, parameters);
    }

    private LanguageTag replace0(final LanguageTagName name,
                                 final Map<LanguageTagParameterName<?>, Object> parameters) {
        return name.isWildcard() && parameters.isEmpty() ?
                WILDCARD :
                new LanguageTag(name, parameters);
    }

    // isXXX........................................................................................................

    /**
     * Returns true if this LanguageTag is a wildcard.
     */
    public boolean isWildcard() {
        return this.value.isWildcard();
    }

    // HasQFactorWeight................................................................................................

    /**
     * Retrieves the q-weight for this value.
     */
    public final Optional<Float> qFactorWeight() {
        return this.qFactorWeight(LanguageTagParameterName.Q_FACTOR);
    }

    // HeaderValue........................................................................................................

    /**
     * Returns the text or header value form.
     */
    public String toHeaderText() {
        return this.toString();
    }

    // HasHeaderScope ....................................................................................................

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    // Object..........................................................................................................

    @Override
    int hashCode0(final LanguageTagName value) {
        return value.hashCode();
    }

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof LanguageTag;
    }

    @Override
    boolean equals1(final LanguageTagName value, final LanguageTagName otherValue) {
        return value.equals(otherValue);
    }
}


