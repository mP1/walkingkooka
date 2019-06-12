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
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Holds a cache control directive name.
 */
public final class CacheControlDirectiveName<V> extends HeaderName2<Optional<V>>
        implements Comparable<CacheControlDirectiveName<?>> {

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
                HeaderValueHandler.longHandler(),
                scope);
    }

    /**
     * Registers a directive name including some meta data about its parameters.
     */
    private static <V> CacheControlDirectiveName<V> register(final String name,
                                                             final CacheControlDirectiveNameParameter required,
                                                             final HeaderValueHandler<V> handler,
                                                             final CacheControlDirectiveNameScope scope) {
        final CacheControlDirectiveName<V> constant = new CacheControlDirectiveName<V>(name, required, handler, scope);
        CONSTANTS.put(name, constant);
        return constant;
    }

    /**
     * Holds all constants.
     */
    private final static Map<String, CacheControlDirectiveName<?>> CONSTANTS = Maps.sorted(CacheControlDirectiveName.CASE_SENSITIVITY.comparator());

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
            HeaderValueHandler.longHandler(),
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

    private static final CharPredicate INITIAL_CHAR_PREDICATE = CharPredicates.builder()
            .range('A', 'Z')
            .range('a', 'z')
            .range('0', '9')
            .build()
            .setToString("Charset initial");
    private static final CharPredicate PART_CHAR_PREDICATE = CharPredicates.rfc2045Token();

    // factory ..........................................................................................................

    /**
     * Returns a {@link CacheControlDirectiveName}
     */
    public static CacheControlDirectiveName<?> with(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(name,
                "value",
                INITIAL_CHAR_PREDICATE,
                PART_CHAR_PREDICATE);

        final CacheControlDirectiveName<?> directiveName = CONSTANTS.get(name);
        return null != directiveName ?
                directiveName :
                new CacheControlDirectiveName<>(name,
                        CacheControlDirectiveNameParameter.OPTIONAL,
                        CacheControlDirectiveExtensionHeaderValueHandler.INSTANCE, // maybe
                        CacheControlDirectiveNameScope.UNKNOWN);
    }

    /**
     * Private ctor use constant or factory.
     */
    private CacheControlDirectiveName(final String name,
                                      final CacheControlDirectiveNameParameter parameter,
                                      final HeaderValueHandler<V> handler,
                                      final CacheControlDirectiveNameScope scope) {
        super(name);
        this.parameter = parameter;
        this.handler = handler;
        this.scope = scope;
    }

    /**
     * Returns true if the this directive is an extension which also requires quoted values.
     */
    public boolean isExtension() {
        return CONSTANTS.containsKey(this.value());
    }

    @Override
    public Optional<V> checkValue(final Object parameter) {
        Objects.requireNonNull(parameter, "parameter");
        return this.parameter.check(parameter, this);
    }

    private final CacheControlDirectiveNameParameter parameter;

    final HeaderValueHandler<V> handler;

    @Override
    public Optional<V> toValue(final String text) {
        return Optional.of(this.handler.parse(text, this));
    }

    boolean requiresParameter() {
        return this.parameter == CacheControlDirectiveNameParameter.REQUIRED;
    }

    /**
     * Factory that creates a {@link CacheControlDirective} with the parameter which may or may not be empty or present.
     */
    public CacheControlDirective<V> setParameter(final Optional<V> parameter) {
        return CacheControlDirective.with(this,
                this.checkValue(parameter));
    }

    // HeaderValue...........................................................................................................

    boolean isRequest() {
        return this.scope.isRequest();
    }

    boolean isResponse() {
        return this.scope.isResponse();
    }

    private final CacheControlDirectiveNameScope scope;

    // HeaderName2......................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof CacheControlDirectiveName;
    }

    // Comparable.......................................................................................................

    @Override
    public int compareTo(CacheControlDirectiveName<?> other) {
        return this.compareTo0(other);
    }
}
