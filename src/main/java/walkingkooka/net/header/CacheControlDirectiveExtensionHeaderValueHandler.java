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

import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

/**
 * A handler that accepts both {@link Long} and {@link String} values. This handler is only intended
 * for extension (non standard) directives which could hold either numbers or quoted text.
 */
final class CacheControlDirectiveExtensionHeaderValueHandler extends NonStringHeaderValueHandler<Object> {

    /**
     * Singleton
     */
    final static CacheControlDirectiveExtensionHeaderValueHandler INSTANCE = new CacheControlDirectiveExtensionHeaderValueHandler();

    /**
     * Private ctor use singleton
     */
    private CacheControlDirectiveExtensionHeaderValueHandler() {
        super();
    }

    /**
     * Try parsing as a {@link Long} and then {@link String}
     */
    @Override
    Object parse0(final String text, final Name name) throws HeaderValueException, RuntimeException {
        Object value;

        try {
            value = LONG.parse(text, name);
        } catch (final HeaderValueException cause) {
            value = QUOTED_UNQUOTED_STRING.parse(text, name);
        }

        return value;
    }

    /**
     * Try checking as a {@link Long} and then {@link String}
     */
    @Override
    void check0(final Object value, final Name name) {
        try {
            LONG.check(value, name);
        } catch (final HeaderValueException cause) {
            QUOTED_UNQUOTED_STRING.check(value, name);
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
        return QUOTED_UNQUOTED_STRING.toText(String.class.cast(value), name);
    }

    private String failInvalidValue(final Object value, final Name name) {
        throw new HeaderValueException(name + "  value is not a long or string " + CharSequences.quoteIfChars(value));
    }

    private final static HeaderValueHandler<String> QUOTED_UNQUOTED_STRING = HeaderValueHandler.quotedUnquotedString(
            CharPredicates.asciiPrintable(),
            false,
            CharPredicates.rfc2045Token());

    private final static HeaderValueHandler<Long> LONG = HeaderValueHandler.longHandler();

    @Override
    public String toString() {
        return CacheControlDirective.class.getSimpleName() + "Extension";
    }
}
