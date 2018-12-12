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
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

/**
 * A {@link HeaderValueConverter} that if necessary adds quotes and escape/encodes such characters.
 * It assumes any {@link String} to be parsed have had any double quotes removed, and adds them
 * when necessary.
 * <br>
 * <a href="https://mimesniff.spec.whatwg.org/#parsing-a-mime-type">mime type</a>
 * <a href="https://fetch.spec.whatwg.org/#collect-an-http-quoted-string">Quoted string</a>
 */
final class MediaTypeParameterHeaderValueConverter extends HeaderValueConverter2<String> {

    /**
     * Singleton
     */
    final static MediaTypeParameterHeaderValueConverter INSTANCE = new MediaTypeParameterHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private MediaTypeParameterHeaderValueConverter() {
        super();
    }

    @Override
    String parse0(final String text, final Name name) {
        final HeaderValueConverter<String> converter = text.charAt(0) == DOUBLE_QUOTE ?
                QUOTED :
                UNQUOTED;
        return converter.parse(text, name);
    }

    private final static char DOUBLE_QUOTE = '"';

    private final static CharPredicate RFC2045 = CharPredicates.rfc2045Token();
    private final static CharPredicate SPECIAL = CharPredicates.rfc2045TokenSpecial();
    private final static CharPredicate RFC2045_SPECIAL = RFC2045.or(SPECIAL);

    private final static HeaderValueConverter<String> UNQUOTED = HeaderValueConverters.string(RFC2045);
    private final static HeaderValueConverter<String> QUOTED = HeaderValueConverters.string(RFC2045_SPECIAL,
            StringHeaderValueConverterFeature.DOUBLE_QUOTES,
            StringHeaderValueConverterFeature.BACKSLASH_ESCAPING);

    @Override
    void check0(final Object value) {
        this.checkType(value, String.class);
    }

    @Override
    String toText0(final String value, final Name name) {
        boolean requiresQuotes = false;

        final StringBuilder quoted = new StringBuilder();
        quoted.append(DOUBLE_QUOTE);

        for(char c : value.toCharArray()) {
            requiresQuotes = requiresQuotes || !RFC2045.test(c);

            if(BACKSLASH == c || DOUBLE_QUOTE == c){
                quoted.append(BACKSLASH);
                requiresQuotes = true;
            }
            quoted.append(c);
        }
        quoted.append(DOUBLE_QUOTE);

        return requiresQuotes?
                quoted.toString():
                value;
    }

    private final static char BACKSLASH = '\\';

    @Override
    public String toString() {
        return MediaType.class.getSimpleName() + "Parameter";
    }
}
