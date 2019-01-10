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

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Holds a cache control directive name.
 */
public final class CacheControlDirectiveName<T> implements HeaderName<Optional<T>>, Comparable<CacheControlDirectiveName<?>> {

    /**
     * Registers one of the parameterless directive names.
     */
    private static CacheControlDirectiveName<Void> registerWithoutParameter(final String name,
                                                                            final CacheControlDirectiveNameScope scope) {
        return register(name,
                CacheControlDirectiveNameParameter.ABSENT,
                null,
                scope);
    }

    /**
     * Registers a directive name with a required seconds parameter.
     */
    private static CacheControlDirectiveName<Long> registerWithRequiredSecondsParameter(final String name,
                                                                                        final CacheControlDirectiveNameScope scope) {
        return register(name,
                CacheControlDirectiveNameParameter.REQUIRED,
                HeaderValueConverter.longConverter(),
                scope);
    }

    /**
     * Registers a directive name including some meta data about its parameters.
     */
    private static <T> CacheControlDirectiveName<T> register(final String name,
                                                             final CacheControlDirectiveNameParameter required,
                                                             final HeaderValueConverter<T> value,
                                                             final CacheControlDirectiveNameScope scope) {
        final CacheControlDirectiveName<T> constant = new CacheControlDirectiveName<T>(name, required, value, scope);
        CONSTANTS.put(name, constant);
        return constant;
    }

    /**
     * Holds all constants.
     */
    private final static Map<String, CacheControlDirectiveName> CONSTANTS = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

    /**
     * max-age
     */
    public final static CacheControlDirectiveName<Long> MAX_AGE = registerWithRequiredSecondsParameter("max-age",
            CacheControlDirectiveNameScope.REQUEST_RESPONSE);

    /**
     * max-stale
     */
    public final static CacheControlDirectiveName<Long> MAX_STALE = register("max-stale",
            CacheControlDirectiveNameParameter.OPTIONAL,
            HeaderValueConverter.longConverter(),
            CacheControlDirectiveNameScope.REQUEST);

    /**
     * min-fresh
     */
    public final static CacheControlDirectiveName<Long> MIN_FRESH = registerWithRequiredSecondsParameter("min-fresh",
            CacheControlDirectiveNameScope.REQUEST);

    /**
     * must-revalidate
     */
    public final static CacheControlDirectiveName<Void> MUST_REVALIDATE = registerWithoutParameter("must-revalidate",
            CacheControlDirectiveNameScope.RESPONSE);

    /**
     * no-cache
     */
    public final static CacheControlDirectiveName<Void> NO_CACHE = registerWithoutParameter("no-cache",
            CacheControlDirectiveNameScope.REQUEST_RESPONSE);

    /**
     * no-store
     */
    public final static CacheControlDirectiveName<Void> NO_STORE = registerWithoutParameter("no-store",
            CacheControlDirectiveNameScope.REQUEST_RESPONSE);

    /**
     * no-transform
     */
    public final static CacheControlDirectiveName<Void> NO_TRANSFORM = registerWithoutParameter("no-transform",
            CacheControlDirectiveNameScope.REQUEST_RESPONSE);

    /**
     * only-if-cached
     */
    public final static CacheControlDirectiveName<Void> ONLY_IF_CACHED = registerWithoutParameter("only-if-cached",
            CacheControlDirectiveNameScope.REQUEST);

    /**
     * private
     */
    public final static CacheControlDirectiveName<Void> PRIVATE = registerWithoutParameter("private",
            CacheControlDirectiveNameScope.RESPONSE);

    /**
     * proxy-revalidate
     */
    public final static CacheControlDirectiveName<Void> PROXY_REVALIDATE = registerWithoutParameter("proxy-revalidate",
            CacheControlDirectiveNameScope.RESPONSE);

    /**
     * public
     */
    public final static CacheControlDirectiveName<Void> PUBLIC = registerWithoutParameter("public",
            CacheControlDirectiveNameScope.RESPONSE);

    /**
     * s-maxage
     */
    public final static CacheControlDirectiveName<Long> S_MAXAGE = registerWithRequiredSecondsParameter("s-maxage",
            CacheControlDirectiveNameScope.RESPONSE);

    // token predicates ................................................................................................

    static final CharPredicate INITIAL_CHAR_PREDICATE = CharPredicates.builder()
            .range('A', 'Z')
            .range('a', 'z')
            .range('0', '9')
            .build()
            .setToString("Charset initial");
    static final CharPredicate PART_CHAR_PREDICATE = CharPredicates.rfc2045Token();

    // factory ..........................................................................................................

    /**
     * Returns a {@link CacheControlDirectiveName}
     */
    public static CacheControlDirectiveName<?> with(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(name,
                "value",
                INITIAL_CHAR_PREDICATE,
                PART_CHAR_PREDICATE);

        final CacheControlDirectiveName directiveName = CONSTANTS.get(name);
        return null != directiveName ?
                directiveName :
                new CacheControlDirectiveName<>(name,
                        CacheControlDirectiveNameParameter.OPTIONAL,
                        CacheControlDirectiveExtensionHeaderValueConverter.INSTANCE, // maybe
                        CacheControlDirectiveNameScope.UNKNOWN);
    }

    /**
     * Private ctor use constant or factory.
     */
    private CacheControlDirectiveName(final String name,
                                      final CacheControlDirectiveNameParameter parameter,
                                      final HeaderValueConverter<T> converter,
                                      final CacheControlDirectiveNameScope scope) {
        super();
        this.name = name;
        this.parameter = parameter;
        this.converter = converter;
        this.scope = scope;
    }

    /**
     * Returns true if the this directive is an extension which also requires quoted values.
     */
    public boolean isExtension() {
        return CONSTANTS.containsKey(this.name);
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    @Override
    public Optional<T> checkValue(final Object parameter) {
        Objects.requireNonNull(parameter, "parameter");
        return this.parameter.check(parameter, this);
    }

    private final CacheControlDirectiveNameParameter parameter;

    final HeaderValueConverter<T> converter;

    @Override
    public Optional<T> toValue(final String text) {
        return Optional.of(this.converter.parse(text, this));
    }

    boolean requiresParameter() {
        return this.parameter == CacheControlDirectiveNameParameter.REQUIRED;
    }

    /**
     * Factory that creates a {@link CacheControlDirective} with the parameter which may or may not be empty or present.
     */
    public CacheControlDirective<T> setParameter(final Optional<T> parameter) {
        return CacheControlDirective.with(this,
                this.checkValue(parameter));
    }

    // Comparable...........................................................................................................

    @Override
    public final int compareTo(final CacheControlDirectiveName other) {
        return CASE_SENSITIVITY.comparator().compare(this.value(), other.value());
    }

    boolean isRequest() {
        return this.scope.isRequest();
    }

    boolean isResponse() {
        return this.scope.isResponse();
    }

    private final CacheControlDirectiveNameScope scope;

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return CASE_SENSITIVITY.hash(this.value());
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                other instanceof CacheControlDirectiveName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final CacheControlDirectiveName other) {
        return this.compareTo(other) == 0;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    @Override
    public String toString() {
        return this.value();
    }
}
