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

package walkingkooka.net.http;

import walkingkooka.naming.Name;
import walkingkooka.net.header.HeaderValueConverter;
import walkingkooka.net.header.HeaderValueConverters;
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.net.header.StringHeaderValueConverterFeature;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

/**
 * A converter that accepts both {@link Long} and {@link String} values. This converter is only intended
 * for extension (non standard) directives which could hold either numbers or quoted text.
 */
final class CacheControlDirectiveExtensionHeaderValueConverter extends HttpHeaderValueConverter<Object> {

    /**
     * Singleton
     */
    final static CacheControlDirectiveExtensionHeaderValueConverter INSTANCE = new CacheControlDirectiveExtensionHeaderValueConverter();

    /**
     * Private ctor use singleton
     */
    private CacheControlDirectiveExtensionHeaderValueConverter() {
        super();
    }

    /**
     * Try parsing as a {@link Long} and then {@link String}
     */
    @Override
    Object parse0(final String text, final Name name) throws HeaderValueException, RuntimeException {
        Object value;

        if(text.charAt(0) == DOUBLE_QUOTE) {
            value = QUOTED.parse(text, name);
        } else {
            try {
                value = LONG.parse(text, name);
            } catch (final HeaderValueException cause) {
                value = UNQUOTED.parse(text, name);
            }
        }

        return value;
    }

    private final static char DOUBLE_QUOTE = '"';

    /**
     * Try checking as a {@link Long} and then {@link String}
     */
    @Override
    void check0(final Object value) {
        try {
            LONG.check(value);
        } catch (final HeaderValueException cause) {
            try {
                QUOTED.check(value);
            } catch (final HeaderValueException cause2) {
                UNQUOTED.check(value);
            }
        }
    }

    @Override
    String toText0(final Object value, final Name name) {
        return value instanceof Long ?
                this.toTextLong(Long.class.cast(value), name) :
                value instanceof String ?
                        this.toTextString(String.class.cast(value), name) :
                    this.failInvalidValue(value, name);
    }

    private String toTextLong(final Long value, final Name name) {
        return LONG.toText(Long.class.cast(value), name);
    }

    private String toTextString(final String value, final Name name) {
        return QUOTED.toText(String.class.cast(value), name);
    }

    private String failInvalidValue(final Object value, final Name name) {
        throw new HeaderValueException(name + "  value is not a long or string " + CharSequences.quoteIfChars(value));
    }

    private final static HeaderValueConverter<String> UNQUOTED = HeaderValueConverters.string(CharPredicates.rfc2045Token());
    private final static HeaderValueConverter<String> QUOTED = HeaderValueConverters.string(CharPredicates.rfc2045Token(),
            StringHeaderValueConverterFeature.DOUBLE_QUOTES);
    private final static HeaderValueConverter<Long> LONG = HeaderValueConverters.longConverter();

    @Override
    public String toString() {
        return CacheControlDirective.class.getSimpleName() + "Extension";
    }
}
