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

/**
 * A {@link HeaderValueHandler} that handles parsing a quoted boundary string into its raw form.<br>
 * <pre>
 * Accept-Language: <language>
 * Accept-Language: *
 *
 * // Multiple types, weighted with the quality value syntax:
 * Accept-Language: fr-CH, fr;q=0.9, en;q=0.8, de;q=0.7, *;q=0.5
 * </pre>
 */
final class AcceptLanguageHeaderValueHandler extends NonStringHeaderValueHandler<AcceptLanguage> {

    /**
     * Singleton
     */
    final static AcceptLanguageHeaderValueHandler INSTANCE = new AcceptLanguageHeaderValueHandler();

    /**
     * Private ctor use singleton.
     */
    private AcceptLanguageHeaderValueHandler() {
        super();
    }

    @Override
    AcceptLanguage parse0(final String text, final Name name) {
        return AcceptLanguageHeaderValueParser.parseAcceptLanguage(text);
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkType(value, AcceptLanguage.class, name);
    }

    @Override
    String toText0(final AcceptLanguage acceptEncoding, final Name name) {
        return acceptEncoding.toHeaderText();
    }

    @Override
    public String toString() {
        return AcceptLanguage.class.getSimpleName();
    }
}
