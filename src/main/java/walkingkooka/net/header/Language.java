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

package walkingkooka.net.header;

import walkingkooka.collect.map.Maps;
import walkingkooka.net.HasQFactorWeight;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Holds a Language which is the value of a accept-language and similar headers.
 */
public final class Language extends HeaderValueWithParameters2<Language, LanguageParameterName<?>, LanguageName>
        implements Predicate<Language>,
        HasQFactorWeight {

    /**
     * No parameters constant.
     */
    public final static Map<LanguageParameterName<?>, Object> NO_PARAMETERS = Maps.empty();

    /**
     * A {@link Language} wildcard without any parameters.
     */
    public final static Language WILDCARD = new Language(LanguageName.WILDCARD, NO_PARAMETERS);

    /**
     * Factory that creates a new {@link Language}
     */
    public static Language with(final LanguageName value) {
        checkValue(value);

        return value.isWildcard() ?
                WILDCARD :
                new Language(value, NO_PARAMETERS);
    }

    /**
     * Parsers a header value holding a single tag.
     */
    public static Language parse(final String text) {
        return LanguageOneHeaderValueParser.parseOne(text);
    }

    /**
     * Parsers a header value which may hold one or more tags.
     */
    public static List<Language> parseList(final String text) {
        return LanguageListHeaderValueParser.parseList(text);
    }

    /**
     * Private ctor use factory methods.
     */
    private Language(final LanguageName value, final Map<LanguageParameterName<?>, Object> parameters) {
        super(value, parameters);
    }

    // Predicate.......................................................................................................

    /**
     * Only returns true if languageTag is matched by the given languageTag. If this is a wildcard it matches any other languageTag.
     * If the argument is a wildcard a false is always returned even if this is a wildcard.
     */
    @Override
    public boolean test(final Language languageTag) {
        Objects.requireNonNull(languageTag, "languageTag");
        return this.value.test(languageTag);
    }

    // value............................................................................................................

    /**
     * Would be setter that returns a {@link Language} with the given value creating a new instance as necessary.
     */
    public Language setValue(final LanguageName value) {
        checkValue(value);

        return this.value.equals(value) ?
                this :
                this.replace(value);
    }

    private static void checkValue(final LanguageName value) {
        Objects.requireNonNull(value, "value");
    }

    // replace ........................................................................................................

    private Language replace(final LanguageName value) {
        return this.replace0(value, this.parameters);
    }

    @Override
    Language replace(final Map<LanguageParameterName<?>, Object> parameters) {
        return this.replace0(this.value, parameters);
    }

    private Language replace0(final LanguageName name,
                              final Map<LanguageParameterName<?>, Object> parameters) {
        return name.isWildcard() && parameters.isEmpty() ?
                WILDCARD :
                new Language(name, parameters);
    }

    // headerValue........................................................................................................

    @Override
    String toHeaderTextValue() {
        return this.value.toString();
    }

    @Override
    String toHeaderTextParameterSeparator() {
        return TO_HEADERTEXT_PARAMETER_SEPARATOR;
    }

    /**
     * Returns true if this Language is a wildcard.
     */
    public boolean isWildcard() {
        return this.value.isWildcard();
    }

    // HasQFactorWeight................................................................................................

    /**
     * Retrieves the q-weight for this value.
     */
    public Optional<Float> qFactorWeight() {
        return this.qFactorWeight(LanguageParameterName.Q_FACTOR);
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
    int hashCode0(final LanguageName value) {
        return value.hashCode();
    }

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof Language;
    }

    @Override
    boolean equals1(final LanguageName value, final LanguageName otherValue) {
        return value.equals(otherValue);
    }
}


